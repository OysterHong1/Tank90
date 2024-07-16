package com.tedu.element;

import com.tedu.manager.EnemyValue;

import javax.swing.*;
import java.awt.*;

/*
 * @说明 本类为敌人子弹类，本类的实体对象由各类对象调用和创建
 * @子类开发步骤  1.继承父类，重写Show方法
 *             2. 按照需求选择性重写方法
 *             3. 思考子类特有属性
 *
 * */
public class EnemyFile extends ElementObj{

    private String fx;//子弹朝向
    private int atk = 1; //子弹的攻击力
    private int speed = 3; //子弹打出速度


    public EnemyFile(){}//空构造方法

    @Override //子弹工厂，由玩家对象调用，每次进入射击状态时产生一颗子弹
    public ElementObj createElement(String str){
        String[] split = str.split(",");
        for(String str1: split){
            String[] split2 = str1.split(":");
            switch(split2[0]){
                case "x": this.setX(Integer.parseInt(split2[1])); break;
                case "y": this.setY(Integer.parseInt(split2[1])); break;
                case "f": this.fx = split2[1]; break;

            }
        }
        this.setW(8);
        this.setH(8);
        return this;
    }

    @Override
    protected void move() {
        if(this.getX() < -10 || this.getX() > 800 || this.getY() < -10 || this.getY() > 800){
            this.setLive(false);
            return;
        }

        switch(this.fx){
            case "bot_up": this.setY(this.getY() - this.speed); break;
            case "bot_down": this.setY(this.getY() + this.speed); break;
            case "bot_left": this.setX(this.getX() - this.speed); break;
            case "bot_right": this.setX(this.getX() + this.speed); break;
            case "strong_enemy_up": this.setY(this.getY() - this.speed); break;
            case "strong_enemy_down": this.setY(this.getY() + this.speed); break;
            case "strong_enemy_left": this.setX(this.getX() - this.speed); break;
            case "strong_enemy_right": this.setX(this.getX() + this.speed); break;
            case "fast_enemy_up": this.setY(this.getY() - this.speed); break;
            case "fast_enemy_down": this.setY(this.getY() + this.speed); break;
            case "fast_enemy_left": this.setX(this.getX() - this.speed); break;
            case "fast_enemy_right": this.setX(this.getX() + this.speed); break;
        }
    }

    @Override
    public void showElement(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillOval(this.getX(),this.getY(),this.getW(),this.getH());
    }

    /*
     * 对子弹类，子弹死亡情况分为两种：
     *   1.子弹离开边界
     *   2.产生碰撞
     *
     *   处理方式：达到死亡条件时，修改生存状态
     *   子弹死亡后，需要在集合中删除对象，这部分交由主线程
     * */
    @Override
    public void die() {
        //死亡方法内后续可添加死亡动画，道具掉落等功能
    }
}
