package com.nfu.old.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;


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
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;

import static android.R.attr.breadCrumbShortTitle;
import static android.R.attr.data;

public class HomeFragment extends Fragment implements View.OnClickListener{

    Unbinder unbinder;

    @BindView(R.id.home_fragment_viewpager)
    ViewPager mViewPager;
    @BindView(R.id.nfu_hot_list_ad_indicator)
    PointPagerIndicator pointPagerIndicator;

    @BindView(R.id.ll_policy)
    LinearLayout ll_policy;
    @BindView(R.id.ll_transaction_query)
    LinearLayout ll_transaction_query;
    @BindView(R.id.ll_rights)
    LinearLayout ll_rights;
    @BindView(R.id.ll_announcement)
    LinearLayout ll_announcement;
    @BindView(R.id.ll_socialwork)
    LinearLayout ll_socialwork;
    @BindView(R.id.ll_dynamic)
    LinearLayout ll_dynamic;

    private Timer mTimer;
    private TimerTask mTimerTask;

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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.i("HomeFragment--->onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home_fragment, container, false);
        LogUtil.i("HomeFragment--->onCreateView");
        unbinder = ButterKnife.bind(this, rootView);
        initPager();
        initEvents();
        loadData();
        return rootView;
    }

    private void initEvents(){
        ll_policy.setOnClickListener(this);
        ll_transaction_query.setOnClickListener(this);
        ll_rights.setOnClickListener(this);
        ll_announcement.setOnClickListener(this);
        ll_socialwork.setOnClickListener(this);
        ll_dynamic.setOnClickListener(this);
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

                if (ads.size()<2){
                    pointPagerIndicator.setVisibility(View.INVISIBLE);
                }else {
                    startAdTimer();
                }

            }
        });
    }

    public void startAdTimer(){
        if(mTimerTask == null){
            mTimer = new Timer();
            mTimerTask = new TimerTask() {
                @Override
                public void run() {
                    MyHandle.sendEmptyMessage(0);
                }
            };
            mTimer.schedule(mTimerTask, 5000, 5000);
        }
    }
    public void stopAdTimer(){
        if(mTimer != null){
            mTimer.cancel();
            mTimer = null;
        }
        if(mTimerTask != null){
            mTimerTask.cancel();
            mTimerTask =null;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        stopAdTimer();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_policy:
                PolicyFragment policyFragment = new PolicyFragment();
                gotoFragment(policyFragment);
                break;
            case R.id.ll_transaction_query:
                break;
            case R.id.ll_rights:
                break;
            case R.id.ll_announcement:
                break;
            case R.id.ll_dynamic:
                break;
            case R.id.ll_socialwork:
                break;
        }
    }

    private void gotoFragment(Fragment fragment){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.activity_main_content_frameLayout, fragment);
        fragmentTransaction.commit();
    }
}
