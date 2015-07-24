package com.example.song.dznews.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.example.song.dznews.R;
import com.example.song.dznews.utils.PreferenceUtils;
import com.example.song.dznews.utils.ThemeUtils;

/**
 * Created by Song on 2015/7/20.
 */
public abstract class BaseActivity extends AppCompatActivity {
    private static final String TAG ="BaseActivity";

    private PreferenceUtils preferenceUtils;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        preferenceUtils = PreferenceUtils.getInstance(this);
        initTheme();
        super.onCreate(savedInstanceState);
        setContentView(getLayoutView());
    }
    public void initTheme() {
        ThemeUtils.Theme theme = getCurrentTheme();
        ThemeUtils.changeTheme(this,theme);
    }

    public ThemeUtils.Theme getCurrentTheme(){
        if(preferenceUtils ==null){
            preferenceUtils  = PreferenceUtils.getInstance(getApplication());
        }
        int drawableValue = preferenceUtils.getThemeParam(getString(R.string.change_theme_key));
        Log.d(TAG,drawableValue+"");
        return ThemeUtils.Theme.getMappedEnumValue(drawableValue);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id){
            case R.id.action_settings:
                Intent intent  = new Intent(this, SettingActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected abstract int getLayoutView();

}
