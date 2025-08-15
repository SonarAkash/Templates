package Gemini;

// Method 2: Implementing the Runnable interface

class DocumentTask implements Runnable {
    private String documentName;

    public DocumentTask(String name) {
        this.documentName = name;
    }

    // Again, the run() method contains the job to be done.
    @Override
    public void run() {
        try {
            System.out.println("-> Starting task for " + documentName);
            for (int i = 1; i <= 5; i++) {
                System.out.println("   Processing " + documentName + ": Part #" + i);
                Thread.sleep(500); // Pause for half a second
            }
        } catch (InterruptedException e) {
            System.out.println("Task for " + documentName + " was interrupted.");
        }
        System.out.println("<- Finished task for " + documentName);
    }
}

public class ThreadCreationFromRunnable {
    public static void main(String[] args) {
        System.out.println("== Main thread started. ==");

        // Create instances of our task
        DocumentTask task1 = new DocumentTask("DataAnalysis.csv");
        DocumentTask task2 = new DocumentTask("UserManual.docx");

        // Create Thread objects and pass them our tasks
        Thread thread1 = new Thread(task1);
        Thread thread2 = new Thread(task2);

        // Start the threads
        thread1.start();
        thread2.start();

        System.out.println("== Main thread finished. ==");
    }
}
