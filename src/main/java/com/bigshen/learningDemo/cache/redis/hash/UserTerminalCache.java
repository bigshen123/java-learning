package com.bigshen.learningDemo.cache.redis.hash;

/**
 * @Author BYJ
 * @Date 2025/3/4 21:28
 * @Describe
 */
import redis.clients.jedis.Jedis;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.*;

public class UserTerminalCache {
    private final Jedis jedis;
    private final ObjectMapper objectMapper;

    public UserTerminalCache(String redisHost) {
        this.jedis = new Jedis(redisHost);
        this.objectMapper = new ObjectMapper();
    }

    /**
     * 用户登录时更新终端信息
     * @param userId 用户ID
     * @param userGroupId 用户组ID (可能为空)
     * @param terminalId 新终端ID
     */
    public void updateUserTerminal(String userId, String userGroupId, String terminalId) {
        String key;

        // 确定 Redis 的存储 Key
        if (userGroupId != null && !userGroupId.isEmpty()) {
            key = "user_group:" + userGroupId;
        } else {
            key = "user_terminal:" + userId;
        }

        try {
            // 获取当前终端列表
            String terminalListJson = jedis.hget(key, userId);
            Set<String> terminalSet;

            if (terminalListJson != null) {
                terminalSet = objectMapper.readValue(terminalListJson, HashSet.class);
            } else {
                terminalSet = new HashSet<>();
            }

            // 添加新终端 ID
            terminalSet.add(terminalId);

            // 更新 Redis
            jedis.hset(key, userId, objectMapper.writeValueAsString(terminalSet));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取用户的终端列表
     * @param userId 用户ID
     * @param userGroupId 用户组ID (可能为空)
     * @return 终端ID列表
     */
    public Set<String> getUserTerminals(String userId, String userGroupId) {
        String key = (userGroupId != null && !userGroupId.isEmpty()) ? "user_group:" + userGroupId : "user_terminal:" + userId;

        try {
            String terminalListJson = jedis.hget(key, userId);
            if (terminalListJson != null) {
                return objectMapper.readValue(terminalListJson, HashSet.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new HashSet<>();
    }

    public static void main(String[] args) {
        UserTerminalCache cache = new UserTerminalCache("127.0.0.1");

        // 示例数据
        String userId = "user123";
        String userGroupId = "groupA";
        String terminal1 = "deviceA";
        String terminal2 = "deviceB";

        // 第一次登录
        cache.updateUserTerminal(userId, userGroupId, terminal1);
        System.out.println("终端列表: " + cache.getUserTerminals(userId, userGroupId));

        // 第二次登录，添加新终端
        cache.updateUserTerminal(userId, userGroupId, terminal2);
        System.out.println("终端列表: " + cache.getUserTerminals(userId, userGroupId));
    }
}

