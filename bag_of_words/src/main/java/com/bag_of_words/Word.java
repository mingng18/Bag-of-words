package com.bag_of_words;

public class Word implements Comparable<Word> {

    private String word;
    private int frequency;

    public Word(String word) {

        this.setWord(word);
        this.setFrequency(1);

    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    @Override
    public int compareTo(Word o1) {

        return o1.frequency - this.frequency;

    }



}