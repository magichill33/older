package com.nfu.old.config;

/**
 * Created by wpp.
 *
 * @description
 * @date 2017/7/20
 */
public interface ConnectUrl {
    String baseUrl = "http://17103938iy.iask.in";

    String getNewsList = baseUrl + "/bjllapp/bjllXxfbQuery/bjllXxfbQueryAction.do?method=GetNewsList&signKey=" + ApiConfig.signKey;
    String getNewsDetail = baseUrl + "/bjllapp/bjllXxfbQuery/bjllXxfbQueryAction.do?method=GetNewsDetail&signKey=" + ApiConfig.signKey;
    String getTurnPic = baseUrl + "/bjllapp/bjllXxfbQuery/bjllXxfbQueryAction.do?method=GetTurnPicture&signKey=" + ApiConfig.signKey;
    String getOpinionFeedback = baseUrl + "/bjllapp/opinionBackManage/opinionBackManageAction.do?method=GetOpinionFeedback&signKey=" + ApiConfig.signKey;
    String getBusinessConditions = baseUrl + "/bjllapp/bjllFwManage/bjllFwManageAction.do?method=GetBusinessConditions&signKey=" + ApiConfig.signKey;
    String getXbsFws = baseUrl + "/bjllapp/bjllFwManage/bjllFwManageAction.do?method=GetXbsFws&signKey=" + ApiConfig.signKey;
    String getXbsFwsDetail = baseUrl + "/bjllapp/bjllFwManage/bjllFwManageAction.do?method=GetXbsFwsDetail&signKey=" + ApiConfig.signKey;
    String getNewsListByKey = baseUrl + "/bjllapp/bjllXxfbQuery/bjllXxfbQueryAction.do?method=GetKeywordNewsList&signKey=" + ApiConfig.signKey;

    String query_card = "https://fhzx.bjrcb.com/appoint/cardSchedulQuery.jhtml";
}
