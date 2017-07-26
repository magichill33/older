package com.nfu.old.view;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.nfu.old.R;
import com.nfu.old.utils.SharedPreferencesManager;

/**
 * Created by Administrator on 2017/7/26.
 */

public class ContactMsgWindow {
    private  int mCode;
    private Context mContext;
    private EditText nameET;
    private EditText numberET;
    private Button commitBtn;
    public PopupWindow mPopWindow;
    public String name;
    private String number;
    private MsgCallBack mCallback;

    public ContactMsgWindow(Context context, int code) {
        this.mContext = context;
        this.mCode = code;
        initLayout();
        initEvent();
    }

    private void initLayout() {
        //设置contentView
        //getWindowManager().getDefaultDisplay().getWidth(),getWindowManager().getDefaultDisplay().getHeight()
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.home_constacts_msg_window, null);
        mPopWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        mPopWindow.setContentView(contentView);
        nameET = (EditText) contentView.findViewById(R.id.constacts_msg_name_et);
        numberET = (EditText) contentView.findViewById(R.id.constacts_msg_number_et);
//        interactorCid.setSelection(cid.length());

        switch (mCode) {
            case 1:
                String namefirst = SharedPreferencesManager.getString("contacts_msg_first", "name", "儿子");
                nameET.setText(namefirst);
                nameET.setSelection(namefirst.length());
                String numberfirst = SharedPreferencesManager.getString("contacts_msg_first", "number", "13688888888");
                numberET.setText(numberfirst);
                break;
            case 2:
                String nameSecond = SharedPreferencesManager.getString("contacts_msg_second", "name", "警察");
                nameET.setText(nameSecond);
                nameET.setSelection(nameSecond.length());
                String numberSecond = SharedPreferencesManager.getString("contacts_msg_second", "number", "13688888888");
                numberET.setText(numberSecond);
                break;
            case 3:
                String nameThird = SharedPreferencesManager.getString("contacts_msg_third", "name", "女儿");
                nameET.setText(nameThird);
                nameET.setSelection(nameThird.length());
                String numberThird = SharedPreferencesManager.getString("contacts_msg_third", "number", "13688888888");
                numberET.setText(numberThird);
                break;
            case 4:
                String nameFourth = SharedPreferencesManager.getString("contacts_msg_fourth", "name", "医生");
                nameET.setText(nameFourth);
                nameET.setSelection(nameFourth.length());
                String numberFourth = SharedPreferencesManager.getString("contacts_msg_fourth", "number", "13688888888");
                numberET.setText(numberFourth);
                break;
        }
//		tokenET.setSelection(token.length());
        commitBtn = (Button) contentView.findViewById(R.id.contacts_call_commit_btn);
    }
    private void initEvent() {
        commitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = nameET.getText().toString().trim();
                number = numberET.getText().toString().trim();
                if(!TextUtils.isEmpty(name) &&!TextUtils.isEmpty(number) ) {
                    switch (mCode) {
                        case 1:
                            SharedPreferencesManager.putString("contacts_msg_first","name",name);
                            SharedPreferencesManager.putString("contacts_msg_first","number",number);
                            break;
                        case 2:
                            SharedPreferencesManager.putString("contacts_msg_second","name",name);
                            SharedPreferencesManager.putString("contacts_msg_second","number",number);
                            break;
                        case 3:
                            SharedPreferencesManager.putString("contacts_msg_third","name",name);
                            SharedPreferencesManager.putString("contacts_msg_third","number",number);
                            break;
                        case 4:
                            SharedPreferencesManager.putString("contacts_msg_fourth","name",name);
                            SharedPreferencesManager.putString("contacts_msg_fourth","number",number);
                            break;
                    }
                    mCallback.onCommit();
                    mPopWindow.dismiss();
                }


            }
        });

    }
    public void show(View rootview) {
        //显示PopupWindow
        mPopWindow.showAtLocation(rootview,  Gravity.BOTTOM,0,150);
        //设置动画所对应的style
        mPopWindow.setAnimationStyle(R.style.contactMsgAnim);
    }
    public void setMsgCallBack(MsgCallBack callback) {
        this.mCallback = callback;
    }
    public interface MsgCallBack{
        void onCommit();
    }

}
