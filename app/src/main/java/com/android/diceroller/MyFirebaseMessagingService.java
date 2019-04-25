package com.android.diceroller;

import android.content.Intent;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String sessionId;
        String status;
        if (remoteMessage.getNotification() != null) {
            sessionId = remoteMessage.getNotification().getBody();
            status = "active";
        } else {
            sessionId =  remoteMessage.getData().get("sessionId");
            status = remoteMessage.getData().get("status");
        }
        Log.d("SESSIONID", sessionId);
        Log.d("STATUS", status);
        LocalBroadcastManager broadcaster = LocalBroadcastManager.getInstance(getBaseContext());

        Intent intent = new Intent("firebase");
        intent.putExtra("sessionId", sessionId);
        intent.putExtra("status", status);
        broadcaster.sendBroadcast(intent);
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.d("TOKENFIREBASE", s);
    }
}
