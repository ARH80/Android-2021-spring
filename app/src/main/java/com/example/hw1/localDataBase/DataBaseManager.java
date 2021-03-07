package com.example.hw1.localDataBase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Handler;
import android.os.Message;

import com.example.hw1.MainActivity;
import com.example.hw1.model.Coin;
import com.google.gson.Gson;

import java.util.ArrayList;


public class DataBaseManager implements Runnable {

    private Handler uiHandler;
    private Context context;
    private MainActivity mainActivity;
    private int task;
    private DataBaseHelper db;
    public ArrayList<Integer> coinId;
    public ArrayList<Coin> coins;
    private Coin coin;

    public DataBaseManager(MainActivity mainActivity, Handler uiHandler, Context context) {
        this.uiHandler = uiHandler;
        this.context = context;
        this.db = new DataBaseHelper(context);
        this.coinId = new ArrayList<>();
        this.mainActivity = mainActivity;
        this.coins = new ArrayList<>();
    }

    public void refreshDb(ArrayList<Coin> coins) {
        for (Coin coin : coins) {
            if (coinId.contains(coin.id)) {
                updateDbRow(coin);
            } else {
                insertToDb(coin);
            }
            this.coin = coin;
        }
    }

    public void readFromDb() {
        Cursor res = db.getAllData();
        if (res.getCount() == 0) {
            Message msg = new Message();
            msg.what = 0;
            uiHandler.sendMessage(msg);
            return;
        }
        while (res.moveToNext()) {
            if (!coinId.contains(res.getInt(0))) {
                Coin coin = new Gson().fromJson(res.getString(1), Coin.class);
                coinId.add(res.getInt(0));
                mainActivity.getCoins().add(coin);
            }
        }


        Message msg = new Message();
        msg.what = 2;
        uiHandler.sendMessage(msg);
    }

    @Override
    public void run() {
        switch (task) {
            case 1:
                readFromDb();
                break;
            case 2:
                insertToDb(coin);
                break;
            case 3:
                updateDbRow(coin);
                break;
            case 4:
                refreshDb(coins);
                break;
        }

    }

    private void updateDbRow(Coin coin) {
        db.updateData(String.valueOf(coin.id), new Gson().toJson(coin));
    }

    private void insertToDb(Coin coin) {
        String coinJson = new Gson().toJson(coin);
        db.insertData(coin.id, coinJson);
        coinId.add(coin.id);
    }

    public void setCoin(Coin coin) {
        this.coin = coin;
    }

    public void setTask(int task) {
        this.task = task;
    }
}
