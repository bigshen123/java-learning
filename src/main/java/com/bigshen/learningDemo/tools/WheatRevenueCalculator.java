package com.bigshen.learningDemo.tools;

import java.util.Scanner;

/**
 * @author byj
 * @date 2024/6/21
 * @Description 实用工具 计算农产量的收益
 */
public class WheatRevenueCalculator {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // 输入总产量（单位：斤）
        System.out.print("请输入小麦的总产量（斤）：");
        double totalYield = scanner.nextDouble();

        // 输入小麦单价（单位：元/斤）
        System.out.print("请输入小麦的单价（元/斤）：");
        double unitPrice = scanner.nextDouble();

        // 输入总亩数
        System.out.print("请输入总亩数：");
        double totalAcres = scanner.nextDouble();

        // 计算总收益
        double totalRevenue = calculateTotalRevenue(totalYield, unitPrice);

        // 计算每亩地的收益
        double revenuePerAcre = calculateRevenuePerAcre(totalRevenue, totalAcres);

        // 打印结果
        System.out.printf("总收益：%.2f 元%n", totalRevenue);
        System.out.printf("每亩地的收益：%.2f 元%n", revenuePerAcre);
        System.out.printf("150亩地的收益：%.2f 元%n", 150 * revenuePerAcre);
        System.out.printf("250亩地的收益：%.2f 元%n", 250 * revenuePerAcre);
    }

    /**
     * 计算总收益
     *
     * @param totalYield 总产量（斤）
     * @param unitPrice 单价（元/斤）
     * @return 总收益（元）
     */
    public static double calculateTotalRevenue(double totalYield, double unitPrice) {
        return totalYield * unitPrice;
    }

    /**
     * 计算每亩地的收益
     *
     * @param totalRevenue 总收益（元）
     * @param totalAcres 总亩数
     * @return 每亩地的收益（元）
     */
    public static double calculateRevenuePerAcre(double totalRevenue, double totalAcres) {
        return totalRevenue / totalAcres;
    }
}
