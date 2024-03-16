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

    public void acceptMessage(String message) {
        System.out.printf("Accepted message: '%s'\n", message);
    }

    public void dropMessage(String message) {
        System.out.printf("Dropped message: '%s'\n", message);
    }

}
