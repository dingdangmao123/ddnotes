package com.dingdangmao.wetouch;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.util.LruCache;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;

import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;

public class Main extends Base {


    @BindView(R.id.dl)
    public DrawerLayout dl;

    @BindView(R.id.drawer)
    public NavigationView nv;


    @BindView(R.id.main)
    public RecyclerView rv;




    private Adapter app;
    private db mydb = new db(this, "mydb.db", null, 2);
    private ArrayList<Model> mlist = new ArrayList<Model>();
    private HashMap<Integer, String> mytag = new HashMap<Integer, String>();

    private MessageReceiver receiver;
    private IntentFilter filter;
    private boolean refresh = true;


    @Override
    public void init(@Nullable Bundle savedInstanceState) {

        ActionBar bar = getSupportActionBar();
        StatusBarUtil.setColorForDrawerLayout(this, dl, ContextCompat.getColor(this, R.color.colorPrimary));

        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setDisplayShowTitleEnabled(false);
        }


        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem mi) {
                switch (mi.getItemId()) {
                    case R.id.count:
                        Intent intent1 = new Intent(Main.this, Chart.class);
                        startActivity(intent1);
                        break;
                    case R.id.out:
                        Intent intent2 = new Intent(Main.this, Out.class);
                        startActivity(intent2);
                        break;
                    case R.id.type:
                        Intent intent3 = new Intent(Main.this, Type.class);
                        startActivity(intent3);
                        break;
                    case R.id.psd:
                        Intent intent4 = new Intent(Main.this, Reset.class);
                        startActivity(intent4);
                        break;
                    case R.id.about:
                        Intent intent5 = new Intent(Main.this, About.class);
                        startActivity(intent5);
                        break;
                    default:
                        break;
                }
                dl.closeDrawers();
                return true;

            }

        });


        receiver = new MessageReceiver();
        filter = new IntentFilter();
        filter.addAction("com.dingdangmao.wetouch.REFRESH");
        registerReceiver(receiver, filter);

        app = new Adapter(this,mlist, mytag);
        rv.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        rv.setAdapter(app);
        rv.setNestedScrollingEnabled(false);

    }

    @Override
    public int getLayoutId() {

        return R.layout.activity_main;

    }


    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

            dl.openDrawer(GravityCompat.START);

        } else if (item.getItemId() == R.id.main_add) {

            Intent intent = new Intent(Main.this, Add.class);
            startActivity(intent);
        }

        return true;
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option, menu);
        return super.onCreateOptionsMenu(menu);

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
        } else
            Log.i("tag", "not refresh");

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }


    private void Refresh() {

        SQLiteDatabase write = mydb.getWritableDatabase();
        mlist.clear();
        Cursor cursor = write.rawQuery("select * from money order by id DESC  limit 0,30", null);

        if (cursor.moveToFirst()) {
            do {

                int id = cursor.getInt(cursor.getColumnIndex("id"));
                int year = cursor.getInt(cursor.getColumnIndex("year"));
                int month = cursor.getInt(cursor.getColumnIndex("month"));
                int day = cursor.getInt(cursor.getColumnIndex("day"));
                float total = cursor.getFloat(cursor.getColumnIndex("total"));
                int type = cursor.getInt(cursor.getColumnIndex("type"));
                String tip = cursor.getString(cursor.getColumnIndex("tip"));
                Log.i("model",id+" ");
                mlist.add(new Model(dateTounix.To(year, month, day), total, type, tip,id));

            } while (cursor.moveToNext());

        }

        cursor.close();

        mytag.clear();

        cursor = write.query("tag", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String tag = cursor.getString(cursor.getColumnIndex("type"));
                mytag.put(id, tag);
            } while (cursor.moveToNext());
        }
        cursor.close();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                app.notifyDataSetChanged();
            }
        });
    }

    class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            refresh = true;

        }
    }

}



