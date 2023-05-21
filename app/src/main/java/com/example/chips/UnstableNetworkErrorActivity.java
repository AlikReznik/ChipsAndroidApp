package com.example.chips;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.chips.utils.DataFlowControl;

public class UnstableNetworkErrorActivity extends AppCompatActivity {
    /*
    *   Unstable network error activity
    * */

    /*
     *   Activity variables
     * */
    private static boolean activityVisible;

    /*---------------------------------------------------------------------------------------------------------------------------------------------------*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unstable_network_error);
    }

    protected void onStart() {
        super.onStart();
        DataFlowControl.context = UnstableNetworkErrorActivity.this;
        activityVisible = true;
    }

    protected void onPause() {
        super.onPause();
        activityVisible = false;
    }

    /*---------------------------------------------------------------------------------------------------------------------------------------------------*/

    /*
     *   isActivityVisible:
     *      Update chat information dialog
     *
     *   Input:
     *       None
     *
     *   Output:
     *       None
     * */
    public static boolean isActivityVisible() {
        return activityVisible;
    }

}