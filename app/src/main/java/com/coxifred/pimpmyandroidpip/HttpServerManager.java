package com.coxifred.pimpmyandroidpip;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpServerManager {


        private static PopupListener instanceServer=null;

        private static void getInstance()
        {
            if ( HttpServerManager.instanceServer == null)
            {
                try {
                    HttpServerManager.instanceServer = new PopupListener();
                }catch (Exception e)
                {
                    System.err.println("Can't start PopupListener " + e);
                }
            }
        }

        public static void startServer()
        {
            getInstance();
            System.out.println("Starting server");
            try {
                HttpServerManager.instanceServer.startListener();
                System.out.println("Trying request");


                Thread thread = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            URL url = new URL("http://192.168.2.17:61000/parameters");



                            HttpURLConnection con = (HttpURLConnection) url.openConnection();
                            con.setRequestMethod("GET");
                            int status = con.getResponseCode();
                            System.out.println("HTTP return code is " + status);
                            BufferedReader in = new BufferedReader(
                                    new InputStreamReader(con.getInputStream()));
                            String inputLine;
                            StringBuffer content = new StringBuffer();
                            while ((inputLine = in.readLine()) != null) {
                                content.append(inputLine);
                            }
                            in.close();
                            System.out.println("Test access returns " + content);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                thread.start();


                System.out.println("Hint: Hit Ctrl+C to stop Jetty.");
            } catch (Exception e)
            {
                System.err.println("Can't start HttpListener " + e);
                e.printStackTrace();
            }

        }

        public static void stopServer()
        {
            getInstance();
            System.out.println("Stopping server");
            try {
                HttpServerManager.instanceServer.stopListener();
            }
            catch (Exception e)
                {
                    System.err.println("Can't stop Jetty " + e);
                }
        }
}



