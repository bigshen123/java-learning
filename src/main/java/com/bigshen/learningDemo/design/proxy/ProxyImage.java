package com.bigshen.learningDemo.design.proxy;

/**
 * @Description:代理图片
 * @Author: byj
 * @Date: 2019/12/4 14:41
 */
public class ProxyImage implements Image{

    private RealImage realImage;
    private String fileName;

    ProxyImage(String fileName){
        this.fileName = fileName;
    }

    @Override
    public void display() {
        if(realImage == null){
            realImage = new RealImage(fileName);
        }
        realImage.display();
    }
}