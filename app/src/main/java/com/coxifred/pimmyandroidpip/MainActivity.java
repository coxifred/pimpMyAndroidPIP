package com.coxifred.pimmyandroidpip;

import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import androidx.fragment.app.FragmentActivity;

import com.coxifred.pimmyandroidpip.beans.CoxifredPopup;
import com.coxifred.pimmyandroidpip.services.OverlayService;
import com.coxifred.pimmyandroidpip.utils.Functions;

/*
 * Main Activity class that loads {@link MainFragment}.
 */
public class MainActivity extends FragmentActivity implements View.OnClickListener{

    private Button startButton;
    private Button stopButton;
    private Button testButton;
    private Button reduceButton;
    private TextView statusTextView;
    private TextView logTextView;
    private OverlayService overlayService;
    boolean mBound = false;

    @Override
    protected void onResume() {
        Functions.log("DBG", "OnResume","MainActivity.onResume");
        super.onResume();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Functions.log("DBG", "Starting mainActivity","MainActivity.onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startButton = findViewById(R.id.startServiceButton);
        startButton.setOnClickListener(this);


        stopButton = findViewById(R.id.stopServiceButton);
        stopButton.setOnClickListener(this);
        stopButton.setVisibility(View.INVISIBLE);

        testButton = findViewById(R.id.testButton);
        testButton.setOnClickListener(this);

        reduceButton = findViewById(R.id.reduceButton);
        reduceButton.setOnClickListener(this);


        statusTextView = findViewById(R.id.statusTextView);
        logTextView= findViewById(R.id.LogMessagesText);



        if (savedInstanceState == null) {
            if ( checkDrawOverlayPermission() )
            {
                startOverlayService();
            }
        }
        logTextView.setText(Functions.getMessages());
    }

    private static int REQUEST_CODE = 1;
    private boolean checkDrawOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            /** check if we already  have permission to draw over other apps */
            if (!Settings.canDrawOverlays(this)) {
                Functions.log("WNG","canDrawOverlays NOK","MainActivity.onCreate");
                /** if not construct intent to request permission */
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                /** request permission via start activity for result */
                startActivityForResult(intent, REQUEST_CODE);
                return false;
            } else {
                Functions.log("DBG","canDrawOverlays OK","MainActivity.onCreate");

            }
        }
        return true;
    }

    @Override
    @TargetApi(Build.VERSION_CODES.M)
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        /** check if received result code
         is equal our requested code for draw permission  */
        //super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (Settings.canDrawOverlays(this)) {
                startOverlayService();
            }
        }
    }

    private void startOverlayService() {
        Intent intent = new Intent(this, OverlayService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                startForegroundService(intent);
            }catch(Exception e)
            {
                Functions.log("ERR", e.getMessage() ,"MainActivity.startOverlayService");
            }

        } else {
            try {
            startService(intent);
            }catch(Exception e)
            {
            Functions.log("ERR", e.getMessage() ,"MainActivity.startOverlayService");
            }

            }
    }

    public void setText(String message)
    {
        if (statusTextView != null) {
            statusTextView.setText(message);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.startServiceButton) {
           overlayService.startHttpServer();
           /* Wait 1 sec */
            Functions.patience(1000L);
            setText(overlayService.getStatus());
            if ( overlayService.getLastReturnCommand() == 0)
            {
                startButton.setVisibility(View.INVISIBLE);
                stopButton.setVisibility(View.VISIBLE);
                testButton.setVisibility(View.VISIBLE);
            }
        }
        if (view.getId()==R.id.stopServiceButton) {
            overlayService.stopHttpServer();
            Functions.patience(1000L);
            setText(overlayService.getStatus());
            if ( overlayService.getLastReturnCommand() == 0)
            {
                startButton.setVisibility(View.VISIBLE);
                stopButton.setVisibility(View.INVISIBLE);
                testButton.setVisibility(View.INVISIBLE);
            }
        }

        if (view.getId()==R.id.testButton) {
            CoxifredPopup cp=new CoxifredPopup();
            cp.setMessage("This is a test");
            OverlayService.toDisplay.add(cp);
        }

        if (view.getId()==R.id.reduceButton) {
            this.moveTaskToBack(true);
        }

        logTextView.setText(Functions.getMessages());
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(connection);
        mBound = false;
    }

    /** Defines callbacks for service binding, passed to bindService(). */
    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance.
            OverlayService.LocalBinder binder = (OverlayService.LocalBinder) service;
            overlayService = binder.getService();
            overlayService.startHttpServer();
            /* Wait 1 sec */
            Functions.patience(1000L);
            setText(overlayService.getStatus());
            startButton.setVisibility(View.INVISIBLE);
            stopButton.setVisibility(View.VISIBLE);
            testButton.setVisibility(View.VISIBLE);
            moveTaskToBack(true);
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };
}