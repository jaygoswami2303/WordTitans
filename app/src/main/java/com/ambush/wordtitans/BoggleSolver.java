package com.ambush.wordtitans;

/**
 * Created by Jay on 01-10-2016.
 */

import java.util.HashSet;
import java.util.Set;

public class BoggleSolver {
    private Node root;

    private static class Node {
        private String word;
        private Node[] next = new Node[26];
    }

    public BoggleSolver(String[] dictionary) {
        for (int i = 0; i < dictionary.length; i++)
            put(dictionary[i], dictionary[i]);
    }

    public Iterable<String> getAllValidWords(BoggleBoard board) {
        Node next_n;
        int b_row = board.rows(), b_col = board.cols();
        Set<String> valid_words = new HashSet<String>(3*b_row*b_col);
        boolean[][] visited = new boolean[b_row][b_col];

        for (int i = 0; i < b_row; i++)
            for (int j = 0; j < b_col; j++) {
                    next_n = get(root, board.getLetter(i, j), 0);

                if (next_n != null)
                    dfs(board, visited, valid_words, i, j, next_n);
            }

        return valid_words;
    }

    private void dfs(BoggleBoard board, boolean[][] visited, Set<String> valid_words, int i, int j, Node x) {
        Node next_n;
        int b_row = board.rows(), b_col = board.cols();

        visited[i][j] = true;

        for(int adj_row = i - 1; adj_row <= i + 1; adj_row++)
            for(int adj_col = j - 1; adj_col <= j + 1; adj_col++) {
                if (adj_row == i && adj_col == j)
                    continue;
                if (adj_row < b_row && adj_col < b_col && adj_row >= 0 && adj_col >= 0) {
                    if (!visited[adj_row][adj_col]) {
                            next_n = get(x, board.getLetter(adj_row, adj_col), 0);

                        if (next_n != null)
                            dfs(board, visited, valid_words, adj_row, adj_col, next_n);
                    }
                }
            }

        visited[i][j] = false;

        if (x.word != null)
            if (x.word.length() > 2)
                valid_words.add(x.word);
    }

    private Node get(Node x, char key, int d) {
        if (x == null) return null;
        if (d == 1) return x;
        return get(x.next[key-65], key, d+1);
    }

    private void put(String key, String word) {
        root = put(root, key, word, 0);
    }

    private Node put(Node x, String key, String word, int d) {
        if (x == null) x = new Node();
        if (d == key.length()) {
            x.word = word;
            return x;
        }
        char c = key.charAt(d);
        x.next[c-65] = put(x.next[c-65], key, word, d+1);
        return x;
    }
}
