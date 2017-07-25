package com.nfu.old.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.nfu.old.R;
import com.nfu.old.view.ButtonExtendM;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bindView(inflater,R.layout.home_policy_fragment,container);
        initView();
        loadData();
        return rootView;
    }


    @Override
    protected void loadData() {

    }

    @Override
    protected void initView() {
        btnBack.setOnClickListener(new ButtonExtendM.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        edQuery.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
