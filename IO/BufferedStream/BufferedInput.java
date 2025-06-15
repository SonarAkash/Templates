package IO.BufferedStream;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;

public class BufferedInput {
    public void readRawData(String fileName) {
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(fileName))) {
            int readLen;
            byte b[] = new byte[1024];
            while((readLen = bis.read(b)) != -1){
                System.out.print(new String(b, 0, readLen));
            }
            System.out.println("______");
            System.out.println("Data Read.");
            System.out.println("______");
        } catch (Exception e) {
            System.out.println("Error while opening the file.");
        } 
    }
    public void readTextData(String fileName){
        try (BufferedReader bis = new BufferedReader(new FileReader(fileName))) {
            String data;
            while((data = bis.readLine()) != null){
                System.out.print(data);
            }
            System.out.println("______");
            System.out.println("Data Read.");
            System.out.println("______");

        } catch (Exception e) {
            System.out.println("Error while opening the file.");
        }
    }
}
