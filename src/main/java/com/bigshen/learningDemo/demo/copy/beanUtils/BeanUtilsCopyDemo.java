package com.bigshen.learningDemo.demo.copy.beanUtils;

import com.bigshen.learningDemo.utils.copy.CommonUtil;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author byj
 * @date 2022/10/13
 * //浅拷贝
 * BeanUtils.copyProperties(s, t);
 * //深copy的方法
 *  1、重载clone()方法
 *  2、Apache Commons Lang序列化
 *  3、Gson序列化
 *  4、Jackson序列化
 */
public class BeanUtilsCopyDemo {

    @Test
    public void peopleDemo() throws CloneNotSupportedException {
        People people = new People();
        List<People.EatFood> eatFoods = new ArrayList<>();
        People.EatFood day1 = new People.EatFood();
        People.EatFood day2 = new People.EatFood();
        day1.setMorning("aaa");
        day2.setMorning("aaaaaa");
        day1.setNoon("bbb");
        day2.setNoon("bbbbbb");
        day1.setNight("ccc");
        day2.setNight("cccccc");
        eatFoods.add(day1);
        eatFoods.add(day2);
        people.setName("小明");
        people.setEatFoods(eatFoods);

        People people2 = (People) people.clone();
        people2.getEatFoods().add(new People.EatFood());
        System.out.println(people2.equals(people));
    }

    @Test
    public void ListPeopleDemo(){
        List<People> peopleList = new ArrayList<>();
        List<People.EatFood> eatFoods = new ArrayList<>();
        People.EatFood eatFood = new People.EatFood();
        eatFood.setMorning("aa");
        eatFood.setNoon("bb");
        eatFood.setNight("cc");
        eatFoods.add(eatFood);
        People people1 = new People();
        people1.setName("people1");
        people1.setEatFoods(eatFoods);
        People people2 = CommonUtil.convertBean(people1, People.class);
        people2.setName("people2");
        peopleList.add(people1);
        peopleList.add(people2);

        List<People> peopleList2 = CommonUtil.convertBeanList(peopleList, People.class);
        System.out.println(peopleList2);
    }
}
