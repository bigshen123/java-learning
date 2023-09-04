package com.bigshen.learningDemo.design.builder.DecorationPackage;

import com.bigshen.learningDemo.design.builder.DecorationPackage.ceiling.LevelOneCeiling;
import com.bigshen.learningDemo.design.builder.DecorationPackage.ceiling.LevelTwoCeiling;
import com.bigshen.learningDemo.design.builder.DecorationPackage.coat.DuluxCoat;
import com.bigshen.learningDemo.design.builder.DecorationPackage.coat.LiBangCoat;
import com.bigshen.learningDemo.design.builder.DecorationPackage.floor.ShengXiangFloor;
import com.bigshen.learningDemo.design.builder.DecorationPackage.tile.DongPengTile;
import com.bigshen.learningDemo.design.builder.DecorationPackage.tile.MarcoPoloTile;

/**
 * @Author BYJ
 * @Date 2023/9/4 21:01
 * @Describe
 */
public class Builder {
    public IMenu levelOne(Double area) {
        return new DecorationPackageMenu(area, "豪华欧式")
                // 吊顶，二级顶
                .appendCeiling(new LevelTwoCeiling())
                // 涂料，多乐士
                .appendCoat(new DuluxCoat())
                // 地板，圣象
                .appendFloor(new ShengXiangFloor());
    }

    public IMenu levelTwo(Double area){
        return new DecorationPackageMenu(area, "轻奢田园")
                // 吊顶，二级顶
                .appendCeiling(new LevelTwoCeiling())
                // 涂料，立邦
                .appendCoat(new LiBangCoat())
                // 地砖，马可波罗
                .appendTile(new MarcoPoloTile());
    }

    public IMenu levelThree(Double area){
        return new DecorationPackageMenu(area, "现代简约")
                // 吊顶，二级顶
                .appendCeiling(new LevelOneCeiling())
                // 涂料，立邦
                .appendCoat(new LiBangCoat())
                // 地砖，东鹏
                .appendTile(new DongPengTile());
    }
}
