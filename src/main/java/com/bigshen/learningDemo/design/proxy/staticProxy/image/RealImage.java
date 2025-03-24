package com.bigshen.learningDemo.design.proxy.staticProxy.image;

/**
 * @Description:
 * @Author: byj
 * @Date: 2019/12/4 14:40
 */
public class RealImage implements Image {

    private String fileName;

    RealImage(String fileName){
        this.fileName = fileName;
        loadFromDisk(fileName);
    }

    @Override
    public void display() {
        System.out.println("Displaying " + fileName);
    }

    private void loadFromDisk(String fileName){
        System.out.println("Loading " + fileName);
    }
}