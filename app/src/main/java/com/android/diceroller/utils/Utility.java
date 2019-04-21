package com.android.diceroller.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;

import com.android.diceroller.ImageItem;
import com.android.diceroller.data.model.Dice;

import java.util.ArrayList;
import java.util.List;

public class Utility {

    public static int calculateNoOfColumns(Context context, float columnWidthDp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float screenWidthDp = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (screenWidthDp / columnWidthDp + 0.5);
        return noOfColumns;
    }

    public static ArrayList<ImageItem> getData(Context context, List<Dice> dice){
        ArrayList<ImageItem> imageItems = new ArrayList<>();
        for (int i = 0; i < dice.size(); i++) {
            String diceType = "d" + Integer.toString(dice.get(i).getDiceType());
            int type = dice.get(i).getDiceType();
            if(diceType.equals("d100")){
                diceType = "d10";
            }
            int id = context.getResources().getIdentifier(diceType , "drawable", context.getPackageName());
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),id);
            String value = Integer.toString(dice.get(i).getRolledValue());
            imageItems.add(new ImageItem(bitmap, value, type));
        }
        return imageItems;
    }

    public static boolean isConnectedToNetwork(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        boolean isConnected = false;
        if (connectivityManager != null) {
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            isConnected = (activeNetwork != null) && (activeNetwork.isConnectedOrConnecting());
        }

        return isConnected;
    }
}
