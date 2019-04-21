package com.android.diceroller;

import android.graphics.Bitmap;

public class ImageItem {
    private Bitmap image;
    private String title;
    private int diceType;

    public ImageItem(Bitmap image, String title, int diceType) {
        super();
        this.image = image;
        this.title = title;
        this.diceType = diceType;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image)
    {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDiceType() {
        return diceType;
    }

    public void setDiceType(int diceType) {
        this.diceType = diceType;
    }
}
