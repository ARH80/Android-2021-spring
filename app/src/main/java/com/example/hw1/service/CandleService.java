package com.example.hw1.service;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.hw1.CoinActivity;
import com.example.hw1.model.CandleResponseItem;
import com.example.hw1.model.Range;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mikephil.charting.data.CandleEntry;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import lombok.AllArgsConstructor;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@AllArgsConstructor
public class CandleService {
    private static final String YOUR_COIN_IO_API_KEY = "7AB3FC97-BB79-4368-A4E3-A89B03449E4F";
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
    private final OkHttpClient okHttpClient = new OkHttpClient.Builder().callTimeout(5, TimeUnit.SECONDS).build();

    private CoinActivity coinActivity;
    private Handler uiHandler;
    private Context context;
    private final ObjectMapper objectMapper = new ObjectMapper();


    public void getCandles(String symbol, Range range) {


        String miniUrl;
        final String description;
        switch (range) {

            case weekly:
                miniUrl = "period_id=1DAY".concat("&time_end=".concat(getCurrentDate()).concat("&limit=7"));
                description = "Daily candles from now";
                break;

            case oneMonth:
                miniUrl = "period_id=1DAY".concat("&time_end=".concat(getCurrentDate()).concat("&limit=30"));
                description = "Daily candles from now";
                break;

            default:
                miniUrl = "";
                description = "";

        }

        HttpUrl httpUrl = HttpUrl.parse("https://rest.coinapi.io/v1/ohlcv/"
                .concat(symbol)
                .concat("/USD/history?".concat(miniUrl)))
                .newBuilder().build();


        final Request request = new Request.Builder()
                .url(httpUrl)
                .addHeader("X-CoinAPI-Key", YOUR_COIN_IO_API_KEY)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.v("TAG", e.getMessage());
                Message message = new Message();
                message.what = -1;
                uiHandler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.v("TAG", response.toString());
                    Message message = new Message();
                    message.what = -1;
                    uiHandler.sendMessage(message);
                } else {
                    List<CandleResponseItem> items = objectMapper.readValue(response.body().string(), new TypeReference<List<CandleResponseItem>>() {
                    });
                    ArrayList<CandleEntry> candleSticks = new ArrayList<>();
                    for (int i = 0; i < items.size(); i++) {
                        CandleResponseItem item = items.get(i);
                        candleSticks.add(new CandleEntry(i, item.getPriceHigh(), item.getPriceLow(), item.getPriceOpen(), item.getPriceClose()));
                    }
                    Message message = new Message();
                    message.obj = candleSticks;
                    message.what = 1;
                    uiHandler.sendMessage(message);
                }
            }
        });

    }

    private String getCurrentDate() {
        return simpleDateFormat.format(new Date());
    }

}
