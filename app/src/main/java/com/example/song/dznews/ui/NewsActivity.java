package com.example.song.dznews.ui;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.song.dznews.R;
import com.example.song.dznews.utils.NewsUtils;
import com.example.song.dznews.utils.PreferenceUtils;
import com.example.song.dznews.utils.ThemeUtils;
import com.example.song.dznews.utils.VolleyUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;

import db.greendao.dznews.NewsDetail;

public class NewsActivity extends AppCompatActivity {
    private  int article_id ;
    private NewsDetail detail = new NewsDetail();

    private CoordinatorLayout coordinatorLayout;
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private WebView news_content_webview;
    private PreferenceUtils preferenceUtils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        preferenceUtils = PreferenceUtils.getInstance(this);
        initTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        init();
        article_id= getIntent().getIntExtra("article_id",0);
        getNewsDetail(article_id);

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
        return ThemeUtils.Theme.getMappedEnumValue(drawableValue);
    }

    private void init() {
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.rootLayout);
        toolbar= (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
        news_content_webview = (WebView) findViewById(R.id.news_content);
        WebSettings settings = news_content_webview.getSettings();
        settings.setDefaultTextEncodingName("UTF-8");
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setBuiltInZoomControls(false); // 设置显示缩放按钮
        settings.setSupportZoom(false); // 支持缩放
    }


    private void getNewsDetail(final long article_id){
        String article_url = NewsUtils.CNBETA_GET_NEWS_DETAIL+article_id;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, article_url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                detail.setId(article_id);
                try {
                    detail.setTitle(response.getString("title"));
                    detail.setContent(response.getString("content"));
                    collapsingToolbarLayout.setTitle(response.getString("title"));
                    news_content_webview.loadData("<head><style>img{width:320px !important;}</style></head> "+response.getString("content"), "text/html; charset=UTF-8", null);
                    Log.d("tag",detail.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        VolleyUtils.getInstance(NewsActivity.this).addToRequestQueue(request);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_news, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){
            case android.R.id.home:
                finish();
            default:return super.onOptionsItemSelected(item);

        }
    }
}
