package Gemini;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;

public class TrueAsync {
    public static void main(String[] args) {
        // 1. Create the underlying thread pool
        ExecutorService threadPool = Executors.newFixedThreadPool(3);
        
        // 2. Wrap the pool in a CompletionService
        ExecutorCompletionService<Long> completionService = new ExecutorCompletionService<>(threadPool);

        int[] numbers = {20, 10, 15, 5, 18, 7};

        // 3. Submit all tasks to the CompletionService
        for (int number : numbers) {
            completionService.submit(new FactorialCalculator(number));
        }

        // 4. Create and start a DEDICATED thread to process results
        Thread resultProcessorThread = new Thread(() -> {
            System.out.println("Result Processor: Starting to wait for results.");
            for (int i = 0; i < numbers.length; i++) {
                try {
                    Future<Long> completedFuture = completionService.take();
                    Long result = completedFuture.get();
                    System.out.println("<- RESULT RECEIVED by Processor: " + result);
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
            // This thread is the one that knows when all work is done,
            // so IT is responsible for shutting down the pool.
            System.out.println("Result Processor: All tasks complete. Shutting down the pool.");
            threadPool.shutdown();
        });
        
        resultProcessorThread.start(); // Fire and forget!

        // 5. The main thread is now FREE!
        System.out.println("!!! Main thread is now free to do other work and will exit. !!!");
    }
}


// explanation

/*
 * Above is an asynchronous style of programming, and it behaves similarly to CompletableFuture in the sense that:

Tasks run in parallel in a thread pool.

Results are processed as soon as they complete (order of arrival, not submission order) thanks to ExecutorCompletionService.

The main thread does not block — you even showed this with your "Main thread is now free..." message.

Why this is truly async

You submit all tasks at once — they start executing independently in worker threads.

The resultProcessorThread uses completionService.take() which waits only for the next completed task, not for all tasks in submission order.

Because .take() returns the first finished future, a faster task’s result is printed immediately, even if it was submitted later.

Relation to CompletableFuture

CompletableFuture is more declarative — you can attach callbacks like .thenApply(), .thenAccept() without manually looping over take().

CompletionService is more manual — you explicitly wait for completed tasks in a loop.

Both achieve asynchronous, out-of-order processing.

Is your code asynchronous?

Yes —

Main thread is not blocked.

Worker threads execute tasks concurrently.

Results are handled in arrival order, not submission order.

The only blocking is inside the resultProcessorThread (on .take()), but that’s okay — it’s not blocking the main thread, and this is the standard way to consume results as soon as they’re ready.
 * 
 * 
 */