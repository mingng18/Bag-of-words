package com.tntco.bagofwordsmavenfx;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class WordFrequency {
    private final SimpleStringProperty word;
    private final SimpleIntegerProperty frequency;

    public WordFrequency(String word, int frequency) {
        this.word = new SimpleStringProperty(word);
        this.frequency = new SimpleIntegerProperty(frequency);
    }

    public String getWord() { return word.get(); }
    public int getFrequency() { return frequency.get(); }
}