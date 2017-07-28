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
import com.nfu.old.fragment.HomeFragment;
import com.nfu.old.model.NewsModel;
import com.nfu.old.utils.DensityUtil;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Administrator on 2017-7-26.
 */

public class PolicyListAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<NewsModel> newsModelList;
    private IOnDetailListener iOnDetailListener;
    private final int WITH_PIC = 10;
    private final int NORMAL = 20;

    public PolicyListAdapter(Context mContext, List<NewsModel> newsModelList, IOnDetailListener iOnDetailListener) {
        this.mContext = mContext;
        this.newsModelList = newsModelList;
        this.iOnDetailListener = iOnDetailListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        if (viewType == WITH_PIC){
            View view = LayoutInflater.from(mContext).inflate(R.layout.news_item1,parent,false);
            viewHolder = new MyViewHolder1(view);
        }else if(viewType == NORMAL){
            View view = LayoutInflater.from(mContext).inflate(R.layout.news_item2,parent,false);
            viewHolder = new MyViewHolder2(view);
        }
        return viewHolder;
    }

    RequestOptions options = new RequestOptions().override(DensityUtil.dip2px(mContext,60),DensityUtil.dip2px(mContext,60))
            .centerCrop().placeholder(R.drawable.def_turn);

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder1){
            NewsModel newsModel = newsModelList.get(position);
            MyViewHolder1 holder1 = (MyViewHolder1) holder;
            String content = newsModel.getContent();
            if (content.length()>15){
                content = content.substring(0,15);
            }
            content = content + "...";
            holder1.content.setText(content);

            String title = newsModel.getTitle();
            if (title.length()>10){
                title = title.substring(0,10) + "...";
            }
            holder1.title.setText(title);
            if (!NfuResource.getInstance().isUseDefPic()){
                Glide.with(mContext).load(newsModel.getPicurl()).apply(options).into(holder1.pic);
            }else {
                Glide.with(mContext).load(R.drawable.def_turn).apply(options).into(holder1.pic);
            }
        }else if (holder instanceof MyViewHolder2){
            NewsModel newsModel = newsModelList.get(position);
            MyViewHolder2 holder2 = (MyViewHolder2) holder;
            String title = newsModel.getTitle();
            if (title.length()>15){
                title = title.substring(0,15) + "...";
            }
            holder2.title.setText(title);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (newsModelList!=null){
            NewsModel newsModel = newsModelList.get(position);
            if (TextUtils.isEmpty(newsModel.getPicurl())){
                return NORMAL;
            }else {
                return WITH_PIC;
            }
        }
        return super.getItemViewType(position);
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
        TextView title;
        ImageView arrow;
        TextView content;
        ImageView pic;

        public MyViewHolder1(View itemView) {
            super(itemView);
            card_view = (CardView) itemView.findViewById(R.id.card_view);
            title = (TextView) itemView.findViewById(R.id.tv_title);
            content = (TextView) itemView.findViewById(R.id.tv_content);
            pic = (ImageView) itemView.findViewById(R.id.news_pic);
            arrow = (ImageView) itemView.findViewById(R.id.right_arrow);

            card_view.setOnClickListener(new View.OnClickListener() {
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

    private class MyViewHolder2 extends RecyclerView.ViewHolder{
        TextView title;
        ImageView arrow;
        CardView card_view;

        public MyViewHolder2(View itemView) {
            super(itemView);
            card_view = (CardView) itemView.findViewById(R.id.card_view);
            title = (TextView) itemView.findViewById(R.id.tv_title);
            arrow = (ImageView) itemView.findViewById(R.id.right_arrow);

            card_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (iOnDetailListener!=null){
                        iOnDetailListener.onDetailListener(newsModelList.get(getAdapterPosition()-1));
                    }
                }
            });
        }
    }

    public interface IOnDetailListener{
        void onDetailListener(NewsModel model);
    }
}
