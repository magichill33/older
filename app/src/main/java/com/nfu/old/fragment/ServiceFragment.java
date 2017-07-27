package com.nfu.old.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

import com.google.gson.Gson;
import com.nfu.old.R;
import com.nfu.old.adapter.PictureAdapter;
import com.nfu.old.manager.ApiManager;
import com.nfu.old.model.NewsListModel;
import com.nfu.old.model.NewsModels;
import com.nfu.old.utils.LogUtil;
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
    Unbinder unbinder;
    @BindView(R.id.fragment_service_content_gridview)
    GridView mGridView;
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
        initData();
        return rootView;
    }

    private void initData() {
   /*     SimpleAdapter pictureAdapter = new SimpleAdapter(getContext(), getList(),
                R.layout.gridview_item, from, to);*/

        PictureAdapter pictureAdapter=new PictureAdapter(titles, images,imagesPress, getContext());

        mGridView.setAdapter(pictureAdapter);
//        mGridView.setSelection(0);
       mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //先请求item对应的数据然后给服务二级页面

                loadData(serviceTypeId[position]);
                //gotoFragment();
            }
        });
    }

    private void gotoFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.activity_main_content_frameLayout, fragment);
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



    private void initPager() {

    }
    private void loadData(Integer serviceTypeId) {
        ApiManager.getInstance().getXbsFws(String.valueOf(serviceTypeId), 5, 0, 0, "", new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtil.i("ServiceFragment--->loadData--->getServiceList--->onError::"+e);

            }

            @Override
            public void onResponse(String response, int id) {
                LogUtil.i("ServiceFragment--->loadData--->getServiceList--->onResponse::"+response);
                NewsListModel newsListModel =  new Gson().fromJson(response,NewsListModel.class);
                LogUtil.i("PolicyFragment--->loadData--->getNewsList--->newsListModel::"+newsListModel);
                NewsModels newsModels = new Gson().fromJson(newsListModel.getStrResult(),NewsModels.class);
                LogUtil.i("PolicyFragment--->loadData--->getNewsList--->NewsModels::"+newsModels);
//                policyListAdapter.setNewsData(newsModels.getData());
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
