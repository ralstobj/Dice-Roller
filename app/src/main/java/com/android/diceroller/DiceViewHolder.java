package com.android.diceroller;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.diceroller.data.model.Dice;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class DiceViewHolder extends RecyclerView.ViewHolder {
    //Used to work with the elements of our custom UI.

    ConstraintLayout relativeDiceItem;
    DiceActionListener listener;
    ImageView diceImage;
    TextView diceRolledValue;
    Dice dice;
    Context c;
    public interface DiceActionListener{
        void getDice(Dice d);
        void deleteDice(Dice d);
    }

    public DiceViewHolder(View itemView, final DiceActionListener listener) {
        super(itemView);
        this.listener = listener;
        diceRolledValue = itemView.findViewById(R.id.dice_type_text);
        diceImage =  itemView.findViewById(R.id.dice_image);
        c = itemView.getContext();
        relativeDiceItem = itemView.findViewById(R.id.dice_grid_layout);
        if(this.listener != null) {
            relativeDiceItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.getDice(dice);
                }
            });
            relativeDiceItem.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.deleteDice(dice);
                    return true;
                }
            });
        }

    }

    public void setItem(ImageItem item) {
        this.dice = item.getDice();
        this.diceImage.setImageBitmap(item.getImage());
        this.diceRolledValue.setText(Integer.toString(item.getDice().getRolledValue()));
        if(this.dice.getDiceType() == 20) {
            if (this.dice.getRolledValue() == 20) {
                this.diceImage.setBackgroundColor(c.getResources().getColor(R.color.Gold));
            } else if (this.dice.getRolledValue() == 1) {
                this.diceImage.setBackgroundColor(c.getResources().getColor(R.color.Crimson));
            }
            else{
                this.diceImage.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }
        }else{
            this.diceImage.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
    }
}
