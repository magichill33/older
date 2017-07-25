package com.nfu.old.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.nfu.old.R;
import com.nfu.old.adapter.PictureAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

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
    Integer[] images = { R.drawable.service_item_pic_chaojishichang_unselected, R.drawable.service_item_pic_canyin_unselected,
            R.drawable.service_item_pic_jiazhengfuwu_unselected, R.drawable.service_item_pic_shequ_unselected,
            R.drawable.service_item_pic_yiyaoyiliao_unselected ,R.drawable.service_item_pic_rijian_unselected,
            R.drawable.service_item_pic_yanglaojigou_unselected,R.drawable.service_item_pic_shenghuo_selected,
            R.drawable.service_item_pic_wenhua_unselected,R.drawable.service_item_pic_xingfu_unselected};
    Integer[] imagesPress = { R.drawable.service_item_pic_chaojishichang_selected, R.drawable.service_item_pic_canyin_selected,
            R.drawable.service_item_pic_jiazhengfuwu_selected, R.drawable.service_item_pic_shequ_selected,
            R.drawable.service_item_pic_yiyaoyiliao_selected ,R.drawable.service_item_pic_rijian_selected,
            R.drawable.service_item_pic_yanglaojigou_selected,R.drawable.service_item_pic_shenghuo_unselected,
            R.drawable.service_item_pic_wenhua_selected,R.drawable.service_item_pic_xingfu_selected};


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

        PictureAdapter pictureAdapter=new PictureAdapter(titles, images, getContext());

        mGridView.setAdapter(pictureAdapter);
        mGridView.setSelection(0);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
//              adapter.setSelection(position);

                ImageView itemBtn = (ImageView) view.findViewById(R.id.item_pic);
                TextView itemTv = (TextView) view.findViewById(R.id.item_pic_title);
                if (view.isSelected() == false) {
                    itemTv.setTextColor(Color.WHITE);

                } else {
//                    itemBtn.setSelected(false);
                    itemBtn.setBackgroundColor(imagesPress[position]);
                    itemTv.setTextColor(getResources().getColor(R.color.base_red_color));
                }
            }
//              adapter.setSelection(position);
//             adapter.notifyDataSetChanged();
        });
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
    private void loadData() {
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
