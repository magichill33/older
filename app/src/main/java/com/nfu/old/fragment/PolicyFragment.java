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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nfu.old.R;
import com.nfu.old.adapter.PolicyListAdapter;
import com.nfu.old.adapter.SearchAndContributionActivityViewPagerAdapter;
import com.nfu.old.manager.ApiManager;
import com.nfu.old.model.NewsListModel;
import com.nfu.old.model.NewsModel;
import com.nfu.old.model.NewsModels;
import com.nfu.old.utils.LogUtil;
import com.nfu.old.view.ButtonExtendM;
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
    RecyclerView policy_recyclerview;

    private String searchStr;
    private static final int TEXTCHANGE = 99;
    private PolicyListAdapter policyListAdapter;
    private PolicyListAdapter date_listAdapter;
    private PolicyListAdapter ctr_listAdapter;
    private RecyclerView dateRecyclerView;
    private RecyclerView ctrRecyclerView;
    private int d_currentPage = 0;
    private int d_iRecordCount = 0;
    private int ctr_currentPage = 0;
    private int ctr_iRecordCount = 0;

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
                    getNewsListByKey(searchStr,d_currentPage,d_iRecordCount,"createdate");
                    break;
            }
        }
    };

    @Override
    protected void loadData() {
        ApiManager.getInstance().getNewsList("1002", 5, 0, 0, "desc", new StringCallback() {
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
                policyListAdapter.setNewsData(newsModels.getData());
            }
        });
    }

    @Override
    protected void initView() {
        dateRecyclerView = new RecyclerView(getContext());
       // dateRecyclerView.setLayoutParams();
        dateRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        date_listAdapter = new PolicyListAdapter(getContext(), null, new PolicyListAdapter.IOnDetailListener() {
            @Override
            public void onDetailListener(NewsModel model) {

            }
        });
        dateRecyclerView.setAdapter(date_listAdapter);

        ctrRecyclerView = new RecyclerView(getContext());
        // dateRecyclerView.setLayoutParams();
        ctrRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ctr_listAdapter = new PolicyListAdapter(getContext(), null, new PolicyListAdapter.IOnDetailListener() {
            @Override
            public void onDetailListener(NewsModel model) {

            }
        });
        ctrRecyclerView.setAdapter(ctr_listAdapter);

        policy_recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        policyListAdapter = new PolicyListAdapter(getContext(), null, new PolicyListAdapter.IOnDetailListener() {
            @Override
            public void onDetailListener(NewsModel model) {
                ApiManager.getInstance().getNewsDetail(model.getId(), new StringCallback() {
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
        });
        policy_recyclerview.setAdapter(policyListAdapter);

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

        btnBack.setOnClickListener(new ButtonExtendM.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        ArrayList<View> views = new ArrayList<>();
        views.add(dateRecyclerView);
        views.add(ctrRecyclerView);
        SearchAndContributionActivityViewPagerAdapter viewPagerAdapter = new SearchAndContributionActivityViewPagerAdapter(views,new String[]{"按日期排序","点击率"});
        mViewPager.setAdapter(viewPagerAdapter);
        mPagerIndicator.setViewPager(mViewPager,0);
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

    private void getNewsListByKey(String keyword, int currentPage, int iRecordCount, final String strOrderBy ){
        ApiManager.getInstance().getNewsListByKey("1002", keyword, 8, currentPage, iRecordCount, strOrderBy, "desc", new StringCallback() {
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
                    date_listAdapter.setNewsData(newsModels.getData());
                    if (newsModels!=null){
                        d_currentPage = newsModels.getCurrentPage();
                        d_iRecordCount = newsModels.getRecordCount();
                    }
                }else {
                    ctr_listAdapter.setNewsData(newsModels.getData());
                    if (newsModels!=null){
                        ctr_currentPage = newsModels.getCurrentPage();
                        ctr_iRecordCount = newsModels.getRecordCount();
                    }
                }

            }
        });
    }
}
