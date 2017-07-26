/*

package com.nfu.old.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nfu.old.R;

import static android.content.DialogInterface.BUTTON_POSITIVE;

*//*

*/
/**
 * Created by Administrator on 2017/7/26.
 *//*
*/
/*


public class ContactCallDailog extends Dialog{


    *//*

*/
/**
     * 自定义对话框    AlertDialog.Builder 模式
     *
     * @author Administrator
     * @version 1.0
     * @date 2016-10-18 下午4:15:04
     *//*
*/
/*


    public ContactCallDailog(Context context) {
        super(context);
    }

    public ContactCallDailog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
     *//*

*/
/*   DisplayMetrics dm = new DisplayMetrics();
        window.getWindowManager().getDefaultDisplay().getMetrics(dm);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        // layoutParams.width = (int) (dm.widthPixels / 2);
        // layoutParams.height = (int) (dm.heightPixels / 2.2);

        layoutParams.width = (int) (DisplayManagers.getInstance().getScreenWidth() * 0.55);
        layoutParams.height = (int) (DisplayManagers.getInstance().getScreenHeight() * 0.55);
        window.setAttributes(layoutParams);*//*


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;}import com.nfu.old.R;

@SuppressLint("InflateParams")
    public static class Builder {
        private Context mContext;
        private String mTitle;
        private String mPositiveButtonText;
        private String mNegativeButtonText;
        private boolean flag = true;
        private boolean isHost = false;

        private DialogInterface.OnClickListener mPositiveButtonClickListener, mNegativeButtonClickListener;
        private RadioGroup selectRoleRg;

        public Builder(Context context) {
            mContext = context;
        }

        public Builder setTitle(String content) {
            mTitle = content;
            return this;
        }

        public Builder setTitle(int resId) {
            mTitle = (String) mContext.getText(resId);
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText, DialogInterface.OnClickListener listener) {
            mPositiveButtonText = positiveButtonText;
            mPositiveButtonClickListener = listener;
            return this;
        }

        public Builder setCancelable(boolean flag) {
            this.flag = flag;
            return this;
        }

        public boolean getIsHost() {
            return isHost;
        }

        public Builder setPositiveButton(int positiveButtonText, DialogInterface.OnClickListener listener) {
            mPositiveButtonText = (String) mContext.getText(positiveButtonText);
            mPositiveButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText, DialogInterface.OnClickListener listener) {
            mNegativeButtonText = negativeButtonText;
            mNegativeButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(int negativeButtonText, DialogInterface.OnClickListener listener) {
            mNegativeButtonText = (String) mContext.getText(negativeButtonText);
            mNegativeButtonClickListener = listener;
            return this;
        }

        public ContactCallDailog create() {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view = inflater.inflate(R.layout.dialog_login, null);
            final ContactCallDailog customAlertDialog = new ContactCallDailog(mContext, R.style.alertDialog);
            TextView tvAlertTitle = (TextView) view.findViewById(R.id.title);
            tvAlertTitle.setText(mTitle);
            selectRoleRg = (RadioGroup) view.findViewById(R.id.select_role_rg);
*/
/*if(selectRoleRg.getCheckedRadioButtonId() == R.id.setting_fenbianlv_rb1){

			}else if (selectRoleRg.getCheckedRadioButtonId() == R.id.setting_fenbianlv_rb2) {

			}*//*
*/
/*


            Button btnPositive = (Button) view.findViewById(R.id.positiveButton);
            if (!TextUtils.isEmpty(mPositiveButtonText)) {
                btnPositive.setText(mPositiveButtonText);
                btnPositive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        if (selectRoleRg.getCheckedRadioButtonId() == R.id.host_rb) {
                            isHost = true;
                        } else if (selectRoleRg.getCheckedRadioButtonId() == R.id.audience_rb) {
                            isHost = false;
                        }
                        mPositiveButtonClickListener.onClick(customAlertDialog, BUTTON_POSITIVE);
                    }
                });
            } else {
                btnPositive.setVisibility(View.GONE);
            }
            Button btnNegative = (Button) view.findViewById(R.id.negativeButton);
            if (!TextUtils.isEmpty(mNegativeButtonText)) {
                btnNegative.setText(mNegativeButtonText);
                if (mNegativeButtonClickListener != null) {
                    btnNegative.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mNegativeButtonClickListener.onClick(customAlertDialog, BUTTON_NEGATIVE);
                        }
                    });
                } else {
                    btnNegative.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            customAlertDialog.dismiss();
                        }
                    });
                }
            } else {
                btnNegative.setVisibility(View.GONE);
            }
            if (View.VISIBLE == btnPositive.getVisibility() && View.GONE == btnNegative.getVisibility()) {
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                btnPositive.setLayoutParams(layoutParams);
            }
            customAlertDialog.setContentView(view);
            customAlertDialog.setCancelable(flag);
            return customAlertDialog;
        }

    }


}

*/
