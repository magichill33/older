package com.nfu.old.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.nfu.old.R;
import com.nfu.old.adapter.HotAdPagerAdapter;
import com.nfu.old.adapter.NewsListAdapter;
import com.nfu.old.config.NfuResource;
import com.nfu.old.manager.ApiManager;
import com.nfu.old.model.NewsListModel;
import com.nfu.old.model.NewsModel;
import com.nfu.old.model.NewsModels;
import com.nfu.old.model.TurnPicModel;
import com.nfu.old.utils.AppUtils;
import com.nfu.old.utils.LogUtil;
import com.nfu.old.utils.SharedPreferencesManager;
import com.nfu.old.view.ContactCallDailog;
import com.nfu.old.view.ContactMsgWindow;
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

public class HomeFragment extends Fragment implements View.OnClickListener {

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
    @BindView(R.id.ll_oldservice)
    LinearLayout ll_oldservice;
    @BindView(R.id.ll_oldconsult)
    LinearLayout ll_oldconsult;

    @BindView(R.id.news_recyclerview)
    XRecyclerView news_recyclerview;

    private NewsListAdapter newsListAdapter;

    private final static int REFRESH_TYPE = 1001;
    private final static int LOADMORE_TYPE = 1002;
    private int n_currentPage = 0;
    private int n_iRecordCount = 0;
    private static final int PAGESIZE = 5;

    @BindView(R.id.root)
    ScrollView rootView;

    @BindView(R.id.activity_main_call_ib)
    ImageView activity_main_call_ib;
    @BindView(R.id.activity_main_setting_ib)
    ImageView activity_main_setting_ib;


    private Timer mTimer;
    private TimerTask mTimerTask;

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
    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        Log.e("HomeFragment", "HomeFragment **** onAttach...");
        super.onAttach(activity);
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.e("HomeFragment", "HomeFragment **** onCreate...");
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e("HomeFragment", "HomeFragment **** onCreateView...");
        if(rootView== null) {
            View rootView = inflater.inflate(R.layout.home_fragment, container, false);
            unbinder = ButterKnife.bind(this, rootView);
        }

        initPager();
        initEvents();
        loadData();
        return rootView;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e("HomeFragment", "HomeFragment **** onActivityCreated...");
    }
    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        Log.e("HomeFragment", "HomeFragment **** onStart...");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.e("HomeFragment", "HomeFragment **** onResume...");
        // TODO Auto-generated method stub
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.e("HomeFragment", "HomeFragment **** onPause...");
        // TODO Auto-generated method stub
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.e("HomeFragment", "HomeFragment **** onStop...");
        // TODO Auto-generated method stub
        super.onStop();
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        Log.e("HomeFragment", "HomeFragment **** onDestroy...");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        Log.e("HomeFragment", "HomeFragment **** onDetach...");
        super.onDetach();
    }
    /*private void initData() {
        chageMsg(1);
        chageMsg(2);
        chageMsg(3);
        chageMsg(4);

    }*/

    private void initEvents() {
        ll_policy.setOnClickListener(this);
        ll_transaction_query.setOnClickListener(this);
        ll_rights.setOnClickListener(this);
        ll_announcement.setOnClickListener(this);
        ll_oldservice.setOnClickListener(this);
        ll_oldconsult.setOnClickListener(this);

        activity_main_call_ib.setOnClickListener(this);
        activity_main_setting_ib.setOnClickListener(this);

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

        news_recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        news_recyclerview.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        news_recyclerview.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        newsListAdapter = new NewsListAdapter(getContext(), null, new NewsListAdapter.IOnDetailListener() {
            @Override
            public void onDetailListener(NewsModel model) {
                gotoDetailFragment(model.getId());
            }
        });
        //news_recyclerview.addItemDecoration(new MyItemDecoration(getContext(),MyItemDecoration.VERTICAL_LIST));
        news_recyclerview.setAdapter(newsListAdapter);
        news_recyclerview.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                LogUtil.i("news_recyclerview--->onRefresh");
                getNormalList(0, 0, REFRESH_TYPE);


            }

            @Override
            public void onLoadMore() {
                LogUtil.i("news_recyclerview--->onLoadMore");
                getNormalList(n_currentPage, n_iRecordCount, LOADMORE_TYPE);
            }
        });

    }

    RequestOptions options = new RequestOptions()
            .centerCrop().placeholder(R.drawable.def_turn);

    private void loadData() {
        ApiManager.getInstance().getTurnPic("1008", new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtil.i("HomeFragment--->loadData--->onError--->" + e);
            }

            @Override
            public void onResponse(String response, int id) {
                if(getContext()!=null){
                    LogUtil.i("HomeFragment--->loadData--->onResponse--->" + response);
                    try {
                        TurnPicModel turnPicModel = new Gson().fromJson(response, TurnPicModel.class);
                        LogUtil.i("HomeFragment--->loadData--->TurnPicModel--->" + turnPicModel);
                        final List<TurnPicModel.StrResultBean> pics = turnPicModel.getStrResult();
                        final ArrayList<ImageView> ads = new ArrayList<ImageView>();
                        if (pics != null && pics.size() > 0 && !NfuResource.getInstance().isUseDefPic()) {
                            for (int i = 0; i < pics.size(); i++) {
                                ImageView imageView = new ImageView(getContext());
                                Glide.with(HomeFragment.this).load(pics.get(i).getPicurl()).apply(options).into(imageView);
                                ads.add(imageView);
                            }

                            if (ads.size() == 2) {
                                pointPagerIndicator.setIsTwoPage(true);
                            } else {
                                Log.e("HomeFragment", "pointPagerIndicator **** pointPagerIndicator..." +pointPagerIndicator);
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

        getNormalList(0,0,REFRESH_TYPE);

    }

    private void getNormalList(int currentPage, int iRecordCount, final int type) {
        ApiManager.getInstance().getAllNewsList(PAGESIZE, currentPage, iRecordCount, "createdate", "desc", new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtil.i("AnnouncementFragment--->getNormalList--->getNewsList--->onError::" + e);
                if (type == REFRESH_TYPE) {
                    news_recyclerview.refreshComplete();
                } else {
                    news_recyclerview.loadMoreComplete();

                }
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtil.i("AnnouncementFragment--->getNormalList--->getNewsList--->onResponse::" + response);
                NewsListModel newsListModel = new Gson().fromJson(response, NewsListModel.class);
                LogUtil.i("AnnouncementFragment--->getNormalList--->getNewsList--->newsListModel::" + newsListModel);
                NewsModels newsModels = new Gson().fromJson(newsListModel.getStrResult(), NewsModels.class);
                LogUtil.i("AnnouncementFragment--->getNormalList--->getNewsList--->NewsModels::" + newsModels);
                if (type == REFRESH_TYPE) {
                    if (newsListModel != null&&newsModels!=null) {
                        n_currentPage = newsModels.getCurrentPage();
                        n_currentPage++;
                        n_iRecordCount = newsModels.getRecordCount();
                    }
                    newsListAdapter.setNewsData(newsModels.getData());
                    news_recyclerview.refreshComplete();
                } else {

                    if (newsListModel != null) {
                        if (n_currentPage <= newsModels.getCurrentPage()) {
                            n_currentPage = newsModels.getCurrentPage();
                            n_currentPage++;
                            n_iRecordCount = newsModels.getRecordCount();
                            newsListAdapter.addNewsData(newsModels.getData());
                        }
                        news_recyclerview.loadMoreComplete();
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
        Log.e("HomeFragment", "HomeFragment **** onDestroyView...");
        super.onDestroyView();
        unbinder.unbind();
        stopAdTimer();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_policy:
                PolicyFragment policyFragment = new PolicyFragment();
                gotoFragment(policyFragment);
                break;
            case R.id.ll_transaction_query:
                TransactionFragment transactionFragment = new TransactionFragment();
                gotoFragment(transactionFragment);
                break;
            case R.id.ll_rights:
                SafeguardFragment safeguardFragment = new SafeguardFragment();
                gotoFragment(safeguardFragment);
                break;
            case R.id.ll_announcement:
                AnnouncementFragment announcementragment = new AnnouncementFragment();
                gotoFragment(announcementragment);
                break;
            case R.id.ll_oldconsult:
              //  DistrictFragment districtFragment = new DistrictFragment();
                OldConsultFragment OldConsultFragment =new OldConsultFragment();
                gotoFragment(OldConsultFragment);
                break;
            case R.id.ll_oldservice:

                OldServiceFragment oldServiceFragment = new OldServiceFragment();
                gotoFragment(oldServiceFragment);

                break;
            case R.id.activity_main_call_ib:
                HotLineFragment hotLineFragment = new HotLineFragment();
                Bundle bundle = new Bundle();
                bundle.putString("title","首都老龄");
                hotLineFragment.setArguments(bundle);
                gotoFragment(hotLineFragment);
                break;
            case R.id.activity_main_setting_ib:
                SettingFragment settingFragment = new SettingFragment();
                gotoFragment(settingFragment);
                break;
        }
    }

    private String getCurrNumber(int code) {
        String number = "";
        switch (code) {
            case 1:
                number = SharedPreferencesManager.getString("contacts_msg_first", "number", "13688888888");
                break;
            case 2:
                number = SharedPreferencesManager.getString("contacts_msg_second", "number", "13688888888");
                break;
            case 3:
                number = SharedPreferencesManager.getString("contacts_msg_third", "number", "13688888888");
                break;
            case 4:
                number = SharedPreferencesManager.getString("contacts_msg_fourth", "number", "13688888888");
                break;
        }
        return number;
    }

    private void showDialog(int code, final String currNumber) {
        // Builder builder = new AlertDialog.Builder(context);
        // builder.setTitle("下载").setMessage("你想要下载:"+appName+"?");
        //
        // builder.setPositiveButton("确认", new OnClickListener() {
        //
        // @Override
        // public void onClick(DialogInterface dialog, int which) {
        // dialog.dismiss();
        // startDownload();
        // }
        // });
        //
        // builder.setNegativeButton("取消", new OnClickListener() {
        //
        // @Override
        // public void onClick(DialogInterface dialog, int which) {
        // dialog.dismiss();
        // }
        // });
        //
        // builder.create().show();
        final String number;


        ContactCallDailog.Builder builder = new ContactCallDailog.Builder(getContext());
        builder.setContent(currNumber);

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                // 调用打掉话界面

                    AppUtils.call(getContext(),currNumber);


            }
        });
        ContactCallDailog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        dialog.show();
    }


/*    private void chageMsg(int code) {
        switch (code) {
            case 1:
                String name = SharedPreferencesManager.getString("contacts_msg_first", "name", "儿子");
                String number = SharedPreferencesManager.getString("contacts_msg_first", "number", "13688888888");
                contact_msg_first_name_tv.setText(name);
                contact_msg_first_number_tv.setText(number);
                break;
            case 2:
                String nameSecond = SharedPreferencesManager.getString("contacts_msg_second", "name", "警察");
                contact_msg_second_name_tv.setText(nameSecond);
                String numberSecond = SharedPreferencesManager.getString("contacts_msg_second", "number", "13688888888");
                contact_msg_second_number_tv.setText(numberSecond);
                break;
            case 3:
                String nameThird = SharedPreferencesManager.getString("contacts_msg_third", "name", "女儿");
                contact_msg_third_name_tv.setText(nameThird);
                String numberThird = SharedPreferencesManager.getString("contacts_msg_third", "number", "13688888888");
                contact_msg_third_number_tv.setText(numberThird);
                break;
            case 4:
                String nameFourth = SharedPreferencesManager.getString("contacts_msg_fourth", "name", "医生");
                contact_msg_fourth_name_tv.setText(nameFourth);
                String numberFourth = SharedPreferencesManager.getString("contacts_msg_fourth", "number", "13688888888");
                contact_msg_fourth_number_tv.setText(numberFourth);
                break;

        }
    }*/

    private void gotoFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.hide(this);
        fragmentTransaction.add(R.id.activity_main_content_frameLayout , fragment);
//        fragmentTransaction.replace(R.id.activity_main_content_frameLayout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
