package com.example.hw1.localDataBase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.hw1.Coin;
import com.example.hw1.MainActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.logging.Handler;

public class DataBaseManager implements Runnable {

    private Handler uiHandler;
    private Context context;
    private MainActivity mainActivity;
    private int task;
    private DataBaseHelper db;
    private ArrayList<String> coinId;

    public DataBaseManager(MainActivity mainActivity, Handler uiHandler, Context context, int task) {
        this.uiHandler = uiHandler;
        this.context = context;
        this.task = task;
        this.db = new DataBaseHelper(context);
        this.coinId = new ArrayList<>();
        this.mainActivity = mainActivity;
    }

    public void readFromDb() {
        Cursor res = db.getAllData();
        if (res.getCount() == 0) {
            return;
        }
        while (res.moveToNext()) {
            if (!coinId.contains(res.getString(0))) {
                Coin coin = new Gson().fromJson(res.getString(1), Coin.class);
                coinId.add(res.getString(0));
                mainActivity.getCoins().add(coin);
            }
        }
    }

    @Override
    public void run() {
        switch (task) {
            case 1:
                readFromDb();
                break;
            case 2:

                break;
        }

    }
}
