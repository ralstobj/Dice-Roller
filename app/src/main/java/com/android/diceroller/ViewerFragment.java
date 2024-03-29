package com.android.diceroller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.diceroller.data.model.Dice;
import com.android.diceroller.data.model.Session;
import com.android.diceroller.data.remote.ApiUtils;
import com.android.diceroller.data.remote.DiceService;
import com.android.diceroller.utils.Utility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class ViewerFragment extends Fragment {

    private RecyclerView rvDice;
    private RecyclerView.LayoutManager rvLayoutManager;
    private TextView sessionIdTextView;
    private ImageAdapter imageAdapter;
    private BroadcastReceiver receiver;
    private String currentSession = "R6M6M";
    private DiceService mService;

    public ViewerFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.viewer_layout, container, false);
        currentSession = getArguments().getString("sessionId");
        String diceString = getArguments().getString("dice");
        Gson gson = new Gson();
        Type diceListType = new TypeToken<ArrayList<Dice>>(){}.getType();
        List<Dice> dice = gson.fromJson(diceString,diceListType);
        initialSetup(rootView,dice);
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
                    String sessionId = intent.getStringExtra("sessionId");
                    String status = intent.getStringExtra("status");
                    if(status.equals("active")){
                        if(sessionId.equals(currentSession)){
                            getDice(sessionId);
                        }
                    } else{
                        showSessionEndedDialog(rootView);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver((receiver),
                new IntentFilter("firebase")
        );
        return rootView;
    }

    private void initialSetup(View v, List<Dice> dice){
        sessionIdTextView = v.findViewById(R.id.sessionId_viewer_text);
        String text = getResources().getString(R.string.session_code) + " " + currentSession;
        sessionIdTextView.setText(text);
        Toolbar toolbar = v.findViewById(R.id.viewer_toolbar);
        toolbar.inflateMenu(R.menu.exit_session_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.exit_session:
                        Utility.clearStack(getFragmentManager());
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
        imageAdapter = new ImageAdapter(getData(dice), null);
        rvDice.setAdapter(imageAdapter);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(receiver);
        FirebaseMessaging.getInstance().unsubscribeFromTopic("diceUser");
    }

    private ArrayList<ImageItem> getData(List<Dice> dice) {
        return Utility.getData(getContext(), dice);
    }

    private void getDice(String sessionId) {
        mService.getDice(sessionId).enqueue(new Callback<Session>() {
            @Override
            public void onResponse(Call<Session> call, Response<Session> response) {

                if(response.isSuccessful()) {
                    String status = response.body().getStatus();
                        imageAdapter = new ImageAdapter(getData(response.body().getDice()), null);
                        rvDice.setAdapter(imageAdapter);
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
    private void showSessionEndedDialog(View view){
        SessionEnded sessionEnded = new SessionEnded();
        sessionEnded.show(getFragmentManager(), "session ended");
    }
}
