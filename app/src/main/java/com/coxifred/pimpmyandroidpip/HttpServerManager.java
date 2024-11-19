package com.coxifred.pimpmyandroidpip;


import static androidx.core.content.ContextCompat.getSystemService;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.util.Enumeration;

public class HttpServerManager {


        private static PopupListener instanceServer=null;
        private static Boolean aLive=false;
        private static String statusMessage="Unknown status";

        private static void getInstance()
        {
            if ( HttpServerManager.instanceServer == null)
            {
                try {
                    System.out.println("Instantiate a new PopupListener");
                    HttpServerManager.instanceServer = new PopupListener();
                }catch (Exception e)
                {
                    System.err.println("Can't start PopupListener " + e);
                }
            }
        }




    public static String status(Context ctx,Activity activity,Integer resId)
        {
            getInstance();
            System.out.println("Calling status method status is " + HttpServerManager.statusMessage);

            //generatePopup(ctx, activity,resId);

            /*
            CharSequence text = HttpServerManager.statusMessage;
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(ctx, text, duration);
            toast.show();
            */

            return HttpServerManager.statusMessage;
        }

        public static void alive()
        {
            Thread thread = new Thread(() -> {
                 try {
                     URL url = new URL("http://localhost:8080");
                     HttpURLConnection con = (HttpURLConnection) url.openConnection();
                     con.setRequestMethod("GET");
                     int status = con.getResponseCode();
                     if ( status == 200)
                     {
                         aLive=true;
                         String ipListening="listening port 8080 on ";
                         Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
                         while(interfaces.hasMoreElements()) {
                             NetworkInterface networkInterface = interfaces.nextElement();
                             Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                             System.out.println("Interface: " + networkInterface.getName());
                             while(inetAddresses.hasMoreElements())
                             {
                                 InetAddress ia=inetAddresses.nextElement();
                                 String[] ips=ia.getHostAddress().split("\\.");
                                 if ( ips.length == 4 ) {
                                     System.out.println("   address: " + ia.getHostAddress());
                                     ipListening+=ia.getHostAddress()+ ", ";
                                 }
                             }
                         }


                         HttpServerManager.statusMessage="Server is alive " + ipListening;
                         System.out.println("Server is alive");
                     }else
                     {
                         System.out.println("Server is down");
                         HttpServerManager.statusMessage="Server is down";
                         aLive=false;
                     }

                 } catch (Exception e) {
                     System.out.println("Server is down");
                     HttpServerManager.statusMessage="Server is down";
                     aLive=false;
                     e.printStackTrace();
                 }
             });

            thread.start();


        }

        public static void startServer()
        {
            getInstance();
            HttpServerManager.statusMessage="Starting server...";
            System.out.println("Starting server");
            try {
                HttpServerManager.instanceServer.startListener();
                alive();
            } catch (Exception e)
            {
                System.err.println("Can't start HttpListener " + e);
                HttpServerManager.statusMessage="Error on starting server " + e.getMessage();
                e.printStackTrace();
            }

        }

        public static void stopServer()
        {
            getInstance();
            HttpServerManager.statusMessage="Stopping server...";
            System.out.println("Stopping server");
            try {
                HttpServerManager.instanceServer.stopListener();
                alive();

            }
            catch (Exception e)
                {
                    HttpServerManager.statusMessage="Error on stopping server " + e.getMessage();
                    System.err.println("Can't stop HttpListener " + e);
                    e.printStackTrace();
                }
        }


}



