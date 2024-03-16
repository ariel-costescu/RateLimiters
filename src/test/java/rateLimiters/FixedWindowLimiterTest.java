package rateLimiters;

import java.util.concurrent.TimeUnit;

public class FixedWindowLimiterTest extends RateLimiterTest {
    @Override
    public void setRateLimiter(Integer limit, TimeUnit timeUnit) {
        rateLimiter = new FixedWindowLimiter(limit, timeUnit);
    }
}
