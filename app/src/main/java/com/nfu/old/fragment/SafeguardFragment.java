package com.nfu.old.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.nfu.old.R;
import com.nfu.old.adapter.PolicyListAdapter;
import com.nfu.old.manager.ApiManager;
import com.nfu.old.model.NewsListModel;
import com.nfu.old.model.NewsModel;
import com.nfu.old.model.NewsModels;
import com.nfu.old.utils.LogUtil;
import com.nfu.old.view.ButtonExtendM;
import com.nfu.old.view.MyItemDecoration;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.BindView;
import okhttp3.Call;

/**
 * Created by Administrator on 2017/7/25.
 * 咨询页面
 */

public class SafeguardFragment extends BaseFragment {
    @BindView(R.id.btn_back)
    ButtonExtendM btnBack;
    @BindView(R.id.top_title)
    TextView tv_title;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bindView(inflater,R.layout.safeguard_fragment,container);
        initView();
        loadData();
        return rootView;
    }




    @Override
    protected void loadData() {

    }

    @Override
    protected void initView() {



        tv_title.setText(R.string.home_fragment_maintain_str);
        btnBack.setOnClickListener(new ButtonExtendM.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });


    }

}
