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

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class Utility {

    public static int calculateNoOfColumns(Context context, float columnWidthDp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float screenWidthDp = displayMetrics.widthPixels / displayMetrics.density;
        return (int) (screenWidthDp / columnWidthDp + 0.5);
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
            imageItems.add(new ImageItem(bitmap, dice.get(i)));
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

    public static void clearStack(FragmentManager fm) {
        //Here we are clearing back stack fragment entries
        int backStackEntry = fm.getBackStackEntryCount();
        if (backStackEntry > 0) {
            for (int i = 0; i < backStackEntry; i++) {
                fm.popBackStackImmediate();
            }
        }

        //Here we are removing all the fragment that are shown here
        if (fm.getFragments() != null && fm.getFragments().size() > 0) {
            for (int i = 0; i < fm.getFragments().size()-1; i++) {
                Fragment mFragment = fm.getFragments().get(i);
                if (mFragment != null) {
                    fm.beginTransaction().remove(mFragment).commit();
                }
            }
        }
    }
}
