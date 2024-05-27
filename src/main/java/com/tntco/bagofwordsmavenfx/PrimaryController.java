package com.tntco.bagofwordsmavenfx;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import static com.tntco.bagofwordsmavenfx.App.showOutputScene;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class PrimaryController {

    @FXML
    private Button uploadButton;

    @FXML
    private Text loading;

    @FXML
    private void handleUploadButton() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Text File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {

            uploadButton.setVisible(false);
            loading.setVisible(true);

            Service<Map<String, Object>> service = new Service<Map<String, Object>>() {
                @Override
                protected Task<Map<String, Object>> createTask() {
                    return new Task<Map<String, Object>>() {
                        @Override
                        protected Map<String, Object> call() throws Exception {
                            return Server.processFile(file.getAbsolutePath());
                        }
                    };
                }
            };

            service.setOnSucceeded(event -> {
                Map<String, Object> result = service.getValue();
                if (result != null) {
                    try {
                        showOutputScene(
                                (Map<String, Integer>) result.get("wordFrequencies"),
                                (Long) result.get("totalTimeMethodOne"),
                                (Map<String, Integer>) result.get("wordFrequenciesTwo"),
                                (Long) result.get("totalTimeMethodTwo"),
                                (Map<String, Integer>) result.get("wordFrequenciesThree"),
                                (Long) result.get("totalTimeMethodThree"),
                                (Map<String, Integer>) result.get("wordFrequenciesFour"),
                                (Long) result.get("totalTimeMethodFour"));
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                uploadButton.setVisible(true);
                loading.setVisible(false);
            });

            service.setOnFailed(event -> {
                Throwable ex = service.getException();
                ex.printStackTrace();
                uploadButton.setVisible(true);
                loading.setVisible(false);
            });

            service.start();

            // No opening a new thread
//            Platform.runLater(() -> {
//                uploadButton.setVisible(false);
//                loading.setVisible(true);
//            });
//            Map<String, Object> result = Server.processFile(file.getAbsolutePath());
//            Platform.runLater(() -> {
//                try {
//                    showOutputScene(
//                            (Map<String, Integer>) result.get("wordFrequencies"),
//                            (Long) result.get("totalTimeMethodOne"),
//                            (Map<String, Integer>) result.get("wordFrequenciesTwo"),
//                            (Long) result.get("totalTimeMethodTwo"),
//                            (Map<String, Integer>) result.get("wordFrequenciesThree"),
//                            (Long) result.get("totalTimeMethodThree"),
//                            (Map<String, Integer>) result.get("wordFrequenciesFour"),
//                            (Long) result.get("totalTimeMethodFour"));
//                } catch (IOException ex) {
//                    ex.printStackTrace();
//                }
//            });
//
//            uploadButton.setVisible(true);
//            loading.setVisible(false);

        }
    }
}
