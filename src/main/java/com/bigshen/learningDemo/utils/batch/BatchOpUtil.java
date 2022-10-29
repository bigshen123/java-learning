package com.bigshen.learningDemo.utils.batch;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

/**
 * @author byj
 * @date 2022/10/26
 */
@Slf4j
public class BatchOpUtil {
    /**
     * 批量对id集合中id 进行操作（action中可含异步操作此时idCompletableFutures 需初始化，并将异步操作CompletableFuture&lt;Boolean&gt; 放入）
     *
     * @param ids                  被操作的id集合
     * @param action               针对id集合元素的 操作内容
     * @param idCompletableFutures id 进行异步 的对象集合，action中可含异步操作此时 该参数 需初始化传入，并将异步操作结果对象 CompletableFuture<Boolean> 放入
     * @param opName               操作名称
     * @return id集合中id 进行操作 成功的id
     */
    public static List<String> batchIdsOp(@NotNull Collection<String> ids, @NotNull Consumer<String> action,
                                          @Nullable Map<String, CompletableFuture<Boolean>> idCompletableFutures,
                                          @NotNull String opName) {
        ArrayList<String> successIds = new ArrayList<>();
        if (ids.isEmpty()) {
            log.warn("批量操作{}无id:{}", opName, ids);
            return successIds;
        }
        ids.forEach(action);
        if (MapUtils.isEmpty(idCompletableFutures)) {
            successIds.addAll(ids);
            log.warn("操作{} ids:{}无异步操作", opName, ids);
            return successIds;
        }
        CompletableFuture<Boolean>[] futureArray = new CompletableFuture[ids.size()];
        idCompletableFutures.values().toArray(futureArray);
        AtomicBoolean hasInterrupt = new AtomicBoolean(false);
        try {
            CompletableFuture.allOf(futureArray).get();
        } catch (Exception e) {
            log.error("批量操作{}失败, ids:{}, error:{}", opName, ids, e.getMessage(), e);
            if (e instanceof InterruptedException) {
                hasInterrupt.compareAndSet(false, true);
            }
        }
        idCompletableFutures.forEach((id, future) -> {
            try {
                if (future.get()) {
                    successIds.add(id);
                } else {
                    log.error("批量操作{}失败, id:{}", opName, id);
                }
            } catch (Exception ex) {
                log.error("批量操作{}失败, id:{}, error:{}", opName, id, ex.getMessage(), ex);
                if (ex instanceof InterruptedException) {
                    hasInterrupt.compareAndSet(false, true);
                }
            }
        });
        if (hasInterrupt.get()) {
            Thread.currentThread().interrupt();
        }
        return successIds;
    }
}
