// package Gemini;

// import java.io.BufferedReader;
// import java.io.BufferedWriter;
// import java.io.IOException;
// import java.nio.file.*;
// import java.util.concurrent.ExecutorService;
// import java.util.concurrent.Executors;
// import java.util.concurrent.TimeUnit;
// import java.util.concurrent.atomic.AtomicInteger;
// import java.util.stream.Stream;
// import static java.nio.file.StandardWatchEventKinds.*;

// public class ReadAndDeleteFiles {
//     public static void main(String[] args) throws Exception {
//         System.out.println("Working dir: " + System.getProperty("user.dir"));
//         final AtomicInteger counter = new AtomicInteger(0);
//         Runnable insertFile = new Runnable() {
//             @Override
//             public void run(){ // D:\Java\Gemini\Logs
//                 Path src = Paths.get("D:\\Java\\Gemini\\randomText.txt");
//                 Path dest = Paths.get("D:\\Java\\Gemini\\Logs\\randomText" + counter.incrementAndGet() + ".txt");
//                 try {
//                     BufferedReader reader = Files.newBufferedReader(src);
//                     BufferedWriter writer = Files.newBufferedWriter(dest, StandardOpenOption.CREATE_NEW);
//                     String currentLine = null;
//                     while((currentLine = reader.readLine()) != null){
//                         writer.write(currentLine);
//                         writer.newLine();
//                     }
//                 } catch (IOException e) {
//                     // TODO Auto-generated catch block
//                     e.printStackTrace();
//                 }
//             }
//         };

//         Path dir = Paths.get("D:\\Java\\Gemini\\Logs");
//         // Step 1: Process existing files

//         processExistingFiles(dir);

//         ExecutorService executor = Executors.newFixedThreadPool(4);
//         for(int i=1; i<11; i++){
//             executor.submit(insertFile);
//         }

//         executor.shutdown();

//         try {
//             // Wait for 2 minute for the tasks to complete
//             if (!executor.awaitTermination(120, TimeUnit.SECONDS)) {
//                 System.err.println("Tasks did not complete in time. Forcing shutdown.");
//                 executor.shutdownNow();
//             }
//         } catch (InterruptedException e) {
//             executor.shutdownNow();
//         }


//         // Step 2: Watch for new files
//         watchForNewFiles(dir);
//     }

//     private static void processExistingFiles(Path dir) {
//         System.out.println("Processing existing files in: " + dir);
//         try (Stream<Path> paths = Files.list(dir)) {
//             paths.filter(Files::isRegularFile)
//                  .forEach(ReadAndDeleteFiles::processAndDelete);
//         } catch (IOException e) {
//             System.err.println("Error listing existing files: " + e.getMessage());
//         }
//     }

//     private static void watchForNewFiles(Path dir) throws IOException, InterruptedException {
//         WatchService watchService = FileSystems.getDefault().newWatchService();
//         dir.register(watchService, ENTRY_CREATE);

//         System.out.println("Watching for new files...");

//         while (true) {
//             WatchKey key = watchService.take(); // waits for events
//             for (WatchEvent<?> event : key.pollEvents()) {
//                 if (event.kind() == ENTRY_CREATE) {
//                     Path newFile = dir.resolve((Path) event.context());

//                     // Optional: Small delay to ensure file write completes
//                     try { Thread.sleep(500); } catch (InterruptedException ignored) {}

//                     processAndDelete(newFile);
//                 }
//             }
//             key.reset();
//         }
//     }

//     private static void processAndDelete(Path filePath) {
//         try {
//             long lastSize = -1;
//             long currentSize = Files.size(filePath);

//             // Wait until file size stays the same for 1 second
//             while (currentSize != lastSize) {
//                 lastSize = currentSize;
//                 Thread.sleep(1000);
//                 currentSize = Files.size(filePath);
//             }

//             // Now read & delete
//             System.out.println("Reading: " + filePath);
//             try (Stream<String> lines = Files.lines(filePath)) {
//                 lines.forEach(System.out::println);
//             }

//             Files.delete(filePath);
//             System.out.println("Deleted: " + filePath);
//         } catch (IOException | InterruptedException e) {
//             System.err.println("Error processing file: " + filePath + " - " + e.getMessage());
//         }
//     }
// }




package Gemini;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;

public class ReadAndDeleteFiles {

    public static void main(String[] args) throws Exception {
        Path dir = Paths.get("D:\\Java\\Gemini\\Logs");
        Path src = Paths.get("D:\\Java\\Gemini\\randomText.txt");

        // Ensure directories exist
        Files.createDirectories(dir);

        System.out.println("Working dir: " + System.getProperty("user.dir"));

        // Step 1: Process backlog before watcher starts
        processExistingFiles(dir);

        // Step 2: Start watcher in its own thread BEFORE producers
        Thread watcherThread = new Thread(() -> {
            try {
                watchForNewFiles(dir);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        watcherThread.setDaemon(true);
        watcherThread.start();

        // Step 3: Simulate file producers
        startFileProducers(src, dir);

        // Keep main alive to allow watcher to run
        Thread.currentThread().join();
    }

    private static void processExistingFiles(Path dir) {
        System.out.println("Processing existing files in: " + dir);
        try (Stream<Path> paths = Files.list(dir)) {
            paths.filter(Files::isRegularFile)
                 .forEach(ReadAndDeleteFiles::processAndDeleteWhenStable);
        } catch (IOException e) {
            System.err.println("Error listing existing files: " + e.getMessage());
        }
    }

    private static void watchForNewFiles(Path dir) throws IOException, InterruptedException {
        WatchService watchService = FileSystems.getDefault().newWatchService();
        dir.register(watchService, ENTRY_CREATE);

        System.out.println("Watching for new files...");

        while (true) {
            WatchKey key = watchService.take(); // waits for events
            for (WatchEvent<?> event : key.pollEvents()) {
                if (event.kind() == ENTRY_CREATE) {
                    Path newFile = dir.resolve((Path) event.context());
                    processAndDeleteWhenStable(newFile);
                }
            }
            key.reset();
        }
    }

    private static void processAndDeleteWhenStable(Path filePath) {
        try {
            // Wait until file size stays constant for 1 second
            long lastSize = -1;
            long currentSize = Files.size(filePath);
            while (currentSize != lastSize) {
                lastSize = currentSize;
                Thread.sleep(1000);
                currentSize = Files.size(filePath);
            }

            // Read file
            System.out.println("Reading: " + filePath);
            try (Stream<String> lines = Files.lines(filePath)) {
                lines.forEach(System.out::println);
            }

            // Delete after reading
            Files.delete(filePath);
            System.out.println("Deleted: " + filePath);

        } catch (IOException | InterruptedException e) {
            System.err.println("Error processing file: " + filePath + " - " + e.getMessage());
        }
    }

    private static void startFileProducers(Path src, Path destDir) throws IOException {
        final AtomicInteger counter = new AtomicInteger(0);
        ExecutorService executor = Executors.newFixedThreadPool(4);

        Runnable insertFile = () -> {
            Path dest = destDir.resolve("randomText" + counter.incrementAndGet() + ".txt");
            try (
                BufferedReader reader = Files.newBufferedReader(src);
                BufferedWriter writer = Files.newBufferedWriter(dest, StandardOpenOption.CREATE_NEW)
            ) {
                String currentLine;
                while ((currentLine = reader.readLine()) != null) {
                    writer.write(currentLine);
                    writer.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        };

        // Submit some file creation tasks
        for (int i = 1; i <= 10; i++) {
            executor.submit(insertFile);
        }

        executor.shutdown();
    }
}
