package com.bag_of_words;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConcurrentBoW {
    private static final String INPUT_FILE = "C:/Users/user/Documents/Courses/Y3S2/WIF3011 CP/Bag-of-words/bag_of_words/src/main/resources/war_and_peace.txt";
    private static final int NUM_OF_THREADS = 8;

    public static void main(String[] args) throws IOException {
        //http://localhost:8000/bowConcurrent
        String text = new String(Files.readAllBytes(Paths.get(INPUT_FILE)));
        startServer(text);
    }

    public static void startServer(String text) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/bowConcurrent", new WordCountHandler(text));
        server.start();
    }

    private static class WordCountHandler implements HttpHandler {
        private String text;

        public WordCountHandler(String text) {
            this.text = text;
        }

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            long startTime  = System.nanoTime();
            System.out.println("Available processors: " + Runtime.getRuntime().availableProcessors());
            FindFrequencyConcurrent concurrent = new FindFrequencyConcurrent(text, NUM_OF_THREADS);
            System.out.println("\nComplete: " + ((System.nanoTime() - startTime ) / 1000000) + "ms");

            Word[] sortedWordFrequencies = concurrent.getFrequency().values().toArray(new Word[0]);
            Arrays.sort(sortedWordFrequencies);

            StringBuilder responseBuilder = new StringBuilder();
            for (int i = 0; i < sortedWordFrequencies.length; i++) {
                responseBuilder.append(String.format("%2d : %-14s%5d%n", (i + 1), sortedWordFrequencies[i].getWord(), sortedWordFrequencies[i].getFrequency()));
            }
            byte[] response = responseBuilder.toString().getBytes();

            httpExchange.sendResponseHeaders(200, response.length);
            OutputStream outputStream = httpExchange.getResponseBody();
            outputStream.write(response);
            outputStream.close();
        }
    }
}

class FindFrequencyConcurrent {
    private final String text;
    HashMap<String, Word> combinedWordMap = new HashMap<>();
    private int numOfThreads;
    private List<String> words = new ArrayList<>();

    public FindFrequencyConcurrent(String text, int numOfThreads) {
        this.text = text;
        this.numOfThreads = numOfThreads;
        processFile();
    }

    private void processFile() {
        if ((text != null) && (text.length() > 0)) {
            String[] lineWords = text.toLowerCase().split("[^a-z0-9']+");
            for (String word : lineWords) {
                if (!word.isEmpty()) {
                    words.add(word);
                }
            }
        }
    }

    public HashMap<String, Word> getFrequency() {
        FindFrequencyWorker[] fnw = new FindFrequencyWorker[numOfThreads];
        Thread[] threads = new Thread[numOfThreads];
        int totalWords = words.size();
        int range = totalWords / numOfThreads;

        for (int i = 0; i < numOfThreads; i++) {
            int start = i * range;
            int end = (i == numOfThreads - 1) ? totalWords : (start + range);
            fnw[i] = new FindFrequencyWorker(words.subList(start, end));
            threads[i] = new Thread(fnw[i]);
            threads[i].start();
        }

        try {
            for (Thread thread : threads) {
                thread.join();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        for (FindFrequencyWorker worker : fnw) {
            HashMap<String, Word> threadWordFreq = worker.getWordFrequency();
            for (Map.Entry<String, Word> entry : threadWordFreq.entrySet()) {
                String wordKey = entry.getKey();
                Word word = entry.getValue();
                combinedWordMap.merge(wordKey, word, (existingWord, newWord) -> {
                    existingWord.setFrequency(existingWord.getFrequency() + newWord.getFrequency());
                    return existingWord;
                });
            }
        }
        return combinedWordMap;
    }
}

class FindFrequencyWorker implements Runnable {
    private final List<String> words;
    private final HashMap<String, Word> wordMap = new HashMap<>();

    public FindFrequencyWorker(List<String> words) {
        this.words = words;
    }

    @Override
    public void run() {
        for (String word : words) {
            if (!word.isEmpty()) {
                word = word.endsWith("'s") ? word.substring(0, word.length() - 2) : word;
                wordMap.merge(word, new Word(word), (existingWord, newWord) -> {
                    existingWord.setFrequency(existingWord.getFrequency() + 1);
                    return existingWord;
                });
            }
        }
    }

    public HashMap<String, Word> getWordFrequency() {
        return wordMap;
    }
}

