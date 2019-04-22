package com.android.diceroller;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ImageAdapter extends RecyclerView.Adapter {

    private DiceViewHolder.DiceActionListener diceListener;
    private ArrayList<ImageItem> imageItemArrayList = new ArrayList<ImageItem>();

    public ImageAdapter(ArrayList<ImageItem> imageArrayList, DiceViewHolder.DiceActionListener listener) {
        this.diceListener = listener;
        this.imageItemArrayList = imageArrayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_layout, parent, false);

        return new DiceViewHolder(v,diceListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DiceViewHolder vh = (DiceViewHolder) holder;
        vh.setItem(imageItemArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return imageItemArrayList.size();
    }
}
