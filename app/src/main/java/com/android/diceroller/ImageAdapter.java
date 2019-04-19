package com.android.diceroller;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    Context mainActivityContext;
    private ArrayList<ImageItem> imageItemArrayList = new ArrayList<ImageItem>();

    public ImageAdapter(Context mainActivityContext, ArrayList<ImageItem> imageArrayList) {
        this.mainActivityContext = mainActivityContext;
        this.imageItemArrayList = imageArrayList;
    }

    //TODO(15): Complete each method as mentioned below


    @Override
    public ImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.grid_item_layout, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Bitmap diceImage = imageItemArrayList.get(position).getImage();
        String currentRolledValue = imageItemArrayList.get(position).getTitle();

        holder.diceRolledValue.setText(currentRolledValue);
        holder.diceImage.setImageBitmap(diceImage);
    }


    @Override
    public int getItemCount() {
        //Returns total number of rows inside recycler view

        return imageItemArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //Used to work with the elements of our custom UI.

        RelativeLayout relativeDiceItem;

        ImageView diceImage;
        TextView diceRolledValue;

        public ViewHolder(View itemView) {
            super(itemView);

            diceRolledValue = itemView.findViewById(R.id.dice_type_text);
            diceImage =  itemView.findViewById(R.id.dice_image);

            relativeDiceItem = itemView.findViewById(R.id.dice_grid_layout);

            relativeDiceItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mainActivityContext,
                            "You clicked item number: "+ getAdapterPosition(),
                            Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
}
