package com.coxifred.pimmyandroidpip.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;

import com.coxifred.pimmyandroidpip.R;
import com.coxifred.pimmyandroidpip.beans.CoxifredPopup;
import com.coxifred.pimmyandroidpip.utils.Functions;

import java.util.ArrayList;
import java.util.List;

public class OverlayService extends Service  {

    private WindowManager wm;
    private CardView aCardView;
    private TextView aTextView;
    private ConstraintLayout cl;
    private HttpServerManager httpServerManager;
    private Handler handler=new Handler();
    public static List<CoxifredPopup> toDisplay=new ArrayList<CoxifredPopup>();

    // Start For Comm with activity
    private final IBinder binder = new LocalBinder();
    public class LocalBinder extends Binder {
        public OverlayService getService() {
            // Return this instance of OverlayService so clients can call public methods.
            return OverlayService.this;
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
    // End For Comm with activity

    public String getStatus()
    {
        return httpServerManager.status();
    }

    public Integer getLastReturnCommand()
    {
        return httpServerManager.lastReturnCommand();
    }

    public void startHttpServer()
    {
        httpServerManager.startServer();
    }

    public void stopHttpServer()
    {
        httpServerManager.stopServer();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onCreate();

        if ( httpServerManager == null )
        {
            Functions.log("DBG","Instanciating httpServer","OverlayService.onStartCommand");
            httpServerManager= new HttpServerManager();
        }else
        {
            Functions.log("DBG","HttpServer already instanciated","OverlayService.onStartCommand");
        }


        if (Build.VERSION.SDK_INT >= 26) {
            String CHANNEL_ID = "channel1";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Overlay notification",
                    NotificationManager.IMPORTANCE_LOW);

            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE))
                    .createNotificationChannel(channel);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("pimpMyAndroidPIP")
                    .setContentText("pimpMyAndroidPIP")
                    .setSmallIcon(R.drawable.logo)
                    .build();

            startForeground(1, notification);
        }


        wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                        performPopups();
                        handler.postDelayed(this,1000);

            }
        });


        return START_NOT_STICKY;
    }

    public void performPopups()
    {
        for ( CoxifredPopup aPopup:toDisplay)
        {
            Functions.log("DBG","Displaying a popup type " + aPopup.getPopupType(),"OverlayService.performPopups");
            if ( aPopup.getPopupType().equals("SimpleMessage"))
            {
                simpleMessage(aPopup);
            }

        }
        toDisplay.clear();

    }

    public void simpleMessage(CoxifredPopup aPopup)
    {
        Functions.log("DBG","Displaying a popup","OverlayService.simpleMessage");

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View clFromXml = inflater.inflate(R.layout.popuplayout, null);
        CardView cardView=clFromXml.findViewById(R.id.card);
        cardView.setLayoutParams(new ViewGroup.LayoutParams(aPopup.getCardWidth(),ViewGroup.LayoutParams.WRAP_CONTENT));

        WebView mWebview = clFromXml.findViewById(R.id.web_view);
        if ( ! "".equals(aPopup.getRtcUrl())) {
            mWebview.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, aPopup.getRtcHeight()));
            mWebview.loadUrl(aPopup.getRtcUrl());
            mWebview.setInitialScale(aPopup.getRtcScale());
            WebSettings set = mWebview.getSettings();
            set.setUseWideViewPort(true);
            set.setLoadWithOverviewMode(true);
            set.setJavaScriptEnabled(true);
            set.setBuiltInZoomControls(true);
        }else {
            mWebview.setLayoutParams(new ViewGroup.LayoutParams(1, 1));
            mWebview.setVisibility(View.INVISIBLE);
        }


        TextView title=clFromXml.findViewById(R.id.title);
        if ( ! "".equals(aPopup.getMessage())) {
            title.setText(aPopup.getMessage());
        }else{
            title.setVisibility(TextView.INVISIBLE);
        }

        TextView detail=clFromXml.findViewById(R.id.detail);
        if ( ! "".equals(aPopup.getDetail())) {
            detail.setText(aPopup.getDetail());
        }else{
            detail.setHeight(1);
            detail.setVisibility(TextView.INVISIBLE);
        }

        ImageView iv=clFromXml.findViewById(R.id.header_image);
        if ( ! "".equals(aPopup.getImageUrl()) && aPopup.getImageBitmap() != null) {
            iv.setImageBitmap(aPopup.getImageBitmap());
        }else{
            iv.setImageBitmap(null);
           }




        int type = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
                ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                : WindowManager.LayoutParams.TYPE_PHONE;

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                type,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.END | Gravity.TOP;
        params.x = 5;
        params.y = 5;

        wm.addView(clFromXml, params);
        Thread aThread=new Thread( () -> {
            Functions.patience(aPopup.getTimeToDisplay());
            try {
                wm.removeView(clFromXml);
            } catch (Exception e)
            {
                Functions.log("ERR","Can't cleanup view " + e.getMessage(),"OverlayService.simpleMessage");
            }
        });
        aThread.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (aCardView != null) {
            wm.removeView(aCardView);
            aCardView = null;
        }
    }


}
