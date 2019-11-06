package net.leelink.communityboss.bean;

public class OrderBean {

    /**
     * OrderId : 31
     * UserId : 100000001
     * Store : {"StoreId":2,"StoreName":"尬聊","HeadImage":"b33e0494dd9141db8364e83d2f560189.png"}
     * State : 2
     * OrderTime : 2019-11-06T11:53:03.137
     * TotalPrice : 130
     * DeliveryTime : 2019-05-30T12:10:00
     * DeliveryAddress : string22223928252
     * DeliveryName : string
     * DeliveryPhone : 15122478510
     * Remarks : balance
     * PaymentMethod : null
     */

    private String OrderId;
    private String UserId;
    private StoreBean Store;
    private int State;
    private String OrderTime;
    private float TotalPrice;
    private String DeliveryTime;
    private String DeliveryAddress;
    private String DeliveryName;
    private String DeliveryPhone;
    private String Remarks;
    private Object PaymentMethod;

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String OrderId) {
        this.OrderId = OrderId;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String UserId) {
        this.UserId = UserId;
    }

    public StoreBean getStore() {
        return Store;
    }

    public void setStore(StoreBean Store) {
        this.Store = Store;
    }

    public int getState() {
        return State;
    }

    public void setState(int State) {
        this.State = State;
    }

    public String getOrderTime() {
        return OrderTime;
    }

    public void setOrderTime(String OrderTime) {
        this.OrderTime = OrderTime;
    }

    public float getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(float TotalPrice) {
        this.TotalPrice = TotalPrice;
    }

    public String getDeliveryTime() {
        return DeliveryTime;
    }

    public void setDeliveryTime(String DeliveryTime) {
        this.DeliveryTime = DeliveryTime;
    }

    public String getDeliveryAddress() {
        return DeliveryAddress;
    }

    public void setDeliveryAddress(String DeliveryAddress) {
        this.DeliveryAddress = DeliveryAddress;
    }

    public String getDeliveryName() {
        return DeliveryName;
    }

    public void setDeliveryName(String DeliveryName) {
        this.DeliveryName = DeliveryName;
    }

    public String getDeliveryPhone() {
        return DeliveryPhone;
    }

    public void setDeliveryPhone(String DeliveryPhone) {
        this.DeliveryPhone = DeliveryPhone;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String Remarks) {
        this.Remarks = Remarks;
    }

    public Object getPaymentMethod() {
        return PaymentMethod;
    }

    public void setPaymentMethod(Object PaymentMethod) {
        this.PaymentMethod = PaymentMethod;
    }

    public static class StoreBean {
        /**
         * StoreId : 2
         * StoreName : 尬聊
         * HeadImage : b33e0494dd9141db8364e83d2f560189.png
         */

        private int StoreId;
        private String StoreName;
        private String HeadImage;

        public int getStoreId() {
            return StoreId;
        }

        public void setStoreId(int StoreId) {
            this.StoreId = StoreId;
        }

        public String getStoreName() {
            return StoreName;
        }

        public void setStoreName(String StoreName) {
            this.StoreName = StoreName;
        }

        public String getHeadImage() {
            return HeadImage;
        }

        public void setHeadImage(String HeadImage) {
            this.HeadImage = HeadImage;
        }
    }
}
