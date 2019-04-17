package com.android.diceroller.data.remote;

public class ApiUtils {

    public static final String BASE_URL = "https://rollie.azurewebsites.net/rollieRest.php/api/v1/";

    public static DiceService getDiceService() {
        return RetrofitClient.getClient(BASE_URL).create(DiceService.class);
    }

}
