import java.util.concurrent.Callable;

public class Task<T> {
    private final Callable<? extends T> callable;
    private volatile T result = null;
    private volatile RuntimeException exception = null;
    private volatile boolean called = false;

    public Task(Callable<? extends T> callable) {
        this.callable = callable;
    }

    public T get() throws Exception {
        if (exception != null) {
            throw exception;
        }

        if (result != null) {
            return result;
        }

        if (called) {
            synchronized (this) {
                while (result == null && exception == null) {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (result != null) {
                return result;
            } else {
                throw exception;
            }
        } else {
            called = true;
            try {
                result = callable.call();
                synchronized (this) {
                    this.notifyAll();
                }
                return result;
            } catch (RuntimeException e) {
                exception = e;
                synchronized (this) {
                    this.notifyAll();
                }
                throw exception;
            }
        }
    }
}
