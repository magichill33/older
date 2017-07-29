package com.nfu.old.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nfu.old.R;
import com.nfu.old.model.NewsModel;
import com.nfu.old.model.ServiceModel;
import com.nfu.old.view.ButtonExtendM;

import butterknife.BindView;

/**
 * Created by user on 2017/7/29.
 */

public class ServiceDetailFragment extends BaseFragment {
    @BindView(R.id.btn_back)
    ButtonExtendM btnBack;
    @BindView(R.id.top_title)
    TextView tv_title;

    @BindView(R.id.shopName)
    TextView shopName;
    @BindView(R.id.shopType)
    TextView shopType;
    @BindView(R.id.shopManager)
    TextView shopManager;
    @BindView(R.id.shopTelephone)
    TextView shopTelephone;
    @BindView(R.id.servicePhone)
    TextView servicePhone;
    @BindView(R.id.businessAddress)
    TextView businessAddress;
    @BindView(R.id.shopNumber)
    TextView shopNumber;
    @BindView(R.id.shopCountryName)
    TextView shopCountryName;
    @BindView(R.id.shopStreetName)
    TextView shopStreetName;
    @BindView(R.id.shopCommunityName)
    TextView shopCommunityName;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bindView(inflater, R.layout.service_detail_fragment, container);
        loadData();
        return rootView;
    }

    @Override
    protected void loadData() {
        Bundle bundle = getArguments();
        ServiceModel serviceModel = null;

        if (bundle != null) {
            serviceModel = (ServiceModel) bundle.getSerializable("servicemodel");
        }


        shopName.setText(serviceModel.getShopName());
        shopType.setText(serviceModel.getShopType());
        shopManager.setText(serviceModel.getShopManager());
        shopTelephone.setText(serviceModel.getShopManager());
        servicePhone.setText(serviceModel.getShopTelephone());
        businessAddress.setText(serviceModel.getServicePhone());
        shopNumber.setText(serviceModel.getShopNumber());
        shopCountryName.setText(serviceModel.getShopCountryName());
        shopStreetName.setText(serviceModel.getShopStreetName());
        shopCommunityName.setText(serviceModel.getShopCommunityName());;
    }

    @Override
    protected void initView() {

    }
}
