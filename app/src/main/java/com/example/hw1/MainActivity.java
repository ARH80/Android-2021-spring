package com.example.hw1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import com.example.hw1.adaptors.CoinAdaptor;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements CoinAdaptor.OnCoinListener {

    public ArrayList<Coin> coins = new ArrayList<>();
    private UiHandler mHandler;
    private ThreadPoolExecutor executorPool;
    private Button reloadButton;
    private Button moreCoinsButton;
    private int numOfCoins = 10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView coinsRecyclerView = findViewById(R.id.coin_recyclerView);
        coinsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //TODO coins array list need to be initialized
        CoinAdaptor coinAdaptor = new CoinAdaptor(coins, this, this);
        coinsRecyclerView.addItemDecoration(new DividerItemDecoration(coinsRecyclerView.getContext(), DividerItemDecoration.VERTICAL));
        coinsRecyclerView.setAdapter(coinAdaptor);

        this.executorPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
        this.mHandler = new UiHandler();
        mHandler.setContext(this);
        this.reloadButton = (Button) findViewById(R.id.reloadBtn);
        this.moreCoinsButton = (Button) findViewById(R.id.moreCoinsBtn);
        reloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reload();
            }
        });
        moreCoinsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMoreCoins();
            }
        });


    }

    public void reload() {
            this.executorPool.execute(new MakeApiCall(numOfCoins, mHandler, this));
    }


    public void addMoreCoins() {
        numOfCoins += 10;
        reload();
    }

    @Override
    public void onClick(int position) {

    }

    private static class UiHandler extends Handler{
        private WeakReference<Context> mWeakRefContext;

        public void setContext(Context context) {
            this.mWeakRefContext = new WeakReference<Context>(context);
        }

        public WeakReference<Context> getmWeakRefContext() {
            return mWeakRefContext;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case -1:
                    if (mWeakRefContext != null && mWeakRefContext.get() != null){

                    }
            }
        }
    }
}