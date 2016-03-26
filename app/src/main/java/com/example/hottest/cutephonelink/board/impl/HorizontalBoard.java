package com.example.hottest.cutephonelink.board.impl;

import com.example.hottest.cutephonelink.board.AbstaractBoard;
import com.example.hottest.cutephonelink.board.GameConf;
import com.example.hottest.cutephonelink.view.Piece;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hottest on 2016/3/16.
 */
public class HorizontalBoard extends AbstaractBoard {
    @Override
    protected List<Piece> createPieces(GameConf config, Piece[][] pieces) {
        List<Piece> notnullPiece = new ArrayList<Piece>();
        for (int i = 0; i < pieces.length; i++) {
            for (int j = 0; j < pieces[i].length; j++) {
                if (j % 2 == 0) {
                    //在水平的布局中，单数行不会创建方块
                    Piece piece = new Piece(i, j);
                    notnullPiece.add(piece);
                }
            }
        }
        return notnullPiece;
    }
}
