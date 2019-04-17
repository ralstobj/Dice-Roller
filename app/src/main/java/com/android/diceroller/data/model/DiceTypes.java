package com.android.diceroller.data.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DiceTypes {

    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("diceTypes")
    @Expose
    private List<Integer> diceTypes = null;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<Integer> getDiceTypes() {
        return diceTypes;
    }

    public void setDiceTypes(List<Integer> diceTypes) {
        this.diceTypes = diceTypes;
    }

    public DiceTypes(String token, List<Integer> diceTypes) {
        this.token = token;
        this.diceTypes = diceTypes;
    }
}
