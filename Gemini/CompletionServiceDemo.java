package Gemini;

import java.util.concurrent.*;

// FactorialCalculator class is the same as in Module 5...
class FactorialCalculator implements Callable<Long> {
    private final int number;

    public FactorialCalculator(int number) {
        this.number = number;
    }

    @Override
    public Long call() throws Exception {
        // Simulate tasks of varying lengths. A larger number takes longer.
        long sleepTime = number * 20L;
        Thread.sleep(sleepTime);
        
        long result = 1;
        for (int i = 2; i <= number; i++) {
            result *= i;
        }
        System.out.println("-> Finished: Factorial for " + number + " in thread: " + Thread.currentThread().getName());
        return result;
    }
}

public class CompletionServiceDemo {
    public static void main(String[] args) {
        // 1. Create the underlying thread pool
        ExecutorService threadPool = Executors.newFixedThreadPool(3);
        
        // 2. Wrap the pool in a CompletionService
        ExecutorCompletionService<Long> completionService = new ExecutorCompletionService<>(threadPool);

        // Numbers to process. Note the order: a long task (20) is first.
        int[] numbers = {25, 26, 15, 5, 18, 7};

        // 3. Submit all tasks to the CompletionService
        for (int number : numbers) {
            completionService.submit(new FactorialCalculator(number));
        }

        System.out.println("All tasks submitted. Processing results as they complete...");

        // 4. Loop to retrieve results. We loop 'numbers.length' times.
        for (int i = 0; i < numbers.length; i++) {
            try {
                // take() BLOCKS until the NEXT future is complete.
                // It returns the Future of the task that just finished.
                Future<Long> completedFuture = completionService.take();
                
                // .get() here will return immediately because the task is already done.
                Long result = completedFuture.get();
                System.out.println("<- RESULT RECEIVED: " + result);

            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        // Remember to shut down the original thread pool
        System.out.println("All results received. Shutting down the executor.");
        threadPool.shutdown();
    }
}

// explanation

/*
 * this is a form of true asynchronous execution in Java, but with an important nuance: it’s asynchronous at the level of task execution, not necessarily at the level of non-blocking I/O.

Let me break it down for you step by step so you know exactly why this is async, and where it differs from other async models.

1. Why this is asynchronous

You’re using a thread pool (ExecutorService) to execute tasks concurrently.

All your factorial calculations start running in parallel (up to 3 at a time because of newFixedThreadPool(3)).

You’re not waiting for them in the order they were submitted.
Instead, ExecutorCompletionService lets you process results as soon as any task finishes, which is the async behavior.

While one thread is computing factorial(25) (long), another might finish factorial(5) quickly and return early.

2. How it works under the hood

completionService.submit(...) hands off each Callable to the executor immediately.

The executor assigns them to threads and starts running them right away.

Each task, when finished, is queued inside ExecutorCompletionService’s internal completion queue.

completionService.take() waits for the next finished task, not the next in submission order — that’s why you get results as they complete.

3. Nuance: Async vs Non-blocking

This is true asynchronous execution in terms of parallelism because:

You’re not computing tasks one-by-one sequentially.

You don’t block waiting for task start; you only block when you explicitly wait for results.

But this is not:

Non-blocking programming (like CompletableFuture chaining or reactive streams), where the code never blocks a thread while waiting.

Single-threaded async (like JavaScript’s event loop).

Here, completionService.take() blocks until a result is available — meaning a thread is parked while waiting. That’s fine for CPU-bound work like factorial calculations, but in highly scalable I/O scenarios, blocking can be a bottleneck.

4. In summary

✅ Yes, this is async — tasks start and run concurrently, results are handled as they complete.
⚠️ But it’s blocking async — your main loop still blocks waiting for results, instead of reacting via callbacks or futures.

If you wanted to make it non-blocking async, you’d use:

CompletableFuture.supplyAsync(...).thenAccept(...)

Or a reactive library like Project Reactor (Mono, Flux).
 * 
 * 
 * 
 */