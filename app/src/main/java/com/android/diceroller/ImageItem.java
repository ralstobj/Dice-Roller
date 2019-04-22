package com.android.diceroller;

import android.graphics.Bitmap;

import com.android.diceroller.data.model.Dice;

public class ImageItem {
    private Bitmap image;
    private Dice dice;

    public ImageItem(Bitmap image, Dice dice) {
        super();
        this.image = image;
        this.dice = dice;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image)
    {
        this.image = image;
    }

    public Dice getDice() {
        return dice;
    }

    public void setDice(Dice dice) {
        this.dice = dice;
    }
}
