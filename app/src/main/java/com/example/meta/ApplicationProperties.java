package com.example.meta;

import android.content.Context;

import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * @author chris
 * @create 2021/12/2
 */
public class ApplicationProperties {
    private static ConfigProperties properties;
    public static Context context;
    /**
     * 图片缓存路径
     */
    public static String imageCachePath;
    /**
     * 图片保存路径
     */
    public static String imageSavePath;
    /**
     * 视频缓存路径
     */
    public static String videoCachePath;
    /**
     * 视频保存路径
     */
    public static String videoSavePath;
    /**
     * 图片最小有效尺寸，长、宽低于此像素值的图片不保存
     */
    public static int imageMinLength;
    /**
     * 处理器线程数
     */
    public static int processor;

    /**
     * 日志文件总开关
     */
    public static boolean logSwitch;
    /**
     * 日志写入文件开关
     */
    public static boolean logWriteToFile;
    /**
     * 输入日志类型，w代表只输出告警信息等，v代表输出所有
     */
    public static char logType;
    /**
     * 日志文件的保存路径
     */
    public static String logSavePath;
    /**
     * sd卡中日志文件的最多保存天数
     */
    public static int logFileSaveDays;
    /**
     * 本类输出的日志文件名称
     */
    public static String logFileName;
    /**
     * 日志的输出格式
     */
    public static String logDateFormat;
    /**
     * 日志文件命名格式
     */
    public static String logFileFormat;

    /**
     * 初始化
     */
    public static void initial() {
        try {
            properties = new ConfigProperties(context);
        } catch (IOException e) {
            LogUtil.e("读取配置异常", e.getMessage());
        }
        if (properties != null) {
            imageCachePath = properties.getImageCachePath();
            imageSavePath = properties.getImageSavePath();
            videoCachePath = properties.getVideoCachePath();
            videoSavePath = properties.getVideoSavePath();
            imageMinLength = properties.getImageMinLength();
            logSwitch = properties.isLogSwitch();
            logWriteToFile = properties.isLogWriteToFile();
            logType = properties.getLogType().charAt(0);
            logSavePath = context.getExternalFilesDir(null).getAbsolutePath() + "/" + properties.getLogSavePath();
            logFileSaveDays = properties.getLogFileSaveDays();
            logFileName = properties.getLogFileName();
            logDateFormat = properties.getLogDateFormat();
            logFileFormat = properties.getLogFileFormat();
        }
        processor = Runtime.getRuntime().availableProcessors();

        LogUtil.e("CPU cores", processor);
        LogUtil.e("imageCachePath", imageCachePath);
        LogUtil.e("imageSavePath", imageSavePath);
        LogUtil.e("videoCachePath", videoCachePath);
        LogUtil.e("videoSavePath", videoSavePath);
        LogUtil.e("imageMinLength", imageMinLength);
        LogUtil.e("logSwitch", logSwitch);
        LogUtil.e("logWriteToFile", logWriteToFile);
        LogUtil.e("logType", logType);
        LogUtil.e("logSavePath", logSavePath);
        LogUtil.e("logFileSaveDays", logFileSaveDays);
        LogUtil.e("logFileName", logFileName);
        LogUtil.e("logDateFormat", logDateFormat);
        LogUtil.e("logFileFormat", logFileFormat);
    }
}
