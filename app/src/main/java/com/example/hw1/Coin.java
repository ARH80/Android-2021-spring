package com.example.hw1;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

public class Coin {

    public int id;
    public String fullName;
    public String symbol;
    public double currentPrice;
    public double percentageChange_1h;
    public double percentageChange_24h;
    public double percentageChange_7d;
    public String imgUrl;
    public int rank;
    public byte[] imageBytes;

    public Coin(int id, String fullName, String symbol, double currentPrice, double percentageChange_1h,
                double percentageChange_24h, double percentageChange_7d, int rank) {
        this.id = id;
        this.rank = rank;
        this.fullName = fullName;
        this.symbol = symbol;
        this.currentPrice = currentPrice;
        this.percentageChange_1h = percentageChange_1h;
        this.percentageChange_24h = percentageChange_24h;
        this.percentageChange_7d = percentageChange_7d;
    }

    public void setBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        imageBytes =  outputStream.toByteArray();
    }

    @Override
    public String toString() {
        return "Coin{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", symbol='" + symbol + '\'' +
                ", currentPrice=" + currentPrice +
                ", percentageChange_1h=" + percentageChange_1h +
                ", percentageChange_24h=" + percentageChange_24h +
                ", percentageChange_7d=" + percentageChange_7d +
                '}';
    }
}
