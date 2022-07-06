package com.bigshen.chatDemoService.demo.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

/**
 * @Description:用jdk自带包实现抓取指定网页元素/内容
 * @Author: BIGSHEN
 * @Date: 2019/12/24 15:01S
 */
public class JDKDemo {
    public static void main(String[] args) {
        doGet();
    }

    private static void doGet() {
        try {
            String urlStr = "http://www.baidu.com/";
            URL url = new URL(urlStr);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setInstanceFollowRedirects(false);
            httpURLConnection.connect();

            BufferedReader bufferedReader=new BufferedReader
                    (new InputStreamReader(httpURLConnection.getInputStream(), StandardCharsets.UTF_8));
            String ss,total="";
            while ((ss=bufferedReader.readLine())!=null){
                total+=ss;
                total+="\n";
            }
            String[] split = total.split(";");
            for (String s : split) {
                System.out.println(s);
            }
            //System.out.println("total"+total);
            bufferedReader.close();
            httpURLConnection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
