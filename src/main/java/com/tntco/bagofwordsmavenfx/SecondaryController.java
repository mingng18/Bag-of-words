package com.tntco.bagofwordsmavenfx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

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
    private TableView<WordFrequency> tableTwo;
    @FXML
    private TableColumn<WordFrequency, String> c3;
    @FXML
    private TableColumn<WordFrequency, Number> c4;

    @FXML
    private TableView<WordFrequency> tableThree;
    @FXML
    private TableColumn<WordFrequency, String> 5;
    @FXML
    private TableColumn<WordFrequency, Number> c6;

    @FXML
    private Text timerOne;
    @FXML
    private Text timerTwo;
    @FXML
    private Text timerThree;

    @FXML
    private void initialize() {
        c1.setCellValueFactory(new PropertyValueFactory<>("word"));
        c2.setCellValueFactory(new PropertyValueFactory<>("frequency"));
        table.setItems(FXCollections.observableArrayList());

        c3.setCellValueFactory(new PropertyValueFactory<>("word"));
        c4.setCellValueFactory(new PropertyValueFactory<>("frequency"));
        tableTwo.setItems(FXCollections.observableArrayList());

        c5.setCellValueFactory(new PropertyValueFactory<>("word"));
        c6.setCellValueFactory(new PropertyValueFactory<>("frequency"));
        tableThree.setItems(FXCollections.observableArrayList());
    }

    public void setWords(Map<String, Integer> wordFrequencies, Map<String, Integer> wordFrequenciesTwo, Map<String, Integer> wordFrequenciesThree) {
        ObservableList<WordFrequency> items = FXCollections.observableArrayList();
        wordFrequencies.forEach((word, freq) -> items.add(new WordFrequency(word, freq)));
        table.setItems(items);

        ObservableList<WordFrequency> itemsTwo  = FXCollections.observableArrayList();
        System.out.println("wordFrequenciesTwo" + wordFrequenciesTwo);
        wordFrequenciesTwo.forEach((word, freq) -> itemsTwo.add(new WordFrequency(word, freq)));
        tableTwo.setItems(itemsTwo);

        ObservableList<WordFrequency> itemsThree  = FXCollections.observableArrayList();
        System.out.println("wordFrequenciesThree" + wordFrequenciesThree);
        wordFrequenciesThree.forEach((word, freq) -> itemsThree.add(new WordFrequency(word, freq)));
        tableThree.setItems(itemsThree);
    }

    public void setTime(Long totalTimeMethodOne, Long totalTimeMethodTwo, Long totalTimeMethodThree) {
        timerOne.setText("Time taken : " + totalTimeMethodOne + "ms");
        timerTwo.setText("Time taken : " + totalTimeMethodTwo + "ms");
        timerThree.setText("Time taken : " + totalTimeMethodThree + "ms");
    }

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }
}
