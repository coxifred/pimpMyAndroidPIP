package com.coxifred.pimmyandroidpip.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.coxifred.pimmyandroidpip.utils.Functions;

public class CoxifredReceiver extends BroadcastReceiver {
    public CoxifredReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Functions.log("DBG", "Receive Broadcast intent"  ,"CoxifredReceiver.onReceive");

        Intent serviceIntent = new Intent(context, OverlayService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                context.startForegroundService(serviceIntent);
            }catch(Exception e)
            {
                Functions.log("ERR", e.getMessage() ,"CoxifredReceiver.onReceive");
            }

        } else {
            try {
                context.startService(serviceIntent);
            }catch(Exception e)
            {
                Functions.log("ERR", e.getMessage() ,"CoxifredReceiver.onReceive");
            }

        }

    }
}