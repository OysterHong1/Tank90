package com.tedu.element;

import javax.swing.*;
import java.awt.*;

public class MapObj extends ElementObj{

    private String name; //墙的类型

    @Override
    public ElementObj createElement(String str) {
        //System.out.println(str);
        String []arr = str.split(",");
        ImageIcon icon = null;
        switch(arr[0]){
            case "GRASS": icon = new ImageIcon("src/main/resources/image/map/grass.png");
                this.name = "GRASS"; // 设置名称为 GRASS//lin
                break;
            case "BRICK": icon = new ImageIcon("src/main/resources/image/map/brick.png");
                this.name = "BRICK"; // 设置名称为 BRICK//lin
                break;
            case "RIVER": icon = new ImageIcon("src/main/resources/image/map/river.png");
                this.name = "RIVER"; // 设置名称为 RIVER//lin
                break;
            case "IRON":  icon = new ImageIcon("src/main/resources/image/map/iron.png");
                this.name = "IRON"; // 设置名称为 IRON///lin
                break;
        }
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

    /**
     * @auther lin
     * @return
     * 添加两个方法来判断是否可穿越
     */
    public boolean isPassable() {
        return "GRASS".equals(name); // 只有草地可以穿越

    }
    public boolean isRiver() {
        return "RIVER".equals(name);//子弹可以穿越河流
    }

    /**
     * @author llin
     * 添加一个铁墙判断
     */
    public boolean isIron(){return "IRON".equals(name);}//子弹不可以穿越铁墙

}
