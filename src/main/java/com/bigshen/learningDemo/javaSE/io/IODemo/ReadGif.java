package com.bigshen.learningDemo.javaSE.io.IODemo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

/**
 * @author byj
 * @date 2024/4/19
 * @Description
 */
public class ReadGif {
    public static void main(String[] args) {
        try {
            // 读取GIF文件
//            Path path = Paths.get("C:\\Users\\Lenovo\\Desktop\\2785.gif_wh860.gif");
//            byte[] gifBytes = Files.readAllBytes(path);
//            // 将字节数组编码为Base64字符串
//            String base64String = Base64.getEncoder().encodeToString(gifBytes);
//            // 输出Base64字符串
//            System.out.print(base64String);

            String base64GIT = "R0lGODlhAQABAIABAP///wAAACH5BAEKAAEALAAAAAABAAEAAAICTAEAOw==";
            byte[] decode = Base64.getDecoder().decode(base64GIT);
            Files.write(Paths.get("C:\\Users\\Lenovo\\Desktop\\2.gif"),decode);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
