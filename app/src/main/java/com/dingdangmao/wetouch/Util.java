package com.dingdangmao.wetouch;

import android.app.Activity;
import android.support.v4.content.ContextCompat;

import com.jaeger.library.StatusBarUtil;

/**
 * Created by suxiaohui on 2017/5/21.
 */

public class Util {
    public static void toolbar(Activity context){
        StatusBarUtil.setColor(context, ContextCompat.getColor(context,R.color.colorPrimary));
       // StatusBarUtil.setTransparent(context);
       // StatusBarUtil.setTranslucent(context,255);

    }
}
