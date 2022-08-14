package com.bigshen.learningDemo.demo.jvm.CpuAndMem;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author BYJ
 * @Date 2022/5/7 15:26
 * @Describe
 */
public class SysCpuAndMemUsed {

    private final static String MEM_CMD = "/proc/meminfo";
    private final static String CPU_CMD = "/proc/stat";

    private static String[] getCpuInfo() {
        String[] cpuInfos = null;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(CPU_CMD)), 1000);
            String load = reader.readLine();
            cpuInfos = load.split(" ");
        } catch (Throwable ex) {
            System.out.println();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Throwable e) {
                    System.out.println("");
                }
            }
        }
        return cpuInfos;
    }

    public static int getCpuRate() {
        try {
            String[] cpuInfos1 = getCpuInfo();
            long startTotalCpuTimes = Long.parseLong(cpuInfos1[2])
                    + Long.parseLong(cpuInfos1[3]) + Long.parseLong(cpuInfos1[4])
                    + Long.parseLong(cpuInfos1[6]) + Long.parseLong(cpuInfos1[5])
                    + Long.parseLong(cpuInfos1[7]) + Long.parseLong(cpuInfos1[8]);
            long startIdle = Long.parseLong(cpuInfos1[5]);
            Thread.sleep(200);
            String[] cpuInfos2 = getCpuInfo();
            long endTotalCpuTimes = Long.parseLong(cpuInfos2[2])
                    + Long.parseLong(cpuInfos2[3]) + Long.parseLong(cpuInfos2[4])
                    + Long.parseLong(cpuInfos2[6]) + Long.parseLong(cpuInfos2[5])
                    + Long.parseLong(cpuInfos2[7]) + Long.parseLong(cpuInfos2[8]);
            long endIdle = Long.parseLong(cpuInfos2[5]);
            //cpu总使用率 = （（totalTime2 - totalTime1）- （IdleTime2 - IdleTime1））/ （totalTime2 - totalTime1）
            return (int) (100 * ((endTotalCpuTimes - startTotalCpuTimes) - (endIdle - startIdle)) /
                    (endTotalCpuTimes - startTotalCpuTimes));
        } catch (Throwable e) {
            return 0;
        }
    }

    public static int memoryUsage() {
        Map<String, Object> map = new HashMap<>(12);
        try (InputStreamReader inputs = new InputStreamReader(new FileInputStream(MEM_CMD), StandardCharsets.UTF_8);
             BufferedReader buffer = new BufferedReader(inputs)) {
            String line = "";
            while (true) {
                line = buffer.readLine();
                if (line == null) {
                    break;
                }
                int beginIndex = 0;
                int endIndex = line.indexOf(":");
                if (endIndex != -1) {
                    String key = line.substring(beginIndex, endIndex);
                    beginIndex = endIndex + 1;
                    endIndex = line.length();
                    String memory = line.substring(beginIndex, endIndex);
                    String value = memory.replace("kB", "").trim();
                    map.put(key, value);
                }
            }

            long memTotal = Long.parseLong(map.get("MemTotal").toString());
            long memFree = Long.parseLong(map.get("MemFree").toString());
            long memused = memTotal - memFree;
            long buffers = Long.parseLong(map.get("Buffers").toString());
            long cached = Long.parseLong(map.get("Cached").toString());
            //mem总使用率 = （memused-buffers-cached）/ memTotal * 100
            double usage = (double) (memused - buffers - cached) / memTotal * 100;
            return (int) usage;
        } catch (Exception e) {
            System.out.println("");
        }
        return 0;
    }


    public static void main(String[] args) {
        //系统CPU占用百分比
        int cpuRate = getCpuRate();
        System.out.println("系统CPU使用率是：" + cpuRate);
        //系统内存占用百分比
        int memoryUsage = memoryUsage();
        System.out.println("系统内存使用率:" + memoryUsage);
    }
}
