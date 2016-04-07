package com.example.hottest.cutephonelink.board.impl;

import android.graphics.Point;

import com.example.hottest.cutephonelink.Link;
import com.example.hottest.cutephonelink.board.AbstaractBoard;
import com.example.hottest.cutephonelink.board.GameConf;
import com.example.hottest.cutephonelink.board.GameService;
import com.example.hottest.cutephonelink.object.LinkInfo;
import com.example.hottest.cutephonelink.view.Piece;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by hottest on 2016/3/14.
 */
public class GameServiceImpl implements GameService {
    private Piece[][] pieces;
    private GameConf gameConf;

    public GameServiceImpl(GameConf gameConf) {
        this.gameConf = gameConf;
    }

    //初始化游戏界面
    @Override
    public void start() {
        AbstaractBoard board = null;
        Random random = new Random();
        //随机生成一个board的子类
        int i = random.nextInt(11);
        switch (i) {
            case 3:
                //生成一个竖排的GameView初始界面
                board = new VerticalBoard();
                break;
            case 2:
                //生成一个横排的GameView初始界面
                board = new HorizontalBoard();
                break;
            default:
                board = new FullBoard();
                break;
        }
        //初始化Piece[][]数组
        pieces = board.create(this.gameConf);

    }

    @Override
    public Piece[][] getPieces() {
        return this.pieces;
    }

    @Override
    public Boolean hasPieces() {
        for (int i = 0; i < pieces.length; i++) {
            for (int j = 0; j < pieces[i].length; i++) {
                //当有任意一个Piece存在，即可判断当前界面中还有方块
                if (pieces[i][j] != null) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 根据触碰点的位置找到此方块，如何将触碰点的位置换算为
     */
    @Override
    public Piece findPiece(float touchX, float touchY) {
        //首先计算出此触碰点与开始点之间的距离
        int relativeX = (int) touchX - this.gameConf.getBeginImageX();
        int relativeY = (int) touchY - this.gameConf.getBeginImageY();
        if (relativeX < 0 || relativeY < 0) {
            return null;
        }
        //获取此点在数组中的相对位置
        int size = this.gameConf.PIECE_HEIGHT;
        //计算出其相对位置点应该代表的卡片的在pieces数组中的位置
        int indexX = getIndex(size, relativeX);
        int indexY = getIndex(size, relativeY);
        //当计算出来的位置索引比真实数组中的还要小的时候，返回null
        if (indexX < 0 || indexY < 0) {
            return null;
        }
        if (indexX >= this.gameConf.getxSize() || indexY >= this.gameConf.getySize()) {
            return null;
        }
        //当所触碰的位置存在卡片的时候，返回卡片的位置
        return pieces[indexX][indexY];
    }

    /*处理两个卡片之间的连线问题
    * 参数为Piece对象，真的在自己设置算法的时候要考虑参数的传递类型*/
    public LinkInfo link(Piece p1, Piece p2) {
        //当两个Piece是同一个，即选择了同一个卡片，返回null
        if (p1.equals(p2)) {
            return null;
        }
        //当两个Piece的图片不相同，则返回null
        if (p1.getPieceImage().equals(p2.getPieceImage())) {
            return null;
        }
        //当p1位于p2的右侧的时候，交换p1,p2的位置，
        // 此时通过BeginX与IndexX得到的应该都是性质一样的，结果不一样的IndexX是行号，
        if (p1.getBeginX() > p2.getBeginX()) {
            return link(p2, p1);
        }
        //获取p1,p2的中心点位置
        Point p1center = p1.getCenter();
        Point p2center = p2.getCenter();
        //在两个Piece满足图片相同时，判断其之间是否有通路
        //(1)当p1,p2之间没有拐点的时候
        //(1.1)若p1,p2处于同一行上,此时又是在判断是否处于同一行是，此时应该是通过通过IndexX是否相同啊？？？
        if (p1.getIndexY() == p2.getIndexY()) {
            if (!isXBlock(p1center, p2center, gameConf.PIECE_WIDTH)) {
                return new LinkInfo(p1center, p2center);
            }
        }
        //(1.2)若p1,p2在同一列上，
        if (p1.getIndexX() == p2.getIndexX()) {
            if (isYBlock(p1center, p2center, gameConf.PIECE_HEIGHT)) {
                return new LinkInfo(p1center, p2center);
            }
        }
        //(2)p1,p2之间有一个拐点的时候
        Point conrerPoint = getCornerPoint(p1center, p2center, gameConf.PIECE_WIDTH, gameConf.PIECE_HEIGHT);
        if (conrerPoint != null) {
            return new LinkInfo(p1center, conrerPoint, p2center);
        }
        //(3)p1,p2之间有两个拐点的时候


        return null;
    }

    /**
     * 当它们之间有两个拐点的时候，
     *
     * @param p1
     * @param p2
     * @return 返回的结果集为Map对应的每个key-value对应一种连接方式
     * 其中的key,value分别代表第一个，第二个拐点
     * Map的size说明有几种连接方式
     */
    private Map<Point, Point> getLinkPoints(Point p1, Point p2, int pieceWidth, int pieceHeight) {
        Map<Point, Point> result = new HashMap<Point, Point>();
        //获取以p1为中心点，向上、向下、向右的通道
        List<Point> upChanel1 = getUpChanel(p1, p2.y, pieceHeight);
        List<Point> downChanel1 = getDownChanel(p1, p2.y, pieceHeight);
        List<Point> rightChanel1 = getRightChanel(p1, p2.x, pieceWidth);
        //获取以p2位中心点，向上、向下、向左的通道
        List<Point> upChanel2 = getUpChanel(p2, p1.y, pieceHeight);
        List<Point> downChanel2 = getDownChanel(p2, p1.y, pieceHeight);
        List<Point> leftChanel2 = getLeftChanel(p2, p1.x, pieceWidth);
        //获得board的最大高度
        int boardHeight = gameConf.getBeginImageY() + (gameConf.getySize() + 1) * pieceHeight;
        //获得board的最大宽度
        int boardWidth = gameConf.getBeginImageX() + (gameConf.getxSize() + 1) * pieceWidth;
        //确定p1,p2之间的位置关系，当p2位于p1的左上角或者左下角的时候，调换p1,p2的位置重新执行该函数
        if (isLeftUp(p1, p2) || isLeftDown(p1, p2)) {
            //调换顺序，重新调用此方法
            return getLinkPoints(p2, p1, pieceWidth, pieceHeight);
        }
        //当p1,p2位于同一行的情况下，(此时有两种情况)
        if (p1.y == p2.y) {
            //向上遍历
            //以p1为中心点向上遍历获取所有的点集
            upChanel1 = getUpChanel(p1, 0, pieceHeight);
            //以p2为中心点向上遍历获取所有的点集
            upChanel2 = getUpChanel(p2, 0, pieceHeight);
            //获得其向上连接时，所有的可连接点
            Map<Point, Point> xUpLinkPoints = getXLinkPoints(upChanel1, upChanel2, pieceWidth);

            //向下遍历
            //以p1为中心向下遍历获取所有的点集 ,不得超过Board的高度
            downChanel1 = getDownChanel(p1, boardHeight, pieceHeight);
            //以p2为中心向下遍历获取所有的点集
            downChanel2 = getDownChanel(p2, boardHeight, pieceHeight);
            Map<Point, Point> xDownLinkPoints = getXLinkPoints(downChanel1, downChanel2, pieceWidth);

            result.putAll(xUpLinkPoints);
            result.putAll(xDownLinkPoints);

        }
        //当p1,p2位于同一列的情况下，(此时有两种情况)
        if(p1.x==p2.x){
            //同时向左遍历
            //以p1为中心向左遍历的所有通道
           List<Point> leftChanel1 = getLeftChanel(p1, 0, pieceWidth);
            //以p2为中心向左遍历的所有通道
             leftChanel2 = getLeftChanel(p2, 0, pieceWidth);

            //同时向右遍历
            //以p1为中心向右遍历的所有通道
            rightChanel1 = getRightChanel(p1, boardWidth, pieceWidth);
            List<Point> rightChanel2 = getRightChanel(p2, boardWidth, pieceWidth);

            Map<Point, Point> yLeftLinkPoints = getYLinkPoints(leftChanel1, leftChanel2, pieceHeight);
            Map<Point, Point> yRightLinkPoints = getYLinkPoints(rightChanel1, rightChanel2, pieceHeight);
            result.putAll(yLeftLinkPoints);
            result.putAll(yRightLinkPoints);

        }
        //p2位于p1的右下角的时候
        if(isRightDown(p1, p2)){
            //获取p1向下遍历，p2向上遍历，横向连接的集合
            //获取p1向下遍历，p2向下遍历，横向相连的集合
            //获取p1向右遍历，p2向左遍历，纵向连接的集合
            //获取p1向右遍历，p2向右遍历，纵向相连的集合
            //获取p1向上遍历，p2向上遍历，横向连接的集合
            //获取p1向左遍历,p2向左遍历, 纵向相连的集合

        }
        //p2位于p1的右上角的时候
        if(isRightUp(p1, p2)){

        }

        return result;
    }

    /**
     * 遍历两个集合，首先判断其中是否有位于同一行的元素
     * 如果在同一行的话，再进行判断其中间是否有障碍点，没有的话，将这两个点添加至Map中
     */
    private Map<Point, Point> getXLinkPoints(List<Point> p1Chanel, List<Point> p2Chanel, int pieceWidth) {
        Map<Point, Point> result = new HashMap<Point, Point>();
        //遍历p1Chanel中的所有元素
        for (int i = 0; i < p1Chanel.size(); i++) {
            //将p1Chanel中的此位置元素拿出
            Point temp0 = p1Chanel.get(i);
            //遍历p2Chanel中的所有元素
            for (int m = 0; m < p2Chanel.size(); m++) {
                //将p2Chanel此位置的元素
                Point temp1 = p2Chanel.get(m);
                //判断这两个点是否位于同一行
                if (temp1.y == temp1.y) {
                    //如果这两个位置之间没有障碍物的话，将其添加至Map中
                    if (!isXBlock(temp0, temp1, pieceWidth))
                        result.put(temp0, temp1);
                }
            }
        }
        return result;
    }
       	/**
       	 * 遍历两个集合, 先判断第一个集合的元素的x座标与另一个集合中的元素x座标相同(纵向),
       	 * 如果相同, 即在同一列, 再判断是否有障碍, 没有则加到结果的Map中去
       	 *
       	 * @param p1Chanel
       	 * @param p2Chanel
       	 * @param pieceHeight
       	 * @return
       	 */
    private Map<Point,Point> getYLinkPoints(List<Point> p1Chanel,List<Point> p2Chanel,int pieceHeight){
           Map<Point,Point> result =new HashMap<Point,Point>();
        for(int i=0;i<p1Chanel.size();i++){
            Point temp1=p1Chanel.get(i);
            for (int j=0;j<p2Chanel.size();j++){
                     Point temp2=p2Chanel.get(j);
                //判断两个点是否在同一列
                if(temp1.x==temp2.x) {
                    //如果在竖直方向上两个位置之间没有障碍物的话，将其添加至Map中
                    if (!isYBlock(temp1, temp2, pieceHeight)) {
                        result.put(temp1, temp2);
                    }
                }
            }
        }
          return result;
    }


    /*当两个卡片之间有一个拐点的时候，找到这个拐点，*/
    public Point getCornerPoint(Point p1, Point p2, int pieceWidth, int pieceHeight) {
        //首先判断p1,p2的相对位置,若p2位于p1的左侧时，调换相互位置
        if (isLeftUp(p1, p2) || isLeftDown(p1, p2)) {
            return getCornerPoint(p2, p1, pieceWidth, pieceHeight);
        }
        //当p2位于p1的右侧时，
        //p1向上，向下，向右的所有通道分别为
        List<Point> p1UpChanel = getUpChanel(p1, p2.y, pieceHeight);
        List<Point> p1DownChanel = getDownChanel(p1, p2.y, pieceHeight);
        List<Point> p1RightChanel = getRightChanel(p1, p2.x, pieceWidth);
        //p2向上，向下，向左的所有通道分别为
        List<Point> p2LeftChanel = getLeftChanel(p2, p1.x, pieceWidth);
        List<Point> p2UpChanel = getUpChanel(p2, p1.y, pieceHeight);
        List<Point> p2DownChanel = getDownChanel(p2, p1.y, pieceHeight);
        //当p2位于p1的右上方时
        if (isRightUp(p1, p2)) {
            //p1上面，p2左边的通道
            Point linkPoint1 = getWrapPoint(p1UpChanel, p2LeftChanel);
            //p1右边，p2下面的通道
            Point linkPoint2 = getWrapPoint(p1RightChanel, p2DownChanel);
            //如果linkPoint1为空的话，返回linkPoint2
            return (linkPoint1 == null) ? linkPoint2 : linkPoint1;
        }
        //当p2位于p1的右下方时
        if (isRightDown(p1, p2)) {
            //p1右边，p2上面的通道
            Point linkPoint1 = getWrapPoint(p1RightChanel, p2UpChanel);
            //p1下面，p2左边的通道
            Point linkPoint2 = getWrapPoint(p1DownChanel, p2LeftChanel);
            return (linkPoint1 == null) ? linkPoint2 : linkPoint1;
        }
        return null;
    }

    /**
     * @param size 为每一个卡片的宽或高
     */
    public int getIndex(int size, int relative) {
        int index = -1;
        //根据其相对比值判断相应的位置
        if (relative % size != 0) {
            index = relative / size;
        } else {
            index = relative / size - 1;
        }
        return index;
    }

    /*p1和p2在同一行中，判断其是否为直接相连(其中间没有其他的卡片)，若有障碍物，则返回true,否则的话false*/
    public boolean isXBlock(Point p1, Point p2, int pieceWidth) {
        //当p2位于p1的左边的时候，将其交换位置
        if (p1.x > p2.x) {
            return isXBlock(p2, p1, pieceWidth);
        }
        for (int m = p1.x + pieceWidth; m < p2.x; m = m + pieceWidth) {
            if (hasPiece(m, p1.y)) {
                //中间有障碍，返回真
                return true;
            }
        }
        return false;
    }

    /*p1和p2在同一列，判断其是否可以直接相连，若有障碍物，则返回true，否则返回false
    * 其默认的是p1在上，p2在下面，（所谓的默认是因为此方法中有的东西顺序的问题）*/
    public boolean isYBlock(Point p1, Point p2, int pieceHeight) {
        //当p2在p1上面,将其交换位置
        for (int m = p1.y + pieceHeight; m < p2.y; m = m + pieceHeight) {
            if (hasPiece(p1.x, m)) {
                //当中间有障碍物的时候返回true
                return true;
            }
        }
        return false;
    }

    //通过一个点来判断此点对应的位置是否对应有卡片
    public boolean hasPiece(int x, int y) {
        //当有piece的时候返回true
        if (findPiece(x, y) != null)
            return true;
        return false;
    }

    //wrap缠绕，即获取两个通道之间的交点
    private Point getWrapPoint(List<Point> p1Chanel, List<Point> p2Chanel) {
        //将p1Chanel与p2Chanel之间的数据一一比较，在遍历p1Chanel的过程中，将与p2Chanel做比较
        for (int i = 0; i < p1Chanel.size(); i++) {
            Point temp1Point = p1Chanel.get(i);
            for (int j = 0; j < p2Chanel.size(); j++) {
                Point temp2Point = p2Chanel.get(j);
                //如果两个List有同样的元素的话，就返回交点
                if (temp1Point.equals(temp2Point))
                    return temp1Point;
            }
        }
        return null;
    }

    /*判断p2是否在p1的左上角，
    * @param p1
    * @param P2
    * @return 若是p2位于p1的左上角，返回true。否则的话，false*/
    private boolean isLeftUp(Point p1, Point p2) {
        return (p2.x < p1.x && p2.y < p1.y);
    }

    /*判断p2是否在p1的左下角，
    * @param p1
    * @param P2
    * @return 若是p2位于p1的左下角，返回true。否则的话，false*/
    private boolean isLeftDown(Point p1, Point p2) {
        return (p2.x < p1.x && p2.y > p1.y);
    }

    /*判断p2是否在p1的右上角，
   * @param p1
   * @param P2
   * @return 若是p2位于p1的右上角，返回true。否则的话，false*/
    private boolean isRightUp(Point p1, Point p2) {
        return (p2.x > p1.x && p2.y < p1.y);
    }

    /*判断p2是否在p1的右下角，
   * @param p1
   * @param P2
   * @return 若是p2位于p1的右下角，返回true。否则的话，false*/
    private boolean isRightDown(Point p1, Point p2) {
        return (p2.x > p1.x && p2.y > p1.y);
    }

    /*给定一个Point,返回其左边的所有通道
    * @param p
    * @param min,向左遍历时最小得界限，
    * @param pieceWidth 卡片的宽度*/
    private List<Point> getLeftChanel(Point p, int min, int pieceWidth) {
        List<Point> allLeftChanel = new ArrayList<Point>();
        int pointY = p.y;
        for (int m = p.x - pieceWidth; m >= min; m = m - pieceWidth) {
            //如果有卡片的话，则遇到障碍物，返回结果集
            if (hasPiece(m, pointY)) {
                return allLeftChanel;
            }
            //否则的话，将其添加到allLeftChanel中
            allLeftChanel.add(new Point(m, pointY));
        }
        return allLeftChanel;
    }

    /*给定一个Point,返回其右边的所有通道
 * @param p
 * @param max,向右遍历时最大得界限，
 * @param pieceWidth 卡片的宽度*/
    private List<Point> getRightChanel(Point p, int max, int pieceWidth) {
        List<Point> allRightChanel = new ArrayList<Point>();
        for (int m = p.x + pieceWidth; m <= max; m = m + pieceWidth) {
            //如果在向右的方向上有障碍物时，返回通道allRightChanel集合
            if (hasPiece(m, p.y)) {
                return allRightChanel;
            }
            //否则，此点为通道上元素，添加至allRightChanel集合
            allRightChanel.add(new Point(m, p.y));
        }
        return allRightChanel;
    }

    /*给定一个Point,返回其向上的所有通道
        * @param p
        * @param min,向上遍历时最小得界限，
        * @param pieceWidth 卡片的高度*/
    private List<Point> getUpChanel(Point p, int min, int pieceHeight) {
        List<Point> allUpChanel = new ArrayList<Point>();
        for (int m = p.y - pieceHeight; m >= min; m = m - pieceHeight) {
            //判断其向上的方向中是否有障碍物，
            if (hasPiece(p.x, m)) {
                //如果遇到障碍物，直接返回
                return allUpChanel;
            }
            //当其中有通道时，将其添加至集合中
            allUpChanel.add(new Point(p.x, m));
        }
        return allUpChanel;
    }

    /*给定一个Point,返回其向下的所有通道
       * @param p
       * @param min,向下遍历时最大得界限，
       * @param pieceWidth 卡片的高度*/
    private List<Point> getDownChanel(Point p, int max, int pieceHeight) {
        List<Point> allDownChanel = new ArrayList<Point>();
        for (int m = p.y + pieceHeight; m <= max; m = m + pieceHeight) {
            if (hasPiece(p.x, m)) {
                return allDownChanel;
            }
            allDownChanel.add(new Point(p.x, m));
        }
        return allDownChanel;
    }
}

