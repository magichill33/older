package com.nfu.old.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by user on 2017/7/27.
 */

public class AppUtils {

    /**
     * 调用拨号界面
     *
     * @param phone 电话号码
     */
    public  static void call(Context context, String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
