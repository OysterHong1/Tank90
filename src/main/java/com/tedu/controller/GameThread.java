package com.tedu.controller;

import com.tedu.element.*;
import com.tedu.manager.*;

import javax.sound.sampled.*;
import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/*
* @说明 游戏主进程，用于控制游戏加载，游戏关卡，游戏运行时的自动化
*      游戏判定，游戏地图切换，资源释放和重新读取
* */
public class GameThread extends Thread{

    private JFrame parentFrame; // 主窗口
    private ElementManager em;

    private boolean LevelFlag = true; // 关卡标识，当场上还有敌人时置为true，敌人全部死亡后置为false
                                      // 敌人检测函数为LevelEndCheck函数
    private boolean GameFlag = true; // 值为true时标识正在游戏中，当最后一关通关后置为false，同时进入gameover阶段

    private int TotalLevel = 10;//关卡总数，后续增加关卡后需要在这里修改
    private int playerLevel = 3;//当前关卡标识，默认从第一关开始
    public Score playerScore; //得分对象用于记录分数

    public int playerNum = 0;


    public GameThread(){
        em = ElementManager.getManager();
    }

    @Override
    public void run() {

        while (GameFlag){
            LevelFlag = true; //恢复LevelFlag为true，保证下一次进入关卡能够正常运行
            while(playerNum == 0) {//当游戏模式为0时进入
                gameMain();//游戏主菜单//lin
            }

            if(playerLevel > TotalLevel){ //到达最后一关，直接进入over阶段
                GameFlag = false;
                break;
            }

            gameLoad(playerLevel);//游戏开始前 加载游戏资源
            playStartMusic();
            gameRun();//游戏进行时 游戏过程中
            if(!LevelFlag){
                playerNum = 0;
                System.out.println(playerNum);
                try{
                    playGameOverMusic();
                    sleep(2500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                run(); // 重新调用 run 方法以返回主菜单
            }

            try {
                sleep(30);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        gameOver();
    }

    /**
     * @auther lin
     * 添加主界面
     */
    private void gameMain() {
        em.clear();
        GameLoad.loadBackground(); // 加载背景图片
        GameLoad.loadDanButton(); //加载单双按钮
        GameLoad.loadSuanButton(); //加载单双按钮
        GameLoad.loadPropButton();//加载道具按钮
        GameMListener lM = new GameMListener();//实例化鼠标监听
        while(playerNum==0){
            playerNum=lM.getDSmodel();//将鼠标监听赋值到DSmoshi单双模式
            System.out.println(playerNum);//刷新playerNum
        }
        if(playerNum==3){//道具说明
            String itemName = "游戏说明";
            String itemDescription = "<html><body>" +
                    "<h3>玩家控制和攻击方式：</h3>" +
                    "<ul>" +
                    "<li>player1 通过上下左右控制，空格攻击</li>" +
                    "<li>player2 通过wasd控制，j攻击</li>" +
                    "</ul>" +
                    "<h3>道具功能说明：</h3>" +
                    "<ul>" +
                    "<li>星星道具：玩家拾取道具后，可以获得8秒的无敌时间。</li>" +
                    "<li>铲子道具：玩家拾取道具后，可以加固基地周边的围墙10秒。</li>" +
                    "<li>疗伤道具：玩家拾取道具后，可以增加拾取者一条生命，使其可以多被击中一次。</li>" +
                    "</ul>" +
                    "<h3>地图元素说明：</h3>" +
                    "<ul>" +
                    "<li>砖 (Brick)：不可穿越，敌方坦克和玩家均可破坏。</li>" +
                    "<li>草 (Grass)：不可破坏，玩家和敌方坦克均可穿越。</li>" +
                    "<li>水 (Water)：不可破坏，玩家和敌方坦克不可穿越，但子弹可以穿过。</li>" +
                    "<li>钢墙 (Iron)：不可破坏，不可穿越。</li>" +
                    "</ul>" +
                    "<h3>游戏目标：</h3>" +
                    "<p>保护基地和消灭所有敌人。</p>" +
                    "</body></html>";

            JOptionPane.showMessageDialog(
                    null,
                    itemDescription,
                    itemName,
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
        while(playerNum==3){//继续重复监听
            playerNum=lM.getDSmodel();//将鼠标监听赋值到DSmoshi单双模式
            System.out.println(playerNum);//刷新playerNum！
        }
    }

    private void gameLoad(int playerLevel) { //游戏资源加载
        em.clear();

        GameLoad.loadObj(); //获取各个类

        GameLoad.loadImg(); //图片资源加载

        GameLoad.MapLoad(playerLevel);//加载地图，参数为关卡号

        GameLoad.BaseLoad();

        GameLoad.PlayLoad(playerNum);//加载主角

        GameLoad.EnemyLoad(playerLevel);//加载敌人

        playerScore = GameLoad.ScoreLoad();//得分情况加载,并将创建的对象返回
    }
    /*
    * @说明 游戏过程中需要做的事情：1.自动化玩家的移动，碰撞，死亡
    *                          2.新元素的增加
    *                          3.暂停等
    * */


    private long gameTime = 0L;
    private void gameRun() {
        while(LevelFlag){ //LevelFlag用于控制关卡结束，当敌人全部死亡时置为false，代表本关结束
            Map<GameElement, List<ElementObj>> all = em.getGameElements();
            //加载玩家列表
            moveAndUpdate(all,gameTime); //更新函数
            GameLoad.ItemHandler(gameTime); //产生道具使用的函数

            PKCheck(); //碰撞检查
            LevelEndCheck(); //检查敌人数量，若为零则进入下一关
            StautsCheck(); //检查状态函数，用于检查状态，并对有时效性的状态进行处理，如玩家无敌状态
            GameOverCheck(); //检查玩家的生命状态

            gameTime++;//唯一的时间控制
            try {
                sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /*
    * @说明，该类检查玩家生命状态，若两玩家均死亡则GameOver
    * */
    private void GameOverCheck() {
        List<ElementObj> players1 = em.getElementsByKey(GameElement.PLAY);
        List<ElementObj> players2 = em.getElementsByKey(GameElement.PLAY2);

        if (players1.isEmpty() && players2.isEmpty()) {
            LevelFlag = false;
            String message = "Game Over!";
            JOptionPane.showMessageDialog(parentFrame, message, "本关失败!", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /*
    * @说明 这是一个检查碰撞的目录方法，由gameRun调用
    * */
    private void PKCheck() {
        List<ElementObj> play = em.getElementsByKey(GameElement.PLAY);
        List<ElementObj> play2 = em.getElementsByKey(GameElement.PLAY2);// 玩家2  LJX
        //加载敌人列表
        List<ElementObj> enemys = em.getElementsByKey(GameElement.ENEMY);
        List<ElementObj> fastenemys = em.getElementsByKey(GameElement.FASTENEMY);
        List<ElementObj> strongenemys = em.getElementsByKey(GameElement.STRONGENEMY);
        //加载子弹列表
        List<ElementObj> files = em.getElementsByKey(GameElement.PLAYFILE);
        List<ElementObj> enemyfiles = em.getElementsByKey(GameElement.ENEMYFILE);
        //加载地图信息
        List<ElementObj> maps = em.getElementsByKey(GameElement.MAPS);
        //加载道具列表
        List<ElementObj> stars = em.getElementsByKey(GameElement.ITEM_STAR);
        List<ElementObj> shovels = em.getElementsByKey(GameElement.ITEM_SHOVEL);
        List<ElementObj> heals = em.getElementsByKey(GameElement.ITEM_HEAL);
        //加载基地
        List<ElementObj> base = em.getElementsByKey(GameElement.BASE);
        //加载基地围墙
        List<ElementObj> basewall = em.getElementsByKey(GameElement.BASEPROTECTION);


        EntityPK(play,maps);  //玩家与地图的碰撞
        EntityPK(play2,maps);
        EntityPK(play,basewall);
        EntityPK(play2,basewall);

        EntityPK(enemys,maps);//敌人与地图的碰撞
        EntityPK(fastenemys,maps);
        EntityPK(strongenemys,maps);
        EntityPK(enemys,basewall);//敌人与基地围墙的碰撞
        EntityPK(fastenemys,basewall);
        EntityPK(strongenemys,basewall);

        EntityPK(enemys,fastenemys); //敌人之间的碰撞
        EntityPK(enemys,strongenemys);
        EntityPK(fastenemys,strongenemys);

        EntityPK(play,enemys);//玩家与普通敌人的碰撞
        EntityPK(play2,enemys);
        EntityPK(play,fastenemys);//玩家与快速敌人的碰撞
        EntityPK(play2,fastenemys);//
        EntityPK(play,strongenemys);//玩家与强壮敌人的碰撞
        EntityPK(play2,strongenemys);

        EntityPK(play,play2);//玩家之间的碰撞
        EntityPK(play2,play);

        BulletPK(files,enemys);//子弹击杀敌人
        BulletPK(files,fastenemys);
        BulletPK(files,strongenemys);
        BulletPK(files,basewall); // 子弹击打加强围墙

        BulletPK(enemyfiles,play);//子弹击打玩家
        BulletPK(enemyfiles,play2);//子弹击打玩家
        BulletPK(enemyfiles,basewall); // 子弹击打加强围墙

        BulletPK1(files,maps);  //处理子弹与地图模型的关系
        BulletPK1(enemyfiles,maps);//

        StarPK(play,stars); //处理玩家对星星道具的拾取
        StarPK(play2,stars);
        ShovelPK(play,shovels);//处理玩家对铲子道具的拾取
        ShovelPK(play2,shovels);
        HealPK(play,heals);//处理玩家对疗伤道具的拾取
        HealPK(play2,heals);

        BasePK(play,base);//处理玩家一摧毁基地的方法
        BasePK(play2,base);//处理玩家二摧毁基地的方法
        BasePK(enemys,base);//处理玩家二摧毁基地的方法
        BasePK(fastenemys,base);//处理玩家二摧毁基地的方法
        BasePK(strongenemys,base);//处理玩家二摧毁基地的方法
        BasePK(files,base);//处理玩家发出子弹摧毁基地的方法
        BasePK(enemyfiles,base);//处理敌方发出子弹摧毁基地的方法
    }


    /*
    * @说明 本函数用于检查有时效性的状态，帮助元素恢复原状态
    *       服务于 星星道具，铲子道具
    *
    * @author HJH
    * */
    private void StautsCheck() {
        // 获取所有玩家
        List<ElementObj> player1s = em.getElementsByKey(GameElement.PLAY);
        List<ElementObj> player2s = em.getElementsByKey(GameElement.PLAY2);
        List<ElementObj> basewalls = em.getElementsByKey(GameElement.BASEPROTECTION);
        // 检查并更新无敌状态
        for (ElementObj player1 : player1s) {
            if (!player1.isHitable() && gameTime - player1.getStatusChangedTime() >= 1000) {
                player1.setHitable(true); // 取消无敌状态
                System.out.println("玩家1无敌状态结束");
            }
        }
        for (ElementObj player2 : player2s) {
            if (!player2.isHitable() && gameTime - player2.getStatusChangedTime() >= 1000) {
                player2.setHitable(true); // 取消无敌状态
                System.out.println("玩家2无敌状态结束");
            }
        }
        for (Iterator<ElementObj> iterator = basewalls.iterator(); iterator.hasNext(); ) {
            ElementObj basewall = iterator.next();
            // 检查 basewall 是否满足移除条件
            if (gameTime - basewall.getStatusChangedTime() >= 1000) {
                iterator.remove(); // 从列表中移除当前 basewall 元素
            }
        }

    }


    //游戏元素自动化方法
    public void moveAndUpdate( Map<GameElement, List<ElementObj>> all,long gameTime){
        //GameElement.values();//隐藏方法，返回值是一个数组，数组的顺序就是定义枚举的顺序
        for(GameElement ge:GameElement.values()){
            List<ElementObj> list = all.get(ge);
            for(int i = 0; i < list.size();i++){
                ElementObj obj = list.get(i);
                if(!obj.isLive()){ //也可以采用这样的方式：for(int i = list.size()-1; i>=0;i--)
                    obj.die();
                    list.remove(i--);
                    continue;
                }
                obj.model(gameTime);
            }
        }
    }

    /*
    * @说明 子弹类碰撞，处理子弹和其他实体之间的碰撞，调用时第一个参数必须为子弹
    * @author HJH
    * */
    public void BulletPK(List<ElementObj> listA, List<ElementObj> listB){
        for(int i = 0 ; i < listA.size();i++){
            ElementObj obj1 = listA.get(i);
            for(int j = 0; j < listB.size();j++){
                ElementObj obj2 = listB.get(j);
                if(obj1.pk(obj2)){
                    obj1.setLive(false); //子弹碰撞后消失

                    if(obj2 instanceof Enemy){//若子弹击打的是敌方坦克，则加分；地图对象等不加分
                        playerScore.AddScore(EnemyValue.ENEMY.getBotValue());
                        obj2.setLive(false);
                    }
                    if(obj2 instanceof FastEnemy){//若子弹击打的是敌方快速坦克，则加分
                        playerScore.AddScore(EnemyValue.FAST_ENEMY.getBotValue());
                        obj2.setLive(false);
                    }
                    if(obj2 instanceof StrongEnemy){//若子弹击打的是Strong坦克，则使其hp-1，直至坦克死亡时再加分
                        if(obj2.getHp() == 1){
                            playerScore.AddScore(EnemyValue.STRONG_ENEMY.getBotValue());
                            obj2.setLive(false);
                        }else{
                            obj2.setHp(obj2.getHp()-1);
                        }
                    }
                    if(obj2 instanceof Play && obj2.isHitable() || obj2 instanceof Play2 && obj2.isHitable()){ //若是玩家且玩家不处于无敌状态
                        obj2.setHp(obj2.getHp() - 1); //击中后使玩家hp -1
                        System.out.println("被击中,剩余hp为：" + obj2.getHp());
                        if(obj2.getHp() == 0){
                            obj2.setLive(false);
                        }
                    }
                    if(obj2 instanceof MapObj){
                        obj2.setLive(false);
                    }
                    break;
                }

                //后续根据敌人类型进行拓展
            }
        }
    }

    /**
     * @auther lin
     * @param listA
     * @param listB
     * 子弹与地图碰撞
     */
    private void BulletPK1(List<ElementObj> listA, List<ElementObj> listB) {
        for (ElementObj file : listA) {
            for (ElementObj mapObj : listB) {
                if (file.pk(mapObj)) {
                    // 如果文件（子弹）与地图碰撞
                    if (mapObj instanceof MapObj) {
                        MapObj map = (MapObj) mapObj;
                        if (!map.isPassable()&&!map.isRiver()) {//判断草地与河流
                            // 如果地图元素不可穿越，则文件（子弹）消失
                            if(map.isIron()){//铁墙不消失
                                file.setLive(false);
                            }else {
                                file.setLive(false);
                                map.setLive(false);
                            }
                        }
                    }
                    break; // 结束内层循环
                }
            }
        }
    }

    //非子弹类碰撞，如玩家和地图，敌人和地图之间的碰撞
    //@author HJH
    public void EntityPK(List<ElementObj> listA, List<ElementObj> listB){
        for(int i = 0 ; i < listA.size();i++){
            ElementObj obj1 = listA.get(i);
            for(int j = 0; j < listB.size();j++){
                ElementObj obj2 = listB.get(j);

                if(obj1.pk(obj2)){
                    if (obj2 instanceof MapObj) {//增加一个判断
                        MapObj mapObj = (MapObj) obj2;
                        if (obj1.pk(obj2) && !mapObj.isPassable()) {
                            obj1.handleEntityPK(); //handleEntityPK为ElementObj父类的方法，用于处理碰撞
                        }
                    }else{
                        obj1.handleEntityPK();
                    }
                }
            }
        }
    }

    /*
    * @说明 处理玩家对治疗道具的拾取,玩家拾取道具后，可以增加拾取者一条生命，使其可以多被击中一次
    * @author HJH
    * */
    private void HealPK(List<ElementObj> play, List<ElementObj> grenades) {
        for(int i = 0 ; i < play.size();i++){
            ElementObj obj1 = play.get(i);
            for(int j = 0; j < grenades.size();j++){
                ElementObj obj2 = grenades.get(j);
                if(obj1.pk(obj2)){
                    obj2.setLive(false);//碰撞后道具消失
                    obj1.handleHealPK(); //handlePK为ElementObj父类的方法，用于处理碰撞
                    playerScore.AddScore(50);
                }
            }
        }
    }

    /*
    * @说明 处理玩家对铲子道具的拾取，玩家拾取道具后，可以加固基地周边的围墙10s
    * @author HJH
    * */
    private void ShovelPK(List<ElementObj> play, List<ElementObj> shovels) {
        for(int i = 0 ; i < play.size();i++){
            ElementObj obj1 = play.get(i);
            for(int j = 0; j < shovels.size();j++){
                ElementObj obj2 = shovels.get(j);
                if(obj1.pk(obj2)){
                    obj2.setLive(false);//碰撞后道具消失
                    GameLoad.BaseWallLoad(gameTime);
                }
            }
        }
    }

    /*
    * @说明 处理玩家对星星道具的拾取的方法，玩家拾取道具后，可以获得8s的无敌时间
    * @author HJH
    * */
    private void StarPK(List<ElementObj> play, List<ElementObj> stars) {
        for(int i = 0 ; i < play.size();i++){
            ElementObj obj1 = play.get(i);
            for(int j = 0; j < stars.size();j++){
                ElementObj obj2 = stars.get(j);
                if(obj1.pk(obj2)){
                    obj2.setLive(false);//碰撞后道具消失
                    obj1.handleStarPK(gameTime); //handlePK为ElementObj父类的方法，用于处理碰撞
                }
            }
        }
    }

    /*
     * @说明 处理基地碰撞的方法，调用时顺序不能更改，基地必须为第二个参数
     * @author HJH
     * */
    private void BasePK(List<ElementObj> entity, List<ElementObj> base) {
        for(int i = 0 ; i < entity.size();i++){
            ElementObj obj1 = entity.get(i);
            for(int j = 0; j < base.size();j++){
                ElementObj obj2 = base.get(j);
                if(obj1.pk(obj2)){
                    if(entity instanceof PlayFile || entity instanceof EnemyFile) ((PlayFile) entity).setLive(false);//如果打击基地的是子弹，首先要将子弹清除
                    obj2.handleEntityPK(); //handlePK为ElementObj父类的方法，用于处理碰撞
                    playerNum = 0;
                    LevelFlag = false;
                }
            }
        }
    }


    /*
    * @说明 本方法通过见检查敌人个数检查当前关卡是否完成，同时可以在每关完成后
    * @author HJH
    * */
    public void LevelEndCheck(){
        List<ElementObj> enemyCheck = em.getElementsByKey(GameElement.ENEMY);
        List<ElementObj> fastenemyCheck = em.getElementsByKey(GameElement.FASTENEMY);
        List<ElementObj> strongenemyCheck = em.getElementsByKey(GameElement.STRONGENEMY);
        if(enemyCheck.isEmpty() && fastenemyCheck.isEmpty() && strongenemyCheck.isEmpty() && GameLoad.isEnemyGenerationComplete){
            try {
                sleep(1000);
                LevelFlag = false;

                String message = "Level " + playerLevel + " Completed\n";
                message += "Score: " + playerScore.getScore() + "\n";

                // 创建对话框并显示统计结果
                JOptionPane.showMessageDialog(parentFrame, message, "本关通过!", JOptionPane.INFORMATION_MESSAGE);
                if(playerLevel == TotalLevel){
                    System.exit(0);
                }else{
                    playerLevel++;
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }



    private void gameOver() {
        try{
            playGameOverMusic();
            sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        GameFlag = false;
        System.out.println("GameOver！");
    }


    /**
     * @author LJX
     */
    public void playStartMusic(){
        // 使用类加载器获取资源文件
        URL audioUrl = getClass().getClassLoader().getResource("audio/stage_start.wav");
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

    /**
     * @author LJX
     */
    public void playGameOverMusic(){
        // 使用类加载器获取资源文件
        URL audioUrl = getClass().getClassLoader().getResource("audio/game_over.wav");
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
