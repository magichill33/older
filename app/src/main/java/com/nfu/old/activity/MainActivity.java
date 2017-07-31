package com.nfu.old.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import java.lang.reflect.Type;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.R.attr.fragment;
import static com.baidu.location.d.j.P;

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
    HomeFragment homeFragment;
    ConsultFragment consultFragment;
    ServiceFragment serviceFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);
        if(savedInstanceState ==null){
            setHomeFragment();
        }
        initView();
    }

    private void setHomeFragment() {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        if(homeFragment == null){
            homeFragment = new HomeFragment();
        }
        fragmentTransaction.replace(R.id.activity_main_content_frameLayout, homeFragment);
        fragmentTransaction.commit();

    }

    boolean isHomeClick = false;
    boolean isServiceClick = false;
    boolean isConsultClick = false;

    private void initView() {
        btnHome.setNfuSeleted(true);

        btnHome.setOnClickListener(new ButtonExtendM.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearSelected();
                btnHome.setNfuSeleted(true);
                Fragment fragment = getVisibleFragment();
                if (fragment==null||!(fragment instanceof HomeFragment)){
                    setHomeFragment();
                }



            }
        });


        btnConsult.setOnClickListener(new ButtonExtendM.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearSelected();
                btnConsult.setNfuSeleted(true);
                Fragment fragment = getVisibleFragment();
                if (fragment==null||!(fragment instanceof ConsultFragment)){
                    setConsultFragment();
                }
            }
        });

        btnService.setOnClickListener(new ButtonExtendM.OnClickListener()

        {
            @Override
            public void onClick(View v) {

                clearSelected();
                btnService.setNfuSeleted(true);
                Fragment fragment = getVisibleFragment();
                if (fragment==null||!(fragment instanceof ServiceFragment)){
                    setServiceFragment();
                }
            }
        });
    }

    private Fragment getVisibleFragment(){
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment f:fragments){
            if (f!=null && f.isVisible()){
               return f;
            }
        }
        return null;
    }

    private void setServiceFragment() {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        if(serviceFragment == null){
            serviceFragment = new ServiceFragment();
        }
        fragmentTransaction.replace(R.id.activity_main_content_frameLayout, serviceFragment);
        fragmentTransaction.commit();
    }


    private void setConsultFragment() {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        if(consultFragment == null){
            consultFragment = new ConsultFragment();
        }
        fragmentTransaction.replace(R.id.activity_main_content_frameLayout, consultFragment);
        fragmentTransaction.commit();
    }


    private void clearSelected() {
        btnHome.setNfuSeleted(false);
        btnConsult.setNfuSeleted(false);
        btnService.setNfuSeleted(false);
//        isClick = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
