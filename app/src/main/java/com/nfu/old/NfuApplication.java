package com.nfu.old;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.nfu.old.utils.LogUtil;
import com.nfu.old.utils.SharedPreferencesManager;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

import static android.R.attr.value;

/**
 * Created by Administrator on 2017-7-25.
 */

public class NfuApplication extends Application {
    public static Context context;

    /**
     *
     */
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(new LoggerInterceptor("TAG"))
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build();

        OkHttpUtils.initClient(okHttpClient);

        SharedPreferencesManager.createInstance(this);
        try {
            ApplicationInfo appInfo = getPackageManager().getApplicationInfo(getPackageName(),
                    PackageManager.GET_META_DATA);
            Constant.BAIDU_AK = appInfo.metaData.getString("com.baidu.lbsapi.API_KEY");
            LogUtil.d(" MyLocationListener mata-data value == " +   Constant.BAIDU_AK );
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }



    }
}
