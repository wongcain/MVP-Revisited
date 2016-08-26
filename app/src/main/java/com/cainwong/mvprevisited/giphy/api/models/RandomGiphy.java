package com.cainwong.mvprevisited.giphy.api.models;

import com.google.gson.annotations.SerializedName;

public class RandomGiphy {

    @SerializedName("data")
    private Image image;

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public static class Image {

        private String fixedHeightDownsampledUrl;

        private String fixedHeightSmallStillUrl;

        public String getFixedHeightSmallStillUrl() {
            return fixedHeightSmallStillUrl;
        }

        public String getFixedHeightDownsampledUrl() {
            return fixedHeightDownsampledUrl;
        }

        public void setFixedHeightDownsampledUrl(String fixedHeightDownsampledUrl) {
            this.fixedHeightDownsampledUrl = fixedHeightDownsampledUrl;
        }

        public void setFixedHeightSmallStillUrl(String fixedHeightSmallStillUrl) {
            this.fixedHeightSmallStillUrl = fixedHeightSmallStillUrl;
        }

    }
}
