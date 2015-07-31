package com.example.song.dznews.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.song.dznews.R;
import com.example.song.dznews.ui.NewsActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import db.greendao.dznews.News;

/**
 * Created by Song on 2015/7/22.
 */
public class NewsItemAdapter extends RecyclerView.Adapter<NewsItemAdapter.NewsViewHolder>{

    private List<News> newsList = new ArrayList<>();
    private Context context;

    public NewsItemAdapter() {
    }

    public NewsItemAdapter(List<News> newsList, Context context) {
        if(newsList!=null&&context!=null){
            this.newsList.addAll(newsList);
            this.context = context;
        }
    }

    @Override
    public NewsItemAdapter.NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.news_item_card,parent,false);
        NewsViewHolder nvh=new NewsViewHolder(v);
        return nvh;
    }

    @Override
    public void onBindViewHolder(NewsItemAdapter.NewsViewHolder holder, int position) {
        final int j=position;
        Picasso.with(context).load("http:"+newsList.get(position).getTopic()).into(holder.news_photo);
        holder.news_title.setText(newsList.get(position).getTitle());
        holder.news_desc.setText(newsList.get(position).getIntro());

        //为btn_share btn_readMore cardView设置点击事件
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,NewsActivity.class);
                intent.putExtra("article_id",newsList.get(j).getArticle_id());
                context.startActivity(intent);
            }
        });

        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
                intent.putExtra(Intent.EXTRA_TEXT, newsList.get(j).getIntro());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(Intent.createChooser(intent, newsList.get(j).getTitle()));
            }
        });

        holder.readMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,NewsActivity.class);
                intent.putExtra("article_id",newsList.get(j).getArticle_id());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public void addNews(List<News> newsList){
        this.newsList=newsList;
    }

    static class NewsViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView news_photo;
        TextView news_title;
        TextView news_desc;
        Button share;
        Button readMore;


        public NewsViewHolder(View itemView) {
            super(itemView);
            cardView= (CardView) itemView.findViewById(R.id.card_view);
            news_photo= (ImageView) itemView.findViewById(R.id.news_photo);
            news_title= (TextView) itemView.findViewById(R.id.news_title);
            news_desc= (TextView) itemView.findViewById(R.id.news_desc);
            share= (Button) itemView.findViewById(R.id.btn_share);
            readMore= (Button) itemView.findViewById(R.id.btn_more);
        }
    }


}
