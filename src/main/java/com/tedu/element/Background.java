package com.tedu.element;

import com.tedu.show.GameJFrame;

import javax.swing.*;
import java.awt.*;

/**
 * @author lin
 * 游戏主页面
 * 实现选择单双人模式
 */
public class Background extends ElementObj {

    public Background() {
    }

    @Override
    public ElementObj createElement(String str) {

        this.setX(180);
        this.setY(100);
        this.setH(136);
        this.setW(376);
        ImageIcon icon = new ImageIcon("src/main/resources/image/anniu/battle_city.png");
        this.setIcon(icon);
        return this;
    }


    @Override
    public void showElement(Graphics g) {
        g.drawImage(this.getIcon().getImage(),
                this.getX(), this.getY(),
                this.getW(), this.getH(),
                null);
    }

}
