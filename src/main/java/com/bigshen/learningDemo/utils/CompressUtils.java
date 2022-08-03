package com.bigshen.learningDemo.utils;

import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.Properties;

/**
 * @ClassName CompressDemo
 * @Description:TODO
 * @Author: byj
 * @Date: 2020/5/8
 */
public class CompressUtils {
    public static void main(String[] args) throws Exception {
        String compressFilePath = "C:\\Users\\byj\\Desktop\\meta\\ds-task-mobile-app.zip";
        String compressPath = "C:\\Users\\byj\\Desktop\\meta\\test\\task";
        /*String substring = compressFilePath.substring(compressFilePath.lastIndexOf("."));
        System.out.println(substring);*/
        if (".war".equals(compressFilePath.substring(compressFilePath.lastIndexOf(".")))){
            unWar(compressFilePath, compressPath);
        }else if (".zip".equals(compressFilePath.substring(compressFilePath.lastIndexOf(".")))){
            unZip(compressFilePath, compressPath);
        }else if (".rar".equals(compressFilePath.substring(compressFilePath.lastIndexOf(".")))){
            unRar(compressFilePath, compressPath);
        }else if (".gz".equals(compressFilePath.substring(compressFilePath.lastIndexOf(".")))){
            unTargz(compressFilePath, compressPath);
        }
    }

    /**
     * 解压tar.gz包的方法
     *
     * @param compressFilePath
     * @param compressPath
     */
    private static void unTargz(String compressFilePath, String compressPath) throws Exception {
        File warFile = new File(compressFilePath);
        if (!warFile.exists()) {
            throw new Exception(String.format("解压文件不存在！文件路径：%s", compressFilePath));
        }
        //创建解压路径
        mkdirs(compressPath);

        InputStream is = null;
        ArchiveInputStream ain = null;
        OutputStream out = null;
        try {
            is = new GzipCompressorInputStream(new FileInputStream(warFile));
            ain = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, is, "UTF-8");

            TarArchiveEntry entry = null;
            while ((entry = (TarArchiveEntry) ain.getNextEntry()) != null) {
                if (entry.isDirectory()) {
                    new File(compressPath, entry.getName()).mkdir();
                } else {
                    out = FileUtils.openOutputStream(new File(compressPath, entry.getName()));
                    IOUtils.copy(ain, out);
                }
            }
        } catch (Exception e) {
            throw new Exception(String.format("文件解压失败！文件[%s]，异常信息：%s", compressFilePath, e.getMessage()));
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (ain != null) {
                    ain.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * 解压rar包的方法
     *
     * @param compressFilePath
     * @param compressPath
     */
    private static void unRar(String compressFilePath, String compressPath) {
        try{
            Properties props = System.getProperties();
            String system = props.getProperty("os.name");
            system = system.toLowerCase();
            if(system.contains("windows")){
                String cmd ="start winrar x -o+ "+compressFilePath+" "+compressPath;
                String[] command = {"cmd","/c", cmd };
                Runtime runtime = Runtime.getRuntime();
                Process process = runtime.exec(command);
                Thread.sleep(10000);
            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    /**
     * 解压zip包的方法
     *
     * @param compressFilePath
     * @param compressPath
     */
    private static void unZip(String compressFilePath, String compressPath) throws Exception {
        File warFile = new File(compressFilePath);
        if (!warFile.exists()) {
            throw new Exception(String.format("解压文件不存在！文件路径：%s", compressFilePath));
        }
        //创建解压路径
        mkdirs(compressPath);

        InputStream is = null;
        ArchiveInputStream ain = null;
        OutputStream out = null;
        try {
            is = new BufferedInputStream(new FileInputStream(warFile));
            ain = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, is, "UTF-8");

            ZipArchiveEntry entry = null;
            while ((entry = (ZipArchiveEntry) ain.getNextEntry()) != null) {
                if (entry.isDirectory()) {
                    new File(compressPath, entry.getName()).mkdir();
                } else {
                    out = FileUtils.openOutputStream(new File(compressPath, entry.getName()));
                    IOUtils.copy(ain, out);
                }
            }
        } catch (Exception e) {
            throw new Exception(String.format("文件解压失败！文件[%s]，异常信息：%s", compressFilePath, e.getMessage()));
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (ain != null) {
                    ain.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * 解压war包方法
     *
     * @param compressFilePath 解压文件路径
     * @param compressPath     解压路径
     */
    private static void unWar(String compressFilePath, String compressPath) throws Exception {
        File warFile = new File(compressFilePath);
        if (!warFile.exists()) {
            throw new Exception(String.format("解压文件不存在！文件路径：%s", compressFilePath));
        }
        //创建解压路径
        mkdirs(compressPath);

        InputStream is = null;
        ArchiveInputStream ain = null;
        OutputStream out = null;
        try {
            is = new BufferedInputStream(new FileInputStream(warFile));
            ain = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.JAR, is, "UTF-8");

            JarArchiveEntry entry = null;
            while ((entry = (JarArchiveEntry) ain.getNextEntry()) != null) {
                if (entry.isDirectory()) {
                    new File(compressPath, entry.getName()).mkdir();
                } else {
                    out = FileUtils.openOutputStream(new File(compressPath, entry.getName()));
                    IOUtils.copy(ain, out);
                }
            }
        } catch (Exception e) {
            throw new Exception(String.format("文件解压失败！文件[%s]，异常信息：%s", compressFilePath, e.getMessage()));
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (ain != null) {
                    ain.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * 创建目录
     * 不存在，创建，存在则不创建
     *
     * @param compressPath 路径
     */
    private static void mkdirs(String compressPath) {
        File compressFile = new File(compressPath);
        if (!compressFile.exists()) {
            compressFile.mkdirs();
        }
    }


}
