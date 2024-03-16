package rateLimiters;

import java.util.concurrent.TimeUnit;

public abstract class RateLimiter {
    protected Integer limit;
    protected TimeUnit timeUnit;

    public RateLimiter(Integer limit, TimeUnit timeUnit) {
        this.limit = limit;
        this.timeUnit = timeUnit;
    }

    public abstract boolean tryLimit(String message);

}
