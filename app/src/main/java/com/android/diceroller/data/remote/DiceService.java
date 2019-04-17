package com.android.diceroller.data.remote;

import com.android.diceroller.data.model.CreatorInfo;
import com.android.diceroller.data.model.DiceIds;
import com.android.diceroller.data.model.DiceTypes;
import com.android.diceroller.data.model.Session;
import com.android.diceroller.data.model.SessionInfo;
import com.android.diceroller.data.model.Username;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface DiceService {

    @GET("session/{sessionId}")
    Call<Session> getDice(@Path("sessionId") String sessionId);

    @Headers("Content-Type: application/json")
    @POST("session")
    Call<CreatorInfo> createSession(@Body Username username);

    @Headers("Content-Type: application/json")
    @POST("session/{sessionId}")
    Call<Session> addDice(@Path("sessionId") String sessionId, @Body DiceTypes diceTypes);

    @Headers("Content-Type: application/json")
    @PUT("session/{sessionId}")
    Call<Session> rollDice(@Path("sessionId") String sessionId, @Body DiceIds diceIds);

    @Headers("Content-Type: application/json")
    @HTTP(method = "DELETE", path = "session/{sessionId}", hasBody = true)
    Call<Session> deleteDice(@Path("sessionId") String sessionId, @Body DiceIds diceIds);

    @Headers("Content-Type: application/json")
    @HTTP(method = "DELETE", path = "session", hasBody = true)
    Call<Session> deleteSession(@Body SessionInfo sessionInfo);



}
