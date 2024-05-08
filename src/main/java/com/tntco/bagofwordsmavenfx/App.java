package com.tntco.bagofwordsmavenfx;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * JavaFX App
 */
public class App extends Application {

    private static final String INPUT_FILE = "C:\\Users\\Asus\\IdeaProjects\\Bag-of-words\\src\\main\\resources\\com\\tntco\\bagofwordsmavenfx\\war_and_peace.txt";
    static Map<String, Integer> wordFrequencies = new HashMap<>();
    private static final int NUMBER_OF_THREADS = 8;

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("primary"), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        try {
            String text = new String(Files.readAllBytes(Paths.get(INPUT_FILE)));
            System.out.println("Processing");
            text = cleanText(text);
            System.out.println("Processing Done");

            createBagOfWords(text);

            for (Map.Entry<String, Integer> entry : wordFrequencies.entrySet()) {
                System.out.println("Word: " + entry.getKey() + ", Count: " + entry.getValue());
            }

            launch();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static String cleanText(String text) {
        text = text.replaceAll("(?<![a-zA-Z])'|'(?![a-zA-Z])", " ").replaceAll("[^a-zA-Z' ]", " ").toLowerCase();
        return text;
    }

    public static void createBagOfWords(String text) {
        String[] words = text.split(" ");
        for (String word : words) {
            wordFrequencies.put(word, wordFrequencies.getOrDefault(word, 0) + 1);
        }
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