package com.example.song.dznews.fragment;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.song.dznews.R;
import com.example.song.dznews.adapter.ColorsListAdapter;
import com.example.song.dznews.event.ChangeThemeEvent;
import com.example.song.dznews.utils.PreferenceUtils;

import java.util.Arrays;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Song on 2015/7/17.
 */
public class SettingFragment extends PreferenceFragment {

    public static final String NEWS_SETTING_NAME = "news.setting";


    private Preference change_theme_key;

    /*
        return a  new SettingFragment Instance
     */
    public static SettingFragment newInstance() {
        SettingFragment fragment = new SettingFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        getPreferenceManager().setSharedPreferencesName(NEWS_SETTING_NAME);
        change_theme_key = findPreference(getString(R.string.change_theme_key));
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if (preference == null) {
            return super.onPreferenceTreeClick(preferenceScreen, preference);
        }
        String key = preference.getKey();
        //Change Theme
        if (TextUtils.equals(key, getString(R.string.change_theme_key))) {
            showThemeChooseDialog();
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    /*
        弹出选择主题对话框
     */
    private void showThemeChooseDialog() {
        AlertDialog.Builder builder = generatDialogBuilder();
        builder.setTitle(R.string.choose_theme);
        final Integer[] res = new Integer[]{R.drawable.red_round, R.drawable.brown_round, R.drawable.blue_round,
                R.drawable.blue_grey_round, R.drawable.yellow_round, R.drawable.deep_purple_round,
                R.drawable.pink_round, R.drawable.green_round};
        List<Integer> list = Arrays.asList(res);
        ColorsListAdapter adapter = new ColorsListAdapter(getActivity(), list);
        adapter.setCheckItem(getCurrentTheme());
        GridView gridView = (GridView) LayoutInflater.from(getActivity()).inflate(R.layout.choose_color_panel_layout, null);
        gridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
        gridView.setCacheColorHint(0);
        gridView.setAdapter(adapter);
        builder.setView(gridView);
        final AlertDialog dialog = builder.show();
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();
                //保存Drawable
                PreferenceUtils.getInstance(getActivity())
                        .saveParam(getString(R.string.change_theme_key), res[position]);
                    EventBus.getDefault().post(new ChangeThemeEvent("hello"));
                getActivity().finish();
            }
        });
    }

    private AlertDialog.Builder generatDialogBuilder() {
        return new AlertDialog.Builder(getActivity(), R.style.PinkDialogTheme);
    }

    public int getCurrentTheme() {
        return PreferenceUtils.getInstance(getActivity())
                .getThemeParam(getString(R.string.change_theme_key));
    }
}
