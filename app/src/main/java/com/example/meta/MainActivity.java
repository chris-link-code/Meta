package com.example.meta;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.io.File;

/**
 * @author chris
 * @create 2021/12/2
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 获取页面控件
        Button saveButton = findViewById(R.id.saveButton);
        Button closeButton = findViewById(R.id.closeButton);
        TextView resultDisplay = findViewById(R.id.resultTextView);
        Button deleteImageButton = findViewById(R.id.deleteImageButton);
        Button deleteVideoButton = findViewById(R.id.deleteVideoButton);

        // 获得context,必须在初始化前获得获得context,初始化要用到
        ApplicationProperties.context = getApplicationContext();
        // 初始化
        ApplicationProperties.initial();

        //点击事件
        saveButton.setOnClickListener(v -> {
            // 清空resultDisplay
            if (resultDisplay.getTextSize() > 0) {
                resultDisplay.setText("");
            }

            LogUtil.e("点击事件", System.currentTimeMillis());
            saveButton.setEnabled(false);

            Utils.downloadImage();
            //Utils.downloadVideo();

            resultDisplay.setText("OK");
        });

        // 关闭事件
        closeButton.setOnClickListener(v -> {
            resultDisplay.setText("CLOSING");
            moveTaskToBack(true);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                LogUtil.e("INFO", e.getMessage());
            }
            LogUtil.e("INFO", "CLOSING");
            System.exit(0);
        });

        // 删除图片缓存
        deleteImageButton.setOnClickListener(view -> {
            File file = new File(ApplicationProperties.imageCachePath);
            Utils.deleteFile(file);
        });

        // 删除视频缓存
        deleteVideoButton.setOnClickListener(view -> {
            File file = new File(ApplicationProperties.videoCachePath);
            Utils.deleteFile(file);
        });

        /*
         * 测试Utils的方法
         * 在非Activity中需要用Context方法来调用getExternalFilesDir()
         */
        // Utils.storeMethodTest(context);
    }
}