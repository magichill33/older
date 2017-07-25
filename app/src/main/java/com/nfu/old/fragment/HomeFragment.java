package com.nfu.old.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.nfu.old.R;
import com.nfu.old.adapter.HotAdPagerAdapter;
import com.nfu.old.config.NfuResource;
import com.nfu.old.manager.ApiManager;
import com.nfu.old.model.TurnPicModel;
import com.nfu.old.utils.LogUtil;
import com.nfu.old.utils.NetUtil;
import com.nfu.old.view.PointPagerIndicator;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;

import static android.R.attr.data;

public class HomeFragment extends Fragment {

    Unbinder unbinder;

    @BindView(R.id.home_fragment_viewpager)
    ViewPager mViewPager;

    @BindView(R.id.nfu_hot_list_ad_indicator)
    PointPagerIndicator pointPagerIndicator;

    int adIndex = 0;
    private Handler MyHandle = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(mViewPager != null && mViewPager.getAdapter() != null && mViewPager.getAdapter().getCount() != 0){
                mViewPager.setCurrentItem(adIndex);
            }
            adIndex++;
            if(adIndex == mViewPager.getAdapter().getCount() - 1){
                adIndex = 0;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home_fragment, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initPager();
        loadData();
        return rootView;
    }

    private void initPager(){
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                adIndex = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    RequestOptions options = new RequestOptions()
            .centerCrop().placeholder(R.drawable.def_turn);

    private void loadData(){
        ApiManager.getInstance().getTurnPic("1008", new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtil.i("HomeFragment--->loadData--->onError--->"+e);
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtil.i("HomeFragment--->loadData--->onResponse--->"+response);
                TurnPicModel turnPicModel = new Gson().fromJson(response,TurnPicModel.class);
                LogUtil.i("HomeFragment--->loadData--->TurnPicModel--->"+turnPicModel);
                List<TurnPicModel.StrResultBean> pics = turnPicModel.getStrResult();
                ArrayList<ImageView> ads = new ArrayList<ImageView>();
                if (pics!=null&&pics.size()>0&& !NfuResource.getInstance().isUseDefPic()){
                    for (int i = 0; i < pics.size(); i++) {
                        ImageView imageView = new ImageView(getContext());
                        Glide.with(HomeFragment.this).load(pics.get(i).getPicurl()).apply(options).into(imageView);
                        ads.add(imageView);
                    }
                    if(ads.size() == 2){
                        pointPagerIndicator.setIsTwoPage(true);
                    } else {
                        pointPagerIndicator.setIsTwoPage(false);
                    }
                }else {
                    ImageView imageView = new ImageView(getContext());
                    Glide.with(getContext()).load(R.drawable.def_turn).into(imageView);
                    ads.add(imageView);
                }

                HotAdPagerAdapter adPagerAdapter = new HotAdPagerAdapter(new HotAdPagerAdapter.AdItemOnClickListener() {
                    @Override
                    public void viewPagerItemOnClickListener(int position) {
                        LogUtil.d("viewPagerItemOnClickListener:position:"+position);

                    }
                });
                adPagerAdapter.setData(ads);
                mViewPager.setAdapter(adPagerAdapter);
                pointPagerIndicator.setViewPager(mViewPager);
                if(pics != null && pics.size() != 0){
                    int mid = adPagerAdapter.getCount() / 2;
                    pointPagerIndicator.setCurrentItem(mid - mid/pics.size(), false);
                }

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
