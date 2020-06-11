package com.ambush.wordtitans;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;


public class SplashScreenActivity extends Activity {
    Animation anim,anim2;
    ImageView iv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        anim= new RotateAnimation(0,360,100,100);
        anim.setDuration(1500);
        //anim2 = new AlphaAnimation(0.0f,0.1f);
        //anim2.setDuration(1500);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        iv = (ImageView)findViewById(R.id.iv);
        iv.setImageResource(R.drawable.image);
        //iv.startAnimation(anim2);
        iv.startAnimation(anim);
        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(2500);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                    Intent intent = new Intent(SplashScreenActivity.this,MainActivity.class);
                    startActivity(intent);
                }
            }
        };
        timerThread.start();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }

}

