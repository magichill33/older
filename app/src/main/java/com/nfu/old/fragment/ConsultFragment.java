package com.nfu.old.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.nfu.old.R;
import com.nfu.old.adapter.ConsultListAdapter;
import com.nfu.old.adapter.HotAdPagerAdapter;
import com.nfu.old.adapter.ServiceListAdapter;
import com.nfu.old.config.NfuResource;
import com.nfu.old.manager.ApiManager;
import com.nfu.old.model.NewsListModel;
import com.nfu.old.model.NewsModel;
import com.nfu.old.model.TurnPicModel;
import com.nfu.old.utils.LogUtil;
import com.nfu.old.view.ButtonExtendM;
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

/**
 * Created by Administrator on 2017/7/25.
 * 咨询页面
 */

public class ConsultFragment extends Fragment  implements View.OnClickListener {
    Unbinder unbinder;
    @BindView(R.id.consult_fragment_viewpager)
    ViewPager mViewPager;
    @BindView(R.id.nfu_hot_list_ad_indicator)
    PointPagerIndicator pointPagerIndicator;
    @BindView(R.id.consult_recyclerview)
    RecyclerView consult_recyclerview;




    @BindView(R.id.fragment_consult_call_ib)
    ImageView activity_main_call_ib;
    @BindView(R.id.fragment_consult_setting_ib)
    ImageView activity_main_setting_ib;

    private ConsultListAdapter policyListAdapter;
    private Timer mTimer;
    private TimerTask mTimerTask;

    RequestOptions options = new RequestOptions()
            .centerCrop().placeholder(R.drawable.def_turn);
    int adIndex = 0;
    private Handler MyHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (mViewPager != null && mViewPager.getAdapter() != null && mViewPager.getAdapter().getCount() != 0) {
                mViewPager.setCurrentItem(adIndex);
            }
            adIndex++;
            if (adIndex == mViewPager.getAdapter().getCount() - 1) {
                adIndex = 0;
            }
        }
    };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.consult_fragment, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initView();
        initPager();
        loadData();
        return rootView;
    }



    protected void initView() {
        final String[] mTitles  = {"53家老龄委成员单位","16区老龄办电话","市级老龄工作机构"};
        int[] mIconId = {R.drawable.consult_qujidongtai_bg,R.drawable.consult_zhengcejiedu,R.drawable.consult_meitibaodao_bg};
        consult_recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        policyListAdapter = new ConsultListAdapter(getContext(), mTitles, mIconId,new ConsultListAdapter.IOnDetailListener() {

            @Override
            public void onDetailListener(String title) {
                if ("53家老龄委成员单位".equals(title)){
                    OlderListFragment olderListFragment = new OlderListFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("typeId",1);
                    bundle.putString("title",mTitles[0]);
                    olderListFragment.setArguments(bundle);
                    gotoFragment(olderListFragment);
                }else if ("16区老龄办电话".equals(title)){
                    OlderListFragment olderListFragment = new OlderListFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("typeId",2);
                    bundle.putString("title",mTitles[1]);
                    olderListFragment.setArguments(bundle);
                    gotoFragment(olderListFragment);
                }else if ("市级老龄工作机构".equals(title)){
                    OlderWorkListFragment olderWorkListFragment = new OlderWorkListFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("typeId",3);
                    bundle.putString("title",mTitles[2]);
                    olderWorkListFragment.setArguments(bundle);
                    gotoFragment(olderWorkListFragment);
                }
            }
        });
        consult_recyclerview.setAdapter(policyListAdapter);

        activity_main_call_ib.setOnClickListener(this);
        activity_main_setting_ib.setOnClickListener(this);
    }

    private void gotoFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.hide(this);
        fragmentTransaction.add(R.id.activity_main_content_frameLayout , fragment);
        fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.replace(R.id.activity_main_content_frameLayout, fragment);
        fragmentTransaction.commit();
    }

    private void initPager() {
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
    private void loadData() {
        ApiManager.getInstance().getTurnPic("1008", new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtil.i("HomeFragment--->loadData--->onError--->" + e);
            }

            @Override
            public void onResponse(String response, int id) {
                if (getContext()!=null){
                    LogUtil.i("HomeFragment--->loadData--->onResponse--->" + response);
                    try {
                        TurnPicModel turnPicModel = new Gson().fromJson(response, TurnPicModel.class);
                        LogUtil.i("HomeFragment--->loadData--->TurnPicModel--->" + turnPicModel);
                        final List<TurnPicModel.StrResultBean> pics = turnPicModel.getStrResult();
                        ArrayList<ImageView> ads = new ArrayList<ImageView>();
                        if (pics != null && pics.size() > 0 && !NfuResource.getInstance().isUseDefPic()) {
                            for (int i = 0; i < pics.size(); i++) {
                                ImageView imageView = new ImageView(getContext());
                                Glide.with(ConsultFragment.this).load(pics.get(i).getPicurl()).apply(options).into(imageView);
                                ads.add(imageView);
                            }
                            if (ads.size() == 2) {
                                pointPagerIndicator.setIsTwoPage(true);
                            } else {
                                pointPagerIndicator.setIsTwoPage(false);
                            }
                        } else {
                            ImageView imageView = new ImageView(getContext());
                            Glide.with(getContext()).load(R.drawable.def_turn).into(imageView);
                            ads.add(imageView);
                        }

                        HotAdPagerAdapter adPagerAdapter = new HotAdPagerAdapter(new HotAdPagerAdapter.AdItemOnClickListener() {
                            @Override
                            public void viewPagerItemOnClickListener(int position) {
                                LogUtil.d("viewPagerItemOnClickListener:position:" + position);
                                TurnPicModel.StrResultBean model = pics.get(position);
                                gotoDetailFragment(model.getId());
                            }
                        });


                        adPagerAdapter.setData(ads);
                        mViewPager.setAdapter(adPagerAdapter);
                        pointPagerIndicator.setViewPager(mViewPager);
                        if (pics != null && pics.size() != 0) {
                            int mid = adPagerAdapter.getCount() / 2;
                            pointPagerIndicator.setCurrentItem(mid - mid / pics.size(), false);
                        }

                        if (ads.size() < 2) {
                            pointPagerIndicator.setVisibility(View.INVISIBLE);
                        } else {
                            startAdTimer();
                        }

                    }catch (Exception e){
                        LogUtil.i("HomeFragment--->loadData--->Exception--->"+e);
                    }

                }

            }
        });
    }

    private void gotoDetailFragment(String id){
        ApiManager.getInstance().getNewsDetail(id, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtil.i("PolicyFragment--->initView--->getNewsDetail--->onError::"+e);
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtil.i("PolicyFragment--->initView--->getNewsDetail--->onResponse::"+response);
                NewsListModel listModel = new Gson().fromJson(response,NewsListModel.class);
                LogUtil.i("PolicyFragment--->initView--->getNewsDetail--->NewsListModel::"+listModel);
                NewsModel model1 = new Gson().fromJson(listModel.getStrResult(),NewsModel.class);
                LogUtil.i("PolicyFragment--->initView--->getNewsDetail--->NewsModel::"+model1);
                gotoDetailFragment(model1);
            }
        });
    }

    private void gotoDetailFragment(NewsModel newsModel){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        NewsDetailFragment newsDetailFragment = new NewsDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title","媒体报道");
        bundle.putSerializable("news",newsModel);
        newsDetailFragment.setArguments(bundle);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.activity_main_content_frameLayout, newsDetailFragment);
        fragmentTransaction.commit();
    }

    public void startAdTimer() {
        if (mTimerTask == null) {
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

    public void stopAdTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
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
            case R.id.fragment_consult_call_ib:
                HotLineFragment hotLineFragment = new HotLineFragment();
                Bundle bundle = new Bundle();
                bundle.putString("title",getString(R.string.consult_fragment_top_title));
                hotLineFragment.setArguments(bundle);
                gotoFragment(hotLineFragment);
                break;
            case R.id.fragment_consult_setting_ib:
                SettingFragment settingFragment = new SettingFragment();
                gotoFragment(settingFragment);
                break;
        }
    }
}
