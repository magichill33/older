package com.nfu.old.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.nfu.old.R;
import com.nfu.old.adapter.OldListAdapter;
import com.nfu.old.manager.ApiManager;
import com.nfu.old.model.NewsListModel;
import com.nfu.old.model.NewsModel;
import com.nfu.old.model.OldWorkInfo;
import com.nfu.old.model.OlderModel;
import com.nfu.old.model.OlderModels;
import com.nfu.old.utils.LogUtil;
import com.nfu.old.view.ButtonExtendM;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.BindView;
import okhttp3.Call;

/**
 * Created by Administrator on 2017/7/27.
 */

public class OlderWorkListFragment extends BaseFragment {
    @BindView(R.id.btn_back)
    ButtonExtendM btnBack;
    @BindView(R.id.top_title)
    TextView tv_title;

    @BindView(R.id.news_recyclerview)
    RecyclerView news_recyclerview;


    private OldListAdapter oldListAdapter;

    private int p_currentPage = 0;
    private int p_iRecordCount = 0;

    private final static int REFRESH_TYPE = 1001;
    private final static int LOADMORE_TYPE = 1002;
    private static final int PAGESIZE = 10;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bindView(inflater, R.layout.old_work_list_fragment, container);
        initView();
        loadData();
        return rootView;
    }


    @Override
    protected void loadData() {
        Bundle bundle = getArguments();
        if (bundle!=null){
            String title = bundle.getString("title");
            tv_title.setText(title);
        }
        getNormalList();
    }

    @Override
    protected void initView() {

        news_recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        oldListAdapter = new OldListAdapter(getContext(), null, null);
        news_recyclerview.setAdapter(oldListAdapter);
        // policy_recyclerview.addItemDecoration(new MyItemDecoration(getContext(),MyItemDecoration.VERTICAL_LIST));



        btnBack.setOnClickListener(new ButtonExtendM.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });


    }

    private void getNormalList() {
        ApiManager.getInstance().getCityInstitutions(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtil.i("OlderWorkListFragment--->loadData--->getNewsList--->onError::" + e);

            }

            @Override
            public void onResponse(String response, int id) {
                LogUtil.i("OlderWorkListFragment--->loadData--->getNewsList--->onResponse::" + response);
                try {
                    OldWorkInfo newsListModel = new Gson().fromJson(response, OldWorkInfo.class);
                    LogUtil.i("OlderWorkListFragment--->loadData--->getNewsList--->newsListModel::" + newsListModel);
                    oldListAdapter.setNewsData(newsListModel.getStrResult());

                } catch (Exception e) {
                    LogUtil.i("OlderWorkListFragment--->loadData--->Exception--->"+e);
                }

            }
        });
    }


    private void gotoDetailFragment(String id) {
        ApiManager.getInstance().getNewsDetail(id, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtil.i("OlderWorkListFragment--->initView--->getNewsDetail--->onError::" + e);
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtil.i("OlderWorkListFragment--->initView--->getNewsDetail--->onResponse::" + response);
                NewsListModel listModel = new Gson().fromJson(response, NewsListModel.class);
                LogUtil.i("OlderWorkListFragment--->initView--->getNewsDetail--->NewsListModel::" + listModel);
                NewsModel model1 = new Gson().fromJson(listModel.getStrResult(), NewsModel.class);
                LogUtil.i("OlderWorkListFragment--->initView--->getNewsDetail--->NewsModel::" + model1);
                gotoDetailFragment(model1);
            }
        });
    }

    private void gotoDetailFragment(NewsModel newsModel) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        NewsDetailFragment newsDetailFragment = new NewsDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", getString(R.string.consult_item_meitibaodao));
        bundle.putSerializable("news", newsModel);
        newsDetailFragment.setArguments(bundle);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.activity_main_content_frameLayout, newsDetailFragment);
        fragmentTransaction.commit();
    }
}
