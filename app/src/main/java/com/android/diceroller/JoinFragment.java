package com.android.diceroller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.diceroller.data.model.Session;
import com.android.diceroller.data.remote.ApiUtils;
import com.android.diceroller.data.remote.DiceService;
import com.android.diceroller.utils.Utility;
import com.google.gson.Gson;

import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JoinFragment extends Fragment implements View.OnClickListener {
    OnSessionEnteredListener callback;
    private Button joinSessionButton;
    private EditText joinSessionEditText;
    private DiceService mService;
    public JoinFragment(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.join_session_layout, container, false);
        joinSessionEditText = rootView.findViewById(R.id.join_session_text);
        joinSessionButton = rootView.findViewById(R.id.join_session);
        joinSessionButton.setOnClickListener(this);
        mService = ApiUtils.getDiceService();
        return rootView;
    }


    @Override
    public void onClick(View v) {
        if(joinSessionEditText.getText().toString().trim().equals("")) {
            Toast.makeText(v.getContext(), "Please Enter A Session Code", Toast.LENGTH_SHORT).show();
        }else{
            if(Utility.isConnectedToNetwork(getContext())) {
                getDice(joinSessionEditText.getText().toString().toUpperCase());
            }
        }
    }

    public void setSession(OnSessionEnteredListener callback) {
        this.callback = callback;
    }

    public interface OnSessionEnteredListener {
        public void sessionEntered(String sessionId, String dice);
    }

    public void getDice(final String sessionId) {
        mService.getDice(sessionId).enqueue(new Callback<Session>() {
            @Override
            public void onResponse(Call<Session> call, Response<Session> response) {

                if(response.isSuccessful()) {
                    String status = response.body().getStatus();
                    if(status.equals("FAIL")){
                        Toast.makeText(getContext(), response.body().getMsg() , Toast.LENGTH_SHORT).show();
                    }else {
                        Gson gson = new Gson();
                        String json = gson.toJson(response.body().getDice());
                        callback.sessionEntered(sessionId, json);
                    }
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
}
