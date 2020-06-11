package com.ambush.wordtitans;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    TextView tv[][], tvWord, timerText, tvScore,tvList;
    final Handler handler = new Handler();
    Button bSubmit, bUndo;
    Dictionary dictionary;
    BoggleBoard Board;
    BoggleSolver solver;
    HashSet<String> wordsList;
    InputStream is;
    char board[][];
    boolean flag[][];
    String text;
    LinearLayout linearLayout;
    Animation anim;
    final int MAX_WORD_SIZE = 4;
    int count = 0;
    int userScore = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = new TextView[4][4];
        board = new char[4][4];
        flag = new boolean[4][4];
        text = "";
        bSubmit = (Button) findViewById(R.id.bSubmit);
        bUndo = (Button) findViewById(R.id.bUndo);
        anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        linearLayout.startAnimation(anim);

        tvList = (TextView) findViewById(R.id.tvList);
        tvScore = (TextView) findViewById(R.id.tvScore);
        tvWord = (TextView) findViewById(R.id.tvWord);
        tv[0][0] = (TextView) findViewById(R.id.tv1);
        tv[0][1] = (TextView) findViewById(R.id.tv2);
        tv[0][2] = (TextView) findViewById(R.id.tv3);
        tv[0][3] = (TextView) findViewById(R.id.tv4);
        tv[1][0] = (TextView) findViewById(R.id.tv5);
        tv[1][1] = (TextView) findViewById(R.id.tv6);
        tv[1][2] = (TextView) findViewById(R.id.tv7);
        tv[1][3] = (TextView) findViewById(R.id.tv8);
        tv[2][0] = (TextView) findViewById(R.id.tv9);
        tv[2][1] = (TextView) findViewById(R.id.tv10);
        tv[2][2] = (TextView) findViewById(R.id.tv11);
        tv[2][3] = (TextView) findViewById(R.id.tv12);
        tv[3][0] = (TextView) findViewById(R.id.tv13);
        tv[3][1] = (TextView) findViewById(R.id.tv14);
        tv[3][2] = (TextView) findViewById(R.id.tv15);
        tv[3][3] = (TextView) findViewById(R.id.tv16);

        /*Log.d("gtfrdegvrfde",Integer.toString(R.id.tv1));
        Log.d("aaaaaaaaaaaa",Integer.toString(tv[0][0].getId()));
        */
        bSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(wordsList.contains(text)) {
                    userScore += count;
                    tvScore.setText("Score: " + Integer.toString(userScore));
                    tvList.setText(tvList.getText() + "\n" + text);
                    wordsList.remove(text);
                }
                else {
                    Toast.makeText(MainActivity.this,"INVALID WORD!",Toast.LENGTH_SHORT).show();
                }
                clear();
            }
        });

        bUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear();
            }
        });
        Random random = new Random();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                tv[i][j].setText((char) (random.nextInt(26) + 'A') + "");
                board[i][j] = tv[i][j].getText().charAt(0);
            }
        }
        Board = new BoggleBoard(board);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                final int k = i, l = j;
                tv[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        text += tv[k][l].getText();
                        if(text.length()==5) {
                            text = text.substring(0, 4);
                            disable(-1,-1);
                            return;
                        }
                        tvWord.setText(text);
                        switch (count) {
                            case 0:
                                tv[k][l].setBackgroundColor(Color.rgb(102, 0, 51));
                                break;
                            case 1:
                                tv[k][l].setBackgroundColor(Color.rgb(153, 0, 76));
                                break;
                            case 2:
                                tv[k][l].setBackgroundColor(Color.rgb(204, 0, 102));
                                break;
                            case 3:
                                tv[k][l].setBackgroundColor(Color.rgb(255, 0, 127));
                                break;
                        }
                        count++;
                        tv[k][l].setTextColor(Color.WHITE);
                        tvWord.setBackgroundResource(R.drawable.texture);
                        flag[k][l] = true;
                        disable(k,l);
                    }
                });

            }
        }

        try {
            is = getAssets().open("words.txt");
            dictionary = new Dictionary(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //words = dictionary.findWords(board);

        ArrayList<String> temp = dictionary.getWords();
        String s[] = new String[temp.size()];
        solver = new BoggleSolver(temp.toArray(s));
        wordsList = (HashSet)solver.getAllValidWords(Board);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_word, menu);

        MenuItem timerItem = menu.findItem(R.id.counter);
        timerText = (TextView) MenuItemCompat.getActionView(timerItem);
        timerText.setTextSize(20.0f);
        timerText.setPadding(10, 0, 10, 0); //Or something like that...
        timerText.setText("Start");
        timerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0;i<4;i++) {
                    for (int j = 0; j < 4; j++) {
                        tv[i][j].setEnabled(true);
                    }
                }
                bUndo.setEnabled(true);
                bSubmit.setEnabled(true);
                startTimer(180000, 1000); //One tick every second for 30 seconds
            }
        });


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    private void startTimer(long duration, long interval) {

        CountDownTimer timer = new CountDownTimer(duration, interval) {

            @Override
            public void onFinish() {
                timerText.setText("Time's Up");
                disable(-1,-1);
                for(int i=0;i<4;i++) {
                    for(int j=0;j<4;j++) {
                        flag[i][j]=false;
                    }
                }
                bUndo.setEnabled(false);
                bSubmit.setEnabled(false);
                count = 0;
                Iterator<String> i = wordsList.iterator();
                while(i.hasNext()) {
                    tvList.setText(tvList.getText() + "\n" + i.next());
                }
            }

            @Override
            public void onTick(long millisecondsLeft) {
                int secondsLeft = (int) (millisecondsLeft / 1000);
                timerText.setText(secondsToString(secondsLeft));

                final int temp = secondsLeft;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (temp == 1)
                            timerText.setText("00:00:00");
                    }
                }, 1000);
            }

        };

        timer.start();
    }

    private String secondsToString(int improperSeconds) {

        //Seconds must be fewer than are in a day

        Time secConverter = new Time();

        secConverter.hour = 0;
        secConverter.minute = 0;
        secConverter.second = 0;

        secConverter.second = improperSeconds;
        secConverter.normalize(true);

        String hours = String.valueOf(secConverter.hour);
        String minutes = String.valueOf(secConverter.minute);
        String seconds = String.valueOf(secConverter.second);

        if (seconds.length() < 2) {
            seconds = "0" + seconds;
        }
        if (minutes.length() < 2) {
            minutes = "0" + minutes;
        }
        if (hours.length() < 2) {
            hours = "0" + hours;
        }

        if (seconds.equals("00") && hours.equals("00") && minutes.equals("00"))
            seconds = "00";
        String timeString = hours + ":" + minutes + ":" + seconds;
        return timeString;
    }

    private void enable(int a,int b) {
        for(int i=Math.max(a-1,0);i<=Math.min(a+1,3);i++) {
            for(int j=Math.max(b-1,0);j<=Math.min(b+1,3);j++) {
                if(!(i==a && j==b)) {
                    if(!flag[i][j])
                        tv[i][j].setEnabled(true);
                }
            }
        }
    }

    private void disable(int a,int b)
    {
        for(int i=0;i<4;i++) {
            for(int j=0;j<4;j++) {
                tv[i][j].setEnabled(false);
            }
        }
         if(!(a==-1))
             enable(a,b);

    }

    private void clear() {
        for(int i=0;i<4;i++) {
            for(int j=0;j<4;j++) {
                tv[i][j].setEnabled(true);
                tv[i][j].setBackgroundColor(Color.rgb(169, 244, 63));
                tv[i][j].setTextColor(Color.BLACK);
                flag[i][j] = false;
            }
        }
        text = "";
        tvWord.setText(text);
        count = 0;
    }
}





