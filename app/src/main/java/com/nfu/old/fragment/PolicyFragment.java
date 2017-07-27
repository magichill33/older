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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.nfu.old.R;
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
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;

/**
 * Created by Administrator on 2017/7/25.
 * 咨询页面
 */

public class PolicyFragment extends BaseFragment {
    @BindView(R.id.btn_back)
    ButtonExtendM btnBack;
    @BindView(R.id.top_title)
    TextView tv_title;
    @BindView(R.id.nfu_activity_search_layout_et)
    EditText edQuery;
    @BindView(R.id.nfu_activity_search_layout_et_clean)
    ImageView mCleanTextIv;
    @BindView(R.id.ll_search)
    LinearLayout ll_search;
    @BindView(R.id.nfu_activity_search_layout_tab)
    PagerIndicator mPagerIndicator;
    @BindView(R.id.nfu_activity_search_layout_viewpager)
    ViewPager mViewPager;
    @BindView(R.id.policy_recyclerview)
    XRecyclerView policy_recyclerview;

    private final static int REFRESH_TYPE = 1001;
    private final static int LOADMORE_TYPE = 1002;
    private String searchStr;
    private static final int TEXTCHANGE = 99;
    private static final int PAGESIZE = 15;
    private PolicyListAdapter policyListAdapter;
    private PolicyListAdapter date_listAdapter;
    private PolicyListAdapter ctr_listAdapter;
    private XRecyclerView dateRecyclerView;
    private XRecyclerView ctrRecyclerView;
    private int d_currentPage = 0;
    private int d_iRecordCount = 0;
    private int ctr_currentPage = 0;
    private int ctr_iRecordCount = 0;
    private int p_currentPage = 0;
    private int p_iRecordCount = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bindView(inflater,R.layout.home_policy_fragment,container);
        initView();
        loadData();
        return rootView;
    }


    private Handler msgHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case TEXTCHANGE:
                    if (ll_search.getVisibility()!=View.VISIBLE){
                        policy_recyclerview.setVisibility(View.GONE);
                        ll_search.setVisibility(View.VISIBLE);
                    }
                    getNewsListByKey(searchStr,d_currentPage,d_iRecordCount,"createdate",LOADMORE_TYPE);
                    getNewsListByKey(searchStr,ctr_currentPage,ctr_iRecordCount,"count",LOADMORE_TYPE);
                    break;
            }
        }
    };

    @Override
    protected void loadData() {
        ApiManager.getInstance().getNewsList("1002", PAGESIZE, 0, 0, "desc", new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtil.i("PolicyFragment--->loadData--->getNewsList--->onError::"+e);
                if (ll_search.getVisibility() == View.VISIBLE){

                }
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtil.i("PolicyFragment--->loadData--->getNewsList--->onResponse::"+response);
                NewsListModel newsListModel =  new Gson().fromJson(response,NewsListModel.class);
                LogUtil.i("PolicyFragment--->loadData--->getNewsList--->newsListModel::"+newsListModel);
                NewsModels newsModels = new Gson().fromJson(newsListModel.getStrResult(),NewsModels.class);
                LogUtil.i("PolicyFragment--->loadData--->getNewsList--->NewsModels::"+newsModels);
                p_currentPage = newsModels.getCurrentPage();
                p_currentPage++;
                p_iRecordCount = newsModels.getRecordCount();
                policyListAdapter.setNewsData(newsModels.getData());
            }
        });
    }

    @Override
    protected void initView() {
        dateRecyclerView = new XRecyclerView(getContext());
       // dateRecyclerView.setLayoutParams();
        dateRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        dateRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        dateRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        date_listAdapter = new PolicyListAdapter(getContext(), null, new PolicyListAdapter.IOnDetailListener() {
            @Override
            public void onDetailListener(NewsModel model) {
                gotoDetailFragment(model.getId());
            }
        });
        dateRecyclerView.setAdapter(date_listAdapter);
        dateRecyclerView.addItemDecoration(new MyItemDecoration(getContext(),MyItemDecoration.VERTICAL_LIST));
        dateRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                LogUtil.i("dateRecyclerView--->onRefresh");
                getNewsListByKey(searchStr,0,0,"createdate",REFRESH_TYPE);
            }

            @Override
            public void onLoadMore() {
                LogUtil.i("dateRecyclerView--->onLoadMore");
                getNewsListByKey(searchStr,d_currentPage,d_iRecordCount,"createdate",LOADMORE_TYPE);
            }
        });


        ctrRecyclerView = new XRecyclerView(getContext());
        ctrRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        ctrRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        // dateRecyclerView.setLayoutParams();
        ctrRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ctr_listAdapter = new PolicyListAdapter(getContext(), null, new PolicyListAdapter.IOnDetailListener() {
            @Override
            public void onDetailListener(NewsModel model) {
                gotoDetailFragment(model.getId());
            }
        });
        ctrRecyclerView.setAdapter(ctr_listAdapter);
        ctrRecyclerView.addItemDecoration(new MyItemDecoration(getContext(),MyItemDecoration.VERTICAL_LIST));
        ctrRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                LogUtil.i("ctrRecyclerView--->onRefresh");
                getNewsListByKey(searchStr,0,0,"count",REFRESH_TYPE);
            }

            @Override
            public void onLoadMore() {
                LogUtil.i("ctrRecyclerView--->onRefresh");
                getNewsListByKey(searchStr,ctr_currentPage,ctr_iRecordCount,"count",LOADMORE_TYPE);
            }
        });

        policy_recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        policyListAdapter = new PolicyListAdapter(getContext(), null, new PolicyListAdapter.IOnDetailListener() {
            @Override
            public void onDetailListener(NewsModel model) {

                gotoDetailFragment(model.getId());
            }
        });
        policy_recyclerview.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        policy_recyclerview.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        policy_recyclerview.setAdapter(policyListAdapter);
        policy_recyclerview.addItemDecoration(new MyItemDecoration(getContext(),MyItemDecoration.VERTICAL_LIST));
        policy_recyclerview.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                LogUtil.i("policy_recyclerview--->onRefresh");
                getNormalList(0,0,REFRESH_TYPE);
            }

            @Override
            public void onLoadMore() {
                LogUtil.i("policy_recyclerview--->onRefresh");
                getNormalList(p_currentPage,p_iRecordCount,LOADMORE_TYPE);
            }
        });


        btnBack.setOnClickListener(new ButtonExtendM.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        mCleanTextIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edQuery.setText("");
            }
        });

        edQuery.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                msgHandler.removeMessages(TEXTCHANGE);
                if ("".equals(s.toString())) {
                    mCleanTextIv.setVisibility(View.INVISIBLE);
                    searchStr = "";
                    date_listAdapter.setNewsData(null);
                    ctr_listAdapter.setNewsData(null);
                } else {
                    searchStr = s.toString();
                    d_iRecordCount = 0;
                    d_currentPage = 0;
                    ctr_currentPage = 0;
                    ctr_iRecordCount = 0;
                    date_listAdapter.setNewsData(null);
                    ctr_listAdapter.setNewsData(null);
                    mCleanTextIv.setVisibility(View.VISIBLE);
                    Message message = msgHandler.obtainMessage();
                    message.what = TEXTCHANGE;
                    message.obj = s.toString();
                    msgHandler.sendMessageDelayed(message, 500);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ArrayList<View> views = new ArrayList<>();
        views.add(dateRecyclerView);
        views.add(ctrRecyclerView);
        SearchAndContributionActivityViewPagerAdapter viewPagerAdapter = new SearchAndContributionActivityViewPagerAdapter(views,new String[]{"按日期排序","点击率"});
        mViewPager.setAdapter(viewPagerAdapter);
        mPagerIndicator.setViewPager(mViewPager,0);
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
        bundle.putString("title","政策解读");
        bundle.putSerializable("news",newsModel);
        newsDetailFragment.setArguments(bundle);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.activity_main_content_frameLayout, newsDetailFragment);
        fragmentTransaction.commit();
    }



    private void getNewsListByKey(String keyword, int currentPage, int iRecordCount, final String strOrderBy, final int type){
        ApiManager.getInstance().getNewsListByKey("1002", keyword, PAGESIZE, currentPage, iRecordCount, strOrderBy, "desc", new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtil.i("PolicyFragment--->initView--->getNewsListByKey--->onError::"+e);
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtil.i("PolicyFragment--->initView--->getNewsListByKey--->onResponse::"+response);
                NewsListModel newsListModel =  new Gson().fromJson(response,NewsListModel.class);
                LogUtil.i("PolicyFragment--->loadData--->getNewsListByKey--->newsListModel::"+newsListModel);
                NewsModels newsModels = new Gson().fromJson(newsListModel.getStrResult(),NewsModels.class);
                LogUtil.i("PolicyFragment--->loadData--->getNewsListByKey--->NewsModels::"+newsModels);
                if (strOrderBy.equalsIgnoreCase("createdate")){

                    if (type == REFRESH_TYPE){
                        if (newsModels!=null){
                            d_currentPage = newsModels.getCurrentPage();
                            d_currentPage++;
                            d_iRecordCount = newsModels.getRecordCount();
                        }
                        date_listAdapter.setNewsData(newsModels.getData());
                        dateRecyclerView.refreshComplete();
                    }else {
                        if (newsModels!=null){
                            if (d_currentPage <= newsModels.getCurrentPage()){
                                d_currentPage = newsModels.getCurrentPage();
                                d_currentPage++;
                                d_iRecordCount = newsModels.getRecordCount();
                                date_listAdapter.addNewsData(newsModels.getData());
                            }

                        }
                        dateRecyclerView.loadMoreComplete();

                    }
                }else {

                    if (type == REFRESH_TYPE){
                        if (newsModels!=null){
                            ctr_currentPage = newsModels.getCurrentPage();
                            ctr_currentPage++;
                            ctr_iRecordCount = newsModels.getRecordCount();
                        }
                        ctr_listAdapter.setNewsData(newsModels.getData());
                        ctrRecyclerView.refreshComplete();
                    }else {
                        if (newsModels!=null){
                            if (ctr_currentPage <= newsModels.getCurrentPage()){
                                ctr_currentPage = newsModels.getCurrentPage();
                                ctr_currentPage++;
                                ctr_iRecordCount = newsModels.getRecordCount();
                                ctr_listAdapter.addNewsData(newsModels.getData());
                            }

                        }
                        ctrRecyclerView.loadMoreComplete();

                    }

                }

            }
        });
    }


    private void getNormalList(int currentPage, int iRecordCount,final int type){
        ApiManager.getInstance().getNewsList("1002", PAGESIZE, currentPage, iRecordCount, "desc", new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtil.i("PolicyFragment--->loadData--->getNewsList--->onError::"+e);
                if (type == REFRESH_TYPE){
                    policy_recyclerview.refreshComplete();
                }else {
                    policy_recyclerview.loadMoreComplete();

                }
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtil.i("PolicyFragment--->loadData--->getNewsList--->onResponse::"+response);
                NewsListModel newsListModel =  new Gson().fromJson(response,NewsListModel.class);
                LogUtil.i("PolicyFragment--->loadData--->getNewsList--->newsListModel::"+newsListModel);
                NewsModels newsModels = new Gson().fromJson(newsListModel.getStrResult(),NewsModels.class);
                LogUtil.i("PolicyFragment--->loadData--->getNewsList--->NewsModels::"+newsModels);
                if (type == REFRESH_TYPE){
                    if (newsModels!=null){
                        p_currentPage = newsModels.getCurrentPage();
                        p_currentPage++;
                        p_iRecordCount = newsModels.getRecordCount();
                    }
                    policyListAdapter.setNewsData(newsModels.getData());
                    policy_recyclerview.refreshComplete();
                }else {
                    if (newsModels!=null){
                        if (p_currentPage <= newsModels.getCurrentPage()){
                            p_currentPage = newsModels.getCurrentPage();
                            p_currentPage++;
                            p_iRecordCount = newsModels.getRecordCount();
                            policyListAdapter.addNewsData(newsModels.getData());
                        }

                    }
                    policy_recyclerview.loadMoreComplete();

                }

            }
        });
    }
}
