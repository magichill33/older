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

import com.google.gson.Gson;
import com.nfu.old.R;
import com.nfu.old.manager.ApiManager;
import com.nfu.old.model.Balance;
import com.nfu.old.model.NewsListModel;
import com.nfu.old.model.NewsModel;
import com.nfu.old.model.Transaction;
import com.nfu.old.utils.DensityUtil;
import com.nfu.old.utils.LogUtil;
import com.nfu.old.view.ButtonExtendM;
import com.nfu.old.view.NfuCustomDialog;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.BindView;
import okhttp3.Call;

/**
 * Created by Administrator on 2017-7-27.
 */

public class TransactionFragment extends BaseFragment implements View.OnClickListener {
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
    @BindView(R.id.btn_balance)
    Button btn_balance;
    @BindView(R.id.btn_purchase_record)
    Button btn_purchase_record;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bindView(inflater, R.layout.transaction_fragment, container);
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
        tv_title.setText(getString(R.string.home_fragment_query_str));
        card_item.setOnClickListener(this);
        btn_transact.setOnClickListener(this);
        btn_schedule.setOnClickListener(this);
        btn_balance.setOnClickListener(this);
        btn_purchase_record.setOnClickListener(this);
    }

    private void gotoDetailFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.hide(this);
        fragmentTransaction.add(R.id.activity_main_content_frameLayout , fragment);
        fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.replace(R.id.activity_main_content_frameLayout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.card_item:
                ApiManager.getInstance().getBusinessConditions("6654", new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtil.i("TransactionFragment--->getBusinessConditions--->onError::" + e);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            LogUtil.i("TransactionFragment--->getBusinessConditions--->onResponse::" + response);
                            NewsListModel models = new Gson().fromJson(response, NewsListModel.class);
                            LogUtil.i("TransactionFragment--->getBusinessConditions--->NewsListModel::" + models);
                            Transaction transaction = new Gson().fromJson(models.getStrResult(), Transaction.class);
                            LogUtil.i("TransactionFragment--->getBusinessConditions--->Transaction::" + transaction);
                            TransactionDetailFragment detailFragment = new TransactionDetailFragment();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("model", transaction);
                            detailFragment.setArguments(bundle);
                            gotoDetailFragment(detailFragment);
                        } catch (Exception e) {
                            LogUtil.i("TransactionFragment--->loadData--->Exception--->" + e);
                        }
                    }
                });
                break;
            case R.id.btn_transact:
                showTipDialog();
                break;
            case R.id.btn_schedule:
                TransactQueryFragment transactQueryFragment = new TransactQueryFragment();
                gotoDetailFragment(transactQueryFragment);
                break;
            case R.id.btn_balance:
                TransactionBalanceFragment transactionBalanceFragment = new TransactionBalanceFragment();
                gotoDetailFragment(transactionBalanceFragment);
                break;
            case R.id.btn_purchase_record:
                TransactionRecorderFragment transactionRecorderFragment = new TransactionRecorderFragment();
                gotoDetailFragment(transactionRecorderFragment);
                break;
        }
    }

    private void showTipDialog() {
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

        params.width = DensityUtil.dip2px(getContext(), 276.8f);
        params.height = DensityUtil.dip2px(getContext(), 160.5f);
        // 方式一：设置属性
        window.setAttributes(params);
        dialog.show();
    }
}
