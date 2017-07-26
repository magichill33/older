package com.nfu.old.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nfu.old.R;
import com.nfu.old.model.NewsModel;

import java.util.List;

/**
 * Created by Administrator on 2017-7-26.
 */

public class PolicyListAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<NewsModel> newsModelList;
    private IOnDetailListener iOnDetailListener;

    public PolicyListAdapter(Context mContext, List<NewsModel> newsModelList, IOnDetailListener iOnDetailListener) {
        this.mContext = mContext;
        this.newsModelList = newsModelList;
        this.iOnDetailListener = iOnDetailListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    private class MyViewHolder1 extends RecyclerView.ViewHolder{
        TextView title;
        ImageView arrow;
        TextView content;
        ImageView pic;

        public MyViewHolder1(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.tv_title);
            content = (TextView) itemView.findViewById(R.id.tv_content);
            pic = (ImageView) itemView.findViewById(R.id.news_pic);
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

    public interface IOnDetailListener{
        void onDetailListener(NewsModel model);
    }
}
