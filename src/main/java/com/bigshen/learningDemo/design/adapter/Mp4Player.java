package com.bigshen.learningDemo.design.adapter;

/**
 * @Description:MP4播放器
 * @Author: byj
 * @Date: 2019/12/3 16:07
 */
public class Mp4Player implements AdvancedMediaPlayer{

    @Override
    public void playVlc(String fileName) {
        //什么也不做
    }

    @Override
    public void playMp4(String fileName) {
        System.out.println("Playing mp4 file. Name: "+ fileName);
    }
}
