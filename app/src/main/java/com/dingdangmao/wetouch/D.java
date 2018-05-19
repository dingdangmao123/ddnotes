package com.dingdangmao.wetouch;

import android.content.Context;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by suxiaohui on 2017/5/16.
 */

public class D {
    public static void show(Context context,String content){
        new SweetAlertDialog(context)
                .setContentText(content)
                .show();
    }
    public static void tip(Context context,String title){
        new SweetAlertDialog(context)
                .setTitleText(title)
                .show();
    }
    public static void success(Context context,String content) {
        new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                .setContentText(content)
                .show();
    }
}
