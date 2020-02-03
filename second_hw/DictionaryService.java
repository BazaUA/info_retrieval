import com.google.common.collect.Lists;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class DictionaryService {
    private Map<String, List<Boolean>> termDocumentIndex = new TreeMap<>();
    private Map<String, List<Integer>> invertedIndex = new TreeMap<>();
    private int numberOfDocuments;

    public void createDictionary(List<File> books) {
        numberOfDocuments = books.size();
        AtomicInteger index = new AtomicInteger();
        books.forEach(book -> readFile(book, index.getAndIncrement()));
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
                addToTermDocumentIndex(token, fileIndex);
            }
        }
    }

    private String addToTermDocumentIndex(String token, int fileIndex) {
        if (termDocumentIndex.get(token) == null) {
            List<Boolean> initialList = Arrays.asList(new Boolean[numberOfDocuments]);
            initialList.set(fileIndex, Boolean.TRUE);
            termDocumentIndex.put(token, initialList);
        } else if (termDocumentIndex.get(token) != null && termDocumentIndex.get(token).get(fileIndex) == null) {
            termDocumentIndex.get(token).set(fileIndex, Boolean.TRUE);
        }
        return token;
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

    public String retrieve(String query) {
        List<Integer> result = new ArrayList<>();
        List<String> tokensToSearch = getTokensToSearch(query);
        List<Operation> operations = getOperations(query);
        for (int i = 0; i < operations.size(); i++) {
            switch (operations.get(i)) {
                case OR:
                    result.addAll(orOperation(tokensToSearch.get(i), tokensToSearch.get(i + 1)));
                    break;
                case AND:
                    result.addAll(andOperation(tokensToSearch.get(i), tokensToSearch.get(i + 1)));
                    break;
                case NOT:
                    result.addAll(notOperation(tokensToSearch.get(i), tokensToSearch.get(i + 1)));
                    break;
            }
        }
        return result.toString();
    }


    private Collection<Integer> notOperation(String firstToken, String secondToken) {
        List<Integer> documentsForFirst = invertedIndex.get(firstToken);
        List<Integer> documentsForSecond = invertedIndex.get(secondToken);
        documentsForFirst.removeAll(documentsForSecond);
        return documentsForFirst;
    }

    private Collection<Integer> andOperation(String firstToken, String secondToken) {
        List<Integer> documentsForFirst = invertedIndex.get(firstToken);
        List<Integer> documentsForSecond = invertedIndex.get(secondToken);
        return documentsForFirst.stream()
                .distinct()
                .filter(documentsForSecond::contains)
                .collect(Collectors.toSet());
    }

    private Collection<Integer> orOperation(String firstToken, String secondToken) {
        Collection<Integer> result = new TreeSet<>();
        result.addAll(invertedIndex.get(firstToken));
        result.addAll(invertedIndex.get(secondToken));
        return result;
    }

    private List<Operation> getOperations(String query) {
        List<Operation> result = new ArrayList<>();
        String[] tokensWithOperators = query.split(" ");
        for (int i = 1; i < tokensWithOperators.length; i = i + 2) {
            result.add(Enum.valueOf(Operation.class, tokensWithOperators[i]));
        }
        return result;
    }

    private List<String> getTokensToSearch(String query) {
        List<String> result = new ArrayList<>();
        String[] tokensWithOperators = query.split(" ");
        for (int i = 0; i < tokensWithOperators.length; i = i + 2) {
            result.add(tokensWithOperators[i]);
        }
        return result;
    }

    private enum Operation {
        AND, OR, NOT
    }
}
