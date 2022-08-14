package com.bigshen.learningDemo.demo.check;

/**
 * @Description:校验qq号码
 * 1、5~10位
 * 2、不能0开头
 * 3、必须是数字
 * @Author: BIGSHEN
 * @Date: 2019/12/7 8:46
 */
public class CheckQQ {
    public static void main(String[] args) {
        boolean b = checkNoRegExp("2345332");
        System.out.println(b);
    }
    private static boolean checkNoRegExp(String qq){
        boolean flag=true;
        if (qq.length()>=5 && qq.length()<=10){
            if (!qq.startsWith("0")){
                char[] arr = qq.toCharArray();
                for (char c : arr) {
                    if (c<'0' || c>'9') {
                        flag = false;
                        System.out.println("必须是数字");
                        break;
                    }
                }
            }else {
                flag=false;
                System.out.println("不能以0开头");
            }
        }else{
            flag=false;
            System.out.println("长度不合法");
        }
        return flag;
    }
}
