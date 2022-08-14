package com.bigshen.learningDemo.utils.file;

import com.bigshen.learningDemo.utils.CmdUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.util.Arrays;

public class DSFileUtils {
    private static final Log logger = LogFactory.getLog(DSFileUtils.class);
    static String os = CmdUtil.getOS();

    /**
     * 删除目录及目录下的文件
     *
     * @param dir 要删除的目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String dir) {
        // 如果dir不以文件分隔符结尾，自动添加文件分隔符
        if (!dir.endsWith(File.separator))
            dir = dir + File.separator;
        File dirFile = new File(dir);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
            System.out.println("删除目录失败：" + dir + "不存在！");
            return false;
        }
        boolean flag = true;
        // 删除文件夹中的所有文件包括子目录
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 删除子文件
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
            // 删除子目录
            else if (files[i].isDirectory()) {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
        }
        if (!flag) {
            logger.error("删除目录失败！");
            return false;
        }
        // 删除当前目录
        if (dirFile.delete()) {
            System.out.println("删除目录" + dir + "成功！");
            return true;
        } else {
            logger.error("删除目录失败！" + dirFile.getAbsolutePath());

            return false;
        }
    }

    /**
     * 删除文件，可以是文件或文件夹
     *
     * @param fileName 要删除的文件名
     * @return 删除成功返回true，否则返回false
     */
    public static boolean delete(String fileName) {

        File file = new File(fileName);

        if (!file.exists()) {
            System.out.println("删除文件失败:" + fileName + "不存在！");
            return false;
        } else {
            if (file.isFile())
                return deleteFile(fileName);
            else
                return deleteDirectory(fileName);
        }
    }

    /**
     * 删除单个文件
     *
     * @param fileName 要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                System.out.println("删除单个文件" + fileName + "成功！");
                return true;
            } else {
                System.out.println("删除单个文件" + fileName + "失败！");
                return false;
            }
        } else {
            System.out.println("删除单个文件失败：" + fileName + "不存在！");
            return false;
        }
    }

    /**
     * 目录拷贝
     *
     * @param srcPath  原始目录
     * @param destPath 目标目录
     */
    public static void dirCopy(String srcPath, String destPath) {
        File src = new File(srcPath);
        if (!new File(destPath).exists()) {
            new File(destPath).mkdirs();
        }
        for (File s : src.listFiles()) {
            if (s.isFile()) {
                fileCopy(s.getPath(), destPath + File.separator + s.getName());
            } else {
                dirCopy(s.getPath(), destPath + File.separator + s.getName());
            }
        }
    }

    /**
     * 文件拷贝
     *
     * @param srcPath  原始文件
     * @param destPath 目标文件
     */
    public static void fileCopy(String srcPath, String destPath) {
        if ("windows".equals(os)) {
            File src = new File(srcPath);
            File dest = new File(destPath);
            // 使用jdk1.7 try-with-resource 释放资源，并添加了缓存流
            try (InputStream is = new BufferedInputStream(new FileInputStream(src));
                 OutputStream out = new BufferedOutputStream(new FileOutputStream(dest))) {
                byte[] flush = new byte[1024];
                int len = -1;
                while ((len = is.read(flush)) != -1) {
                    out.write(flush, 0, len);
                }
                out.flush();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                destPath = destPath.replace("\\", "/");
                String copycmd = "cp -r " + srcPath + " " + destPath;
                logger.info("copy jdk:" + copycmd);
                CmdUtil.executeCmd(copycmd);
            } catch (Exception e) {
                logger.error("copy jdk error:" + e.getMessage());
            }

        }
    }

    public static void fileCopyByCMD(String srcPath, String destPath) {
        StringBuilder cmdStr = new StringBuilder();
        srcPath = srcPath.replaceAll("/", "\\\\");

        cmdStr.append("cmd /c copy /y " + srcPath + " \"" + destPath + "\"");

        try {
            Process result = Runtime.getRuntime().exec(cmdStr.toString());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * 读取本地文件夹下文件
     *
     * @param fileName ds-portal-publish-configuration.xml
     * @return 字节数组
     */
    public static byte[] readFromFile(String fileName) {
        byte[] data = null;
        InputStream is = null;
        ByteArrayOutputStream bos = null;
        try {
            String filePath = "." + System.getProperty("file.separator") + "meta" + System.getProperty("file.separator") + fileName;

            try {
                is = new FileInputStream(filePath);
            } catch (Exception ex) {
                is = DSFileUtils.class.getClassLoader().getResourceAsStream("meta/" + fileName);
            }
            bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = is.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            data = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
                bos.close();
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
        return data;
    }

    /**
     * 文件写入指定目录下
     * @param bytes 字节数组
     */
    public static void writeFromByteArray(byte[] bytes){
        FileOutputStream fos;
        String outPutPath="D:/ds-portal-publish-configuration2.xml";
        try {
            fos = new FileOutputStream(outPutPath);
            fos.write(bytes);
            fos.close();
            System.out.println("文件已写入："+outPutPath);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public static void main(String[] args) {
        byte[] bytes = readFromFile("ds-portal-publish-configuration.xml");
        System.out.println(Arrays.toString(bytes));
        writeFromByteArray(bytes);
    }
}
