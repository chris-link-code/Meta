package com.example.meta;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.meta", appContext.getPackageName());
    }

    @Test
    public void threadPoolTest() {
        int processors = Runtime.getRuntime().availableProcessors();
        //MyLog.e("LOG", "CPU cores: " + processors);
        System.out.println("CPU cores: " + processors);
        ExecutorService service = Executors.newFixedThreadPool(processors);
        for (int i = 0; i < 100; i++) {
            final int index = i;
            // System.out.println("task: " + i);
            Runnable runnable = () -> {
                System.out.println(Thread.currentThread().getName() + ",thread run " + index);
            };
            service.execute(runnable);
        }
        service.shutdown();
        System.out.println(System.currentTimeMillis() + "\t---------------");
        try {
            // 确认线程池的所有任务都执行完毕，再执行后面的代码
            service.awaitTermination(30, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(System.currentTimeMillis() + "\t++++++++++++++++");
    }

    @Test
    public void imagePixelTest() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        //File path = context.getExternalFilesDir(null);
        File path = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        //File path = new File("/storage/emulated/0/Music/");
        //File path = new File("/storage/emulated/0/");

        // /storage/emulated/0/Android/data/com.example.meta/files
        // /storage/emulated/0/Music/
        System.out.println(path.getAbsolutePath());
        File[] files = path.listFiles();
        if (files == null || files.length < 1) {
            System.out.println(path.getAbsolutePath() + " hasn't file");
            return;
        }
        for (File file : files) {
            System.out.println(file.getName());
            if (file.isFile() && file.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                if (bitmap != null) {
                    int width = bitmap.getWidth();
                    int height = bitmap.getHeight();
                    System.out.println(file.getName()
                            + "\twidth: " + width
                            + "\theight: " + height);
                }
            }
        }
    }
}