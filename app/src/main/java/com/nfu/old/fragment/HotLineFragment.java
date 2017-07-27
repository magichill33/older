
package com.nfu.old.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nfu.old.R;
import com.nfu.old.model.ServiceModels;
import com.nfu.old.utils.AppUtils;
import com.nfu.old.view.ButtonExtendM;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.nfu.old.R.id.top_title;

/**
 * Created by user on 2017/7/27.
 */

public class HotLineFragment extends Fragment {
    Unbinder unbinder;
    @BindView(R.id.hotline_number)
    TextView hotlineNumber;
    @BindView(R.id.top_title)
    TextView toptitle;
    @BindView(R.id.hotline_call)
    ImageView hotlineCall;

    @BindView(R.id.btn_back)
    ButtonExtendM btnBack;
    private String title = "资询";
    private String hotlineCallPhone;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.hotline_fragment, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initView();
        loadData();
        return rootView;
    }

    private void loadData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            title = bundle.getString("title");
        }

        toptitle.setText(title);
    }


    private void initView() {

        btnBack.setOnClickListener(new ButtonExtendM.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });
        hotlineCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 调用打掉话界面
                AppUtils.call(getContext(),hotlineNumber.getText().toString());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
