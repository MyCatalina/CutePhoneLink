package com.example.hottest.cutephonelink.board;

import com.example.hottest.cutephonelink.util.ImageUtil;
import com.example.hottest.cutephonelink.view.Piece;
import com.example.hottest.cutephonelink.view.PieceImage;

import java.util.List;

/**初始化游戏状态数据
 * Created by hottest on 2016/3/16.
 */
public abstract class AbstaractBoard {
    protected abstract List<Piece> createPieces(GameConf config,Piece[][] pieces);
    public Piece[][] create(GameConf config){
        //创建Piece[][]数组
        Piece[][] pieces=new Piece[config.getxSize()][config.getySize()];

        //返回非空的Piece集合，该集合由子类创建
        List<Piece> nullpieces=createPieces(config, pieces);

        //根据非空Piece对象的集合的大小来取图片
         List<PieceImage> playImages = ImageUtil.getPlayImages(config.getContext(), nullpieces.size());
        //所有图片的宽高均相等
       int imageWidth=playImages.get(0).getBitmap().getWidth();
        int imageHeight=playImages.get(0).getBitmap().getHeight();
        /*int imageHeight=config.PIECE_HEIGHT;
        int imageWidth=config.PIECE_WIDTH*/

        //遍历非空集合
        for (int i=0;i<nullpieces.size();i++) {
            //依次获得每个Piece对象
           Piece piece = nullpieces.get(i);
            piece.setPieceImage(playImages.get(i));
            //计算每个方块左上角X,Y坐标,
            piece.setBeginX(config.getBeginImageX()+piece.getIndexX()*imageWidth);
            piece.setBeginY(config.getBeginImageY()+piece.getIndexY()*imageHeight);

            //将该方块对象放入方块数组的相应位置处
            pieces[piece.getIndexX()][piece.getIndexY()]=piece;

        }
        return pieces;

    }

}
