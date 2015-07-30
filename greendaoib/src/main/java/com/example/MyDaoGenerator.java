package com.example;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class MyDaoGenerator {

    public static void main(String [] args){
        Schema schema = new Schema(1,"db.greendao.dznews");
        addNews(schema);
        addNewsDetail(schema);
        try {
            new DaoGenerator().generateAll(schema, "DaoExample/src/main/java-gen");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void addNewsDetail(Schema schema) {
        Entity newsDetail = schema.addEntity("NewsDetail");
        newsDetail.addIdProperty();
        newsDetail.addStringProperty("title");
        newsDetail.addStringProperty("content");
    }


    private static void addNews(Schema schema) {
        Entity news = schema.addEntity("News");
        news.addIdProperty();
        news.addIntProperty("article_id");
        news.addStringProperty("title");
        news.addStringProperty("date");
        news.addStringProperty("intro");
        news.addStringProperty("topic");
        news.addIntProperty("view_num");
        news.addIntProperty("comment_num");
        news.addStringProperty("source");
        news.addStringProperty("source_link");
        news.addIntProperty("hot");
        news.addIntProperty("pushed");

    }
}
