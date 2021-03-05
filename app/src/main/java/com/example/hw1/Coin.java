package com.example.hw1;

public class Coin {

    public int id;
    public String fullName;
    public String symbol;
    public double currentPrice;
    public double percentageChange_1h;
    public double percentageChange_24h;
    public double percentageChange_7d;

    public Coin(int id, String fullName, String symbol, double currentPrice, double percentageChange_1h,
                double percentageChange_24h, double percentageChange_7d) {
        this.id = id;
        this.fullName = fullName;
        this.symbol = symbol;
        this.currentPrice = currentPrice;
        this.percentageChange_1h = percentageChange_1h;
        this.percentageChange_24h = percentageChange_24h;
        this.percentageChange_7d = percentageChange_7d;
    }


}
