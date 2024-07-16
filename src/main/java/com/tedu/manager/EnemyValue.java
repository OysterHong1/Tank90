package com.tedu.manager;

/*
* @说明 枚举类，记录各类敌人被消灭的分值
*      目前只记录了一种敌人类型，故也只有一个分数值，后续根据添加情况进行增加
* */
public enum EnemyValue {
    ENEMY(10),//普通敌人10分
    FAST_ENEMY(15),//快速敌人15分
    STRONG_ENEMY(20);//强敌20分

    private final int botValue;

    EnemyValue(int botValue){this.botValue = botValue;}

    public int getBotValue(){
        return this.botValue;
    }


}
