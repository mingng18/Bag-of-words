package com.tntco.bagofwordsmavenfx;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import static com.tntco.bagofwordsmavenfx.App.showOutputScene;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class PrimaryController {

    @FXML
    private Button uploadButton;

    @FXML
    private void handleUploadButton() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Text File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            Map<String, Object> result = Server.processFile(file.getAbsolutePath());
            System.out.println(result);
            System.out.println("returned");
            System.out.println("Total time taken : "+ result.get("totalTimeMethodOne") + " milliseconds");
            showOutputScene((Map<String, Integer>) result.get("wordFrequencies"));
        }
    }
}
