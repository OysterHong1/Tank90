package com.tedu.show;

import com.tedu.element.ElementObj;
import com.tedu.manager.ElementManager;
import com.tedu.manager.GameElement;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

//游戏的主要面板
//进行元素的显示，进行界面的刷新（多线程）
public class GameMainJPanel extends JPanel implements Runnable {
    private ElementManager em;
    public GameMainJPanel(){
        init();
    }

    public void init(){
        em = ElementManager.getManager();
        this.setBackground(Color.BLACK);
    }



    @Override
    public void paint(Graphics g){
        super.paint(g);
        //map key-value key是无序不可重复的
        //set同样无序不可重复
        Map<GameElement, List<ElementObj>> all = em.getGameElements();
        //GameElement.values();//隐藏方法，返回值是一个数组，数组的顺序就是定义枚举的顺序
        for(GameElement ge:GameElement.values()){
            List<ElementObj> list = all.get(ge);
            for(int i = 0; i < list.size();i++){
                ElementObj obj = list.get(i);
                obj.showElement(g);
            }
        }
    }

    @Override
    public void run() {
        while(true){
            this.repaint();
            //使用休眠来控制速度
            try {
                Thread.sleep(30); //休眠40ms,即一秒刷新25次
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
