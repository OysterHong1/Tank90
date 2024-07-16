package com.tedu.element;

import com.tedu.manager.ElementManager;
import com.tedu.manager.GameElement;
import com.tedu.manager.GameLoad;

import javax.swing.*;
import java.awt.*;

/**
 * @author l'j'x
 * @说明 第二个玩家
 */

public class Play2 extends ElementObj{

    //四个属性进行玩家移动状态的判定
    //多属性还可以使用map<泛型，boolean> set<判定对象>
    private boolean left = false;
    private boolean up = false;
    private boolean right = false;
    private boolean down = false;

    //使用pkType作为玩家坦克的攻击状态
    private boolean pkType = false;

    private String fx = "up2"; //记录当前面向的方向,默认为up

    private int TankSpeed = 3; //坦克移动的默认速度为3


    public Play2(){}

/*    public Play(int x, int y, int w, int h, ImageIcon icon) {
        super(x, y, w, h, icon);
    }*/

    @Override
    public ElementObj createElement(String str) {
        String[] split = str.split(",");
        this.setX(Integer.parseInt(split[0]));
        this.setY(Integer.parseInt(split[1]));
        ImageIcon icon2 = GameLoad.imgMap.get(split[2]);
        this.setW(32); //宽度，高度均设置成32
        this.setH(32);
        this.setIcon(icon2);
        this.setHp(1); //设置初始生命值为1
        return this;
    }

    @Override
    public void showElement(Graphics g) {
        g.drawImage(this.getIcon().getImage(),this.getX(),this.getY(),this.getW(),this.getH(),null);
    }

    @Override
    public void keyClick(boolean bl,int key){
        if(bl){
            switch(key){
                //A
                case 65: this.right = false;this.down = false; this.up = false;
                    this.left = true;
                    if(this.isHitable()){//普通状态下
                        this.fx = "left2"; break;
                    }else{ //无敌状态下
                        this.fx = "ultimate_left"; break;
                    }
                //W
                case 87: this.down = false;this.right = false; this.left = false;
                    this.up = true;
                    if(this.isHitable()){//普通状态下
                        this.fx = "up2"; break;
                    }else{ //无敌状态下
                        this.fx = "ultimate_up"; break;
                    }
                //D
                case 68: this.left = false;this.up = false;this.down = false;
                    this.right = true;
                    if(this.isHitable()){//普通状态下
                        this.fx = "right2"; break;
                    }else{ //无敌状态下
                        this.fx = "ultimate_right"; break;
                    }
                //S
                case 83: this.up = false;this.left = false;this.right=false;
                    this.down = true;
                    if(this.isHitable()){//普通状态下
                        this.fx = "down2"; break;
                    }else{ //无敌状态下
                        this.fx = "ultimate_down"; break;
                    }
                //J
                case 74: this.pkType = true; break; //按下J发射子弹


            }
        }else{
            switch(key){
                case 65: this.left = false;  break;
                case 87: this.up = false;  break;
                case 68: this.right = false;  break;
                case 83: this.down = false; break;
                case 74: this.pkType = false; break;
            }
        }


        //System.out.println("当前坐标："+ this.getX()+" "+this.getY());
    }

    /*
     * @说明 移动方法的重写，能判别当前移动的方向，并进行边界设定，当变更分辨率时要修改判别式中的参数
     * */
    @Override
    public void move(){
        if(this.left && this.getX()>0){
            this.setX(this.getX() - TankSpeed);
        }
        if(this.right && this.getX() < 780 - this.getW() - 20){
            this.setX(this.getX() + TankSpeed);
        }
        if(this.up && this.getY() > 0 ){
            this.setY(this.getY() - TankSpeed);
        }
        if(this.down && this.getY() < 600 - this.getH() - 40){
            this.setY(this.getY() + TankSpeed);
        }
    }

    @Override
    public void updateImage(){
        this.setIcon(GameLoad.imgMap.get(fx));
    }

    private long filetime = 0;//控制子弹的间隔时间,暂定为50
    @Override //子弹添加函数
    public void add(long gameTime) {
        if(!this.pkType || gameTime - this.filetime <= 30){return;}
        this.filetime = gameTime;
        ElementObj obj = GameLoad.getObj("PlayFile");
        ElementObj element = obj.createElement(this.toString());//返回对象实体并初始化数据
        ElementManager.getManager().addElement(element, GameElement.PLAYFILE);
    }

    @Override
    public String toString(){
        //{x:3,y:5,f:up}//模拟json格式传输

        //根据坦克朝向调整子弹坐标
        int tankImageSize = this.getW() - 10; //坦克图片大小目前为50，10为微调值
        int BulletX = this.getX();
        int BulletY = this.getY();
        switch(this.fx){
            case "up2": BulletX = this.getX() + (tankImageSize/2); break;
            case "ultimate_up": BulletX = this.getX() + (tankImageSize/2); break;
            case "down2": BulletX = this.getX() + (tankImageSize/2); BulletY = this.getY() + (tankImageSize/2); break;
            case "ultimate_down": BulletX = this.getX() + (tankImageSize/2); BulletY = this.getY() + (tankImageSize/2); break;
            case "left2":BulletY = this.getY() + (tankImageSize/2); break;
            case "ultimate_left":BulletY = this.getY() + (tankImageSize/2); break;
            case "right2":BulletX = this.getX() + (tankImageSize);BulletY = this.getY()+(tankImageSize/2);break;
            case "ultimate_right":BulletX = this.getX() + (tankImageSize);BulletY = this.getY()+(tankImageSize/2);break;
        }


        return "x:" + BulletX + ",y:" + BulletY + ",f:" + this.fx;
    }

    @Override
    public void handleEntityPK() {//处理主角和地形之间的碰撞
        if(this.left)this.setX(this.getX() + TankSpeed);
        if(this.right) this.setX(this.getX() - TankSpeed);
        if(this.up) this.setY(this.getY() + TankSpeed);
        if(this.down) this.setY(this.getY() - TankSpeed);
    }

    @Override //用于处理玩家获得星星道具后的状态
    public void handleStarPK(long gameTime) {
        this.setStatusChangedTime(gameTime);
        System.out.println(getStatusChangedTime());
        this.setHitable(false);//设置玩家为不可击中状态
        System.out.println("玩家2无敌状态启动");
    }

    /*
     * @说明 用于处理玩家获得治疗道具后的加血过程
     * */
    @Override
    public void handleHealPK() {
        this.setHp(this.getHp() + 1); //拾取道具后生命值+1
    }
}
