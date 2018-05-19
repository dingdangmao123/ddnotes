package com.dingdangmao.wetouch;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import butterknife.ButterKnife;

/**
 * Created by gapcoder on 2018/5/19.
 */

abstract public class Base extends AppCompatActivity {



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Log.i("tag","beforeinit");
        init(savedInstanceState);

    }

    public void init(@Nullable Bundle savedInstanceState){

    }

    abstract public int getLayoutId();
}
