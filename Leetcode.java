// Method 1: Extending the Thread class

class DocumentPrinter extends Thread {
    private String documentName;

    public DocumentPrinter(String name) {
        this.documentName = name;
    }

    // This is the heart of the thread. The code inside run() is what the thread will execute.
    @Override
    public void run() {
        try {
            System.out.println("-> Starting to print " + documentName);
            for (int i = 1; i <= 5; i++) {
                System.out.println("   Printing " + documentName + ": Page #" + i);
                // Thread.sleep() pauses the thread for a specified time (in milliseconds).
                // This helps simulate a real task and makes it easier to see threads interleaving.
                Thread.sleep(500); // Pause for half a second
            }
        } catch (InterruptedException e) {
            // This exception is thrown if another thread interrupts this one while it's sleeping.
            System.out.println(documentName + " printing was interrupted.");
        }
        System.out.println("<- Finished printing " + documentName);
    }
}

public class Leetcode {
    public static void main(String[] args) {
        System.out.println("== Main thread started. ==");

        // Create two instances of our thread class
        DocumentPrinter thread1 = new DocumentPrinter("AnnualReport.pdf");
        DocumentPrinter thread2 = new DocumentPrinter("Invoice_123.pdf");

        // Start the threads. This is crucial!
        // Calling start() allocates a new thread of execution and then calls our run() method.
        thread1.start();
        thread2.start();

        System.out.println("== Main thread finished. ==");
        // Note: The main thread might finish before the printer threads do!
    }
}
