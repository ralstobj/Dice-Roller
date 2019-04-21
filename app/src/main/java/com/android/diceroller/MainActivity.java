package com.android.diceroller;

import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


public class MainActivity extends AppCompatActivity implements JoinFragment.OnSessionEnteredListener, CreateFragment.OnSessionCreatedListener {



    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }
            LandingFragment landingFragment = new LandingFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, landingFragment).commit();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void sessionEntered(String sessionId, String dice) {
        ViewerFragment newFragment = new ViewerFragment();
        Bundle args = new Bundle();
        args.putString("sessionId", sessionId);
        args.putString("dice", dice);
        newFragment.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack("join");
        transaction.commit();
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        if (fragment instanceof JoinFragment) {
            JoinFragment joinFragment = (JoinFragment) fragment;
            joinFragment.setSession(this);
        } else if (fragment instanceof  CreateFragment){
            CreateFragment createFragment = (CreateFragment) fragment;
            createFragment.setUsername(this);
        }
    }

    @Override
    public void sessionCreated(String sessionId, String token) {
        ControllerFragment newFragment = new ControllerFragment();
        Bundle args = new Bundle();
        args.putString("sessionId", sessionId);
        args.putString("token", token);
        newFragment.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack("create");
        transaction.commit();
    }

}



