package com.dingdangmao.wetouch;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;
import me.gujun.android.taggroup.TagGroup;

public class Add extends AppCompatActivity {
    private Button ok;
    private Button date;
    private Button tip_date;
    private Button tip_type;
    private db mydb = new db(this, "mydb.db", null, 3);
    private HashMap<String, Integer> type = new HashMap<String, Integer>();
    private int cur = 0;
    private String timestr;
    private TagGroup mTagGroup;
    private WheelPicker wheel;
    private TextView money;
    private TextView tip;
    private TagContainerLayout mTag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_add);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            ActionBar bar = getSupportActionBar();
            if (bar != null) {
                bar.setDisplayHomeAsUpEnabled(true);
                bar.setDisplayShowTitleEnabled(false);
            }

            Util.toolbar(this);

            money = (TextView) findViewById(R.id.money);
            tip = (TextView) findViewById(R.id.tip);
            tip_date = (Button) findViewById(R.id.tip_date);
            tip_type = (Button) findViewById(R.id.tip_type);

            if (savedInstanceState != null) {
                cur = savedInstanceState.getInt("cur");
                timestr = savedInstanceState.getString("timestr");
                tip.setText(savedInstanceState.getString("tip"));
                money.setText(savedInstanceState.getString("money"));
            }
            Calendar now = Calendar.getInstance();
            timestr=String.valueOf(now.get(Calendar.YEAR))+"-"+ String.valueOf(now.get(Calendar.MONTH) + 1)+"-"+String.valueOf(now.get(Calendar.DAY_OF_MONTH));
            tip_date.setText(timestr);

            tip_date.setBackgroundResource(MyColor.get());
            tip_type.setBackgroundResource(MyColor.get());

            mTag = (TagContainerLayout) findViewById(R.id.ttag);
            mTag.setTheme(0);
            mTag.setTagBackgroundColor(Color.TRANSPARENT);
            mTag.setOnTagClickListener(new TagView.OnTagClickListener() {
                @Override
                public void onTagClick(int position, String tag){
                    if (tag.equals("自定义")) {
                        Intent intent = new Intent(Add.this, Type.class);
                        startActivity(intent);
                    } else {
                        cur = type.get(tag);
                        if(tip_type.getVisibility()==View.INVISIBLE)
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

            ok = (Button) findViewById(R.id.ok);
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

                        write.execSQL("insert into money(year,month,day,total,tip,type)values(?,?,?,?,?,?)", new String[]{tmp[0], tmp[1], tmp[2], mstr,
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

            date = (Button) findViewById(R.id.date);
            date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final boolean[] type=new boolean[]{true,true,true,false,false,false};
                    TimePickerView pvTime = new TimePickerView.Builder(Add.this, new TimePickerView.OnTimeSelectListener() {
                        @Override
                        public void onTimeSelect(Date date, View v) {//选中事件回调

                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                            timestr= formatter.format(date);
                            tip_date.setText(timestr);
                        }
                    }).setType(type).setCancelText("取消").setSubmitText("确定")
                            .setSubmitColor(R.color.colorButton)//确定按钮文字颜色
                            .setCancelColor(R.color.colorTip).build();
                    pvTime.show();
                }
            });
        }catch(Exception e){
            D.tip(this,e.toString());
        }

    }

    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

    public void onResume(){
        super.onResume();
        SQLiteDatabase read = mydb.getWritableDatabase();
        Cursor cursor = read.query("tag", null, null, null, null, null, null);
        type.clear();
        cur = 0;
        if (cursor.moveToFirst()) {
          do {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String tag = cursor.getString(cursor.getColumnIndex("type"));
                type.put(tag, id);
            }  while (cursor.moveToNext());
        }
        cursor.close();
        Set<String> myset = type.keySet();

        List<String> taglist=new ArrayList<String>();
        taglist.addAll(myset);
        taglist.add("自定义");
        mTag.setTags(taglist);
    }

    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putInt("cur",cur);
        outState.putString("timestr",timestr);
        outState.putString("money",money.getText().toString());
        outState.putString("tip",tip.getText().toString());
    }
}
