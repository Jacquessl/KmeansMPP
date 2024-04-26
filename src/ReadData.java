import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;


public class ReadData {

    private String path;

    public ReadData(String path) {
        this.path = path;
    }

    public List<String[]> readData() {
        try (
                FileChannel inputChannel = FileChannel.open(Paths.get(path), StandardOpenOption.READ);
        ) {
            ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
            int bytesRead;
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            while ((bytesRead = inputChannel.read(buffer)) != -1) {
                buffer.flip();
                byte[] data = new byte[bytesRead];
                buffer.get(data);
                buffer.flip();
                while (buffer.hasRemaining()) {
                    byteArrayOutputStream.write(buffer.get());
                }
                buffer.clear();
            }
            byte[] data = byteArrayOutputStream.toByteArray();
            String decoded = new String(data, StandardCharsets.UTF_8);
            String[] split = decoded.split("\n");
            List<String[]> result = new ArrayList<>();
            for (String str : split){
                result.add(str.split(","));
            }
            return result;
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}
