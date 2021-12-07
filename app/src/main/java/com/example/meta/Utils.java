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
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author chris
 * @create 2021/11/28
 */
public class Utils {
    /**
     * 阻塞队列
     */
    private static LinkedBlockingQueue<File> queue = new LinkedBlockingQueue<>();
    /**
     * 线程池
     */
    private static ExecutorService executor = Executors.newFixedThreadPool(ApplicationProperties.processor);
    /**
     * 生产者计数
     */
    private static AtomicInteger produceCount = new AtomicInteger();

    private static CopyOnWriteArrayList<File> list = new CopyOnWriteArrayList<>();

    // private static boolean scanDone = false;


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
        LogUtil.e("downloading file", dest.getName());
        FileUtils.copyFile(source, dest);
    }

    /**
     * 下载图片
     * 利用阻塞队列LinkedBlockingQueue的生产者消费者模式
     */
    public static void downloadImageWithQueue() {
        // TODO: 2021/12/6
        //  查询优秀的生产者消费者模式代码

        File pathFile = new File(ApplicationProperties.imageCachePath);
        List<File> list = findFileList(pathFile, null);
        if (list == null || list.size() < 1) {
            LogUtil.e(ApplicationProperties.imageCachePath, "There are no picture");
            return;
        }
        LogUtil.e("scan files", list.size());

        for (File file : list) {
            produceCount.incrementAndGet();

            // 过滤图片线程，生产者线程
            Runnable filterRunnable = () -> {
                if (isBigImage(file)) {
                    try {
                        queue.put(file);
                    } catch (InterruptedException e) {
                        LogUtil.e(file.getAbsolutePath(), e.getMessage());
                    }
                }
            };

            // 复制图片线程，消费者线程
            Runnable copyRunnable = () -> {
                try {
                    /*
                    * 这里存在问题
                    * 总会出现消费者线程先执行的情况
                    * 导致丢数据
                    */
                    if (produceCount.get() > 0) {
                        LogUtil.e("produceCount", produceCount.get());
                        produceCount.decrementAndGet();
                        if (!queue.isEmpty()) {
                            File takeFile = queue.take();
                            String newPath = ApplicationProperties.imageSavePath + takeFile.getName().replaceAll(".cnt", ".jpg");
                            File dest = new File(newPath);
                            Utils.copyFile(takeFile, dest);
                        }
                    }
                } catch (Exception e) {
                    LogUtil.e(file.getAbsolutePath() + " copy error", e.getMessage());
                }
            };

            executor.execute(filterRunnable);
            executor.execute(copyRunnable);
        }
        executor.shutdown();

        try {
            boolean awaitTermination = executor.awaitTermination(30, TimeUnit.MINUTES);
            LogUtil.e("ExecutorService awaitTermination", awaitTermination);
        } catch (InterruptedException e) {
            LogUtil.e("ExecutorService termination interrupted", e.getMessage());
        }
        LogUtil.e("download done", System.currentTimeMillis());
        LogUtil.e("ExecutorService isTerminated", executor.isTerminated());

       /* boolean keepDownload = true;
        while (keepDownload) {
            if (queue.isEmpty() && (produceCount.get() < 1)) {
                keepDownload = false;
            }
            if (produceCount.get() > 0) {
                produceCount.decrementAndGet();
                File file = null;
                try {
                    file = queue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (file != null) {
                    File finalFile = file;
                    Runnable runnable = () -> {
                        try {
                            String newPath = ApplicationProperties.imageSavePath + finalFile.getName().replaceAll(".cnt", ".jpg");
                            File dest = new File(newPath);
                            Utils.copyFile(finalFile, dest);
                        } catch (IOException e) {
                            LogUtil.e(finalFile.getAbsolutePath() + " copy error", e.getMessage());
                        }
                    };
                    executor.execute(runnable);
                }
            }
        }
        executor.shutdown();


        // 等待过滤图片的线程池执行完毕
        try {
            boolean awaitTermination = executor.awaitTermination(30, TimeUnit.MINUTES);
            LogUtil.e("ExecutorService awaitTermination", awaitTermination);
        } catch (InterruptedException e) {
            LogUtil.e("ExecutorService termination interrupted", e.getMessage());
        }
        scanDone = true;
        LogUtil.e("download done", System.currentTimeMillis());
        LogUtil.e("ExecutorService isTerminated", executor.isTerminated());*/
        
/*
        // 等待过滤图片的线程池执行完毕
        try {
            boolean downloadAwaitTermination = downloadService.awaitTermination(30, TimeUnit.MINUTES);
            LogUtil.e("download image ExecutorService awaitTermination", downloadAwaitTermination);
        } catch (InterruptedException e) {
            LogUtil.e("download image pool termination interrupted", e.getMessage());
        }
        scanDone = true;
        LogUtil.e("download done", System.currentTimeMillis());
        LogUtil.e("download image ExecutorService isTerminated", downloadService.isTerminated());*/




        /*boolean keepCopy = true;
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
        boolean downloading = true;
        while (downloading) {
            try {
                boolean termination = service.awaitTermination(1, TimeUnit.SECONDS);
                if (termination) {
                    downloading = false;
                    LogUtil.e("download done", System.currentTimeMillis());
                }
            } catch (InterruptedException e) {
                LogUtil.e("termination interrupted", e.getMessage());
            }
        }*/
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
     * 过滤掉小图片
     */
    public static void getFilteredImage() {
        /*File pathFile = new File(ApplicationProperties.imageCachePath);
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
                            tempList.add(file);
                        } catch (Exception e) {
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
        try {
            boolean awaitTermination = service.awaitTermination(30, TimeUnit.MINUTES);
            LogUtil.e("getFilteredImage ExecutorService awaitTermination", awaitTermination);
        } catch (InterruptedException e) {
            LogUtil.e("getFilteredImage pool termination interrupted", e.getMessage());
        }
        LogUtil.e("filter done", System.currentTimeMillis());
        LogUtil.e("getFilteredImage ExecutorService isTerminated", service.isTerminated());*/


        File pathFile = new File(ApplicationProperties.imageCachePath);
        List<File> list = findFileList(pathFile, null);
        if (list == null || list.size() < 1) {
            LogUtil.e(ApplicationProperties.imageCachePath, "There are no picture");
            return;
        }
        LogUtil.e("scan files", list.size());

        for (File file : list) {
            try {
                Runnable runnable = () -> {
                    if (isBigImage(file)) {
                        try {
                            produceCount.incrementAndGet();
                            queue.put(file);
                        } catch (InterruptedException e) {
                            LogUtil.e(file.getAbsolutePath(), e.getMessage());
                        }
                    }
                };
                executor.execute(runnable);
            } catch (Exception e) {
                LogUtil.e(file.getAbsolutePath(), e.getMessage());
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
        boolean bigImage = false;
        if (file.isFile() && file.exists()) {
            try {
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                if (bitmap != null) {
                    int width = bitmap.getWidth();
                    int height = bitmap.getHeight();

                    if (width > ApplicationProperties.imageMinLength && height > ApplicationProperties.imageMinLength) {
                        bigImage = true;
                        LogUtil.e(file.getName(), "width: " + width + "\t height: " + height);
                    }
                }
            } catch (Exception e) {
                LogUtil.e(file.getAbsolutePath() + " ERROR", e.getMessage());
            }
        }
        return bigImage;
    }
}
