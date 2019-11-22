package net.leelink.communityboss.bean;

import java.util.List;

public class OrderDetail {



    private String OrderId;
    private String UserId;
    private StoreBean Store;
    private int State;
    private String OrderTime;
    private double TotalPrice;
    private String DeliveryTime;
    private String DeliveryAddress;
    private String DeliveryName;
    private String DeliveryPhone;
    private String Remarks;
    private Object PaymentMethod;
    private List<DetailsBean> Details;

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

    public double getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(double TotalPrice) {
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

    public List<DetailsBean> getDetails() {
        return Details;
    }

    public void setDetails(List<DetailsBean> Details) {
        this.Details = Details;
    }

    public static class StoreBean {
        /**
         * StoreId : 2
         * StoreName : 肥肥快乐
         * HeadImage : 3bc3b91e82b5463388a96bf4e096f88f.png
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

    public static class DetailsBean {
        /**
         * Id : 58
         * CommodityName : 肥肥快乐桶
         * Price : 0.01
         * Sales : 1
         * CommodityImage : 8474362d90284955842287d8926a0400.png
         */

        private String Id;
        private String CommodityName;
        private float Price;
        private int Sales;
        private String CommodityImage;

        public String getId() {
            return Id;
        }

        public void setId(String Id) {
            this.Id = Id;
        }

        public String getCommodityName() {
            return CommodityName;
        }

        public void setCommodityName(String CommodityName) {
            this.CommodityName = CommodityName;
        }

        public float getPrice() {
            return Price;
        }

        public void setPrice(float Price) {
            this.Price = Price;
        }

        public int getSales() {
            return Sales;
        }

        public void setSales(int Sales) {
            this.Sales = Sales;
        }

        public String getCommodityImage() {
            return CommodityImage;
        }

        public void setCommodityImage(String CommodityImage) {
            this.CommodityImage = CommodityImage;
        }
    }
}
