//package com.bigshen.learningDemo.demo.micrometer;
//
//import io.micrometer.core.instrument.Meter;
//import io.micrometer.core.instrument.MeterRegistry;
//import io.micrometer.core.instrument.binder.jvm.ClassLoaderMetrics;
//import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics;
//import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
//import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics;
//
//import java.util.HashSet;
//import java.util.List;
//
///**
// * @Author BYJ
// * @Date 2024/1/21 17:04
// * @Describe
// */
//public enum JvmMetricsBinder {
//    INSTANCE;
//
//    private MeterRegistry registry;
//
//    public synchronized void bind(MeterRegistry registry) {
//        if (this.registry == null) {
//            this.registry = registry;
//        }
//
//        new JvmInfoMetrics().bindTo(this.registry);
//        new JvmMemoryMetrics().bindTo(this.registry);
//        new JvmThreadMetrics().bindTo(this.registry);
//        new JvmHeapPressureMetrics().bindTo(this.registry);
//        new JvmGcMetrics().bindTo(this.registry);
//        new JvmCompilationMetrics().bindTo(this.registry);
//        new ClassLoaderMetrics().bindTo(this.registry);
//
//    }
//
//    public synchronized void reset() {
//        List<Meter> meters = this.registry.getMeters();
//        for (Meter meter : meters) {
//            // 遍历，移除通过当前单例构造的指标
//            if (JVM_METRICS_NAMES.contains(meter.getId().getName())) {
//                meter.close();
//                this.registry.remove(meter);
//            }
//        }
//    }
//
//    private static final HashSet<String> JVM_METRICS_NAMES = new HashSet<>();
//
//    static {
//        JVM_METRICS_NAMES.add("jvm.info");
//        JVM_METRICS_NAMES.add("jvm.buffer.count");
//        JVM_METRICS_NAMES.add("jvm.buffer.memory.used");
//        JVM_METRICS_NAMES.add("jvm.buffer.total.capacity");
//        JVM_METRICS_NAMES.add("jvm.memory.used");
//        JVM_METRICS_NAMES.add("jvm.memory.committed");
//        JVM_METRICS_NAMES.add("jvm.memory.max");
//        JVM_METRICS_NAMES.add("jvm.threads.peak");
//        JVM_METRICS_NAMES.add("jvm.threads.daemon");
//        JVM_METRICS_NAMES.add("jvm.threads.live");
//        JVM_METRICS_NAMES.add("jvm.threads.states");
//        JVM_METRICS_NAMES.add("jvm.memory.usage.after.gc");
//        JVM_METRICS_NAMES.add("jvm.gc.overhead");
//        JVM_METRICS_NAMES.add("jvm.gc.max.data.size");
//        JVM_METRICS_NAMES.add("jvm.gc.live.data.size");
//        JVM_METRICS_NAMES.add("jvm.gc.memory.allocated");
//        JVM_METRICS_NAMES.add("jvm.gc.memory.promoted");
//        JVM_METRICS_NAMES.add("jvm.gc.concurrent.phase.time");
//        JVM_METRICS_NAMES.add("jvm.gc.pause");
//        JVM_METRICS_NAMES.add("jvm.compilation.time");
//        JVM_METRICS_NAMES.add("jvm.classes.loaded");
//        JVM_METRICS_NAMES.add("jvm.classes.unloaded");
//    }
//}
