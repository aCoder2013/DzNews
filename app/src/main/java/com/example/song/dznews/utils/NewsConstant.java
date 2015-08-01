package com.example.song.dznews.utils;

/**
 * Created by Song on 2015/7/30.
 */
public class NewsConstant {

    public static final String APP_KEY = "53645803"; // 应用的APP_KEY
    public static final String REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";// 应用的回调页
    public static final String SCOPE = // 应用申请的高级权限
            "email,direct_messages_read,direct_messages_write,"
                    + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                    + "follow_app_official_microblog," + "invitation_write";

    public static final String DATABASE_NBME="news.db";
    public static final String NEWS_HAVE_CACHES="newshavecaches";
    public static final String CACHE_PREFERENCE_NAME="cachename";
}
