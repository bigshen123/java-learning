package com.bigshen.learningDemo.demo.copy.beanUtils;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * @author byj
 * @date 2022/10/13
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class People implements Serializable,Cloneable {
    private Integer id = 1;
    private String name = "";
    private Integer age = 1;
    private Integer sex = 1;
    private List<EatFood> eatFoods;

    @Override
    protected Object clone() throws CloneNotSupportedException {
        People clone = (People) super.clone();
        clone.eatFoods.forEach(eatFood -> {
            try {
                eatFood = (EatFood) eatFood.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        });
        return super.clone();
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EatFood implements Serializable,Cloneable  {
        private String morning;
        private String noon;
        private String night;

        @Override
        protected Object clone() throws CloneNotSupportedException {
            return super.clone();
        }
    }
}
