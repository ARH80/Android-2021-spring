package com.example.hw1.adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hw1.Coin;
import com.example.hw1.R;

import java.util.ArrayList;

public class CoinAdaptor extends RecyclerView.Adapter<CoinAdaptor.ViewHolder> {

    private ArrayList<Coin> data;
    private LayoutInflater inflater;
    private OnCoinListener listener;

    public CoinAdaptor(ArrayList<Coin> data, Context context, OnCoinListener listener) {
        this.data = data;
        this.listener = listener;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.coin_row, parent, false), listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Coin coin = data.get(position);
        //TODO: complete onBindViewHolder method in Coin adaptor
        //holder.coinFullNameTxt.setText();
        //holder.coinCurrentPrice.setText();
        //holder.coin24Volume.setText();
        //holder.coinMarketCap.setText();
        //holder.coin1HChange.setText();
        //holder.coin1DChange.setText();
        //holder.coin1WChange.setText();
        //holder.coinRank.setText();
        //holder.coinImage
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView coinFullNameTxt;
        TextView coinCurrentPrice;
        TextView coin24Volume;
        TextView coinMarketCap;
        TextView coin1HChange;
        TextView coin1DChange;
        TextView coin1WChange;
        TextView coinRank;
        ImageView coinImage;
        OnCoinListener onCoinListener;

        public ViewHolder(@NonNull View itemView, OnCoinListener onCoinListener) {
            super(itemView);
            coinFullNameTxt = itemView.findViewById(R.id.currencyListfullNameTextView);
            coinCurrentPrice = itemView.findViewById(R.id.currencyListCurrPriceTextView);
            coin24Volume = itemView.findViewById(R.id.currencyListVolumeTextView);
            coinMarketCap = itemView.findViewById(R.id.currencyListMarketcapTextView);
            coin1HChange = itemView.findViewById(R.id.oneHourChangeTextView);
            coin1DChange = itemView.findViewById(R.id.dayChangeTextView);
            coin1WChange = itemView.findViewById(R.id.weekChangeTextView);
            coinRank = itemView.findViewById(R.id.rankTextView);
            coinImage = itemView.findViewById(R.id.currencyListCoinImageView);
            this.onCoinListener = onCoinListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onCoinListener.onClick(getAdapterPosition());
        }
    }

    public interface OnCoinListener {
        void onClick(int position);
    }
}
