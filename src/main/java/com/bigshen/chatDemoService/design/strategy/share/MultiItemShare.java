package com.bigshen.chatDemoService.design.strategy.share;

/**
 * @author byj
 * @date 2022/2/9
 */
public class MultiItemShare implements ShareStrategy{
    @Override
    public void shareAlgorithm(String param) {
        System.out.println("当前分享图片是" + param);
    }
}
