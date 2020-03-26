import java.io.File;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        File books_folder = new File("/Users/Vitaliy_Bazalytskyi/Desktop/info_retrieval/books");
        DictionaryService dictionaryService = new DictionaryService();
        dictionaryService.createDictionary(Arrays.asList(books_folder.listFiles()));
    }
}
