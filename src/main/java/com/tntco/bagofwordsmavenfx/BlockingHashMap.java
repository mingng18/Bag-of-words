package com.tntco.bagofwordsmavenfx;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BlockingHashMap {
    private static Map<String, Integer> wordFrequencies;
    private Lock lock;

    public BlockingHashMap() {
        this.wordFrequencies = new HashMap<>();
        this.lock = new ReentrantLock();
    }

    public synchronized void writeValue(String word) {
        lock.lock();
        try {
            wordFrequencies.put(word, wordFrequencies.getOrDefault(word, 0) + 1);
        } finally {
            lock.unlock();
        }
    }

    public void printCounts() {
        //Sort Map
        this.wordFrequencies = sortByValue(wordFrequencies);
        for (Map.Entry<String, Integer> entry : wordFrequencies.entrySet()) {
            System.out.println("Word: " + entry.getKey() + ", Count: " + entry.getValue());
        }
    }


    // This one havent really study, from gpt
    private static Map<String, Integer> sortByValue (Map<String, Integer> map) {
        // Create a stream from the entries of the map
        Stream<Map.Entry<String, Integer>> sortedStream = map.entrySet().stream()
                // Sort the entries by value in descending order
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        // Collect the sorted entries into a LinkedHashMap to preserve the order
        Map<String, Integer> sortedMap = sortedStream.collect(
                Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        return sortedMap;
    }
}
