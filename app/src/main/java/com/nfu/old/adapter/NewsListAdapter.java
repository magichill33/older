package com.nfu.old.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.nfu.old.R;
import com.nfu.old.config.NfuResource;
import com.nfu.old.model.NewsModel;
import com.nfu.old.utils.DensityUtil;

import java.util.List;

/**
 * Created by Administrator on 2017-7-26.
 */

public class NewsListAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<NewsModel> newsModelList;
    private IOnDetailListener iOnDetailListener;
    /*private final int WITH_PIC = 10;
    private final int NORMAL = 20;*/

    public NewsListAdapter(Context mContext, List<NewsModel> newsModelList, IOnDetailListener iOnDetailListener) {
        this.mContext = mContext;
        this.newsModelList = newsModelList;
        this.iOnDetailListener = iOnDetailListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.news_item3,parent,false);

        return new MyViewHolder(view);
    }

    RequestOptions options = new RequestOptions().override(DensityUtil.dip2px(mContext,60),DensityUtil.dip2px(mContext,60))
            .centerCrop().placeholder(R.drawable.def_turn);

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        NewsModel newsModel = newsModelList.get(position);
        MyViewHolder holder1 = (MyViewHolder) holder;
        String content = newsModel.getContent();
        if (content.length()>40){
            content = content.substring(0,40);
        }
        content = content + "...";
        holder1.content.setText(content);

        String title = newsModel.getTitle();
        if (title.length()>15){
            title = title.substring(0,15) + "...";
        }
        holder1.title.setText(title);


    }

    @Override
    public int getItemCount() {
        if (newsModelList == null){
            return 0;
        }
        return newsModelList.size();

    }

    private class MyViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        ImageView arrow;
        TextView content;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.tv_title);
            content = (TextView) itemView.findViewById(R.id.tv_content);
            arrow = (ImageView) itemView.findViewById(R.id.right_arrow);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (iOnDetailListener!=null){
                        iOnDetailListener.onDetailListener(newsModelList.get(getAdapterPosition()-1));
                    }
                }
            });
        }
    }

    public void setNewsData(List<NewsModel> newsData){
        newsModelList = newsData;
        notifyDataSetChanged();
    }

    public void addNewsData(List<NewsModel> newsData){
        if (newsModelList!=null){
            newsModelList.addAll(newsData);
        }else {
            newsModelList = newsData;
        }

        notifyDataSetChanged();
    }

    public interface IOnDetailListener{
        void onDetailListener(NewsModel model);
    }
}
