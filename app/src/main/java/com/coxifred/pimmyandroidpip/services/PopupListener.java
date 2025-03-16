package com.coxifred.pimmyandroidpip.services;

import com.coxifred.pimmyandroidpip.beans.AbstractMessage;
import com.coxifred.pimmyandroidpip.beans.CoxifredPopup;
import com.coxifred.pimmyandroidpip.beans.CoxifredSleep;
import com.coxifred.pimmyandroidpip.utils.Functions;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.util.Enumeration;

import fi.iki.elonen.NanoHTTPD;

public class PopupListener extends NanoHTTPD{

    private String statusMessage="";
    private Integer lastReturnCommand=0;

    public PopupListener() throws IOException {
        super(8080);

    }

    public String getStatusMessage()
    {
        return statusMessage;
    }

    public Integer getLastReturnCommand()
    {
        return lastReturnCommand;
    }



    public void alive()
    {
        Thread thread = new Thread(() -> {
            try {
                URL url = new URL("http://localhost:8080");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                int status = con.getResponseCode();
                if ( status == 200)
                {
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

                    statusMessage="Server is alive " + ipListening;
                    lastReturnCommand=0;
                    Functions.log("DBG","Server is alive","PopupListener.alive");
                }else
                {
                    Functions.log("ERR","Server is down","PopupListener.alive");
                    lastReturnCommand=1;
                    statusMessage="Server is down";
                }

            } catch (Exception e) {
                Functions.log("ERR","Server is down " + e.getMessage(),"PopupListener.alive");
                statusMessage="Server is down " + e.getMessage();
                lastReturnCommand=1;
                e.printStackTrace();
            }
        });

        thread.start();


    }

    public void startListener()  {
         try {
             if (!isAlive()) {
                 start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
             }
            alive();
         }catch (Exception e)
         {
             statusMessage="Server is down " + e.getMessage();
             lastReturnCommand=1;
         }
    }

    public void stopListener()
    {
        stop();
        statusMessage="Stopped";
        lastReturnCommand=0;
    }

    // Redirection
    // telnet localhost 5554
    // auth <token> (cat /home/fred/.emulator_console_auth_token)
    // redir add tcp:8080:8080
    @Override
    public Response serve(IHTTPSession session) {
        Functions.log("DBG","A request is coming","PopupListener.serve");
        String aMethod=session.getMethod().toString();
        if (aMethod.equals("GET"))
        {
            StringBuilder strb=new StringBuilder();
            strb.append("<html><body>");
                strb.append("<h1>Welcome to PimpMyAndroidPip</h1><br>");
                for ( String aLogLine : Functions.messages)
                {
                    strb.append(aLogLine).append("<br>");
                }
            strb.append("</body></html>");

            return newFixedLengthResponse(strb.toString());
        }
        else if (aMethod.equals("POST"))
        {
            try {
                int contentLength = Integer.parseInt(session.getHeaders().get("content-length"));
                byte[] buffer = new byte[contentLength];
                session.getInputStream().read(buffer, 0, contentLength);
                //session.parseBody(new HashMap<>());
                String requestBody = new String(buffer);
                Functions.log("DBG","Post data " + requestBody,"PopupListener.serve");
                try {
                    Functions.log("DBG", "Trying to json parse " + requestBody, "PopupListener.serve");
                    Gson aGson = new Gson();
                    AbstractMessage am = aGson.fromJson(requestBody, AbstractMessage.class);
                    Functions.log("DBG","Successfully parsed","PopupListener.serve");
                    if (am.getMessageType().equals("popup")) {
                        CoxifredPopup cp=aGson.fromJson(requestBody, CoxifredPopup.class);
                        cp.internalLoad();
                        OverlayService.toDisplay.add(cp);
                        Functions.log("DBG","Sent to the background service","PopupListener.serve");
                    } else if (am.getMessageType().equals("sleep")) {
                        CoxifredSleep cs=aGson.fromJson(requestBody, CoxifredSleep.class);
                        OverlayService.toSleep.add(cs);
                    }


                }catch (Exception e)
                {
                    Functions.log("ERR","Can't parse as json " + requestBody  + " " + e.getMessage(),"PopupListener.serve");
                    return newFixedLengthResponse("Can't parse as json " + requestBody + " " + e.getMessage());
                }

                return newFixedLengthResponse("Request body = " + requestBody);

            } catch (Exception e) {
                return newFixedLengthResponse("Request error = " + e.getMessage());
            }

        }
        return newFixedLengthResponse(Response.Status.NOT_FOUND, MIME_PLAINTEXT,
                "The method " + aMethod + " isn't implemented");
    }
}



