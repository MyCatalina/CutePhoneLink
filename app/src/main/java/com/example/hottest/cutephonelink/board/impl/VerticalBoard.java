package com.example.hottest.cutephonelink.board.impl;

import com.example.hottest.cutephonelink.board.AbstaractBoard;
import com.example.hottest.cutephonelink.board.GameConf;
import com.example.hottest.cutephonelink.view.Piece;

import java.util.ArrayList;
import java.util.List;

/**竖直排列方块
 * Created by hottest on 2016/3/16.
 */
public class VerticalBoard extends AbstaractBoard {
    @Override
    protected List<Piece> createPieces(GameConf config, Piece[][] pieces) {
        List<Piece> notnullPieces=new ArrayList<Piece>();
        for (int i=0;i<pieces.length;i++ ) {
            for (int j=0;j<pieces[i].length;j++){
                //只在竖排排列的时候，符合条件的去显示，每隔一列去显示，当其符合条件的时候(i%2==0的列，即只设置索引为偶数的列)，就将其添加至List集合中
                if(i%2==0){
                    //此时是单数列不会显示方块
                    Piece piece=new Piece(i,j);
                    notnullPieces.add(piece);
                }
            }
        }

        return notnullPieces;
    }
}
