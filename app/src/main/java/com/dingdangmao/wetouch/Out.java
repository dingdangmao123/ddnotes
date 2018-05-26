package com.dingdangmao.wetouch;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;

public class Out extends Base {


    @BindView(R.id.start)
    public Button start;

    @BindView(R.id.end)
    public Button end;


    @BindView(R.id.starttime)
    public TextView starttime;


    @BindView(R.id.endtime)
    public TextView endtime;


    @BindView(R.id.csv)
    public TextView csv;

    @BindView(R.id.ok)
    public Button ok;

    @BindView(R.id.load)
    public Button load;

    public boolean flag = false;

    String l = null;
    String r = null;

    private db mydb = new db(this, "mydb.db", null, 2);

    private HashMap<Integer, String> mytag = new HashMap<Integer, String>();
    public final String fname = "data.csv";


    @Override
    public int getLayoutId() {
        return R.layout.activity_out;
    }

    @Override
    public void init(Bundle savedInstanceState) {

        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setDisplayShowTitleEnabled(false);
        }

        Util.toolbar(this);

        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ((l != null && r == null) || (l == null && r != null)) {
                    T.show2(Out.this, "时间区间不对");
                    return;
                }

                if (flag) {

                    T.show2(Out.this, "正在生成CSV文件");

                } else {

                    l = null;
                    r = null;
                    starttime.setText("");
                    endtime.setText("");

                    flag = true;
                    Pool.run(new Runnable() {
                        public void run() {
                            SQLiteDatabase write = mydb.getWritableDatabase();
                            File file = new File(getExternalCacheDir(), fname);
                            FileOutputStream fos;
                            BufferedWriter bw;
                            String log = "文件生成成功";
                            try {
                                if (file.exists()) {
                                    file.createNewFile();
                                }
                                fos = new FileOutputStream(file);
                                bw = new BufferedWriter(new OutputStreamWriter(fos, "utf-8"));
                                Cursor c = null;
                                if (l == null && r == null)
                                    c = write.rawQuery("select total,type,year,month,day,tip from money order by id DESC", null);
                                else {

                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                    Date date = null;
                                    try {
                                        date = simpleDateFormat.parse(l);
                                        int left = ((int) date.getTime() / 1000);
                                        date = simpleDateFormat.parse(r);
                                        int right = ((int) date.getTime() / 1000);

                                        if (left >= right) {
                                            T.show2(Out.this, "时间区间错误");
                                            return;
                                        }

                                        c = write.rawQuery("select total,type,year,month,day,tip from money where unix>=? and unix<=? order by id DESC", new String[]{String.valueOf(left), String.valueOf(right)});

                                    } catch (Exception e) {

                                    }

                                }

                                if (c.moveToFirst()) {
                                    do {
                                        float total = c.getFloat(0); //获取第一列的值,第一列的索引从0开始
                                        int type = c.getInt(1);
                                        int y = c.getInt(2);
                                        int m = c.getInt(3);
                                        int d = c.getInt(4);
                                        bw.write(String.valueOf(y) + "-" + String.valueOf(m) + "-" + String.valueOf(d) + "," + String.valueOf(total) + ","
                                                + mytag.get(type) + "," + c.getString(5));
                                        bw.newLine();
                                    } while (c.moveToNext());
                                    c.close();
                                    bw.close();
                                    write.close();
                                }else{
                                    T.show2(Out.this,"没有数据");
                                    return ;
                                }



                            } catch (Exception e) {
                                log = e.toString();

                            }

                            final String str = log;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    flag = false;
                                    TextView csv = (TextView) findViewById(R.id.csv);
                                    csv.setText(fname);
                                    T.show2(Out.this, str);
                                }
                            });

                        }
                    });

                }


            }

        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    File file = new File(getExternalCacheDir(), fname);
                    if (flag) {
                        D.tip(Out.this, "正在生成CSV文件");
                    } else if (!file.exists()) {
                        D.tip(Out.this, "文件异常，请重新生成！");
                    } else {
                        Uri uri;
                        if (Build.VERSION.SDK_INT >= 24) {
                            uri = FileProvider.getUriForFile(Out.this, "com.dingdangmao.wetouch.fileprovider", file);
                        } else {
                            uri = Uri.fromFile(file);
                        }
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_SUBJECT, "叮当记账CSV格式数据"); //
                        intent.putExtra(Intent.EXTRA_TEXT, "来自 叮当记帐 的CSV记录文件"); //正文
                        intent.putExtra(Intent.EXTRA_STREAM, uri); //添加附件，附件为file对象
                        startActivity(intent);
                    }
                } catch (Exception e) {
                    D.tip(Out.this, e.toString());
                }
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final boolean[] type = new boolean[]{true, true, true, false, false, false};
                TimePickerView pvTime = new TimePickerView.Builder(Out.this, new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {

                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                        l = formatter.format(date);
                        starttime.setText(l);

                    }
                }).setType(type).setCancelText("取消").setSubmitText("确定")
                        .setSubmitColor(R.color.colorButton)
                        .setCancelColor(R.color.colorTip).build();
                pvTime.show();
            }
        });

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final boolean[] type = new boolean[]{true, true, true, false, false, false};
                TimePickerView pvTime = new TimePickerView.Builder(Out.this, new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {

                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                        r = formatter.format(date);
                        endtime.setText(r);

                    }
                }).setType(type).setCancelText("取消").setSubmitText("确定")
                        .setSubmitColor(R.color.colorButton)
                        .setCancelColor(R.color.colorTip).build();
                pvTime.show();
            }
        });


        init();
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

    public void init() {
        SQLiteDatabase write = mydb.getWritableDatabase();
        Cursor cursor = write.query("tag", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String tag = cursor.getString(cursor.getColumnIndex("type"));
                Log.i("tag", tag);
                mytag.put(id, tag);
            } while (cursor.moveToNext());
        }
        cursor.close();
        File file = new File(Environment.getExternalStorageDirectory(), "data.csv");
        if (file.exists()) {
            csv.setText(fname + " 已存在 \n可以直接导出或者你可以重新生成以导出最新数据");
        }
    }

}
