package IO.BufferedStream;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;

public class BufferedOuput {
    public void writeRawData(List<String> fileNames) {
        for (String inputFilePath : fileNames) {
            String outputFilePath = inputFilePath.replace("input", "output");

            try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outputFilePath, true));
                    BufferedInputStream bis = new BufferedInputStream(new FileInputStream(inputFilePath));) {
                int byteRead;
                byte[] b = new byte[1024];
                while ((byteRead = bis.read(b)) != -1) {
                    bos.write(b, 0, byteRead);
                }
                System.out.println("Date written");
                System.out.println("______");
            } catch (Exception e) { 
                System.out.println("Error while opening the file");
            }
        }
    }

    public void writeTextData(List<String> fileNames) {
        for (String inputFilePath : fileNames) {
            String outputFilePath = inputFilePath.replace("input", "output");
            try (BufferedReader br = new BufferedReader(new FileReader(inputFilePath));
                    BufferedWriter bw = new BufferedWriter(new FileWriter(outputFilePath, true));){
                String data;
                while((data = br.readLine()) != null){
                    bw.write(data);
                    bw.newLine();
                }
                System.out.println("______");
                System.out.println("Date written");
            } catch (Exception e) {
                System.out.println("Error while opening the file");
            }
        }
    }
}
