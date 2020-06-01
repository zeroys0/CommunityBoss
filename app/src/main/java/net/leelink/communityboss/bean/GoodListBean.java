package net.leelink.communityboss.bean;

public class GoodListBean {



    private String id;
    private String productNo;
    private int shStoreId;
    private String name;
    private int productTypeId;
    private Object productType;
    private String unit;
    private double unitPrice;
    private double storePrice;
    private double organPrice;
    private String updateName;
    private String updateTime;
    private int state;
    private double profit;
    private String productImgPath;
    private String remark;
    private int monthlySales;
    private int goodNum;
    private int badNum;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductNo() {
        return productNo;
    }

    public void setProductNo(String productNo) {
        this.productNo = productNo;
    }

    public int getShStoreId() {
        return shStoreId;
    }

    public void setShStoreId(int shStoreId) {
        this.shStoreId = shStoreId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(int productTypeId) {
        this.productTypeId = productTypeId;
    }

    public Object getProductType() {
        return productType;
    }

    public void setProductType(Object productType) {
        this.productType = productType;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getStorePrice() {
        return storePrice;
    }

    public void setStorePrice(double storePrice) {
        this.storePrice = storePrice;
    }

    public double getOrganPrice() {
        return organPrice;
    }

    public void setOrganPrice(double organPrice) {
        this.organPrice = organPrice;
    }

    public String getUpdateName() {
        return updateName;
    }

    public void setUpdateName(String updateName) {
        this.updateName = updateName;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }

    public String getProductImgPath() {
        return productImgPath;
    }

    public void setProductImgPath(String productImgPath) {
        this.productImgPath = productImgPath;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getMonthlySales() {
        return monthlySales;
    }

    public void setMonthlySales(int monthlySales) {
        this.monthlySales = monthlySales;
    }

    public int getGoodNum() {
        return goodNum;
    }

    public void setGoodNum(int goodNum) {
        this.goodNum = goodNum;
    }

    public int getBadNum() {
        return badNum;
    }

    public void setBadNum(int badNum) {
        this.badNum = badNum;
    }
}
