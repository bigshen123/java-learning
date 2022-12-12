package com.bigshen.learningDemo.lambda;


import com.bigshen.learningDemo.common.exception.ApiException;
import com.bigshen.learningDemo.concurrent.hashmap.User;
import com.bigshen.learningDemo.utils.entity.OperateInfo;
import com.bigshen.learningDemo.utils.entity.OrderInfo;
import com.bigshen.learningDemo.utils.entity.OrderInfoResponse;
import com.bigshen.learningDemo.utils.entity.Userinfo;
import com.bigshen.learningDemo.utils.enums.OrderTypeEnum;
import com.bigshen.learningDemo.utils.json.JacksonUtil;
import org.apache.commons.compress.utils.Lists;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @ClassName LambdaDemo
 * @Description:TODO
 * @Author: byj
 * @Date: 2020/5/26
 */
public class LambdaDemo {
    @Test
    public void forEachDemo() {
        ArrayList<String> list = new ArrayList<>(Arrays.asList("I", "LOVE", "YOU"));
        list.forEach(s -> {
            if (s.length() > 3) {
                System.out.println(s);
            }
        });
        // 使用forEach()结合Lambda表达式迭代Map
        HashMap<Integer, String> map = new HashMap<>();
        map.put(1, "one");
        map.put(2, "two");
        map.put(3, "three");
        map.forEach((k, v) -> System.out.println(k + "=" + v));
    }

    @Test
    public void removeIfDemo() {
        ArrayList<String> list = new ArrayList<>(Arrays.asList("I", "love", "you", "too"));
        /*Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()){
            if (iterator.next().length()>3){
                iterator.remove();
            }
        }*/
        list.removeIf(s -> s.length() > 3);
        System.out.println(list);
    }

    @Test
    public void replaceAllDemo() {
        // 使用下标实现元素替换
        ArrayList<String> list = new ArrayList<>(Arrays.asList("I", "love", "you", "too", "lovely"));
      /*  for(int i=0; i<list.size(); i++){
            String str = list.get(i);
            if(str.length()>3)
                list.set(i, str.toUpperCase());
        }*/
        list.replaceAll(str -> {
            if (str.length() > 3) {
                return str.toUpperCase();
            }
            return str;
        });
        // 使用replaceAll()结合Lambda表达式实现
        HashMap<Integer, String> map = new HashMap<>();
        map.put(1, "one");
        map.put(2, "two");
        map.put(3, "three");
        map.replaceAll((k, v) -> v.toUpperCase());
        System.out.println(list);
        System.out.println(map);
    }

    @Test
    public void sortDemo() {
        // Collections.sort()方法
        ArrayList<String> list = new ArrayList<>(Arrays.asList("I", "love", "you", "to"));
        /*Collections.sort(list, new Comparator<String>(){
            @Override
            public int compare(String str1, String str2){
                return str1.length()-str2.length();
            }
        });*/
        list.sort((str1, str2) -> str2.length() - str1.length());
        //list.sort(Comparator.comparingInt(String::length));
        System.out.println(list);
    }

    @Test
    public void getOrDefaultDemo() {
        // 查询Map中指定的值，不存在时使用默认值
        HashMap<Integer, String> map = new HashMap<>();
        map.put(1, "one");
        map.put(2, "two");
        map.put(3, "three");
        // Java7以及之前做法
    /*    if(map.containsKey(4)){ // 1
            System.out.println(map.get(4));
        }else{
            System.out.println("NoValue");
        }*/
        // Java8使用Map.getOrDefault()
        System.out.println(map.getOrDefault(4, "NoValue")); // 2
    }

    @Test
    public void computeIfAbsentDemo() {
        Map<Integer, Set<String>> map = new HashMap<>();
        // Java7及以前的实现方式
        /*if(map.containsKey(1)){
            map.get(1).add("one");
        }else{
            Set<String> valueSet = new HashSet<>();
            valueSet.add("one");
            map.put(1, valueSet);
        }*/
        // Java8的实现方式
        map.computeIfAbsent(1, v -> new HashSet<>()).add("yi");
        System.out.println(map);
    }

    @Test
    public void streamDemo() {
        Stream<String> stream = Stream.of("I", "love", "you", "too");
        ArrayList<Object> list = stream.collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        System.out.println(list);
        List<Userinfo> userinfos = new ArrayList<>();
        Date dbDate = new Date();
        List<Date> dates = new ArrayList<>();
        long time = dbDate.getTime();
        time += 60000;
        Date date = new Date(time);
        System.out.println(date.getTime());
        Date date1 = new Date(1668505658191L);
        System.out.println(date1);
        System.out.println(date);
        dates.add(date);
        if (dates.stream().anyMatch(i -> i.after(new Date(i.getTime() + 180000)))) {
            System.out.println("adsasad");
        }
    }

    @Test
    public void test() {
        List<OrderInfo> orderInfoList = Lists.newArrayList();
        List<OperateInfo> operateInfoList = Lists.newArrayList();
        orderInfoList.add(new OrderInfo(1, 1, 2, 2));
        orderInfoList.add(new OrderInfo(2, 2, 3, 1));
        operateInfoList.add(new OperateInfo(1, "增加"));
        operateInfoList.add(new OperateInfo(2, "修改"));
        operateInfoList.add(new OperateInfo(3, "删除"));
        //operateInfoList转map(key:operateId  value:operateName)
        Map<Integer, String> operateMap = operateInfoList.stream().collect(Collectors.
                toMap(OperateInfo::getOperateId, OperateInfo::getOperateName, (x, y) -> x));

        //orderInfoList分组（key:type  value:List<OrderInfo>）
        Map<Integer, List<OrderInfo>> orderInfoGroup = orderInfoList.stream()
                .filter(order -> order.getType() != null)
                .collect(Collectors.groupingBy(OrderInfo::getType));

        List<List<OrderInfoResponse>> result = new ArrayList<>();
        orderInfoGroup.values().forEach(list -> {
            //根据orderId递减
            list = list.stream().sorted(Comparator.comparing(OrderInfo::getOrderId).reversed()).collect(Collectors.toList());

            //List<OrderInfo>转List<OrderInfoResponse>
            List<OrderInfoResponse> responses = list.stream().map(order -> {
                OrderInfoResponse response = new OrderInfoResponse();
                response.setOrderId(order.getOrderId());
                response.setTypeName(OrderTypeEnum.getNameByCode(order.getType()));
                response.setCreatorName(operateMap.get(order.getCreatorId()));
                return response;
            }).collect(Collectors.toList());
            result.add(responses);
        });

        //orderInfoList第一个type为TYPE1的对象
        OrderInfo orderInfo = orderInfoList.stream()
                .filter(order -> OrderTypeEnum.TYPE2.getCode().equals(order.getType()))
                .findFirst().orElse(null);
        System.out.println(orderInfo);
    }

    @Test
    public void peekTest() {
        List<Persion> lists = new ArrayList<Persion>();
        lists.add(new Persion(1L, "p1"));
        lists.add(new Persion(2L, "p2"));
        lists.add(new Persion(3L, "p3"));
        lists.add(new Persion(4L, "p4"));
        System.out.println(lists);

        List<Persion> list2 = lists.stream()
                .filter(f -> f.getName().startsWith("p"))
                .peek(t -> System.out.println(t.getName()))
                .collect(Collectors.toList());

        System.out.println(list2);
    }

    @Test
    public void demo() {
        String[] test = {"tom", "jerry", "lily", "lucy", "apple", "atom", "toms", "tommy"};
        List<String> list = Arrays.asList(test);
        list.stream().filter(s -> s.length() >= 4).filter(s -> s.contains("tom")).map(s -> s + s.length()).forEach(System.out::println);
    }

    @Test
    public void matchTest() {
        // 定制排序
        List<User> employees = Arrays.asList(
                new User("张三", 18),
                new User("李四", 38),
                new User("王五", 50),
                new User("赵六", 16),
                new User("田七", 8)
        );
        //anyMatch 检查是否匹配所有元素
        Boolean isAllMatch = employees.stream().allMatch((x) -> x.getAge() > 10);
        System.out.println("是否匹配所有元素:" + isAllMatch);
        //anyMatch：检查是否至少匹配一个元素
        Boolean isAnyMatch = employees.stream().anyMatch((x) -> x.getAge() > 10);
        System.out.println("是否匹配至少一个元素:" + isAnyMatch);
    }

    @Test
    public void reduceTest() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        // 按照下面的规则进行累加操作
        // reduce的规约，就是把前面定义的起始值，作为了x
        Integer num = list.stream().reduce(0, Integer::sum);
        System.out.println(num);
    }

    @Test
    public void findFirstTest() {
        List<User> users = new ArrayList<>();
        User user1 = new User();
        user1.setAge(9);
        user1.setName("test");
        users.add(user1);
        User user = users.stream().findFirst().orElseThrow(() ->
                new ApiException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "资源变动配置json为空"));
        System.out.println(user.getAge());
    }

}
