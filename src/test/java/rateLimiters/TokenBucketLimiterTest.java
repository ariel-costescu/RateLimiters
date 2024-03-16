package rateLimiters;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class TokenBucketLimiterTest {

    public static final Duration ONE_SECOND = Duration.ofSeconds(1);
    private RateLimiter tokenBucketLimiter;

    @Test
    void whenWithinLimitThenAcceptAllMessages() {
        final int limit = 10;
        final TimeUnit timeUnit = TimeUnit.MINUTES;
        tokenBucketLimiter = new TokenBucketLimiter(limit, timeUnit);
        final boolean allAccepted = tryNMessagesWithPause(limit, ONE_SECOND);
        assertTrue(allAccepted);
    }

    @Test
    void whenOverLimitThenNotAllMessagesAccepted() {
        final int limit = 10;
        final TimeUnit timeUnit = TimeUnit.MINUTES;
        tokenBucketLimiter = new TokenBucketLimiter(limit, timeUnit);
        final int overLimitBy = 5;
        final int nMessages = limit + overLimitBy;
        final boolean allAccepted = tryNMessagesWithPause(nMessages, ONE_SECOND);
        assertFalse(allAccepted);
    }

    @Test
    void whenBucketIsRefilledThenNewMessageAccepted() throws InterruptedException {
        final int limit = 5;
        final TimeUnit timeUnit = TimeUnit.SECONDS;
        tokenBucketLimiter = new TokenBucketLimiter(limit, timeUnit);
        final boolean allAcceptedAfterOverLimit = tryNMessagesWithPause(limit + 1, null);
        assertFalse(allAcceptedAfterOverLimit);
        Thread.sleep(Duration.ofSeconds(1));
        final boolean allAcceptedAfterBucketRefilled = tryNMessagesWithPause(1, null);
        assertTrue(allAcceptedAfterBucketRefilled);
    }

    private boolean tryNMessagesWithPause(int nMessages, Duration duration) {
        List<String> messages = IntStream.range(0, nMessages)
                .mapToObj(i -> UUID.randomUUID().toString())
                .toList();
        return messages.stream().
                map(message -> {
                    if (duration != null) {
                        try {
                            Thread.sleep(duration);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    return tokenBucketLimiter.tryLimit(message);
                })
                .reduce((b1, b2) -> b1 && b2).orElse(false);
    }
}