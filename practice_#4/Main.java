import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        File books_folder = new File("/Users/Vitaliy_Bazalytskyi/Desktop/info_retrieval/big_txt");
        DictionaryService dictionaryService = new DictionaryService();
        dictionaryService.createDictionary(Arrays.asList(books_folder.listFiles()));
    }
}
