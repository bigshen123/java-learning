package com.bigshen.learningDemo.design.builder.MealBuilder;

/**
 * @Description:备餐建造者类
 * @Author: byj
 * @Date: 2019/12/3 15:14
 */
public class MealBuilder {
    /**
     * 准备素食餐品
     * @return
     */
    Meal prepareVegMeal(){
        Meal meal = new Meal();
        meal.addItem(new VegBurger());
        meal.addItem(new Coke());
        return meal;
    }

    /**
     * 准备肉食餐品
     * @return
     */
    Meal prepareNonVegMeal(){
        Meal meal = new Meal();
        meal.addItem(new ChickenBurger());
        meal.addItem(new Pepsi());
        return meal;
    }
}