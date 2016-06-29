package com.sight.water.whaterviewdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.sight.water.whaterviewdemo.view.HeartView;

/**
 * 点赞 的页面
 */
public class HeartActivity extends AppCompatActivity {


    HeartView heartView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart);

        heartView = (HeartView) findViewById(R.id.heart);
        heartView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                heartView.addHeart();
            }
        });
    }
}
