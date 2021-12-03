package com.example.meta;

import android.content.Context;
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

        Button saveButton = findViewById(R.id.saveButton);
        Button closeButton = findViewById(R.id.closeButton);
        TextView resultDisplay = findViewById(R.id.resultTextView);

        Context context = getApplicationContext();
        InitialApplication.initial(context);

        //点击事件
        saveButton.setOnClickListener(v -> {
            long start = System.currentTimeMillis();
            // 清空resultDisplay
            if (resultDisplay.getTextSize() > 0) {
                resultDisplay.setText("");
            }
            MyLog.e("INFO", "点击事件");
            long end = Utils.download();
            String result = "执行完毕，耗时 " + (end - start) + " 毫秒";
            MyLog.e("OK", result);
            resultDisplay.setText(result);
        });

        //关闭事件
        closeButton.setOnClickListener(v -> {
            moveTaskToBack(true);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                MyLog.e("INFO", e.getMessage());
            }
            MyLog.e("INFO", "CLOSING");
            System.exit(0);
        });

        /**
         * 测试Utils的方法
         * 在非Activity中需要用Context方法来调用getExternalFilesDir()
         */
        // Utils.storeMethodTest(context);
    }
}