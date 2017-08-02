package com.nfu.old.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import okhttp3.Call;

/**
 * Created by Administrator on 2017/7/25.
 * 咨询页面
 */

public class MediaFragment extends BaseFragment {
    @BindView(R.id.btn_back)
    ButtonExtendM btnBack;
    @BindView(R.id.top_title)
    TextView tv_title;
    @BindView(R.id.policy_recyclerview)
    XRecyclerView policy_recyclerview;
    @BindView(R.id.nfu_activity_search_layout_et)
    EditText edQuery;
    @BindView(R.id.nfu_activity_search_layout_et_clean)
    ImageView mCleanTextIv;

    private final static int REFRESH_TYPE = 1001;
    private final static int LOADMORE_TYPE = 1002;
    private static final int PAGESIZE = 15;
    private PolicyListAdapter policyListAdapter;

    private int p_currentPage = 0;
    private int p_iRecordCount = 0;

    private String searchStr;
    private static final int TEXTCHANGE = 99;
    private static final int SEARCH_ALL = 100;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bindView(inflater,R.layout.media_fragment,container);
        initView();
        loadData();
        return rootView;
    }

    private Handler msgHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case TEXTCHANGE:
                    getNormalListByKey(searchStr,p_currentPage,p_iRecordCount,REFRESH_TYPE);
                    break;
                case SEARCH_ALL:
                    getNormalList(0,0,REFRESH_TYPE);
                    break;
            }
        }
    };


    @Override
    protected void loadData() {
        ApiManager.getInstance().getNewsList("1013", PAGESIZE, 0, 0,"createdate",  "desc", new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtil.i("PolicyFragment--->loadData--->getNewsList--->onError::"+e);

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
        tv_title.setText(R.string.consult_item_meitibaodao);

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
       // policy_recyclerview.addItemDecoration(new MyItemDecoration(getContext(),MyItemDecoration.VERTICAL_LIST));
        policy_recyclerview.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                LogUtil.i("policy_recyclerview--->onRefresh");
                if (TextUtils.isEmpty(searchStr)){
                    getNormalList(0,0,REFRESH_TYPE);
                }else {
                    getNormalListByKey(searchStr,0,0,REFRESH_TYPE);
                }

            }

            @Override
            public void onLoadMore() {
                LogUtil.i("policy_recyclerview--->onRefresh");
                if (TextUtils.isEmpty(searchStr)){
                    getNormalList(p_currentPage,p_iRecordCount,LOADMORE_TYPE);
                }else {
                    getNormalListByKey(searchStr,p_currentPage,p_iRecordCount,LOADMORE_TYPE);
                }

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
                p_iRecordCount = 0;
                p_currentPage = 0;


                if ("".equals(s.toString())) {
                    mCleanTextIv.setVisibility(View.INVISIBLE);
                    searchStr = "";
                    policyListAdapter.setNewsData(null);

                    Message message = msgHandler.obtainMessage();
                    message.what = SEARCH_ALL;
                    message.obj = s.toString();
                    msgHandler.sendMessageDelayed(message, 500);
                } else {
                    searchStr = s.toString();

                    policyListAdapter.setNewsData(null);
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
        bundle.putString("title",getString(R.string.consult_item_meitibaodao));
        bundle.putSerializable("news",newsModel);
        newsDetailFragment.setArguments(bundle);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.activity_main_content_frameLayout, newsDetailFragment);
        fragmentTransaction.commit();
    }

    private void getNormalListByKey(String key,int currentPage, int iRecordCount,final int type){
        ApiManager.getInstance().getNewsListByKey("1013",key, PAGESIZE, currentPage, iRecordCount,"createdate",  "desc", new StringCallback() {
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

    private void getNormalList(int currentPage, int iRecordCount,final int type){
        ApiManager.getInstance().getNewsList("1013", PAGESIZE, currentPage, iRecordCount,"createdate",  "desc", new StringCallback() {
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
