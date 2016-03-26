package com.example.hottest.cutephonelink.view;

/**
 * Created by hottest on 2016/3/12.
 */
/**Piece类封装游戏界面一个方块的具体位置，包括其相对于界面的位置（X,Y），以及在所有方块中的位置*/

public class Piece {
    private PieceImage pieceImage;
    //方块左上角在布局界面的位置
    private int beginX;
    private int beginY;
    //方块在Piece[][]二位数据中的位置
    private int indexX;
    private int indexY;
//设置Piece在棋盘数组中的位置
    public Piece(int indexX, int indexY) {
        this.indexX = indexX;
        this.indexY = indexY;
    }

    public int getBeginX() {
        return beginX;
    }

    public void setBeginX(int beginX) {
        this.beginX = beginX;
    }

    public int getBeginY() {
        return beginY;
    }

    public void setBeginY(int beginY) {
        this.beginY = beginY;
    }

    public int getIndexX() {
        return indexX;
    }

    public void setIndexX(int indexX) {
        this.indexX = indexX;
    }

    public int getIndexY() {
        return indexY;
    }

    public void setIndexY(int indexY) {
        this.indexY = indexY;
    }

    public PieceImage getPieceImage() {
        return pieceImage;
    }

    public void setPieceImage(PieceImage pieceImage) {
        this.pieceImage = pieceImage;
    }
    //判断两个piece是否为同一个
    public boolean isSameImage(Piece other){
        if(pieceImage==null){
            if(other.pieceImage!=null){
                return false;
            }
        }
        //当两个方块的ImageID相同的时候，即可认为两个方块相同
        return pieceImage.getImageID()==other.pieceImage.getImageID();
    }
}
