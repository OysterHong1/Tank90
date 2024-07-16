package com.tedu.element;

import com.tedu.show.GameJFrame;

import javax.swing.*;
import java.awt.*;

/**
 * @author lin
 * 单双模式按钮
 */
public class DanButton extends ElementObj{
    public DanButton() {
    }

    @Override
    public ElementObj createElement(String str){
        this.setH(50);
        this.setW(130);
        this.setX(GameJFrame.GameX / 2 - 70);
        this.setY(GameJFrame.GameY / 2 - 30);
        ImageIcon icon = new ImageIcon("src/main/resources/image/anniu/singlePlayer.png");
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
