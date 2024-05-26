package com.tntco.bagofwordsmavenfx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import static com.tntco.bagofwordsmavenfx.App.showPrimaryScene;

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
    private TableView<WordFrequency> tableFour;


    @FXML
    private TableColumn<WordFrequency, String> wordTabOne;

    @FXML
    private TableColumn<WordFrequency, String> wordTabTwo;

    @FXML
    private TableColumn<WordFrequency, String> wordTabThree;

    @FXML
    private TableColumn<WordFrequency, String> wordTabFour;

    @FXML
    private TableColumn<WordFrequency, Number> frequencyTabOne;

    @FXML
    private TableColumn<WordFrequency, Number> frequencyTabTwo;

    @FXML
    private TableColumn<WordFrequency, Number> frequencyTabThree;

    @FXML
    private TableColumn<WordFrequency, Number> frequencyTabFour;

    @FXML
    private Text titleGeneralOne;

    @FXML
    private Text titleGeneralTwo;

    @FXML
    private Text titleGeneralThree;

    @FXML
    private Text titleGeneralFour;

    @FXML
    private Text timerGeneralOne;

    @FXML
    private Text timerGeneralTwo;

    @FXML
    private Text timerGeneralThree;

    @FXML
    private Text timerGeneralFour;

    @FXML
    private Text wordGeneralOne;

    @FXML
    private Text wordGeneralTwo;

    @FXML
    private Text wordGeneralThree;

    @FXML
    private Text wordGeneralFour;

    @FXML
    private Text titleOne;

    @FXML
    private Text titleTwo;

    @FXML
    private Text titleThree;

    @FXML
    private Text titleFour;

    @FXML
    private Text descOne;

    @FXML
    private Text descTwo;

    @FXML
    private Text descThree;

    @FXML
    private Text descFour;

    @FXML
    private Text timerOne;

    @FXML
    private Text timerTwo;

    @FXML
    private Text timerThree;

    @FXML
    private Text timerFour;

    @FXML
    private Text wordCountOne;

    @FXML
    private Text wordCountTwo;

    @FXML
    private Text wordCountThree;

    @FXML
    private Text wordCountFour;

    @FXML
    private BarChart<String, Number> barChart;

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

        wordTabFour.setCellValueFactory(new PropertyValueFactory<>("word"));
        frequencyTabFour.setCellValueFactory(new PropertyValueFactory<>("frequency"));
        tableFour.setItems(FXCollections.observableArrayList());


        backButton.setOnMousePressed(event -> {
            // Your callback code here
            try {
                System.out.println("Back button pressed");
                showPrimaryScene();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
    }

    public void setWords(Map<String, Integer> wordFrequencies, Map<String, Integer> wordFrequenciesTwo,
            Map<String, Integer> wordFrequenciesThree, Map<String, Integer> wordFrequenciesFour) {
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

        ObservableList<WordFrequency> itemsFour = FXCollections.observableArrayList();
        System.out.println("wordFrequenciesFour" + wordFrequenciesFour);
        wordFrequenciesFour.forEach((word, freq) -> itemsFour.add(new WordFrequency(word, freq)));
        tableFour.setItems(itemsFour);
    }

    public void setTime(Long totalTimeMethodOne, Long totalTimeMethodTwo, Long totalTimeMethodThree, Long totalTimeMethodFour) {
        timerOne.setText(totalTimeMethodOne + "ms");
        timerGeneralOne.setText(totalTimeMethodOne + "ms");
        timerTwo.setText(totalTimeMethodTwo + "ms");
        timerGeneralTwo.setText(totalTimeMethodTwo + "ms");
        timerThree.setText(totalTimeMethodThree + "ms");
        timerGeneralThree.setText(totalTimeMethodFour + "ms");
        timerFour.setText(totalTimeMethodFour + "ms");
        timerGeneralFour.setText(totalTimeMethodFour + "ms");
        updateBarChart();
    }

    public void setWordCount(String count1, String count2, String count3, String count4) {
        wordCountOne.setText(count1 + "");
        wordGeneralOne.setText(count1 + "");
        wordCountTwo.setText(count2 + "");
        wordGeneralTwo.setText(count2 + "");
        wordCountThree.setText(count3 + "");
        wordGeneralThree.setText(count3 + "");
        wordCountFour.setText(count4 + "");
        wordGeneralFour.setText(count4 + "");
    }

    public void setTitle(String title1, String title2, String title3, String title4) {
        titleOne.setText(title1 + "");
        titleGeneralOne.setText(title1 + "");
        titleTwo.setText(title2 + "");
        titleGeneralTwo.setText(title2 + "");
        titleThree.setText(title3 + "");
        titleGeneralThree.setText(title3 + "");
        titleFour.setText(title4 + "");
        titleGeneralFour.setText(title4 + "");

        updateBarChart();
    }

    public void setdesc(String desc1, String desc2, String desc3, String desc4) {
        descOne.setText(desc1 + "");
        descTwo.setText(desc2 + "");
        descThree.setText(desc3 + "");
        descFour.setText(desc4+ "");
    }

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }

    private void updateBarChart() {
        String title1 = titleGeneralOne.getText();
        String title2 = titleGeneralTwo.getText();
        String title3 = titleGeneralThree.getText();
        String title4 = titleGeneralFour.getText();

        Long time1 = parseTime(timerGeneralOne.getText());
        Long time2 = parseTime(timerGeneralTwo.getText());
        Long time3 = parseTime(timerGeneralThree.getText());
        Long time4 = parseTime(timerGeneralFour.getText());

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>(title1, time1));
        series.getData().add(new XYChart.Data<>(title2, time2));
        series.getData().add(new XYChart.Data<>(title3, time3));
        series.getData().add(new XYChart.Data<>(title4, time4));

        barChart.getData().clear();
        barChart.getData().add(series);
    }

    private Long parseTime(String timeString) {
        try {
            if (timeString.endsWith("ms")) {
                return Long.parseLong(timeString.replace("ms", "").trim());
            } else if (timeString.endsWith("s")) {
                return Long.parseLong(timeString.replace("s", "").trim()) * 1000;
            } else {
                return Long.parseLong(timeString.trim());
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0L;
        }
    }
}
