package Gemini;

class SafeCounter {
    private int count = 0;

    // By adding 'synchronized', we ensure that only one thread
    // can execute this method on a given instance of SafeCounter at a time.
    public synchronized void increment() {
        count++;
    }

    public int getCount() {
        return count;
    }
}

public class SynchronizationDemo {
    public static void main(String[] args) throws InterruptedException {
        SafeCounter counter = new SafeCounter();

        Runnable task = () -> {
            for (int i = 0; i < 10000000; i++) {
                counter.increment();
            }
        };

        Thread thread1 = new Thread(task);
        Thread thread2 = new Thread(task);

        long start = System.currentTimeMillis();
        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();
        long end = System.currentTimeMillis();
        System.out.println("with Threads : " + (end - start));
        System.out.println("Final count: " + counter.getCount());


        Thread thread3 = new Thread(task);
        // Thread thread4 = new Thread(task);

        start = System.currentTimeMillis();
        
        thread3.start();
        // thread4.start();

        thread3.join();
        // thread4.join();
        end = System.currentTimeMillis();
        System.out.println("without Threads : " + (end - start));


        // Now, the result will be 20,000 every single time.
        System.out.println("Final count: " + counter.getCount());
    }
}

