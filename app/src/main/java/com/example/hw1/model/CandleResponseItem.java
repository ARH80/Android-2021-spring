package com.example.hw1.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CandleResponseItem {
    @JsonProperty("time_period_start")
    private String timePeriodStart;
    @JsonProperty("time_period_end")
    private String timePeriodEnd;
    @JsonProperty("time_open")
    private String timeOpen;
    @JsonProperty("time_close")
    private String timeClose;
    @JsonProperty("price_open")
    private float priceOpen;
    @JsonProperty("price_high")
    private float priceHigh;
    @JsonProperty("price_low")
    private float priceLow;
    @JsonProperty("price_close")
    private float priceClose;
    @JsonProperty("volume_traded")
    private float volumeTraded;
    @JsonProperty("trades_count")
    private long tradesCount;

}
