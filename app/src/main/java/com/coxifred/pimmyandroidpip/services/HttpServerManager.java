package com.coxifred.pimmyandroidpip.services;


import com.coxifred.pimmyandroidpip.utils.Functions;

public class HttpServerManager {


        private static PopupListener instanceServer=null;
        private static String statusMessage="Unknown status";
        private static Integer lastReturnCommand=0;

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




        public String status()
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


    public Integer lastReturnCommand()
    {
        getInstance();
        return HttpServerManager.lastReturnCommand;
    }


    public void startServer()
        {
            getInstance();
            Functions.log("DBG","Starting server","HttpServerManager.startServer");
            HttpServerManager.instanceServer.startListener();
            Functions.patience(1000L);
            statusMessage=instanceServer.getStatusMessage();
            lastReturnCommand=instanceServer.getLastReturnCommand();
        }

        public void stopServer()
        {

            getInstance();
            HttpServerManager.statusMessage="Stopping server...";
            Functions.log("DBG","Stopping server..","HttpServerManager.stopServer");
            try {
                HttpServerManager.instanceServer.stopListener();
                Functions.patience(1000L);
                statusMessage=instanceServer.getStatusMessage();
                lastReturnCommand=instanceServer.getLastReturnCommand();
                Functions.log("DBG","Server stopped","HttpServerManager.stopServer");

            }
            catch (Exception e)
                {
                    HttpServerManager.statusMessage="Error on stopping server " + e.getMessage();
                    Functions.log("ERR","Can't stop server " + e.getMessage(),"HttpServerManager.stopServer");
                    e.printStackTrace();
                }
        }


}



