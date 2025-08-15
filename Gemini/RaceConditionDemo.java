package Gemini;

class UnsafeCounter {
    private int count = 0;

    // This method is the "critical section" - the part of the code
    // that accesses the shared resource.
    public void increment() {
        count++;
    }

    public int getCount() {
        return count;
    }
}

public class RaceConditionDemo {
    public static void main(String[] args) throws InterruptedException {
        UnsafeCounter counter = new UnsafeCounter();

        // We create a task that increments the counter 10,000 times
        Runnable task = () -> {
            for (int i = 0; i < 10000; i++) {
                counter.increment();
            }
        };

        // Create two threads to run the same task
        Thread thread1 = new Thread(task);
        Thread thread2 = new Thread(task);

        thread1.start();
        thread2.start();

        // We must wait for the threads to finish before printing the result.
        // The join() method makes the main thread wait until the other thread terminates.
        thread1.join();
        thread2.join();

        // The expected result is 20,000. But what will we get?
        System.out.println("Final count: " + counter.getCount());
    }
}

