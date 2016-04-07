package com.example.hottest.cutephonelink;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.hottest.cutephonelink.board.GameConf;
import com.example.hottest.cutephonelink.board.impl.GameServiceImpl;
import com.example.hottest.cutephonelink.object.LinkInfo;
import com.example.hottest.cutephonelink.view.GameView;
import com.example.hottest.cutephonelink.view.Piece;
import java.util.Timer;
import java.util.TimerTask;

public class Link extends ActionBarActivity {

    private GameConf gameConf;
    private GameView gameView;
    private TextView timeText;
    //失败后弹出的对话框
    private AlertDialog.Builder lostDialog;
    //游戏胜利后的对话框
    private AlertDialog.Builder successDialog;
    //游戏暂停后的对话框
    private AlertDialog.Builder pauseDialog;
    private Button starButton;
    private Piece selected=null;
    private GameServiceImpl gameService;
    //定时器
    private Timer timer=new Timer();
    //记录游戏剩余时间
    private int gameTime;
    //记录当前是否处于游戏状态
    private boolean isPlaying;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link);
        //隐藏ActionBar
        getSupportActionBar().hide();
        init();
    }
    private Handler handler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x123:
                    timeText.setText("剩余时间："+gameTime);
                    gameTime--;
                    //当时间小于0，游戏失败
                    if(gameTime<0){
                        stopTimer();
                        isPlaying=false;
                        lostDialog.show();
                        return;
                    }
                   break;

            }
        }
    };

    //初始化界面中对游戏卡片的绘制，以及卡片的点击事件（及其处理），
    public void init() {
        gameConf = new GameConf(2, 10, this, 500000, 8, 9);
        gameView = (GameView) findViewById(R.id.gameView);
        //找到显示游戏剩余时间的TextView
        timeText = (TextView) findViewById(R.id.timeText);
        //拿到开始按钮
        starButton = (Button) findViewById(R.id.starButton);
        gameService = new GameServiceImpl(this.gameConf);
        gameView.setGameService(gameService);
        //为开始按钮添加单击事件绑定监听器
        starButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //进入页面后直接开始游戏
                startGame(gameConf.DEFAULT_TIME);
            }
        });
        //为当前的gameView视图绑定TouchListener事件，处理其中的卡片点击事件
        this.gameView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    gameViewTouchDown(event);
                }
                if(event.getAction()==MotionEvent.ACTION_UP){
                    gameViewTouchUp(event);
                }
                return true;
            }
        });
        // 初始化游戏失败的对话框
        lostDialog =createDialog("Lost","失败啦，重新开始吧",R.drawable.lost)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startGame(GameConf.DEFAULT_TIME);
                            }
                        }
                );
        //初始化游戏胜利时的对话框,本轮游戏胜出之后，开始新的游戏
        successDialog=createDialog("Success","本轮游戏成功，进入下级吧",R.drawable.success)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startGame(GameConf.DEFAULT_TIME);
                            }
                        }
                );

      /*  //暂停游戏的对话框
        pauseDialog=createDialog("pause","游戏暂停一下哦",R.drawable.pause)
                .setPositiveButton("暂停", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                gameView.setVisibility(View.VISIBLE);
                startGame(gameTime);
            }
        }
        );*/
    }
    //当本activity失去焦点的时候
    @Override
    protected void onPause() {
        //失去焦点的时候，暂停游戏
        stopTimer();
        super.onPause();
    }
    //当本activity获取焦点，可以交互的时候
    @Override
    protected void onResume() {
        //如果当前游戏正处于游戏状态时，以剩余时间重写开始游戏
        if(isPlaying){
            startGame(gameTime);
        }
        super.onResume();
    }
    //触碰游戏区域的处理方法,重新绘制此gameView
    private void gameViewTouchUp(MotionEvent event) {
        this.gameView.postInvalidate();
    }

    /**当点击屏幕的时候,*/
    public void gameViewTouchDown(MotionEvent e){
        //(1)判断所点击的位置是否有卡片存在
         Piece[][] pieces = gameService.getPieces();
        //获取用户点击屏幕的坐标
        float touchX = e.getX();
        float touchY = e.getY();
        //查找当前点击的位置是否有图片存在
        Piece currentPiece=gameService.findPiece(touchX,touchY);
        if(currentPiece==null){
            return;
        }
        //(2)当有卡片存在的时候，判断其点击的第一张还是第二张
        this.gameView.setSelectPiece(currentPiece);
        //表示之前没有选中任何一个Piece
        if(this.selected==null){
            //将当前piece设置为已selected的piece，并重新绘制GamePanel
            this.selected=currentPiece;
            this.gameView.postInvalidate();
            return;
        }
        if(this.selected!=null){
            //连接这两个卡片
             LinkInfo link = this.gameService.link(selected, currentPiece);
            if(link==null){
                //当前卡片不可连接的时候，
                this.selected=currentPiece;
                this.gameView.postInvalidate();
                return;
            }else{
                //当连接成功的时候，处理连接成功的情况
                handleSuccessLink(link,selected,currentPiece,pieces);
            }
        }
    }
    //当两个方块相同时，进行连接
    private void handleSuccessLink(LinkInfo link,Piece prePiece,Piece currentPiece,Piece[][] pieces){
        //gameView处理LinkInfo
        this.gameView.setLinkInfo(link);
        this.gameView.setSelectPiece(null);
        this.gameView.postInvalidate();
        //将这两个piece对象从Piece[][]数组中删除
        pieces[prePiece.getIndexX()][prePiece.getIndexY()]=null;
        pieces[currentPiece.getIndexX()][currentPiece.getIndexY()]=null;
        //将当前选中的方块设置为null
        this.selected=null;
        //当前pieces中若没有剩余的方块，则游戏胜出
        if(!this.gameService.hasPieces()){
            //游戏胜利
            successDialog.show();
            stopTimer();
            isPlaying=false;
        }
    }
    //以gameTime作为剩余时间开始或者恢复游戏
    public void startGame(int gameTime){
     //首先判断当前是否还有定时器存在
        if(this.timer!=null){
           stopTimer();
        }
        //重新设置游戏的时间
        this.gameTime=gameTime;
      //如果游戏剩余时间与游戏总时间相同，即为重新开始
        if(gameTime==gameConf.DEFAULT_TIME){
            gameView.startGame();
        }
        //设置当前正在处于游戏的状态
        isPlaying=true;
        this.timer=new Timer();
        //启动定时器，每隔一秒发送一次消息
        this.timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //此时传递的只是Message的what值
                handler.sendEmptyMessage(0x123);

            }
        },0,1000);
        this.selected=null;

    }
    //创建对话框的工具
    private AlertDialog.Builder createDialog(String title,String message,int imageSource){
        return new AlertDialog.Builder(this).setTitle(title)
                .setMessage(message).setIcon(imageSource);
    }
    private void stopTimer(){
        //停止定时器
        this.timer.cancel();
        this.timer=null;
    }
}
