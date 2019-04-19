package com.android.diceroller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.bottomappbar.BottomAppBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class ControllerFragment extends Fragment {
    String sessionId;
    TextView sessionIdTextView;
    BottomAppBar bar;
    public ControllerFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.controller_layout, container, false);
        sessionIdTextView = rootView.findViewById(R.id.sessionId_text);
        sessionId = getArguments().getString("sessionId");
        sessionIdTextView.setText("Session Code: " + sessionId);
        bar = rootView.findViewById(R.id.bar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(bar);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.bottom_menu, menu);
    }
}
