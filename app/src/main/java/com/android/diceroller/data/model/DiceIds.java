package com.android.diceroller.data.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DiceIds {

    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("diceIds")
    @Expose
    private List<Integer> diceIds = null;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<Integer> getDiceIds() {
        return diceIds;
    }

    public void setDiceIds(List<Integer> diceIds) {
        this.diceIds = diceIds;
    }
    public void addDiceIds(Integer diceId){
        this.diceIds.add(diceId);
    }
    public DiceIds(String token, List<Integer> diceIds) {
        this.token = token;
        this.diceIds = diceIds;
    }

    public DiceIds(String token) {
        this.token = token;
    }
}
