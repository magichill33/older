package com.nfu.old.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.google.gson.Gson;
import com.nfu.old.Constant;
import com.nfu.old.R;
import com.nfu.old.adapter.PictureAdapter;
import com.nfu.old.manager.ApiManager;
import com.nfu.old.model.NewsListModel;
import com.nfu.old.model.NewsModels;
import com.nfu.old.model.ServiceListModel;
import com.nfu.old.model.ServiceModels;
import com.nfu.old.utils.LogUtil;
import com.nfu.old.utils.map.MyLocationListener;
import com.nfu.old.view.ButtonExtendM;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;

/**
 * Created by Administrator on 2017/7/25.
 */

public class ServiceFragment extends Fragment {
    private static final int ACCESS_COARSE_LOCATION_REQUEST_CODE = 1;
    Unbinder unbinder;
    @BindView(R.id.fragment_service_content_gridview)
    GridView mGridView;
    @BindView(R.id.fragment_service_location_ib)
    ButtonExtendM loctionBtn;
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener;

    private String[] from = { "image", "title" };
    private int[] to = { R.id.item_pic, R.id.item_pic_title};
    String[] titles = new String[] { "超级商场", "餐饮", "家政服务", "社区便利店"
            , "医药医疗" ,"日间照料","养老机构","生活照料","文化娱乐","幸福彩虹"};
    Integer[] images = { R.drawable.service_item_pic_chaojishichang_selected, R.drawable.service_item_pic_canyin_unselected,
            R.drawable.service_item_pic_jiazhengfuwu_unselected, R.drawable.service_item_pic_shequ_unselected,
            R.drawable.service_item_pic_yiyaoyiliao_unselected ,R.drawable.service_item_pic_rijian_unselected,
            R.drawable.service_item_pic_yanglaojigou_unselected,R.drawable.service_item_pic_shenghuo_selected,
            R.drawable.service_item_pic_wenhua_unselected,R.drawable.service_item_pic_xingfu_unselected};
    Integer[] imagesPress = { R.drawable.service_item_pic_chaojishichang_unselected, R.drawable.service_item_pic_canyin_selected,
            R.drawable.service_item_pic_jiazhengfuwu_selected, R.drawable.service_item_pic_shequ_selected,
            R.drawable.service_item_pic_yiyaoyiliao_selected ,R.drawable.service_item_pic_rijian_selected,
            R.drawable.service_item_pic_yanglaojigou_selected,R.drawable.service_item_pic_shenghuo_unselected,
            R.drawable.service_item_pic_wenhua_selected,R.drawable.service_item_pic_xingfu_selected};
    /**
     * 服务商分类ID:
     * 超市商场7764,餐饮7765,家政服务7768,生活照料7770,医药医疗7774,
     * 日间照料7775,养老机构7778,文化娱乐7779,社区便利店7814,幸福彩虹7877
     */
    Integer[] serviceTypeId ={7764,7765,7768,7770,7774,
                                7775,7778,7779,7814,7877};
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.service_fragment, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initView();
        initData();
        return rootView;
    }

    private void initView() {
        loctionBtn.setVisibility(View.VISIBLE);
        loctionBtn.setOnClickListener(new ButtonExtendM.OnClickListener() {
            @Override
            public void onClick(View v) {
                //调用刷新位置信息

            }
        });
    }

    private void initData() {
        initMap();
   /*     SimpleAdapter pictureAdapter = new SimpleAdapter(getContext(), getList(),
                R.layout.gridview_item, from, to);*/

        PictureAdapter pictureAdapter=new PictureAdapter(titles, images,imagesPress, getContext());
        pictureAdapter.setOnItemClickCallBack(new PictureAdapter.OnItemClickCallBack() {
            @Override
            public void onItemClickCallBack(int position) {
                //先请求item对应的数据然后给服务二级页面

                loadData(serviceTypeId[position]);
                //gotoFragment();
            }
        });
        mGridView.setAdapter(pictureAdapter);
//        mGridView.setSelection(0);
     /*  mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //先请求item对应的数据然后给服务二级页面

                loadData(serviceTypeId[position]);
                //gotoFragment();
            }
        });*/


    }

    private void initMap() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },
                    ACCESS_COARSE_LOCATION_REQUEST_CODE);
        }else {
            getLocate();
        }
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

   /* public List<Map<String, Object>> getList() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = null;

        String[] titles = new String[] { "超级商场", "餐饮", "家政服务", "社区便利店"
                , "医药医疗" ,"日间照料","养老机构","生活照料","文化娱乐","幸福彩虹"};
        Integer[] images = { R.drawable.service_item_pic_chaojishichang_unselected, R.drawable.service_item_pic_canyin_unselected,
                             R.drawable.service_item_pic_jiazhengfuwu_unselected, R.drawable.service_item_pic_shequ_unselected,
                            R.drawable.service_item_pic_yiyaoyiliao_unselected ,R.drawable.service_item_pic_rijian_unselected,
                            R.drawable.service_item_pic_yanglaojigou_unselected,R.drawable.service_item_pic_shenghuo_unselected,
                            R.drawable.service_item_pic_wenhua_unselected,R.drawable.service_item_pic_xingfu_unselected};

        for (int i = 0; i < images.length; i++) {
            map = new HashMap<String, Object>();
            map.put("image", images[i]);
            map.put("title", titles[i]);
            list.add(map);
        }
        return list;
    }*/


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ACCESS_COARSE_LOCATION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                getLocate();
            } else {
                // Permission Denied
                Toast.makeText(getActivity(), "访问被拒绝！", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void initPager() {

    }
    private void loadData(Integer serviceTypeId) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        ServiceListFragment serviceListFragment = new ServiceListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title","服务查询");
         bundle.putInt("serviceTypeId",serviceTypeId);
        serviceListFragment.setArguments(bundle);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.activity_main_content_frameLayout, serviceListFragment);
        fragmentTransaction.commit();
/*        ApiManager.getInstance().getXbsFws(String.valueOf(serviceTypeId), 5, 0, 0, "", "","",new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtil.i("ServiceFragment--->loadData--->getServiceList--->onError::"+e);

            }

            @Override
            public void onResponse(String response, int id) {
                LogUtil.i("ServiceFragment--->loadData--->getServiceList--->onResponse::"+response);
                ServiceListModel servicesListModel =  new Gson().fromJson(response,ServiceListModel.class);
                LogUtil.i("ServiceFragment--->loadData--->getServiceList--->servicesListModel::"+servicesListModel);
                ServiceModels serviceModels = new Gson().fromJson(servicesListModel.getStrResult(),ServiceModels.class);
                LogUtil.i("ServiceFragment--->loadData--->getServiceList--->ServiceModels::"+serviceModels);
                gotoServiceListFragment(serviceModels);
            }
        });*/

    }
    public void getLocate() {
        mLocationClient = new LocationClient(getContext().getApplicationContext());     //声明LocationClient类
        myListener = new MyLocationListener(mLocationClient);
        initLocation();
        mLocationClient.registerLocationListener(myListener);    //注册监听函数
        mLocationClient.start();
    }
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要


        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

    private void gotoServiceListFragment(ServiceModels serviceModels) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        ServiceListFragment serviceListFragment = new ServiceListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title","服务查询");
        bundle.putSerializable("servicemodels",serviceModels);
        serviceListFragment.setArguments(bundle);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.activity_main_content_frameLayout, serviceListFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
