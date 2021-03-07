package com.example.hw1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hw1.adaptors.CoinAdaptor;
import com.example.hw1.model.Coin;
import com.example.hw1.service.CoinService;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;


public class MainActivity extends AppCompatActivity implements CoinAdaptor.OnCoinListener {

    private ArrayList<Coin> coins = new ArrayList<>();
    private UiHandler mHandler;
    private ThreadPoolExecutor executorPool;
    private Button reloadButton;
    private Button moreCoinsButton;
    private CoinService coinService;
    private CoinAdaptor coinAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView coinsRecyclerView = findViewById(R.id.coin_recyclerView);
        coinsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.coinAdaptor = new CoinAdaptor(coins, this, this);
        coinsRecyclerView.addItemDecoration(new DividerItemDecoration(coinsRecyclerView.getContext(), DividerItemDecoration.VERTICAL));
        coinsRecyclerView.setAdapter(coinAdaptor);

        this.executorPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
        this.mHandler = new UiHandler();
        this.mHandler.setContext(this);
        this.reloadButton = findViewById(R.id.reloadBtn);
        this.moreCoinsButton = findViewById(R.id.moreCoinsBtn);
        this.coinService = new CoinService(this, mHandler, this);
        reloadButton.setOnClickListener(view -> reload());
        moreCoinsButton.setOnClickListener(view -> addMoreCoins());


    }

    public void reload() {
        executorPool.execute(coinService);
    }


    public void addMoreCoins() {
        coinService.addMoreCoins();
        reload();
    }

    @Override
    public void onClick(int position) {
        Intent myIntent = new Intent(MainActivity.this, CoinActivity.class);
        myIntent.putExtra("position", position);
        myIntent.putExtra("symbol", coins.get(position).getSymbol());
        MainActivity.this.startActivity(myIntent);
    }

    private class UiHandler extends Handler {
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
                    if (mWeakRefContext != null && mWeakRefContext.get() != null) {
                        Toast.makeText(mWeakRefContext.get(), "Couldn't reach server", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 1:
                    if (mWeakRefContext != null && mWeakRefContext.get() != null) {
//                        result.setText(Arrays.toString(coins.toArray())); // this is just for test
                        coinAdaptor.setData(coins);
                        coinAdaptor.notifyDataSetChanged();
                        Toast.makeText(mWeakRefContext.get(), "Coins are updated", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    }

    public ArrayList<Coin> getCoins() {
        return coins;
    }

    public void setCoins(ArrayList<Coin> coins) {
        this.coins = coins;
    }
}