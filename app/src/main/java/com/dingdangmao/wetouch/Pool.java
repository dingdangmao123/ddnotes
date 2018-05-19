package com.dingdangmao.wetouch;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by gapcoder on 2018/5/19.
 */

public class Pool {
    private static ExecutorService exec=Executors.newFixedThreadPool(1);

    public static void run(Runnable r){

        exec.submit(r);

    }
}
