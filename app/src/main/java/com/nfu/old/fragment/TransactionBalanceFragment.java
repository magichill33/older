package com.nfu.old.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nfu.old.R;
import com.nfu.old.manager.ApiManager;
import com.nfu.old.model.Balance;
import com.nfu.old.model.Transaction;
import com.nfu.old.utils.LogUtil;
import com.nfu.old.utils.ToastUtil;
import com.nfu.old.view.ButtonExtendM;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.BindView;
import okhttp3.Call;

/**
 * Created by Administrator on 2017-7-27.
 */

public class TransactionBalanceFragment extends BaseFragment {
    @BindView(R.id.btn_back)
    ButtonExtendM btnBack;
    @BindView(R.id.top_title)
    TextView tv_title;
    @BindView(R.id.et_carnum)
    EditText ed_cardnum;
    @BindView(R.id.btn_balance)
    Button btn_query;
    @BindView(R.id.tv_balance)
    TextView tv_balance;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bindView(inflater, R.layout.transaction_balance_fragment,container);
        initView();
        loadData();
        return rootView;
    }

    @Override
    protected void loadData() {
        Bundle bundle = getArguments();

    }

    @Override
    protected void initView() {
        btnBack.setOnClickListener(new ButtonExtendM.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });
        tv_title.setText("余额查询");
        btn_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cardnum = ed_cardnum.getText().toString();
                if (!TextUtils.isEmpty(cardnum)){
                    ApiManager.getInstance().getOlderBalance(cardnum, "2", new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            LogUtil.i("TransactionBalanceFragment--->getOlderBalance--->"+e);
                            ToastUtil.showShortToast(getContext(), R.string.str_errorinfo);
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            LogUtil.i("TransactionBalanceFragment--->getOlderBalance--->onResponse"+response);
                            Balance balance = new Gson().fromJson(response,Balance.class);
                            if ("success".equalsIgnoreCase(balance.getStatus())){
                                tv_balance.setText("您的余额为："+ balance.getBalance() + "元");
                            }else {
                                ToastUtil.showShortToast(getContext(),"此号暂时查不出金额来");
                            }

                        }
                    });
                }else {
                    ToastUtil.showShortToast(getContext(),"请输入养老助残卡号");
                }

            }
        });

    }

}
