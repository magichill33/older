package com.nfu.old.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nfu.old.R;

/**
 * Created by user on 2017/7/25.
 */

public class ConsultItemView extends RelativeLayout {

    /**
     * 定义控件
     */
    private ImageView ivIcon;
    private ImageButton goIcon;
    private TextView tvContent;


    public ConsultItemView(Context context) {

        this(context,null,0);
    }

    public ConsultItemView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ConsultItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData(context, attrs, defStyleAttr);
    }

    private void initData(Context context, AttributeSet attrs, int defStyle) {
        ///加载布局
       LayoutInflater.from(getContext()).inflate(R.layout.fragment_consult_item,this,true);
        //  View view = View.inflate(context, R.layout.fragment_consult_item, this);

        //初始化控件
        ivIcon = (ImageView) findViewById(R.id.fragment_consult_item_imageview);
        tvContent = (TextView) findViewById(R.id.fragment_consult_title_textview);
        goIcon = (ImageButton) findViewById(R.id.fragment_consult_go_ib);
        // 拿到布局文件中的数据
        TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.ConsultItemView, defStyle, 0);
        if(a!=null){

            String des = a.getString(R.styleable.ConsultItemView_text_content);
            int itemBg = a.getInt(R.styleable.ConsultItemView_image_bg,0);
            tvContent.setText(des);
            ivIcon.setBackgroundResource(itemBg);
        }
      //  this.addView(view);
    }
}
