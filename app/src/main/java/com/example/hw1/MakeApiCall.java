package com.example.hw1;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.nfc.Tag;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.http.HttpHeaders;

public class MakeApiCall implements Runnable{

    private int coinsToBeShown;
    private final String url = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest";
    private final String apiKey = "3675f481-d4db-4195-b319-549d5a5ce2ee";
    private Handler uiHandler;
    private Context context;

    public MakeApiCall(int coinsToBeShown, Handler uiHandler, Context context) {
        this.coinsToBeShown = coinsToBeShown;
        this.uiHandler = uiHandler;
        this.context = context;
    }

    @Override
    public void run() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(this.url)
                .addHeader("Accept", "application/json")
                .addHeader("X-CMC_PRO_API_KEY", this.apiKey)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d("error", e.getMessage());
                Message message = new Message();
                message.what = -1;
                uiHandler.sendMessage(message);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Message message = new Message();
                message.obj = response;
                message.what = 1;
                uiHandler.sendMessage(message);
            }
        });
    }
}
