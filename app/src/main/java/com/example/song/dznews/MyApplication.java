package com.example.song.dznews;

import android.app.Application;
import android.content.Context;

import com.example.song.dznews.utils.NewsConstant;

import db.greendao.dznews.DaoMaster;

/**
 * Created by Song on 2015/7/30.
 */
public class MyApplication extends Application {

    private static Context context;
    private static DaoMaster.DevOpenHelper devOpenHelper;
    private static DaoMaster master;

    public static DaoMaster getDaoMaster(Context c){
        if(context==null){
            context = c;
        }
        if(devOpenHelper==null){
            devOpenHelper = new DaoMaster.DevOpenHelper(context, NewsConstant.DATABASE_NBME,null);
        }
        return master==null?(master = new DaoMaster(devOpenHelper.getWritableDatabase())):master;
    }
}
