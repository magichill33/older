package com.nfu.old.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.nfu.old.R;
import com.nfu.old.config.ConnectUrl;
import com.nfu.old.model.NewsModel;
import com.nfu.old.view.ButtonExtendM;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/7/25.
 * 咨询页面
 */

public class TransactQueryFragment extends BaseFragment {
    @BindView(R.id.btn_back)
    ButtonExtendM btnBack;
    @BindView(R.id.top_title)
    TextView top_title;
    @BindView(R.id.wv_transact_query)
    WebView wv_content;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bindView(inflater,R.layout.transact_query_fragment,container);
        initView();
        loadData();
        return rootView;
    }

    @Override
    protected void loadData() {
        top_title.setText("办理查询");
        wv_content.loadUrl(ConnectUrl.query_card);
    }

    @Override
    protected void initView() {
        wv_content.getSettings().setJavaScriptEnabled(true);
        wv_content.getSettings().setUseWideViewPort(true);
        wv_content.getSettings().setLoadWithOverviewMode(true);
        wv_content.getSettings().setBuiltInZoomControls(true);
       // wv_content.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        wv_content.getSettings().setSupportZoom(true);
        btnBack.setOnClickListener(new ButtonExtendM.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });
    }


}
