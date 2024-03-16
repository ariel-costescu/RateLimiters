package rateLimiters;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

public class TokenBucketLimiter extends RateLimiter {

    private long currentTokens;
    private Instant timeReference;
    public TokenBucketLimiter(Integer limit, TimeUnit timeUnit) {
        super(limit, timeUnit);
        currentTokens = limit;
        timeReference = Instant.now();
    }

    @Override
    public boolean tryLimit(String message) {
        final Instant now = Instant.now();
        long elapsedTimeUnits = timeUnit.toChronoUnit().between(timeReference, now);
        timeReference = now;
        currentTokens = Math.min(limit, currentTokens + limit * elapsedTimeUnits);
        if (currentTokens > 1) {
            acceptMessage(message);
            currentTokens -= 1;
            return true;
        } else {
            dropMessage(message);
            return false;
        }
    }

    public void acceptMessage(String message) {
        System.out.printf("Accepted message: '%s'\n", message);
    }

    public void dropMessage(String message) {
        System.out.printf("Dropped message: '%s'\n", message);
    }
}
