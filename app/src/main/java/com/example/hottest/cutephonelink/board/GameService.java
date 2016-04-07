package com.example.hottest.cutephonelink.board;

import android.graphics.Point;

import com.example.hottest.cutephonelink.object.LinkInfo;
import com.example.hottest.cutephonelink.view.Piece;

/**
 * Created by hottest on 2016/3/13.
 */
public interface GameService {
    /**
     * 初始化游戏状态，控制游戏开始的方法
     */
    void start();

    Piece[][] getPieces();

    /**
     * 判断Piece[][]中是否还存在非空的Piece对象，如果存在，返回true,否则返回false
     */
    Boolean hasPieces();

    /**
     * 根据鼠标的点击返回一个Piece对象
     *
     * @ param touchX表示所点击的X坐标
     * @ param touchY表示所点击的Y坐标
     */
    Piece findPiece(float touchX, float touchY);

    /**
     * 判断两个Piece是否可以连接，如果可以的话，返回LinkInfo对象，否则返回null
     *
     * @param p1 第一个Piece对象
     * @param p2 第二个Piece对象
     */
    LinkInfo link(Piece p1, Piece p2);
}
