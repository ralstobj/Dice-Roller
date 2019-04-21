package com.android.diceroller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.diceroller.utils.Utility;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class LandingFragment extends Fragment implements View.OnClickListener {

    private Button joinButton;
    private Button createButton;

    public LandingFragment(){

    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.landing_screen, container, false);
        joinButton = rootView.findViewById(R.id.landing_join);
        createButton = rootView.findViewById(R.id.landing_create);
        joinButton.setOnClickListener(this);
        createButton.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        if(Utility.isConnectedToNetwork(getContext())) {
            switch (v.getId()) {
                case R.id.landing_join:
                    JoinFragment joinFragment = new JoinFragment();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, joinFragment).addToBackStack(null).commit();
                    break;
                case R.id.landing_create:
                    CreateFragment createFragment = new CreateFragment();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, createFragment).addToBackStack(null).commit();
                    break;
            }
        }
    }
}
