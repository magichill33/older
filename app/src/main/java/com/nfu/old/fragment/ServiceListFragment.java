package com.nfu.old.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.nfu.old.Constant;
import com.nfu.old.R;
import com.nfu.old.adapter.SearchAndContributionActivityViewPagerAdapter;
import com.nfu.old.adapter.ServiceListAdapter;
import com.nfu.old.manager.ApiManager;
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
//    @BindView(R.id.list_item_call)
//    ImageView list_item_call;
//    @BindView(R.id.list_item_jinru)
//    ImageView list_item_jinru;

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
//    @BindView(R.id.policy_recyclerview)
//    XRecyclerView policy_recyclerview;

    @BindView(R.id.fragment_service_location_ib)
    ButtonExtendM loctionBtn;

    private String searchStr;
    private static final int TEXTCHANGE = 99;
    private ServiceListAdapter policyListAdapter;
    private ServiceListAdapter nearbylistAdapter;

    private ServiceListAdapter alllistAdapter;
    private XRecyclerView nearbyRecyclerView;
    private XRecyclerView allRecyclerView;
    private int nearby_currentPage = 0;
    private int nearby_iRecordCount = 0;
    private int all_currentPage = 0;
    private int all_iRecordCount = 0;
    private int p_currentPage = 0;
    private int p_iRecordCount = 0;

    private final static int REFRESH_TYPE = 1001;
    private final static int LOADMORE_TYPE = 1002;
    private final static int LEFT_TYPE = 2001;
    private final static int RIGHT_TYPE = 2002;
    private static final int PAGESIZE = 7;

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

 /*       ApiManager.getInstance().getXbsFws(String.valueOf(serviceTypeId), PAGESIZE, 0, 0, String.valueOf(Constant.lontitude), String.valueOf(Constant.latitude),"",new StringCallback() {
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
                nearbylistAdapter.setNewsData(serviceModels.getData());
                nearby_iRecordCount = serviceModels.getRecordCount();
                nearby_currentPage= serviceModels.getCurrentPage();
                alllistAdapter.setNewsData(serviceModels.getData());
                all_iRecordCount = serviceModels.getRecordCount();
                all_currentPage= serviceModels.getCurrentPage();
            }
        });*/


        ApiManager.getInstance().getXbsFws(String.valueOf(serviceTypeId), PAGESIZE, 0, 0, "", "","",new StringCallback() {
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
                nearbylistAdapter.setNewsData(serviceModels.getData());
                nearby_iRecordCount = serviceModels.getRecordCount();
                nearby_currentPage= serviceModels.getCurrentPage();
                alllistAdapter.setNewsData(serviceModels.getData());
                all_iRecordCount = serviceModels.getRecordCount();
                all_currentPage= serviceModels.getCurrentPage();
            }
        });


    }

    @Override
    protected void initView() {
        nearbyRecyclerView = new XRecyclerView(getContext());
        // dateRecyclerView.setLayoutParams();
        nearbyRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        nearbyRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        nearbyRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        nearbylistAdapter = new ServiceListAdapter(getContext(), null, new ServiceListAdapter.IOnDetailListener() {
            @Override
            public void onDetailListener(ServiceModel model) {
                //进入服务详情页面
                gotoServiceDetailFragment(model);
            }
        });
        nearbyRecyclerView.setAdapter(nearbylistAdapter);

        nearbyRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                LogUtil.i("nearbyRecyclerView--->onRefresh");
                getNormalList(nearby_currentPage,nearby_iRecordCount,REFRESH_TYPE,LEFT_TYPE);
            }

            @Override
            public void onLoadMore() {
                LogUtil.i("nearbyRecyclerView--->onLoadMore");
                getNormalList(nearby_currentPage,nearby_iRecordCount,LOADMORE_TYPE,LEFT_TYPE);
            }
        });
        allRecyclerView = new XRecyclerView(getContext());
        // dateRecyclerView.setLayoutParams();
        allRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        allRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        allRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        alllistAdapter = new ServiceListAdapter(getContext(), null, new ServiceListAdapter.IOnDetailListener() {
            @Override
            public void onDetailListener(ServiceModel model) {
                //进入服务详情页面
                gotoServiceDetailFragment(model);
            }
        });
        allRecyclerView.setAdapter(alllistAdapter);

        allRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                LogUtil.i("allRecyclerView--->onRefresh");
                getNormalList(all_currentPage,all_iRecordCount,REFRESH_TYPE,RIGHT_TYPE);
            }

            @Override
            public void onLoadMore() {
                LogUtil.i("allRecyclerView--->onLoadMore");
                getNormalList(all_currentPage,all_iRecordCount,LOADMORE_TYPE,RIGHT_TYPE);
            }
        });

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
                nearbylistAdapter.setNewsData(null);
                alllistAdapter.setNewsData(null);
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
        views.add(nearbyRecyclerView);
        views.add(allRecyclerView);
            SearchAndContributionActivityViewPagerAdapter viewPagerAdapter = new SearchAndContributionActivityViewPagerAdapter(views, new String[]{"附近服务机构", "全部服务机构"});
        mViewPager.setAdapter(viewPagerAdapter);
        mPagerIndicator.setViewPager(mViewPager,0);
        }

    private void fetchQueryDate(String equeryStr) {
        ApiManager.getInstance().getXbsFws(String.valueOf(serviceTypeId), PAGESIZE, 0, 0, String.valueOf(Constant.lontitude), String.valueOf(Constant.latitude), equeryStr, new StringCallback() {
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
                nearbylistAdapter.setNewsData(serviceModels.getData());
                nearby_iRecordCount = serviceModels.getRecordCount();
                alllistAdapter.setNewsData(serviceModels.getData());
                nearby_iRecordCount = serviceModels.getRecordCount();
            }
        });
    }

    private void getNormalList(int currentPage, int iRecordCount,final int type,final int viewpagerIndex) {
        ApiManager.getInstance().getXbsFws(String.valueOf(serviceTypeId), PAGESIZE, currentPage, iRecordCount, String.valueOf(Constant.lontitude), String.valueOf(Constant.latitude), "", new StringCallback() {

            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtil.i("ServiceFragment--->loadData--->getServiceList--->onError::" + e);
                if (type == REFRESH_TYPE) {
                    nearbyRecyclerView.refreshComplete();
                } else {
                    nearbyRecyclerView.loadMoreComplete();
                }
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtil.i("ServiceFragment--->loadData--->getServiceList--->onResponse::" + response);
                ServiceListModel servicesListModel = new Gson().fromJson(response, ServiceListModel.class);
                LogUtil.i("ServiceFragment--->loadData--->getServiceList--->servicesListModel::" + servicesListModel);
                ServiceModels serviceModels = new Gson().fromJson(servicesListModel.getStrResult(), ServiceModels.class);
                LogUtil.i("ServiceFragment--->loadData--->getServiceList--->ServiceModels::" + serviceModels);
                if(viewpagerIndex == LEFT_TYPE){
                    LogUtil.i("ServiceFragment---> nearbyPage");
                    if (type == REFRESH_TYPE) {
                        if (serviceModels != null) {
                            nearby_currentPage = serviceModels.getCurrentPage();
                            nearby_currentPage++;
                            nearby_iRecordCount = serviceModels.getRecordCount();
                        }
                        nearbylistAdapter.setNewsData(serviceModels.getData());
                        nearbyRecyclerView.refreshComplete();
                    } else {
                        if (serviceModels != null) {
                            if (nearby_currentPage <= serviceModels.getCurrentPage()) {
                                nearby_currentPage = serviceModels.getCurrentPage();
                                nearby_currentPage++;
                                nearby_iRecordCount = serviceModels.getRecordCount();
                                nearbylistAdapter.addNewsData(serviceModels.getData());
                            }

                        }
                        nearbyRecyclerView.loadMoreComplete();

                    }
                }else if(viewpagerIndex == RIGHT_TYPE) {

                    LogUtil.i("ServiceFragment--->allPage");
                    if (type == REFRESH_TYPE) {
                        if (serviceModels != null) {
                            all_currentPage = serviceModels.getCurrentPage();
                            all_currentPage++;
                            all_iRecordCount = serviceModels.getRecordCount();
                        }
                        alllistAdapter.setNewsData(serviceModels.getData());
                        allRecyclerView.refreshComplete();
                    } else {
                        if (serviceModels != null) {
                            if (all_currentPage <= serviceModels.getCurrentPage()) {
                                all_currentPage = serviceModels.getCurrentPage();
                                all_currentPage++;
                                all_iRecordCount = serviceModels.getRecordCount();
                                alllistAdapter.addNewsData(serviceModels.getData());
                            }

                        }
                        allRecyclerView.loadMoreComplete();
                    }
                }


            }
        });
    }

    private void gotoServiceDetailFragment(ServiceModel serviceModel) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        ServiceDetailFragment serviceDetailFragment= new ServiceDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title","商家信息");
        bundle.putSerializable("servicemodel",serviceModel);
        serviceDetailFragment.setArguments(bundle);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.activity_main_content_frameLayout, serviceDetailFragment);
        fragmentTransaction.commit();
    }
}
