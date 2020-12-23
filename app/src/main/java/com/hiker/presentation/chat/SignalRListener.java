package com.hiker.presentation.chat;

import android.view.View;

import com.microsoft.signalr.HubConnection;
import com.microsoft.signalr.HubConnectionBuilder;

public class SignalRListener {
    private static SignalRListener instance;

    HubConnection hubConnection;

    public SignalRListener(View view){
        hubConnection = HubConnectionBuilder.create("http://192.168.0.171:6000/chathub").build();
    }

    public static SignalRListener getInstance(View view){
        if (instance == null){
            instance = new SignalRListener(view);
        }
        return instance;
    }
}
