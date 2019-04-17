package com.android.diceroller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }
            ItemGridFragment gridFragment = new ItemGridFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, gridFragment).commit();
        }
    }

    @Override
    public void onItemSelected(long id) {

    }
}



