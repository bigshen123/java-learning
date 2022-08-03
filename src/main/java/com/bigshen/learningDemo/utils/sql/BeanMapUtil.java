package com.bigshen.learningDemo.utils.sql;

//import org.activiti.engine.history.HistoricVariableInstance;
//import org.springframework.util.ObjectUtils;

//import javax.servlet.http.HttpServletRequest;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

//import java.util.List;
//import java.util.Properties;

/**
 * @Description
 * @Author wzc
 * @Date 2019/12/10 14:49
 */
public class BeanMapUtil {
    //一：map2javabean 把map对象转换为javabean
    public  static <T> T map2bean(Map<String, Object> map,Class<T> beantype) throws Exception{
        T object=beantype.newInstance();//创建对象
        //获取类的属性描述器
        BeanInfo beaninfo=Introspector.getBeanInfo(beantype,Object.class);
        //获取类的属性集
        PropertyDescriptor[] pro=beaninfo.getPropertyDescriptors();
        for (PropertyDescriptor property : pro) {
            //获取属性的名字
            String name=property.getName();
            Object value=map.get(name);//得到属性name在map中对应的value。
            Method set=property.getWriteMethod();//得到属性的set方法
            //接下来将map的value转换为属性的value
            set.invoke(object, value);//执行set方法
        }
        return object;
    }
    //二：将javabean转换为map
    public static Map<String,Object> javabean2map(Object bean) throws Exception{//传入一个javabean对象
        Map<String, Object> map=new HashMap<>();
        //获取类的属性描述器
        BeanInfo beaninfo=Introspector.getBeanInfo(bean.getClass(),Object.class);
        //获取类的属性集
        PropertyDescriptor[] pro=beaninfo.getPropertyDescriptors();
        for (PropertyDescriptor property : pro) {
            String key=property.getName();//得到属性的name
            Method get=property.getReadMethod();
            Object value=get.invoke(bean);//执行get方法得到属性的值
            map.put(key, value);
        }
        return map;
    }



    public  static <T> T oracleMap2bean(Map<String, Object> map,Class<T> beantype) {
        T object;
		try {
			object = beantype.newInstance();
			//获取类的属性描述器
			BeanInfo beaninfo=Introspector.getBeanInfo(beantype,Object.class);
			//获取类的属性集
			PropertyDescriptor[] pro=beaninfo.getPropertyDescriptors();
			for (PropertyDescriptor property : pro) {
			    //获取属性的名字
			    String name=property.getName();
			    Object value=map.get(name.toUpperCase());//得到属性name在map中对应的value。
			    Method set=property.getWriteMethod();//得到属性的set方法
			    //接下来将map的value转换为属性的value
			    if (value instanceof BigDecimal) {
			        set.invoke(object, Integer.parseInt(value.toString()));//执行set方法
			    } else {
			        set.invoke(object, value);//执行set方法
			    }
			}
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
		        | IntrospectionException e) {
			throw new RuntimeException(e);
		}
        return object;
    }

    public  static <T> T postgreMap2bean(Map<String, Object> map,Class<T> beantype) {
        T object;
		try {
			object = beantype.newInstance();
			//获取类的属性描述器
			BeanInfo beaninfo=Introspector.getBeanInfo(beantype,Object.class);
			//获取类的属性集
			PropertyDescriptor[] pro=beaninfo.getPropertyDescriptors();
			for (PropertyDescriptor property : pro) {
			    //获取属性的名字
			    String name=property.getName();
			    Object value=map.get(name.toLowerCase());//得到属性name在map中对应的value。
			    Method set=property.getWriteMethod();//得到属性的set方法
			    if (value instanceof BigInteger) {
			        set.invoke(object, Integer.parseInt(value.toString()));//执行set方法
			    }else if (value instanceof BigDecimal) {
                    set.invoke(object, Integer.parseInt(value.toString()));//执行set方法
                } else {
                    set.invoke(object, value);//执行set方法
                }
			}
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
		        | IntrospectionException e) {
			throw new RuntimeException(e);
		}
        return object;
    }


    /*public  static <T> T xml2bean(String fileName,Class<T> beantype) throws Exception{
        T object=beantype.newInstance();//创建对象
        //获取类的属性描述器
        BeanInfo beaninfo=Introspector.getBeanInfo(beantype,Object.class);
        //获取类的属性集
        PropertyDescriptor[] pro=beaninfo.getPropertyDescriptors();
        Properties p = PropertiesUtil.getProperty(fileName);
        if (ObjectUtils.isEmpty(p)) {
            return null;
        }
        for (PropertyDescriptor property : pro) {
            //获取属性的名字
            String name=property.getName();
            Object value=p.get(name);//得到属性name在map中对应的value。
            Method set=property.getWriteMethod();//得到属性的set方法
            //接下来将map的value转换为属性的value
            set.invoke(object, value);//执行set方法
        }
        return object;
    }*/

    /*public  static <T> T requestHeaderVal2bean(HttpServletRequest request, Class<T> beantype) throws Exception{
        T object=beantype.newInstance();//创建对象
        //获取类的属性描述器
        BeanInfo beaninfo=Introspector.getBeanInfo(beantype,Object.class);
        //获取类的属性集
        PropertyDescriptor[] pro=beaninfo.getPropertyDescriptors();
        for (PropertyDescriptor property : pro) {
            //获取属性的名字
            String name=property.getName();
            Object value=request.getHeader(name);//得到属性name在map中对应的value。
            Method set=property.getWriteMethod();//得到属性的set方法
            //接下来将map的value转换为属性的value
            set.invoke(object, java.net.URLDecoder.decode(value.toString(),"UTF-8"));//执行set方法
        }
        return object;
    }*/


}
