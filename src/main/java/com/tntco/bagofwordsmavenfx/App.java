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
    private static final int NUMBER_OF_THREADS = 8;

    private static Scene scene;
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        Server.startServer();
        primaryStage = stage;
        scene = new Scene(loadFXML("primary"), 640, 480);
        stage.setScene(scene);
        stage.setTitle("Bag Of Words");
        stage.setOnCloseRequest(windowEvent -> {
            Server.stopServer();
        });
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        Parent root = loadFXML(fxml);
        scene.setRoot(root);
    }

    public static void showOutputScene(
            Map<String, Integer> wordFrequencies,
            Long totalTimeMethodOne,
            Map<String, Integer> wordFrequenciesTwo,
            Long totalTimeMethodTwo,
            Map<String, Integer> wordFrequenciesThree,
            Long totalTimeMethodThree
            ) throws IOException {

        // Ensure path is correct
        FXMLLoader loader = new FXMLLoader(App.class.getResource("secondary.fxml"));
        Parent root = loader.load();
        SecondaryController controller = loader.getController();
        controller.setWords(wordFrequencies, wordFrequenciesTwo, wordFrequenciesThree); // Set text in TextArea
        controller.setTime(totalTimeMethodOne, totalTimeMethodTwo, totalTimeMethodThree);

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

}
