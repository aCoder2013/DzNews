package com.example.song.dznews.utils;

import android.app.Activity;
import android.content.Context;

import com.example.song.dznews.R;
import com.example.song.dznews.ui.BaseActivity;

/**
 * Created by Song on 2015/7/19.
 */
public class ThemeUtils {


    public static void changeTheme(Activity activity, Theme theme) {
        if(activity==null){
            return;
        }
        int style =R.style.RedTheme;
        switch (theme){
            case RED:
                style = R.style.RedTheme;
                break;
            case BROWN:
                style = R.style.BrownTheme;
                break;
            case BLUE:
                style = R.style.BlueTheme;
                break;
            case BLUE_GREY:
                style = R.style.BlueGreyTheme;
                break;
            case YELLOW:
                style = R.style.YellowTheme;
                break;
            case DEEP_PURPLE:
                style = R.style.DeepPurpleTheme;
                break;
            case PINK:
                style = R.style.PinkTheme;
                break;
            case GREEN:
                style = R.style.GreenTheme;
                break;
            default:
                break;
            }
            activity.setTheme(style);
        }

    public enum Theme{
        RED(R.drawable.red_round),
        BROWN(R.drawable.brown_round),
        BLUE(R.drawable.blue_round),
        BLUE_GREY(R.drawable.blue_grey_round),
        YELLOW(R.drawable.yellow_round),
        DEEP_PURPLE(R.drawable.deep_purple_round),
        PINK(R.drawable.pink_round),
        GREEN(R.drawable.green_round);

        private int drawableValue;
        Theme(int drawableValue) {
            this.drawableValue = drawableValue;
        }


        public int getDrawableValue() {
            return drawableValue;
        }

        /*
            根据DrawableValue得到匹配的枚举值
         */
        public static Theme getMappedEnumValue(final int drawableValue){
            for(Theme theme:Theme.values()){
                if(theme.getDrawableValue()==drawableValue){
                    return theme;
                }
            }
            return BLUE;
        }
    }
}
