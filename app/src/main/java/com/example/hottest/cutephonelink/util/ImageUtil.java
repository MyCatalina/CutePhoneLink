package com.example.hottest.cutephonelink.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.hottest.cutephonelink.R;
import com.example.hottest.cutephonelink.view.PieceImage;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by hottest on 2016/3/13.
 */
public class ImageUtil {
    //保存所有连连看图片资源ID(均为整形)
    private static List<Integer>  imageValues=getImageValues();

    //获取所有图片资源的ID
    public static List<Integer> getImageValues(){
      try {
           //得到R.drawable下所有图片资源,R.drawable为一个类，通过类的反射拿到类中所有的成员变量
            Field[] drawablefields = R.drawable.class.getFields();
                  List<Integer> resourceValues=new ArrayList<Integer>();
               for (Field field:drawablefields) {
                        if(field.getName().indexOf("p_")!=-1){
                    resourceValues.add(field.getInt(R.drawable.class));
                }
            }
          return resourceValues;
            } catch (IllegalAccessException e) {
               return null;
      }
    }
  /**从R.drawable中随机选取size张图片，返回的结果为资源ID
   * @param sourcevalues 从此集合中获取需要的图片
   * @param size 所需要获取图片的个数
   * @return result 返回结果为所需要资源的集合*/
    public static List<Integer> getRandomValue(List<Integer> sourcevalues,int size){
        Random random=new Random();
        List<Integer> result=new ArrayList<Integer>();
        for (int i=0;i<size;i++) {
            try {
                int index = random.nextInt(sourcevalues.size());
                Integer image = sourcevalues.get(index);
                result.add(image);
            }catch(IndexOutOfBoundsException e){
                return result;
            }
        }
        return result;
    }
/**从R.drawable中获取size个图片资源ID
 * @param size 为需要选取的图片个数
 * return playImageValues 返回的为size个图片资源ID集合，即显示在UI界面上的*/
   public static List<Integer> getPlayValues(int size){
       if(size%2!=0){
           size+=1;
       }
       List<Integer> playImageValues=getRandomValue(imageValues,size/2);
       //将所有的图片资源复制
       playImageValues.addAll(playImageValues);
       //将所有的图片资源洗牌
       Collections.shuffle(playImageValues);
       return playImageValues;
   }
/**将所有需要显示的资源封装为PieceImage
 * @param context
 * @param  size UI界面显示的所有图片个数 */
 public static List<PieceImage> getPlayImages(Context context,int size){
     List<Integer> imageId=getPlayValues(size);
     List<PieceImage> result=new ArrayList<PieceImage>();
     for (Integer value:imageId) {
         //加载图片
         Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), value);
         //将图片封装在PieceImage对象中
         PieceImage pieceImage=new PieceImage(bitmap,value);
         result.add(pieceImage);
     }
     return result;
 }
    //获得选中标识的图片
    public static Bitmap getSelectedImage(Context context){
         Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.selected);
        return bitmap;
    }

}
