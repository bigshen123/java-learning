package com.bigshen.learningDemo.utils.encrypt;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.security.Key;
import java.security.SecureRandom;

public class DocumentDemo {
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentB = dbf.newDocumentBuilder();
        File file=new File("E:\\IdeaProjects\\ds-encryption-service\\src\\main\\resources\\ds-portal-publish-configuration.xml");
        System.out.println(file);
        System.out.println(file.exists());
        Document document = documentB.parse(file);
        System.out.println(document);
        String path="C:\\Users\\Public\\Desktop";
        writeToXML(path,document);
    }

    /**
     * 加密操作
     * @param path 加密输出路径
     * @param document 传入xml文档
     */
    private static void writeToXML(String path, Document document) {
        System.out.println(path);
        try {
            // 创建Document对象：转成String：
            StringWriter strResponse = null;
            OutputFormat format = new OutputFormat(document);
            format.setEncoding("GB2312");
            strResponse = new StringWriter();
            XMLSerializer serializer = new XMLSerializer(strResponse, format);
            try {
                serializer.asDOMSerializer();
                serializer.serialize(document);
            } catch (IOException ex1) {
                ex1.printStackTrace();
            }
            StringBuffer sb = strResponse.getBuffer();

            // 再由String转成InputStream
            InputStream inputStream = new ByteArrayInputStream(sb.toString()
                    .getBytes());
            Cipher cipher = Cipher.getInstance("DES");
            // 产生密钥
            //Key key = CreateMyRegularKey("DES");
            KeyGenerator keyGenerator  = KeyGenerator.getInstance("DES");
            SecureRandom secureRandom = new SecureRandom();
            keyGenerator.init(secureRandom);
            Key key = keyGenerator.generateKey();
            String des = Cipher.getInstance("DES").getAlgorithm();
            System.out.println("Key algorithm: " + des);// DES
            // 读入并加密文件
            try {
                // 输入流,用密钥和随机源初始化 cipher
                cipher.init(Cipher.ENCRYPT_MODE, key);
                BufferedInputStream in = new BufferedInputStream(inputStream);
                // 输出流, path加密后.xml
                CipherOutputStream out = new CipherOutputStream(
                        new BufferedOutputStream(new FileOutputStream(path)),
                        cipher);
                int i;
                do {
                    i = in.read();
                    if (i != -1)
                        out.write(i);
                } while (i != -1);

                in.close();
                out.close();
                inputStream.close();
                System.out.println("加密文件完成！");

            } catch (Exception ey5) {
                System.out.println("Error when encrypt the file");
                System.exit(0);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 解密操作
     * @param xmlPath 传入解密文件
     * @return 解密后文件
     */
    public static Document getDocument(String xmlPath) {

        Document document = null;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;

        try {
            // 解密要用Cipher来实现
            Cipher cipher = Cipher.getInstance("DES");
            System.out.println("get....Cipher algorithm: "
                    + cipher.getAlgorithm());// DES

            //Key key = CreateMyRegularKey("DES");
            Key key  = KeyGenerator.getInstance("DES").generateKey();
            // 解密模式
            cipher.init(Cipher.DECRYPT_MODE, key);

            // 输入流
            CipherInputStream in = new CipherInputStream(
                    new BufferedInputStream(new FileInputStream(xmlPath)),
                    cipher);
            builder = factory.newDocumentBuilder();
            document = builder.parse(in);
            System.out.println("in");
            in.close();
        }catch (Exception e) {
        e.printStackTrace();
        return null;
    }
		return document;
}
}
