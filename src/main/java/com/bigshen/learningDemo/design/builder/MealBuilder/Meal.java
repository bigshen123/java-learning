package com.bigshen.learningDemo.design.builder.MealBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:备餐
 * @Author: byj
 * @Date: 2019/12/3 15:14
 */
class Meal {
    private List<Item> items = new ArrayList<Item>();

    void addItem(Item item){
        items.add(item);
    }

    float getCost(){
        float cost = 0.0f;
        for (Item item : items) {
            cost += item.price();
        }
        return cost;
    }

    void showItems(){
        for (Item item : items) {
            System.out.print("Item : "+item.name());
            System.out.print(", Packing : "+item.packing().pack());
            System.out.println(", Price : "+item.price());
        }
    }
}