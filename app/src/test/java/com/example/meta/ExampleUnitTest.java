package com.example.meta;

import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    /**
     * 测试线程池执行完毕
     */
    @Test
    public void threadPoolDone() {
        int core = Runtime.getRuntime().availableProcessors();
        System.out.println(core);
        ExecutorService service = Executors.newFixedThreadPool(core);
        for (int i = 0; i < 100; i++) {
            int n = i;
            Runnable runnable = () -> {
                System.out.println(System.currentTimeMillis() + "\t" + n);
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

}