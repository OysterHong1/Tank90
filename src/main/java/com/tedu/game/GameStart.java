package com.tedu.game;
//启动类，程序的唯一入口

import com.tedu.controller.GameListener;
import com.tedu.controller.GameMListener;
import com.tedu.controller.GameThread;
import com.tedu.show.GameJFrame;
import com.tedu.show.GameMainJPanel;

public class GameStart {

    public static void main(String[] args) {
        GameJFrame gj = new GameJFrame();

        //实例化面板
        GameMainJPanel jp = new GameMainJPanel();
        //实例化监听
        GameListener listener = new GameListener();

        GameMListener listenerM = new GameMListener();//鼠标监听实例化//lin

        GameThread th = new GameThread();

        //注入
        gj.setjPanel(jp);
        gj.setKeyListener(listener);
        gj.setMouseListener(listenerM);//lin
        gj.setThread(th);

        gj.start();
    }
}
