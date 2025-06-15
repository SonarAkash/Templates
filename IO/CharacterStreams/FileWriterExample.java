package IO.CharacterStreams;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;

public class FileWriterExample {
   public void writeData(List<String> paths){
        for(String s : paths){
            String outputPath = s.replace("input", "output");
            try (FileReader fr = new FileReader(s);
                 FileWriter fw = new FileWriter(outputPath, true);) {
                
                    char cbuf[] = new char[1024];
                    int charRead;
                    while((charRead = fr.read(cbuf)) != -1){
                        fw.write(cbuf, 0, charRead);
                    }
                    System.out.println("Data written.");
            } catch (Exception e) {
               System.err.println("Error while opening the file"); 
            }
        }
   } 
}
