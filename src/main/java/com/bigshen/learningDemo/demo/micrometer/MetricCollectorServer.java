package com.bigshen.learningDemo.demo.micrometer;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ReflectUtil;
import io.micrometer.core.instrument.Clock;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.prometheus.client.Collector;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.exporter.HTTPServer;
import lombok.extern.slf4j.Slf4j;
import ucs.common.core.exception.ApplicationException;
import ucs.serve.adapter.server.support.promethues.metrics.api.ApiDatagramMetricsCollector;
import ucs.serve.adapter.server.support.promethues.metrics.api.ApiRequestMetricsCollector;
import ucs.serve.adapter.server.support.promethues.metrics.runtime.CrlCacheGaugeCollector;
import ucs.serve.adapter.server.support.promethues.metrics.runtime.JvmMetricsBinder;
import ucs.serve.adapter.server.support.promethues.metrics.runtime.OcspHistogramMetricsCollector;
import ucs.serve.adapter.server.support.promethues.metrics.runtime.ServerWorkThreadMetricsBinder;
import ucs.shared.cfg.sub.ServerCfg;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;

/**
 * @author yang rs
 * @date 2023/12/28 10:21
 */
@Slf4j
public class MetricCollectorServer {

    private static HTTPServer Server;

    private static PrometheusMeterRegistry prometheusMeterRegistry;

    public static synchronized void startServer(ServerCfg serverCfg) {
        // 关闭服务
        stopServer();
        // 避免重复注册的情况
        Map<Collector, List<String>> collectorListMap = (Map<Collector, List<String>>) ReflectUtil.getFieldValue(CollectorRegistry.defaultRegistry, "collectorsToNames");
        if (CollectionUtil.isNotEmpty(collectorListMap)) {
            _start0(serverCfg);
            return;
        }

        // 创建 Micrometer 的 PrometheusMeterRegistry
        prometheusMeterRegistry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT, CollectorRegistry.defaultRegistry, Clock.SYSTEM);

        // ----------------- 配置加载指标  -----------------

        // ----------------- 运行状态指标  -----------------
        // 注册 JVM指标
        JvmMetricsBinder.INSTANCE.bind(prometheusMeterRegistry);

        _start0(serverCfg);
    }

    private static synchronized void _start0(ServerCfg serverCfg) {
        try {
            int port = Convert.toInt(serverCfg.getServerPort());
            Server = new HTTPServer(new InetSocketAddress(port), prometheusMeterRegistry.getPrometheusRegistry(), true);
            log.info("监控采集服务启动成功，listenPort:{}", port);
        } catch (IOException e) {
            throw new ApplicationException(e.getMessage(), e);
        }
    }

    public static synchronized void stopServer() {
        if (Server != null) {
            Server.close();
            Server = null;
            log.warn("监控采集服务已关闭");
        }
    }

}
