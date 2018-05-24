package com.dingdangmao.wetouch;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class start extends Base {


    Mh ins=new Mh();

    @Override
    public void init(@Nullable Bundle savedInstanceState) {
        super.init(savedInstanceState);

        ins.postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent i=new Intent(start.this,login.class);
                start.this.startActivity(i);
                finish();

            }
        },3000);
    }

    @Override
    public int getLayoutId() {

        return R.layout.activity_start;

    }

    @Override
    public void beforeView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    static class Mh extends Handler{
        @Override
        public void handleMessage(Message msg) {

        }
    }

}
