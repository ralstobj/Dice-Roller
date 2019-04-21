package com.android.diceroller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.diceroller.data.model.Dice;
import com.android.diceroller.data.model.DiceIds;
import com.android.diceroller.data.model.DiceTypes;
import com.android.diceroller.data.model.Session;
import com.android.diceroller.data.model.SessionInfo;
import com.android.diceroller.data.remote.ApiUtils;
import com.android.diceroller.data.remote.DiceService;
import com.android.diceroller.utils.Utility;
import com.google.android.material.bottomappbar.BottomAppBar;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ControllerFragment extends Fragment {

    private RecyclerView rvDice;
    private RecyclerView.LayoutManager rvLayoutManager;
    private ImageAdapter imageAdapter;
    private String sessionId;
    private TextView sessionIdTextView;
    private BottomAppBar bar;
    private DiceService mService;

    public ControllerFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.controller_layout, container, false);
        //Get the sessionId from the arguments
        sessionId = getArguments().getString("sessionId");
        //Find and update the text view to show the sessionId
        sessionIdTextView = rootView.findViewById(R.id.sessionId_text);
        sessionIdTextView.setText("Session Code: " + sessionId);
        //Create Button App Bar
        bar = rootView.findViewById(R.id.bar);
        Toolbar toolbar = rootView.findViewById(R.id.controller_toolbar);
        toolbar.inflateMenu(R.menu.end_session_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.delete_session:
                        //TODO: Call end session
                        return true;
                    default:
                        return false;
                }
            }
        });
        //Connect the Dice Service to the interface
        mService = ApiUtils.getDiceService();
        //Setup adapter
        rvDice = rootView.findViewById(R.id.gridView);
        int mNoOfColumns = Utility.calculateNoOfColumns(rootView.getContext(),110);
        rvLayoutManager = new GridLayoutManager(getActivity(),mNoOfColumns);
        rvDice.setLayoutManager(rvLayoutManager);
        return rootView;
    }


    public void deleteSession(String token, String sessionId) {
        SessionInfo sessionInfo = new SessionInfo(token,sessionId);
        mService.deleteSession(sessionInfo).enqueue(new Callback<Session>() {
            @Override
            public void onResponse(Call<Session> call, Response<Session> response) {

                if(response.isSuccessful()) {
                    //TODO: Check if fail or success and do something
                }else {
                    int statusCode  = response.code();
                }
            }

            @Override
            public void onFailure(Call<Session> call, Throwable t) {

            }
        });
    }

    public void addDice(String token, String sessionId, List<Integer> types){
        DiceTypes diceTypes = new DiceTypes(token, types);
        mService.addDice(sessionId,diceTypes).enqueue(new Callback<Session>() {
            @Override
            public void onResponse(Call<Session> call, Response<Session> response) {

                if(response.isSuccessful()) {
                    imageAdapter = new ImageAdapter(getActivity(), getData(response.body().getDice()));
                    imageAdapter.setHasStableIds(true);
                    rvDice.setAdapter(imageAdapter);
                }else {
                    int statusCode  = response.code();
                }
            }

            @Override
            public void onFailure(Call<Session> call, Throwable t) {

            }
        });
    }
    public void rollDice(String token, String sessionId, List<Integer>ids){
        DiceIds diceIds = new DiceIds(token, ids);
        mService.rollDice(sessionId,diceIds).enqueue(new Callback<Session>() {
            @Override
            public void onResponse(Call<Session> call, Response<Session> response) {

                if(response.isSuccessful()) {
                    imageAdapter = new ImageAdapter(getActivity(), getData(response.body().getDice()));
                    imageAdapter.setHasStableIds(true);
                    rvDice.setAdapter(imageAdapter);
                }else {
                    int statusCode  = response.code();
                }
            }

            @Override
            public void onFailure(Call<Session> call, Throwable t) {

            }
        });
    }
    public void deleteDice(String token, String sessionId, List<Integer>ids){
        DiceIds diceIds = new DiceIds(token,ids);
        mService.deleteDice(sessionId,diceIds).enqueue(new Callback<Session>() {
            @Override
            public void onResponse(Call<Session> call, Response<Session> response) {

                if(response.isSuccessful()) {
                    imageAdapter = new ImageAdapter(getActivity(), getData(response.body().getDice()));
                    imageAdapter.setHasStableIds(true);
                    rvDice.setAdapter(imageAdapter);
                }else {
                    int statusCode  = response.code();
                }
            }

            @Override
            public void onFailure(Call<Session> call, Throwable t) {

            }
        });
    }

    private ArrayList<ImageItem> getData(List<Dice> dice) {
        return Utility.getData(getContext(), dice);
    }

}
