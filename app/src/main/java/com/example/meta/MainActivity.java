 package com.example.meta;

import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

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

        // 获得context
        ApplicationProperties.context = getApplicationContext();
        // 初始化
        ApplicationProperties.initial();

        //点击事件
        saveButton.setOnClickListener(v -> {
            // long start = System.currentTimeMillis();
            // 清空resultDisplay
            if (resultDisplay.getTextSize() > 0) {
                resultDisplay.setText("");
            }

            LogUtil.e("INFO", "点击事件");

            Utils.downloadImage();
            Utils.downloadVideo();

            LogUtil.e("INFO", "OK");
            resultDisplay.setText("OK");
        });

        //关闭事件
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

        /**
         * 测试Utils的方法
         * 在非Activity中需要用Context方法来调用getExternalFilesDir()
         */
        // Utils.storeMethodTest(context);
    }
}