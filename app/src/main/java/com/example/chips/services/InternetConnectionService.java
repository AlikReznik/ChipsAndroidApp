package com.example.chips.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import com.example.chips.LoginActivity;
import com.example.chips.UnstableNetworkErrorActivity;
import com.example.chips.utils.DataFlowControl;

public class InternetConnectionService extends Service {
    /*
     *  InternetConnectionService:
     *  Checks if the user is connected to the ethernet
     *
     * */

    /*
     *   onStartCommand:
     *      Starts the service
     *
     *   Input:
     *       Intent intent, int flags, int startId
     *
     *   Output:
     *       int
     * */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            try {
                                Thread.sleep(2000);
                                if(!networkConnectionExist() && !UnstableNetworkErrorActivity.isActivityVisible()){
                                    Intent intent = new Intent(DataFlowControl.context, UnstableNetworkErrorActivity.class);
                                    DataFlowControl.context.startActivity(intent);
                                }
                                if(networkConnectionExist() && UnstableNetworkErrorActivity.isActivityVisible()){
                                    Intent intent = new Intent(DataFlowControl.context, LoginActivity.class);
                                    DataFlowControl.context.startActivity(intent);
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
        ).start();
        return super.onStartCommand(intent, flags, startId);
    }

    /*
     *   onBind:
     *      Returns null
     *
     *   Input:
     *       Intent intent
     *
     *   Output:
     *       IBinder
     * */
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /*
     *   networkConnectionExist:
     *      Checks if phone is connected to wifi | mobile network
     *
     *   Input:
     *       None
     *
     *   Output:
     *       boolean
     * */
    private boolean networkConnectionExist() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }
}