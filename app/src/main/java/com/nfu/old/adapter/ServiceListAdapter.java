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
import com.nfu.old.model.ServiceModel;
import com.nfu.old.utils.AppUtils;
import com.nfu.old.utils.DensityUtil;

import java.text.DecimalFormat;
import java.util.List;

import static com.nfu.old.R.id.loction_tv;

/**
 * Created by Administrator on 2017-7-26.
 */

public class ServiceListAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<ServiceModel> newsModelList;
    private IOnDetailListener iOnDetailListener;
    private final int WITH_PIC = 10;
    private final int NORMAL = 20;

    public ServiceListAdapter(Context mContext, List<ServiceModel> newsModelList, IOnDetailListener iOnDetailListener) {
        this.mContext = mContext;
        this.newsModelList = newsModelList;
        this.iOnDetailListener = iOnDetailListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        View view = LayoutInflater.from(mContext).inflate(R.layout.service_list_item,parent,false);
        viewHolder = new MyViewHolder1(view);
        return viewHolder;
    }

    RequestOptions options = new RequestOptions().override(DensityUtil.dip2px(mContext,75),DensityUtil.dip2px(mContext,75))
            .centerCrop().placeholder(R.drawable.def_turn);

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //list_item_shopname,list_item_shoptype_tv,list_item_shoptelephone_tv,list_item_shopmanager_tv,list_item_businessaddress,loction_tv;
        ServiceModel newsModel = newsModelList.get(position);
        MyViewHolder1 holder1 = (MyViewHolder1) holder;
        String shopName = newsModel.getShopName();
        holder1.list_item_shopname.setText(shopName);

        String shopType = newsModel.getShopType();
        holder1.list_item_shoptype_tv.setText(shopType);


        String shopManager = newsModel.getShopManager();
        holder1.list_item_shopmanager_tv.setText(shopManager);

        String shopTelephone = newsModel.getShopTelephone();
        holder1.list_item_shoptelephone_tv.setText(shopTelephone);

        String businessAddress = newsModel.getBusinessAddress();
        holder1.list_item_businessaddress.setText(businessAddress);

        String distance = newsModel.getDistance();
      //  距您266m
        if(!TextUtils.isEmpty(distance)) {
            int dist = Integer.valueOf(distance);
            if(dist >=1000) {
                float dist1 = ( (float)dist/ (float) 1000.0);
                DecimalFormat df2  = new DecimalFormat("###.0");
                String distance1 =String.valueOf( df2.format(dist1));
//                char c = distance.charAt(distance.length() - 1);
//                if(distance1.charAt(distance1.length()-1)==48 ){
//
//                    String distance2 = distance.substring(0, distance1.length() - 2);
//                    holder1.loction_tv.setText("距您"+ distance2 +"km");
//                }else {
//                    holder1.loction_tv.setText("距您"+ distance1 +"km");
//                }
                holder1.loction_tv.setText("距您"+ distance1 +"km");
            }else if(dist > 0 && dist <1000){
                holder1.loction_tv.setText("距您"+dist+"m");
            }
        }else {
            holder1.loction_tv.setText("未知位置");
        }

//        holder1.loction_tv.setVisibility(View.INVISIBLE);

        if (!NfuResource.getInstance().isUseDefPic()){
            Glide.with(mContext).load(newsModel.getShopPic()).apply(options).into(holder1.shoppic);
        }else {
            Glide.with(mContext).load(R.drawable.def_turn).apply(options).into(holder1.shoppic);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (newsModelList!=null){
            ServiceModel newsModel = newsModelList.get(position);
          /*  if (TextUtils.isEmpty(newsModel.getPicurl())){
                return NORMAL;
            }else {
                return WITH_PIC;
            }*/
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
        TextView list_item_shopname,list_item_shoptype_tv,list_item_shoptelephone_tv,list_item_shopmanager_tv,list_item_businessaddress,loction_tv;
        ImageView shoppic,jinru,call,location;

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
             shoppic = (ImageView)itemView.findViewById(R.id.list_item_shoppic);
             jinru = (ImageView)itemView.findViewById(R.id.list_item_jinru);
             call = (ImageView)itemView.findViewById(R.id.list_item_call);
             location = (ImageView)itemView.findViewById(R.id.loction_icon);
            call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 调用打掉话界面
                    AppUtils.call(v.getContext(),newsModelList.get(getAdapterPosition()).getShopTelephone());
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (iOnDetailListener!=null){
                        iOnDetailListener.onDetailListener(newsModelList.get(getAdapterPosition()));
                    }
                }
            });

            loction_tv = (TextView)itemView.findViewById(R.id.loction_tv);
            list_item_shopname = (TextView)itemView.findViewById(R.id.list_item_shopname);
            list_item_shoptype_tv = (TextView)itemView.findViewById(R.id.list_item_shoptype_tv);
            list_item_shoptelephone_tv = (TextView)itemView.findViewById(R.id.list_item_shoptelephone_tv);
            list_item_shopmanager_tv = (TextView)itemView.findViewById(R.id.list_item_shopmanager_tv);
            list_item_businessaddress = (TextView)itemView.findViewById(R.id.list_item_businessaddress);
        }
    }

    public void setNewsData(List<ServiceModel> newsData){
        newsModelList = newsData;
        notifyDataSetChanged();
    }

    public void addNewsData(List<ServiceModel> data) {
        if (newsModelList!=null){
            newsModelList.addAll(data);
        }else {
            newsModelList = data;
        }

        notifyDataSetChanged();
    }

    public interface IOnDetailListener{
        void onDetailListener(ServiceModel model);
    }
}
