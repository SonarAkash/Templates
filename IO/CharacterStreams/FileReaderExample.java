package IO.CharacterStreams;

import java.io.FileReader;
import java.util.List;

public class FileReaderExample {
    public void readData(List<String> fileName) {
        for (String path : fileName) {
            try (FileReader fr = new FileReader(path)) {
                int charRead;
                char[] cbuf = new char[1024];
                while ((charRead = fr.read(cbuf)) != -1) {
                    System.out.print(new String(cbuf, 0, charRead));
                }
                System.out.println("Data Read.");
                System.out.println();
            } catch (Exception e) {
                System.out.println("Error while opening the file");
            }
        }
    }
}
