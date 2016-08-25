package com.cainwong.mvprevisited.api.giphy.models;

public class RandomGiphImage {

    private String imageUrl;

    private Integer imageWidth;

    private Integer imageHeight;

    private String fixedHeightDownsampledUrl;

    private Integer fixedHeightDownsampledWidth;

    private Integer fixedHeightDownsampledHeight;

    private String fixedHeightSmallStillUrl;

    private Integer fixedHeightSmallWidth;

    private Integer fixedHeightSmallHeight;

    public Integer getFixedHeightSmallHeight() {
        return fixedHeightSmallHeight;
    }

    public void setFixedHeightSmallHeight(Integer fixedHeightSmallHeight) {
        this.fixedHeightSmallHeight = fixedHeightSmallHeight;
    }

    public String getFixedHeightSmallStillUrl() {
        return fixedHeightSmallStillUrl;
    }

    public void setFixedHeightSmallStillUrl(String fixedHeightSmallStillUrl) {
        this.fixedHeightSmallStillUrl = fixedHeightSmallStillUrl;
    }

    public Integer getFixedHeightSmallWidth() {
        return fixedHeightSmallWidth;
    }

    public void setFixedHeightSmallWidth(Integer fixedHeightSmallWidth) {
        this.fixedHeightSmallWidth = fixedHeightSmallWidth;
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

    public Integer getFixedHeightDownsampledHeight() {
        return fixedHeightDownsampledHeight;
    }

    public void setFixedHeightDownsampledHeight(Integer fixedHeightDownsampledHeight) {
        this.fixedHeightDownsampledHeight = fixedHeightDownsampledHeight;
    }

    public String getFixedHeightDownsampledUrl() {
        return fixedHeightDownsampledUrl;
    }

    public void setFixedHeightDownsampledUrl(String fixedHeightDownsampledUrl) {
        this.fixedHeightDownsampledUrl = fixedHeightDownsampledUrl;
    }

    public Integer getFixedHeightDownsampledWidth() {
        return fixedHeightDownsampledWidth;
    }

    public void setFixedHeightDownsampledWidth(Integer fixedHeightDownsampledWidth) {
        this.fixedHeightDownsampledWidth = fixedHeightDownsampledWidth;
    }
}
