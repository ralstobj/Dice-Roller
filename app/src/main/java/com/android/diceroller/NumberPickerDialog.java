package com.android.diceroller;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import androidx.fragment.app.DialogFragment;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class NumberPickerDialog extends DialogFragment {
    OnDiceAdded callback;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LinearLayout LL = new LinearLayout(getActivity());
        LL.setOrientation(LinearLayout.HORIZONTAL);
        final NumberPicker diceTypePicker = new NumberPicker(getActivity());

        diceTypePicker.setMinValue(1);
        diceTypePicker.setMaxValue(6);
        diceTypePicker.setDisplayedValues(new String[] { "D4", "D6","D8","D10","D12","D20"});

        final NumberPicker diceAmountPicker = new NumberPicker(getActivity());
        diceAmountPicker.setMinValue(1);
        diceAmountPicker.setMaxValue(20);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(50, 50);
        params.gravity = Gravity.CENTER;
        LinearLayout.LayoutParams typeParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        typeParams.weight = 1;
        LinearLayout.LayoutParams numParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        numParams.weight = 1;
        LL.setLayoutParams(params);
        LL.addView(diceTypePicker,typeParams);
        LL.addView(diceAmountPicker,numParams);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Select Dice");
        builder.setView(LL);
        builder.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                callback.diceAdded(diceTypePicker.getValue(), diceAmountPicker.getValue());
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        return builder.create();
    }

    public void setDiceAdder(OnDiceAdded callback) {
        this.callback = callback;
    }

    public interface OnDiceAdded {
        public void diceAdded(int type, int quantity);
    }
}
