import com.google.common.collect.Lists;

import java.io.*;
import java.util.*;

public class DictionaryService {
    private Map<String, List<Integer>> invertedIndex = new HashMap<>();
    private String pathToBlocks = "/Users/Vitaliy_Bazalytskyi/Desktop/info_retrieval/blocks";
    private int blockNumber;

    public void createDictionary(List<File> books) throws IOException {
        int i = 0;
        for (File book : books) {
            readFile(book, ++i);
        }
        margeBlocks();
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

    private void writeBlockToDisk() throws FileNotFoundException {
        File folder = new File(pathToBlocks);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File block = new File(pathToBlocks + "/block" + ++blockNumber);
        try {
            block.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        PrintWriter printWriter = new PrintWriter(block);
        Map<String, List<Integer>> sortedMap = new TreeMap<>(invertedIndex);
        for (Map.Entry<String, List<Integer>> entry : sortedMap.entrySet()) {
            String documentsSeparatedWithComma = entry.getValue().toString().replace("[", "").replace("]", "");
            printWriter.println(entry.getKey() + ":" + documentsSeparatedWithComma);
        }

    }

    private void addWordsToDictionary(String line, int fileIndex) throws FileNotFoundException {
        checkIfFreeMemoryAvailable();
        List<String> words = Arrays.asList(line.split("\\W+"));
        for (String token : words) {
            token = standardizeWord(token);
            if (StringUtils.isNotNullOrEmptyOrOneChar(token)) {
                addToInvertedIndex(token, fileIndex);
            }
        }
    }

    private void checkIfFreeMemoryAvailable() throws FileNotFoundException {
        int usedMemory = (int) (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
        int freeMemory = (int) Runtime.getRuntime().maxMemory() - usedMemory;
        if (freeMemory > 10 * 1000 * 1000) {
            writeBlockToDisk();
            invertedIndex = new HashMap<>();
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

    private void margeBlocks() throws IOException {
        List<File> blocks = Arrays.asList(new File("/Users/Vitaliy_Bazalytskyi/Desktop/info_retrieval/big_txt").listFiles());
        File generalFile = new File(pathToBlocks + "/general_block");
        generalFile.createNewFile();
        PrintWriter printWriter = new PrintWriter(generalFile);
        for (File block : blocks) {
            // to be implemented...
        }
    }
}
