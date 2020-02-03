import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;

public class FileUtils {
    public static void serializeDictionary(String fileName, Object object) {
        FileOutputStream fout = null;
        ObjectOutputStream oos = null;
        try {
            fout = new FileOutputStream(fileName);
            oos = new ObjectOutputStream(fout);
            oos.writeObject(object);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeToFileDictionary(String fileName, Set<String> dictionary) {
        File file = new File(fileName);
        try {
            Files.write(Paths.get(file.toURI()), dictionary);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
