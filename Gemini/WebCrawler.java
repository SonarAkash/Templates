package Gemini;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Represents the task of crawling a single URL.
 * Each instance of this class will be executed by a thread in our pool.
 */
class CrawlerTask implements Runnable {
    // A static, shared collection of URLs that have already been visited.
    // We use a Set for fast lookups (O(1) average time complexity).
    // This is the CRITICAL SHARED RESOURCE that requires synchronization.
    public static final Set<String> visitedUrls = new HashSet<>();

    private final String url;
    private final ExecutorService executor;
    private final int maxPages;

    public CrawlerTask(String url, ExecutorService executor, int maxPages) {
        this.url = url;
        this.executor = executor;
        this.maxPages = maxPages;
    }

    @Override
    public void run() {
        // --- Synchronization Block Start ---
        // We must ensure that two threads don't check and add the same URL simultaneously.
        synchronized (visitedUrls) {
            // Check if we should stop crawling.
            if (visitedUrls.size() >= maxPages) {
                return; // Stop if we've reached the limit.
            }

            // If the URL is new, add it to the set and proceed.
            if (!visitedUrls.contains(url)) {
                visitedUrls.add(url);
            } else {
                return; // If already visited, this task is done.
            }
        }
        // --- Synchronization Block End ---

        // If we've made it this far, it's a new URL to process.
        System.out.println("Crawling: " + url + " (Visited: " + visitedUrls.size() + "/" + maxPages + ") by " + Thread.currentThread().getName());

        // Simulate fetching and parsing the webpage to find links.
        List<String> foundLinks = MockWebPage.fetchAndParse(url);

        for (String link : foundLinks) {
            // For each found link, submit a new task to the executor.
            // We must check the size again before submitting.
            synchronized (visitedUrls) {
                if (visitedUrls.size() < maxPages) {
                    executor.submit(new CrawlerTask(link, executor, maxPages));
                }
            }
        }
    }
}

/**
 * A mock class to simulate fetching a webpage and finding links.
 * In a real-world scenario, this would involve HTTP requests and HTML parsing.
 */
class MockWebPage {
    public static List<String> fetchAndParse(String url) {
        // Simulate network latency
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Simulate finding links on the page.
        // Each page links to 3 new pages.
        return List.of(
            url + "/page" + (int)(Math.random() * 100),
            url + "/resource" + (int)(Math.random() * 100),
            "http://another-site.com/link" + (int)(Math.random() * 100)
        );
    }
}

/**
 * The main class that orchestrates the web crawling process.
 */
public class WebCrawler {
    public static void main(String[] args) {
        // --- Module 5: The Executor Framework ---
        // We create a fixed-size thread pool. This team of 4 "crawler" threads
        // will pick up and execute CrawlerTasks concurrently.
        int poolSize = 4;
        ExecutorService executor = Executors.newFixedThreadPool(poolSize);

        // The starting point of our crawl.
        String seedUrl = "http://start-here.com";
        int maxPagesToCrawl = 50;

        System.out.println("Starting web crawl from: " + seedUrl);
        System.out.println("Using a thread pool of size: " + poolSize);
        System.out.println("Will stop after crawling " + maxPagesToCrawl + " unique pages.");

        // --- Module 2: Creating Threads with Runnable ---
        // We submit the first task to our executor service.
        executor.submit(new CrawlerTask(seedUrl, executor, maxPagesToCrawl));

        // Now we need to gracefully shut down the executor.
        // We can't just call shutdown() immediately, as new tasks are being submitted by other tasks.
        // A common strategy is to periodically check if the work is done.
        try {
            while (true) {
                // Wait for a bit
                Thread.sleep(2000);
                
                // Check if the number of visited pages has reached our target
                synchronized (CrawlerTask.visitedUrls) {
                    if (CrawlerTask.visitedUrls.size() >= maxPagesToCrawl) {
                        System.out.println("\nTarget page count reached. Shutting down executor...");
                        executor.shutdown();
                        break;
                    }
                }
            }
            
            // Wait for the already submitted tasks to finish.
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                System.err.println("Tasks did not complete in time. Forcing shutdown.");
                executor.shutdownNow();
            }

        } catch (InterruptedException e) {
            executor.shutdownNow();
        }

        System.out.println("\nCrawl finished. Total unique pages visited: " + CrawlerTask.visitedUrls.size());
        // Uncomment the line below to see all the URLs that were visited.
        // System.out.println("Visited URLs: " + CrawlerTask.visitedUrls);
    }
}

