package com.nfu.old.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nfu.old.R;
import com.nfu.old.fragment.ServiceFragment;

import java.util.ArrayList;
import java.util.List;

import static com.nfu.old.R.attr.iconDrawablePress;

/**
 * Created by Administrator on 2017/7/25.
 */

public class PictureAdapter extends BaseAdapter {
    private Context context;
    Integer[] mImagesPress;
    Integer[] mImages;
    private List<Picture> pictures=new ArrayList<Picture>();

    public PictureAdapter(String[] titles, Integer[] images, Integer[] imagesPress ,Context context) {
        super();
        this.context = context;
        this.mImagesPress = imagesPress;
        this.mImages = images;
        for (int i = 0; i < images.length; i++) {
            Picture picture = new Picture(titles[i], images[i]);
            pictures.add(picture);
        }

    }

    @Override
    public int getCount() {

        if (null != pictures) {
            return pictures.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {

        return pictures.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }
    ImageView itemBtn;
    TextView itemTv;
    View view;
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            // 获得容器
            convertView = LayoutInflater.from(this.context).inflate(R.layout.gridview_item, null);

            // 初始化组件
            viewHolder.image = (ImageView) convertView.findViewById(R.id.item_pic);
            viewHolder.title = (TextView) convertView.findViewById(R.id.item_pic_title);
            // 给converHolder附加一个对象
            convertView.setTag(viewHolder);
        } else {
            // 取得converHolder附加的对象
            viewHolder = (ViewHolder) convertView.getTag();

        }

        // 给组件设置资源
        Picture picture = pictures.get(position);
        viewHolder.image.setImageResource(picture.getImageId());
        viewHolder.title.setText(picture.getTitle());
        viewHolder.image.setTag(position);

        convertView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                view  =v;

                itemBtn = (ImageView) v.findViewById(R.id.item_pic);
                itemTv = (TextView) v.findViewById(R.id.item_pic_title);
                int  position = (int)itemBtn.getTag();
                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    Log.d("PictureAdapter","ACTION_DOWN  position = " +position );
                    switch (position) {
                        case 0:
                            changeDown(0);

                            break;
                        case 1:
                            changeDown(1);
                            break;
                        case 2:
                            changeDown(2);
                            break;
                        case 3:
                            changeDown(3);
                            break;
                        case 4:
                            changeDown(4);
                            break;
                        case 5:
                            changeDown(5);
                            break;
                        case 6:
                            changeDown(6);
                            break;
                        case 7:
                            changeDown(7);
                            break;
                        case 8:
                            changeDown(8);
                            break;
                        case 9:
                            changeDown(9);
                            break;
                    }
                    return true;
                }else if(event.getAction() == MotionEvent.ACTION_MOVE){
                    Log.d("PictureAdapter","ACTION_MOVE position = " +position );
                    return false;
                } else if(event.getAction() == MotionEvent.ACTION_UP||event.getAction() == MotionEvent.ACTION_CANCEL){
                    if(event.getAction() == MotionEvent.ACTION_UP){
                        Log.d("PictureAdapter","ACTION_UP  position = " +position);
                    }else if(event.getAction() == MotionEvent.ACTION_CANCEL) {
                        Log.d("PictureAdapter","ACTION_CANCEL position = " +position );
                    }
                    switch (position) {
                        case 0:
                            changeUp(0);
                            break;
                        case 1:
                            changeUp(1);
                            break;
                        case 2:
                            changeUp(2);

                            break;
                        case 3:
                            changeUp(3);
                            break;
                        case 4:
                            changeUp(4);
                            break;
                        case 5:
                            changeUp(5);
                            break;
                        case 6:
                            changeUp(6);
                            break;
                        case 7:
                            changeUp(7);
                            break;
                        case 8:
                            changeUp(8);
                            break;
                        case 9:
                            changeUp(9);
                            break;
                    }
                    return false;
                }
                return false;
            }
        });

        return convertView;
    }
    private void changeDown(int position) {
        //textView颜色变换，item背景切换，imageview切换
//        itemBtn.setBackgroundColor(mImagesPress[position]);
        itemBtn.setImageDrawable(context.getResources().getDrawable(mImagesPress[position]));
        view.setBackgroundColor(context.getResources().getColor(R.color.base_red_color));
        itemTv.setTextColor(Color.WHITE);
    }
    private void changeUp(int position) {
        //textView颜色变换，item背景切换，imageview切换
//        itemBtn.setBackgroundColor(mImages[position]);
        itemBtn.setImageDrawable(context.getResources().getDrawable(mImages[position]));
        view.setBackgroundColor(context.getResources().getColor(R.color.white));
        itemTv.setTextColor(context.getResources().getColor(R.color.base_red_color));
        //跳转对应详情页面
        mCallBack.onItemClickCallBack(position);
    }
    OnItemClickCallBack mCallBack;
    public void setOnItemClickCallBack(OnItemClickCallBack callBack) {
        this.mCallBack = callBack;
    }
    public interface  OnItemClickCallBack  {
       void onItemClickCallBack(int position);
    }
    class ViewHolder {
        public TextView title;
        public ImageView image;
    }

    class Picture {

        private String title;
        private int imageId;

        public Picture(String title, Integer imageId) {
            this.imageId = imageId;
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

        public int getImageId() {
            return imageId;
        }

    }
}
