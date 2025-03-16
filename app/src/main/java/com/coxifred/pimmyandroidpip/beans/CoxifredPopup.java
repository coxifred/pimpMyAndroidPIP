package com.coxifred.pimmyandroidpip.beans;

import android.graphics.Bitmap;

import com.coxifred.pimmyandroidpip.utils.Functions;

public class CoxifredPopup  extends AbstractMessage{

    String popupType="SimpleMessage";
    Integer cardWidth=400;
    Integer cardHeight=0;
    String message="";



    String detail="";
    String imageUrl="";
    String imageIconUrl="";
    String rtcUrl="";
    Integer rtcHeight=400;
    Integer rtcScale=50;
    Integer imageWidth=200;
    Integer imageHeight;
    Integer xMargin=0;
    Integer alpha=50;
    Integer yMargin=0;
    Integer textHeight=1;
    Integer textBackgroundColor=-1;
    Integer cardBackgroundColor=-1;
    Bitmap imageBitmap;
    Bitmap imageIconBitmap;
    Long timeToDisplay=10000L;

    public Bitmap getImageIconBitmap() {
        return imageIconBitmap;
    }

    public void setImageIconBitmap(Bitmap imageIconBitmap) {
        this.imageIconBitmap = imageIconBitmap;
    }

    public Integer getCardBackgroundColor() {
        return cardBackgroundColor;
    }

    public void setCardBackgroundColor(Integer cardBackgroundColor) {
        this.cardBackgroundColor = cardBackgroundColor;
    }

    public Integer getTextBackgroundColor() {
        return textBackgroundColor;
    }

    public void setTextBackgroundColor(Integer textBackgroundColor) {
        this.textBackgroundColor = textBackgroundColor;
    }

    public Integer getCardHeight() {
        return cardHeight;
    }

    public Integer getTextHeight() {
        return textHeight;
    }

    public void setTextHeight(Integer textHeight) {
        this.textHeight = textHeight;
    }

    public Integer getAlpha() {
        return alpha;
    }

    public void setAlpha(Integer alpha) {
        this.alpha = alpha;
    }

    public void setCardHeight(Integer cardHeight) {
        this.cardHeight = cardHeight;
    }
    public String getImageIconUrl() {
        return imageIconUrl;
    }

    public void setImageIconUrl(String imageIconUrl) {
        this.imageIconUrl = imageIconUrl;
    }

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

    public Integer getxMargin() {
        return xMargin;
    }

    public void setxMargin(Integer xMargin) {
        this.xMargin = xMargin;
    }

    public Integer getyMargin() {
        return yMargin;
    }

    public void setyMargin(Integer yMargin) {
        this.yMargin = yMargin;
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

        if ( ! "".equals(imageIconUrl) )
        {
            Bitmap aBitmap=Functions.getImageBitmap(imageIconUrl);
            imageIconBitmap=Functions.getImageBitmap(imageIconUrl);

        }
    }
}
