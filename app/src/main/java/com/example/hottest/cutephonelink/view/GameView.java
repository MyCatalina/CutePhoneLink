package com.example.hottest.cutephonelink.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import com.example.hottest.cutephonelink.R;
import com.example.hottest.cutephonelink.board.GameService;
import com.example.hottest.cutephonelink.object.LinkInfo;
import com.example.hottest.cutephonelink.util.ImageUtil;

import java.util.List;

/**
 * Created by hottest on 2016/3/13.
 */
public class GameView extends View {
    //游戏逻辑的实现类
    private GameService gameService;
    private Piece selectedPiece;
    //连接信息对象
    private LinkInfo linkInfo;
    //选中标识的图片对象
    private Bitmap selectedImage;
    private Paint paint;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.paint = new Paint();
        //R.drawable.heart The resource id of the image data
        //BitmapShader用于图片的渲染,使用位图平铺作为连接线
        BitmapShader bitmapShader = new BitmapShader(BitmapFactory.decodeResource(this.getResources(), R.drawable.heart),
                Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        paint.setShader(bitmapShader);
        this.selectedImage = ImageUtil.getSelectedImage(context);
    }

    public void setGameService(GameService gameService) {
        this.gameService = gameService;
    }

    public void setLinkInfo(LinkInfo linkInfo) {
        this.linkInfo = linkInfo;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.gameService == null)
            return;
        Piece[][] pieces = gameService.getPieces();
        if (pieces != null) {
            for (int i = 0; i < pieces.length; i++) {
                for (int j = 0; j < pieces[i].length; j++) {
                    if (pieces[i][j] != null) {
                        Piece piece = pieces[i][j];
                        canvas.drawBitmap(piece.getPieceImage().getBitmap(), piece.getBeginX(), piece.getBeginY(), null);
                    }
                }
            }
        }
        if (this.linkInfo != null) {
            //绘制连接线
            drawLine(this.linkInfo, canvas);
            //处理完后清空linkInfo对象
            this.linkInfo=null;
        }
        //画选中标识的图片
        if(this.selectedPiece!=null){
            canvas.drawBitmap(this.selectedImage,selectedPiece.getBeginX(),selectedPiece.getBeginY(),null);
        }
    }
    //根据linkInfo绘制连接线的方法
    public void drawLine(LinkInfo linkInfo,Canvas canvas){
         List<Point> linkPoints = linkInfo.getLinkPoints();
        //遍历linkInfo中的所有点
        for(int i=0;i<linkPoints.size()-1;i++){
            Point currentpoint=linkPoints.get(i);
            Point nextpoint=linkPoints.get(i+1);
            canvas.drawLine(currentpoint.x,currentpoint.y,nextpoint.x,nextpoint.y,paint);
        }
    }
    //设置当前选中的选中方块的方法
    public void setSelectPiece(Piece piece){
        this.selectedPiece=piece;
    }
    public void startGame(){
        this.gameService.start();
        this.postInvalidate();
    }
}
