package com.nfu.old.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nfu.old.R;
import com.nfu.old.model.Transaction;
import com.nfu.old.utils.DensityUtil;
import com.nfu.old.view.ButtonExtendM;
import com.nfu.old.view.NfuCustomDialog;

import butterknife.BindView;

/**
 * Created by Administrator on 2017-7-27.
 */

public class TransactionDetailFragment extends BaseFragment {
    @BindView(R.id.btn_back)
    ButtonExtendM btnBack;
    @BindView(R.id.top_title)
    TextView tv_title;
    @BindView(R.id.tv_tj)
    TextView tv_tj;
    @BindView(R.id.tv_ff)
    TextView tv_ff;
    @BindView(R.id.tv_sm)
    TextView tv_sm;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bindView(inflater, R.layout.transaction_detail_fragment,container);
        initView();
        loadData();
        return rootView;
    }

    @Override
    protected void loadData() {
        Bundle bundle = getArguments();
        Transaction transaction = (Transaction) bundle.getSerializable("model");
        tv_tj.setText(Html.fromHtml(transaction.getBltj()));
        tv_ff.setText(Html.fromHtml(transaction.getBlff()));
        tv_sm.setText(Html.fromHtml(transaction.getTbsm()));
    }

    @Override
    protected void initView() {
        btnBack.setOnClickListener(new ButtonExtendM.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });
        tv_title.setText("办事指南");

    }

}
