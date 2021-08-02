package rw.kanis.shop.domain.executor;

public interface MainThread {

    /**
     * Make runnable operation run rw the main thread.
     *
     * @param runnable The runnable to run.
     */
    void post(final Runnable runnable);
}