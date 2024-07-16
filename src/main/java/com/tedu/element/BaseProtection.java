package com.tedu.element;

import javax.swing.*;
import java.awt.*;

/*
* @说明 此类实质上是地图的一部分，将其从MapObj中分离出来，已便于对铲子道具的处理
*       此类是基地周边一圈的围墙
*       只在玩家拾取铲子道具后生成，同时在10后消除
* @Author HJH
* */
public class BaseProtection extends ElementObj{

    @Override
    public ElementObj createElement(String str) {
        //System.out.println(str);
        String []arr = str.split(",");
        ImageIcon icon = new ImageIcon("src/main/resources/image/map/iron.png");

        int x = Integer.parseInt(arr[1]);
        int y = Integer.parseInt(arr[2]);
        int w = icon.getIconWidth();
        int h = icon.getIconHeight();
        this.setH(h);
        this.setW(w);
        this.setX(x);
        this.setY(y);
        this.setIcon(icon);
        return this;
    }

    @Override
    public void showElement(Graphics g) {
        g.drawImage(this.getIcon().getImage(),this.getX(),this.getY(),this.getW(),this.getH(),null);
    }

    @Override
    public long getStatusChangedTime() {
        return super.getStatusChangedTime();
    }

    @Override
    public void setStatusChangedTime(long stautsChangedTime) {
        super.setStatusChangedTime(stautsChangedTime);
    }
}
