package com.tntco.bagofwordsmavenfx;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ConcurrentDriver {
    private static final String INPUT_FILE = "C:\\Users\\Asus\\IdeaProjects\\Bag-of-words\\src\\main\\resources\\com\\tntco\\bagofwordsmavenfx\\war_and_peace.txt";
    private static final int NUMBER_OF_THREADS = 8;

    public static void main(String[] args) throws IOException {
        BlockingHashMap blockingHashMap = new BlockingHashMap();
        Worker[] workers = new Worker[NUMBER_OF_THREADS];
        Thread[] t = new Thread[NUMBER_OF_THREADS];
        String[] words;

        try {
            String text = new String(Files.readAllBytes(Paths.get(INPUT_FILE)));
            text = cleanText(text);

            // Trim the spaces between words
            text = text.replaceAll("\\s+", " ");
            words = text.split(" ");

            int begin = 0;
            int range = words.length / NUMBER_OF_THREADS;
            int next = range;

            for (int i = 0; i < NUMBER_OF_THREADS; i++) {
                if (i == NUMBER_OF_THREADS - 1) {
                    next = words.length;
                }
                workers[i] = new Worker(begin, next, words, blockingHashMap);
                t[i] = new Thread(workers[i]);
                t[i].start();
                begin = next;
                next += range;
            }

            try {
                for (int i = 0; i < NUMBER_OF_THREADS; i++) {
                    t[i].join();
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }

            blockingHashMap.printCounts();
        } catch (IOException ex) {
            ex.printStackTrace();
        }


    }

    private static String cleanText(String text) {
        text = text.replaceAll("(?<![a-zA-Z])'|'(?![a-zA-Z])", " ").replaceAll("[^a-zA-Z' ]", " ").toLowerCase();
        return text;
    }


    public static void startServer(String text) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/search", new WordCountHandler(text));
        Executor executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
        server.setExecutor(executor);
        server.start();
    }

    private static class WordCountHandler implements HttpHandler {

        private String text;

        public WordCountHandler(String text) {
            this.text = text;
        }

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String query = httpExchange.getRequestURI().getQuery();
            String[] keyValue = query.split("=");
            String action = keyValue[0];
            String word = keyValue[1];
            if (!action.equals("word")) {
                httpExchange.sendResponseHeaders(400, 0);
                return;
            }

            long count = countWord(word);
            String bagOfWords = createBagOfWords(text);

            // byte[] response = Long.toString(count).getBytes();
            byte[] response = bagOfWords.getBytes();
            httpExchange.sendResponseHeaders(200, response.length);
            OutputStream outputStream = httpExchange.getResponseBody();
            outputStream.write(response);
            outputStream.close();
        }

        private long countWord(String word) {
            long count = 0;
            int index = 0;
            while (index >= 0) {
                index = text.indexOf(word, index);

                if (index >= 0) {
                    count++;
                    index++;
                }
            }
            return count;
        }

        // create bag of words
        private static String createBagOfWords(String text) {
            HashMap<String, Integer> wordFrequencies = new HashMap<>();
            String[] words = text.split(" ");

            for (String word : words) {
                wordFrequencies.put(word, wordFrequencies.getOrDefault(word, 0) + 1);
            }

            return wordFrequencies.toString();
        }
    }
}
