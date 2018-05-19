package com.dingdangmao.wetouch;

/**
 * Created by suxiaohui on 2017/5/11.
 */

public class Model {
    private int time;
    private float money;
    private int type;
    private String tip;
    private static int unix=(int)(System.currentTimeMillis()/1000);
    public Model(int time,float money,int type,String tip){
        this.time=time;
        this.money=money;
        this.type=type;
        this.tip=tip;
    }
    public String getTime(){

            int tmp=(unix-time)/86400;
        if(tmp<0)
            return "未来";
            else if(tmp==0)
                return "今天";
            else if(tmp==1)
                return "昨天";
            else
                return String.valueOf(tmp)+" 天前";
    }
    public float getMoney(){ return this.money;}
    public int getType(){ return this.type;}
    public String getTip(){ return this.tip;}
}
