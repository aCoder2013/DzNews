package com.example.song.dznews.utils;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.song.dznews.model.News;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Song on 2015/7/22.
 */
public class NewsUtils {
    private static final String TAG="NewsUtils";
    //获取首页新闻
    public static final String CNBETA_NEWS_lIST_URL="https://cnbeta1.com/api/getArticles";
    //获取更多新闻
    public static final String CNBETA_MORE_NEWS_URL="https://cnbeta1.com/api/getMoreArticles/{fromArticleID}";
    //获取新闻详情
    public static final String CNBETA_GET_NEWS_DETAIL ="https://cnbeta1.com/api/getArticleDetail/";

    /*
        Cnbeta
     */

}
