package com.example.hw1;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hw1.model.Range;
import com.example.hw1.service.CandleService;
import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.formatter.DefaultAxisValueFormatter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class CoinActivity extends AppCompatActivity {
    private CandleService candleService;
    private ThreadPoolExecutor executorPool;

    private Button monthBtn;
    private Button weekBtn;

    private String symbol;
    private Range range = Range.oneMonth;
    private CandleStickChart candleStickChart;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        this.symbol = intent.getStringExtra("symbol");

        setContentView(R.layout.activity_coin);
        // adding chart to activity
        candleStickChart = findViewById(R.id.candle_stick_chart);
        changeSettingOfChart();
        this.monthBtn = findViewById(R.id.monthCandleBtn);
        this.weekBtn = findViewById(R.id.weakCandleBtn);
        monthBtn.setOnClickListener(view -> onChartPeriodChange(Range.oneMonth));
        weekBtn.setOnClickListener(view -> onChartPeriodChange(Range.weekly));

        //get data
        ChartHandler handler = new ChartHandler();
        handler.setContext(this);
        this.executorPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
        candleService = new CandleService(this, handler, this);
        executorPool.execute(() -> candleService.getCandles(symbol, range));

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("getting data....");
        progressDialog.show();
    }

    private void onChartPeriodChange(Range actionRange) {
        if (!actionRange.equals(this.range)) {
            this.range = actionRange;
            monthBtn.setEnabled(false);
            weekBtn.setEnabled(false);
            executorPool.execute(() -> candleService.getCandles(this.symbol, this.range));
        }
    }

    private void changeSettingOfChart() {
        candleStickChart.setHighlightPerDragEnabled(true);

        candleStickChart.setDrawBorders(true);

        candleStickChart.setBorderColor(getResources().getColor(R.color.lightGrey));

        YAxis yAxis = candleStickChart.getAxisLeft();
        YAxis rightAxis = candleStickChart.getAxisRight();
        yAxis.setDrawGridLines(false);
        rightAxis.setDrawGridLines(false);
        candleStickChart.requestDisallowInterceptTouchEvent(true);

        XAxis xAxis = candleStickChart.getXAxis();
        xAxis.setDrawGridLines(false);// disable x axis grid lines
        xAxis.setDrawLabels(false);
        rightAxis.setTextColor(Color.WHITE);
        yAxis.setDrawLabels(false);
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setAvoidFirstLastClipping(true);

        Legend l = candleStickChart.getLegend();
        l.setEnabled(false);
    }

    private class ChartHandler extends Handler {
        private WeakReference<Context> mWeakRefContext;

        public void setContext(Context context) {
            this.mWeakRefContext = new WeakReference<Context>(context);
        }

        public WeakReference<Context> getmWeakRefContext() {
            return mWeakRefContext;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case -1:
                    if (mWeakRefContext != null && mWeakRefContext.get() != null) {
                        Toast.makeText(mWeakRefContext.get(), "Couldn't reach server", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                    break;
                case 1:
                    ArrayList<CandleEntry> yValsCandleStick = (ArrayList<CandleEntry>) msg.obj;
                    CandleDataSet set1 = new CandleDataSet(yValsCandleStick, "DataSet 1");
                    set1.setColor(Color.rgb(80, 80, 80));
                    set1.setShadowColor(getResources().getColor(R.color.lightGrey));
                    set1.setShadowWidth(0.8f);
                    set1.setDecreasingColor(getResources().getColor(R.color.colorRed));
                    set1.setDecreasingPaintStyle(Paint.Style.FILL);
                    set1.setIncreasingColor(getResources().getColor(R.color.colorAccent));
                    set1.setIncreasingPaintStyle(Paint.Style.FILL);
                    set1.setNeutralColor(Color.LTGRAY);
                    set1.setDrawValues(false);

                    CandleData data = new CandleData(set1);

                    candleStickChart.setData(data);
                    candleStickChart.invalidate();
                    progressDialog.dismiss();
                    break;
            }
            weekBtn.setEnabled(true);
            monthBtn.setEnabled(true);
        }
    }

}