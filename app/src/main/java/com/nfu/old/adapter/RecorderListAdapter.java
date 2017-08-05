package com.nfu.old.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
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
import com.nfu.old.model.Recorders;
import com.nfu.old.utils.DensityUtil;

import java.util.List;

/**
 * Created by Administrator on 2017-7-26.
 */

public class RecorderListAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<Recorders.ItemsBean> newsModelList;
    private final int WITH_PIC = 10;
    private final int NORMAL = 20;

    public RecorderListAdapter(Context mContext, List<Recorders.ItemsBean> items)
    {
        this.mContext = mContext;
        newsModelList = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        View view = LayoutInflater.from(mContext).inflate(R.layout.recoder_item,parent,false);
        viewHolder = new MyViewHolder1(view);

        return viewHolder;
    }

    RequestOptions options = new RequestOptions().override(DensityUtil.dip2px(mContext,60),DensityUtil.dip2px(mContext,60))
            .centerCrop().placeholder(R.drawable.def_turn);

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            Recorders.ItemsBean newsModel = newsModelList.get(position);
            MyViewHolder1 holder1 = (MyViewHolder1) holder;
            holder1.money.setText(newsModel.getBalance());
            holder1.date.setText(newsModel.getTime());

    }



    @Override
    public int getItemCount() {
        if (newsModelList == null){
            return 0;
        }
        return newsModelList.size();

    }

    private class MyViewHolder1 extends RecyclerView.ViewHolder{
        CardView card_view;
        TextView date;
        TextView money;

        public MyViewHolder1(View itemView) {
            super(itemView);
            card_view = (CardView) itemView.findViewById(R.id.card_view);
            date = (TextView) itemView.findViewById(R.id.tv_date);
            money = (TextView) itemView.findViewById(R.id.tv_money);


        }
    }

    public void setNewsData(List<Recorders.ItemsBean> newsData){
        newsModelList = newsData;
        notifyDataSetChanged();
    }

    public void addNewsData(List<Recorders.ItemsBean> newsData){
        if (newsModelList!=null){
            newsModelList.addAll(newsData);
        }else {
            newsModelList = newsData;
        }

        notifyDataSetChanged();
    }



}
