package com.example.song.dznews.ui;

import android.content.Context;
import android.content.Intent;
import android.os.PersistableBundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
 import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.song.dznews.R;
import com.example.song.dznews.adapter.NewsItemAdapter;
import com.example.song.dznews.event.ChangeThemeEvent;
import com.example.song.dznews.model.News;
import com.example.song.dznews.utils.NewsUtils;
import com.example.song.dznews.utils.VolleyUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public class MainActivity extends BaseActivity {
    private static final String TAG="MainActivity";

    private long  lastExitTime;//上次按下返回键的时间

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle ;
    private NavigationView navigationView;
    private RecyclerView recyclerView;
    private CoordinatorLayout rootLayout;
    private NewsItemAdapter adapter;
    private List<News> newsList = new ArrayList<>() ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        initTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rootLayout = (CoordinatorLayout) findViewById(R.id.rootLayout);
        initToolbar();
        initDrawer();
        initNavigation();
        initRecyclerView();
    }

    private void initRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        getNewsList(MainActivity.this,NewsUtils.CNBETA_NEWS_lIST_URL);
        adapter = new NewsItemAdapter(newsList,MainActivity.this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                //得到当前显示的最后一个item的view
                View lastChildView = recyclerView.getLayoutManager().getChildAt(recyclerView.getLayoutManager().getChildCount()-1);
                //得到lastChildView的bottom坐标值
                int lastChildBottom = lastChildView.getBottom();
                //得到Recyclerview的底部坐标减去底部padding值，也就是显示内容最底部的坐标
                int recyclerBottom =  recyclerView.getBottom()-recyclerView.getPaddingBottom();
                //通过这个lastChildView得到这个view当前的position值
                int lastPosition  = recyclerView.getLayoutManager().getPosition(lastChildView);

                //判断lastChildView的bottom值跟recyclerBottom
                //判断lastPosition是不是最后一个position
                //如果两个条件都满足则说明是真正的滑动到了底部
                if(lastChildBottom == recyclerBottom && lastPosition == recyclerView.getLayoutManager().getItemCount()-1 ){
                    Snackbar.make(rootLayout,"正在刷新",Snackbar.LENGTH_SHORT).show();
                    int last_article_id = newsList.get(newsList.size()-1).getArticle_id();
                    getNewsList(MainActivity.this,NewsUtils.CNBETA_MORE_NEWS_URL+last_article_id);
                }
            }
        });
    }

    //找到数组中的最大值
    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStart() {
        super.onStart();
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    // This method will be called when a ChangeThemeEvent is posted
    public void onEvent(ChangeThemeEvent event){
        Log.d(TAG,"ChangeThemeEvent");
        this.recreate();
    }

    private void initNavigation() {
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        //监听导航栏菜单点击事件
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch (id){
                    case R.id.action_nav_settings:
                        menuItem.setChecked(true);
                        drawerLayout.closeDrawers();
                        Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                        startActivity(intent);
                        break;
                }
                return false;
            }
        });
    }

    private void initDrawer() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close){
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /*
        双击返回键退出应用
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            long currentExitTime = System.currentTimeMillis();
            if((currentExitTime-lastExitTime) > 2*1000){
                Toast.makeText(MainActivity.this, "再按一次退出应用", Toast.LENGTH_SHORT)
                        .show();
                lastExitTime = currentExitTime;
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected int getLayoutView() {
        return R.layout.activity_main;
    }



    public  void getNewsList(Context context,String news_url){

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, news_url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        ArrayList newses = new ArrayList<>();
                        for(int i =0;i<response.length();i++){
                            try {
                                JSONObject newsObject  =response.getJSONObject(i);
                                News news = new News();
                                news.setId(newsObject.getInt("id"));
                                news.setArticle_id(newsObject.getInt("article_id"));
                                news.setTitle(newsObject.getString("title"));
                                news.setDate(newsObject.getString("date"));
                                news.setIntro(newsObject.getString("intro"));
                                news.setTopic(newsObject.getString("topic"));
                                news.setView_num(newsObject.getInt("view_num"));
                                news.setComment_num(newsObject.getInt("comment_num"));
                                news.setSource(newsObject.getString("source"));
                                news.setSource_link(newsObject.getString("source_link"));
                                news.setHot(newsObject.getInt("hot"));
                                news.setPushed(newsObject.getInt("pushed"));
                                Log.d(TAG,news.toString());
                                newses.add(news);
                            } catch (JSONException e) {
                                Log.d(TAG,e.getMessage());
                                e.printStackTrace();
                            }
                        }
                        newsList.addAll(newses);
                        adapter.notifyDataSetChanged();
                        adapter.addNews(newsList);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Log.e(TAG,error.getMessage());
                error.printStackTrace();
            }
        });
        VolleyUtils.getInstance(MainActivity.this).addToRequestQueue(request);
    }
}
