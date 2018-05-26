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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;

import butterknife.BindView;

public class Out extends Base {

    @BindView(R.id.csv)
    public TextView csv;

    @BindView(R.id.ok)
    public Button ok;

    @BindView(R.id.load)
    public Button load;

    private boolean flag=false;
    private db mydb=new db(this,"mydb.db",null,2);

    private HashMap<Integer,String> mytag=new HashMap<Integer,String>();
    private final String fname="data.csv";


    private Handler hld=new Handler(){
        public void handleMessage(Message msg){
            if(msg.what==1){
                flag=false;
                TextView csv=(TextView)findViewById(R.id.csv);
                csv.setText(fname);
                T.show2(Out.this,String.valueOf(msg.obj));
            }
        }
    };

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
                if(flag){
                    T.show2(Out.this, "正在生成CSV文件");
                }else {
                    flag=true;
                    Pool.run(new Runnable() {
                        public void run() {
                            SQLiteDatabase write = mydb.getWritableDatabase();
                            File file = new File(getExternalCacheDir(),fname);
                            FileOutputStream fos;
                            BufferedWriter bw;
                            String log="文件生成成功";
                            try {
                                if(file.exists()){
                                    file.createNewFile();
                                }
                                fos = new FileOutputStream(file);
                                bw = new BufferedWriter(new OutputStreamWriter(fos,"utf-8"));
                                Cursor c = write.rawQuery("select total,type,year,month,day,tip from money order by id DESC", null);
                                if (c.moveToFirst()) {
                                do{
                                    float total = c.getFloat(0); //获取第一列的值,第一列的索引从0开始
                                    int type=c.getInt(1);
                                    int y=c.getInt(2);
                                    int m=c.getInt(3);
                                    int d=c.getInt(4);
                                    bw.write(String.valueOf(y)+"-"+String.valueOf(m)+"-"+String.valueOf(d)+","+String.valueOf(total)+","
                                    +mytag.get(type)+","+c.getString(5));
                                    bw.newLine();
                                }while (c.moveToNext());
                                }
                                c.close();
                                bw.close();
                                write.close();

                            }catch(Exception e){
                                log=e.toString();

                            }
                            Message msg=hld.obtainMessage();
                            msg.what=1;
                            msg.obj=log;
                            hld.sendMessage(msg);

                        }
                    });

                }


            }

        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                try {
                    File file = new File(getExternalCacheDir(), fname);
                    if (flag) {
                        D.tip(Out.this, "正在生成CSV文件");
                    } else if (!file.exists()) {
                        D.tip(Out.this, "文件异常，请重新生成！");
                    } else {
                        //附件文件地址
                        Uri uri;
                        if(Build.VERSION.SDK_INT>=24){
                            uri= FileProvider.getUriForFile(Out.this,"com.dingdangmao.wetouch.fileprovider",file);
                        }else {
                            uri = Uri.fromFile(file);
                        }
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_SUBJECT, "叮当记账CSV格式数据"); //
                        intent.putExtra(Intent.EXTRA_TEXT, "来自 叮当记帐 的CSV记录文件"); //正文
                        intent.putExtra(Intent.EXTRA_STREAM,uri); //添加附件，附件为file对象
                        startActivity(intent);
                    }
                }catch(Exception  e){
                    D.tip(Out.this, e.toString());
                }
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
    public void init(){
     SQLiteDatabase write = mydb.getWritableDatabase();
        Cursor cursor=write.query("tag",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String tag = cursor.getString(cursor.getColumnIndex("type"));
                Log.i("tag",tag);
                mytag.put(id,tag);
            }while(cursor.moveToNext());
        }
        cursor.close();
        File file = new File(Environment.getExternalStorageDirectory(),"data.csv");
        if(file.exists()){
            csv.setText(fname+" 已存在 \n可以直接导出或者你可以重新生成以导出最新数据");
        }
    }

}
