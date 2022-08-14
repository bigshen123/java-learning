package com.bigshen.learningDemo.utils.json;

/**
 * @ClassName TestFastJson
 * @Description:TODO
 * @Author: byj
 * @Date: 2020/5/26
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import com.bigshen.learningDemo.utils.entity.Userinfo;
import org.junit.Test;

import java.util.*;

/**
 * fastjson 是一个性能很好的 Java 语言实现的 JSON 解析器和生成器，来自阿里巴巴的工程师开发。 主要特点：
 * 1.快速FAST(比其它任何基于Java的解析器和生成器更快，包括jackson） 强大（支持普通JDK类包括任意Java Bean
 * 2.Class、Collection、Map、Date或enum） 零依赖（没有依赖其它任何类库除了JDK）
 */

public class TestFastJson {

    public static void main(String[] args) {
        String json = "{\"name\":\"chenggang\",\"age\":24}";
        String arrayAyy = "[[\'马云',50],null,[\'马化腾',30]]";
//        Entity2json("zhangsan", 24);
//        list2Json();
        //Complexdata();
//        Deserialization(json);
//        DateFormate(new Date());
//        Json2Eetity(json);
        String2JSONArray(arrayAyy);
        //JsonArrayTest();
    }

    /**
     * 实体转为Json
     */
    public static void Entity2json(String name, int age) {
        Userinfo info = new Userinfo(name, age);
        String str_json = JSON.toJSONString(info);
        System.out.println("实体转化为Json" + str_json);
    }

    /**
     * list转Json
     */
    public static void list2Json() {
        List<Userinfo> list = new ArrayList<Userinfo>();
        Userinfo userinfo1 = new Userinfo("lisi", 15);
        Userinfo userinfo2 = new Userinfo("wangwu", 16);
        list.add(userinfo1);
        list.add(userinfo2);
        String json = JSON.toJSONString(list, true);
        System.out.println("List集合转json格式字符串 :" + json);
    }

    /**
     * 字符数组转化为JSon
     *
     * @param arrayAyy
     */
    private static void String2JSONArray(String arrayAyy) {
        JSONArray array = JSONArray.parseArray(arrayAyy);
        System.out.println("数组：" + array);
        System.out.println("数组长度: " + array.size());
        Collection nuCon = new Vector();
        nuCon.add(null);
        array.removeAll(nuCon);
        System.out.println("数组：" + array);
        System.out.println("数组长度: " + array.size());
    }

    /**
     * 复杂数据类型
     */
    public static void Complexdata() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("username", "zhangsan");
        map.put("age", 24);
        map.put("sex", "男");

        // map集合
        HashMap<String, Object> temp = new HashMap<String, Object>();
        temp.put("name", "xiaohong");
        temp.put("age", "23");
        map.put("girlInfo", temp);

        // list集合
        List<String> list = new ArrayList<String>();
        list.add("爬山");
        list.add("骑车");
        list.add("旅游");
        map.put("hobby", list);
        String jsonString = JSON.toJSONString(map);
        System.out.println("复杂数据类型:" + jsonString);
    }

    public static void Deserialization(String json) {
        Userinfo userInfo = JSON.parseObject(json, Userinfo.class);
        System.out.println("姓名是:" + userInfo.getName() + ", 年龄是:"
                + userInfo.getAge());
    }

    /**
     * 格式化日期
     *
     * @param date
     */
    public static void DateFormate(Date date) {
        System.out.println("输出毫秒值：" + JSON.toJSONString(date));
        System.out.println("默认格式为:"
                + JSON.toJSONString(date,
                SerializerFeature.WriteDateUseDateFormat));
        System.out.println("自定义日期："
                + JSON.toJSONStringWithDateFormat(date, "yyyy-MM-dd",
                SerializerFeature.WriteDateUseDateFormat));
    }

    // Json转为实体
    private static void Json2Eetity(String json) {
        Userinfo userInfo = JSON.parseObject(json, Userinfo.class);
        System.out.println("输出对象的地址：" + userInfo.toString());
        System.out.println("输出对象的名字：" + userInfo.getName());
    }

    /**
     * JsonArray转换
     * 四个字符串:
     * "[\"HDC-51\"]",
     * "[\"HDC-51\", \"HDC-55\"]",
     * "[\"HDC-50\", \"HDC-55\", \"HDC-55-2\"]",
     * "[\"HDC-51\", \"HDC-55\", \"HDC-55-2\",\"HDC-21N\"]",
     * 分别向四个字符串中添加String macType ="HDC-50" ,
     * 并判断字符串中各个元素是否与macType相同, 相同则不添加, 不相同则添加.
     * 最后输出四个字符串,要求格式同开始字符串格式一致.
     */
    @Test
    public void JsonArrayTest() {
        List<String> contentList = new ArrayList<>();
        contentList.add("[\"HDC-51\"]");
        contentList.add("[\"HDC-51\", \"HDC-55\"]");
        contentList.add("[\"HDC-50\", \"HDC-55\", \"HDC-55-2\"]");
        contentList.add("[\"HDC-51\", \"HDC-55\", \"HDC-55-2\",\"HDC-21N\"]");
        System.out.println(contentList);
        String macType = "HDC-50";

        for (String content : contentList) {

            try {
                JSONArray contentArray = JSONArray.parseArray(content);

                //System.out.println("contentArray前 : " + contentArray);
                if (!contentArray.contains(macType)) {
                    contentArray.add(macType);
                }
                System.out.println("contentArray后 : " + contentArray);
            } catch (Exception e) {

                e.printStackTrace();
            }
        }
    }
    @Test
    public void JsonTest() {
        //1.json的简单格式{"name":"lisi","age",12}
        String json1 = "{'name':'lisi','age':12}";

        //解析1
        JSONObject json1Obj = JSONObject.parseObject(json1);
        String name = json1Obj.get("name").toString();
        System.out.println(name);//lisi

        //解析2
        Map<String, Object> json1Map = (Map<String, Object>) JSONObject.parseObject(json1, Map.class);
        String name2 = json1Map.get("name").toString();
        System.out.println(name2);//lisi


        //2.json数组格式(含有[]符号，用JsonArray解析)
        String json2 = "{'code':'aaa','name':['array1','array2','array3']}";

        JSONObject parseObject2 = JSONObject.parseObject(json2);
        String code = parseObject2.get("code").toString();
        System.out.println(code);//aaa

        JSONArray array2 = (JSONArray) parseObject2.get("name");
        System.out.println(array2.get(0));//array1

        //复杂的json
        ArrayList<String> array = new ArrayList<String>();
        array.add("array1");
        array.add("array2");
        array.add("array3");

        Map<String, Object> map2 = new HashMap<String, Object>();
        map2.put("code", "bbb");
        map2.put("code2", "bbb");


        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", array);
        map.put("code", "aaa");
        map.put("data", map2);
        String jsonString = JSONObject.toJSONString(map);

        System.out.println(jsonString);

        String ss = "{'code':'aaa','data':{'code':'bbb','code2':'bbb'},'name':['array1','array2','array3']}";

        Map<String, Object> parseObject = JSONObject.parseObject(ss, Map.class);
        System.out.println(parseObject.toString());//{name=["array1","array2","array3"], data={"code":"bbb","code2":"bbb"}, code=aaa}

        Map<String, Object> map3 = (Map<String, Object>) parseObject.get("data");
        System.out.println(map3.toString());//{"code":"bbb","code2":"bbb"}

        JSONArray arrayList = (JSONArray) parseObject.get("name");
        System.out.println(arrayList.toString());//["array1","array2","array3"]
    }
}
