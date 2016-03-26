package com.example.hottest.cutephonelink.board.impl;

import android.graphics.Point;

import com.example.hottest.cutephonelink.board.GameService;
import com.example.hottest.cutephonelink.object.LinkInfo;
import com.example.hottest.cutephonelink.view.Piece;

/**
 * Created by hottest on 2016/3/14.
 */
public class GameServiceImpl implements GameService {
    @Override
    public void start() {


    }

    @Override
    public Piece[][] getPieces() {
        return new Piece[0][];
    }

    @Override
    public Boolean hasPieces() {
        return null;
    }

    @Override
    public Piece findPiece(float touchX, float touchY) {
        return null;
    }

    @Override
    public LinkInfo link(Point p1, Point p2) {
        return null;
    }
}
