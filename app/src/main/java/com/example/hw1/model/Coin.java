package com.example.hw1.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
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

}

