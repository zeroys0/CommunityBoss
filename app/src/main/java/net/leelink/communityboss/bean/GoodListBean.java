package net.leelink.communityboss.bean;

public class GoodListBean {


    private int CommodityId;
    private int StoreId;
    private String Name;
    private float Price;
    private String Details;
    private String AddTime;
    private String HeadImage;
    private int MonthlySales;

    public int getCommodityId() {
        return CommodityId;
    }

    public void setCommodityId(int CommodityId) {
        this.CommodityId = CommodityId;
    }

    public int getStoreId() {
        return StoreId;
    }

    public void setStoreId(int StoreId) {
        this.StoreId = StoreId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public float getPrice() {
        return Price;
    }

    public void setPrice(float Price) {
        this.Price = Price;
    }

    public String getDetails() {
        return Details;
    }

    public void setDetails(String Details) {
        this.Details = Details;
    }

    public String getAddTime() {
        return AddTime;
    }

    public void setAddTime(String AddTime) {
        this.AddTime = AddTime;
    }

    public String getHeadImage() {
        return HeadImage;
    }

    public void setHeadImage(String HeadImage) {
        this.HeadImage = HeadImage;
    }

    public int getMonthlySales() {
        return MonthlySales;
    }

    public void setMonthlySales(int MonthlySales) {
        this.MonthlySales = MonthlySales;
    }
}
