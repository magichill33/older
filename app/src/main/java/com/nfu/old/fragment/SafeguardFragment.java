package com.nfu.old.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.nfu.old.utils.AppUtils;
import com.nfu.old.utils.LogUtil;
import com.nfu.old.view.ButtonExtendM;
import com.nfu.old.view.MyItemDecoration;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.BindView;
import okhttp3.Call;

import static android.R.attr.fragment;

/**
 * Created by Administrator on 2017/7/25.
 * 咨询页面
 */

public class SafeguardFragment extends BaseFragment implements View.OnClickListener{
    @BindView(R.id.btn_back)
    ButtonExtendM btnBack;
    @BindView(R.id.top_title)
    TextView tv_title;
    @BindView(R.id.btn_complain)
    Button btn_complain;
    @BindView(R.id.btn_help)
    Button btn_help;
    @BindView(R.id.btn_namelist)
    Button btn_namelist;

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
        btn_complain.setOnClickListener(this);
        btn_help.setOnClickListener(this);
        btn_namelist.setOnClickListener(this);
        tv_title.setText(R.string.home_fragment_maintain_str);
        btnBack.setOnClickListener(new ButtonExtendM.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_help:
                gotoDetailFragment(2);
                break;
            case R.id.btn_complain:
                gotoDetailFragment(1);
                break;
            case R.id.btn_namelist:
                //AppUtils.call(getContext(),getString(R.string.safeguard_phone_str));
                break;
        }
    }

    private void gotoDetailFragment(int type){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        SafeguardDetailFragment safeguardDetailFragment = new SafeguardDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title",getString(R.string.home_fragment_maintain_str));
        bundle.putInt("type",type);
        safeguardDetailFragment.setArguments(bundle);
        fragmentTransaction.hide(this);
        fragmentTransaction.add(R.id.activity_main_content_frameLayout , safeguardDetailFragment);
        fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.replace(R.id.activity_main_content_frameLayout, safeguardDetailFragment);
        fragmentTransaction.commit();
    }
}
