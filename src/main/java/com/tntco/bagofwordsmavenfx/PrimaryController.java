package com.tntco.bagofwordsmavenfx;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class PrimaryController {

    @FXML
    private Button uploadButton;

    @FXML
    private void handleUploadButton() throws IOException {
        App.setRoot("secondary");
    }
}
