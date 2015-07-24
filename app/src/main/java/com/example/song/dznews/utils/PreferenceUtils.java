package com.example.song.dznews.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.song.dznews.fragment.SettingFragment;

/**
 * 设置相关工具类
 * Created by Song on 2015/7/19.
 */
public class PreferenceUtils {

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private static PreferenceUtils preferenceUtils = null;

    /*
        静态工厂方法
     */
    public static PreferenceUtils getInstance(Context context){
        if(preferenceUtils==null){
            preferenceUtils = new PreferenceUtils(context);
        }
        return preferenceUtils;
    }

    private  PreferenceUtils(Context context) {
        preferences = context.getSharedPreferences(SettingFragment.NEWS_SETTING_NAME,Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public String getStringParam(String key){
        return getStringParam(key,"");
    }

    public String getStringParam(String key,String defaultValue){
        return preferences.getString(key,defaultValue);
    }

    /*
        Save String Param
     */
    public void saveParam(String key,String value){
        editor.putString(key,value).commit();
    }

    /*
        Save Integer Param
     */
    public void saveParam(String key,int  value){
        editor.putInt(key,value).commit();
    }
    /*
        Get Theme Param
        If not exit ,them {@code return ThemeUtils.Theme.RED.getDrawableValue()};
        eg:
        R.drawable.red_round, R.drawable.brown_round, R.drawable.blue_round,
                R.drawable.blue_grey_round, R.drawable.yellow_round, R.drawable.deep_purple_round,
                R.drawable.pink_round, R.drawable.green_round
     */
    public int getThemeParam(String key){
        return getThemeParam(key, ThemeUtils.Theme.RED.getDrawableValue());//default theme
    }

    /*
        Get Theme  Param
        If not exit,then return defaultValue;
     */
    public int getThemeParam(String key, int defaultValue){
        return preferences.getInt(key,defaultValue);
    }
}
