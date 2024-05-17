/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tntco.bagofwordsmavenfx;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import static com.tntco.bagofwordsmavenfx.App.showOutputScene;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.math.BigInteger;
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
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author kwany
 */
public class Server {

    private static final String INPUT_FILE = "C:\\Users\\user\\Documents\\.Y3S2\\WIF3011 CPP\\text_file.txt";
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

    // load the file from the client side, then pass to to the server side for
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

            System.out.println("Processing");
            text = cleanText(text);
            System.out.println("Processing Done");

            long methodOneStartTime = System.currentTimeMillis();
            Map<String, Integer> wordFrequencies = createBagOfWords(text);
            long methodOneEndTime = System.currentTimeMillis();

            long totalTimeMethodOne = methodOneEndTime - methodOneStartTime;

            Map<String, Object> combinedResponse = new HashMap<>();
            combinedResponse.put("wordFrequencies", wordFrequencies);
            combinedResponse.put("totalTimeMethodOne", totalTimeMethodOne);

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

        // create bag of words using sequencial processing
        private Map<String, Integer> createBagOfWords(String text) {
            long startTime = System.currentTimeMillis();
            Map<String, Integer> wordFrequencies = new HashMap<>();
            String[] words = text.split(" ");

            for (String word : words) {
                wordFrequencies.put(word, wordFrequencies.getOrDefault(word, 0) + 1);
            }

            long endTime = System.currentTimeMillis();
            return wordFrequencies;
        }

        // TODO: create bag of words using parallel processing method 1

        // TODO: create bag of words using parallel processing method 2

        public String cleanText(String text) {
            text = text.replaceAll("(?<![a-zA-Z])'|'(?![a-zA-Z])", " ").replaceAll("[^a-zA-Z' ]", " ").toLowerCase();
            return text;
        }
    }

}
