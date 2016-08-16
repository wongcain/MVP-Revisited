package com.cainwong.mvprevisited.api.giphy.models;

public class RandomGiphImage {

    private String imageUrl;

    private Integer imageWidth;

    private Integer imageHeight;

    private String fixedWidthSmallStillUrl;

    private Integer fixedWidthSmallWidth;

    private Integer fixedWidthSmallHeight;

    public Integer getFixedWidthSmallHeight() {
        return fixedWidthSmallHeight;
    }

    public void setFixedWidthSmallHeight(Integer fixedWidthSmallHeight) {
        this.fixedWidthSmallHeight = fixedWidthSmallHeight;
    }

    public String getFixedWidthSmallStillUrl() {
        return fixedWidthSmallStillUrl;
    }

    public void setFixedWidthSmallStillUrl(String fixedWidthSmallStillUrl) {
        this.fixedWidthSmallStillUrl = fixedWidthSmallStillUrl;
    }

    public Integer getFixedWidthSmallWidth() {
        return fixedWidthSmallWidth;
    }

    public void setFixedWidthSmallWidth(Integer fixedWidthSmallWidth) {
        this.fixedWidthSmallWidth = fixedWidthSmallWidth;
    }

    public Integer getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(Integer imageHeight) {
        this.imageHeight = imageHeight;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(Integer imageWidth) {
        this.imageWidth = imageWidth;
    }
}
