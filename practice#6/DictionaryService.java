import com.google.common.collect.Lists;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class DictionaryService {
    private Map<String, List<Integer>> invertedIndex = new HashMap<>();
    private Set<String> dictionary = new HashSet<>();
    private StringBuilder compressedDictionary = new StringBuilder();
    private List<TableEntry> table = new ArrayList<>();

    public void createDictionary(List<File> books) {
        int i =0;
        for (File book : books) {
            readFile(book, ++i);
        }
        compress();
        System.out.println("the end");
    }

    private void compress() {
        List<String> sortedDictionary = convertDictionaryToSortedList();
        sortedDictionary.forEach(word -> {
            compressedDictionary.append(word);

            int pointerToWord = compressedDictionary.length() - word.length();
            List<Integer> pointerToInvertedIndex = invertedIndex.get(word);

            table.add(new TableEntry(pointerToInvertedIndex, pointerToWord));
        });
    }

    private List<String> convertDictionaryToSortedList() {
        List<String> dictionary = new ArrayList<>(this.dictionary);
        Collections.sort(dictionary);
        return dictionary;
    }

    private void readFile(File book, int fileIndex) {
        try (FileInputStream inputStream = new FileInputStream(book); Scanner sc = new Scanner(inputStream, "UTF-8")) {
            while (sc.hasNextLine()) {
                addWordsToDictionary(sc.nextLine(), fileIndex);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addWordsToDictionary(String line, int fileIndex) {
        List<String> words = Arrays.asList(line.split("\\W+"));
        for (String token : words) {
            token = standardizeWord(token);
            if (StringUtils.isNotNullOrEmptyOrOneChar(token)) {
                addToInvertedIndex(token, fileIndex);
                dictionary.add(token);
            }
        }
    }

    private String addToInvertedIndex(String token, int fileIndex) {
        if (invertedIndex.get(token) == null) {
            invertedIndex.put(token, Lists.newArrayList(fileIndex));
        } else if (!invertedIndex.get(token).contains(fileIndex)) {
            invertedIndex.get(token).add(fileIndex);
        }
        return token;
    }

    private String standardizeWord(String word) {
        return word.replaceAll("\\P{L}", "").toLowerCase().trim();
    }
}
