package com.example.hottest.cutephonelink.object;

/**
 * Created by hottest on 2016/3/13.
 */

import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;

/**处理方块之间的连接
 * （1）方块之间有2个连接点（这两个连接点就是这两个要连接的方块），即两个方块之间是通过一条线段相连的，
 * （2）方块之间有3个连接点，即两个方块之间是通过两条线段相连的，中间有一个拐点
 * （3）方块之间有4个连接点，即两个方块之间是通过三条线段相连的，中间有两个拐点*/
public class LinkInfo {
    //创建一个集合用于保存连接点信息，其中Point代表一个点（android.graphics.Point），封装了其X,Y坐标
    private List<Point> points=new ArrayList<Point>();
    //表示两个方块之间可以相连，中间没有转折点
    public LinkInfo(Point point1,Point point2){
        points.add(point1);
        points.add(point2);
    }
    //表示三个方块之间可以相连，point2是point1和point3之间的转折点
    public LinkInfo(Point point1,Point point2,Point point3){
        points.add(point1);
        points.add(point2);
        points.add(point3);
    }
    //表示四个方块之间可以相连，point2和point3是point1和point4之间的转折点
    public LinkInfo(Point point1,Point point2,Point point3,Point point4){
        points.add(point1);
        points.add(point2);
    }
    //返回连接集合
    public List<Point> getLinkPoints(){
        return points;
    }
}
