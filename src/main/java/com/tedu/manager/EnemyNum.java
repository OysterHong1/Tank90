package com.tedu.manager;

/*
 * @说明 枚举类，用于存储每关中敌人的数量。
 *       增加新的关卡时，除添加枚举项之外，还要修改getEnemyCountByLevel函数
 * */

public enum EnemyNum {
    LEVEL_1(5, 3, 2),
    LEVEL_2(10, 5, 4),
    LEVEL_3(12, 6, 5),
    LEVEL_4(12,6,6),
    LEVEL_5(12,7,7),
    LEVEL_6(10,10,8),
    LEVEL_7(8,12,8),
    LEVEL_8(4,10,10),
    LEVEL_9(7,10,12),
    LEVEL_10(0,0,20);

    private final int normalEnemyCount;
    private final int fastEnemyCount;
    private final int strongEnemyCount;

    EnemyNum(int normalEnemyCount, int fastEnemyCount, int strongEnemyCount) {
        this.normalEnemyCount = normalEnemyCount;
        this.fastEnemyCount = fastEnemyCount;
        this.strongEnemyCount = strongEnemyCount;
    }

    public static int getNormalEnemyCountByLevel(int playerLevel) {
        switch (playerLevel) {
            case 1:
                return LEVEL_1.getNormalEnemyCount();
            case 2:
                return LEVEL_2.getNormalEnemyCount();
            case 3:
                return LEVEL_3.getNormalEnemyCount();
            case 4:
                return LEVEL_4.getNormalEnemyCount();
            case 5:
                return LEVEL_5.getNormalEnemyCount();
            case 6:
                return LEVEL_6.getNormalEnemyCount();
            case 7:
                return LEVEL_7.getNormalEnemyCount();
            case 8:
                return LEVEL_8.getNormalEnemyCount();
            case 9:
                return LEVEL_9.getNormalEnemyCount();
            case 10:
                return LEVEL_10.getNormalEnemyCount();
            // 添加更多关卡时，继续增加 case
            default:
                throw new IllegalArgumentException("Invalid level");
        }
    }

    public static int getFastEnemyCountByLevel(int playerLevel) {
        switch (playerLevel) {
            case 1:
                return LEVEL_1.getFastEnemyCount();
            case 2:
                return LEVEL_2.getFastEnemyCount();
            case 3:
                return LEVEL_3.getFastEnemyCount();
            case 4:
                return LEVEL_4.getFastEnemyCount();
            case 5:
                return LEVEL_5.getFastEnemyCount();
            case 6:
                return LEVEL_6.getFastEnemyCount();
            case 7:
                return LEVEL_7.getFastEnemyCount();
            case 8:
                return LEVEL_8.getFastEnemyCount();
            case 9:
                return LEVEL_9.getFastEnemyCount();
            case 10:
                return LEVEL_10.getFastEnemyCount();
            // 添加更多关卡时，继续增加 case
            default:
                throw new IllegalArgumentException("Invalid level");
        }
    }

    public static int getStrongEnemyCountByLevel(int playerLevel) {
        switch (playerLevel) {
            case 1:
                return LEVEL_1.getStrongEnemyCount();
            case 2:
                return LEVEL_2.getStrongEnemyCount();
            case 3:
                return LEVEL_3.getStrongEnemyCount();
            case 4:
                return LEVEL_4.getStrongEnemyCount();
            case 5:
                return LEVEL_5.getStrongEnemyCount();
            case 6:
                return LEVEL_6.getStrongEnemyCount();
            case 7:
                return LEVEL_7.getStrongEnemyCount();
            case 8:
                return LEVEL_8.getStrongEnemyCount();
            case 9:
                return LEVEL_9.getStrongEnemyCount();
            case 10:
                return LEVEL_10.getStrongEnemyCount();
            // 添加更多关卡时，继续增加 case
            default:
                throw new IllegalArgumentException("Invalid level");
        }
    }

    public int getNormalEnemyCount() {
        return this.normalEnemyCount;
    }

    public int getFastEnemyCount() {
        return this.fastEnemyCount;
    }

    public int getStrongEnemyCount() {
        return this.strongEnemyCount;
    }
}
