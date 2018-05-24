

package com.dingdangmao.wetouch;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;

import butterknife.BindView;
import jp.wasabeef.blurry.Blurry;

public class Reset extends Base {

    String psd="";

    String tmp="";

    int cur=1;


    @BindView(R.id.iv)
    ImageView iv;

    @BindView(R.id.tip)
    public TextView tip;

    @BindView(R.id.pin)
    public PinLockView pin;

    @BindView(R.id.dot)
    public IndicatorDots dot;

    @Override
    public int getLayoutId() {

        return R.layout.activity_reset;
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

        Blurry.with(this)
                .radius(20)
                .from(BitmapFactory.decodeResource(getResources(), R.drawable.bg)).into(iv);

        pin.setPinLockListener(pinl);
        pin.attachIndicatorDots(dot);
        psd=Psd.get(this);
    }

    @Override
    public void initToolbar() {

    }


    private PinLockListener pinl = new PinLockListener() {
        @Override
        public void onComplete(String p) {

            if(cur==1){

                if(psd==""){
                    cur=2;
                    return ;
                }

                if(!psd.equals(p)){
                    T.show2(Reset.this,"原密码错误！");
                    pin.resetPinLockView();
                    return ;
                }
                cur=2;
                tip.setText("输入新密码");
                pin.resetPinLockView();

                return ;
            }

            if(cur==2){
                tmp=p;
                cur=3;
                tip.setText("再次输入");
                pin.resetPinLockView();
                return ;
            }

            if(!tmp.equals(p)){
                T.show2(Reset.this,"两次密码不一致！");
                tip.setText("输入新密码");
                pin.resetPinLockView();
                cur=2;
                return ;
            }

            Psd.set(Reset.this,p);
            T.show2(getApplicationContext(),"密码设置成功");
            finish();
        }

        @Override
        public void onEmpty() {

        }

        @Override
        public void onPinChange(int pinLength, String intermediatePin) {

        }
    };

}
