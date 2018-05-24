package com.dingdangmao.wetouch;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import butterknife.ButterKnife;

/**
 * Created by gapcoder on 2018/5/19.
 */

abstract public class Base extends AppCompatActivity {

    public final String TAG="tag";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        beforeView();
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        initToolbar();
        init(savedInstanceState);

    }
    public void beforeView(){

    }

    public void init(@Nullable Bundle savedInstanceState){

    }

    public void initToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    abstract public int getLayoutId();


}
