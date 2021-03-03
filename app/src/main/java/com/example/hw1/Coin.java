package com.example.hw1;

public class Coin {

    public String fullName;
    public String symbol;
    public String currentPrice;
    public Object img;
    public String percentageChange_1h;
    public String percentageChange_24h;
    public String percentageChange_7d;

    public Coin(String fullName, String symbol, String currentPrice, Object img, String percentageChange_1h, String percentageChange_24h, String percentageChange_7d) {
        this.fullName = fullName;
        this.symbol = symbol;
        this.currentPrice = currentPrice;
        this.img = img;
        this.percentageChange_1h = percentageChange_1h;
        this.percentageChange_24h = percentageChange_24h;
        this.percentageChange_7d = percentageChange_7d;
    }


}
