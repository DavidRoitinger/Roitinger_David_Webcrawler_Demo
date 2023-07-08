package htl.roida.roitingerdavidwebcrawlerdemo;

import java.util.*;
import java.util.stream.Collectors;

public class Statistic {

    private final Map<String, Integer> wordMap = new HashMap<>();

    private static Statistic instance = null;

    private Statistic() {
        instance = this;
    }

    public static Statistic getInstance() {
        if (instance == null) {
            return new Statistic();
        }
        return instance;
    }

    public void putWordInMap(String word){
        if (wordMap.containsKey(word)) {
            wordMap.replace(word, wordMap.get(word)+1);
            return;
        }
        wordMap.put(word, 1);

    }

    public Map<String, Integer> getWordMap() {
        return wordMap;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String key: wordMap.entrySet().stream().sorted(Map.Entry.comparingByValue()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new)).keySet()) {
            if (wordMap.get(key) > 3) {
                stringBuilder.append(key).append(": ").append(wordMap.get(key)).append("\n");
            }
        }
        return stringBuilder.toString();
    }
}
