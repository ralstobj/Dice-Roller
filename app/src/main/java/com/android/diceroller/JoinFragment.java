package com.android.diceroller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

public class JoinFragment extends Fragment implements View.OnClickListener {
    OnSessionEnteredListener callback;
    private Button joinSessionButton;
    private EditText joinSessionEditText;
    public JoinFragment(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.join_session_layout, container, false);
        joinSessionEditText = rootView.findViewById(R.id.join_session_text);
        joinSessionButton = rootView.findViewById(R.id.join_session);
        joinSessionButton.setOnClickListener(this);
        return rootView;
    }


    @Override
    public void onClick(View v) {
        callback.sessionEntered(joinSessionEditText.getText().toString());
    }

    public void setSession(OnSessionEnteredListener callback) {
        this.callback = callback;
    }

    public interface OnSessionEnteredListener {
        public void sessionEntered(String sessionId);
    }
}