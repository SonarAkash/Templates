package IO.ByteStreams;

import java.io.FileInputStream;

public class FileInputStreamExample {
    public void readData(String fileName) {
        try (FileInputStream fis = new FileInputStream(fileName)) {
            int byteData;
            while ((byteData = fis.read()) != -1) {
                System.out.print(byteData);
            }
            System.out.println("Data Read.");
        } catch (Exception e) {
            System.out.println("Error while opening the file.");
        }

        try (FileInputStream fis = new FileInputStream(fileName)) {
            int byteRead;
            byte b[] = new byte[10];
            while ((byteRead = fis.read(b)) != -1) {
                System.out.print(new String(b, 0, byteRead));
            }
            System.out.println("Data Read.");
        } catch (Exception e) {
            System.out.println("Error while opening the file.");
        }
    }
}
