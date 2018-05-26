package com.dingdangmao.wetouch;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.LayoutInflaterFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import butterknife.ButterKnife;

/**
 * Created by gapcoder on 2018/5/19.
 */

abstract public class Base extends AppCompatActivity {

    public final String TAG="tag";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        TypeFace();
        super.onCreate(savedInstanceState);
        beforeView();
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        initToolbar();
        init(savedInstanceState);

    }

    private void TypeFace(){

        final Typeface typeface = Typeface.createFromAsset(getAssets(), "fz.TTF");

        LayoutInflaterCompat.setFactory(LayoutInflater.from(this), new LayoutInflaterFactory()
        {
            @Override
            public View onCreateView(View parent, String name, Context context, AttributeSet attrs)
            {
                AppCompatDelegate delegate = getDelegate();
                View view = delegate.createView(parent, name, context, attrs);

                if ( view!= null && (view instanceof TextView))
                {
                    ((TextView) view).setTypeface(typeface);
                }
                return view;
            }
        });
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
