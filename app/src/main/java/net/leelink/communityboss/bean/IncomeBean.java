package net.leelink.communityboss.bean;

import java.util.List;

public class IncomeBean {



    private String month;
    private String monthAmount;
    private List<StoreInComeVoBean> storeInComeVo;

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getMonthAmount() {
        return monthAmount;
    }

    public void setMonthAmount(String monthAmount) {
        this.monthAmount = monthAmount;
    }

    public List<StoreInComeVoBean> getStoreInComeVo() {
        return storeInComeVo;
    }

    public void setStoreInComeVo(List<StoreInComeVoBean> storeInComeVo) {
        this.storeInComeVo = storeInComeVo;
    }

    public static class StoreInComeVoBean {
        /**
         * orderNo : D2020122901200211
         * createTime : 2020-12-29 13:23:29
         * amount : 2
         */

        private String orderNo;
        private String createTime;
        private String amount;

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }
    }
}
