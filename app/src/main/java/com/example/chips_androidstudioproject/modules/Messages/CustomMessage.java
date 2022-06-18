package com.example.chips_androidstudioproject.modules.Messages;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.Timestamp;

import java.util.Map;

public abstract class CustomMessage {
    /*
     * Basic message data
     * */
    protected String sender;
    protected Timestamp time;

    /*
     * Constructors
     * */
    public CustomMessage(String sender, Timestamp time)
    {
        this.sender = sender;
        this.time = time;
    }
    public CustomMessage() { }

    /*
     * Listview item setter
     * */
    public abstract View setView(LayoutInflater layoutInflater, ViewGroup parent);

    /*
     * Converts the object to an object for firestore
     * */
    public abstract Map<String, Object> convertToMap();

}
