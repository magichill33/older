package com.nfu.old.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.nfu.old.R;
import com.nfu.old.activity.RoutePlanActivity;
import com.nfu.old.config.NfuResource;
import com.nfu.old.model.OldServiceModel;
import com.nfu.old.utils.AppUtils;
import com.nfu.old.utils.DensityUtil;

import java.text.DecimalFormat;
import java.util.List;

import static com.nfu.old.R.id.businessAddress;
import static com.nfu.old.R.id.shopManager;
import static com.nfu.old.R.id.shopName;
import static com.nfu.old.R.id.shopType;
import static com.nfu.old.R.id.textView;

/**
 * Created by Administrator on 2017-7-26.
 */

public class OldServiceListAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<OldServiceModel> newsModelList;
    private IOnDetailListener iOnDetailListener;
    private final int WITH_PIC = 10;
    private final int NORMAL = 20;

    public OldServiceListAdapter(Context mContext, List<OldServiceModel> newsModelList, IOnDetailListener iOnDetailListener) {
        this.mContext = mContext;
        this.newsModelList = newsModelList;
        this.iOnDetailListener = iOnDetailListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        View view = LayoutInflater.from(mContext).inflate(R.layout.oldservice_list_item,parent,false);
        viewHolder = new MyViewHolder1(view);
        return viewHolder;
    }

    RequestOptions options = new RequestOptions().override(DensityUtil.dip2px(mContext,75),DensityUtil.dip2px(mContext,75))
            .centerCrop().placeholder(R.drawable.def_turn);

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //    TextView list_item_institutionName,list_item_contactPhone_tv,list_item_officeHours_tv,list_item_address;
        //ImageView shoppic,jinru,call;
        OldServiceModel oldServiceModel = newsModelList.get(position);
        MyViewHolder1 holder1 = (MyViewHolder1) holder;
        String institutionName = oldServiceModel.getInstitutionName();
        holder1.list_item_institutionName.setText(institutionName);

        String contactPhone = oldServiceModel.getContactPhone();
        holder1.list_item_contactPhone_tv.setText(contactPhone);


        String officeHours = oldServiceModel.getOfficeHours();
        if(!TextUtils.isEmpty(officeHours)){
            holder1.list_item_officeHours_tv.setText(officeHours);
        }else {
            holder1.rl_officeHours.setVisibility(View.GONE);
        }


        String address = oldServiceModel.getAddress();
        holder1.list_item_address.setText(address);

        String distance = oldServiceModel.getDistance();
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

//        if (!NfuResource.getInstance().isUseDefPic()){
//            Glide.with(mContext).load(newsModel.getShopPic()).apply(options).into(holder1.shoppic);
//        }else {
            Glide.with(mContext).load(R.drawable.def_turn).apply(options).into(holder1.shoppic);
//        }
    }

    @Override
    public int getItemViewType(int position) {
        if (newsModelList!=null){
            OldServiceModel newsModel = newsModelList.get(position);
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
        TextView list_item_institutionName,list_item_contactPhone_tv,list_item_officeHours_tv,list_item_address,loction_tv;
        ImageView shoppic,jinru,call;
        LinearLayout location;
        RelativeLayout rl_officeHours;

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
             location = (LinearLayout)itemView.findViewById(R.id.loction_icon);
            call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 调用打掉话界面
                    AppUtils.call(v.getContext(),newsModelList.get(getAdapterPosition()).getContactPhone());
                }
            });
            //条用百度地图进行导航
            location.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext,RoutePlanActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("OldServiceModel", newsModelList.get(getAdapterPosition()));
//                    intent.putExtra("OldServiceModel", newsModelList.get(getAdapterPosition());
//                    bundle.putS
//                    intent.putExtras(bundle);
                    intent.putExtra("latitude", newsModelList.get(getAdapterPosition()).getLatitude());
                    intent.putExtra("longitude", newsModelList.get(getAdapterPosition()).getLongitude());

                    mContext.startActivity(intent);
                }
            });

       /*     itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (iOnDetailListener!=null){
                        iOnDetailListener.onDetailListener(newsModelList.get(getAdapterPosition()));
                    }
                }
            });*/

            list_item_institutionName = (TextView)itemView.findViewById(R.id.list_item_institutionName);
            list_item_contactPhone_tv = (TextView)itemView.findViewById(R.id.list_item_contactPhone_tv);
            list_item_officeHours_tv = (TextView)itemView.findViewById(R.id.list_item_officeHours_tv);
            rl_officeHours = (RelativeLayout)itemView.findViewById(R.id.rl_officeHours);
            list_item_address = (TextView)itemView.findViewById(R.id.list_item_address);
            loction_tv = (TextView)itemView.findViewById(R.id.loction_tv);
//            list_item_institutionName .setEllipsize(TextUtils.TruncateAt.MARQUEE);
//            list_item_institutionName .setSingleLine(true);
//            list_item_institutionName .setMarqueeRepeatLimit(6);
//            list_item_institutionName.requestFocus();
//            list_item_address .setEllipsize(TextUtils.TruncateAt.MARQUEE);
//            list_item_address .setSingleLine(true);
//            list_item_address .setMarqueeRepeatLimit(6);
//            list_item_address.requestFocus();

        }
    }

    public void setNewsData(List<OldServiceModel> newsData){
        newsModelList = newsData;
        notifyDataSetChanged();
    }

    public void addNewsData(List<OldServiceModel> data) {
        if (newsModelList!=null){
            newsModelList.addAll(data);
        }else {
            newsModelList = data;
        }

        notifyDataSetChanged();
    }

    public interface IOnDetailListener{
        void onDetailListener(OldServiceModel model);
    }
}
