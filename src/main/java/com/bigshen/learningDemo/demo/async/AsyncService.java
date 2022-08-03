package com.bigshen.learningDemo.demo.async;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;


/**
 * @Description:下面模拟一个查找对应字符开头电影的方法，我们给这个方法加上了 @Async注解来告诉
 * Spring 它是一个异步的方法。另外，这个方法的返回值 CompletableFuture.completedFuture(results)
 * 这代表我们需要返回结果，也就是说程序必须把任务执行完成之后再返回给用户。
 * @Author: BIGSHEN
 * @Date: 2019/12/29 10:19
 */
@Service
public class AsyncService {

    private static final Logger logger = LoggerFactory.getLogger(AsyncService.class);

    private List<String> movies =
            new ArrayList<>(
                    Arrays.asList(
                            "Forrest Gump",
                            "Titanic",
                            "Spirited Away",
                            "The Shawshank Redemption",
                            "Zootopia",
                            "Farewell ",
                            "Joker",
                            "Crawl"));

    /** 示范使用：找到特定字符/字符串开头的电影 */
    @Async
    public CompletableFuture<List<String>> completableFutureTask(String start) {
        // 打印日志
        logger.warn(Thread.currentThread().getName() + "start this task!");
        // 找到特定字符/字符串开头的电影
        List<String> results =
                movies.stream().filter(movie -> movie.startsWith(start)).collect(Collectors.toList());
        // 模拟这是一个耗时的任务
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //返回一个已经用给定值完成的新的CompletableFuture。
        return CompletableFuture.completedFuture(results);
    }
}
