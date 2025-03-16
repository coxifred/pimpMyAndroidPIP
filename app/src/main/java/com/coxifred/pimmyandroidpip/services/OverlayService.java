package com.coxifred.pimmyandroidpip.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.media.AudioManager;
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
import com.coxifred.pimmyandroidpip.beans.CoxifredSleep;
import com.coxifred.pimmyandroidpip.utils.Functions;

import java.util.ArrayList;
import java.util.List;

public class OverlayService extends Service implements View.OnClickListener {

    private WindowManager wm;
    private View clFromXml;
    private CardView aCardView;
    private TextView aTextView;
    private ConstraintLayout cl;
    private HttpServerManager httpServerManager;
    private Handler handler=new Handler();
    public static List<CoxifredPopup> toDisplay=new ArrayList<CoxifredPopup>();
    public static List<CoxifredSleep> toSleep=new ArrayList<CoxifredSleep>();

    // Start For Comm with activity
    private final IBinder binder = new LocalBinder();

    @Override
    public void onClick(View v) {
        wm.removeView(clFromXml);
    }

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
                        performMuteAndBlackScreen();
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

    public void performMuteAndBlackScreen()
    {
        for ( CoxifredSleep aSleep:toSleep)
        {
            Functions.log("DBG","Muting sound application ","OverlayService.performMuteAndBlackScreen");
            AudioManager aManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                aManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE, 0);
            } else {
                aManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
            }
            CoxifredPopup aPopup=new CoxifredPopup();
            aPopup.setPopupType("popup");
            aPopup.setCardWidth(2400);
            aPopup.setCardHeight(2400);
            aPopup.setxMargin(0);
            aPopup.setyMargin(0);
            aPopup.setAlpha(256);
            aPopup.setTextHeight(2400);
            aPopup.setTimeToDisplay(aSleep.getTimeToSleep());
            aPopup.setTextBackgroundColor(Color.BLACK);
            aPopup.setCardBackgroundColor(Color.BLACK);
            aPopup.setMessageType("SimpleMessage");
            aPopup.setMessage("Sleeping mode");
            aPopup.setDetail("");
            aPopup.setTimeToDisplay(aSleep.getTimeToSleep());
            simpleMessage(aPopup);

        }
        toSleep.clear();

    }

    public void simpleMessage(CoxifredPopup aPopup)
    {
        Functions.log("DBG","Displaying a popup","OverlayService.simpleMessage");

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        clFromXml = inflater.inflate(R.layout.popuplayout, null);
        clFromXml.getBackground().setAlpha(aPopup.getAlpha());
        CardView cardView=clFromXml.findViewById(R.id.card);
        cardView.getBackground().setAlpha(aPopup.getAlpha());

        if ( ! aPopup.getCardBackgroundColor().equals(-1) ) {
            cardView.setBackgroundColor(aPopup.getCardBackgroundColor());
        }
        cardView.setBackgroundColor(Color.BLACK);
        if ( ! aPopup.getCardHeight().equals(0)) {
            cardView.setLayoutParams(new ViewGroup.LayoutParams(aPopup.getCardWidth(), ViewGroup.LayoutParams.WRAP_CONTENT));
        }else
        {
            cardView.setLayoutParams(new ViewGroup.LayoutParams(aPopup.getCardWidth(), aPopup.getCardHeight()));
        }

        WebView mWebview = clFromXml.findViewById(R.id.web_view);
        if ( ! "".equals(aPopup.getRtcUrl())) {
            mWebview.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, aPopup.getRtcHeight()));
            mWebview.loadUrl(aPopup.getRtcUrl());
            mWebview.setInitialScale(aPopup.getRtcScale());
            mWebview.setBackgroundColor(Color.BLUE);
            WebSettings set = mWebview.getSettings();
            set.setUseWideViewPort(false);
            set.setLoadWithOverviewMode(true);
            //mWebview.setBackground(Color.BLUE);
            set.setJavaScriptEnabled(true);
            set.setBuiltInZoomControls(false);
        }else {
            mWebview.setVisibility(WebView.INVISIBLE);
            mWebview.setLayoutParams(new ViewGroup.LayoutParams(1, 1));
        }

        ImageView iv=clFromXml.findViewById(R.id.header_image);
        if ( ! "".equals(aPopup.getImageUrl()) && aPopup.getImageBitmap() != null) {
            iv.setImageBitmap(aPopup.getImageBitmap());
        }else{
            iv.setImageBitmap(null);
            iv.setBackgroundColor(Color.BLUE);
            iv.setMaxHeight(1);
            iv.setVisibility(ImageView.INVISIBLE);
        }



        ImageView iconv=clFromXml.findViewById(R.id.icon_image);
        if ( ! "".equals(aPopup.getImageIconUrl()) && aPopup.getImageIconBitmap() != null) {
            iconv.setImageBitmap(aPopup.getImageIconBitmap());
        }else{
            iconv.setImageBitmap(null);
            iconv.setMaxHeight(1);
            iconv.setBackgroundColor(Color.BLUE);
            iconv.setVisibility(ImageView.INVISIBLE);
        }

        TextView title=clFromXml.findViewById(R.id.title);
        if ( ! aPopup.getTextBackgroundColor().equals(-1) ) {
            title.setBackgroundColor(aPopup.getTextBackgroundColor());
        }
        if ( ! "".equals(aPopup.getMessage())) {
            title.setText(aPopup.getMessage());
            title.setHeight(aPopup.getTextHeight());
        }else{
            title.setHeight(1);
            title.setVisibility(TextView.INVISIBLE);
        }
        if ( aPopup.getMessage().equals("Sleeping mode")) {
            title.setOnClickListener(this);
        }

        TextView detail=clFromXml.findViewById(R.id.detail);
        if ( ! "".equals(aPopup.getDetail())) {
            detail.setText(aPopup.getDetail());
        }else{
            detail.setHeight(1);
            detail.setVisibility(TextView.INVISIBLE);
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

        params.x=aPopup.getxMargin();
        params.y = aPopup.getyMargin();

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
