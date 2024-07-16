package com.tedu.element;
/*
* @说明 道具类，星星，效果为给予玩家10s的无敌时间
*      道具存在时间10s
*       无敌状态下不受敌方子弹影响
*       无敌状态下与敌人产生碰撞可直接杀死敌方坦克
* */


import com.tedu.manager.GameLoad;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Item_star extends ElementObj{

    public Item_star(){}//空构造方法

    @Override
    public void showElement(Graphics g) {
        g.drawImage(this.getIcon().getImage(),this.getX(),this.getY(),this.getW(),this.getH(),null);
    }

    @Override
    public ElementObj createElement(String str) {
        //System.out.println("生成了一个星星");
        ImageIcon StarIcon = GameLoad.imgMap.get(str);

        Random ran = new Random(System.nanoTime());

        int x = ran.nextInt(650);
        x += 50; //避免道具生成在最上方，与Score显示重叠
        int y = ran.nextInt(500);
        this.setX(x);
        this.setY(y);
        //System.out.println(x);

        this.setW(30);
        this.setH(30);
        this.setIcon(StarIcon);

        return this;
    }


}
