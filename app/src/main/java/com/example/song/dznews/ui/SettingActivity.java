package com.example.song.dznews.ui;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;

import com.example.song.dznews.R;
import com.example.song.dznews.fragment.SettingFragment;
import com.example.song.dznews.utils.ThemeUtils;

public class SettingActivity extends BaseActivity {

    private Toolbar toolbar;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP ){
            //设置状态栏颜色
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //Change Status Color
            window.setStatusBarColor(getResources().getColor(getStatusColcor()));
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
        initFragment();
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /*
        Init Method
     */
    private void initFragment() {
        SettingFragment fragment = SettingFragment.newInstance();
        getFragmentManager().beginTransaction().replace(R.id.fragment_content, fragment)
                .commit();
    }


    @Override
    protected int getLayoutView() {
        return R.layout.activity_setting;
    }

    public int  getStatusColcor() {
        ThemeUtils.Theme theme = getCurrentTheme();
        switch (theme){
            case RED:
                return R.color.dark_red;
            case BLUE:
                return R.color.dark_blue;
            case BROWN:
                return R.color.dark_brown;
            case BLUE_GREY:
                return R.color.dark_blue_grey;
            case GREEN:
                return R.color.dark_green;
            case YELLOW:
                return R.color.dark_yellow;
            case PINK:
                return R.color.dark_pink;
            case DEEP_PURPLE:
                return R.color.dark_deep_purple;
            default:return android.R.color.black;
        }
    }
}
