package IO;

import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class NewIO {
    public static void main(String[] args) {
        Path source = Paths.get("./text/input/example.txt");
        Path destination = Paths.get("./text/output/out.txt");

        try (
                FileChannel srcChannel = FileChannel.open(source, StandardOpenOption.READ);
                FileChannel desChannel = FileChannel.open(destination, StandardOpenOption.WRITE)

        ) {
            ByteBuffer dts = ByteBuffer.allocate(1024);
            while(srcChannel.read(dts) > 0){
                dts.flip();
                while(dts.hasRemaining()){
                    desChannel.write(dts);
                }
            }
            dts.clear();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
