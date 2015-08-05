package com.example.song.dznews.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.song.dznews.MyApplication;
import com.example.song.dznews.R;
import com.example.song.dznews.adapter.NewsItemAdapter;
import com.example.song.dznews.event.ChangeThemeEvent;
import com.example.song.dznews.utils.NewsConstant;
import com.example.song.dznews.utils.NewsUtils;
import com.example.song.dznews.utils.VolleyUtils;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.sina.weibo.sdk.openapi.models.User;
import com.sina.weibo.sdk.utils.LogUtil;
import com.squareup.picasso.Picasso;
import com.yalantis.phoenix.PullToRefreshView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import db.greendao.dznews.DaoMaster;
import db.greendao.dznews.DaoSession;
import db.greendao.dznews.News;
import db.greendao.dznews.NewsDao;
import de.greenrobot.event.EventBus;

import static com.example.song.dznews.ui.MainActivity.NewsLoadType.LOAD_MORE;
import static com.example.song.dznews.ui.MainActivity.NewsLoadType.REFERESH;

public class MainActivity extends BaseActivity {
    private static final String TAG="MainActivity";
    public static int sScreenWidth;
    public static int sProfileImageHeight;
    private long  lastExitTime;//上次按下返回键的时间
    private boolean haveCache =false;
    private SharedPreferences sharedPreference;
    private SharedPreferences.Editor editor;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle ;
    private NavigationView navigationView;
    private PullToRefreshView pullToRefreshView;
    private RecyclerView recyclerView;
    private CoordinatorLayout rootLayout;
    private NewsItemAdapter adapter;
    private List<News> newsList = new ArrayList<>() ;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private NewsDao newsDao;
    private AuthInfo authInfo;
    private SsoHandler ssoHandler;
    private RoundedImageView header_image;
    private TextView header_text_title;
    private TextView header_email;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        initTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sScreenWidth = getResources().getDisplayMetrics().widthPixels;
        sProfileImageHeight = getResources().getDimensionPixelSize(R.dimen.height_profile_image);
        rootLayout = (CoordinatorLayout) findViewById(R.id.rootLayout);
        initDB();
        initSharedPre();
        initPullToRefereshView();
        initToolbar();
        initDrawer();
        initNavigation();
        initRecyclerView();
    }

    private void initSharedPre() {
        sharedPreference = getSharedPreferences(NewsConstant.CACHE_PREFERENCE_NAME,MODE_PRIVATE);
        editor =sharedPreference.edit();
        //Check if there is caches
        haveCache = sharedPreference.getBoolean(NewsConstant.NEWS_HAVE_CACHES,false);
        if(haveCache){
            newsList.clear();
            newsList.addAll(newsDao.queryBuilder().orderDesc(NewsDao.Properties.Id).list());
        }else {
            getNewsList(MainActivity.this, NewsUtils.CNBETA_NEWS_lIST_URL,REFERESH);
        }
    }

    private void initDB() {
        daoMaster = MyApplication.getDaoMaster(MainActivity.this);
        daoSession =  daoMaster.newSession();
        newsDao = daoSession.getNewsDao();
    }

    private void initPullToRefereshView() {
        pullToRefreshView = (PullToRefreshView) findViewById(R.id.pull_to_refresh);
        pullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        newsDao.deleteAll();//删除旧数据
                        newsList.clear();
                        getNewsList(MainActivity.this, NewsUtils.CNBETA_NEWS_lIST_URL,REFERESH);
                        pullToRefreshView.setRefreshing(false);
                    }
                }, 1000);
            }
        });
    }

    private void initRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        adapter = new NewsItemAdapter(newsList,MainActivity.this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                Log.d(TAG,""+(recyclerView.getLayoutManager().getChildCount()-1));
                //得到当前显示的最后一个item的view
                View lastChildView = recyclerView.getLayoutManager().getChildAt(recyclerView.getLayoutManager().getChildCount()-1);
                if(lastChildView==null){
                    return;
                }
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
                    Snackbar.make(rootLayout,"正在刷新",Snackbar.LENGTH_LONG).show();
                    int last_article_id = newsList.get(newsList.size()-1).getArticle_id();
                    getNewsList(MainActivity.this,NewsUtils.CNBETA_MORE_NEWS_URL+last_article_id,LOAD_MORE);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
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
        header_image = (RoundedImageView) findViewById(R.id.header_image);
        header_text_title = (TextView) findViewById(R.id.header_text_title);
        header_email = (TextView) findViewById(R.id.header_email);
        if(sharedPreference.getLong("uuid",0L)!=0L){
            Picasso.with(MainActivity.this).load(sharedPreference.getString("avatar_hd",null)).into(header_image);
            header_text_title.setText(sharedPreference.getString("screen_name",null));
            header_email.setText(sharedPreference.getString("profile_url","登陆了就是不一样"));
        }
        //微博登陆
        authInfo  = new AuthInfo(this,NewsConstant.APP_KEY,NewsConstant.REDIRECT_URL,NewsConstant.SCOPE);
        header_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ssoHandler = new SsoHandler(MainActivity.this, authInfo);
                ssoHandler.authorizeClientSso(new AuthListener());
            }
        });
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



    /*
        Get NewsList
        @Param newsLoadType:新闻加载类型，接受MainActivity.NewsLoadType类型
     */
    public  void getNewsList(Context context,String news_url,final NewsLoadType newsLoadType){

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, news_url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        ArrayList<News> newses = new ArrayList<>();
                        for(int i =0;i<response.length();i++){
                            try {
                                JSONObject newsObject  =response.getJSONObject(i);
                                News news = getNews(newsObject);
                                switch(newsLoadType){
                                    case REFERESH:
                                        newsDao.insert(news);
                                        break;
                                    case LOAD_MORE:
                                        break;
                                    default:break;

                                }
                                newses.add(news);
                            } catch (JSONException e) {
                                Log.d(TAG,e.getMessage());
                                e.printStackTrace();
                            }
                        }
                        newsList.addAll(newses);
                        adapter.addNews(newsList);
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Log.e(TAG,error.getMessage());
                error.printStackTrace();
            }
        });
        VolleyUtils.getInstance(MainActivity.this).addToRequestQueue(request);
        editor.putBoolean(NewsConstant.NEWS_HAVE_CACHES,true);
        editor.commit();
    }

    /*
        Helper Method To Get NewsList
     */
    @NonNull
    private News getNews(JSONObject newsObject) throws JSONException {
        News news = new News();
        news.setId(newsObject.getLong("id"));
        news.setArticle_id(newsObject.getInt("article_id"));
        news.setIntro(newsObject.getString("intro"));
        news.setTitle(newsObject.getString("title"));
        news.setDate(newsObject.getString("date"));
        news.setTopic(newsObject.getString("topic"));
        news.setView_num(newsObject.getInt("view_num"));
        news.setComment_num(newsObject.getInt("comment_num"));
        news.setSource(newsObject.getString("source"));
        news.setSource_link(newsObject.getString("source_link"));
        news.setHot(newsObject.getInt("hot"));
        news.setPushed(newsObject.getInt("pushed"));
        Log.d(TAG, news.toString());
        return news;
    }

    /*
        新闻加载类型
     */
    enum NewsLoadType{
        REFERESH,LOAD_MORE
    }

    /*
        内部类
     */
    class AuthListener implements WeiboAuthListener {
        private static final String TAG="AuthListener";
        private Oauth2AccessToken accessToken;
        private UsersAPI usersAPI;
        @Override
        public void onComplete(Bundle bundle) {
            accessToken = Oauth2AccessToken.parseAccessToken(bundle); // 从 Bundle 中解析 Token
            accessToken.getUid();
            if (accessToken.isSessionValid()) {
                long uuid = Long.parseLong(accessToken.getUid());
                editor.putLong("uuid",uuid);
                usersAPI = new UsersAPI(MainActivity.this, NewsConstant.APP_KEY,accessToken);
                usersAPI.show(uuid, new RequestListener() {
                    @Override
                    public void onComplete(String response) {
                        if (!TextUtils.isEmpty(response)) {
                            LogUtil.i(TAG, response);
                            // 调用 User#parse 将JSON串解析成User对象
                            User user = User.parse(response);
                            if (user != null) {
                                editor.putString("screen_name",user.screen_name);//昵称
                                editor.putString("domain",user.description);//个性化域名
                                editor.putString("avatar_hd",user.avatar_hd);//高清头像地址
                                Picasso.with(MainActivity.this).load(user.avatar_hd).into(header_image);
                                header_text_title.setText(user.screen_name);
                                header_email.setText("登陆了就是不一样");
                                Log.d(TAG,user.description);
                                Log.d(TAG,user.domain);
                                Toast.makeText(MainActivity.this,"登陆成功",Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(MainActivity.this, "获取失败", Toast.LENGTH_LONG).show();
                            }
                        }
                    }

                    @Override
                    public void onWeiboException(WeiboException e) {

                    }
                });
            } else {
                // 当您注册的应用程序签名不正确时，就会收到错误Code，请确保签名正确
                String code = bundle.getString("code", "");
                Log.e(TAG,code);
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {

        }

        @Override
        public void onCancel() {

        }


    }

}
