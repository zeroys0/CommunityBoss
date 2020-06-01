package net.leelink.communityboss.bean;

public class OrderBean {


    private float actualPayPrice;
    private String appointTime;
    private String orderId;
    private String orderNo;
    private int orderState;

    public int getOrderState() {
        return orderState;
    }

    public void setOrderState(int orderState) {
        this.orderState = orderState;
    }

    public float getActualPayPrice() {
        return actualPayPrice;
    }

    public void setActualPayPrice(float actualPayPrice) {
        this.actualPayPrice = actualPayPrice;
    }

    public String getAppointTime() {
        return appointTime;
    }

    public void setAppointTime(String appointTime) {
        this.appointTime = appointTime;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
}
