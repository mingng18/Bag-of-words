package com.tntco.bagofwordsmavenfx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.util.Map;

public class SecondaryController {

    @FXML
    private TableView<WordFrequency> table;
    @FXML
    private TableColumn<WordFrequency, String> c1;
    @FXML
    private TableColumn<WordFrequency, Number> c2;

    @FXML
    private void initialize() {
        c1.setCellValueFactory(new PropertyValueFactory<>("word"));
        c2.setCellValueFactory(new PropertyValueFactory<>("frequency"));
        table.setItems(FXCollections.observableArrayList());
    }

    public void setWords(Map<String, Integer> wordFrequencies) {
        ObservableList<WordFrequency> items = FXCollections.observableArrayList();
        wordFrequencies.forEach((word, freq) -> items.add(new WordFrequency(word, freq)));
        table.setItems(items);
    }

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }
}
