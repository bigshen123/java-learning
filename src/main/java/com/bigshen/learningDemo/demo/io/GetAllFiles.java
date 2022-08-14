package com.bigshen.learningDemo.demo.io;

import java.io.File;

/**
 * @Description:获取目录下所有的文件
 * @Author: byj
 * @Date: 2019/12/6 12:54
 */
public class GetAllFiles {

    public static void main(String[] args) {
        getAllFiles("D://home");
    }

    private static void getAllFiles(String path) {
        File file=new File(path);
        if (file.exists()){
            File[] files = file.listFiles();
            if (files!=null && files.length==0){
                System.out.println("文件为空");
            }else {
                if (files!=null){
                    for (File file1 : files) {
                         if (file1.isDirectory()){
                            getAllFiles(file1.getAbsolutePath());
                        }else{
                            System.out.println("文件："+file1.getName());
                        }
                    }
                }

            }
        }else {
            System.out.println("文件不存在");
        }

    }
}
