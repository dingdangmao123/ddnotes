package com.dingdangmao.wetouch;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.widget.TextView;

import butterknife.BindView;

public class About extends Base {

    @BindView(R.id.about)
    TextView about;

    @Override
    public int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    public void init(Bundle savedInstanceState) {

        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setDisplayShowTitleEnabled(false);
        }

        Util.toolbar(this);


        String str= "叮当记账是一个功能简洁的记账App,只提供最核心的功能，满足你的记账需求。界面清爽，避免花哨的界面，操作简单方便";

        about.setText(str);

    }
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() ==android.R.id.home )
        {
            finish();
        }
        return true;
    }
}
