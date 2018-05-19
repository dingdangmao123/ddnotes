package com.dingdangmao.wetouch;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;
import java.util.HashMap;

public class Main extends AppCompatActivity {
    private DrawerLayout dl;
    private NavigationView nv;
    private Adapter app;
    private RecyclerView rv;
    private db mydb=new db(this,"mydb.db",null,2);
    private ArrayList<Model> mlist=new ArrayList<Model>();
    private HashMap<Integer,String> mytag=new HashMap<Integer,String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

        }
        setContentView(R.layout.activity_main);
        //StatusBarUtil.setColor(this, Color.parseColor("#FFFFFF"));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar bar = getSupportActionBar();
        nv = (NavigationView) findViewById(R.id.drawer);
       // nv.setItemIconTintList(null);
        dl = (DrawerLayout) findViewById(R.id.dl);
        StatusBarUtil.setColorForDrawerLayout(this,dl,ContextCompat.getColor(this,R.color.colorPrimary));
        //nv.setCheckedItem(R.id.aa);
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setDisplayShowTitleEnabled(false);
        }
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem mi) {
                switch (mi.getItemId()) {
                    case R.id.count:
                        Intent intent1=new Intent(Main.this,Chart.class);
                        startActivity(intent1);
                        break;
                    case R.id.out:
                        Intent intent2=new Intent(Main.this,Out.class);
                        startActivity(intent2);
                        break;
                    case R.id.type:
                        Intent intent3=new Intent(Main.this,Type.class);
                        startActivity(intent3);
                        break;
                    case R.id.about:
                        Intent intent4=new Intent(Main.this,About.class);
                        startActivity(intent4);
                        break;
                    default:
                        break;
                }
                dl.closeDrawers();
                return true;

            }

        });
        app = new Adapter(mlist,mytag);
        rv=(RecyclerView)findViewById(R.id.main);
        rv.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));
        rv.setAdapter(app);


    }

    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            dl.openDrawer(GravityCompat.START);
        }else if(item.getItemId() == R.id.main_add)
        {
            Intent intent=new Intent(Main.this,Add.class);
            startActivity(intent);
        }
        return true;
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option, menu);
        return super.onCreateOptionsMenu(menu);
    }
    public void onResume(){

        super.onResume();
        SQLiteDatabase write=mydb.getWritableDatabase();
        mlist.clear();
        //Cursor cursor=write.query("money",null,null,null,null,null,"id DESC",String.valueOf(20));
        Cursor cursor=write.rawQuery("select * from money order by id DESC  limit 0,20",null);
/*
        cursor = write.rawQuery("select total,type,month,day from money order by id DESC",null);
        if(cursor.moveToFirst()){
            while (cursor.moveToNext()) {
                float total = cursor.getFloat(0); //获取第一列的值,第一列的索引从0开始
                Toast.makeText(Main.this, String.valueOf(total), Toast.LENGTH_SHORT).show();

               // model.add(new ChartModel(total, type, String.valueOf(c.getInt(2)) + "-" + String.valueOf(c.getInt(3))));
            }
        }
*/

        if(cursor.moveToFirst()){
            do{

                int id = cursor.getInt(cursor.getColumnIndex("id"));
                int year=cursor.getInt(cursor.getColumnIndex("year"));
                int month=cursor.getInt(cursor.getColumnIndex("month"));
                int day=cursor.getInt(cursor.getColumnIndex("day"));
                float total = cursor.getFloat(cursor.getColumnIndex("total"));
                int type = cursor.getInt(cursor.getColumnIndex("type"));
                String tip = cursor.getString(cursor.getColumnIndex("tip"));

                //Toast.makeText(Main.this,String.valueOf(total),Toast.LENGTH_SHORT).show();
                mlist.add(new Model(dateTounix.To(year,month,day),total,type,tip));

            }while(cursor.moveToNext());

        }

        cursor.close();
        mytag.clear();
        cursor=write.query("tag",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String tag = cursor.getString(cursor.getColumnIndex("type"));
               // Log.i("tag",tag);
                mytag.put(id,tag);
            } while(cursor.moveToNext());
        }
        cursor.close();
        app.notifyDataSetChanged();
    }
}
