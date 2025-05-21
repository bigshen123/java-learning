package com.bigshen.springcloudconsuldemo.client;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.QueryParams;
import com.ecwid.consul.v1.Response;
import com.ecwid.consul.v1.catalog.CatalogServiceRequest;
import com.ecwid.consul.v1.catalog.CatalogServicesRequest;
import com.ecwid.consul.v1.catalog.model.CatalogService;
import com.ecwid.consul.v1.health.HealthServicesRequest;
import com.ecwid.consul.v1.health.model.HealthService;

import java.util.List;
import java.util.Map;

/**
 * @author byj
 * @date 2025/5/21
 * @Description
 */
public class ConsulServiceFind {
    public static void main(String[] args) {
        ConsulClient localhostClient = new ConsulClient("localhost");
        String serviceName = "myapp";

        // 基于服务名称查询所有健康的服务
        HealthServicesRequest request = HealthServicesRequest.newBuilder()
                .setPassing(true)
                .setQueryParams(QueryParams.DEFAULT)
                .build();
        Response<List<HealthService>> healthyServices = localhostClient.getHealthServices(serviceName, request);
        System.out.println(serviceName + "有" + healthyServices.getValue().size() + "个健康实例");

        // 基于某个Tag查询所有健康的服务
        HealthServicesRequest request1 = HealthServicesRequest.newBuilder()
                .setTag("EU-West")
                .setPassing(true)
                .setQueryParams(QueryParams.DEFAULT)
                .build();
        Response<List<HealthService>> healthyServices1 = localhostClient.getHealthServices("myapp", request1);
        System.out.println(serviceName + "有" + healthyServices1.getValue().size() + "个健康实例");

        // 查询所有的名为myapp的实例（包括健康检查没过的）
        Response<List<CatalogService>> myappServices = localhostClient.getCatalogService(serviceName, CatalogServiceRequest.newBuilder()
                .setQueryParams(QueryParams.DEFAULT).build());
        System.out.println(serviceName + "一共有" + myappServices.getValue().size() + "个实例");

        // 查询所有的服务（包括健康检查没过的）
        Response<Map<String, List<String>>> allServices = localhostClient.getCatalogServices(CatalogServicesRequest.newBuilder()
                .setQueryParams(QueryParams.DEFAULT).build());
        System.out.println("一共有" + allServices.getValue().size() + "个实例");


        // 长轮训监听所有健康服务的变化
        Response<List<HealthService>> curHealthServices = localhostClient.getHealthServices(serviceName, HealthServicesRequest.newBuilder().build());
        Response<List<HealthService>> latestHealthServices = localhostClient.getHealthServices(serviceName, HealthServicesRequest.newBuilder()
                .setQueryParams(QueryParams.Builder.builder()
                        .setWaitTime(300000)
                        .setIndex(curHealthServices.getConsulIndex())
                        .build())
                .build());

        System.out.println("更新后的最新服务个数：" + latestHealthServices.getValue().size());
    }
}
