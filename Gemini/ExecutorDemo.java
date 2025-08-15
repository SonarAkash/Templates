package Gemini;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

// The Callable task from before
class FactorialCalculator implements Callable<Long> {
    private final int number;

    public FactorialCalculator(int number) {
        this.number = number;
    }

    @Override
    public Long call() throws Exception {
        if (number < 0) {
            throw new IllegalArgumentException("Number must be non-negative.");
        }
        long result = 1;
        // Simulate a long-running task
        Thread.sleep(100); 
        for (int i = 2; i <= number; i++) {
            result *= i;
        }
        System.out.println("Computed factorial for " + number + " in thread: " + Thread.currentThread().getName());
        return result;
    }
}

public class ExecutorDemo {
    public static void main(String[] args) {
        // Step 1: Create a thread pool. We have a 2-core CPU, so a pool of 2 threads is efficient.
        ExecutorService executor = Executors.newFixedThreadPool(2);

        // A list to hold the Future objects
        List<Future<Long>> futureResults = new ArrayList<>();

        // Numbers for which we want to calculate factorials
        int[] numbers = {15, 20, 10, 5, 18, 7};

        // Step 2 & 3: Create and submit tasks for each number
        for (int number : numbers) {
            FactorialCalculator task = new FactorialCalculator(number);
            Future<Long> future = executor.submit(task);
            futureResults.add(future);
        }

        System.out.println("All tasks have been submitted. Main thread is now waiting for results...");

        // Step 4: Retrieve and print the results
        for (Future<Long> future : futureResults) {
            try {
                // future.get() blocks until the result is ready
                Long result = future.get();
                System.out.println("Result received: " + result);
            } catch (InterruptedException | ExecutionException e) {
                // ExecutionException wraps the original exception from the call() method
                e.printStackTrace();
            }
        }

        // Step 5: Shut down the executor
        System.out.println("All results received. Shutting down the executor.");
        executor.shutdown();

        try {
            // Wait for 1 minute for the tasks to complete
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                System.err.println("Tasks did not complete in time. Forcing shutdown.");
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }

        System.out.println("Executor has been shut down.");
    }
}


// EXPLANATION

/*
 * 
 *  I HAVE A DOUBT REGARDING THE OUTPUT SEQUENCE, IT IS SAID THAT THE FUTURE.GET() IS BLOCKING (MEANING I WON'T GO FURTHER UNTILL RESULT IS RECEIVED THEN SHOULDN'T THE RESULT OF EACH NUMBER PRESENT IN THE ARRAY BE PRINTED IN SEQUENCE, SINCE WE ARE ITERATING ON THE LIST and list contains future objects and it was added in the sequence ?

ChatGPT said:

I get your confusion — the key point is:

future.get() does block,
but it blocks only until that specific task is finished,
and tasks may finish in a different order than they were submitted because they run concurrently.

🔍 Why your results look out of order

Let’s trace your code:

for (int number : numbers) {
    Future<Long> future = executor.submit(task);
    futureResults.add(future);  // Futures are in submission order
}


At this point:

futureResults has futures in the same order as your numbers array.

But these futures represent tasks running in parallel in the thread pool.

Then you do:

for (Future<Long> future : futureResults) {
    Long result = future.get();
    System.out.println("Result received: " + result);
}


Here’s what happens:

For the first future (number 15):

If it’s already done → get() returns immediately.

If not done → main thread waits until the factorial for 15 is computed.

Meanwhile, the thread pool is free to run other tasks, so maybe number 20 finishes before 15 finishes. That’s why "Computed factorial for 20..." prints before "Result received" for 15.

After 15’s result is printed, you move to the second future (number 20):

If it’s already done → instant return.

If not → block until done (but here it was already done earlier).

And so on…

📌 Why "Computed factorial..." messages look shuffled

The "Computed factorial..." lines are printed inside the worker threads in call():

System.out.println("Computed factorial for " + number + " in thread: " + Thread.currentThread().getName());


These print as soon as each task finishes, in whatever order tasks complete — that has nothing to do with the order of your future.get() loop.

📌 Why "Result received..." stays in order of submission

Notice:

All "Result received: ..." lines are printed in array order.

That’s because you iterate futureResults sequentially, and get() blocks until that particular future finishes.

The only mixing you see is between the "computed" logs (from workers) and the "result received" logs (from main thread), because they’re running at the same time.

TL;DR

future.get() preserves the order of the loop you write, because you block until that future is done before moving on.

The completion messages from the worker threads happen in whatever order tasks finish — which can be different from submission order.
 * 
 * 
 */

