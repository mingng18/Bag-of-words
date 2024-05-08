package com.tntco.bagofwordsmavenfx;


public class Worker implements Runnable {
    private String[] strings;
    private BlockingHashMap blockingHashMap;
    private int begin;
    private int end;


    public Worker(int begin, int next, String[] words, BlockingHashMap blockingHashMap) {
        this.begin = begin;
        this.end = next;
        this.strings = words;
        this.blockingHashMap = blockingHashMap;
    }

    @Override
    public void run() {
        for (int i = begin; i < end; i++) {
            String word = strings[i];
            blockingHashMap.writeValue(word);
        }
    }
}
