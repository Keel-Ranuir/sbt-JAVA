import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

public class FixedThreadPool implements ThreadPool {
    private final Queue<Runnable> tasks = new ArrayDeque<>();
    private final ArrayList<Thread> threads = new ArrayList<Thread>();

    public FixedThreadPool(int threadsNum) {
        for (int i = 0; i < threadsNum; ++i) {
            threads.add(new Thread(this::run));
        }
    }

    @Override
    public void start() {
        for (Thread thread : threads) {
            thread.start();
        }
    }

    @Override
    public void execute(Runnable runnable) {
        synchronized (tasks) {
            tasks.add(runnable);
            tasks.notify();
        }
    }

    private void run() {
        while (true) {
            Runnable task;
            synchronized (tasks) {
                while (tasks.isEmpty()) {
                    try {
                        tasks.wait();
                    } catch (InterruptedException ignored) {}
                }
                task = tasks.poll();
            }
            try {
                task.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
