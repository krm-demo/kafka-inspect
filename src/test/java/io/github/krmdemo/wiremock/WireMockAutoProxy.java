package io.github.krmdemo.wiremock;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.http.ThreadPoolFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.AtomicSafeInitializer;
import org.apache.commons.lang3.concurrent.ConcurrentException;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.util.thread.VirtualThreadPool;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

@Slf4j
public class WireMockAutoProxy {

    private static final AtomicSafeInitializer<WireMockServer> wireMockServerHolder =
        AtomicSafeInitializer.<WireMockServer>builder()
            .setInitializer(WireMockAutoProxy::createAndStart)
            .get();

    private static WireMockServer createAndStart() {
        WireMockServer server = new WireMockServer(autoProxyConfig());
        Runtime.getRuntime().addShutdownHook(new Thread(
            () -> {
                log.info("going to shutdown WireMockServer");
                server.shutdown();
                log.info("WireMockServer is stopped and shut down");
            }, "wiremock-shutdown-thread"));
        server.start();
        return server;
    }

    public static WireMockServer wireMockServer() {
        try {
            return wireMockServerHolder.get();
        } catch (ConcurrentException concEx) {
            throw new IllegalStateException("could not retrieve the instance of WireMockServer", concEx);
        }
    }

    public static WireMockConfiguration autoProxyConfig() {

        WireMockConfiguration cfg = WireMockConfiguration.wireMockConfig();
        cfg.dynamicPort();
        // Configure the Jetty ThreadPool with the custom factory
        cfg.threadPoolFactory(threadPoolFactoryDaemon(true));
//        cfg.threadPoolFactory(threadPoolFactoryVirtual());
        return cfg;
    }

    @SuppressWarnings("SameParameterValue")
    private static ThreadPoolFactory threadPoolFactoryDaemon(boolean daemon) {
        // Create a custom ThreadFactory that marks threads as daemon
        ThreadFactory daemonThreadFactory = runnable -> {
            Thread thread = new Thread(runnable);
            thread.setDaemon(daemon);
            return thread;
        };
        QueuedThreadPool threadPool = new QueuedThreadPool(
            200,    // <-- maxThreads : int
            10,     // <-- minThreads : int,
            60_000, // <-- idleTimeout in milliseconds : int
            10,     // <-- reserved threads : int
            null,   // <-- default : BlockingQueue
            new ThreadGroup("wiremock-thread-group"),
            daemonThreadFactory
        );
        try {
            threadPool.start();
            threadPool.execute(() -> {
                log.info("inside queued thread - daemon is " + Thread.currentThread().isDaemon());
                log.info("inside queued thread - virtual is " + Thread.currentThread().isVirtual());
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return options -> threadPool;
    }

    // Unfortunately, virtual thread-pool does not allow JVM to stop
    private static ThreadPoolFactory threadPoolFactoryVirtual() {
        VirtualThreadPool threadPool = new VirtualThreadPool();
        threadPool.setVirtualThreadsExecutor(Executors.newVirtualThreadPerTaskExecutor());
        try {
            threadPool.start();
            threadPool.execute(() -> {
                log.info("inside virtual thread - daemon is " + Thread.currentThread().isDaemon());
                log.info("inside virtual thread - virtual is " + Thread.currentThread().isVirtual());
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return options -> threadPool;
    }

    static void main() {
        System.out.println("=== enter the main-method: ===");
        wireMockServer();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("=== shutting down the main-method: ===");
            System.out.println(">>> wireMockServer.running = " + wireMockServer().isRunning());
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("<<< wireMockServer.running = " + wireMockServer().isRunning());
            System.out.println("=== shutting down is finished !!! ===");
        }));

        System.out.println("=== leave the main-method ====");
    }
}
