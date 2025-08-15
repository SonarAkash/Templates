package Gemini;

import java.util.LinkedList;
import java.util.Queue;

// This is the shared resource.
class ServingCounter {
    private final Queue<String> dishes = new LinkedList<>();
    private final int MAX_CAPACITY = 3; // The counter can only hold 3 dishes

    // The Chef will call this method
    public synchronized void placeDish(String dish) throws InterruptedException {
        // The 'while' loop is crucial here! We'll explain why below.
        // If the counter is full, the chef must wait.
        while (dishes.size() == MAX_CAPACITY) {
            System.out.println("COUNTER FULL! Chef is waiting...");
            wait(); // Releases the lock on 'this' and waits.
        }

        dishes.add(dish);
        System.out.println("Chef placed: " + dish + " (Counter size: " + dishes.size() + ")");
        
        // The chef has added a dish, so they notify the waiter that there's something to take.
        notify(); 
    }

    // The Waiter will call this method
    public synchronized String serveDish() throws InterruptedException {
        // If the counter is empty, the waiter must wait.
        while (dishes.isEmpty()) {
            System.out.println("COUNTER EMPTY! Waiter is waiting...");
            wait(); // Releases the lock and waits.
        }

        String dish = dishes.poll(); // poll() removes and returns the head of the queue
        System.out.println("Waiter served: " + dish + " (Counter size: " + dishes.size() + ")");
        
        // The waiter has removed a dish, making space, so they notify the chef.
        notify();
        return dish;
    }
}

public class ProducerConsumerDemo {
    public static void main(String[] args) {
        ServingCounter counter = new ServingCounter();

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

