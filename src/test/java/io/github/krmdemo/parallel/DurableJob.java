package io.github.krmdemo.parallel;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.time.Duration;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

@Slf4j
public class DurableJob implements Runnable{
    private final boolean parallel;
    private final List<Step> stepsList = new LinkedList<>();
    private Deque<Step> stepsExecuted = null;

    private long jobStartNanos = -1;
    private long jobFinishNanos = -1;

    public class Step implements Runnable {
        private final int seqNum;
        private final Duration duration;

        AtomicReference<String> threadName = new AtomicReference<>();
        private long stepStartNanos = -1;
        private long stepFinishNanos = -1;

        Step(final Duration duration) {
            this.seqNum = stepsList.size() + 1;
            this.duration = duration;
        }

        Step(final long durationMillis) {
            this(Duration.ofMillis(durationMillis));
        }

        public Duration expectedDuration() {
            return duration;
        }

        public Duration actualDuration() {
            if (stepFinishNanos < 0 || stepStartNanos < 0) {
                return Duration.ZERO;
            } else {
                return Duration.ofNanos(stepFinishNanos - stepStartNanos);
            }
        }

        private void logMsg(String msg) {
//            if (log.isDebugEnabled()) {
                String prefix = parallel ? "(PARALLEL) " : "(SEQUENTIAL) ";
                log.debug(prefix + msg);
  //          }
        }

        @Override
        public void run() {
            threadName.set(Thread.currentThread().getName());
            this.stepStartNanos = System.nanoTime();
            logMsg(String.format("starting the step of duration %3dms", duration.toMillis()));
            pause(duration);
            logMsg(String.format("- finish the step of duration %3dms", duration.toMillis()));
            this.stepFinishNanos = System.nanoTime();
        }

        Step selfRun() {
            this.run();
            return this;
        }

        String dump() {
            String threadNameStr = threadName.get();
            if (threadNameStr == null) {
                return String.format("%3d) step for %3dms; not executed",
                    seqNum, duration.toMillis());
            } else {
                return String.format("%3d) step for %3dms; executed by '%s' during %3dms | %s",
                    seqNum, duration.toMillis(),
                    threadNameStr, actualDuration().toMillis(), timingBar(false));
            }
        }

        String timingBar(boolean actual) {
            long wasteTime = Duration.ofNanos(stepStartNanos - jobStartNanos).toMillis();
            int wasteLen = Math.toIntExact(wasteTime / SCALE_MILLIS);
            String wasteBar = StringUtils.repeat("-", wasteLen);
            Duration displayDuration = actual ? actualDuration() : duration;
            return wasteBar + nameBarOf(displayDuration.toMillis());
        }

        @Override
        public String toString() {
            return this.dump();
        }
    }

    public DurableJob(boolean parallel, long... durationMillisArr) {
        this.parallel = parallel;
        for (long durationMillis : durationMillisArr) {
            stepsList.add(new Step(durationMillis));
        }
    }

    List<Long> stepsMillisList() {
        return stepsList.stream()
            .map(Step::expectedDuration)
            .map(Duration::toMillis)
            .toList();
    }

    Duration jobDuration() {
        return Duration.ofNanos(jobFinishNanos - jobStartNanos);
    }

    @Override
    public void run() {
        jobStartNanos = System.nanoTime();
        Stream<Step> stepStream = parallel ? stepsList.parallelStream() : stepsList.stream();
        this.stepsExecuted = new ConcurrentLinkedDeque<Step>();
        stepStream.map(Step::selfRun).forEach(stepsExecuted::add);
        jobFinishNanos = System.nanoTime();
    }

    String dump() {
        StringBuilder sb = new StringBuilder(String.format(
            "%s job for steps %s",
            parallel ? "Parallel" : "Sequential",
            stepsMillisList()
        ));
        if (jobFinishNanos > 0 && jobStartNanos > 0) {
            sb.append(" was executed in ");
            sb.append(jobDuration().toMillis());
            sb.append("ms:");
        }
        if (stepsExecuted == null) {
            for (Step step : stepsList) {
                sb.append(System.lineSeparator());
                sb.append(step.dump());
            }
        } else {
            for (Step step : stepsExecuted) {
                sb.append(System.lineSeparator());
                sb.append(step.dump());
            }
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return this.dump();
    }

    private static void pause(Duration duration) {
        long startSleepingAt = System.currentTimeMillis();
        try {
            Thread.sleep(duration.toMillis());
        } catch (InterruptedException e) {
            long interruptSleepingAt = System.currentTimeMillis();
            Thread.currentThread().interrupt();
            throw new IllegalStateException(String.format(
                "--- the thread '%s' was interrupted since %dms after start sleeping ---",
                Thread.currentThread().getName(),
                interruptSleepingAt - startSleepingAt
            ));
        }
    }

    private final static int SCALE_MILLIS = 10;

    private static String barTitleOf(long millis) {
        if (millis < 40) {
            return String.format("%02d", millis);
        } else {
            return String.format("%dms", millis);
        }
    }

    private static String nameBarOf(long millis) {
        String title = barTitleOf(millis);
        int extraLen = Math.toIntExact(millis / SCALE_MILLIS) - title.length();
        if (extraLen <= 0) {
            return title;
        } else {
            int halfLen = extraLen / 2;
            String prefix = StringUtils.repeat("<", halfLen);
            String suffix = StringUtils.repeat(">", extraLen - halfLen);
            return prefix + title + suffix;
        }
    }
}
