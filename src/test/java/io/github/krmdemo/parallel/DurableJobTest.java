package io.github.krmdemo.parallel;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class DurableJobTest {

    @Test
    public void testParallel() {
        DurableJob job = new DurableJob(true, 50, 100, 150, 60, 120, 90, 170, 110, 80, 130);
        System.out.println(job.dump());
        System.out.println("-------------------");
        job.run();
        System.out.println("-------------------");
        System.out.println(job.dump());
        log.debug("this is debug");
        log.info("this is info");
    }

    @Test
    public void testSequential() {
        DurableJob job = new DurableJob(false, 50, 100, 150, 60, 120, 90, 170, 110, 80, 130);
        System.out.println(job.dump());
        System.out.println("===================");
        job.run();
        System.out.println("===================");
        System.out.println(job.dump());
        log.debug("this is debug");
        log.info("this is info");
    }
}
