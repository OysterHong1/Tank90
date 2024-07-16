package com.tedu.manager;
//元素管理器，专门储存所有元素，同时提供方法，给予视图和控制获取数据

import com.tedu.element.ElementObj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ElementManager {
    private List<Object> listMap;
    private List<Object> listPlay;

    //GameElement作为key用来区分不同资源，用于获取资源
    //List中元素的泛型是元素基类
    //元素存放到map集合类之中，显示模块只需要获取map即可
    //显示通过调用showElement()实现
    private Map<GameElement,List<ElementObj>> gameElements;
    public Map<GameElement, List<ElementObj>> getGameElements() {
        return gameElements;
    }

    public void addElement(ElementObj obj,GameElement ge){
        List<ElementObj> list = gameElements.get(ge);
        list.add(obj);//添加对象到集合中，按key值就行存储
    }

    public List<ElementObj> getElementsByKey(GameElement ge){
        return gameElements.get(ge);//依据key值返回list集合，取出某一类元素
    }

    /*单例模式：内存中有且只有一个实例
    编写过程中：
    1.需要一个静态属性单例的引用
    2.提供一个静态方法return单例的引用
    3.私有化构造方法*/
    private static ElementManager EM = null;
    public static synchronized ElementManager getManager(){
        //方法需要使用线程锁,保证方法执行过程中只有一个线程
        if(EM == null){
            EM = new ElementManager();
        }
        return EM;
    }

    /*static{
        EM = ElementManager();
    }*/ //饿汉实例化对象 静态语句块在类被加载的时候直接执行

    private ElementManager(){
        init(); //实例化方法
    }

    public void init(){
        //实例化在这里完成
        //为将来可能出现的功能扩展做准备，可重写init方法
        gameElements = new HashMap<GameElement,List<ElementObj>>();
        for(GameElement ge: GameElement.values()){
            gameElements.put(ge,new ArrayList<ElementObj>());
        }
    }

    public void clear(){
        for (GameElement ge : GameElement.values()) {
            gameElements.get(ge).clear();
        }
    }
}
