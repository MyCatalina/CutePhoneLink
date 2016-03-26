package com.example.hottest.cutephonelink.board.impl;

import com.example.hottest.cutephonelink.board.AbstaractBoard;
import com.example.hottest.cutephonelink.board.GameConf;
import com.example.hottest.cutephonelink.view.Piece;

import java.util.ArrayList;
import java.util.List;

/**以矩阵形式显示初始游戏界面
 * Created by hottest on 2016/3/16.
 */
public class FullBoard extends AbstaractBoard {
    @Override
    protected List<Piece> createPieces(GameConf config, Piece[][] pieces) {
        List<Piece> notNullPieces=new ArrayList<Piece>();
        //此时显示的，横纵排均要少于pieces
        for (int i=1;i<pieces.length-1;i++) {
            for (int j=1;j<pieces[i].length-1;j++) {
                //只需要设置piece在矩阵中的坐标即可
                Piece piece=new Piece(i,j);
                notNullPieces.add(piece);
            }
        }
        return notNullPieces;
    }
}
