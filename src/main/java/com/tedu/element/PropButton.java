package com.tedu.element;

import com.tedu.show.GameJFrame;

import javax.swing.*;
import java.awt.*;

/**
 * @author lin
 * 道具按钮
 */
public class PropButton extends ElementObj{
    public PropButton() {
    }

    @Override
    public ElementObj createElement(String str){
        this.setH(50);
        this.setW(130);
        this.setX(GameJFrame.GameX / 2 - 70);
        this.setY(GameJFrame.GameY / 2 + 60);
        ImageIcon icon = new ImageIcon("src/main/resources/image/anniu/instruction.png");
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