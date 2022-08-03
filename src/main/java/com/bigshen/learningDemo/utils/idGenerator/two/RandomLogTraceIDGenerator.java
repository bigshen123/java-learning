package com.bigshen.learningDemo.utils.idGenerator.two;

/**
 * @ClassName RandomLogTraceIDGenerator
 * @Description: TODO
 * @Author BYJ
 * @Date 2020/3/8
 * @Version V1.0
 **/

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

public class RandomLogTraceIDGenerator implements IDGenerator {

    private static final String BASE62 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final char[] CHAR_SET = BASE62.toCharArray();

    @Override
    public String generate() {
        String lastField = null;
        try {
            lastField = getLastFieldOfHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        char[] chars = genRandomChars(8);
        String result = formatId(lastField, chars);
        return result;
    }

    /**
     * 获取八位随机数
     * @param length
     * @return
     */
    private char[] genRandomChars(int length) {
        if (length <= 0) {
            return null;
        }
        char[] result = new char[length];
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            result[i] = CHAR_SET[random.nextInt(CHAR_SET.length)];
        }
        return result;
    }

    /**
     * 获取主机名最后一个字符
     * @return
     * @throws UnknownHostException
     */
    private String getLastFieldOfHostName() throws UnknownHostException {
        String hostName = InetAddress.getLocalHost().getHostName();
        String[] tokens = hostName.split("\\.");
        String result = tokens[tokens.length - 1];
        return result;
    }

    /**
     * 获取当前时间戳
     * @param hostName
     * @param randomChars
     * @return
     */
    private String formatId(String hostName, char[] randomChars) {
        String result = String.format("%s-%d-%s", hostName,
                System.currentTimeMillis(), new String(randomChars));
        return result;
    }
}
