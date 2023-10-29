package com.bigshen.learningDemo.concurrent.atomic.shape;

/**
 * @Author BYJ
 * @Date 2023/10/29 17:52
 * @Describe
 */
class BuildingBlock {
    String shape;
    public BuildingBlock(String shape) {
        this.shape = shape;
    }
    @Override
    public String toString() {
        return "BuildingBlock{" + "shape='" + shape + '}';
    }
}
