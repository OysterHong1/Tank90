package com.tedu.element;

import com.tedu.manager.GameLoad;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

/*
* @说明 本类为基地类，地图加载时默认生成
*       敌方，玩家使用子弹击打，或直接使用坦克触碰均会导致游戏失败
* */
public class Base extends ElementObj{

    private boolean baseFlag = true; //基地存活变量，被攻陷后变为false;

    @Override
    public ElementObj createElement(String str) {
        ImageIcon BaseIcon = GameLoad.imgMap.get(str);
        this.setX(375);
        this.setY(530);
        this.setW(30);
        this.setH(30);
        this.setIcon(BaseIcon);
        System.out.println("创建了基地");
        return this;
    }

    @Override
    public void showElement(Graphics g) {
        g.drawImage(this.getIcon().getImage(),this.getX(),this.getY(),this.getW(),this.getH(),null);
    }

    @Override
    protected void updateImage() {
        if(!baseFlag) this.setIcon(GameLoad.imgMap.get("broken_Base"));
    }

    @Override
    public void handleEntityPK() {
        this.baseFlag = false; //基地被破坏后更新图片为被破坏的基地
    }
}
