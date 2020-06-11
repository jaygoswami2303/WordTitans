package com.ambush.wordtitans;

/**
 * Created by Jay on 01-10-2016.
 */
public class BoggleBoard {
    private final int M;
    private final int N;
    private char[][] board;


    public BoggleBoard(char[][] a) {
        this.M = a.length;
        this.N = a[0].length;
        board = new char[M][N];
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                board[i][j] = a[i][j];
            }
        }
    }

    public int rows() { return M; }

    public int cols() { return N; }

    public char getLetter(int i, int j) {
        return board[i][j];
    }
}
