package com.tedu.element;

import com.tedu.manager.EnemyValue;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

/*
* @说明 本类为玩家子弹类，本类的实体对象由玩家对象调用和创建
* @子类开发步骤  1.继承父类，重写Show方法
*             2. 按照需求选择性重写方法
*             3. 思考子类特有属性
*
* */
public class PlayFile extends ElementObj{

    private String fx;//子弹朝向
    private int atk = 1; //子弹的攻击力
    private int speed = 8; //子弹打出速度


    public PlayFile(){}//空构造方法

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
        playBulletShotMusic();
        return this;
    }

    @Override
    protected void move() {
        if(this.getX() < -10 || this.getX() > 800 || this.getY() < -10 || this.getY() > 800){
            this.setLive(false);
            return;
        }

        switch(this.fx){
            case "up": this.setY(this.getY() - this.speed); break;
            case "down": this.setY(this.getY() + this.speed); break;
            case "left": this.setX(this.getX() - this.speed); break;
            case "right": this.setX(this.getX() + this.speed); break;
            case "up2": this.setY(this.getY() - this.speed); break;
            case "down2": this.setY(this.getY() + this.speed); break;
            case "left2": this.setX(this.getX() - this.speed); break;
            case "right2": this.setX(this.getX() + this.speed); break;
            case "ultimate_up": this.setY(this.getY() - this.speed); break;
            case "ultimate_down": this.setY(this.getY() + this.speed); break;
            case "ultimate_left": this.setX(this.getX() - this.speed); break;
            case "ultimate_right": this.setX(this.getX() + this.speed); break;
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

    /**
     * @author LJX
     */
    public void playBulletShotMusic(){
        // 使用类加载器获取资源文件
        URL audioUrl = getClass().getClassLoader().getResource("audio/bullet_shot.wav");
        if (audioUrl == null) {
            throw new RuntimeException("Audio file not found");
        }

//        File wavFile = new File("../resources/audio/stage_start.wav");
//        Clip clip;
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(audioUrl);
            Clip clip=AudioSystem.getClip();
//            clip = AudioSystem.getClip();
            clip.open(ais);
            clip.start();
        }catch (LineUnavailableException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }catch (UnsupportedAudioFileException e){
            e.printStackTrace();
        }
    }
}
