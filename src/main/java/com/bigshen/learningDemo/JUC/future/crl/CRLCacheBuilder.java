package com.bigshen.learningDemo.JUC.future.crl;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author byj
 * @date 2024/7/16
 * @Description
 */
@Slf4j
public class CRLCacheBuilder {
    private static final ExecutorService executor = Executors.newFixedThreadPool(2);
    private static final ConcurrentHashMap<String, TaskStatus> taskStatusMap = new ConcurrentHashMap<>();

    public static String submitBuildCrlCacheTask(String crlDir, String crlCacheFilePath) {
        String taskId = UUID.randomUUID().toString();
        taskStatusMap.put(taskId, TaskStatus.PENDING);

        executor.submit(() -> {
            taskStatusMap.put(taskId, TaskStatus.IN_PROGRESS);
            try {
                buildCrlCache(crlDir, crlCacheFilePath);
                taskStatusMap.put(taskId, TaskStatus.SUCCESS);
            } catch (IOException e) {
                taskStatusMap.put(taskId, TaskStatus.FAILED);
            }
        });

        return taskId;
    }

    public static TaskStatus getTaskStatus(String taskId) {
        return taskStatusMap.get(taskId);
    }

    private static void buildCrlCache(String crlDir, String crlCacheFilePath) throws IOException {
        try {
            String cmd = "/usr/local/ssl/bin/CRLBin/BuildCRLCache -s -d " + crlDir + " -c " + crlCacheFilePath;
            log.info("BuildCRLCache,cmd:{}", cmd);
            Process p = Runtime.getRuntime().exec(cmd);
            if (!p.waitFor(180, TimeUnit.SECONDS)) {
                p.destroyForcibly();
            }
            if (p.exitValue() != 0) {
                throw new IOException("构建CRLCache文件失败,响应结果非0");
            }

            cmd = "/usr/local/ssl/bin/CRLBin/CheckCRLCache -f " + crlCacheFilePath;
            log.info("CheckCRLCache,cmd:{}", cmd);
            p = Runtime.getRuntime().exec(cmd);
            if (!p.waitFor(180, TimeUnit.SECONDS)) {
                p.destroyForcibly();
            }
            if (p.exitValue() != 0) {
                throw new IOException("检查CRLCache文件失败,响应结果非0");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("构建CRLCache失败", e);
        }
    }
}
