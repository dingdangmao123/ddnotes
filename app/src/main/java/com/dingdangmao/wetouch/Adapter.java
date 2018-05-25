package com.dingdangmao.wetouch;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by suxiaohui on 2017/5/11.
 */

public class Adapter extends RecyclerView.Adapter<Adapter.myViewHolder> {
    private db mydb = null;
    private Context context;
    private java.util.List<Model> mList;
    private HashMap<Integer, String> tag = new HashMap<Integer, String>();

    static class myViewHolder extends RecyclerView.ViewHolder {
        ViewGroup f;
        TextView total;
        TextView tip;
        TextView type;
        TextView time;
        View view;

        public myViewHolder(View view) {
            super(view);
            this.view = view;
            f = (ViewGroup) view.findViewById(R.id.bg);
            total = (TextView) view.findViewById(R.id.total);
            tip = (TextView) view.findViewById(R.id.tip);
            type = (TextView) view.findViewById(R.id.type);
            time = (TextView) view.findViewById(R.id.time);
        }
    }

    Adapter(Context ctx, java.util.List<Model> tmp, HashMap<Integer, String> tag) {
        context = ctx;
        mList = tmp;
        this.tag = tag;
    }

    public myViewHolder onCreateViewHolder(final ViewGroup p, int viewType) {


        View view = LayoutInflater.from(p.getContext()).inflate(R.layout.list, p, false);
        final myViewHolder v = new myViewHolder(view);
        v.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View vv) {

                final int pos = v.getAdapterPosition();
                new SweetAlertDialog(p.getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("确定?")
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
                                delete(pos);
                            }
                        });


                    }
                }).show();

                return true;
            }
        });
        return v;
    }

    public void onBindViewHolder(myViewHolder holder, int position) {
        Model p = mList.get(position);
        try {

            holder.f.setBackgroundColor(MyColor.data2[p.getType() % MyColor.data.length]);
            holder.total.setText("¥ " + String.valueOf(p.getMoney()));
            holder.type.setTextColor(MyColor.data2[p.getType() % MyColor.data.length]);
            holder.tip.setText(p.getTip());
            String tmp = tag.get(p.getType());
            if (tmp != null)
                holder.type.setText(tmp);
            else
                holder.type.setText("其他分类");
            holder.time.setText(p.getTime());

        } catch (Exception e) {

            Log.i("w", e.toString());

        }
    }

    public int getItemCount() {
        return mList.size();
    }

    void delete(final int pos) {

        if (mydb == null) {
            mydb = new db(context, "mydb.db", null, 2);
        }

        SQLiteDatabase write = mydb.getWritableDatabase();
        try {

            int id = mList.get(pos).getId();
            Log.i("delete", pos + " " + id);
            write.execSQL("delete  from money where id=?", new Integer[]{id});


        } catch (Exception e) {
            D.show(context, e.toString());

        }
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mList.remove(pos);
                notifyDataSetChanged();
                D.show(context, "删除成功！");

            }
        });

    }

}

