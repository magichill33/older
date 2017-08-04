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
import com.nfu.old.view.ButtonExtendM;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by user on 2017/8/3.
 */

public class OldConsultFragment extends BaseFragment {
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
        tv_title.setText(R.string.oldconsult_fragment_top_title);

    }

    @Override
    protected void initView() {
        String[] mTitles  = {"53家老龄委成员单位","16区老龄办电话","市级老龄工作机构"};
        int[] mIconId = {R.drawable.consult_qujidongtai_bg,R.drawable.consult_zhengcejiedu,R.drawable.consult_meitibaodao_bg};
        oldservice_recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        policyListAdapter = new ConsultListAdapter(getContext(), mTitles, mIconId,new ConsultListAdapter.IOnDetailListener() {

            @Override
            public void onDetailListener(String title) {
                if ("53家老龄委成员单位".equals(title)){
//                    ServiceFragment serviceFragment = new ServiceFragment();
//                    gotoFragment(serviceFragment);
                }else if ("16区老龄办电话".equals(title)){
//                    PolicyFragment policyFragment = new PolicyFragment();
//                    gotoFragment(policyFragment);
                }else if ("市级老龄工作机构".equals(title)){
//                    MediaFragment mediaFragment = new MediaFragment();
//                    gotoFragment(mediaFragment);
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
