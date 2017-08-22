package com.nfu.old;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.baidu.mapapi.SDKInitializer;
import com.nfu.old.utils.LogUtil;
import com.nfu.old.utils.SharedPreferencesManager;
import com.tencent.bugly.crashreport.CrashReport;
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
        CrashReport.initCrashReport(getApplicationContext(), "64618000fa", true);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(new LoggerInterceptor("TAG"))
                .connectTimeout(20000L, TimeUnit.MILLISECONDS)
                .readTimeout(20000L, TimeUnit.MILLISECONDS)
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

        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());


    }
}
