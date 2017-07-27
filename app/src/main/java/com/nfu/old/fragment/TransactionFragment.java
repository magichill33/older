package com.nfu.old.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nfu.old.R;
import com.nfu.old.model.NewsModel;
import com.nfu.old.utils.DensityUtil;
import com.nfu.old.view.ButtonExtendM;
import com.nfu.old.view.NfuCustomDialog;

import butterknife.BindView;

/**
 * Created by Administrator on 2017-7-27.
 */

public class TransactionFragment extends BaseFragment implements View.OnClickListener{
    @BindView(R.id.btn_back)
    ButtonExtendM btnBack;
    @BindView(R.id.top_title)
    TextView tv_title;
    @BindView(R.id.card_item)
    RelativeLayout card_item;
    @BindView(R.id.btn_transact)
    Button btn_transact;
    @BindView(R.id.btn_schedule)
    Button btn_schedule;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bindView(inflater, R.layout.transaction_fragment,container);
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
        tv_title.setText("办理查询");
        card_item.setOnClickListener(this);
        btn_transact.setOnClickListener(this);
        btn_schedule.setOnClickListener(this);
    }

    private void gotoDetailFragment(Fragment fragment){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.activity_main_content_frameLayout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.card_item:
                break;
            case R.id.btn_transact:
                showTipDialog();
                break;
            case R.id.btn_schedule:
                TransactQueryFragment transactQueryFragment = new TransactQueryFragment();
                gotoDetailFragment(transactQueryFragment);
                break;
        }
    }

    private void showTipDialog(){
        NfuCustomDialog.Builder builder = new NfuCustomDialog.Builder(getContext());
        builder.setMessage("目前业务只支持银行柜台办理");
        builder.setPositiveButton("返回", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        Dialog dialog = builder.create();
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        // 获得代表当前window属性的对象
        WindowManager.LayoutParams params = window.getAttributes();

        params.width = DensityUtil.dip2px(getContext(),276.8f);
        params.height = DensityUtil.dip2px(getContext(),160.5f);
        // 方式一：设置属性
        window.setAttributes(params);
        dialog.show();
    }
}
