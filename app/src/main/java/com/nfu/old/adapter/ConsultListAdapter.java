package com.nfu.old.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.nfu.old.R;
import com.nfu.old.config.NfuResource;
import com.nfu.old.model.ServiceModel;
import com.nfu.old.utils.DensityUtil;

import java.util.List;

/**
 * Created by Administrator on 2017-7-26.
 */

public class ConsultListAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private IOnDetailListener iOnDetailListener;
    private final int WITH_PIC = 10;
    private final int NORMAL = 20;
    private String[] mTitles;
    private int[] mIconId;

    public ConsultListAdapter(Context mContext, String[] titles , int[] iconId, IOnDetailListener iOnDetailListener) {
        this.mContext = mContext;
        this.mTitles = titles;
        this.mIconId = iconId;
        this.iOnDetailListener = iOnDetailListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_consult_item, parent, false);
        viewHolder = new MyViewHolder1(view);

        return viewHolder;
    }

    RequestOptions options = new RequestOptions().override(DensityUtil.dip2px(mContext, 38), DensityUtil.dip2px(mContext, 38))
            .centerCrop().placeholder(R.drawable.def_turn);

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        MyViewHolder1 holder1 = (MyViewHolder1) holder;
        holder1.consult_title_tv.setText(mTitles[position]);
        holder1.consult_item_icon_iv.setBackgroundResource(mIconId[position]);
    }

    @Override
    public int getItemViewType(int position) {

        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        if (mTitles == null) {
            return 0;
        }
        return mTitles.length;

    }

    private class MyViewHolder1 extends RecyclerView.ViewHolder {
        ImageView consult_item_icon_iv, consult_go_ib;
        TextView consult_title_tv;

        public MyViewHolder1(View itemView) {
            super(itemView);
            /*title = (TextView) itemView.findViewById(R.id.tv_title);
            content = (TextView) itemView.findViewById(R.id.tv_content);
            pic = (ImageView) itemView.findViewById(R.id.news_pic);
            arrow = (ImageView) itemView.findViewById(R.id.right_arrow);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (iOnDetailListener!=null){
                        iOnDetailListener.onDetailListener(newsModelList.get(getAdapterPosition()));
                    }
                }
            });*/
            consult_item_icon_iv = (ImageView) itemView.findViewById(R.id.consult_item_icon_iv);
            consult_go_ib = (ImageView) itemView.findViewById(R.id.consult_go_ib);


            consult_title_tv = (TextView) itemView.findViewById(R.id.consult_title_tv);

        }
    }

    public void setNewsData(String[] titles , int[] iconId) {
        this.mTitles = titles;
        this.mIconId = iconId;
        notifyDataSetChanged();
    }

    public interface IOnDetailListener {
        void onDetailListener( );
    }
}
