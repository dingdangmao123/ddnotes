package com.dingdangmao.wetouch;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by suxiaohui on 2017/5/11.
 */

public class db extends SQLiteOpenHelper {
    public static final String CTRATE_MONEY="create table money("
            +"id integer primary key autoincrement,"
            +"year integer,month integer,day integer,total integer,tip text, type integer)";
    public static final String CTRATE_TYPE="create table tag("
            +"id integer primary key autoincrement,"
            +"type text)";
    private Context mcontext;
    public db(Context context,String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context,name,factory,1);
        mcontext=context;
    }
    public void onCreate(SQLiteDatabase db){

    try {
            db.execSQL(CTRATE_MONEY);
            db.execSQL(CTRATE_TYPE);
         }catch(Exception e) {
             Toast.makeText(mcontext,e.toString(),Toast.LENGTH_SHORT).show();
        }
    }
    public void onUpgrade(SQLiteDatabase db,int old,int newv){
        db.execSQL("drop table if exists money");
        db.execSQL("drop table if exists type");
        onCreate(db);
    }

}
