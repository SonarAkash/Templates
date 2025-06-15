import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class ThreadsExample {

    public static void main(String[] args) throws InterruptedException {
        List<Integer> temperatures = List.of(30, 25, 28, 32, 27, 26, 29, 31, 24, 33);
        List<String> newsHeadlines = List.of(
                "Stock market sees a sharp rise today.",
                "Heavy rainfall expected in northern regions.",
                "New AI model breaks previous performance records.",
                "Sports team secures a thrilling last-minute victory.",
                "Government announces tax cuts for small businesses.",
                "Tech company unveils a groundbreaking new smartphone.",
                "Scientists discover a new exoplanet with potential for life.",
                "Local community organizes a charity marathon event.",
                "Fuel prices witness a slight decline this week.",
                "Global summit discusses climate change policies.");
        Random r = new Random();
        Function<Integer[], CompletableFuture<String>> fetchData = (info) -> {
            int tempIdx = r.nextInt(10);
            int newsIdx = r.nextInt(10);
            CompletableFuture<Integer> temp = CompletableFuture.supplyAsync(() -> {
                try {
                    Thread.sleep(info[1]);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return temperatures.get(tempIdx);
            });
            CompletableFuture<String> news = CompletableFuture.supplyAsync(() -> {
                try {
                    Thread.sleep(info[2]);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return newsHeadlines.get(newsIdx);
            });
            return CompletableFuture.allOf(temp, news).thenApply(v -> {
                return info[0] + " : Completed\nResult : " + "Temperature : " + temp.join() + "\nNews : " + news.join() + "\n";
            });
        };
        List<CompletableFuture<String>> futures = new ArrayList<>();
        for(int i=0; i<10; i++){
            futures.add(fetchData.apply(new Integer[]{i, r.nextInt(1001), r.nextInt(1001)}));
        }
        // CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        ArrayList<String> results = new ArrayList<>();
        List<CompletableFuture<Void>> futureList = 
                         futures.stream()
                                .map(f -> f.thenAccept((result) -> { // thenAccept return CompletableFuture<Void>
                                    synchronized(results){
                                        System.out.println(result);
                                        results.add(result);
                                    }
                                }))
                                .toList();
        CompletableFuture<Void> allResults = CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0]));
        allResults.join();
        System.out.println("Printing resulting now ... \n" + results);
    }
}
