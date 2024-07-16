package com.tedu.controller;

import com.tedu.element.ElementObj;
import com.tedu.manager.ElementManager;
import com.tedu.manager.GameElement;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
 * @说明 监听类，用于监听用户的操作KeyListener
 * */
public class GameListener implements KeyListener {
    private ElementManager em = ElementManager.getManager();
    private Set<Integer> set = new HashSet<Integer>();

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        //System.out.println("keyPressed:"+e.getKeyCode());
        int key = e.getKeyCode();
        if(set.contains(key)){return;}

        set.add(key);
        //取得玩家集合
        List<ElementObj> play = em.getElementsByKey(GameElement.PLAY);
        for(ElementObj obj:play){
            obj.keyClick(true,e.getKeyCode());
        }
        /** @Author LJX */
        List<ElementObj> play2 = em.getElementsByKey(GameElement.PLAY2);
        for(ElementObj obj:play2){
            obj.keyClick(true,e.getKeyCode());
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //System.out.println("keyReleased:"+e.getKeyCode());
        int key = e.getKeyCode();
        if(!set.contains(key)){return;}

        set.remove(key);//移除状态
        //取得玩家集合
        List<ElementObj> play = em.getElementsByKey(GameElement.PLAY);
        for(ElementObj obj:play){
            obj.keyClick(false,e.getKeyCode());
        }

        /** @Author LJX */
        List<ElementObj> play2 = em.getElementsByKey(GameElement.PLAY2);
        for(ElementObj obj:play2){
            obj.keyClick(false,e.getKeyCode());
        }

    }
}
