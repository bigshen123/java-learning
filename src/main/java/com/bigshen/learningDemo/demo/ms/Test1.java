package com.bigshen.learningDemo.demo.ms;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author BYJ
 * @Date 2023/3/12 10:16
 * @Describe
 */
public class Test1 {
    public static void main(String[] args) {
        List<Order> orderList = new ArrayList<>();
        Order order1 = new Order(1, "shopA", new BigDecimal(10));
        Order order2 = new Order(2, "shopB", new BigDecimal(20));
        Order order3 = new Order(3, "shopB", new BigDecimal(20));
        Order order4 = new Order(4, "shopC", new BigDecimal(10));
        Order order5 = new Order(5, "shopC", new BigDecimal(20));
        Order order6 = new Order(6, "shopC", new BigDecimal(30));
        Order order7 = new Order(7, "shopC", null);
        orderList.add(order1);
        orderList.add(order2);
        orderList.add(order3);
        orderList.add(order4);
        orderList.add(order5);
        orderList.add(order6);
        orderList.add(order7);

        func1(orderList);
        //func2(orderList);
    }

    private static void func2(List<Order> orderList) {
        Map<String, List<Order>> ordersByShop = new HashMap<>();
        for (Order order : orderList) {
            if (order.getValue() != null) {
                String shop = order.getShop();
                if (!ordersByShop.containsKey(shop)) {
                    ordersByShop.put(shop, new ArrayList<>());
                }
                ordersByShop.get(shop).add(order);
            }
        }
        Map<String, Integer> valueSumByShop = new HashMap<>();
        for (Map.Entry<String, List<Order>> entry : ordersByShop.entrySet()) {
            int sum = 0;
            for (Order order : entry.getValue()) {
                sum += order.getValue().intValue();
            }
            valueSumByShop.put(entry.getKey(), sum);
        }
        Map<String, Double> valueAvgByShop = new HashMap<>();
        for (Map.Entry<String, List<Order>> entry : ordersByShop.entrySet()) {
            double sum = 0;
            int count = 0;
            for (Order order : entry.getValue()) {
                sum += order.getValue().doubleValue();
                count++;
            }
            if (count > 0) {
                double avg = sum / count;
                valueAvgByShop.put(entry.getKey(), Math.round(avg * 100.0) / 100.0);
            }
        }
        Map<String, Integer[]> valueMinMaxByShop = new HashMap<>();
        for (Map.Entry<String, List<Order>> entry : ordersByShop.entrySet()) {
            int min = Integer.MAX_VALUE;
            int max = Integer.MIN_VALUE;
            for (Order order : entry.getValue()) {
                int value = order.getValue().intValue();
                if (value < min) {
                    min = value;
                }
                if (value > max) {
                    max = value;
                }
            }
            valueMinMaxByShop.put(entry.getKey(), new Integer[]{min, max});
        }
    }

    private static void func1(List<Order> orderList) {
        //1 按照shop分组，可以使用Java 8的Stream API的groupingBy方法进行分组：
        Map<String, List<Order>> ordersByShop = orderList.stream()
                .filter(order -> order.getValue() != null)
                .collect(Collectors.groupingBy(Order::getShop));
        System.out.println(ordersByShop);
        //2 求各个shop的value之和，可以使用Java 8的Stream API的mapToInt和sum方法：
        Map<String, Integer> valueSumByShop = ordersByShop.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        entry -> entry.getValue().stream()
                                .mapToInt(order -> order.getValue().intValue())
                                .sum()));
        System.out.println(valueSumByShop);
        //3 求各个shop的value的平均值，可以使用Java 8的Stream API的mapToDouble和average方法：
        Map<String, Double> valueAvgByShop = ordersByShop.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        entry -> entry.getValue().stream()
                                .mapToDouble(order -> order.getValue().doubleValue())
                                .average()
                                .orElse(Double.NaN)));
        System.out.println(valueAvgByShop);
        //4 求各个shop的value最大、最小值，可以使用Java 8的Stream API的mapToInt和summaryStatistics方法
        Map<String, IntSummaryStatistics> valueStatsByShop = ordersByShop.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        entry -> entry.getValue().stream()
                                .mapToInt(order -> order.getValue().intValue())
                                .summaryStatistics()));
        System.out.println(valueStatsByShop);
    }
}
