package com.example.hottest.cutephonelink.view;

import android.graphics.Bitmap;

/**
 * Created by hottest on 2016/3/13.
 */

/**
 * 此类封装了游戏界面上绘制的方块，以及其ID标识
 */
public class PieceImage {
    private Bitmap bitmap;
    private int imageID;

    public PieceImage(Bitmap bitmap, int imageID) {
        this.bitmap = bitmap;
        this.imageID = imageID;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public int getImageID() {
        return imageID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }
}
