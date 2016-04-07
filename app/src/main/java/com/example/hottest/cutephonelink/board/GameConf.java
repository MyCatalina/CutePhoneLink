package com.example.hottest.cutephonelink.board;

import android.content.Context;

/**
 * 保存游戏的一些配置信息
 * Created by hottest on 2016/3/16.
 */

public class GameConf {
    //设置连连看中每个方块的宽高
    public static final int PIECE_WIDTH = 50;
    public static final int PIECE_HEIGHT = 50;
    //设置游戏的默认时间
    public static int DEFAULT_TIME = 50;
    //在Piece[][]中，数组一维的长度
    private int xSize;
    //在Piece[][]中，数组二维的长度
    private int ySize;
    //Board中第一张图片出现的X坐标
    private int beginImageX;
    //board中第一张图片出现的Y坐标
    private int beginImageY;
    //记录游戏的总时间
    private long gametime;
    private Context context;

    public GameConf(int beginImageX, int beginImageY, Context context, long gametime, int xSize, int ySize) {
        this.beginImageX = beginImageX;
        this.beginImageY = beginImageY;
        this.context = context;
        this.gametime = gametime;
        this.xSize = xSize;
        this.ySize = ySize;
    }

    public int getBeginImageX() {
        return beginImageX;
    }

    public int getBeginImageY() {
        return beginImageY;
    }

    public Context getContext() {
        return context;
    }

    public long getGametime() {
        return gametime;
    }

    public int getxSize() {
        return xSize;
    }

    public int getySize() {
        return ySize;
    }
}
