package com.example.hw1;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MakeApiCall implements Runnable{
    private int coinsToBeShown;
    private final String url = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest";
    private final String apiKey = "3675f481-d4db-4195-b319-549d5a5ce2ee";

    public MakeApiCall(int coinsToBeShown) {
        this.coinsToBeShown = coinsToBeShown;
    }

    @Override
    public void run() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(this.url)
                .addHeader("X-CMC_PRO_API_KEY", this.apiKey)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                //TODO
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                //TODO
            }
        });
    }
}
