package rateLimiters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

class TokenBucketLimiterTest {

    private static final Integer LIMIT = 10;
    private static final TimeUnit TIME_UNIT = TimeUnit.MINUTES;

    private RateLimiter tokenBucketLimiter;

    @BeforeEach
    void init() {
        tokenBucketLimiter = new TokenBucketLimiter(LIMIT, TIME_UNIT);
    }

    @Test
    void whenWithinLimitThenAcceptAllMessages() {
        List<String> messages = IntStream.range(0, 10)
                .mapToObj(i -> UUID.randomUUID().toString())
                .toList();
        Boolean allAccepted = messages.stream().
                map(message -> {
                    try {
                        Thread.sleep(Duration.ofSeconds(1));
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    return tokenBucketLimiter.tryLimit(message);
                })
                .reduce((b1, b2) -> b1 && b2).orElse(false);
        assertTrue(allAccepted);
    }
}