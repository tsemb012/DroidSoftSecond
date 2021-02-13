package com.websarva.wings.android.droidsoftsecond.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.websarva.wings.android.droidsoftsecond.R;

public class a000_SplashActivity extends AppCompatActivity {

    final Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a000_activity_splash);
        mHandler.postDelayed(mSplashTask, 1200);//TODO 細かい時間調整を入れる。
    }

    @Override
    protected void onStop() {
        super.onStop();
        mHandler.removeCallbacks(mSplashTask);
    }

    private Runnable mSplashTask = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(a000_SplashActivity.this,a001_MainActivity.class);//画面遷移のためのIntentを準備
            startActivity(intent);//実際の画面遷移を開始
            finish();//現在のActivityを削除
        }
    };
}