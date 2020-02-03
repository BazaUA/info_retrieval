import java.io.File;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        File books_folder = new File("/Users/Vitaliy/Desktop/info_retrieval/books");
        DictionaryService dictionaryService = new DictionaryService();
        dictionaryService.createDictionary(Arrays.asList(books_folder.listFiles()));
        String query;
        Scanner scanner = new Scanner(System.in);
        query = scanner.nextLine();
        while (!("END").equals(query)) {
            System.out.println(dictionaryService.retrieve(query));
            query = scanner.nextLine();
        }
    }
}
