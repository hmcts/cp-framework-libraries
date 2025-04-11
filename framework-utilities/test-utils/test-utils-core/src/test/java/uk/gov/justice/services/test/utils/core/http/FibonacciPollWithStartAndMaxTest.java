package uk.gov.justice.services.test.utils.core.http;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class FibonacciPollWithStartAndMaxTest {

    @Test
    void shouldPollFibonacciMinMaxWithDefaults() {
        FibonacciPollWithStartAndMax pollIntervalWithMax = new FibonacciPollWithStartAndMax();

        long[] result = LongStream.range(1, 9).map(i -> pollIntervalWithMax.next((int) i, Duration.ZERO).toMillis())
                .toArray();
        assertArrayEquals(new long[]{1, 1, 2, 3, 5, 8, 13, 21}, result);

    }

    @Test
    public void shouldPollFibonacciMinMax() {
        FibonacciPollWithStartAndMax pollIntervalWithMax = new FibonacciPollWithStartAndMax(Duration.ofMillis(10), Duration.ofMillis(100));

        long[] result = LongStream.range(1, 9).map(i -> pollIntervalWithMax.next((int) i, Duration.ZERO).toMillis())
                .toArray();

        assertArrayEquals(new long[]{10, 10, 20, 30, 50, 80, 100, 100}, result);
    }

    @Test
    public void shouldPollFibonacciMaxIsSmallerThanMin() {
        FibonacciPollWithStartAndMax pollIntervalWithMax = new FibonacciPollWithStartAndMax(Duration.ofMillis(100), Duration.ofMillis(10));

        long[] result = LongStream.range(1, 4).map(i -> pollIntervalWithMax.next((int) i, Duration.ZERO).toMillis())
                .toArray();

        assertArrayEquals(new long[]{10, 10, 10}, result);
    }
}