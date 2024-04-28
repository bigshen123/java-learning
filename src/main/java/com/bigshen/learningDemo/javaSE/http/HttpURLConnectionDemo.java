package com.bigshen.learningDemo.javaSE.http;

import com.bigshen.learningDemo.common.exception.ApiException;
import org.springframework.http.HttpStatus;
import sun.misc.BASE64Encoder;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;

/**
 * @author byj
 * @date 2022/10/12
 */
public class HttpURLConnectionDemo {
    public static void main(String[] args) {
        String url = "http://10.0.108.17:443";
        try {
            URL netUrl = new URL(url);
            java.net.HttpURLConnection connection = (java.net.HttpURLConnection) netUrl.openConnection();
            connection.setConnectTimeout(3000);
            connection.setReadTimeout(3000);
            if (java.net.HttpURLConnection.HTTP_OK == connection.getResponseCode()) {
                System.out.println("OK");
            } else {
                System.out.println("error");
            }
            Instant now = Instant.now();
            String s = now.toString();
            System.out.println(s);


            Base64.Encoder encoder = Base64.getEncoder();
            String vsmProxySslCert = "-----BEGIN CERTIFICATE-----\\nMIICETCCAbagAwIBAgIJANdLUbe6Li6TMAoGCCqBHM9VAYN1MGIxCzAJBgNVBAYT\\n\\\n" +
                    "    AkNOMREwDwYDVQQIDAhTaGFuZ2hhaTERMA8GA1UEBwwIU2hhbmdoYWkxDDAKBgNV\\nBAoMA2NvbTENMAsGA1UECwwEa29hbDEQMA4GA1UEAwwHa2NzcC1jYTAgFw0yMjA5\\n\\\n" +
                    "    MTQxNDEwMDFaGA8yMTIyMDgyMTE0MTAwMVowYjELMAkGA1UEBhMCQ04xETAPBgNV\\nBAgMCFNoYW5naGFpMREwDwYDVQQHDAhTaGFuZ2hhaTEMMAoGA1UECgwDY29tMQ0w\\n\\\n" +
                    "    CwYDVQQLDARrb2FsMRAwDgYDVQQDDAdrY3NwLWNhMFkwEwYHKoZIzj0CAQYIKoEc\\nz1UBgi0DQgAEKAaHKdZuCr6YCGhVMIz/Yus+bRE1eTs6ZgL5FXeC1pFLInmkfI6T\\n\\\n" +
                    "    2MbtFIZcILB3Nr9UkfKtRVi3zWZXFSXMF6NTMFEwHQYDVR0OBBYEFIhVUOqs2bOd\\nTOzN+gX/uwUESo9rMB8GA1UdIwQYMBaAFIhVUOqs2bOdTOzN+gX/uwUESo9rMA8G\\n\\\n" +
                    "    A1UdEwEB/wQFMAMBAf8wCgYIKoEcz1UBg3UDSQAwRgIhAItSXRVSzXyQFKLMuh/T\\n/vpKwfsNlKaravIkNp5cGnzjAiEAxs20zJEQeWPXAQq+H3T4+0Isv/QoKr+SBAOn\\n\\\n" +
                    "    ByeSQwU=\\n-----END CERTIFICATE-----\\n";
            vsmProxySslCert = encoder.encodeToString(vsmProxySslCert.getBytes(StandardCharsets.UTF_8));
            System.out.println(vsmProxySslCert);
        } catch (IOException e) {
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "KCSP与网关连通性异常", e);
        }
    }
}
