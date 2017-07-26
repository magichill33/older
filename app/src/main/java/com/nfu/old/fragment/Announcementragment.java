package com.nfu.old.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nfu.old.R;
import com.nfu.old.adapter.NewsListAdapter;
import com.nfu.old.adapter.PolicyListAdapter;
import com.nfu.old.adapter.SearchAndContributionActivityViewPagerAdapter;
import com.nfu.old.manager.ApiManager;
import com.nfu.old.model.NewsListModel;
import com.nfu.old.model.NewsModel;
import com.nfu.old.model.NewsModels;
import com.nfu.old.utils.LogUtil;
import com.nfu.old.view.ButtonExtendM;
import com.nfu.old.view.MyItemDecoration;
import com.nfu.old.view.PagerIndicator;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;

import butterknife.BindView;
import okhttp3.Call;

/**
 * Created by Administrator on 2017/7/25.
 * 咨询页面
 */

public class Announcementragment extends BaseFragment {
    @BindView(R.id.btn_back)
    ButtonExtendM btnBack;
    @BindView(R.id.top_title)
    TextView tv_title;


    @BindView(R.id.news_recyclerview)
    RecyclerView news_recyclerview;

    private NewsListAdapter newsListAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bindView(inflater,R.layout.announcement_fragment,container);
        initView();
        loadData();
        return rootView;
    }




    @Override
    protected void loadData() {
        ApiManager.getInstance().getNewsList("1002", 8, 0, 0, "desc", new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtil.i("Announcementragment--->loadData--->getNewsList--->onError::"+e);

            }

            @Override
            public void onResponse(String response, int id) {
                LogUtil.i("Announcementragment--->loadData--->getNewsList--->onResponse::"+response);
                NewsListModel newsListModel =  new Gson().fromJson(response,NewsListModel.class);
                LogUtil.i("Announcementragment--->loadData--->getNewsList--->newsListModel::"+newsListModel);
                NewsModels newsModels = new Gson().fromJson(newsListModel.getStrResult(),NewsModels.class);
                LogUtil.i("Announcementragment--->loadData--->getNewsList--->NewsModels::"+newsModels);
                newsListAdapter.setNewsData(newsModels.getData());
            }
        });
    }

    @Override
    protected void initView() {
        tv_title.setText("通知公告");
        btnBack.setOnClickListener(new ButtonExtendM.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });
        news_recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        newsListAdapter = new NewsListAdapter(getContext(), null, new NewsListAdapter.IOnDetailListener() {
            @Override
            public void onDetailListener(NewsModel model) {
                gotoDetailFragment(model.getId());
            }
        });
        news_recyclerview.addItemDecoration(new MyItemDecoration(getContext(),MyItemDecoration.VERTICAL_LIST));
        news_recyclerview.setAdapter(newsListAdapter);
    }

    private void gotoDetailFragment(String id){
        ApiManager.getInstance().getNewsDetail(id, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtil.i("PolicyFragment--->initView--->getNewsDetail--->onError::"+e);
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtil.i("Announcementragment--->initView--->getNewsDetail--->onResponse::"+response);
                NewsListModel listModel = new Gson().fromJson(response,NewsListModel.class);
                LogUtil.i("Announcementragment--->initView--->getNewsDetail--->NewsListModel::"+listModel);
                NewsModel model1 = new Gson().fromJson(listModel.getStrResult(),NewsModel.class);
                LogUtil.i("Announcementragment--->initView--->getNewsDetail--->NewsModel::"+model1);
                gotoDetailFragment(model1);
            }
        });
    }



    private void gotoDetailFragment(NewsModel newsModel){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        NewsDetailFragment newsDetailFragment = new NewsDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title","通知公告");
        bundle.putSerializable("news",newsModel);
        newsDetailFragment.setArguments(bundle);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.activity_main_content_frameLayout, newsDetailFragment);
        fragmentTransaction.commit();
    }


}
