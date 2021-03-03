package com.example.hw1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class MainActivity extends AppCompatActivity {

    private ThreadPoolExecutor executerPool;
    private Handler mHandler;
    private Button reloadButton;
    private Button moreCoinsButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.executerPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
        this.mHandler = new Handler();
        this.reloadButton = (Button) findViewById(R.id.reloadBtn);
        this.moreCoinsButton = (Button) findViewById(R.id.moreCoinsBtn);

    }

}