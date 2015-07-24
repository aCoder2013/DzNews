package com.example.song.dznews.model;

import java.io.Serializable;

/**
 * 新闻实体
 * Created by Song on 2015/7/22.
 *eg:
 "id": 44861,
 "article_id": 413407,
 "title": "[图]用推文测量美国各州肥胖人群比例分配",
 "date": "2015-07-22 14:50:04",
 "intro": "如果你每天摄入的能量大于消耗的能量，那么日积月累可能变得比较肥胖。但从大数据上来较为精准的测量卡路里摄入和能量消耗是非常困难的事情，不过通过推特有可能做到。近日开源提交网站arXiv的科研团队创造了名为“Lexicocalorimeter”，这是一款在社交网络上测量关于卡路里相关内容的在线交互工具，对推文中包含食物和活动相关的短语进行分析，并通过定制的算法来绘制每个地区内的肥胖人群比例。",
 "topic": "//cnbeta1.sinaapp.com/topics/11-12-14 08-11-26.gif",
 "view_num": 26,
 "comment_num": 0,
 "source": "cnBeta.COM",
 "source_link": "http://www.cnbeta.com/",
 "hot": 0,
 "pushed": 0
 */
public class News  implements Serializable{

    private int id ;
    private int article_id;
    private String title;
    private String date ;
    private String intro;
    private String topic ;
    private int view_num;
    private int comment_num;
    private String source;
    private String source_link;
    private int hot;
    private int pushed;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getArticle_id() {
        return article_id;
    }

    public void setArticle_id(int article_id) {
        this.article_id = article_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public int getView_num() {
        return view_num;
    }

    public void setView_num(int view_num) {
        this.view_num = view_num;
    }

    public int getComment_num() {
        return comment_num;
    }

    public void setComment_num(int comment_num) {
        this.comment_num = comment_num;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSource_link() {
        return source_link;
    }

    public void setSource_link(String source_link) {
        this.source_link = source_link;
    }

    public int getHot() {
        return hot;
    }

    public void setHot(int hot) {
        this.hot = hot;
    }

    public int getPushed() {
        return pushed;
    }

    public void setPushed(int pushed) {
        this.pushed = pushed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        News news = (News) o;

        if (id != news.id) return false;
        if (article_id != news.article_id) return false;
        if (view_num != news.view_num) return false;
        if (comment_num != news.comment_num) return false;
        if (hot != news.hot) return false;
        if (pushed != news.pushed) return false;
        if (title != null ? !title.equals(news.title) : news.title != null) return false;
        if (date != null ? !date.equals(news.date) : news.date != null) return false;
        if (intro != null ? !intro.equals(news.intro) : news.intro != null) return false;
        if (topic != null ? !topic.equals(news.topic) : news.topic != null) return false;
        if (source != null ? !source.equals(news.source) : news.source != null) return false;
        return !(source_link != null ? !source_link.equals(news.source_link) : news.source_link != null);

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + article_id;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (intro != null ? intro.hashCode() : 0);
        result = 31 * result + (topic != null ? topic.hashCode() : 0);
        result = 31 * result + view_num;
        result = 31 * result + comment_num;
        result = 31 * result + (source != null ? source.hashCode() : 0);
        result = 31 * result + (source_link != null ? source_link.hashCode() : 0);
        result = 31 * result + hot;
        result = 31 * result + pushed;
        return result;
    }

    @Override
    public String toString() {
        return "News{" +
                "id=" + id +
                ", article_id=" + article_id +
                ", title='" + title + '\'' +
                ", date='" + date + '\'' +
                ", intro='" + intro + '\'' +
                ", topic='" + topic + '\'' +
                ", view_num=" + view_num +
                ", comment_num=" + comment_num +
                ", source='" + source + '\'' +
                ", source_link='" + source_link + '\'' +
                ", hot=" + hot +
                ", pushed=" + pushed +
                '}';
    }
}
