package com.nfu.old.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
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
import com.nfu.old.config.NfuResource;
import com.nfu.old.fragment.AnnouncementFragment;
import com.nfu.old.fragment.OldConsultFragment;
import com.nfu.old.fragment.OldServiceFragment;
import com.nfu.old.fragment.PolicyFragment;
import com.nfu.old.fragment.SafeguardFragment;
import com.nfu.old.fragment.TransactionFragment;
import com.nfu.old.model.NewsModel;
import com.nfu.old.utils.DensityUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017-7-26.
 */

public class HotListAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<NewsModel> newsModelList;
    private IOnDetailListener iOnDetailListener;
    CardView card_view;
    /*private final int WITH_PIC = 10;
    private final int NORMAL = 20;*/
    private int ACTION_ITEM_TYPE = 1;
    private int HOUSE_ITEM_TYPE = 2;

    public HotListAdapter(Context mContext, List<NewsModel> newsModelList, IOnDetailListener iOnDetailListener) {
        this.mContext = mContext;
        this.newsModelList = newsModelList;
        this.iOnDetailListener = iOnDetailListener;
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ACTION_ITEM_TYPE){
            View view = LayoutInflater.from(mContext).inflate(R.layout.news_grid_item,parent,false);
            return new GridViewHolder(view);
        }else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.news_item3,parent,false);
            return new MyViewHolder(view);
        }

    }

    RequestOptions options = new RequestOptions().override(DensityUtil.dip2px(mContext,60),DensityUtil.dip2px(mContext,60))
            .centerCrop().placeholder(R.drawable.def_turn);

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder){
            NewsModel newsModel = newsModelList.get(position);
            MyViewHolder holder1 = (MyViewHolder) holder;
            String content = newsModel.getContent();
            content = content.replace("&nbsp;"," ");
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
    }

    @Override
    public int getItemCount() {
        if (newsModelList == null){
            return 0;
        }
        return newsModelList.size();

    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {

            case 0:
                return ACTION_ITEM_TYPE;
            default:
                return HOUSE_ITEM_TYPE;
        }
    }

    private class MyViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        ImageView arrow;
        TextView content;
        CardView card_view;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.tv_title);
            content = (TextView) itemView.findViewById(R.id.tv_content);
            arrow = (ImageView) itemView.findViewById(R.id.right_arrow);
            card_view = (CardView) itemView.findViewById(R.id.card_view);

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

    public class GridViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.ll_policy)
        LinearLayout ll_policy;
        @BindView(R.id.ll_transaction_query)
        LinearLayout ll_transaction_query;
        @BindView(R.id.ll_rights)
        LinearLayout ll_rights;
        @BindView(R.id.ll_announcement)
        LinearLayout ll_announcement;
        @BindView(R.id.ll_oldservice)
        LinearLayout ll_oldservice;
        @BindView(R.id.ll_oldconsult)
        LinearLayout ll_oldconsult;

        public GridViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            ll_policy.setOnClickListener(this);
            ll_transaction_query.setOnClickListener(this);
            ll_rights.setOnClickListener(this);
            ll_announcement.setOnClickListener(this);
            ll_oldservice.setOnClickListener(this);
            ll_oldconsult.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ll_policy:
                    PolicyFragment policyFragment = new PolicyFragment();
                    if (iOnDetailListener!=null){
                        iOnDetailListener.gotoFragment(policyFragment);
                    }
                    break;
                case R.id.ll_transaction_query:
                    TransactionFragment transactionFragment = new TransactionFragment();
                    if (iOnDetailListener!=null){
                        iOnDetailListener.gotoFragment(transactionFragment);
                    }
                    break;
                case R.id.ll_rights:
                    SafeguardFragment safeguardFragment = new SafeguardFragment();
                    if (iOnDetailListener!=null){
                        iOnDetailListener.gotoFragment(safeguardFragment);
                    }
                    break;
                case R.id.ll_announcement:
                    AnnouncementFragment announcementragment = new AnnouncementFragment();
                    if (iOnDetailListener!=null){
                        iOnDetailListener.gotoFragment(announcementragment);
                    }
                    break;
                case R.id.ll_oldconsult:
                    //  DistrictFragment districtFragment = new DistrictFragment();
                    OldConsultFragment OldConsultFragment =new OldConsultFragment();
                    if (iOnDetailListener!=null){
                        iOnDetailListener.gotoFragment(OldConsultFragment);
                    }
                    break;
                case R.id.ll_oldservice:

                    OldServiceFragment oldServiceFragment = new OldServiceFragment();
                    if (iOnDetailListener!=null){
                        iOnDetailListener.gotoFragment(oldServiceFragment);
                    }

                    break;
            }
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
        void gotoFragment(Fragment fragment);
    }
}
