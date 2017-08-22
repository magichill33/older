package com.nfu.old.activity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;


import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.nfu.old.R;
import com.nfu.old.fragment.ConsultFragment;
import com.nfu.old.fragment.HomeFragment;
import com.nfu.old.fragment.SafeguardFragment;
import com.nfu.old.fragment.ServiceFragment;
import com.nfu.old.map.MyLocationListener;
import com.nfu.old.utils.BitmapAndStringUtils;
import com.nfu.old.utils.ImageUtils;
import com.nfu.old.utils.LogUtil;
import com.nfu.old.utils.PhotoUtils;
import com.nfu.old.utils.ToastUtil;
import com.nfu.old.view.ButtonExtendM;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.nfu.old.utils.DeviceUtil.hasSdcard;

public class MainActivity extends AppCompatActivity {

    Unbinder unbinder;

    @BindView(R.id.btn_home)
    ButtonExtendM btnHome;
    @BindView(R.id.btn_consult)
    ButtonExtendM btnConsult;
    @BindView(R.id.btn_service)
    ButtonExtendM btnService;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    @BindView(R.id.activity_main_content_frameLayout)
    FrameLayout mContentView;

    private static final int ACCESS_COARSE_LOCATION_REQUEST_CODE = 0x1001;
    private final int PERMS_REQUEST_CODE = 0x1002;
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);
        if(savedInstanceState ==null){
            setHomeFragment();
        }
        initView();
        initData();
    }

    private void initData() {
        initMap();
    }
    private void initMap() {
        // //判断当前Activity是否获得了该权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED ||ContextCompat.checkSelfPermission(this,Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            //没有授权,判断权限申请是否曾经被拒绝过
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)
                    ||ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    ||ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    ||ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE) ) {
                Toast.makeText(this, "你曾经拒绝过此权限,需要重新获取", Toast.LENGTH_SHORT).show();
                //申请WRITE_EXTERNAL_STORAGE权限
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_PHONE_STATE,
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.CALL_PHONE
                        },
                        ACCESS_COARSE_LOCATION_REQUEST_CODE);
            } else {
                //申请WRITE_EXTERNAL_STORAGE权限
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_PHONE_STATE,
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.CALL_PHONE
                        },
                        ACCESS_COARSE_LOCATION_REQUEST_CODE);
            }
        }else {
            getLocate();

        }
    }
    public void getLocate() {
        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case ACCESS_COARSE_LOCATION_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    getLocate();
                } else {
                    // Permission Denied
                    Toast.makeText(this, "访问被拒绝！", Toast.LENGTH_SHORT).show();
                }
                break;
            case CAMERA_PERMISSIONS_REQUEST_CODE: {//调用系统相机申请拍照权限回调
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (hasSdcard()) {
                        File fileUri  =  getFileUrl();
                        imageUri = Uri.fromFile(fileUri);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                            imageUri = FileProvider.getUriForFile(this, "com.nfu.old.fileprovider", fileUri);//通过FileProvider创建一个content类型的Uri
                        PhotoUtils.takePicture(this, imageUri, CODE_CAMERA_REQUEST);
                    } else {
                        ToastUtil.showShortToast(this, "设备没有SD卡！");
                    }
                } else {

                    ToastUtil.showShortToast(this, "请允许打开相机！！");
                }
                break;


            }
            case STORAGE_PERMISSIONS_REQUEST_CODE://调用系统相册申请Sdcard权限回调
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    PhotoUtils.openPic(this, CODE_GALLERY_REQUEST);
                } else {

                    ToastUtil.showShortToast(this, "请允许打操作SDCard！！");
                }
                break;
        }
    }
    private void setHomeFragment() {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        HomeFragment fragment = new HomeFragment();
        fragmentTransaction.replace(R.id.activity_main_content_frameLayout, fragment);
        fragmentTransaction.commit();

    }

    boolean isHomeClick = false;
    boolean isServiceClick = false;
    boolean isConsultClick = false;

    private void initView() {
        btnHome.setNfuSeleted(true);

        btnHome.setOnClickListener(new ButtonExtendM.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearSelected();
                btnHome.setNfuSeleted(true);
                Fragment fragment = getVisibleFragment();
                if (fragment==null||!(fragment instanceof HomeFragment)){
                    setHomeFragment();
                }


            }
        });


        btnConsult.setOnClickListener(new ButtonExtendM.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearSelected();
                btnConsult.setNfuSeleted(true);
                Fragment fragment = getVisibleFragment();
                if (fragment==null||!(fragment instanceof ConsultFragment)){
                    setConsultFragment();
                }
            }
        });

        btnService.setOnClickListener(new ButtonExtendM.OnClickListener()

        {
            @Override
            public void onClick(View v) {

                clearSelected();
                btnService.setNfuSeleted(true);
                Fragment fragment = getVisibleFragment();
                if (fragment==null||!(fragment instanceof ServiceFragment)){
                    setServiceFragment();
                }
            }
        });
    }

    private Fragment getVisibleFragment(){
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment f:fragments){
            if (f!=null && f.isVisible()){
               return f;
            }
        }
        return null;
    }

    private void setServiceFragment() {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        ServiceFragment fragment = new ServiceFragment();
        fragmentTransaction.replace(R.id.activity_main_content_frameLayout, fragment);
        fragmentTransaction.commit();
    }


    private void setConsultFragment() {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        ConsultFragment fragment = new ConsultFragment();
        fragmentTransaction.replace(R.id.activity_main_content_frameLayout, fragment);
        fragmentTransaction.commit();
    }


    private void clearSelected() {
        btnHome.setNfuSeleted(false);
        btnConsult.setNfuSeleted(false);
        btnService.setNfuSeleted(false);
//        isClick = false;
    }


    /**
     * 选择图片的返回码
     */
    public final static int SELECT_IMAGE_RESULT_CODE1 = 200;
    public final static int SELECT_IMAGE_RESULT_CODE2 = 300;
    public final static int SELECT_IMAGE_RESULT_CODE3 = 400;
    private int CODE_CAMERA_REQUEST = 0;
    private int CODE_GALLERY_REQUEST = 0;
    private static final int CAMERA_PERMISSIONS_REQUEST_CODE = 0x03;
    private static final int STORAGE_PERMISSIONS_REQUEST_CODE = 0x04;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtil.i("MainActivity--->onActivityResult--->requestCode:"+requestCode+",resultCode:"+resultCode);
        String imagePath = "";
        if((requestCode == SELECT_IMAGE_RESULT_CODE1||requestCode == SELECT_IMAGE_RESULT_CODE2||
                requestCode == SELECT_IMAGE_RESULT_CODE3) && resultCode== RESULT_OK){
            /*if(data != null && data.getData() != null){//有数据返回直接使用返回的图片地址
                Uri uri = data.getData();
                if(uri.getScheme().equals("content")) {//判断uri地址是以什么开头的
                    imagePath = ImageUtils.getFilePathByFileUri(this, data.getData());
                }else{
                    imagePath = ImageUtils.getFilePathByFileUri(this, getFileUri(uri));
                }

            }else if (IMAGEPATH!=null){
                imagePath = IMAGEPATH;
            }

            if (mOnFragmentResult!=null){
                mOnFragmentResult.onResult(imagePath,requestCode);
            }*/
            if (data != null && data.getData() != null) {//有数据返回直接使用返回的图片地址
                Uri uri = data.getData();
                /*if (uri.getScheme().equals("content")) {//判断uri地址是以什么开头的
                    imagePath = ImageUtils.getFilePathByFileUri(this, data.getData());
                } else {
                    imagePath = ImageUtils.getFilePathByFileUri(this, getFileUri(uri));
                }*/
                Bitmap bitmap = PhotoUtils.getBitmapFromUri(uri,this);
                if (mOnFragmentResult != null) {
                    mOnFragmentResult.onResult(bitmap, requestCode);
                }

            } else if (IMAGEPATH != null) {
                if (mOnFragmentResult != null) {
                    Bitmap bitmap = BitmapAndStringUtils.getimage(IMAGEPATH);
                    mOnFragmentResult.onResult(bitmap, requestCode);
                }
            }
        }
    }

    /**
     * Fragment回调接口
     */
    public OnFragmentResult mOnFragmentResult;

    public void setOnFragmentResult(OnFragmentResult onFragmentResult){
        mOnFragmentResult = onFragmentResult;
    }

    /**
     * 回调数据给Fragment的接口
     */
    public interface OnFragmentResult{
        void onResult(Bitmap bitmap,int requestCode);
    }

    public Uri getFileUri(Uri uri){
        if (uri.getScheme().equals("file")) {
            String path = uri.getEncodedPath();
            LogUtil.i("path1 is " + path);
            if (path != null) {
                path = Uri.decode(path);
                LogUtil.i("path2 is " + path);
                ContentResolver cr = this.getContentResolver();
                StringBuffer buff = new StringBuffer();
                buff.append("(")
                        .append(MediaStore.Images.ImageColumns.DATA)
                        .append("=")
                        .append("'" + path + "'")
                        .append(")");
                Cursor cur = cr.query(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        new String[] { MediaStore.Images.ImageColumns._ID },
                        buff.toString(), null, null);
                int index = 0;
                for (cur.moveToFirst(); !cur.isAfterLast(); cur
                        .moveToNext()) {
                    index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                    // set _id value
                    index = cur.getInt(index);
                }
                if (index == 0) {
                    //do nothing
                } else {
                    Uri uri_temp = Uri
                            .parse("content://media/external/images/media/"
                                    + index);
                    LogUtil.i("uri_temp is " + uri_temp);
                    if (uri_temp != null) {
                        uri = uri_temp;
                    }
                }
            }
        }
        return uri;
    }

    @Override
    protected void onDestroy() {
//        mLocationClient.unRegisterLocationListener(myListener);//取消注册的位置监听，以免内存泄露
//        mLocationClient.stop();// 退出时销毁定位
        super.onDestroy();
        unbinder.unbind();
    }


    private Uri imageUri;
    private Uri cropImageUri;
    private String IMAGEPATH;
    /**
     * 自动获取相机权限
     */
    public void autoObtainCameraPermission(int requestCode) {
        CODE_CAMERA_REQUEST = requestCode;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                ToastUtil.showShortToast(this, "您已经拒绝过一次");
            }
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, CAMERA_PERMISSIONS_REQUEST_CODE);
        } else {//有权限直接调用系统相机拍照
            if (hasSdcard()) {
                File fileUri  =  getFileUrl();
                IMAGEPATH = fileUri.getAbsolutePath();
                imageUri = Uri.fromFile(fileUri);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                    imageUri = FileProvider.getUriForFile(this, "com.nfu.old.fileprovider", fileUri);//通过FileProvider创建一个content类型的Uri
                PhotoUtils.takePicture(this, imageUri, CODE_CAMERA_REQUEST);
            } else {
                ToastUtil.showShortToast(this, "设备没有SD卡！");
            }
        }
    }

    private File getFileUrl(){
        //获取与应用相关联的路径
        String imageFilePath = getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
        //根据当前时间生成图片的名称
        String timestamp = "/" + formatter.format(new Date()) + ".jpg";
        File imageFile = new File(imageFilePath, timestamp);// 通过路径创建保存文件
        return imageFile;
    }

    /**
     * 自动获取sdk权限
     */

    public void autoObtainStoragePermission(int requestCode) {
        CODE_GALLERY_REQUEST = requestCode;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSIONS_REQUEST_CODE);
        } else {
            PhotoUtils.openPic(this, CODE_GALLERY_REQUEST);
        }

    }
}
