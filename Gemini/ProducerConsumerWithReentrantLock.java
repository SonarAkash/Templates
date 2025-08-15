package Gemini;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class ModernServingCounter {
    private final Queue<String> dishes = new LinkedList<>();
    private final int MAX_CAPACITY = 3;
    
    // The lock and its associated conditions
    private final Lock lock = new ReentrantLock();
    private final Condition counterNotEmpty = lock.newCondition();
    private final Condition counterNotFull = lock.newCondition();

    public void placeDish(String dish) throws InterruptedException {
        lock.lock(); // Acquire the lock
        try {
            // Wait while the counter is full
            while (dishes.size() == MAX_CAPACITY) {
                System.out.println("COUNTER FULL! Chef is waiting...");
                counterNotFull.await(); // Replaces wait()
            }

            dishes.add(dish);
            System.out.println("Chef placed: " + dish + " (Counter size: " + dishes.size() + ")");
            
            // Signal the waiter that the counter is no longer empty
            counterNotEmpty.signal(); // Replaces notify()
        } finally {
            lock.unlock(); // ALWAYS unlock in a finally block!
        }
    }

    public String serveDish() throws InterruptedException {
        lock.lock();
        try {
            // Wait while the counter is empty
            while (dishes.isEmpty()) {
                System.out.println("COUNTER EMPTY! Waiter is waiting...");
                counterNotEmpty.await();
            }

            String dish = dishes.poll();
            System.out.println("Waiter served: " + dish + " (Counter size: " + dishes.size() + ")");
            
            // Signal the chef that the counter is no longer full
            counterNotFull.signal(); // You can also use signalAll()
            return dish;
        } finally {
            lock.unlock();
        }
    }
}

// The Main class would be the same as before, just using ModernServingCounter.
public class ProducerConsumerWithReentrantLock {
    public static void main(String[] args) {
        ModernServingCounter counter = new ModernServingCounter();

        // The Chef (Producer)
        Thread chef = new Thread(() -> {
            try {
                for (int i = 1; i <= 6; i++) {
                    counter.placeDish("Dish #" + i);
                    Thread.sleep(1000); // Chef takes time to cook
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // The Waiter (Consumer)
        Thread waiter = new Thread(() -> {
            try {
                for (int i = 0; i < 6; i++) {
                    counter.serveDish();
                    Thread.sleep(2000); // Waiter takes time to serve
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        chef.start();
        waiter.start();
    }
}


// EXPLAINATION 

/*
 * NOW IN THE SETTING DOES THE AWAIT() METHOD ALSO RELEASES LOCK ON THE OBJECT ? IF YES THEN HOW CAUSE WE RELEASE THE LOCK IN THE LATER PART I.E FINALLY BLOCK ?
ChatGPT said:

ANSWER : =>

Yes — await() does release the lock immediately when it’s called, even though you also have lock.unlock() later in the finally block.

The key is: await() is not the same as returning from the method.


🔍 Step-by-step of what await() does


When you call:

counterNotEmpty.await();

inside a block protected by lock.lock(), here’s what happens internally:

You already hold the lock (lock.lock() was called earlier).

await():

Releases the lock (so other threads can acquire it).

Places the current thread in the wait set for that specific Condition.

Suspends the thread (similar to Object.wait()).

Later, when another thread calls signal() or signalAll() on the same Condition:

The waiting thread is moved from the wait set to the entry queue of the same lock.

The awakened thread must re-acquire the lock before await() returns.

Once it re-acquires the lock, execution continues after the await() call.

Eventually, when the method reaches the finally block, then lock.unlock() is called, releasing the lock for real at method exit.


🔄 How this differs from lock.unlock() in finally

lock.unlock() in finally is for normal method exit (successful or due to exception).

await() temporarily gives up the lock mid-method, allowing other threads to run inside the same lock-protected region.

After await() returns, the lock is already re-acquired, so code after it is still in a protected state.


Example Timeline in Your Code

Two waiters, one chef:

Waiter-1 calls serveDish() → finds empty → calls await() → releases lock.

Waiter-2 can now also enter serveDish(), finds empty, calls await() → releases lock.

Chef calls placeDish() → adds food → calls signal() → wakes up one waiter.

That waiter re-acquires the lock, continues after await(), serves dish.

When method ends, lock.unlock() in finally is called.


💡 So the answer is:
Yes, await() releases the lock immediately when called — it’s built into the mechanism of Condition.await().
The later lock.unlock() in finally is unrelated to the lock release inside await(); it’s for cleaning up when you leave the method entirely.
 * 
 * 
 * 
 */