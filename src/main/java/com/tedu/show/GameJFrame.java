package com.tedu.show;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

//游戏窗体，主要实现功能：关闭，显示，最大最小化
//窗体需要嵌入面板，启动主线程
//使用swing awt实现
public class GameJFrame extends JFrame {
    public static int GameX = 780;
    public static int GameY = 600;
    private JPanel jPanel = null; //目前显示的面板
    private KeyListener keyListener = null;//键盘监听
    private Thread tread = null;//游戏主线程
    private MouseListener mouseListener = null;
    private MouseMotionListener mouseMotionListener = null; //鼠标监听


    public GameJFrame(){
        init();
    }

    public void init(){
        this.setSize(GameX,GameY);
        this.setTitle("坦克大战");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);//居中显示
    }

    public void start(){
        if(jPanel != null){
            this.add(jPanel);
        }
        if(keyListener!=null){
            this.addKeyListener(keyListener);
        }
        if(mouseListener!=null){
            this.addMouseListener(mouseListener);
        }
        if(tread != null){
            tread.start();
        }
        this.setVisible(true);

        //如果jp是runnable的子类实体对象
        if(this.jPanel instanceof Runnable){
            new Thread((Runnable)this.jPanel).start();//已经做了判定，强制类型转换不会出错
        }
    }

    //窗体布局
    public void addButton(){

    }


    //set注入
    public void setKeyListener(KeyListener keyListener) {
        this.keyListener = keyListener;
    }

    public void setThread(Thread thread) {
        this.tread = thread;
    }

    public void setMouseListener(MouseListener mouseListener) {
        this.mouseListener = mouseListener;
    }

    public void setMouseMotionListener(MouseMotionListener mouseMotionListener) {
        this.mouseMotionListener = mouseMotionListener;
    }

    public void setjPanel(JPanel jPanel) {
        this.jPanel = jPanel;
    }
}
