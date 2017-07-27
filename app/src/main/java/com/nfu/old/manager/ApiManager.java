package com.nfu.old.manager;

import com.nfu.old.config.ConnectUrl;
import com.nfu.old.utils.LogUtil;
import com.nfu.old.utils.NetUtil;
import com.zhy.http.okhttp.callback.Callback;

/**
 * Created by wpp.
 *
 * @description
 * @date 2017/7/20
 */
public class ApiManager {
    private static volatile ApiManager mInstance;
    private ApiManager(){
    }

    public static ApiManager getInstance(){
        if(mInstance == null){
            synchronized (ApiManager.class){
                if(mInstance == null){
                    mInstance = new ApiManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * 获取新闻列表
     * @param dictionID 栏目ID {@link ApiConfig#dictionIDForInformation}{@link ApiConfig#dictionIdForMedia}
     *                  {@link ApiConfig#dictionIdForNotice}{@link ApiConfig#dictionIdForPolicy}
     *                  {@link ApiConfig#dictionIdForDynamics}
     * @param pageSize 每页记录条数
     * @param currentPage 当前第几页
     * @param iRecordCount 记录总数，第一页传0，第二次调用传上一次服务器返回的数据
     * @param sortBy 排序，可选{@link ApiConfig#sortByASC} {@link ApiConfig#sortByDESC}
     */
    public void getNewsList(String dictionID, int pageSize, int currentPage, int iRecordCount, String sortBy,Callback callback){
        String url = ConnectUrl.getNewsList + "&dictionid=" + dictionID + "&iPageSize=" + pageSize + "&iCurrentPage=" + currentPage
                + "&iRecordCount=" + iRecordCount + "&strOrderBy=createdate&strSortBy=" + sortBy;
        LogUtil.i("ApiManager--->getNewsList--->url::"+url);
        NetUtil.doGet(url,null,callback);
    }

    public void getNewsListByKey(String dictionID,String keyword,int pageSize, int currentPage, int iRecordCount,String strOrderBy,String sortBy,Callback callback){
        String url = ConnectUrl.getNewsListByKey + "&dictionid=" + dictionID + "&keyword=" + keyword + "&iPageSize=" + pageSize + "&iCurrentPage=" + currentPage
                + "&iRecordCount=" + iRecordCount + "&strOrderBy=" + strOrderBy + "&strSortBy=" + sortBy;
        LogUtil.i("ApiManager--->getNewsListByKey--->url::"+url);
        NetUtil.doGet(url,null,callback);
    }

    /**
     * 获取新闻内容
     * @param newsId 新闻id
     */
    public void getNewsDetail(String newsId,Callback callback){
        String url = ConnectUrl.getNewsDetail + "&newsid=" + newsId;
        LogUtil.i("ApiManager--->getNewsDetail--->url::"+url);
        NetUtil.doGet(url,null,callback);
    }

    /**
     * 获取轮播图片
     * @param dictionID 栏目id {@link ApiConfig#dictionIDForInformation}{@link ApiConfig#dictionIdForMedia}
     *                  {@link ApiConfig#dictionIdForNotice}{@link ApiConfig#dictionIdForPolicy}
     *                  {@link ApiConfig#dictionIdForDynamics}
     */
    public void getTurnPic(String dictionID, Callback callback){
        String url = ConnectUrl.getTurnPic + "&dictionid=" + dictionID;
        LogUtil.i("ApiManager--->getTurnPic--->url::"+url);
        NetUtil.doGet(url,null,callback);
    }

    /**
     * 接收意见反馈
     * @param content 反馈内容
     * @param name 反馈者姓名
     * @param mobile 反馈者电话
     */
    public void getOpinionFeedBack(String content, String name, String mobile){
        String url = ConnectUrl.getOpinionFeedback + "&feedbackContent=" + content + "&contacterName=" + name + "&contacterMobile=" + mobile;
    }

    /**
     * 业务办理
     * @param businessId 业务ID {@link ApiConfig#businessForHeightAge}{@link ApiConfig#businessForHeightAgeLost}
     *                  {@link ApiConfig#businessForMedicalAid}{@link ApiConfig#businessForYouDaiKa}
     *                  {@link ApiConfig#businessForYouDaiZheng}{@link ApiConfig#businessForZhuCanKa}
     */
    public void getBusinessConditions(String businessId,Callback callback){
        String url = ConnectUrl.getBusinessConditions + "&businessid=" + businessId;
        LogUtil.i("ApiManager--->getBusinessConditions--->url::"+url);
        NetUtil.doGet(url,null,callback);
    }

    /**
     * 服务查询
     */
    public void getXbsFws(String serviceTypeId, int pageSize, int currentPage, int iRecordCount, String shopName,Callback callback){
        String url = ConnectUrl.getXbsFws + "&serviceTypeId=" + serviceTypeId + "&iPageSize=" + pageSize + "&iCurrentPage=" + currentPage + "&iRecordCount=" + iRecordCount + "&shopName=" + shopName;
        LogUtil.i("ApiManager--->getNewsDetail--->url::"+url);
        NetUtil.doGet(url,null,callback);
    }

    public void getXbsFwsDetail(){

    }

}
