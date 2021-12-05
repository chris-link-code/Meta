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

    private boolean logSwitch;
    private boolean logWriteToFile;
    private String logType;
    private String logSavePath;
    private int logFileSaveDays;
    private String logFileName;
    private String logDateFormat;
    private String logFileFormat;

    public ConfigProperties(Context context) throws IOException {
        Properties properties = new Properties();
        properties.load(context.getAssets().open("config.properties"));

        this.imageCachePath = properties.getProperty("image_cache_path");
        this.imageSavePath = properties.getProperty("image_save_path");
        this.videoCachePath = properties.getProperty("video_cache_path");
        this.videoSavePath = properties.getProperty("video_save_path");
        this.imageMinLength = Integer.parseInt(properties.getProperty("image_min_length"));
        this.logSwitch = Boolean.parseBoolean(properties.getProperty("log_switch"));
        this.logWriteToFile = Boolean.parseBoolean(properties.getProperty("log_write_to_file"));
        this.logType = properties.getProperty("log_type");
        this.logSavePath = properties.getProperty("log_save_path");
        this.logFileSaveDays = Integer.parseInt(properties.getProperty("log_file_save_days"));
        this.logFileName = properties.getProperty("log_file_name");
        this.logDateFormat = properties.getProperty("log_date_format");
        this.logFileFormat = properties.getProperty("log_file_format");
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

    public boolean isLogSwitch() {
        return logSwitch;
    }

    public boolean isLogWriteToFile() {
        return logWriteToFile;
    }

    public String getLogType() {
        return logType;
    }

    public String getLogSavePath() {
        return logSavePath;
    }

    public int getLogFileSaveDays() {
        return logFileSaveDays;
    }

    public String getLogFileName() {
        return logFileName;
    }

    public String getLogDateFormat() {
        return logDateFormat;
    }

    public String getLogFileFormat() {
        return logFileFormat;
    }
}
