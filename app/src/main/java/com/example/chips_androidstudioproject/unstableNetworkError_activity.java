package com.example.chips_androidstudioproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.chips_androidstudioproject.modules.DataFlowControl;

public class unstableNetworkError_activity extends AppCompatActivity {
    private static boolean activityVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.unstable_network_error_layout);
    }

    protected void onStart() {
        super.onStart();
        DataFlowControl.context = unstableNetworkError_activity.this;
        activityVisible = true;
    }

    public static boolean isActivityVisible() {
        return activityVisible;
    }

    protected void onPause() {
        super.onPause();
        activityVisible = false;
    }
}