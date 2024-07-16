package com.tedu.element;

import com.tedu.manager.EnemyValue;

import javax.swing.*;
import java.awt.*;

//所有元素的基类
public abstract class ElementObj {
    private int x; //x坐标
    private int y; //y坐标
    private int w; //width
    private int h; //height
    private ImageIcon icon; //图片

    private String name; //名称

    private boolean hitable = true; //元素是否可以被击中

    private long produceTime = 0L;//记录元素的生成时间，在道具管理模块使用

    private long stautsChangedTime = 0L;

    private boolean live = true;//生存状态 true代表存在，false代表死亡

    private int hp = 1; //生命值

    public ElementObj(){}



    public ElementObj(int x, int y, int w, int h, ImageIcon icon) {
        super();
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.icon = icon;
    }


    /*
    * @说明 本方法返回元素的碰撞矩形对象(实时返回)
    * */
    public Rectangle getRectangle(){
        return new Rectangle(x,y,w,h);
    }


    /*
    * @说明 方法返回值为true/false,返回true则有碰撞,返回false没有碰撞
    * */
    public boolean pk(ElementObj obj){
        return this.getRectangle().intersects(obj.getRectangle());
    }


    //若子类想要创建对象，需重写此方法
    public ElementObj createElement(String str){
        return null;
    }

    public final void model(long gameTime){
        updateImage(); //换装
        move();        //移动
        add(gameTime); //攻击
    }

    protected void updateImage(){} //换图片方法

    protected void add(long gameTime){} //射出子弹方法

    public void die(){}//死亡方法 同时死亡也是一个对象

    //需要移动的子类，请实现move方法
    protected void move(){} //移动方法

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public ImageIcon getIcon() {
        return icon;
    }

    public void setIcon(ImageIcon icon) {
        this.icon = icon;
    }

    public long getProduceTime() {
        return produceTime;
    }

    public void setProduceTime(long produceTime) {
        this.produceTime = produceTime;
    }

    public long getStatusChangedTime() {
        return stautsChangedTime;
    }

    public void setStatusChangedTime(long stautsChangedTime) {
        this.stautsChangedTime = stautsChangedTime;
    }

    public boolean isHitable() {
        return hitable;
    }

    public void setHitable(boolean hitable) {
        this.hitable = hitable;
    }

    public String getName() {
        return name;
    }

    /*
                    @说明 抽象方法 显示元素
                    @param g 画笔 用于进行绘画
                     */
    public void showElement(Graphics g){
    }

    /*
    * @说明 使用父类定义键盘接收方法
    *      若使用接口方式，则需要在监听类中进行接口转换
    * */
    public void keyClick(boolean bl,int key){

    }

    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    /*
    * @说明 实体碰撞处理方法，会主动产生非子弹式碰撞的类需要实现
    * */
    public void handleEntityPK(){}

    /*
    * @说明 星星道具碰撞处理方法，主角类进行实现
    * */
    public void handleStarPK(long gameTime){}

    /*
     * @说明 铲子道具碰撞处理方法，地图类进行实现
     * */
    public void handleShovelPK(){}


    /*
    * @说明 治疗道具碰撞处理方法，玩家类进行实现
    * */
    public void handleHealPK() {
    }

}
