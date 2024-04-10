package com.bigshen.learningDemo.collection.map;

import org.junit.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author byj
 * @date 2024/4/7
 * @Description
 */

@Execution(ExecutionMode.CONCURRENT)
public class ThreadSafeLRUMapTest {

    /**
     * 非并发情况下，测试 map 的基本功能
     */
    @Test
    public void testBasicFunctionality() {
        ThreadSafeLRUMap<String, Integer> map = new ThreadSafeLRUMap<>(4);

        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);
        map.put("four", 4);

        assertEquals(4, map.size());
        assertEquals(1, map.get("one"));
        assertEquals(2, map.get("two"));
        assertEquals(3, map.get("three"));
        assertEquals(4, map.get("four"));

        // 测试 LRU 算法，最近未使用的元素会被移除，即 one 被移除
        map.put("five", 5);
        assertNull(map.get("one"));
        assertEquals(2, map.get("two"));
        assertEquals(3, map.get("three"));
        assertEquals(4, map.get("four"));
        assertEquals(5, map.get("five"));

        map.remove("two");
        assertFalse(map.containsKey("two"));
        assertNull(map.get("two"));
        assertEquals(3, map.size());

        map.clear();
        assertTrue(map.isEmpty());
        assertEquals(0, map.size());
    }

    /**
     * 测试并发情况下，put、get 是否正常
     */
    @Test
    public void testConcurrency() {
        int threadCount = 1000;
        int capacity = 1000000;
        int perThreadCount = capacity / threadCount;
        // 足够大的 map，避免触发 LRU
        Map<String, Integer> map = new ThreadSafeLRUMap<>((int) (capacity * 1.1f));
        CompletableFuture<?>[] futures = new CompletableFuture[threadCount];
        for (int i = 0; i < threadCount; i++) {
            int finalI = i;
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                for (int j = 0; j < perThreadCount; j++) {
                    map.put(finalI + "-" + j, j);
                }
            });
            futures[i] = future;
        }

        // 等待所有线程完成
        CompletableFuture.allOf(futures).join();

        // 检查Map的大小
        assertEquals(capacity, map.size());

        // 测试移除
        futures = new CompletableFuture[threadCount];
        for (int i = 0; i < threadCount; i++) {
            int finalI = i;
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                for (int j = 0; j < perThreadCount; j++) {
                    assertEquals(j, map.get(finalI + "-" + j));
                    assertEquals(j, map.remove(finalI + "-" + j));
                }
            });
            futures[i] = future;
        }

        // 等待所有线程完成
        CompletableFuture.allOf(futures).join();

        // 检查Map的大小
        assertEquals(0, map.size());
    }

    /**
     * 测试并发情况下，LRU 算法是否正确
     */
    @Test
    public void testConcurrency2() {
        // 足够小的 map，便于触发 LRU
        int capacity = 10;
        Map<String, Integer> map = new ThreadSafeLRUMap<>(capacity);
        for (int i = 0; i < capacity; i++) {
            map.put(String.valueOf(i), i);
        }
        // 启动 100 个线程，每个线程持续往 map 添加元素
        CompletableFuture<?>[] futures = new CompletableFuture[100];
        for (int i = 0; i < 100; i++) {
            int finalI = i;
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                for (int j = 0; j < 1000; j++) {
                    map.put(finalI + "-" + j, j);
                    assertEquals(capacity, map.size());
                }
            });
            futures[i] = future;
        }

        // 等待所有线程完成
        CompletableFuture.allOf(futures).join();

        // 检查Map的大小
        assertEquals(capacity, map.size());
    }

    @Test
    public void testComputeIfAbsent() {
        int capacity = 10000;
        Map<String, Integer> map = new ThreadSafeLRUMap<>(capacity);

        // 单线程测试
        map.put("1", 1);
        map.computeIfAbsent("1", k -> {
            return Integer.parseInt(k) + 100;
        });
        assertEquals(1, map.size());
        assertEquals(1, map.get("1"));

        map.computeIfAbsent("2", k -> {
            return Integer.parseInt(k) + 100;
        });
        assertEquals(2, map.size());
        assertEquals(102, map.get("2"));

        map.clear();

        // 多线程测试
        // 预填充数据
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < capacity / 100; j++) {
                // i-j 中的 j 是偶数时，才会被添加到 map 中，value 为 i * 2 + j * 3
                if (j % 2 == 0) {
                    map.put(i + "-" + j, i * 2 + j * 3);
                }
            }
        }
        assertEquals(capacity / 2, map.size());

        // 启动 100 个线程，每个线程持续往 map 添加元素，key 为 i-j，value 为 j
        CompletableFuture<?>[] futures = new CompletableFuture[100];
        for (int i = 0; i < 100; i++) {
            int finalI = i;
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                for (int j = 0; j < capacity / 100; j++) {
                    map.computeIfAbsent(finalI + "-" + j, k -> Integer.parseInt(k.split("-")[1]));
                }
            });
            futures[i] = future;
        }

        // 等待所有线程完成
        CompletableFuture.allOf(futures).join();

        assertEquals(capacity, map.size());

        // 运行后的结果应该为，i-j 中的 j 是偶数时，value 为 i * 2 + j * 3，否则为 j
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < capacity / 100; j++) {
                if (j % 2 == 0) {
                    assertEquals(i * 2 + j * 3, map.get(i + "-" + j));
                } else {
                    assertEquals(j, map.get(i + "-" + j));
                }
            }
        }
    }

}
