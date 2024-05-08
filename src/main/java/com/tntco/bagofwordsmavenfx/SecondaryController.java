package com.tntco.bagofwordsmavenfx;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class SecondaryController {

    @FXML
    private Button backButton;

    @FXML
    private TableColumn<?, ?> c1;

    @FXML
    private TableColumn<?, ?> c2;

    @FXML
    private TableColumn<?, ?> c3;

    @FXML
    private TableView<?> table;

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }
}
