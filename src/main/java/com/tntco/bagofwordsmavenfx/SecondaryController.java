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
import javafx.scene.control.Button;

public class SecondaryController {

    @FXML
    private Button backButton;

    @FXML
    private TableView<WordFrequency> tableOne;

    @FXML
    private TableView<WordFrequency> tableTwo;

    @FXML
    private TableView<WordFrequency> tableThree;

    @FXML
    private TableColumn<WordFrequency, String> wordTabOne;

    @FXML
    private TableColumn<WordFrequency, String> wordTabTwo;

    @FXML
    private TableColumn<WordFrequency, String> wordTabThree;

    @FXML
    private TableColumn<WordFrequency, Number> frequencyTabOne;

    @FXML
    private TableColumn<WordFrequency, Number> frequencyTabTwo;

    @FXML
    private TableColumn<WordFrequency, Number> frequencyTabThree;

    @FXML
    private Text titleGeneralOne;

    @FXML
    private Text titleGeneralTwo;

    @FXML
    private Text titleGeneralThree;

    @FXML
    private Text timerGeneralOne;

    @FXML
    private Text timerGeneralTwo;

    @FXML
    private Text timerGeneralThree;

    @FXML
    private Text wordGeneralOne;

    @FXML
    private Text wordGeneralTwo;

    @FXML
    private Text wordGeneralThree;

    @FXML
    private Text titleOne;

    @FXML
    private Text titleTwo;

    @FXML
    private Text titleThree;

    @FXML
    private Text descOne;

    @FXML
    private Text descTwo;

    @FXML
    private Text descThree;

    @FXML
    private Text timerOne;

    @FXML
    private Text timerThree;

    @FXML
    private Text timerTwo;

    @FXML
    private Text wordCountOne;

    @FXML
    private Text wordCountTwo;

    @FXML
    private Text wordCountThree;

    @FXML
    private void initialize() {
        wordTabOne.setCellValueFactory(new PropertyValueFactory<>("word"));
        frequencyTabOne.setCellValueFactory(new PropertyValueFactory<>("frequency"));
        tableOne.setItems(FXCollections.observableArrayList());

        wordTabTwo.setCellValueFactory(new PropertyValueFactory<>("word"));
        frequencyTabTwo.setCellValueFactory(new PropertyValueFactory<>("frequency"));
        tableTwo.setItems(FXCollections.observableArrayList());

        wordTabThree.setCellValueFactory(new PropertyValueFactory<>("word"));
        frequencyTabThree.setCellValueFactory(new PropertyValueFactory<>("frequency"));
        tableThree.setItems(FXCollections.observableArrayList());
    }

    public void setWords(Map<String, Integer> wordFrequencies, Map<String, Integer> wordFrequenciesTwo, Map<String, Integer> wordFrequenciesThree) {
        ObservableList<WordFrequency> items = FXCollections.observableArrayList();
        wordFrequencies.forEach((word, freq) -> items.add(new WordFrequency(word, freq)));
        tableOne.setItems(items);

        ObservableList<WordFrequency> itemsTwo = FXCollections.observableArrayList();
        System.out.println("wordFrequenciesTwo" + wordFrequenciesTwo);
        wordFrequenciesTwo.forEach((word, freq) -> itemsTwo.add(new WordFrequency(word, freq)));
        tableTwo.setItems(itemsTwo);

        ObservableList<WordFrequency> itemsThree = FXCollections.observableArrayList();
        System.out.println("wordFrequenciesThree" + wordFrequenciesThree);
        wordFrequenciesThree.forEach((word, freq) -> itemsThree.add(new WordFrequency(word, freq)));
        tableThree.setItems(itemsThree);
    }

    public void setTime(Long totalTimeMethodOne, Long totalTimeMethodTwo, Long totalTimeMethodThree) {
        timerOne.setText(totalTimeMethodOne + "ms");
        timerGeneralOne.setText(totalTimeMethodOne + "ms");
        timerTwo.setText(totalTimeMethodTwo + "ms");
        timerGeneralTwo.setText(totalTimeMethodTwo + "ms");
        timerThree.setText(totalTimeMethodThree + "ms");
        timerGeneralThree.setText(totalTimeMethodThree + "ms");
    }

    public void setWordCount(String count1, String count2, String count3) {
        wordCountOne.setText(count1 + "");
        wordGeneralOne.setText(count1 + "");
        wordCountTwo.setText(count2 + "");
        wordGeneralTwo.setText(count2 + "");
        wordCountThree.setText(count3 + "");
        wordGeneralThree.setText(count3 + "");
    }

    public void setTitle(String title1, String title2, String title3) {
        titleOne.setText(title1 + "");
        titleGeneralOne.setText(title1 + "");
        titleTwo.setText(title2 + "");
        titleGeneralTwo.setText(title2 + "");
        titleThree.setText(title3 + "");
        titleGeneralThree.setText(title3 + "");
    }

    public void setdesc(String desc1, String desc2, String desc3) {
        descOne.setText(desc1 + "");
        descTwo.setText(desc2 + "");
        descThree.setText(desc3 + "");
    }

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }
}
