package com.android.diceroller.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Dice {

    @SerializedName("diceId")
    @Expose
    private Integer diceId;
    @SerializedName("diceType")
    @Expose
    private Integer diceType;
    @SerializedName("rolledValue")
    @Expose
    private Integer rolledValue;

    public Integer getDiceId() {
        return diceId;
    }

    public void setDiceId(Integer diceId) {
        this.diceId = diceId;
    }

    public Integer getDiceType() {
        return diceType;
    }

    public void setDiceType(Integer diceType) {
        this.diceType = diceType;
    }

    public Integer getRolledValue() {
        return rolledValue;
    }

    public void setRolledValue(Integer rolledValue) {
        this.rolledValue = rolledValue;
    }

    public Dice(Integer diceId, Integer diceType, Integer rolledValue) {
        this.diceId = diceId;
        this.diceType = diceType;
        this.rolledValue = rolledValue;
    }

}
