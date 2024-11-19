package com.bigshen.learningDemo.demo.geo;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

/**
 * @author byj
 * @date 2024/11/6
 * @Description
 */
public class GeoIPLookup {
    public static void main(String[] args) {
        try {
            File database = new File("path/to/GeoLite2-City.mmdb");  // 替换为数据库文件的路径
            DatabaseReader dbReader = new DatabaseReader.Builder(database).build();

            InetAddress ipAddress = InetAddress.getByName("8.8.8.8");  // 替换为要查询的IP地址
            CityResponse response = dbReader.city(ipAddress);

            String country = response.getCountry().getName();
            String city = response.getCity().getName();
            String postal = response.getPostal().getCode();
            System.out.println("Country: " + country + ", City: " + city + ", Postal: " + postal);

        } catch (IOException | GeoIp2Exception e) {
            e.printStackTrace();
        }
    }
}
