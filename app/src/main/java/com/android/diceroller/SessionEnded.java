package com.android.diceroller;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.android.diceroller.utils.Utility;

import androidx.fragment.app.DialogFragment;

public class SessionEnded extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Session Ended");
        builder.setMessage("The session has ended. ");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Utility.clearStack(getFragmentManager());
            }
        });
        return builder.create();
    }
}

