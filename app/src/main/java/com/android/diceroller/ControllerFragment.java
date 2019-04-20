package com.android.diceroller;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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
import com.android.diceroller.utils.ColumnCalculator;
import com.google.android.material.bottomappbar.BottomAppBar;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
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
        ((AppCompatActivity)getActivity()).setSupportActionBar(bar);
        //Connect the Dice Service to the interface
        mService = ApiUtils.getDiceService();
        //Setup adapter
        rvDice = rootView.findViewById(R.id.gridView);
        rvDice.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        int mNoOfColumns = ColumnCalculator.calculateNoOfColumns(rootView.getContext(),110);
        rvLayoutManager = new GridLayoutManager(getActivity(),mNoOfColumns);
        rvDice.setLayoutManager(rvLayoutManager);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.bottom_menu, menu);
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
        ArrayList<ImageItem> imageItems = new ArrayList<>();
        for (int i = 0; i < dice.size(); i++) {
            String diceType = "d" + Integer.toString(dice.get(i).getDiceType());
            if(diceType.equals("d100")){
                diceType = "d10";
            }
            int id = getResources().getIdentifier(diceType , "drawable", getActivity().getPackageName());
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),id);
            String value = Integer.toString(dice.get(i).getRolledValue());
            imageItems.add(new ImageItem(bitmap, value));
        }
        return imageItems;
    }
}
