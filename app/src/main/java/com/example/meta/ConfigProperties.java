package com.example.meta;

import android.content.Context;

import java.io.IOException;
import java.util.Properties;

/**
 * @author chris
 * @create 2021/12/2
 */
public class ConfigProperties {
    private String imageCachePath;
    private String imageSavePath;
    private String videoCachePath;
    private String videoSavePath;
    private int imageMinLength;

    public ConfigProperties(Context context) throws IOException {
        Properties properties = new Properties();
        properties.load(context.getAssets().open("config.properties"));

        this.imageCachePath = properties.getProperty("image_cache_path");
        this.imageSavePath = properties.getProperty("image_save_path");
        this.videoCachePath = properties.getProperty("video_cache_path");
        this.videoSavePath = properties.getProperty("video_save_path");
        this.imageMinLength = Integer.parseInt(properties.getProperty("image_min_length"));
    }

    public String getImageCachePath() {
        return imageCachePath;
    }

    public String getImageSavePath() {
        return imageSavePath;
    }

    public String getVideoCachePath() {
        return videoCachePath;
    }

    public String getVideoSavePath() {
        return videoSavePath;
    }

    public int getImageMinLength() {
        return imageMinLength;
    }
}
