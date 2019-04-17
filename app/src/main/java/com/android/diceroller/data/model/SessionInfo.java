package com.android.diceroller.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SessionInfo {

    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("sessionId")
    @Expose
    private String sessionId;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public SessionInfo(String token, String sessionId) {
        this.token = token;
        this.sessionId = sessionId;
    }
}
