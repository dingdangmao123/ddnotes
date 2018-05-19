package com.dingdangmao.wetouch;

/**
 * Created by suxiaohui on 2017/5/12.
 */

public class ChartModel {
        private float money;
        private int type;
        private String time;

        public ChartModel(float money,int type,String time){

            this.money=money;
            this.type=type;
            this.time=time;
        }
        public float getMoney(){ return this.money;}
        public int getType(){ return this.type;}
        public String getTime(){ return this.time;}

}
