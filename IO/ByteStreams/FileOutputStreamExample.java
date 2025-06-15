package IO.ByteStreams;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

public class FileOutputStreamExample {
    public void writeData(List<String> fileNames) {
        for (String inputFilePath : fileNames) {
            String outputFilePath = inputFilePath.replace("input", "output");

            try (FileOutputStream fos = new FileOutputStream(outputFilePath, true);
                 FileInputStream fis = new FileInputStream(inputFilePath);
            ) {
                int byteRead;
                byte []b = new byte[1024];
                while((byteRead = fis.read(b)) != -1){
                    fos.write(b, 0, byteRead);
                }
                System.out.println("Date written");
            } catch (Exception e) {;
                System.out.println("Error while opening the file");
            }
        }
    }
}
