package com.example.meta;

import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author chris
 * @create 2021/12/6
 */
public class BlockingQueueTest {
    /**
     * 阻塞队列
     */
    private LinkedBlockingQueue<TestData> queue = new LinkedBlockingQueue<>();
    /**
     * 线程池
     */
    private ExecutorService executor;
    /**
     * 生产者计数
     */
    private AtomicInteger produceCount = new AtomicInteger();

    /**
     * 生产者
     */
    private void produce() {
        for (int i = 0; i < 100; i++) {
            int incrementAndGet = produceCount.incrementAndGet();
            System.out.println("incrementAndGet: " + incrementAndGet);
            int finalI = i;
            Runnable runnable = () -> {
                try {
                    queue.put(new TestData("produce", finalI));
                    System.out.println("produce " + finalI);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            };
            executor.execute(runnable);
        }
    }

    /**
     * 消费者
     */
    private void consume() {
        boolean keep = true;
        while (keep) {
            if (queue.isEmpty() && (produceCount.get() < 1)) {
                keep = false;
            }

            if (produceCount.get() > 0) {
                int decrementAndGet = produceCount.decrementAndGet();
                System.out.println("decrementAndGet: " + decrementAndGet);
                TestData data = null;
                try {
                    data = queue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (data != null) {
                    TestData finalData = data;
                    Runnable runnable = () -> {
                        System.out.println("consume " + finalData.toString());
                    };
                    executor.execute(runnable);
                }
            }
        }
        executor.shutdown();
    }

    @Test
    public void start() {
        int processors = Runtime.getRuntime().availableProcessors();
        System.out.println("CPU cores: " + processors);
        executor = Executors.newFixedThreadPool(processors);

        produce();
        consume();
    }
}
