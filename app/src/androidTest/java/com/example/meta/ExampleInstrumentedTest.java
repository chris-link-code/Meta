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
            System.out.println("task: " + i);
            Runnable runnable = () -> {
                System.out.println(Thread.currentThread().getName() + ",thread start " + index);
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //System.out.println(Thread.currentThread().getName() + ",thread end " + index);
            };
            service.execute(runnable);
        }
    }

    @Test
    public void imagePixelTest() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        File path = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        System.out.println(path.getAbsolutePath());
        File[] files = path.listFiles();
        for (File file : files) {
            if (file.isFile() && file.exists()) {
                //System.out.println(file.getName());
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