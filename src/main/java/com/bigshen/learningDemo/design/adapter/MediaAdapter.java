package com.bigshen.learningDemo.design.adapter;

/**
 * @Description:媒体适配器(作为两个不兼容的接口之间的桥梁)
 * @Author: byj
 * @Date: 2019/12/3 16:07
 */
public class MediaAdapter implements MediaPlayer {

    AdvancedMediaPlayer advancedMusicPlayer;

    MediaAdapter(String audioType){
        if("vlc".equalsIgnoreCase(audioType)){
            advancedMusicPlayer = new VlcPlayer();
        } else if ("mp4".equalsIgnoreCase(audioType)){
            advancedMusicPlayer = new Mp4Player();
        }
    }

    @Override
    public void play(String audioType, String fileName) {
        if("vlc".equalsIgnoreCase(audioType)){
            advancedMusicPlayer.playVlc(fileName);
        }else if("mp4".equalsIgnoreCase(audioType)){
            advancedMusicPlayer.playMp4(fileName);
        }
    }
}
