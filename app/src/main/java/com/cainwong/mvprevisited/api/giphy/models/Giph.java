package com.cainwong.mvprevisited.api.giphy.models;

import android.support.annotation.Nullable;

import java.util.Map;

public  class Giph {

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