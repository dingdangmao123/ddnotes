package com.dingdangmao.wetouch;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.aigestudio.wheelpicker.WheelPicker;
import com.bigkoo.pickerview.TimePickerView;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;
import me.gujun.android.taggroup.TagGroup;

public class Add extends Base {

    @BindView(R.id.ok)
    public Button ok;

    @BindView(R.id.date)
    public Button date;

    @BindView(R.id.tip_date)
    public Button tip_date;

    @BindView(R.id.tip_type)
    public Button tip_type;


    @BindView(R.id.money)
    public TextView money;

    @BindView(R.id.tip)
    public TextView tip;


    @BindView(R.id.ttag)
    public TagContainerLayout mTag;


    private db mydb = new db(this, "mydb.db", null, 3);
    private HashMap<String, Integer> type = new HashMap<String, Integer>();
    private int cur = 0;
    private String timestr;

    private WheelPicker wheel;


    private MessageReceiver receiver;
    private IntentFilter filter;
    private boolean refresh = true;

    @Override
    public int getLayoutId() {
        return R.layout.activity_add;
    }


    @Override
    public void init(@Nullable Bundle savedInstanceState) {


        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setDisplayShowTitleEnabled(false);
        }

        Util.toolbar(this);

        if (savedInstanceState != null) {
            cur = savedInstanceState.getInt("cur");
            timestr = savedInstanceState.getString("timestr");
            tip.setText(savedInstanceState.getString("tip"));
            money.setText(savedInstanceState.getString("money"));
        }

        Calendar now = Calendar.getInstance();
        timestr = String.valueOf(now.get(Calendar.YEAR)) + "-" + String.valueOf(now.get(Calendar.MONTH) + 1) + "-" + String.valueOf(now.get(Calendar.DAY_OF_MONTH));
        tip_date.setText(timestr);

        tip_date.setBackgroundResource(MyColor.get());
        tip_type.setBackgroundResource(MyColor.get());


        final Typeface typeface = Typeface.createFromAsset(getAssets(), "fz.TTF");
        mTag.setTheme(0);
        mTag.setTagBackgroundColor(Color.TRANSPARENT);
        mTag.setTagTypeface(typeface);
        mTag.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, String tag) {
                if (tag.equals("自定义")) {
                    Intent intent = new Intent(Add.this, Type.class);
                    intent.putExtra("flag", true);
                    startActivity(intent);
                } else {
                    cur = type.get(tag);
                    if (tip_type.getVisibility() == View.INVISIBLE)
                        tip_type.setVisibility(View.VISIBLE);
                    tip_type.setText(tag);
                }
            }

            @Override
            public void onTagLongClick(final int position, String text) {
            }

            @Override
            public void onTagCrossClick(int position) {
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase write = mydb.getWritableDatabase();
                String mstr = money.getText().toString();
                String tstr = tip.getText().toString();
                if (mstr.length() == 0) {
                    D.tip(Add.this, "金额不能为空");
                } else if (tstr.length() == 0) {
                    D.tip(Add.this, "请添加说明");
                } else if (timestr == null) {
                    D.tip(Add.this, "你还未选择时间");
                } else if (cur == 0) {
                    D.tip(Add.this, "你还未选择类别");
                } else {

                    String[] tmp = timestr.split("-");
                    SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyy-MM-dd");
                    Date date=null;
                    try {
                         date = simpleDateFormat.parse(tmp[0] + "-" + tmp[1] + "-" + tmp[2]);
                    }catch(Exception e){

                    }
                    int r=(int)(date.getTime()/1000);
                    Log.i("tag",r+"");
                    write.execSQL("insert into money(year,month,day,unix,total,tip,type)values(?,?,?,?,?,?,?)", new String[]{tmp[0], tmp[1], tmp[2], String.valueOf(r),mstr,
                            tstr, String.valueOf(cur)}
                    );

                    Intent i = new Intent("com.dingdangmao.wetouch.REFRESH");
                    sendBroadcast(i);
                    D.tip(Add.this, "添加成功");
                    money.setText("");
                    tip.setText("");
                }
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final boolean[] type = new boolean[]{true, true, true, false, false, false};
                TimePickerView pvTime = new TimePickerView.Builder(Add.this, new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {

                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                        timestr = formatter.format(date);
                        tip_date.setText(timestr);
                    }
                }).setType(type).setCancelText("取消").setSubmitText("确定")
                        .setSubmitColor(R.color.colorButton)
                        .setCancelColor(R.color.colorTip).build();
                pvTime.show();
            }
        });

        receiver = new MessageReceiver();
        filter = new IntentFilter();
        filter.addAction("com.dingdangmao.wetouch.TAG");
        registerReceiver(receiver, filter);

    }

    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return true;
    }

    public void onResume() {
        super.onResume();
        if (refresh) {
            refresh = false;
            Pool.run(new Runnable() {
                @Override
                public void run() {
                    Refresh();
                }
            });
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }


    public void Refresh() {
        SQLiteDatabase read = mydb.getWritableDatabase();
        Cursor cursor = read.query("tag", null, null, null, null, null, "id asc");
        type.clear();
        cur = 0;
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String tag = cursor.getString(cursor.getColumnIndex("type"));
                type.put(tag, id);
            } while (cursor.moveToNext());
        }
        cursor.close();
        Set<String> myset = type.keySet();

        final List<String> taglist = new ArrayList<String>();
        taglist.addAll(myset);
        taglist.add("自定义");

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTag.setTags(taglist);
            }
        });

    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("cur", cur);
        outState.putString("timestr", timestr);
        outState.putString("money", money.getText().toString());
        outState.putString("tip", tip.getText().toString());
    }

    class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            Log.i("tag", "refresh");
            refresh = true;

        }
    }
}
