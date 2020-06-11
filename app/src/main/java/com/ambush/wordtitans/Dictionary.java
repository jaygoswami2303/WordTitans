package com.ambush.wordtitans;

/**
 * Created by Jay on 01-10-2016.
 */

        import java.io.BufferedReader;
        import java.io.IOException;
        import java.io.InputStream;
        import java.io.InputStreamReader;
        import java.util.ArrayList;


public class Dictionary{

    public final static int MIN_WORD_LENGTH = 3;
    private ArrayList<String> words = new ArrayList<>();

    public Dictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        String line = null;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            if (word.length() >= MIN_WORD_LENGTH)
                words.add(line.trim().toUpperCase());
        }
    }

    public ArrayList<String> getWords()
    {
        return words;
    }
}
