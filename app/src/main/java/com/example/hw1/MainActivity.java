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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.hw1.adaptors.CoinAdaptor;
import com.example.hw1.localDataBase.DataBaseManager;
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
    private Button moreCoinsButton;
    private CoinService coinService;
    private CoinAdaptor coinAdaptor;
    private DataBaseManager dbManager;
    private SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView coinsRecyclerView = findViewById(R.id.coin_recyclerView);
        coinsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.coinAdaptor = new CoinAdaptor(coins, this, this);
        coinsRecyclerView.addItemDecoration(new DividerItemDecoration(coinsRecyclerView.getContext(), DividerItemDecoration.VERTICAL));
        coinsRecyclerView.setAdapter(coinAdaptor);

        this.executorPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
        this.mHandler = new UiHandler();
        this.mHandler.setContext(this);
        this.moreCoinsButton = findViewById(R.id.moreCoinsBtn);
        this.coinService = new CoinService(this, mHandler, this);
        this.dbManager = DataBaseManager.getInstance(this, mHandler, this);
        moreCoinsButton.setOnClickListener(view -> addMoreCoins());
        refreshLayout = findViewById(R.id.swiperefresh);
        refreshLayout.setOnRefreshListener(this::reload);

        readFromDb();
    }

    public void reload() {
        refreshLayout.setRefreshing(true);
        moreCoinsButton.setEnabled(false);
        executorPool.execute(() -> coinService.getNewCoins());
    }

    public void refreshDb(ArrayList<Coin> coins) {
        dbManager.coins = coins;
        dbManager.setTask(4);
        executorPool.execute(dbManager);
    }

    public void readFromDb() {
        dbManager.setTask(1);
        executorPool.execute(dbManager);
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
                        readFromDb();
                    }
                    break;
                case 0:
                    if (mWeakRefContext != null && mWeakRefContext.get() != null) {
                        Toast.makeText(mWeakRefContext.get(), "DB is empty reload pls", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 1:
                    if (mWeakRefContext != null && mWeakRefContext.get() != null) {
                        coinAdaptor.setData(coins);
                        coinAdaptor.notifyDataSetChanged();
                        Toast.makeText(mWeakRefContext.get(), "Coins are updated", Toast.LENGTH_SHORT).show();
                        refreshDb(coinAdaptor.getData());
                    }
                    break;
                case 2:
                    if (mWeakRefContext != null && mWeakRefContext.get() != null) {
                        sortDb(coins, coins.size());
                        coinAdaptor.setData(coins);
                        coinAdaptor.notifyDataSetChanged();
                        Toast.makeText(mWeakRefContext.get(), "Data is presented by DB", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 3:
                    if (mWeakRefContext != null && mWeakRefContext.get() != null) {
                        coinAdaptor.setData(coins);
                        coinAdaptor.notifyDataSetChanged();
                        Toast.makeText(mWeakRefContext.get(), "DB is updated", Toast.LENGTH_SHORT).show();
                        refreshDb(coinAdaptor.getData());
                    }
                    break;
            }
            refreshLayout.setRefreshing(false);
            moreCoinsButton.setEnabled(true);
        }
    }

    public void sortDb(ArrayList<Coin> input, int length) {
        if (length <= 1) {
            return;
        }

        sortDb(input, length - 1);

        int last = input.get(length - 1).rank;
        Coin coin = input.get(length - 1);
        int j = length - 2;
        while (j >= 0 && input.get(j).rank > last) {
            input.set(j+1, input.get(j));
            j--;
        }
        input.set(j+1, coin);
    }

    public ArrayList<Coin> getCoins() {
        return coins;
    }

    public void setCoins(ArrayList<Coin> coins) {
        this.coins = coins;
    }
}