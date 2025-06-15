package IO.FileHandling;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class HandleFile {

    public void createFileAndWriteData(String fileName, String source) throws IOException {
        File root = new File("D:\\Java\\IO\\FileHandling\\output");
        if (!root.exists()) {
            if (root.mkdirs()) {
                System.out.println("Created Directory :)");
            } else {
                System.out.println("Failed to create Directory :(");
            }
        }
        File dest = new File(root, fileName);
        if (!dest.exists()) {
            if (dest.createNewFile()) {
                System.out.println("File Created :)");
            } else {
                System.out.println("Failed to create File :(");
            }
        }
        File sourcePath = new File(source);
        System.out.println("source path exist");
        try (BufferedReader br = new BufferedReader(new FileReader(sourcePath));
                BufferedWriter bw = new BufferedWriter(new FileWriter(dest, true))) {
            String data;
            while ((data = br.readLine()) != null) {
                bw.write(data);
                bw.newLine();
            }
            System.out.println("Data written :)");
        } catch (Exception e) {
            System.out.println("Error while opening source file :(");
        }
    }

    public void listTheNameInDir(String dirName) {
        File dir = new File(dirName);
        File[] list = dir.listFiles();
        for (File f : list)
            System.out.println(f.getName());
    }

    public void createFileAndWriteData2(String fileName, String source){
        File root = new File("D:\\Java\\IO\\FileHandling\\output2");
        if(!root.exists()){
            try {
                if(root.mkdir()){
                File sourcePath = new File(source);
                Path outPath = Paths.get(root.getAbsolutePath() + File.separator + fileName + getExtn(sourcePath));
                System.out.println(outPath);
                if(Files.exists(outPath)){
                    Files.createFile(outPath);
                }
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(sourcePath));
                Files.copy(bis, outPath);
                }else{
                    System.out.println("Failed to create Dir :(");
                }
           } catch (IOException e) {
                System.out.println("Error ....");
                e.printStackTrace();
            }
        }else{
            System.out.println("Failed to create Root dir");
        }
    }
    private String getExtn(File p){
        String s = p.getName().toString();
        for(int i=0; i<s.length(); i++){
            if(s.charAt(i) == '.') return s.substring(i); 
        }
        return ".png";
    }
}
