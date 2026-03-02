package io.github.krmdemo.parallel;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.junit.jupiter.api.Test;

import java.text.DecimalFormat;
import java.time.Duration;
import java.util.List;
import java.util.stream.LongStream;
import java.util.stream.Stream;

@Slf4j
public class ParallelProcessingTest {

    private final List<Duration> stepsList = LongStream.of(
        50, 100, 150, 60, 120, 90, 170
        ).mapToObj(Duration::ofMillis).toList();

    @Test
    void testProcess_Serial() throws Exception {
        log.info("start sequential processing of {} steps", stepsList.size());
        long nanosStart = System.nanoTime();
        stepsList.forEach(stepDuration -> {
            log.debug("(SERIAL) starting the step of duration {}ms;", stepDuration.toMillis());
            pause(stepDuration);
            log.debug("(SERIAL) finish the step of duration {}ms;", stepDuration.toMillis());
        });
        long nanosFinish = System.nanoTime();
        log.info("- total duration of sequential processing is {}",
            formatNanosDuration(nanosStart, nanosFinish));
    }

    @Test
    void testProcess_Parallel() throws Exception {
        log.info("start parallel processing of {} steps", stepsList.size());
        long nanosStart = System.nanoTime();
        stepsList.parallelStream().forEach(stepDuration -> {
            log.debug("(PARALLEL) starting the step of duration {}ms;", stepDuration.toMillis());
            pause(stepDuration);
            log.debug("(PARALLEL) finish the step of duration {}ms;", stepDuration.toMillis());
        });
        long nanosFinish = System.nanoTime();
        log.info("- total duration of parallel processing is {}",
            formatNanosDuration(nanosStart, nanosFinish));
    }

    // Define the pattern: "#,##0.00" means:
    // #    - a digit, zero shows up as absent
    // ,    - the grouping (thousands) separator
    // 0    - a digit, zero shows up as zero
    // .    - the decimal separator
    // 00   - fixed two digits after the decimal point
    private final static DecimalFormat FMT_NANOS = new DecimalFormat("#,##0.00");
    private static String formatNanosDuration(long nanosStart, long nanosFinish) {
        return FMT_NANOS.format((nanosFinish - nanosStart) / 1_000_000);
    }

    private void pause(Duration duration) {
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
}
