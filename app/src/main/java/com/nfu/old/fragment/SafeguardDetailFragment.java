package com.nfu.old.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nfu.old.R;
import com.nfu.old.config.ApiConfig;
import com.nfu.old.manager.ApiManager;
import com.nfu.old.model.Feedback;
import com.nfu.old.utils.DensityUtil;
import com.nfu.old.utils.LogUtil;
import com.nfu.old.utils.ToastUtil;
import com.nfu.old.view.ButtonExtendM;
import com.nfu.old.view.NfuCustomDialog;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.BindView;
import okhttp3.Call;

/**
 * Created by Administrator on 2017/7/25.
 * 咨询页面
 */

public class SafeguardDetailFragment extends BaseFragment {
    @BindView(R.id.btn_back)
    ButtonExtendM btnBack;
    @BindView(R.id.top_title)
    TextView tv_title;
    @BindView(R.id.et_question)
    EditText ed_question;
    @BindView(R.id.constacts_msg_name_et)
    EditText ed_name;
    @BindView(R.id.constacts_msg_number_et)
    EditText ed_phone;
    @BindView(R.id.btn_submit)
    Button btn_submit;

    private int type = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bindView(inflater,R.layout.safeguard_detail_fragment,container);
        initView();
        loadData();
        return rootView;
    }




    @Override
    protected void loadData() {
        Bundle bundle = getArguments();
        type = bundle.getInt("type");
        if (type == 1){
            ed_question.setHint(R.string.right_str1);
        }else {
            ed_question.setHint(R.string.right_str2);
        }
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
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(ed_name.getText())||TextUtils.isEmpty(ed_phone.getText())||TextUtils.isEmpty(ed_question.getText())){
                    ToastUtil.showShortToast(getContext(), R.string.feedback_str);
                }else {
                    btn_submit.setClickable(false);
                    Feedback feedback = new Feedback();
                    feedback.setSignKey(ApiConfig.signKey);
                    feedback.setContacterMobile(ed_phone.getText().toString());
                    feedback.setContacterName(ed_name.getText().toString());
                    feedback.setOpinionType(type);
                    feedback.setFeedbackContent(ed_question.getText().toString());
                    String str = new Gson().toJson(feedback);
                    ApiManager.getInstance().postOpinionFeedBack(str, new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            LogUtil.i("SafeguardDetailFragment--->postOpinionFeedBack--->onError--->"+e);
                            ToastUtil.showShortToast(getContext(),R.string.feedback_str_error);
                            btn_submit.setClickable(true);
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            LogUtil.i("SafeguardDetailFragment--->postOpinionFeedBack--->onResponse--->"+response);
                            //ToastUtil.showShortToast(getContext(),R.string.feedback_str_ok);
                            showTipDialog();
                            btn_submit.setClickable(true);
                        }
                    });
                }

            }
        });
    }

    private void showTipDialog(){
        NfuCustomDialog.Builder builder = new NfuCustomDialog.Builder(getContext());
        builder.setMessage(getString(R.string.submit_ok));
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
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
