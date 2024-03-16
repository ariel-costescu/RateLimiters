package rateLimiters;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

public class FixedWindowLimiter extends RateLimiter {

    private int allowance;
    private Instant timeReference;

    public FixedWindowLimiter(Integer limit, TimeUnit timeUnit) {
        super(limit, timeUnit);
        timeReference = Instant.now();
        allowance = limit;
    }

    @Override
    public boolean tryLimit(String message) {
        final Instant now = Instant.now();
        long elapsedTimeUnits = timeUnit.toChronoUnit().between(timeReference, now);
        timeReference = now;
        if (elapsedTimeUnits > 0) {
            allowance = limit - 1;
        } else {
            allowance -= 1;
        }
        if (allowance < 0) {
            dropMessage(message);
            return false;
        } else {
            acceptMessage(message);
            return true;
        }
    }
}
