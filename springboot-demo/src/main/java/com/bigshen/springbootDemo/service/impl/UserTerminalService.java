package com.bigshen.springbootDemo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserTerminalService {

    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public UserTerminalService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void saveUserTerminal(String userGroupId, String userId, String terminalId) {
        String key = "userGroup:" + userGroupId;

        // 获取终端集合
        Set<String> terminals = (Set<String>) redisTemplate.opsForHash().get(key, userId);
        if (terminals == null) {
            terminals = new HashSet<>();
        }

        if (terminals.contains(terminalId)) {
            System.out.println("用户 " + userId + " 的终端 " + terminalId + " 已存在，跳过处理");
            return;
        }

        // 添加终端并更新缓存
        terminals.add(terminalId);
        redisTemplate.opsForHash().put(key, userId, terminals);
        System.out.println("用户 " + userId + " 登录终端 " + terminalId + "，已存入 Redis");
    }
}
