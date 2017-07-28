package com.nfu.old.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nfu.old.Constant;
import com.nfu.old.R;
import com.nfu.old.adapter.SearchAndContributionActivityViewPagerAdapter;
import com.nfu.old.adapter.ServiceListAdapter;
import com.nfu.old.manager.ApiManager;
import com.nfu.old.model.NewsListModel;
import com.nfu.old.model.NewsModel;
import com.nfu.old.model.ServiceListModel;
import com.nfu.old.model.ServiceModel;
import com.nfu.old.model.ServiceModels;
import com.nfu.old.utils.LogUtil;
import com.nfu.old.utils.ToastUtil;
import com.nfu.old.view.ButtonExtendM;
import com.nfu.old.view.PagerIndicator;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;

import butterknife.BindView;
import okhttp3.Call;

/**
 * Created by Administrator on 2017/7/27.
 */

public class ServiceListFragment extends BaseFragment {
    @BindView(R.id.btn_back)
    ButtonExtendM btnBack;
    @BindView(R.id.top_title)
    TextView tv_title;
    @BindView(R.id.nfu_activity_search_layout_et)
    EditText edQuery;
    @BindView(R.id.query_cardview)
    CardView cardViewQuery;
    @BindView(R.id.ll_search)
    LinearLayout ll_search;
    @BindView(R.id.nfu_activity_search_layout_tab)
    PagerIndicator mPagerIndicator;
    @BindView(R.id.nfu_activity_search_layout_viewpager)
    ViewPager mViewPager;
    @BindView(R.id.policy_recyclerview)
    RecyclerView policy_recyclerview;

    @BindView(R.id.fragment_service_location_ib)
    ButtonExtendM loctionBtn;

    private String searchStr;
    private static final int TEXTCHANGE = 99;
    private ServiceListAdapter policyListAdapter;
    private ServiceListAdapter date_listAdapter;
    private ServiceListAdapter ctr_listAdapter;
    private RecyclerView dateRecyclerView;
    private RecyclerView ctrRecyclerView;
    private int d_currentPage = 0;
    private int d_iRecordCount = 0;
    private int ctr_currentPage = 0;
    private int ctr_iRecordCount = 0;

    private String title = "服务查询";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bindView(inflater, R.layout.service_list_fragment, container);
        initView();
        loadData();
        return rootView;
    }

    private Handler msgHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TEXTCHANGE:
                    if (ll_search.getVisibility() != View.VISIBLE) {
//                        policy_recyclerview.setVisibility(View.GONE);
                        ll_search.setVisibility(View.VISIBLE);
                    }
//                    getNewsListByKey(searchStr,d_currentPage,d_iRecordCount,"createdate");
                    break;
            }
        }
    };
    int serviceTypeId;
    @Override
    protected void loadData() {
        Bundle bundle = getArguments();

        if (bundle != null) {
            serviceTypeId = bundle.getInt("serviceTypeId");
            title = bundle.getString("title");
        }

        tv_title.setText(title);

        ApiManager.getInstance().getXbsFws(String.valueOf(serviceTypeId), 5, 0, 0, String.valueOf(Constant.lontitude), String.valueOf(Constant.latitude),"",new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtil.i("ServiceFragment--->loadData--->getServiceList--->onError::"+e);

            }

            @Override
            public void onResponse(String response, int id) {
                LogUtil.i("ServiceFragment--->loadData--->getServiceList--->onResponse::"+response);
                ServiceListModel servicesListModel =  new Gson().fromJson(response,ServiceListModel.class);
                LogUtil.i("ServiceFragment--->loadData--->getServiceList--->servicesListModel::"+servicesListModel);
                ServiceModels serviceModels = new Gson().fromJson(servicesListModel.getStrResult(),ServiceModels.class);
                LogUtil.i("ServiceFragment--->loadData--->getServiceList--->ServiceModels::"+serviceModels);
                date_listAdapter.setNewsData(serviceModels.getData());
                ctr_listAdapter.setNewsData(serviceModels.getData());
            }
        });


    }

    @Override
    protected void initView() {
        dateRecyclerView = new RecyclerView(getContext());
        // dateRecyclerView.setLayoutParams();
        dateRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        date_listAdapter = new ServiceListAdapter(getContext(), null, new ServiceListAdapter.IOnDetailListener() {
            @Override
            public void onDetailListener(ServiceModel model) {

            }
        });
        dateRecyclerView.setAdapter(date_listAdapter);

        ctrRecyclerView = new RecyclerView(getContext());
        // dateRecyclerView.setLayoutParams();
        ctrRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ctr_listAdapter = new ServiceListAdapter(getContext(), null, new ServiceListAdapter.IOnDetailListener() {
            @Override
            public void onDetailListener(ServiceModel model) {

            }
        });
        ctrRecyclerView.setAdapter(ctr_listAdapter);

      /*  policy_recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        policyListAdapter = new ServiceListAdapter(getContext(), null, new ServiceListAdapter.IOnDetailListener() {
            @Override
            public void onDetailListener(ServiceModel model) {
                *//*ApiManager.getInstance().getNewsDetail(model.getId(), new StringCallback() {
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
                });*//*
//                gotoDetailFragment(model.getId());
            }
        });
        policy_recyclerview.setAdapter(policyListAdapter);*/

        btnBack.setOnClickListener(new ButtonExtendM.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        loctionBtn.setVisibility(View.VISIBLE);
        loctionBtn.setOnClickListener(new ButtonExtendM.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 调用刷新位置信息
                loctionBtn.setText("河北省");

            }
        });
        cardViewQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 点击查询
                date_listAdapter.setNewsData(null);
                ctr_listAdapter.setNewsData(null);
                String equeryStr = edQuery.getText().toString().trim();
                if (!TextUtils.isEmpty(equeryStr)) {
                    fetchQueryDate(equeryStr);
                }else {
                    ToastUtil.showShortToast(getActivity(),"查询内容不能为空");
                }
            }
            });


        btnBack.setOnClickListener(new ButtonExtendM.OnClickListener()

            {
                @Override
                public void onClick (View v){
                getFragmentManager().popBackStack();
            }
            });

            ArrayList<View> views = new ArrayList<>();
        views.add(dateRecyclerView);
        views.add(ctrRecyclerView);
            SearchAndContributionActivityViewPagerAdapter viewPagerAdapter = new SearchAndContributionActivityViewPagerAdapter(views, new String[]{"附近服务机构", "全部服务机构"});
        mViewPager.setAdapter(viewPagerAdapter);
        mPagerIndicator.setViewPager(mViewPager,0);
        }

    private void fetchQueryDate(String equeryStr) {
        ApiManager.getInstance().getXbsFws(String.valueOf(serviceTypeId), 5, 0, 0, String.valueOf(Constant.lontitude), String.valueOf(Constant.latitude), equeryStr, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtil.i("ServiceFragment--->loadData--->getServiceList--->onError::" + e);

            }

            @Override
            public void onResponse(String response, int id) {
                LogUtil.i("ServiceFragment--->loadData--->getServiceList--->onResponse::" + response);
                ServiceListModel servicesListModel = new Gson().fromJson(response, ServiceListModel.class);
                LogUtil.i("ServiceFragment--->loadData--->getServiceList--->servicesListModel::" + servicesListModel);
                ServiceModels serviceModels = new Gson().fromJson(servicesListModel.getStrResult(), ServiceModels.class);
                LogUtil.i("ServiceFragment--->loadData--->getServiceList--->ServiceModels::" + serviceModels);
                date_listAdapter.setNewsData(serviceModels.getData());
                ctr_listAdapter.setNewsData(serviceModels.getData());
            }
        });
    }

}
