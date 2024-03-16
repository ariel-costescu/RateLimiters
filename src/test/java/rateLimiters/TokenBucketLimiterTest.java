package rateLimiters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

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
        final int nMessages = LIMIT;
        Boolean allAccepted = tryNMessages(nMessages);
        assertTrue(allAccepted);
    }

    @Test
    void whenOverLimitThenNotAllMessagesAccepted() {
        final int overLimitBy = 5;
        final int nMessages = LIMIT + overLimitBy;
        Boolean allAccepted = tryNMessages(nMessages);
        assertFalse(allAccepted);
    }

    private Boolean tryNMessages(int nMessages) {
        List<String> messages = IntStream.range(0, nMessages)
                .mapToObj(i -> UUID.randomUUID().toString())
                .toList();
        return messages.stream().
                map(message -> {
                    try {
                        Thread.sleep(Duration.ofSeconds(1));
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    return tokenBucketLimiter.tryLimit(message);
                })
                .reduce((b1, b2) -> b1 && b2).orElse(false);
    }
}