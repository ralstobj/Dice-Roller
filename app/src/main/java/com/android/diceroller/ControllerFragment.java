package com.android.diceroller;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.diceroller.data.model.CreatorInfo;
import com.android.diceroller.data.model.Dice;
import com.android.diceroller.data.model.DiceIds;
import com.android.diceroller.data.model.DiceTypes;
import com.android.diceroller.data.model.Session;
import com.android.diceroller.data.model.SessionInfo;
import com.android.diceroller.data.remote.ApiUtils;
import com.android.diceroller.data.remote.DiceService;
import com.android.diceroller.utils.Utility;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ControllerFragment extends Fragment implements View.OnClickListener, NumberPickerDialog.OnDiceAdded, DiceViewHolder.DiceActionListener {

    private RecyclerView rvDice;
    private RecyclerView.LayoutManager rvLayoutManager;
    private ImageAdapter imageAdapter;
    private String sessionId;
    private String token;
    private TextView sessionIdTextView;
    private BottomAppBar bar;
    private FloatingActionButton rollButton;
    private Button addButton;
    private Button deleteButton;
    private DiceService mService;
    private final List<Integer> diceIdsList = new ArrayList<>();

    public ControllerFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.controller_layout, container, false);
        //Get the sessionId from the arguments
        sessionId = getArguments().getString("sessionId");
        token = getArguments().getString("token");
        initialSetup(rootView);
        //Connect the Dice Service to the interface
        mService = ApiUtils.getDiceService();
        getDice(sessionId);
        return rootView;
    }

    private void initialSetup(View v){
        sessionIdTextView = v.findViewById(R.id.sessionId_text);
        String text = getResources().getString(R.string.session_code) + " " + sessionId;
        sessionIdTextView.setText(text);
        addButton = v.findViewById(R.id.add_dice);
        deleteButton = v.findViewById(R.id.delete_dice);
        rollButton = v.findViewById(R.id.roll_dice);
        addButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);
        rollButton.setOnClickListener(this);
        bar = v.findViewById(R.id.bar);
        Toolbar toolbar = v.findViewById(R.id.controller_toolbar);
        toolbar.inflateMenu(R.menu.end_session_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.delete_session:
                        deleteSession(token, sessionId);
                        return true;
                    default:
                        return false;
                }
            }
        });
        rvDice = v.findViewById(R.id.gridView);
        int mNoOfColumns = Utility.calculateNoOfColumns(v.getContext(),120);
        rvLayoutManager = new GridLayoutManager(getActivity(),mNoOfColumns);
        rvDice.setLayoutManager(rvLayoutManager);

    }
    public void deleteSession(String token, String sessionId) {
        SessionInfo sessionInfo = new SessionInfo(token,sessionId);
        mService.deleteSession(sessionInfo).enqueue(new Callback<Session>() {
            @Override
            public void onResponse(Call<Session> call, Response<Session> response) {

                if(response.isSuccessful()) {
                    String status = response.body().getStatus();
                    if(status.equals("OK")){
                        Utility.clearStack(getFragmentManager());
                    }
                }else {
                    int statusCode  = response.code();
                }
            }

            @Override
            public void onFailure(Call<Session> call, Throwable t) {

            }
        });
    }

    private void addDice(String token, String sessionId, List<Integer> types){
        DiceTypes diceTypes = new DiceTypes(token, types);
        mService.addDice(sessionId,diceTypes).enqueue(new Callback<Session>() {
            @Override
            public void onResponse(Call<Session> call, Response<Session> response) {

                if(response.isSuccessful()) {
                    imageAdapter = new ImageAdapter(getData(response.body().getDice()), ControllerFragment.this);
                    rvDice.setAdapter(imageAdapter);
                    diceIdAdder(response.body().getDice());
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
                    imageAdapter = new ImageAdapter(getData(response.body().getDice()), ControllerFragment.this);
                    rvDice.setAdapter(imageAdapter);
                    diceIdsList.clear();
                    diceIdAdder(response.body().getDice());
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
                    imageAdapter = new ImageAdapter(getData(response.body().getDice()), ControllerFragment.this);
                    rvDice.setAdapter(imageAdapter);
                    diceIdsList.clear();
                    diceIdAdder(response.body().getDice());
                }else {
                    int statusCode  = response.code();
                }
            }

            @Override
            public void onFailure(Call<Session> call, Throwable t) {

            }
        });
    }
    private void getDice(String sessionId) {
        mService.getDice(sessionId).enqueue(new Callback<Session>() {
            @Override
            public void onResponse(Call<Session> call, Response<Session> response) {

                if(response.isSuccessful()) {
                    String status = response.body().getStatus();
                    imageAdapter = new ImageAdapter(getData(response.body().getDice()), ControllerFragment.this);
                    rvDice.setAdapter(imageAdapter);
                    diceIdsList.clear();
                    diceIdAdder(response.body().getDice());
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

    @Override
    public void onClick(View v) {
        if(Utility.isConnectedToNetwork(getContext())) {
            switch (v.getId()) {
                case R.id.roll_dice:
                    rollDice(token, sessionId, diceIdsList);
                    break;
                case R.id.add_dice:
                    showNumberPicker(v);
                    break;
                case R.id.delete_dice:
                    deleteDice(token, sessionId, diceIdsList);
                    break;
            }
        }
    }


    private void showNumberPicker(View view){
        NumberPickerDialog newFragment = new NumberPickerDialog();
        newFragment.setDiceAdder(this);
        newFragment.show(getFragmentManager(), "dice picker");
    }


    @Override
    public void diceAdded(int type, int quantity) {
        int diceType;
        switch (type){
            case 1:
                diceType = 20;
                break;
            case 2:
                diceType = 6;
                break;
            case 3:
                diceType = 8;
                break;
            case 4:
                diceType = 10;
                break;
            case 5:
                diceType = 12;
                break;
            case 6:
                diceType = 4;
                break;
            default:
                diceType = 20;
                break;
        }
        List<Integer> types = new ArrayList<Integer>();
        for(int i = 0; i < quantity; i++ ){
            types.add(diceType);
        }
        addDice(token,sessionId,types);
    }

    private void diceIdAdder(List<Dice> holder){
        diceIdsList.clear();
        for(Dice ini : holder)
            diceIdsList.add(ini.getDiceId());
    }

    @Override
    public void getDice(Dice d) {
        List<Integer> die = new ArrayList<>();
        die.add(d.getDiceId());
        rollDice(token,sessionId,die);
    }

    @Override
    public void deleteDice(Dice d) {
        List<Integer> die = new ArrayList<>();
        die.add(d.getDiceId());
        deleteDice(token, sessionId, die);
    }
}
