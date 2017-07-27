package com.nfu.old.model;

import java.util.List;

/**
 * Created by user on 2017/7/27.
 */

public class ServiceModels {

    /**
     * recordCount : 2421
     * pageCount : 485
     * data : [{"shopPic":"","shopStreetId":"859","shopcommunityId":"","shopCountryName":"海淀区","shopTelephone":"18610015781","infoSource":"街道签约","shopCountryId":"8","shopType":"百货购物（超市/商场）","shopNumber":"418110954110119","shopId":"175","shopName":"北京双利恒通商贸有限公司","shopCommunityName":"","servicePhone":"82909758","shopOther":"","businessAddress":"西三旗育新花园小区6号楼","shopManager":"朱青霞","shopStreetName":"西三旗街道"},{"shopPic":"","shopStreetId":"856","shopcommunityId":"","shopCountryName":"海淀区","shopTelephone":"62233692","infoSource":"街道签约","shopCountryId":"8","shopType":"百货购物（超市/商场）","shopNumber":"418110953119004","shopId":"54","shopName":"北京华西嘉誉商贸中心.","shopCommunityName":"","servicePhone":"62233692","shopOther":"","businessAddress":"海淀区红联北村1号楼1门103","shopManager":"尚华西","shopStreetName":"北下关街道"},{"shopPic":"","shopStreetId":"878","shopcommunityId":"","shopCountryName":"海淀区","shopTelephone":"13810750466","infoSource":"协会发展","shopCountryId":"8","shopType":"百货购物（超市/商场）","shopNumber":null,"shopId":"6839","shopName":"北京物美便利超市有限公司华为新店","shopCommunityName":"","servicePhone":"56143484","shopOther":"","businessAddress":"海淀区中关村环保科技园Q13号","shopManager":"张桂芝","shopStreetName":"中关村街道"},{"shopPic":"","shopStreetId":"923","shopcommunityId":"","shopCountryName":"东城区","shopTelephone":"84139818","infoSource":"协会发展","shopCountryId":"461","shopType":"百货购物（超市/商场）","shopNumber":"418110954110611","shopId":"301","shopName":"北京物美安外店","shopCommunityName":"","servicePhone":"84139818","shopOther":"","businessAddress":"东城青年湖北街4号华府景园商业（石化东侧）","shopManager":"王店长            ","shopStreetName":"安定门"},{"shopPic":"","shopStreetId":"872","shopcommunityId":"","shopCountryName":"海淀区","shopTelephone":"13511026441","infoSource":"协会发展","shopCountryId":"8","shopType":"百货购物（超市/商场）","shopNumber":"418110954110095","shopId":"302","shopName":"北京物美大卖场商业管理有限公司物美华天店","shopCommunityName":"","servicePhone":"58891385","shopOther":"","businessAddress":"海淀区北小马厂6号华天大厦","shopManager":"钱崇然","shopStreetName":"羊坊店街道"}]
     * pageSize : 5
     * currentPage : 1
     */

    private int recordCount;
    private int pageCount;
    private int pageSize;
    private int currentPage;
    private List<DataBean> data;

    public int getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(int recordCount) {
        this.recordCount = recordCount;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * shopPic :
         * shopStreetId : 859
         * shopcommunityId :
         * shopCountryName : 海淀区
         * shopTelephone : 18610015781
         * infoSource : 街道签约
         * shopCountryId : 8
         * shopType : 百货购物（超市/商场）
         * shopNumber : 418110954110119
         * shopId : 175
         * shopName : 北京双利恒通商贸有限公司
         * shopCommunityName :
         * servicePhone : 82909758
         * shopOther :
         * businessAddress : 西三旗育新花园小区6号楼
         * shopManager : 朱青霞
         * shopStreetName : 西三旗街道
         */

        private String shopPic;
        private String shopStreetId;
        private String shopcommunityId;
        private String shopCountryName;
        private String shopTelephone;
        private String infoSource;
        private String shopCountryId;
        private String shopType;
        private String shopNumber;
        private String shopId;
        private String shopName;
        private String shopCommunityName;
        private String servicePhone;
        private String shopOther;
        private String businessAddress;
        private String shopManager;
        private String shopStreetName;

        public String getShopPic() {
            return shopPic;
        }

        public void setShopPic(String shopPic) {
            this.shopPic = shopPic;
        }

        public String getShopStreetId() {
            return shopStreetId;
        }

        public void setShopStreetId(String shopStreetId) {
            this.shopStreetId = shopStreetId;
        }

        public String getShopcommunityId() {
            return shopcommunityId;
        }

        public void setShopcommunityId(String shopcommunityId) {
            this.shopcommunityId = shopcommunityId;
        }

        public String getShopCountryName() {
            return shopCountryName;
        }

        public void setShopCountryName(String shopCountryName) {
            this.shopCountryName = shopCountryName;
        }

        public String getShopTelephone() {
            return shopTelephone;
        }

        public void setShopTelephone(String shopTelephone) {
            this.shopTelephone = shopTelephone;
        }

        public String getInfoSource() {
            return infoSource;
        }

        public void setInfoSource(String infoSource) {
            this.infoSource = infoSource;
        }

        public String getShopCountryId() {
            return shopCountryId;
        }

        public void setShopCountryId(String shopCountryId) {
            this.shopCountryId = shopCountryId;
        }

        public String getShopType() {
            return shopType;
        }

        public void setShopType(String shopType) {
            this.shopType = shopType;
        }

        public String getShopNumber() {
            return shopNumber;
        }

        public void setShopNumber(String shopNumber) {
            this.shopNumber = shopNumber;
        }

        public String getShopId() {
            return shopId;
        }

        public void setShopId(String shopId) {
            this.shopId = shopId;
        }

        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        public String getShopCommunityName() {
            return shopCommunityName;
        }

        public void setShopCommunityName(String shopCommunityName) {
            this.shopCommunityName = shopCommunityName;
        }

        public String getServicePhone() {
            return servicePhone;
        }

        public void setServicePhone(String servicePhone) {
            this.servicePhone = servicePhone;
        }

        public String getShopOther() {
            return shopOther;
        }

        public void setShopOther(String shopOther) {
            this.shopOther = shopOther;
        }

        public String getBusinessAddress() {
            return businessAddress;
        }

        public void setBusinessAddress(String businessAddress) {
            this.businessAddress = businessAddress;
        }

        public String getShopManager() {
            return shopManager;
        }

        public void setShopManager(String shopManager) {
            this.shopManager = shopManager;
        }

        public String getShopStreetName() {
            return shopStreetName;
        }

        public void setShopStreetName(String shopStreetName) {
            this.shopStreetName = shopStreetName;
        }
    }
}
