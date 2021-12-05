package com.example.meta;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author chris
 * @create 2021/11/28
 */
public class Utils {
    public static boolean scanDone = false;

    /**
     * 测试路径
     */
    public static void storeMethodTest(Context context) {
        LogUtil.e("LOG", "Environment.getExternalStorageDirectory: " +
                Environment.getExternalStorageDirectory().getAbsolutePath());
        LogUtil.e("LOG", "getExternalFilesDir(null): " +
                context.getExternalFilesDir(null).getAbsolutePath());
        LogUtil.e("LOG", "getExternalFilesDir(Environment.DIRECTORY_PICTURES): " +
                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath());

        LogUtil.e("LOG", "Environment.getExternalStorageState(): " +
                Environment.getExternalStorageState());
    }

    /**
     * 复制文件
     */
    public static void copyFile(File source, File dest)
            throws IOException {
        FileUtils.copyFile(source, dest);
    }

    /**
     * 下载图片
     */
    public static void downloadImage() {
        BlockingQueue<File> queue = new LinkedBlockingQueue<>(10000);
        Utils.getAllImage(queue);
        ExecutorService service = Executors.newFixedThreadPool(ApplicationProperties.processor);
        boolean keepCopy = true;
        while (keepCopy) {
            try {
                if (queue.isEmpty() && Utils.scanDone) {
                    keepCopy = false;
                } else {
                    File file = queue.take();
                    if (file != null) {
                        Runnable runnable = () -> {
                            try {
                                String newPath = ApplicationProperties.imageSavePath + file.getName().replaceAll(".cnt", ".jpg");
                                File dest = new File(newPath);
                                Utils.copyFile(file, dest);
                            } catch (IOException e) {
                                LogUtil.e("COPY ERROR", e.getMessage());
                            }
                        };
                        service.execute(runnable);
                    }
                }
            } catch (InterruptedException e) {
                LogUtil.e("InterruptedException", e.getMessage());
            }
        }
        service.shutdown();
        /**boolean downloading = true;
         while (downloading) {
         try {
         boolean termination = service.awaitTermination(1, TimeUnit.SECONDS);
         if (termination) {
         downloading = false;
         }
         } catch (InterruptedException e) {
         MyLog.e("termination interrupted", e.getMessage());
         }
         }
         long end = System.currentTimeMillis();
         return end;*/
    }

    /**
     * 下载视频
     */
    public static void downloadVideo() {
        File pathFile = new File(ApplicationProperties.videoCachePath);
        List<File> list = findFileList(pathFile, null);
        if (list == null || list.size() < 1) {
            LogUtil.e(ApplicationProperties.videoCachePath, "There are no video");
            return;
        }
        ExecutorService service = Executors.newFixedThreadPool(ApplicationProperties.processor);
        for (File file : list) {
            try {
                if (file != null) {
                    Runnable runnable = () -> {
                        try {
                            String newPath = ApplicationProperties.videoSavePath + file.getName() + ".mp4";
                            File dest = new File(newPath);
                            Utils.copyFile(file, dest);
                        } catch (IOException e) {
                            LogUtil.e("COPY ERROR", e.getMessage());
                        }
                    };
                    service.execute(runnable);
                }
            } catch (Exception e) {
                LogUtil.e(file.getAbsolutePath(), e.getMessage());
            }
        }
        service.shutdown();
        /**boolean downloading = true;
         while (downloading) {
         try {
         boolean termination = service.awaitTermination(1, TimeUnit.SECONDS);
         if (termination) {
         downloading = false;
         }
         } catch (InterruptedException e) {
         MyLog.e("termination interrupted", e.getMessage());
         }
         }*/
    }

    /**
     * 获取path文件夹下的大图片
     */
    public static void getAllImage(BlockingQueue<File> queue) {
        File pathFile = new File(ApplicationProperties.imageCachePath);
        List<File> list = findFileList(pathFile, null);
        if (list == null || list.size() < 1) {
            LogUtil.e(ApplicationProperties.imageCachePath, "There are no picture");
            return;
        }
        LogUtil.e("scan files", list.size());

        ExecutorService service = Executors.newFixedThreadPool(ApplicationProperties.processor);
        for (File file : list) {
            try {
                Runnable runnable = () -> {
                    if (isBigImage(file)) {
                        try {
                            queue.put(file);
                        } catch (InterruptedException e) {
                            LogUtil.e(file.getAbsolutePath(), e.getMessage());
                        }
                    }
                };
                service.execute(runnable);
            } catch (Exception e) {
                LogUtil.e(file.getAbsolutePath(), e.getMessage());
            }
        }
        service.shutdown();
        while (!scanDone) {
            try {
                boolean termination = service.awaitTermination(1, TimeUnit.SECONDS);
                if (termination) {
                    scanDone = true;
                    LogUtil.e("INFO", "scan done");
                }
            } catch (InterruptedException e) {
                LogUtil.e("termination interrupted", e.getMessage());
            }
        }
    }

    /**
     * 递归遍历文件夹下的所有子文件
     *
     * @param path 目录
     * @param list 保存文件名的集合
     * @return
     */
    private static List<File> findFileList(File path, List<File> list) {
        if (!path.exists() || !path.isDirectory()) {
            return list;
        }
        if (list == null) {
            list = new ArrayList<>();
        }
        File[] files = path.listFiles();
        if (files == null || files.length < 1) {
            return null;
        }
        for (File file : files) {
            try {
                if (file.isDirectory()) {
                    // 回调自身继续查询
                    findFileList(file, list);
                } else {
                    list.add(file);
                }
            } catch (Exception e) {
                LogUtil.e(file.getAbsolutePath() + " ERROR", e.getMessage());
            }
        }
        return list;
    }

    /**
     * 判断图片大小
     * 分辨率小于 200 * 200 将认定为小图
     */
    private static boolean isBigImage(File file) {
        boolean flag = false;
        if (file.isFile() && file.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            if (bitmap != null) {
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                if (width > ApplicationProperties.imageMinLength && height > ApplicationProperties.imageMinLength) {
                    flag = true;
                }
                // LogUtil.e(flag + "\t" + file.getName(), "width: " + width + "\t height: " + height);
            }
        }
        return flag;
    }
}
