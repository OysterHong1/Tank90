package com.tedu.element;

import com.tedu.manager.GameLoad;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

/*
* @说明 道具类，铲子
*       效果为使基地周边加上一圈钢墙，持续时间15s
*       道具存在时间为10s
* */
public class Item_shovel extends ElementObj{
    @Override
    public void showElement(Graphics g) {
        g.drawImage(this.getIcon().getImage(),this.getX(),this.getY(),this.getW(),this.getH(),null);

    }

    @Override
    public ElementObj createElement(String str) {
        //System.out.println("生成了一个铲子");
        ImageIcon ShovelIcon = GameLoad.imgMap.get(str);

        Random ran = new Random(System.nanoTime());

        int x = ran.nextInt(650);
        x += 50; //避免道具生成在最上方，与Score显示重叠，实际显示范围[50,700)
        int y = ran.nextInt(500);
        this.setX(x);
        this.setY(y);
        //System.out.println(x);

        this.setW(30);
        this.setH(30);
        this.setIcon(ShovelIcon);

        return this;
    }


}
