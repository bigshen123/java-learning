package com.bigshen.learningDemo.design.adapter;

/**
 * @Description:vlc播放器
 * @Author: byj
 * @Date: 2019/12/3 16:06
 */
public class VlcPlayer implements AdvancedMediaPlayer{
    @Override
    public void playVlc(String fileName) {
        System.out.println("Playing vlc file. Name: "+ fileName);
    }

    @Override
    public void playMp4(String fileName) {
        //什么也不做
    }
}
