package com.coxifred.pimmyandroidpip.beans;

import android.graphics.Bitmap;

import com.coxifred.pimmyandroidpip.utils.Functions;

public class CoxifredPopup {

    String popupType="SimpleMessage";
    Integer cardWidth=400;
    String message="";
    String detail="";
    String imageUrl="";
    String rtcUrl="";
    Integer rtcHeight=400;
    Integer rtcScale=50;
    Integer imageWidth=200;
    Integer imageHeight;
    Bitmap imageBitmap;
    Long timeToDisplay=10000L;

    public Integer getRtcScale() {
        return rtcScale;
    }

    public void setRtcScale(Integer rtcScale) {
        this.rtcScale = rtcScale;
    }

    public Integer getCardWidth() {
        return cardWidth;
    }

    public void setCardWidth(Integer cardWidth) {
        this.cardWidth = cardWidth;
    }

    public Integer getRtcHeight() {
        return rtcHeight;
    }

    public void setRtcHeight(Integer rtcHeight) {
        this.rtcHeight = rtcHeight;
    }

    public String getRtcUrl() {
        return rtcUrl;
    }

    public void setRtcUrl(String rtcUrl) {
        this.rtcUrl = rtcUrl;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Integer getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(Integer imageWidth) {
        this.imageWidth = imageWidth;
    }

    public Integer getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(Integer imageHeight) {
        this.imageHeight = imageHeight;
    }

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    public void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
    }

    public Long getTimeToDisplay() {
        return timeToDisplay;
    }

    public void setTimeToDisplay(Long timeToDisplay) {
        this.timeToDisplay = timeToDisplay;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPopupType() {
        return popupType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setPopupType(String popupType) {
        this.popupType = popupType;
    }

    public void internalLoad()
    {
        if ( ! "".equals(imageUrl) )
        {
            Bitmap aBitmap=Functions.getImageBitmap(imageUrl);
            Functions.log("DBG","Genuine image size is width=" + aBitmap.getWidth() + " height=" + aBitmap.getHeight(),"CoxifredPopup.internalLoad");
            if ( getImageHeight() == null)
            {
                float ratio= (float) (getImageWidth().floatValue() / (float) aBitmap.getWidth());
                float heightCompute=(float)(aBitmap.getHeight()) * ratio;
                Functions.log("DBG","Resizing height (because null) image to height=" + heightCompute + " ratio is " + ratio,"CoxifredPopup.internalLoad");
                setImageHeight((int) heightCompute);
            }else
            {
                float ratio= (float) (getImageHeight().floatValue() / (float)aBitmap.getHeight());
                float widthCompute=(float)(aBitmap.getWidth()) * ratio;
                Functions.log("DBG","Resizing width (because height was provided) image to width " + widthCompute + " ratio is " + ratio,"CoxifredPopup.internalLoad");
                setImageWidth((int) widthCompute);
            }

            imageBitmap=Bitmap.createScaledBitmap(Functions.getImageBitmap(imageUrl), getImageWidth(),getImageHeight(), true);

        }
    }
}
