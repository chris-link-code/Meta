package com.example.meta;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author chris
 * @create 2021/11/26
 */
public class LogUtil {
    private static SimpleDateFormat myLogSdf = new SimpleDateFormat(ApplicationProperties.logDateFormat);
    private static SimpleDateFormat logfile = new SimpleDateFormat(ApplicationProperties.logFileFormat);

    public static void w(String tag, Object msg) { // 警告信息
        log(tag, msg.toString(), 'w');
    }

    public static void e(String tag, Object msg) { // 错误信息
        log(tag, msg.toString(), 'e');
    }

    public static void d(String tag, Object msg) {// 调试信息
        log(tag, msg.toString(), 'd');
    }

    public static void i(String tag, Object msg) {//
        log(tag, msg.toString(), 'i');
    }

    public static void v(String tag, Object msg) {
        log(tag, msg.toString(), 'v');
    }

    public static void w(String tag, String text) {
        log(tag, text, 'w');
    }

    public static void e(String tag, String text) {
        log(tag, text, 'e');
    }

    public static void d(String tag, String text) {
        log(tag, text, 'd');
    }

    public static void i(String tag, String text) {
        log(tag, text, 'i');
    }

    public static void v(String tag, String text) {
        log(tag, text, 'v');
    }

    /**
     * 根据tag, msg和等级，输出日志
     *
     * @param tag
     * @param msg
     * @param level
     */
    private static void log(String tag, String msg, char level) {
        //日志文件总开关
        if (ApplicationProperties.logSwitch) {
            if ('e' == level && ('e' == ApplicationProperties.logType || 'v' == ApplicationProperties.logType)) { // 输出错误信息
                Log.e(tag, msg);
            } else if ('w' == level && ('w' == ApplicationProperties.logType || 'v' == ApplicationProperties.logType)) {
                Log.w(tag, msg);
            } else if ('d' == level && ('d' == ApplicationProperties.logType || 'v' == ApplicationProperties.logType)) {
                Log.d(tag, msg);
            } else if ('i' == level && ('d' == ApplicationProperties.logType || 'v' == ApplicationProperties.logType)) {
                Log.i(tag, msg);
            } else {
                Log.v(tag, msg);
            }
            //日志写入文件开关
            if (ApplicationProperties.logWriteToFile) {
                writeLogToFile(String.valueOf(level), tag, msg);
            }
        }
    }

    /**
     * 打开日志文件并写入日志
     *
     * @param myLogType
     * @param tag
     * @param text
     */
    private static void writeLogToFile(String myLogType, String tag, String text) {// 新建或打开日志文件
        Date nowtime = new Date();
        String needWriteFiel = logfile.format(nowtime);
        String needWriteMessage = myLogSdf.format(nowtime) + "    " + myLogType + "    " + tag + "    " + text;
        //File dirPath = Environment.getExternalStorageDirectory();

        File dirsFile = new File(ApplicationProperties.logSavePath);
        if (!dirsFile.exists()) {
            dirsFile.mkdirs();
        }
        //Log.i("创建文件","创建文件");// ApplicationProperties.logSavePath
        File file = new File(dirsFile.toString(), needWriteFiel + "_" + ApplicationProperties.logFileName);
        if (!file.exists()) {
            try {
                //在指定的文件夹中创建文件
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            // 后面这个参数代表是不是要接上文件中原来的数据，不进行覆盖
            FileWriter filerWriter = new FileWriter(file, true);
            BufferedWriter bufWriter = new BufferedWriter(filerWriter);
            bufWriter.write(needWriteMessage);
            bufWriter.newLine();
            bufWriter.close();
            filerWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除制定的日志文件
     */
    public static void delFile() {
        String needDelFiel = logfile.format(getDateBefore());
        String dirPath = ApplicationProperties.logSavePath;
        File file = new File(dirPath + "/" + needDelFiel + "_" + ApplicationProperties.logFileName);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 得到现在时间前的几天日期，用来得到需要删除的日志文件名
     */
    private static Date getDateBefore() {
        Date nowtime = new Date();
        Calendar now = Calendar.getInstance();
        now.setTime(nowtime);
        now.set(Calendar.DATE, now.get(Calendar.DATE) - ApplicationProperties.logFileSaveDays);
        return now.getTime();
    }
}