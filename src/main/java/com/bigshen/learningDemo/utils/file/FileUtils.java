package com.bigshen.learningDemo.utils.file;

import java.io.File;

/**
 * @author byj
 * @date 2022/10/21
 */
public class FileUtils {

    public static void downAndReadFile(String filePath,String dirPath) throws Exception {
        //检查指定目录,用户没有指定目录 抛出异常提示用户
        if(dirPath==null||dirPath.length()==0) {
            throw new Exception("指定路径目录不能为空");
        }
        File savePath = new File(dirPath);
        //判断文件目录是否存在，不存在即创建目录
        if (!savePath.exists()) {
            savePath.mkdir();
        }


    }
}
