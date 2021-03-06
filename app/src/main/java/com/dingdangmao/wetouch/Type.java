package com.dingdangmao.wetouch;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dingdangmao.wetouch.Event.Tag;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import butterknife.BindView;
import cn.pedant.SweetAlert.SweetAlertDialog;
import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;
import me.gujun.android.taggroup.TagGroup;

public class Type extends Base {


    private db mydb = new db(this, "mydb.db", null, 2);
    private HashSet<String> mytag = new HashSet<String>();
    Typeface typeface = null;

    @BindView(R.id.ok)
    public Button ok;

    @BindView(R.id.newtag)
    public EditText ed;

    @BindView(R.id.ttag)
    public TagContainerLayout mTag;


    boolean flag=false;

    @Override
    public int getLayoutId() {
        return R.layout.activity_type;
    }

    @Override
    public void init(@Nullable Bundle savedInstanceState) {

        super.init(savedInstanceState);

        ActionBar bar = getSupportActionBar();

        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setDisplayShowTitleEnabled(false);
        }

        Util.toolbar(this);
        flag=getIntent().getBooleanExtra("flag",false);
        typeface=Typeface.createFromAsset(getAssets(), "fz.TTF");

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String t = ed.getText().toString().trim();
                if (t.length() == 0) {
                    D.tip(Type.this, "标签不能为空！");
                } else if (t.length() > 10) {
                    D.tip(Type.this, "标签长度不能超过10！");
                } else if (mytag.contains(t)) {
                    D.tip(Type.this, "标签已存在");

                } else {

                    Pool.run(new Runnable() {
                        @Override
                        public void run() {
                            SQLiteDatabase read = mydb.getWritableDatabase();
                            try {

                                read.execSQL("insert into tag(type)values(?)", new String[]{t});

                            } catch (Exception e) {

                                D.tip(Type.this, e.toString());
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mTag.addTag(t);
                                    mytag.add(t);
                                    ed.setText("");
                                    TagView tg=(TagView)mTag.getChildAt(mTag.getChildCount()-1);
                                    tg.setTypeface(typeface);

                                }
                            });
                        }
                    });

                    if(flag) {

                        //Intent i = new Intent("com.dingdangmao.wetouch.TAG");
                        //sendBroadcast(i);
                        EventBus.getDefault().post(new Tag());
                    }
                }

            }
        });

        mTag.setTheme(0);
        mTag.setTagBackgroundColor(Color.TRANSPARENT);
        //mTag.setTagTypeface(typeface);貌似有bug,只能手动设置
        mTag.setOnTagClickListener(new TagView.OnTagClickListener() {

            @Override
            public void onTagClick(int position, String text) {

            }

            @Override
            public void onTagLongClick(final int position, String text) {
                final String s = text;
                new SweetAlertDialog(Type.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText(text)
                        .setContentText("删除后不可恢复！")
                        .setCancelText("取消")
                        .setConfirmText("删除")
                        .showCancelButton(true)
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.cancel();
                            }
                        }).setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        Pool.run(new Runnable() {
                            @Override
                            public void run() {
                                delete(position, s);
                            }
                        });

                    }
                }).show();
            }

            @Override
            public void onTagCrossClick(int position) {

            }
        });

    }

    public void onResume() {
        super.onResume();
        Pool.run(new Runnable() {
            @Override
            public void run() {
                Refresh();
            }
        });
    }

    private void Refresh() {
        mytag.clear();
        SQLiteDatabase read = mydb.getWritableDatabase();
        Cursor cursor = read.query("tag", null, null, null, null, null, "id asc");

        if (cursor.moveToFirst()) {
            do {

                String tag = cursor.getString(cursor.getColumnIndex("type"));
                mytag.add(tag);

            } while (cursor.moveToNext());
        }
        cursor.close();

        List<String> taglist = new ArrayList<String>();
        taglist.addAll(mytag);
        mTag.setTags(taglist);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                for(int i=0;i<mTag.getChildCount();i++)
                {
                    TagView tv=(TagView)mTag.getChildAt(i);
                    tv.setTypeface(typeface);
                    Log.i("typeface",i+"");
                }
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

    private boolean delete(final int position, final String tag) {

        SQLiteDatabase write = mydb.getWritableDatabase();
        try {
            write.execSQL("delete  from tag where type=?", new String[]{tag});
        } catch (Exception e) {
            D.tip(this, e.toString());
            return false;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                D.tip(Type.this, "删除成功！");
                mTag.removeTag(position);
                mytag.remove(tag);
            }
        });
        return true;
    }
}
