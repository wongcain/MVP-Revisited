package com.cainwong.mvprevisited.giphy.api.models;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class TrendingGiphys {

    @SerializedName("data")
    private List<Giph> giphys;

    public TrendingGiphys() {
    }

    public List<Giph> getGiphys() {
        return giphys;
    }

    public void setGiphys(List<Giph> giphys) {
        this.giphys = giphys;
    }

    public static class Giph {

        public enum ImageType{
            fixed_height,
            fixed_height_still,
            fixed_height_downsampled,
            fixed_width,
            fixed_width_still,
            fixed_width_downsampled,
            fixed_height_small,
            fixed_height_small_still,
            fixed_width_small,
            fixed_width_small_still,
            downsized,
            downsized_still,
            downsized_large,
            original,
            original_still
        }

        private Map<ImageType, Image> images;

        public Giph(){}

        public Map<ImageType, Image> getImages() {
            return images;
        }

        public void setImages(Map<ImageType, Image> images) {
            this.images = images;
        }

        @Nullable
        public Image getImage(ImageType type){
            Image image = null;
            if(images != null){
                image = images.get(type);
            }
            return image;
        }
    }


    public static class Image {

        private String url;
        private Integer width;
        private Integer height;

        public Image(){}

        public Integer getHeight() {
            return height;
        }

        public void setHeight(Integer height) {
            this.height = height;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public Integer getWidth() {
            return width;
        }

        public void setWidth(Integer width) {
            this.width = width;
        }

    }

}
