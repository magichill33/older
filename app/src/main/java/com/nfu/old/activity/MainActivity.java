package com.nfu.old.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;


import com.nfu.old.R;
import com.nfu.old.fragment.ConsultFragment;
import com.nfu.old.fragment.HomeFragment;
import com.nfu.old.fragment.ServiceFragment;
import com.nfu.old.view.ButtonExtendM;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity {

    Unbinder unbinder;

    @BindView(R.id.btn_home)
    ButtonExtendM btnHome;
    @BindView(R.id.btn_consult)
    ButtonExtendM btnConsult;
    @BindView(R.id.btn_service)
    ButtonExtendM btnService;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    @BindView(R.id.activity_main_content_frameLayout)
    FrameLayout mContentView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);

        setHomeFragment();
        initView();
    }

    private void setHomeFragment(){
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        HomeFragment fragment = new HomeFragment();
        fragmentTransaction.replace(R.id.activity_main_content_frameLayout, fragment);
        fragmentTransaction.commit();
    }

    private void initView(){
        btnHome.setNfuSeleted(true);
        btnHome.setOnClickListener(new ButtonExtendM.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearSelected();
                btnHome.setNfuSeleted(true);
            }
        });

        btnConsult.setOnClickListener(new ButtonExtendM.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearSelected();
                btnConsult.setNfuSeleted(true);
                setConsultFragment();
            }
        });

        btnService.setOnClickListener(new ButtonExtendM.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearSelected();
                btnService.setNfuSeleted(true);
                setServiceFragment();
                
            }
        });
    }

    private void setServiceFragment() {
        ServiceFragment fragment = new ServiceFragment();
        fragmentTransaction.replace(R.id.activity_main_content_frameLayout, fragment);
        fragmentTransaction.commit();
    }


    private void setConsultFragment() {
        ConsultFragment fragment = new ConsultFragment();
        fragmentTransaction.replace(R.id.activity_main_content_frameLayout, fragment);
        fragmentTransaction.commit();
    }

    private void clearSelected(){
        btnHome.setNfuSeleted(false);
        btnConsult.setNfuSeleted(false);
        btnService.setNfuSeleted(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
