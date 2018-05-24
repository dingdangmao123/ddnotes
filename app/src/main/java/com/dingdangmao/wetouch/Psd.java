package com.dingdangmao.wetouch;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by gapcoder on 2018/5/24.
 */

public class Psd {
    public static void set(Context context,String psd){
        SharedPreferences.Editor editor = context.getSharedPreferences("Token", MODE_PRIVATE).edit();
        editor.putString("psd", psd);
        editor.apply();

    }
    public static String get(Context context){
        SharedPreferences p = context.getSharedPreferences("Token", MODE_PRIVATE);
        return p.getString("psd", "");
    }
}
