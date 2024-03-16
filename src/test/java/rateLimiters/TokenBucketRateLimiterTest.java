package rateLimiters;

import java.util.concurrent.TimeUnit;

public class TokenBucketRateLimiterTest extends RateLimiterTest {
    @Override
    public void setRateLimiter(Integer limit, TimeUnit timeUnit) {
        rateLimiter = new TokenBucketLimiter(limit, timeUnit);
    }
}
