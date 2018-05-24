package com.dingdangmao.wetouch;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;

import butterknife.BindView;

public class login extends Base {


    String psd="";

    @BindView(R.id.pin)
    public PinLockView pin;

    @BindView(R.id.dot)
    public IndicatorDots dot;

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }


    @Override
    public void beforeView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public void init(@Nullable Bundle savedInstanceState) {

        super.init(savedInstanceState);


        pin.setPinLockListener(pinl);
        pin.attachIndicatorDots(dot);

        psd=Psd.get(this);


    }

    @Override
    public void initToolbar() {

    }

    public void jump(){
        Intent i=new Intent(this,Main.class);
        login.this.startActivity(i);
    }

    private PinLockListener pinl = new PinLockListener() {
        @Override
        public void onComplete(String p) {

                if(psd!=""&&!psd.equals(p)){

                   T.show2(login.this,"密码错误!");

                   pin.resetPinLockView();

                }else{

                  jump();

                }

        }

        @Override
        public void onEmpty() {

        }

        @Override
        public void onPinChange(int pinLength, String intermediatePin) {

        }
    };

}
