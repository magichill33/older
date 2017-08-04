package com.nfu.old.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nfu.old.R;
import com.nfu.old.adapter.ConsultListAdapter;
import com.nfu.old.utils.ToastUtil;
import com.nfu.old.view.ButtonExtendM;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by user on 2017/8/3.
 */

public class OldServiceFragment extends BaseFragment {
    @BindView(R.id.btn_back)
    ButtonExtendM btnBack;
    @BindView(R.id.top_title)
    TextView tv_title;
    @BindView(R.id.oldservice_recyclerview)
    RecyclerView oldservice_recyclerview;
    private ConsultListAdapter policyListAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.oldservice_fragment, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initView();
        loadData();
        return rootView;
    }

    @Override
    protected void loadData() {
        tv_title.setText(R.string.oldservice_top_title);

    }

    @Override
    protected void initView() {
        String[] mTitles  = {"全市养老服务机构","社区养老服务场所","养老服务商"};
        int[] mIconId = {R.drawable.cityservice,R.drawable.shequ,R.drawable.yanglaofuwufu};
        oldservice_recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        policyListAdapter = new ConsultListAdapter(getContext(), mTitles, mIconId,new ConsultListAdapter.IOnDetailListener() {

            @Override
            public void onDetailListener(String title) {
                if ("全市养老服务机构".equals(title)){
//                    ToastUtil.showShortToast(getActivity(),"后台正在开发接口中");\
                    CityOldServiceFragment cityOldServiceFragment = new CityOldServiceFragment();
                    gotoFragment(cityOldServiceFragment);
                }else if ("社区养老服务场所".equals(title)){
                    CommunityOldServiceFragment communityOldServiceFragment = new CommunityOldServiceFragment();
                    gotoFragment(communityOldServiceFragment);
                }else if ("养老服务商".equals(title)){
//
                    ServiceFragment serviceFragment = new ServiceFragment();
                    gotoFragment(serviceFragment);
                }
            }
        });
        oldservice_recyclerview.setAdapter(policyListAdapter);
        btnBack.setOnClickListener(new ButtonExtendM.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });
    }


    private void gotoFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.hide(this);
        fragmentTransaction.add(R.id.activity_main_content_frameLayout , fragment);
        fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.replace(R.id.activity_main_content_frameLayout, fragment);
        fragmentTransaction.commit();
    }

}
