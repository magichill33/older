package com.nfu.old.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nfu.old.R;
import com.nfu.old.view.ButtonExtendM;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/8/2.
 */

public class UpdateFragment extends BaseFragment{
    @BindView(R.id.btn_back)
    ButtonExtendM btnBack;
    @BindView(R.id.top_title)
    TextView tv_title;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bindView(inflater, R.layout.update_fragment,container);
        initView();
        loadData();
        return rootView;
    }


    @Override
    protected void loadData() {

    }

    @Override
    protected void initView() {

    }
}
