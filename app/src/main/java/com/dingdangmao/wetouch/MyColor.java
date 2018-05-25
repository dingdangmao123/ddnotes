package com.dingdangmao.wetouch;

import android.graphics.Color;

import java.util.Random;

/**
 * Created by suxiaohui on 2017/5/19.
 */

public class MyColor {
    private static Random r;
    public static final  int[] data=new int[]{R.drawable.bg1,R.drawable.bg2,R.drawable.bg3,
            R.drawable.bg4,R.drawable.bg5,R.drawable.bg6,R.drawable.bg7,R.drawable.bg8,
            R.drawable.bg9,R.drawable.bg10
    };

    public static final  int[] data2=new int[]{
            Color.parseColor("#66808080"),
            Color.parseColor("#6675878a"),
            Color.parseColor("#669E9E9E"),
            Color.parseColor("#66673AB7"),
            Color.parseColor("#66425066"),
            Color.parseColor("#66009688"),
            Color.parseColor("#6600BCD4"),
            Color.parseColor("#66003371"),
    };

    public static int get(){
        if(r==null)
            r=new Random();
        return MyColor.data[r.nextInt(MyColor.data.length)];
    }
}
