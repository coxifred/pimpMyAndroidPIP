package com.coxifred.pimmyandroidpip.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Functions {

    public static List<String> messages=new ArrayList<String>();

    public static String getDateFormat(Date date, String format_Ex_YYYY_MM_DD) {
        if (format_Ex_YYYY_MM_DD == null) {
            format_Ex_YYYY_MM_DD = "yyyy/MM/dd_HH:mm:ss";
        }
        DateFormat dateFormat = new SimpleDateFormat(format_Ex_YYYY_MM_DD);
        return dateFormat.format(date);
    }

    public static Bitmap getImageBitmap(String url) {
        Bitmap bm = null;
        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
            Functions.log("ERR","Can't load url " + url + " cause " + e.getMessage(), "Functions.getImageBitmap");
        }
        return bm;
    }

    public static String getMessages() {
        StringBuilder strb=new StringBuilder();
        for (String aLine:messages)
        {
            strb.append(aLine).append("\n");
        }
        return strb.toString();
    }

    public static String getFieldFromString(String chaine, String delimiteur, Integer Field) {
        String[] liste = chaine.split(delimiteur);
        String retour = "";
        try {
            retour = liste[Field];
        } catch (Exception e) {

        }
        return retour;
    }

    public static Date getDateFormat(String date, String format_Ex_YYYY_MM_DD) {
        Date dt = null;
        try {
            if (format_Ex_YYYY_MM_DD == null) {
                format_Ex_YYYY_MM_DD = "yyyyMMddHHmmss";
            }
            SimpleDateFormat df = new SimpleDateFormat(format_Ex_YYYY_MM_DD);

            dt = df.parse(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return dt;

    }

    public static void log(String level, String message,String from)
    {
        String compiledMessage=getDateFormat(new Date(),null) + "[" + Thread.currentThread().getName() + "] " + level + " [" + from + "] " + message;
        if ( "ERR".equals(level))
        {
            System.err.println(compiledMessage);
        }else
        {
            System.out.println(compiledMessage);
        }

        messages.add(compiledMessage);
    }

    public static void patience(Long timeInMs)
    {
        try {
            Thread.sleep(timeInMs);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


}
