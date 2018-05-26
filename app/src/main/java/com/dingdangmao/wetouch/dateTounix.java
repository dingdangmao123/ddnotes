package com.dingdangmao.wetouch;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by suxiaohui on 2017/5/12.
 */

public class dateTounix {

    public static int To(int year,int month,int day){
        SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyy-MM-dd");
        try {

            Date date = simpleDateFormat.parse(String.valueOf(year) + "-" + String.valueOf(month) + "-" + String.valueOf(day));
            int r=(int)(date.getTime()/1000);
            Log.i("date",r+" ");
            return r;
        }catch(Exception e){

        }
        return 0;
    }

}
