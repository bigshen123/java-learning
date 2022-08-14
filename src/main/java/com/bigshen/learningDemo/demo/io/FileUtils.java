package com.bigshen.learningDemo.demo.io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * @author byj
 * @date 2022/3/9
 */
public class FileUtils {
    public static String fileToString(String filePath) throws Exception {

        String input = null;
        Scanner sc = new Scanner(new File(filePath));
        StringBuffer sb = new StringBuffer();
        while (sc.hasNextLine()) {
            input = sc.nextLine();
            sb.append(input);
            sb.append("\n");
        }

        return sb.toString();

    }

    private static void saveAsFileWriter(String filePath, String content) {
        FileWriter fwriter = null;
        try {
            // true表示不覆盖原来的内容，而是加到文件的后面。若要覆盖原来的内容，直接省略这个参数就好
            fwriter = new FileWriter(filePath);
            fwriter.write(content);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (fwriter!=null){
                    fwriter.flush();
                    fwriter.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * 判断指定文件中是否存在指定字符串
     *
     * @param classPath 文件路径
     * @return true/false
     * @throws IOException
     */
    private static boolean isExistConf(String classPath) throws IOException {
        File file = new File(classPath);
        //每行作为一个字符串，存为列表元素 com.google.guava
        /*List<String> strings = Files.readLines(file, Charsets.UTF_8);
        for (String string : strings) {
            if (string.contains("SkywalkingConfig.JAVA_AGENT")) {
                return true;
            }
        }*/
        return false;
    }


}

