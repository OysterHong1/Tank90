package com.tedu.element;

import com.tedu.manager.ElementManager;
import com.tedu.manager.GameElement;
import com.tedu.manager.GameLoad;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Enemy extends ElementObj{
    private boolean left = false;
    private boolean up = false;
    private boolean right = false;
    private boolean down = false;

    private int botTankSpeed = 1;

    private String fx = "bot_left";

    @Override //敌人生成采用随机生成方式
    public ElementObj createElement(String str) {
        //String[] split = str.split(",");
        ImageIcon Enemyicon = GameLoad.imgMap.get(str);

        Random ran = new Random(System.nanoTime());

        int x = ran.nextInt(700);
        int y = ran.nextInt(100);
        this.setX(x);
        this.setY(y);
        //System.out.println(x);

        this.setW(35);
        this.setH(35);
        this.setIcon(Enemyicon);

        return this;
    }

    @Override
    public void showElement(Graphics g){
        g.drawImage(this.getIcon().getImage(),this.getX(),this.getY(),this.getW(),this.getH(),null);
    }

    @Override
    public void move(){//坦克产生新状态的概率统一设为%0.1
        Random random = new Random();
        int stateNumber = random.nextInt(1000);
        if(stateNumber < 4){
            this.right = false;this.down = false; this.up = false;
            this.left = true;this.fx = "bot_left";
        }
        if(4 <= stateNumber && stateNumber < 8){
            this.left = false;this.up = false;this.down = false;
            this.right = true;this.fx = "bot_right";
        }
        if( 8 <= stateNumber && stateNumber < 12){
            this.down = false;this.right = false; this.left = false;
            this.up = true;this.fx = "bot_up";
        }
        if( 12 <= stateNumber && stateNumber < 16){
            this.up = false;this.left = false;this.right=false;
            this.down = true;this.fx = "bot_down";
        }

        if(this.left && this.getX()>0){
            this.setX(this.getX() - botTankSpeed);
        }
        if(this.right && this.getX() < 780 - this.getW() - 20){
            this.setX(this.getX() + botTankSpeed);
        }
        if(this.up && this.getY() > 0 ){
            this.setY(this.getY() - botTankSpeed);
        }
        if(this.down && this.getY() < 600 - this.getH() - 40){
            this.setY(this.getY() + botTankSpeed);
        }
    }

    private long filetime = 0;//控制子弹的间隔时间,暂定为50
    Random random = new Random();
    int firecontrolNumber = random.nextInt(100);
    @Override //敌人射击，子弹添加函数
    public void add(long gameTime) {
        if(firecontrolNumber > 50 || gameTime - filetime < 50){return;}
        filetime = gameTime;
        ElementObj obj = GameLoad.getObj("EnemyFile");
        ElementObj element = obj.createElement(this.toString());//返回对象实体并初始化数据
        ElementManager.getManager().addElement(element, GameElement.ENEMYFILE);
    }

    @Override
    public String toString(){
        //{x:3,y:5,f:up}//模拟json格式传输

        //根据坦克朝向调整子弹坐标
        int tankImageSize = this.getW() - 10; //10为微调值
        int BulletX = this.getX();
        int BulletY = this.getY();
        switch(this.fx){
            case "bot_up": BulletX = this.getX() + (tankImageSize/2); break;
            case "bot_down": BulletX = this.getX() + (tankImageSize/2); BulletY = this.getY() + (tankImageSize/2); break;
            case "bot_left":BulletY = this.getY() + (tankImageSize/2); break;
            case "bot_right":BulletX = this.getX() + (tankImageSize);BulletY = this.getY()+(tankImageSize/2);break;
        }
        return "x:" + BulletX + ",y:" + BulletY + ",f:" + this.fx;
    }

    @Override
    public void die() {
        super.die();
    }

    @Override
    protected void updateImage() {
        this.setIcon(GameLoad.imgMap.get(fx));
    }

    @Override
    public void handleEntityPK() { //敌方坦克的回弹机制，处理敌方坦克和地图之间的碰撞
        if(this.left)this.setX(this.getX() + botTankSpeed);
        if(this.right) this.setX(this.getX() - botTankSpeed);
        if(this.up) this.setY(this.getY() + botTankSpeed);
        if(this.down) this.setY(this.getY() - botTankSpeed);
    }
}
