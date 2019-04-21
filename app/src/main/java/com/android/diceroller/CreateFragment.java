package com.android.diceroller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.diceroller.data.model.CreatorInfo;
import com.android.diceroller.data.model.Username;
import com.android.diceroller.data.remote.ApiUtils;
import com.android.diceroller.data.remote.DiceService;

import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateFragment extends Fragment implements View.OnClickListener {

    OnSessionCreatedListener callback;
    private Button createSessionButton;
    private EditText createSessionEditText;
    private DiceService mService;
    public CreateFragment(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.create_session_layout, container, false);
        createSessionEditText = rootView.findViewById(R.id.create_session_text);
        createSessionButton = rootView.findViewById(R.id.create_session);
        createSessionButton.setOnClickListener(this);
        mService = ApiUtils.getDiceService();
        return rootView;
    }


    @Override
    public void onClick(View v) {
        if(createSessionEditText.getText().toString().trim().equals("")) {
            Toast.makeText(v.getContext(), "Enter A Username", Toast.LENGTH_SHORT).show();
        }else{
            createSession(createSessionEditText.getText().toString().toUpperCase());
        }
    }

    public void setUsername(OnSessionCreatedListener callback) {
        this.callback = callback;
    }

    public void createSession(String user){
        Username username = new Username(user);
        mService.createSession(username).enqueue(new Callback<CreatorInfo>() {
            @Override
            public void onResponse(Call<CreatorInfo> call, Response<CreatorInfo> response) {

                if(response.isSuccessful()) {
                    String status = response.body().getStatus();
                    if(status.equals("FAIL")){
                        Toast.makeText(getContext(), response.body().getMsg() , Toast.LENGTH_SHORT).show();
                    }else {
                        callback.sessionCreated(response.body().getSessionId(), response.body().getToken());
                    }
                }else {
                    int statusCode  = response.code();
                }
            }

            @Override
            public void onFailure(Call<CreatorInfo> call, Throwable t) {

            }
        });
    }

    public interface OnSessionCreatedListener {
        public void sessionCreated(String sessionId, String token);
    }

}
