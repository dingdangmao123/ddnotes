package com.dingdangmao.wetouch;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import org.sufficientlysecure.htmltextview.HtmlResImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar bar = getSupportActionBar();

        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
            // bar.setHomeAsUpIndicator(R.drawable.menu);
            bar.setDisplayShowTitleEnabled(false);
        }

        Util.toolbar(this);
        HtmlTextView htmlTextView = (HtmlTextView)findViewById(R.id.html_text);
        String html="<h3>作者寄语</h3><p>" +
                "" +"叮当记账是一个功能简洁的记账App,只提供最核心的功能，满足你的记账需求。界面清爽，避免花哨的界面，操作简单方便</p>"
                +"<p>纯粹是为了个人兴趣,做出东西和大家分享</p>"
                +"<a href=\"http://weibo.com/3178315752/profile\"><font color=\"blue\">新浪微博</font></a> 欢迎联系 </p>";
        htmlTextView.setHtml(html,
                new HtmlResImageGetter(htmlTextView));

    }
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() ==android.R.id.home )
        {
            finish();
        }
        return true;
    }
}
