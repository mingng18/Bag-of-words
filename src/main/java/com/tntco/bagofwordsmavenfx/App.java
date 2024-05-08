package com.tntco.bagofwordsmavenfx;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URL;
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

    private static final String INPUT_FILE = "C:\\Users\\user\\Documents\\.Y3S2\\WIF3011 CPP\\text_file.txt";
    static Map<String, Integer> wordFrequencies = new HashMap<>();
    private static final int NUMBER_OF_THREADS = 8;

    private static Scene scene;
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        scene = new Scene(loadFXML("primary"), 640, 480);
        stage.setScene(scene);
        stage.setTitle("Bag Of Words");
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        Parent root = loadFXML(fxml);
        scene.setRoot(root);
    }

    public static void showOutputScene(String text) throws IOException {
        // Ensure path is correct
        FXMLLoader loader = new FXMLLoader(App.class.getResource("secondary.fxml"));
        Parent root = loader.load();
        SecondaryController controller = loader.getController();
        controller.setWords(wordFrequencies);  // Set text in TextArea

        Scene scene = new Scene(root, 640, 480);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
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

    public static void processFile(String filePath) {
        try {
            String text = new String(Files.readAllBytes(Paths.get(filePath)));
            System.out.println("Processing");
            text = cleanText(text);
            System.out.println("Processing Done");

            createBagOfWords(text);

            StringBuilder output = new StringBuilder();
            wordFrequencies.forEach((word, count) ->
                    output.append("Word: ").append(word).append(", Count: ").append(count).append("\n"));

            showOutputScene(output.toString());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
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