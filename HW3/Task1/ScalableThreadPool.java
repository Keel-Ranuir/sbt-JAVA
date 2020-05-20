import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

public class ScalableThreadPool implements ThreadPool {
    private final Queue<Runnable> tasks = new ArrayDeque<>();
    private final ArrayList<Thread> threads = new ArrayList<Thread>();
    private final int minThreadsNum;
    private final int maxThreadsNum;


    public ScalableThreadPool(int minThreadsNum, int maxThreadsNum) {
        this.minThreadsNum = minThreadsNum;
        this.maxThreadsNum = maxThreadsNum;
        for (int i = 0; i < minThreadsNum; ++i) {
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
            if (!tasks.isEmpty() && threads.size() < maxThreadsNum) {
                Thread thread = new Thread(this::run);
                threads.add(thread);
                thread.start();
            } else {
                notify();
            }
        }
    }

    private void run() {
        while (true) {
            Runnable task;
            synchronized (tasks) {
                if(tasks.isEmpty() && threads.size() > minThreadsNum) {
                    threads.remove(Thread.currentThread());
                    return;
                }
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
