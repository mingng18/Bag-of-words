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
import javafx.scene.control.Tab;

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
    private Tab tab1;
    
    @FXML
    private Tab tab2;

    @FXML
    private Tab tab3;

    @FXML
    private Tab tab4;

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
    public Text uniqueGeneralOne;

    @FXML
    public Text uniqueGeneralTwo;

    @FXML
    public Text uniqueGeneralThree;

    @FXML
    public Text uniqueGeneralFour;

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
        timerGeneralThree.setText(totalTimeMethodThree + "ms");
        timerFour.setText(totalTimeMethodFour + "ms");
        timerGeneralFour.setText(totalTimeMethodFour + "ms");
        updateBarChart();
    }

    public void setWordCount(int count) {
        wordCountOne.setText(count + "");
        wordGeneralOne.setText(count + "");
        wordCountTwo.setText(count + "");
        wordGeneralTwo.setText(count + "");
        wordCountThree.setText(count + "");
        wordGeneralThree.setText(count + "");
        wordCountFour.setText(count + "");
        wordGeneralFour.setText(count + "");
    }

    public void setUniqueWordCount(int count1, int count2, int count3, int count4) {
        uniqueGeneralOne.setText(count1 + "");
        uniqueGeneralTwo.setText(count2 + "");
        uniqueGeneralThree.setText(count3 + "");
        uniqueGeneralFour.setText(count4 + "");
    }

    public void setTitle(String title1, String title2, String title3, String title4) {
        titleOne.setText(title1 + "");
        titleGeneralOne.setText(title1 + "");
        tab1.setText(title1 + "");
        titleTwo.setText(title2 + "");
        titleGeneralTwo.setText(title2 + "");
        tab2.setText(title2 + "");
        titleThree.setText(title3 + "");
        titleGeneralThree.setText(title3 + "");
        tab3.setText(title3 + "");
        titleFour.setText(title4 + "");
        titleGeneralFour.setText(title4 + "");
        tab4.setText(title4 + "");

        updateBarChart();
    }

    public void setdesc() {
        descOne.setText("This method processes a given text sequentially to create a bag of words, which is a frequency map of words. It splits the text into words, iterates through each word, and updates the word count in a map. This approach is simple and runs on a single thread, making it straightforward.");
        descTwo.setText("This method uses multiple threads to process text in parallel by dividing the text into chunks. Each thread processes a chunk and generates a sub-map of word frequencies. After all threads complete, their results are merged into a final frequency map.");
        descThree.setText("This method uses pessimistic locking to ensure thread-safe updates to a shared word count map. Multiple threads process chunks of text concurrently, but each update to the shared map is synchronized to prevent concurrent modification issues.");
        descFour.setText("This method uses optimistic locking which allows all threads to create a copy of the shared memory without any blocking. Each thread processes a chunk of text and uses atomic operations to update word frequencies to the shared map concurrently.");
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
