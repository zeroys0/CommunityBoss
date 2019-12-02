package net.leelink.communityboss.bean;

import java.util.List;

public class CommentListBean {

    /**
     * StoreScore : 4.6
     * StoreTaste : 4.5
     * StorePack : 4.5
     * StoreDelivery : 4.5
     * StoreQuality : 5
     * StoreAttitude : 4.5
     * UserAppraiseList : [{"OrderId":86,"Username":"18222728241","UserHeadImage":"4b53fef998dc483cb0d4fa30ae1d1474.png","UserScore":5,"UserMessage":"好厚"},{"OrderId":74,"Username":"15122478510","UserHeadImage":"29ff25e39a024e558a490e6f66af7d19.png","UserScore":4.2,"UserMessage":"很不错"}]
     */

    private double StoreScore;
    private double StoreTaste;
    private double StorePack;
    private double StoreDelivery;
    private double StoreQuality;
    private double StoreAttitude;
    private List<UserAppraiseListBean> UserAppraiseList;

    public double getStoreScore() {
        return StoreScore;
    }

    public void setStoreScore(double StoreScore) {
        this.StoreScore = StoreScore;
    }

    public double getStoreTaste() {
        return StoreTaste;
    }

    public void setStoreTaste(double StoreTaste) {
        this.StoreTaste = StoreTaste;
    }

    public double getStorePack() {
        return StorePack;
    }

    public void setStorePack(double StorePack) {
        this.StorePack = StorePack;
    }

    public double getStoreDelivery() {
        return StoreDelivery;
    }

    public void setStoreDelivery(double StoreDelivery) {
        this.StoreDelivery = StoreDelivery;
    }

    public double getStoreQuality() {
        return StoreQuality;
    }

    public void setStoreQuality(double StoreQuality) {
        this.StoreQuality = StoreQuality;
    }

    public double getStoreAttitude() {
        return StoreAttitude;
    }

    public void setStoreAttitude(double StoreAttitude) {
        this.StoreAttitude = StoreAttitude;
    }

    public List<UserAppraiseListBean> getUserAppraiseList() {
        return UserAppraiseList;
    }

    public void setUserAppraiseList(List<UserAppraiseListBean> UserAppraiseList) {
        this.UserAppraiseList = UserAppraiseList;
    }

    public static class UserAppraiseListBean {


        private String OrderId;
        private String Username;
        private String UserHeadImage;
        private double UserScore;
        private String UserMessage;

        public String getOrderId() {
            return OrderId;
        }

        public void setOrderId(String OrderId) {
            this.OrderId = OrderId;
        }

        public String getUsername() {
            return Username;
        }

        public void setUsername(String Username) {
            this.Username = Username;
        }

        public String getUserHeadImage() {
            return UserHeadImage;
        }

        public void setUserHeadImage(String UserHeadImage) {
            this.UserHeadImage = UserHeadImage;
        }

        public double getUserScore() {
            return UserScore;
        }

        public void setUserScore(double UserScore) {
            this.UserScore = UserScore;
        }

        public String getUserMessage() {
            return UserMessage;
        }

        public void setUserMessage(String UserMessage) {
            this.UserMessage = UserMessage;
        }
    }
}
