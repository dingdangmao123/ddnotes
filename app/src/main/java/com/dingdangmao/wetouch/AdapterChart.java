package com.dingdangmao.wetouch;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;

/**
 * Created by suxiaohui on 2017/5/13.
 */

public class AdapterChart extends RecyclerView.Adapter<AdapterChart.ViewHolder> {
    private java.util.List<ChartModel> mList;
    private HashMap<Integer,String> tag=new HashMap<Integer,String>();
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView total;
        TextView type;
        TextView time;
        View view;
        public ViewHolder(View view){
            super(view);
            this.view=view;
            total=(TextView)view.findViewById(R.id.total);
            type=(TextView)view.findViewById(R.id.type);
            time=(TextView)view.findViewById(R.id.time);
            // hide=(TextView)view.findViewById(R.id.hide);
        }
    }
    AdapterChart(java.util.List<ChartModel> tmp, HashMap<Integer,String> tag){
        mList=tmp;
        this.tag=tag;
    }
    public ViewHolder onCreateViewHolder(ViewGroup p, int viewType){
        View view= LayoutInflater.from(p.getContext()).inflate(R.layout.listchart,p,false);
        ViewHolder v= new ViewHolder(view);
        v.view.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                // Intent intent=new Intent(getContext(), java.util.List.class);
                // startActivity(intent);
            }
        });
        return v;
    }
    public void onBindViewHolder(ViewHolder holder,int position){
        ChartModel p=mList.get(position);
        try {

            holder.total.setText(String.valueOf(p.getMoney()));
            holder.type.setText(tag.get(p.getType()));
            holder.time.setText(p.getTime());

        }catch(Exception e){
            Log.i("w",e.toString());
        }
        //holder.title.setText(p.getTitle());
        // holder.hide.setText(String.valueOf(p.getId()));//.setVisibility(View.GONE);
    }
    public int getItemCount(){
        return mList.size();
    }
}

