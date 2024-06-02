/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tntco.bagofwordsmavenfx;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author kwany
 */
public class Server {

    static Map<String, Integer> wordFrequencies = new HashMap<>();
    private static final int NUMBER_OF_THREADS = Runtime.getRuntime().availableProcessors();
    static HttpServer server;

    public static void startServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress(8000), 0);

        server.createContext(
                "/search", new WordCountHandler());
        System.out.println("Server started at port 8000 with " + NUMBER_OF_THREADS + " threads");
        Executor executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

        server.setExecutor(executor);

        server.start();
    }

    public static void stopServer() {
        server.stop(0);
    }

    // load the file from the client side, then pass to the server side for
    // processing
    public static Map<String, Object> processFile(String filePath) {
        try {
            String text = new String(Files.readAllBytes(Paths.get(filePath)));
            String encodedText = URLEncoder.encode(text, StandardCharsets.UTF_8.toString());
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8000/search?text=" + encodedText))
                    .build();
            byte[] responseBytes = client.sendAsync(request, BodyHandlers.ofByteArray())
                    .thenApply(HttpResponse::body)
                    .join();
            // Decode the response
            ByteArrayInputStream bais = new ByteArrayInputStream(responseBytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            Map<String, Object> wordFrequencies = (Map<String, Object>) ois.readObject();
            ois.close();
            return wordFrequencies;

        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    // Handling the request from the client at the server side
    private static class WordCountHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {

            String query = httpExchange.getRequestURI().getQuery();
            String[] keyValue = query.split("=");
            String action = keyValue[0];
            String text = keyValue[1];
            if (!action.equals("text")) {
                httpExchange.sendResponseHeaders(400, 0);
                return;
            }

            System.out.println("Cleaning Text");
            text = cleanText(text);
            System.out.println("Text Cleaning Done");

            System.out.println("Starting method 1");
            long methodOneStartTime = System.currentTimeMillis();
            Map<String, Integer> wordFrequencies = createBagOfWordsSequential(text);
            long methodOneEndTime = System.currentTimeMillis();
            long totalTimeMethodOne = methodOneEndTime - methodOneStartTime;
            System.out.println(totalTimeMethodOne + " milliseconds used in total time method one");

            System.out.println("Starting method 2");
            long methodTwoStartTime = System.currentTimeMillis();
            Map<String, Integer> wordFrequenciesTwo = createBagOfWordsWithSubMapMerge(text);
            long methodTwoEndTime = System.currentTimeMillis();
            long totalTimeMethodTwo = methodTwoEndTime - methodTwoStartTime;
            System.out.println(totalTimeMethodTwo + " milliseconds used in total time method two");

            System.out.println("Starting method 3");
            long methodThreeStartTime = System.currentTimeMillis();
            Map<String, Integer> wordFrequenciesThree = createBagOfWordsWithSynchronizedBlock(text);
            long methodThreeEndTime = System.currentTimeMillis();
            long totalTimeMethodThree = methodThreeEndTime - methodThreeStartTime;
            System.out.println(totalTimeMethodThree + " milliseconds used in total time method three");

            System.out.println("Starting method 4");
            long methodFourStartTime = System.currentTimeMillis();
            Map<String, Integer> wordFrequenciesFour = createBagOfWordsWithOptimisticLock(text);
            long methodFourEndTime = System.currentTimeMillis();
            long totalTimeMethodFour = methodFourEndTime - methodFourStartTime;
            System.out.println(totalTimeMethodFour + " milliseconds used in total time method four");

            Map<String, Object> combinedResponse = new HashMap<>();
            combinedResponse.put("wordFrequencies", wordFrequencies);
            combinedResponse.put("totalTimeMethodOne", totalTimeMethodOne);
            combinedResponse.put("wordFrequenciesTwo", wordFrequenciesTwo);
            combinedResponse.put("totalTimeMethodTwo", totalTimeMethodTwo);
            combinedResponse.put("wordFrequenciesThree", wordFrequenciesThree);
            combinedResponse.put("totalTimeMethodThree", totalTimeMethodThree);
            combinedResponse.put("wordFrequenciesFour", wordFrequenciesFour);
            combinedResponse.put("totalTimeMethodFour", totalTimeMethodFour);
            combinedResponse.put("totalWordCount", text.split(" ").length);

            // Serialize the combined response
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(combinedResponse);
            oos.close();

            // Send the combined response
            byte[] response = baos.toByteArray();
            httpExchange.sendResponseHeaders(200, response.length);
            OutputStream outputStream = httpExchange.getResponseBody();
            outputStream.write(response);
            outputStream.close();
        }

        // create bag of words using sequential processing
        private Map<String, Integer> createBagOfWordsSequential(String text) {
            Map<String, Integer> wordFrequencies = new HashMap<>();
            String[] words = text.split(" ");

            for (String word : words) {
                if (!word.isEmpty()) {
                    wordFrequencies.put(word, wordFrequencies.getOrDefault(word, 0) + 1);
                }
            }
            return wordFrequencies;
        }

        // 1.1 Merge Sub-Maps (Runnable)
        private Map<String, Integer> createBagOfWordsWithSubMapMerge(String text) {
            ExecutorService executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
            List<String> words = Arrays.asList(text.split(" "));
            int chunkSize = (int) Math.ceil((double) words.size() / NUMBER_OF_THREADS);
            FindFrequencyWorker[] fnw = new FindFrequencyWorker[NUMBER_OF_THREADS];
            for (int i = 0; i < NUMBER_OF_THREADS; i++) {
                int start = i * chunkSize;
                int end = (i == NUMBER_OF_THREADS - 1) ? words.size() : (start + chunkSize);
                fnw[i] = new FindFrequencyWorker(words.subList(start, end));
                executor.execute(fnw[i]);
            }

            executor.shutdown();
            // Wait until all tasks are finished
            while (!executor.isTerminated()) {
            }

            Map<String, Integer> finalResult = new HashMap<>();
            for (FindFrequencyWorker worker : fnw) {
                Map<String, Integer> result = worker.getWordCount();
                mergeWordFrequencies(finalResult, result);
            }
            return finalResult;
        }

        // 1.2 Pessimistic Lock (Synchronized Block)
        private Map<String, Integer> createBagOfWordsWithSynchronizedBlock(String text) {
            ExecutorService executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
            List<String> words = Arrays.asList(text.split(" "));
            int chunkSize = (int) Math.ceil((double) words.size() / NUMBER_OF_THREADS);
            BlockingHashMap blockingHashMap = new BlockingHashMap();

            for (int i = 0; i < words.size(); i += chunkSize) {
                List<String> chunk = words.subList(i, Math.min(i + chunkSize, words.size()));
                executor.execute(() -> {
                    for (String word : chunk) {
                        if (!word.isEmpty()) {
                            blockingHashMap.writeValue(word);
                        }
                    }
                });
            }

            executor.shutdown();
            while (!executor.isTerminated()) {
            }

            return blockingHashMap.getWordCount();
        }

        // 1.3 Optimistic Lock (Atomic Integer)
        private Map<String, Integer> createBagOfWordsWithOptimisticLock(String text) {
            ExecutorService executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
            List<String> words = Arrays.asList(text.split(" "));
            int chunkSize = (int) Math.ceil((double) words.size() / NUMBER_OF_THREADS);
            ConcurrentHashMap<String, AtomicInteger> wordCountMap = new ConcurrentHashMap<>();

            for (int i = 0; i < words.size(); i += chunkSize) {
                List<String> chunk = words.subList(i, Math.min(i + chunkSize, words.size()));
                executor.execute(() -> {
                    for (String word : chunk) {
                        if (!word.isEmpty()) {
                            wordCountMap.computeIfAbsent(word, k -> new AtomicInteger()).incrementAndGet();
                        }
                    }
                });
            }

            executor.shutdown();
            while (!executor.isTerminated()) {
            }

            Map<String, Integer> finalResult = new HashMap<>();
            wordCountMap.forEach((word, count) -> finalResult.put(word, count.get()));
            return finalResult;
        }

        // 1.4 Completable Future (unused)
        private Map<String, Integer> createBagOfWordsWithCompletableFuture(String text) {
            ExecutorService executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
            List<String> words = Arrays.asList(text.split(" "));
            int chunkSize = (int) Math.ceil((double) words.size() / NUMBER_OF_THREADS);

            List<CompletableFuture<Map<String, Integer>>> futures = new ArrayList<>();

            for (int i = 0; i < words.size(); i += chunkSize) {
                List<String> chunk = words.subList(i, Math.min(i + chunkSize, words.size()));
                FindFrequencyWorker3 worker = new FindFrequencyWorker3(chunk);
                futures.add(CompletableFuture.supplyAsync(() -> {
                    try {
                        return worker.call();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }, executor));
            }

            Map<String, Integer> finalResult = new HashMap<>();
            futures.forEach(f -> {
                try {
                    Map<String, Integer> result = f.get();
                    mergeWordFrequencies(finalResult, result);
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
            });
            executor.shutdown();
            return finalResult;
        }

        private void mergeWordFrequencies(Map<String, Integer> mainMap, Map<String, Integer> subMap) {
            subMap.forEach((word, count) -> mainMap.put(word, mainMap.getOrDefault(word, 0) + count));
        }

        public String cleanText(String text) {
            text = text.replaceAll("(?<![a-zA-Z])'|'(?![a-zA-Z])", " ").replaceAll("[^a-zA-Z' ]", " ").toLowerCase();
            return text;
        }
    }

}

class FindFrequencyWorker implements Runnable {
    private final List<String> words;
    private final HashMap<String, Integer> wordCount = new HashMap<>();

    public FindFrequencyWorker(List<String> words) {
        this.words = words;
    }

    @Override
    public void run() {
        for (String word : words) {
            if (!word.isEmpty()) {
                wordCount.put(word, wordCount.getOrDefault(word, 0) + 1);
            }
        }
    }

    public HashMap<String, Integer> getWordCount() {
        return wordCount;
    }
}

class BlockingHashMap {
    private static Map<String, Integer> wordCount;

    public BlockingHashMap() {
        this.wordCount = new HashMap<>();
    }

    public synchronized void writeValue(String word) {
        wordCount.put(word, wordCount.getOrDefault(word, 0) + 1);
    }

    public static Map<String, Integer> getWordCount() {
        return wordCount;
    }
}

class FindFrequencyWorker3 implements Callable<Map<String, Integer>> {
    private final List<String> words;
    private final HashMap<String, Integer> wordCount = new HashMap<>();

    public FindFrequencyWorker3(List<String> words) {
        this.words = words;
    }

    @Override
    public Map<String, Integer> call() {
        for (String word : words) {
            if (!word.isEmpty()) {
                wordCount.put(word, wordCount.getOrDefault(word, 0) + 1);
            }
        }
        return wordCount;
    }
}
