package com.tedu.element;

/*
* @说明 Score类继承ElementObj，储存玩家得分
*       并含有分数增加，分数查询方法
* */

import java.awt.*;

public class Score extends ElementObj{

    private int playerScore;

    public Score(){}

    Font font = new Font("Arial", Font.BOLD, 20); // 字体名字，样式（粗体、斜体等），大小

    //元素显示方法，设定了Score的显示字体
    @Override
    public void showElement(Graphics g) {
        g.setFont(font);
        g.setColor(Color.white);
        g.drawString("Score:" + playerScore,this.getX(),this.getY());
    }

    @Override
    public ElementObj createElement(String str) {
        this.setX(10); //设置得分情况的显示坐标为(10,20)
        this.setY(20);
        return this;
    }

    //得分方法，由碰撞方法调用，传入参数为击杀敌人的分数值
    public void AddScore(int botValue) {
        playerScore += botValue;
    }

    //获取玩家获得的分数，在LevelAndCheck检查函数中调用，用于最后玩家统计结果的显示
    public int getScore() {
        return playerScore;
    }
}
