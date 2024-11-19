package com.bigshen.learningDemo.demo.geo;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.X509Certificate;

/**
 * @author byj
 * @date 2024/11/6
 * @Description
 */
public class IpApiExample {
    public static void main(String[] args) {
//        String publicIP = getPublicIP();
//        if (publicIP != null) {
//            getGeoLocation(publicIP);
//        } else {
//            System.out.println("Could not retrieve public IP.");
//        }

        getGeoLocation("160.248.2.209");

    }

    public static void getGeoLocation(String ip) {
        String apiURL = "http://ip-api.com/json/" + ip;

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(apiURL).openConnection();
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            System.out.println("Geo Location Data: " + response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getPublicIP() {
        String ipServiceUrl = "https://api.ipify.org"; // 使用 HTTPS 协议的公共 IP 查询服务
        try {
            // 强制使用 TLS 1.2
            System.setProperty("https.protocols", "TLSv1.2,TLSv1.3");


            // 忽略证书验证
            disableCertificateValidation();

            URL url = new URL(ipServiceUrl);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String publicIP = in.readLine();
            in.close();
            return publicIP;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void disableCertificateValidation() throws Exception {
        TrustManager[] trustAllCertificates = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                }
        };

        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(null, trustAllCertificates, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    }

}
