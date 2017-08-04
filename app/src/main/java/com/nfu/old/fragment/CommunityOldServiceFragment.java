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
import com.nfu.old.adapter.OldServiceListAdapter;
import com.nfu.old.adapter.SearchAndContributionActivityViewPagerAdapter;
import com.nfu.old.adapter.ServiceListAdapter;
import com.nfu.old.manager.ApiManager;
import com.nfu.old.model.OldServiceListModel;
import com.nfu.old.model.OldServiceModel;
import com.nfu.old.model.OldServiceModels;
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

import static com.nfu.old.R.id.nfu_activity_query_btn;

/**
 * Created by Administrator on 2017/7/27.
 */

public class CommunityOldServiceFragment extends BaseFragment {
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
    @BindView(R.id.nfu_activity_query_btn)
    TextView nfu_activity_query_btn;

    @BindView(R.id.city_old_recyclerview)
    XRecyclerView nearbyRecyclerView;

    @BindView(R.id.fragment_service_location_ib)
    ButtonExtendM loctionBtn;

    private static final int TEXTCHANGE = 99;

    private OldServiceListAdapter nearbylistAdapter;

    //    private XRecyclerView nearbyRecyclerView;
    private int nearby_currentPage = 0;
    private int nearby_iRecordCount = 0;

    private final static int REFRESH_TYPE = 1001;
    private final static int LOADMORE_TYPE = 1002;

    private static final int PAGESIZE = 7;
    private String equeryStr = "";

    private String title = "养老助残服务商";
    int serviceTypeId;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bindView(inflater, R.layout.city_old_service_list_fragment, container);
        initView();
        loadData();
        return rootView;
    }


    @Override
    protected void loadData() {
        Bundle bundle = getArguments();

        if (bundle != null) {
            serviceTypeId = bundle.getInt("serviceTypeId");
            title = bundle.getString("title");
        }
        tv_title.setText(R.string.oldservice_second_top_title);
//        nfu_activity_query_btn.setText("机构查询");
        fetchQueryDate(equeryStr);
    }

    @Override
    protected void initView() {
//        nearbyRecyclerView = new XRecyclerView(getContext());
        // dateRecyclerView.setLayoutParams();
        nearbyRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        nearbyRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        nearbyRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        nearbylistAdapter = new OldServiceListAdapter(getContext(), null, new OldServiceListAdapter.IOnDetailListener() {
            @Override
            public void onDetailListener(OldServiceModel model) {
                //进入服务详情页面
//                gotoServiceDetailFragment(model);
            }
        });
        nearbyRecyclerView.setAdapter(nearbylistAdapter);

        nearbyRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                LogUtil.i("nearbyRecyclerView--->onRefresh");
                geNearbylList(0, 0, REFRESH_TYPE);
            }

            @Override
            public void onLoadMore() {
                LogUtil.i("nearbyRecyclerView--->onLoadMore");
                geNearbylList(nearby_currentPage, nearby_iRecordCount, LOADMORE_TYPE);
            }
        });


        btnBack.setOnClickListener(new ButtonExtendM.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });


        loctionBtn.setVisibility(View.VISIBLE);
        loctionBtn.setText(Constant.CITYNAME);
        loctionBtn.setOnClickListener(new ButtonExtendM.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 调用刷新位置信息
//                loctionBtn.setText("河北省");

            }
        });
        cardViewQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 点击查询
                equeryStr = edQuery.getText().toString().trim();
                if (!TextUtils.isEmpty(equeryStr)) {
                    nearbylistAdapter.setNewsData(null);
                    fetchQueryDate(equeryStr);
                } else {
                    ToastUtil.showShortToast(getActivity(), "查询内容不能为空");
                }
            }
        });


        btnBack.setOnClickListener(new ButtonExtendM.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });


    }

    private void fetchQueryDate(String equeryStr) {
        ApiManager.getInstance().getPublicServiceAgencies("2", PAGESIZE, 0, 0, String.valueOf(Constant.lontitude), String.valueOf(Constant.latitude), equeryStr, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtil.i("ServiceFragment--->loadData--->getServiceList--->onError::" + e);

            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    LogUtil.i("ServiceFragment--->loadData--->getServiceList--->onResponse::" + response);
                    OldServiceListModel oldServiceListModel = new Gson().fromJson(response, OldServiceListModel.class);
                    LogUtil.i("ServiceFragment--->loadData--->getServiceList--->servicesListModel::" + oldServiceListModel);
                    OldServiceModels oldServiceModels = new Gson().fromJson(oldServiceListModel.getStrResult(), OldServiceModels.class);
                    LogUtil.i("ServiceFragment--->loadData--->getServiceList--->ServiceModels::" + oldServiceModels);
//                    nearbylistAdapter.setNewsData(oldServiceModels.getData());
                    nearby_iRecordCount = oldServiceModels.getRecordCount();
                    nearby_currentPage = oldServiceModels.getCurrentPage();
                    nearby_currentPage++;
                    LogUtil.i("ServiceFragment--->loadData--->getServiceList--->first:: nearby_iRecordCount = " + nearby_iRecordCount + " | nearby_currentPage=" + nearby_currentPage);
                    nearbylistAdapter.setNewsData(oldServiceModels.getData());
               /* alllistAdapter.setNewsData(serviceModels.getData());
                all_iRecordCount = serviceModels.getRecordCount();
                all_currentPage= serviceModels.getCurrentPage();*/
                } catch (Exception e) {
                    LogUtil.i("ServiceFragment--->loadData--->Exception--->" + e);
                }
            }
        });



    }

    private void geNearbylList(int currentPage, int iRecordCount, final int type) {
        LogUtil.i("ServiceFragment--->loadData--->getServiceList  --->loadmorebefore :: currentPage =" + currentPage + " | iRecordCount =" + iRecordCount);
        ApiManager.getInstance().getPublicServiceAgencies("2", PAGESIZE, currentPage, iRecordCount, String.valueOf(Constant.lontitude), String.valueOf(Constant.latitude), equeryStr, new StringCallback() {

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
                OldServiceListModel oldServiceListModel = new Gson().fromJson(response, OldServiceListModel.class);
                LogUtil.i("ServiceFragment--->loadData--->getServiceList--->servicesListModel::" + oldServiceListModel);
                OldServiceModels oldServiceModels = new Gson().fromJson(oldServiceListModel.getStrResult(), OldServiceModels.class);
                LogUtil.i("ServiceFragment--->loadData--->getServiceList--->ServiceModels::" + oldServiceModels);

                LogUtil.i("ServiceFragment---> nearbyPage");
                if (type == REFRESH_TYPE) {
                    if (oldServiceModels != null) {
                        nearby_currentPage = oldServiceModels.getCurrentPage();
//                        nearby_currentPage++;
                        nearby_iRecordCount = oldServiceModels.getRecordCount();
                    }
                    nearbylistAdapter.setNewsData(oldServiceModels.getData());
                    nearbyRecyclerView.refreshComplete();
                } else {
                    if (oldServiceModels != null) {
                        if (nearby_currentPage <= oldServiceModels.getCurrentPage()) {
                            nearby_currentPage = oldServiceModels.getCurrentPage();
                            nearby_currentPage++;
                            nearby_iRecordCount = oldServiceModels.getRecordCount();
                            nearbylistAdapter.addNewsData(oldServiceModels.getData());
                            LogUtil.i("ServiceFragment--->loadData--->getServiceList--->loadmore---->:: nearby_iRecordCount = " + nearby_iRecordCount + " | nearby_currentPage=" + nearby_currentPage);
                        }

                    }
                    nearbyRecyclerView.loadMoreComplete();

                }


            }
        });
    }


    private void gotoServiceDetailFragment(OldServiceModel serviceModel) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        ServiceDetailFragment serviceDetailFragment = new ServiceDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", "商家信息");
        bundle.putSerializable("servicemodel", serviceModel);
        serviceDetailFragment.setArguments(bundle);
        fragmentTransaction.hide(this);
        fragmentTransaction.add(R.id.activity_main_content_frameLayout, serviceDetailFragment);
        fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.replace(R.id.activity_main_content_frameLayout, serviceDetailFragment);
        fragmentTransaction.commit();
    }
}
