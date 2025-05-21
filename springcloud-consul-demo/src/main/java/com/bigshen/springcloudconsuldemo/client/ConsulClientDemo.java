package com.bigshen.springcloudconsuldemo.client;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.QueryParams;
import com.ecwid.consul.v1.Response;
import com.ecwid.consul.v1.kv.model.GetValue;

/**
 * @author byj
 * @date 2025/5/21
 * @Description 在控制台修改值后   控制台监听变化  达到准实时感知key变化的效果
 */
public class ConsulClientDemo {
    public static void main(String[] args) {
        System.out.println("开始演示KV监听机制");
        ConsulClient localhostClient = new ConsulClient("localhost");

        String key = "com.my.app.foo";

        // 给key设置一个值
        localhostClient.setKVValue(key, "My Lover");

        // 获取当前key的最新值和版本（consulIndex）
        Response<GetValue> curKvValue = localhostClient.getKVValue(key);

        // 长轮训监听该key在该index后的版本变化（达到准实时感知key变化的效果）
        Response<GetValue> updateKvValue = localhostClient.getKVValue(key, new QueryParams(30000, curKvValue.getConsulIndex()));

        System.out.println("更新后的值：" + updateKvValue.getValue().getDecodedValue());

        System.out.println("kv watch over");
    }
}
