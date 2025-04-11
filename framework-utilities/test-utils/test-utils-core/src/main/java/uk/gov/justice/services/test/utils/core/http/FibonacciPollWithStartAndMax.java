package uk.gov.justice.services.test.utils.core.http;

import org.awaitility.pollinterval.FibonacciPollInterval;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * Fibonacci style Poll interval class with startInterval and MaxInterval.
 */
public class FibonacciPollWithStartAndMax extends FibonacciPollInterval {
    public static final Duration DEFAULT_MAX_INTERVAL_1S = Duration.ofSeconds(1);
    public static final Duration DEFAULT_START_INTERVAL = Duration.ofMillis(1);
    private final Duration startInterval;
    private final Duration maxInterval;


    public FibonacciPollWithStartAndMax(Duration startInterval, Duration maxInterval) {
        super(1, TimeUnit.MILLISECONDS);
        this.startInterval = startInterval;
        this.maxInterval = maxInterval;
    }

    public FibonacciPollWithStartAndMax(Duration startInterval) {
        this(startInterval, DEFAULT_MAX_INTERVAL_1S);
    }

    public FibonacciPollWithStartAndMax() {
        this(DEFAULT_START_INTERVAL, DEFAULT_MAX_INTERVAL_1S);
    }

    @Override
    public Duration next(int pollCount, Duration previousDuration) {
        Duration duration = startInterval.multipliedBy(fibonacci(pollCount));
        if (maxInterval.compareTo(duration) < 0) {
            return maxInterval;
        }
        return duration;
    }
}
