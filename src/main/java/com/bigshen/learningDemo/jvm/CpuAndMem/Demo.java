package com.bigshen.learningDemo.demo.jvm.CpuAndMem;


import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author bayj@koal.com
 */
@Slf4j
public class Demo {
    private static final String LINUX = "linux";
    private static final Integer CPU_THRESHOLD_VALUE = 80;
    private static final Integer MEM_THRESHOLD_VALUE = 80;

    /**
     * 获取Linux内存使用率
     *
     * @return int
     */
    public static int getLinuxUsedMemoryRate() {
        BigDecimal totalMemorySize = new BigDecimal("0.00");
        BigDecimal availableMemorySize = new BigDecimal("0.00");
        int usedMemoryRate = 0;
        String os = getOS();
        if (LINUX.equals(os)) {
            String cmd = "cat /proc/meminfo";
            List<String> result = null;
            try {
                result = executeLinuxCmd(cmd);
            } catch (Exception exception) {
                log.error("执行Linux命令 cat /proc/meminfo 失败");
            }
            if (result != null && result.size() > 0) {
                for (String str : result) {
                    System.out.println(str);
                    if (str.contains("MemTotal")) {
                        str = str.replaceAll("\\D", "");
                        totalMemorySize = new BigDecimal(str);
                        continue;
                    }
                    if (str.contains("MemAvailable")) {
                        System.out.println(str);
                        str = str.replaceAll("\\D", "");
                        availableMemorySize = new BigDecimal(str);
                        break;
                    }
                }
            }
            log.info("总内存：{}", totalMemorySize);
            log.info("可用内存：{}", availableMemorySize);
            BigDecimal usedMemorySize = totalMemorySize.subtract(availableMemorySize);
            log.info("已使用内存：{}", usedMemorySize);
            BigDecimal divide = usedMemorySize.divide(totalMemorySize, 2, BigDecimal.ROUND_HALF_UP);
            usedMemoryRate = (int) (divide.doubleValue() * 100);
        }
        return usedMemoryRate;
    }

    /**
     * 执行Linux命令，并返回命令行输出结果
     *
     * @param cmd 命令
     * @return 命令行输出结果
     * @throws Exception
     */
    private static List<String> executeLinuxCmd(String cmd) throws Exception {
        List<String> result = new ArrayList<String>();
        Process process = null;

        String[] cmds = {"/bin/sh", "-c", new String(cmd.getBytes()), "utf-8"};
        process = Runtime.getRuntime().exec(cmds);

        BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream(),
                StandardCharsets.UTF_8), 1024);
        String line = null;
        while ((line = br.readLine()) != null) {
            result.add(line);
        }
        return result;
    }

    /**
     * 获取操作系统类型
     *
     * @return os
     */
    public static String getOS() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("windows")) {
            return "windows";
        }
        if (os.contains("linux")) {
            return "linux";
        }
        return null;
    }

    /**
     * 功能：获取CPU使用信息
     */
    public static Map<?, ?> cpuinfo() {
        Map<String, Object> map = new HashMap<>(12);
        try (InputStreamReader inputs = new InputStreamReader(new FileInputStream("/proc/stat"), StandardCharsets.UTF_8);
             BufferedReader buffer = new BufferedReader(inputs)) {
            String line = "";
            while (true) {
                line = buffer.readLine();
                if (line == null) {
                    break;
                }
                if (line.startsWith("cpu")) {
                    StringTokenizer tokenizer = new StringTokenizer(line);
                    List<String> temp = new ArrayList<>();
                    while (tokenizer.hasMoreElements()) {
                        String value = tokenizer.nextToken();
                        temp.add(value);
                    }
                    map.put("user", temp.get(1));
                    map.put("nice", temp.get(2));
                    map.put("system", temp.get(3));
                    map.put("idle", temp.get(4));
                    map.put("iowait", temp.get(5));
                    map.put("irq", temp.get(6));
                    map.put("softirq", temp.get(7));
                    map.put("stealstolen", temp.get(8));
                    break;
                }
            }
        } catch (IOException e) {
            log.error("获取CPU使用信息失败:{}", e.getMessage());
        }
        return map;
    }

    /**
     * 功能：获取Linux系统cpu使用率
     */
    public static int cpuUsage() {
        try {
            Map<?, ?> map1 = Demo.cpuinfo();
            //Thread.sleep(5 * 1000);
            //Map<?, ?> map2 = SysCpuAndMemUseUtil.cpuinfo();

            long user1 = Long.parseLong(map1.get("user").toString());
            log.info("cpu使用：{}", user1);
            long nice1 = Long.parseLong(map1.get("nice").toString());
            long system1 = Long.parseLong(map1.get("system").toString());
            long idle1 = Long.parseLong(map1.get("idle").toString());

            //long user2 = Long.parseLong(map2.get("user").toString());
            //long nice2 = Long.parseLong(map2.get("nice").toString());
            //long system2 = Long.parseLong(map2.get("system").toString());
            //long idle2 = Long.parseLong(map2.get("idle").toString());

            float total1 = user1 + system1 + nice1;
            //long total2 = user2 + system2 + nice2;
            //float total = total2 - total1;

            float totalIdle1 = user1 + nice1 + system1 + idle1;
            //long totalIdle2 = user2 + nice2 + system2 + idle2;
            //float totalidle = totalIdle2 - totalIdle1;

            //float cpusage = (total / totalidle) * 100;
            float cpusage = (total1 / totalIdle1) * 100;
            return (int) cpusage;
        } catch (Exception e) {
            log.error("获取CPU使用率失败:{}", e.getMessage());
        }
        return 0;
    }

    /**
     * 功能：获取内存使用率
     */
    public static int memoryUsage() {
        Map<String, Object> map = new HashMap<>(12);
        try (InputStreamReader inputs = new InputStreamReader(new FileInputStream("/proc/meminfo"), StandardCharsets.UTF_8);
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

            double usage = (double) (memused - buffers - cached) / memTotal * 100;
            return (int) usage;
        } catch (Exception e) {
            log.error("获取内存使用率失败:{}", e.getMessage());
        }
        return 0;
    }

    /**
     * 检测系统CPU和MEM使用率是否够用
     */
    public static Boolean sysCpuAndMemCheck() {

        String os = getOS();
        if (os != null && os.equals(LINUX)) {
            // 系统CPU占用百分比
            int cpuUsage = cpuUsage();
            log.info("系统CPU使用率:{}", cpuUsage);

            //系统内存占用百分比
            int memoryUsage = memoryUsage();
            log.info("系统内存使用率:{}", memoryUsage);

            if (cpuUsage > CPU_THRESHOLD_VALUE) {
                log.info("当前CPU使用率较高，停止执行定时任务......");
                return false;
            }

            if (memoryUsage > MEM_THRESHOLD_VALUE) {
                log.info("当前Mem使用率较高，停止执行定时任务......");
                return false;
            }
        }
        return true;
    }



    public static void main(String[] args) {

    }
}
