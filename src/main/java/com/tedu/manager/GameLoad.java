package com.tedu.manager;
//加载器,工具类，大多提供static方法

import com.tedu.element.*;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.Timer;

public class GameLoad {
    private static ElementManager em = ElementManager.getManager();
    public static Map<String, ImageIcon> imgMap = new HashMap<>(); //用于存放图片数据
    //public static Map<String, List<ImageIcon>> imgMaps;
    private static Properties pro = new Properties();

    static long ProduceTime = 0L; //时间量，用于控制道具的生成

    private static Timer enemyTimer = new Timer();
    public static boolean isEnemyGenerationComplete = false; // 标志变量


    /*
    * @说明 传入地图id，加载方法根据文件规则自动产生加载文件
    * */
    public static void MapLoad(int mapId){
        String mapName ="map/"+mapId+".map";
        System.out.println("Map file path: " + mapName);

        //使用io流获取文件对象
        ClassLoader classLoader = GameLoad.class.getClassLoader();
        InputStream maps = classLoader.getResourceAsStream(mapName);
        if(maps == null){
            System.out.println("配置文件异常，请重新安装");
            return;
        }

        try{
            pro.clear();
            pro.load(maps);
            Enumeration<?> names = pro.propertyNames();
            //动态获取key，有了key即可获得value
            while(names.hasMoreElements()){//获取是无序的
                String key = names.nextElement().toString();
                String value = pro.getProperty(key);
                //pro.getProperty(key);
                System.out.println("Key: " + key + ", Value: " + value);
                String [] arrs=pro.getProperty(key).split(";");
                for(int i = 0; i < arrs.length;i++){
                    ElementObj element = new MapObj().createElement(key+","+arrs[i]);
                    em.addElement(element,GameElement.MAPS);
                }
            }
            System.out.println(names);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void BaseWallLoad(long gameTime){
        String fileName ="map/basewall.map";

        //使用io流获取文件对象
        ClassLoader classLoader = GameLoad.class.getClassLoader();
        InputStream basewalls = classLoader.getResourceAsStream(fileName);
        if(basewalls == null){
            System.out.println("配置文件异常，请重新安装");
            return;
        }

        try{
            pro.clear();
            pro.load(basewalls);
            Enumeration<?> names = pro.propertyNames();
            //动态获取key，有了key即可获得value
            while(names.hasMoreElements()){//获取是无序的
                String key = names.nextElement().toString();
                String value = pro.getProperty(key);
                System.out.println("Key: " + key + ", Value: " + value);
                String [] arrs=pro.getProperty(key).split(";");
                for(int i = 0; i < arrs.length;i++){
                    ElementObj element = new BaseProtection().createElement(key+","+arrs[i]);
                    element.setStatusChangedTime(gameTime);
                    em.addElement(element,GameElement.BASEPROTECTION);
                }
            }

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /*
    * @说明 加载玩家坦克图片，加载敌人坦克图片,加载道具图片
    *      图片地址在GameData.pro文件下修改
    * */
    public static void loadImg(){//后期更改可以带参数，为不同关卡的图片加载做准备
        String textUrl = "GameData.pro";
        ClassLoader classLoader = GameLoad.class.getClassLoader();
        InputStream texts = classLoader.getResourceAsStream(textUrl);
        //imgMap用于存放数据
        pro.clear();
        try{
            pro.load(texts);
            Set<Object> set = pro.keySet();
            for(Object o:set){
                String url = pro.getProperty(o.toString());
                //System.out.println(url);
                imgMap.put(o.toString(),new ImageIcon(url));
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void PlayLoad(int playerNum) { //操作坦克加载 //LJX修改
        if (playerNum==1){
            String playStr = "500,500,up"; //给出不一样的字符串传参，可以实现多玩家定义,如: "player1,500,500,up,37,38,39,40,10"
            ElementObj obj = getObj("Play"); //加载类名为"Play"的类
            ElementObj play = obj.createElement(playStr);

            em.addElement(play,GameElement.PLAY);
        } else if (playerNum==2) {
            String playStr = "500,500,up"; //给出不一样的字符串传参，可以实现多玩家定义,如: "player1,500,500,up,37,38,39,40,10"
            ElementObj obj = getObj("Play"); //加载类名为"Play"的类
            ElementObj play = obj.createElement(playStr);

            String playStr2 = "300,500,up2";
            ElementObj obj2 = getObj("Play2");
            ElementObj play2 = obj2.createElement(playStr2);

            em.addElement(play,GameElement.PLAY);
            em.addElement(play2,GameElement.PLAY2);
        }else {
            System.out.println("人数有误");
        }
    }

    public static void EnemyLoad(int playerLevel){
        // 重新初始化 enemyTimer
        if (enemyTimer != null) {
            enemyTimer.cancel();
        }
        enemyTimer = new Timer();

        int normalEnemyNum = EnemyNum.getNormalEnemyCountByLevel(playerLevel);
        int fastEnemyNum = EnemyNum.getFastEnemyCountByLevel(playerLevel);
        int strongEnemyNum = EnemyNum.getStrongEnemyCountByLevel(playerLevel);

        long delay = 8000L; // 每8秒生成一批敌人
        TimerTask generateEnemies = new TimerTask() {
            private int normalCounter = 0;
            private int fastCounter = 0;
            private int strongCounter = 0;

            @Override
            public void run() {
                if (normalCounter < normalEnemyNum) {
                    generateEnemy("Enemy", "bot_down", GameElement.ENEMY);
                    normalCounter++;
                }
                if (fastCounter < fastEnemyNum) {
                    generateEnemy("FastEnemy", "fast_enemy_down", GameElement.FASTENEMY);
                    fastCounter++;
                }
                if (strongCounter < strongEnemyNum) {
                    generateEnemy("StrongEnemy", "strong_enemy_down", GameElement.STRONGENEMY);
                    strongCounter++;
                }
                if (normalCounter >= normalEnemyNum && fastCounter >= fastEnemyNum && strongCounter >= strongEnemyNum) {
                    enemyTimer.cancel();
                    isEnemyGenerationComplete = true; // 设置敌人生成完成标志
                }
            }
        };
        isEnemyGenerationComplete = false; // 重置敌人生成完成标志
        enemyTimer.schedule(generateEnemies, 0, delay);
    }

    private static void generateEnemy(String enemyType, String enemyStr, GameElement gameElement) {
        boolean validPosition;
        ElementObj enemy;
        do {
            validPosition = true;
            ElementObj obj = getObj(enemyType);
            enemy = obj.createElement(enemyStr);

            List<ElementObj> maps = em.getElementsByKey(GameElement.MAPS);
            for (ElementObj map : maps) {
                if (enemy.pk(map)) {
                    validPosition = false;
                    break;
                }
            }
        } while (!validPosition);
        em.addElement(enemy, gameElement);
    }

    //Score类作为一个对象，加载进元素管理器中
    public static Score ScoreLoad(){//加载玩家得分
        ElementObj obj = getObj("Score");
        ElementObj score = obj.createElement("");
        em.addElement(score,GameElement.SCORE);
        return (Score) score;
    }

    //基地生成器，每一关基地的位置固定
    public static void BaseLoad() {
        ElementObj obj = getObj("Base");
        ElementObj base = obj.createElement("Base");
        em.addElement(base,GameElement.BASE);
    }

    /*
    * @说明 道具生成工厂，用途是能够在游戏运行过程之中随机生成不同效果的道具
    *       由游戏线程GameThread在GameRun函数之中调用
    *       能够实现道具的添加和删除
    * */
    public static void ItemHandler(long gameTime) {

        //if(gameTime % 100 == 0)System.out.println("游戏时间:" + gameTime);
        int TotalItem = 3; //道具总数，后续拓展道具时更改数目
        String StarStr = "Item_star";
        String ShovelStr = "Item_shovel";
        String HealStr = "Item_heal";
        Random random = new Random();
        int ProduceType = random.nextInt(TotalItem); //随机数，用于随机生成道具
        boolean validPosition; //判断道具生成位置是否合理的变量

        // 下述代码为检查道具类存在时间的代码，原理是
        // 获取所有类型的道具列表
        List<ElementObj> HealList = em.getElementsByKey(GameElement.ITEM_HEAL);
        List<ElementObj> StarList = em.getElementsByKey(GameElement.ITEM_STAR);
        List<ElementObj> ShovelList = em.getElementsByKey(GameElement.ITEM_SHOVEL);

        // 检查并移除存在时间超过15秒的道具
        List<ElementObj> allItems = new ArrayList<>();
        allItems.addAll(HealList);
        allItems.addAll(StarList);
        allItems.addAll(ShovelList); //合并道具列表

        Iterator<ElementObj> iterator = allItems.iterator();
        while (iterator.hasNext()) {
            ElementObj item = iterator.next();
            if (gameTime - item.getProduceTime() >= 800) { //允许道具存在时间为8s
                item.setLive(false);
                //System.out.println("移除了道具");
            }
        }


        //if块内为道具生成函数
        if(gameTime - ProduceTime >= 300){ //每隔3s生成一个道具
            ProduceTime = gameTime; //更新生成时间
            ElementObj Item = null;
            do {
                validPosition = true;
                switch (ProduceType){
                    case 0://随机数为0时生成无敌道具
                        ElementObj Starobj = getObj(StarStr);
                        Item = Starobj.createElement(StarStr);
                        break;
                    case 1: //随机数为1时生成铲子道具
                        ElementObj Shovelobj = getObj(ShovelStr);
                        Item = Shovelobj.createElement(ShovelStr);
                        break;
                    case 2://随机数为2时生成治疗道具
                        ElementObj Healobj = getObj(HealStr);
                        Item = Healobj.createElement(HealStr);
                        break;
                }
                // 检查道具是否与地图重叠
                List<ElementObj> maps = em.getElementsByKey(GameElement.MAPS);
                for (ElementObj map : maps) {
                    if (Item.pk(map) && Item.pk(Item)) { // 用pk方法检测重叠
                        validPosition = false;
                        break;
                    }
                }
            } while (!validPosition);

            Item.setProduceTime(gameTime);//存储物品的生成时间

            //将道具添加进元素管理器
            if(ProduceType == 0){em.addElement(Item,GameElement.ITEM_STAR);}
            if(ProduceType == 1){em.addElement(Item,GameElement.ITEM_SHOVEL);}
            if(ProduceType == 2){em.addElement(Item,GameElement.ITEM_HEAL);}
        }
    }

    /*
    * @说明 将obj.pro之中定义的类名加载进来
    * */
    private static Map<String,Class<?>> objMap = new HashMap<>();
    public static void loadObj(){
        String texturl = "obj.pro";
        ClassLoader classLoader = GameLoad.class.getClassLoader();
        InputStream texts = classLoader.getResourceAsStream(texturl);
        pro.clear();
        try{
            pro.load(texts);
            Set<Object> set = pro.keySet();
            for(Object o:set){
                String classurl = pro.getProperty(o.toString());
                Class<?> forName = Class.forName(classurl);
                objMap.put(o.toString(),forName);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /*
    * @说明 用于获取类，只需要传入类名，即可返回对应类名的对象
    * */
    public static ElementObj getObj(String str){
        try{
            Class<?> class1 = objMap.get(str);
            Object newInstance = class1.newInstance();
            if(newInstance instanceof ElementObj){
                return (ElementObj)newInstance;
            }
        }catch (InstantiationException e){
            e.printStackTrace();
        }catch(IllegalAccessException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @auther lin
     * 加载背景
     */
    public static void loadBackground() {
        Background choose = new Background();
        ElementObj obj = choose.createElement("");
        em.addElement(obj, GameElement.Background);
    }
    /**
     * @author lin
     * 加载单人按钮
     */
    public static void loadDanButton(){
        DanButton choose = new DanButton();
        ElementObj obj = choose.createElement("");
        em.addElement(obj, GameElement.DanButton);
    }
    /**
     * @author lin
     * 加载单人按钮
     */
    public static void loadSuanButton(){
        SuanButton choose = new SuanButton();
        ElementObj obj = choose.createElement("");
        em.addElement(obj, GameElement.SuanButton);
    }

    /**
     * @author lin
     * 加载道具说明按钮
     */
    public static void loadPropButton(){
        PropButton choose = new PropButton();
        ElementObj obj = choose.createElement("");
        em.addElement(obj, GameElement.PropButton);
    }
}
