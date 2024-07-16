package com.tedu.controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


/**
 * @author lin
 * 鼠标监听
 */
public class GameMListener implements MouseListener {
    private static int DSmodel=0;//单双模式标识//1为单人，2为双人，3为道具说明

    private static int guanqia=0;//关卡标志//lin2.0


    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();//点击获取x坐标
        int y = e.getY();//点击获取y坐标
        System.out.println(x+" "+y);//测试鼠标位置
        if(x>=325&&x<=455&&y>=300&&y<=355){//点击DanButton的范围
//            System.out.println(x+" "+y);
            setDSmodel(1);
        }
        else if(x>=320&&x<=450&&y>=345&&y<=400){//点击SuanButton的范围
//            System.out.println(x+" "+y);
            setDSmodel(2);
        }
        else if(x>=320&&x<=450&&y>=385&&y<=440){//点击道具说明//lin2.0
            setDSmodel(3);//道具说明
//            System.out.println(1);
        }

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    public int getDSmodel() {
        return DSmodel;
    }

    public void setDSmodel(int DSmodel) {
        this.DSmodel = DSmodel;
    }
}
