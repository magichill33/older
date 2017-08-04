package com.nfu.old.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.nfu.old.R;
import com.nfu.old.activity.RoutePlanActivity;
import com.nfu.old.config.NfuResource;
import com.nfu.old.model.OlderModel;
import com.nfu.old.model.ServiceModel;
import com.nfu.old.utils.AppUtils;
import com.nfu.old.utils.DensityUtil;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Administrator on 2017-7-26.
 */

public class OldListAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<OlderModel> newsModelList;
    private IOnDetailListener iOnDetailListener;
    private final int WITH_PIC = 10;
    private final int NORMAL = 20;

    public OldListAdapter(Context mContext, List<OlderModel> newsModelList, IOnDetailListener iOnDetailListener) {
        this.mContext = mContext;
        this.newsModelList = newsModelList;
        this.iOnDetailListener = iOnDetailListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        View view = LayoutInflater.from(mContext).inflate(R.layout.old_list_item,parent,false);
        viewHolder = new MyViewHolder1(view);
        return viewHolder;
    }

    RequestOptions options = new RequestOptions().override(DensityUtil.dip2px(mContext,75),DensityUtil.dip2px(mContext,75))
            .centerCrop().placeholder(R.drawable.def_turn);

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //list_item_shopname,list_item_shoptype_tv,list_item_shoptelephone_tv,list_item_shopmanager_tv,list_item_businessaddress,loction_tv;
        MyViewHolder1 holder1 = (MyViewHolder1) holder;
        OlderModel model = newsModelList.get(position);
        String title = model.getInstitutionsName();
        if (title.length()>15){
            title = title.substring(0,15) + "...";
        }
        holder1.tv_title.setText(title);
        holder1.tv_phone.setText("电话："+model.getTelephone());
    }

    @Override
    public int getItemCount() {
        if (newsModelList == null){
            return 0;
        }
        return newsModelList.size();

    }


    private class MyViewHolder1 extends RecyclerView.ViewHolder{
        TextView tv_title,tv_phone,tv_fax;
        CardView cardView;

        public MyViewHolder1(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_phone = (TextView) itemView.findViewById(R.id.tv_phone);
            //tv_fax = (TextView) itemView.findViewById(R.id.tv_fax);
            cardView = (CardView) itemView.findViewById(R.id.card_view);


            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (iOnDetailListener!=null){
                        iOnDetailListener.onDetailListener(newsModelList.get(getAdapterPosition()));
                    }
                }
            });

        }
    }

    public void setNewsData(List<OlderModel> newsData){
        newsModelList = newsData;
        notifyDataSetChanged();
    }

    public void addNewsData(List<OlderModel> data) {
        if (newsModelList!=null){
            newsModelList.addAll(data);
        }else {
            newsModelList = data;
        }

        notifyDataSetChanged();
    }

    public interface IOnDetailListener{
        void onDetailListener(OlderModel model);
    }
}
