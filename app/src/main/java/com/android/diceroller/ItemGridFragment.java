package com.android.diceroller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.android.diceroller.data.model.CreatorInfo;
import com.android.diceroller.data.model.Dice;
import com.android.diceroller.data.model.DiceIds;
import com.android.diceroller.data.model.DiceTypes;
import com.android.diceroller.data.model.Session;
import com.android.diceroller.data.model.SessionInfo;
import com.android.diceroller.data.model.Username;
import com.android.diceroller.data.remote.ApiUtils;
import com.android.diceroller.data.remote.DiceService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class ItemGridFragment extends Fragment implements AdapterView.OnItemClickListener{

    private OnFragmentInteractionListener mListener;
    private GridView gridView;
    private GridViewAdapter gridAdapter;
    private BroadcastReceiver receiver;
    private String currentSession = "R6M6M";
    private DiceService mService;
    public ItemGridFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.grid, container, false);
        currentSession = getArguments().getString("sessionId");
        mService = ApiUtils.getDiceService();
        FirebaseMessaging.getInstance().subscribeToTopic("diceUser")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = getActivity().getString(R.string.msg_subscribed);
                        if (!task.isSuccessful()) {
                            msg = getActivity().getString(R.string.msg_subscribe_failed);
                        }
                        Log.d(TAG, msg);
                    }
                });
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                try {
                    String sessionId= intent.getStringExtra("sessionId");
                    if(sessionId.equals(currentSession)) {
                        System.out.println("Hello world");
                        getDice(sessionId);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver((receiver),
                new IntentFilter("firebase")
        );

        getDice(currentSession);
        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(receiver);
        FirebaseMessaging.getInstance().unsubscribeFromTopic("diceUser");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mListener.onItemSelected(id);
        }
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

    public void getDice(String sessionId) {
        mService.getDice(sessionId).enqueue(new Callback<Session>() {
            @Override
            public void onResponse(Call<Session> call, Response<Session> response) {

                if(response.isSuccessful()) {
                    gridView = getView().findViewById(R.id.gridView);
                    gridAdapter = new GridViewAdapter(getActivity(), R.layout.grid_item_layout, getData(response.body().getDice()));
                    gridView.setAdapter(gridAdapter);
                    //Log.d("MainActivity", "posts loaded from API");
                }else {
                    int statusCode  = response.code();
                }
            }

            @Override
            public void onFailure(Call<Session> call, Throwable t) {
            }
        });
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

    public void addDice(String token, String sessionId, List<Integer>types){
        DiceTypes diceTypes = new DiceTypes(token, types);
        mService.addDice(sessionId,diceTypes).enqueue(new Callback<Session>() {
            @Override
            public void onResponse(Call<Session> call, Response<Session> response) {

                if(response.isSuccessful()) {
                    gridView = getView().findViewById(R.id.gridView);
                    gridAdapter = new GridViewAdapter(getActivity(), R.layout.grid_item_layout, getData(response.body().getDice()));
                    gridView.setAdapter(gridAdapter);
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
                    gridView = getView().findViewById(R.id.gridView);
                    gridAdapter = new GridViewAdapter(getActivity(), R.layout.grid_item_layout, getData(response.body().getDice()));
                    gridView.setAdapter(gridAdapter);
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
                    gridView = getView().findViewById(R.id.gridView);
                    gridAdapter = new GridViewAdapter(getActivity(), R.layout.grid_item_layout, getData(response.body().getDice()));
                    gridView.setAdapter(gridAdapter);
                }else {
                    int statusCode  = response.code();
                }
            }

            @Override
            public void onFailure(Call<Session> call, Throwable t) {

            }
        });
    }
    public void createSession(String user){
        Username username = new Username(user);
        mService.createSession(username).enqueue(new Callback<CreatorInfo>() {
            @Override
            public void onResponse(Call<CreatorInfo> call, Response<CreatorInfo> response) {

                if(response.isSuccessful()) {
                    //TODO Get token and sessionId and do something
                    //response.body().getSessionId();
                    //response.body().getToken();
                }else {
                    int statusCode  = response.code();
                }
            }

            @Override
            public void onFailure(Call<CreatorInfo> call, Throwable t) {

            }
        });
    }
}
