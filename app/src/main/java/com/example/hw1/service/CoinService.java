package com.example.hw1.service;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.example.hw1.MainActivity;
import com.example.hw1.model.Coin;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CoinService implements Runnable {

    private int coinsToBeShown = 10;
    private final String url = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest";
    private final String apiKey = "3675f481-d4db-4195-b319-549d5a5ce2ee";
    private MainActivity mainActivity;
    private Handler uiHandler;
    private Context context;
    private OkHttpClient okHttpClient = new OkHttpClient.Builder().callTimeout(5, TimeUnit.SECONDS).build();

    public CoinService(MainActivity mainActivity, Handler uiHandler, Context context) {
        this.mainActivity = mainActivity;
        this.uiHandler = uiHandler;
        this.context = context;
    }

    public void addMoreCoins() {
        this.coinsToBeShown += 10;
    }

    @Override
    public void run() {
        HttpUrl.Builder httpBuilder = HttpUrl.parse(url).newBuilder();
        HttpUrl httpUrl = httpBuilder.addQueryParameter("start", "1")
                .addQueryParameter("limit", String.valueOf(coinsToBeShown))
                .addQueryParameter("convert", "USD")
                .build();
        Request request = new Request.Builder()
                .url(httpUrl)
                .addHeader("Accept", "application/json")
                .addHeader("X-CMC_PRO_API_KEY", this.apiKey)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Message message = new Message();
                message.what = -1;
                uiHandler.sendMessage(message);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    ArrayList<Coin> coins = mainActivity.getCoins();
                    coins.clear();
                    JSONObject root = new JSONObject(response.body().string());
                    JSONArray data = root.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject jsonObject = data.getJSONObject(i);
                        int id = jsonObject.getInt("id");
                        String name = jsonObject.getString("name");
                        String symbol = jsonObject.getString("symbol");
                        JSONObject USD = jsonObject.getJSONObject("quote").getJSONObject("USD");
                        double price = USD.getDouble("price");
                        double percentage_change_1h = USD.getDouble("percent_change_1h");
                        double percent_change_24h = USD.getDouble("percent_change_24h");
                        double percent_change_7d = USD.getDouble("percent_change_7d");
                        int rank = jsonObject.getInt("cmc_rank");
                        Coin coin = new Coin(id, name, symbol, price, percentage_change_1h, percent_change_24h, percent_change_7d, rank);
                        coins.add(coin);
                    }
                    mainActivity.setCoins(coins);
                    setCoinsImg();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Message message = new Message();
                message.obj = response;
                message.what = 1;
                uiHandler.sendMessage(message);
            }
        });
    }

    public void setCoinsImg() {
        final ArrayList<Coin> coins = mainActivity.getCoins();
        String metaUrl = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/info";
        StringBuilder idString = new StringBuilder();
        for (int i = 0; i < coins.size(); i++) {
            idString.append(coins.get(i).id);
            if (i != coins.size() - 1) {
                idString.append(",");
            }
        }
        HttpUrl.Builder httpBuilder = HttpUrl.parse(metaUrl).newBuilder();
        HttpUrl httpUrl = httpBuilder.addQueryParameter("id", idString.toString()).build();
        Request request = new Request.Builder()
                .url(httpUrl)
                .addHeader("Accept", "application/json")
                .addHeader("X-CMC_PRO_API_KEY", apiKey)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Message message = new Message();
                message.what = -1;
                uiHandler.sendMessage(message);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    JSONObject root = new JSONObject(response.body().string());
                    JSONObject data = root.getJSONObject("data");
                    for (Coin coin : coins) {
                        coin.imgUrl = data.getJSONObject(String.valueOf(coin.id)).getString("logo");
                        try {
                            URL url = new URL(coin.imgUrl);
                            Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                            coin.setBitmapAsByteArray(image);
                        } catch(IOException e) {
                            System.out.println(e);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
