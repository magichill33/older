package com.nfu.old.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kyleduo.switchbutton.SwitchButton;
import com.nfu.old.R;
import com.nfu.old.config.NfuResource;
import com.nfu.old.fragment.BaseFragment;
import com.nfu.old.model.NewsModel;
import com.nfu.old.utils.ToastUtil;
import com.nfu.old.view.ButtonExtendM;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/7/25.
 * 咨询页面
 */

public class SettingFragment extends BaseFragment implements View.OnClickListener{
    @BindView(R.id.btn_back)
    ButtonExtendM btnBack;
    @BindView(R.id.top_title)
    TextView tv_title;

    @BindView(R.id.card_view1)
    CardView cardView1;
    @BindView(R.id.card_view2)
    CardView cardView2;
    @BindView(R.id.card_view3)
    CardView cardView3;
    @BindView(R.id.card_view4)
    CardView cardView4;
    @BindView(R.id.card_view5)
    CardView cardView5;
    @BindView(R.id.card_view6)
    CardView cardView6;
    @BindView(R.id.card_view7)
    CardView cardView7;
    @BindView(R.id.switchbutton)
    SwitchButton switchbutton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bindView(inflater,R.layout.setting_fragment,container);
        initView();
        loadData();
        return rootView;
    }




    @Override
    protected void loadData() {

    }

    @Override
    protected void initView() {
        if (NfuResource.getInstance().isUse4G()){
            switchbutton.setChecked(true);
        }
        tv_title.setText(R.string.setting_str);

        cardView1.setOnClickListener(this);
        cardView3.setOnClickListener(this);
        cardView4.setOnClickListener(this);
        cardView5.setOnClickListener(this);
        cardView6.setOnClickListener(this);
        cardView7.setOnClickListener(this);


        switchbutton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    NfuResource.getInstance().updateUse4G(true);
                }else {
                    NfuResource.getInstance().updateUse4G(false);
                }
            }
        });

        btnBack.setOnClickListener(new ButtonExtendM.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });


    }


    private void gotoDetailFragment(NewsModel newsModel){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        NewsDetailFragment newsDetailFragment = new NewsDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title",getString(R.string.setting_str));
        bundle.putSerializable("news",newsModel);
        newsDetailFragment.setArguments(bundle);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.activity_main_content_frameLayout, newsDetailFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.card_view1:
                cardView1.setEnabled(false);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.get(getContext()).clearDiskCache();
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                cardView1.setEnabled(true);
                                ToastUtil.showShortToast(getContext(),"清除图片缓存成功！");
                            }
                        });
                    }
                }).start();
                break;
            case R.id.card_view3:
                break;
            case R.id.card_view4:
                break;
            case R.id.card_view5:
                break;
            case R.id.card_view6:
                UpdateFragment updateFragment = new UpdateFragment();
                gotoFragment(updateFragment);
                break;
            case R.id.card_view7:
                break;

        }
    }
    private void gotoFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.hide(this);
        fragmentTransaction.add(R.id.activity_main_content_frameLayout , fragment);
//        fragmentTransaction.replace(R.id.activity_main_content_frameLayout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
