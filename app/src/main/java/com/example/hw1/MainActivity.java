package com.example.hw1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import com.example.hw1.adaptors.CoinAdaptor;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements CoinAdaptor.OnCoinListener {

    public ArrayList<Coin> coins = new ArrayList<>();

    private ThreadPoolExecutor executorPool;
    private Handler mHandler;
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
        this.mHandler = new Handler();
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
        executerPool.execute(new MakeApiCall(this.numOfCoins));
    }


    public void addMoreCoins() {
        this.numOfCoins += 10;
        reload();
    }

    @Override
    public void onClick(int position) {
        //TODO complete this method


    }
}