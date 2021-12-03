package com.example.meta;

import android.content.Context;

import java.io.IOException;

/**
 * @author chris
 * @create 2021/12/2
 */
public class InitialApplication {
    public static String imageCachePath;
    public static String imageSavePath;
    public static String videoCachePath;
    public static String videoSavePath;
    public static int imageMinLength;
    private static ConfigProperties properties;

    /**
     * 初始化
     */
    public static void initial(Context context) {

        try {
            properties = new ConfigProperties(context);
        } catch (IOException e) {
            MyLog.e("读取配置异常", e.getMessage());
        }
        if (properties != null) {
            imageCachePath = properties.getImageCachePath();
            imageSavePath = properties.getImageSavePath();
            videoCachePath = properties.getVideoCachePath();
            videoSavePath = properties.getVideoSavePath();
            imageMinLength = properties.getImageMinLength();
        }
        Utils.PROCESSORS = Runtime.getRuntime().availableProcessors();

        MyLog.e("CPU cores", Utils.PROCESSORS);
        MyLog.e("imageCachePath", imageCachePath);
        MyLog.e("imageSavePath", imageSavePath);
        MyLog.e("videoCachePath", videoCachePath);
        MyLog.e("videoSavePath", videoSavePath);
        MyLog.e("imageMinLength", imageMinLength);
    }
}
