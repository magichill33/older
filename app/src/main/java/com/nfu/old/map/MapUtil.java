package com.nfu.old.map;

import com.google.gson.Gson;
import com.nfu.old.Constant;
import com.nfu.old.model.MapInfo;
import com.nfu.old.utils.LogUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Administrator on 2017/8/1.
 */

public class MapUtil {

    public static String getCoordinate(String lng,String lat) throws IOException {
        StringBuilder resultData = new StringBuilder();
        //秘钥换成你的秘钥，申请地址在下边
        String url ="http://api.map.baidu.com/geocoder/v2/?ak="+ Constant.BAIDU_AK+"&location=" + lat + ","+ lng + "&output=json&pois=1";
        URL myURL = null;
        URLConnection httpsConn = null;
        try {
            myURL = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        InputStreamReader insr = null;
        BufferedReader br = null;
        try {
            httpsConn = (URLConnection) myURL.openConnection();// 不使用代理
            if (httpsConn != null) {
                insr = new InputStreamReader( httpsConn.getInputStream(), "UTF-8");
                br = new BufferedReader(insr);
                String data = null;
                while((data= br.readLine())!=null){
                    resultData.append(data);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(insr!=null){
                insr.close();
            }
            if(br!=null){
                br.close();
            }
        }
        MapInfo mapInfo =  new Gson().fromJson(resultData.toString(), MapInfo.class);
        LogUtil.d("MapUtil --->mapInfo ::" +resultData.toString());
//        String province= JSONObject.fromObject(resultData.toString()).getJSONObject("result")
//                .getJSONObject("addressComponent").getString("province");
        return mapInfo.getResult().getAddressComponent().getCity();
    }

}