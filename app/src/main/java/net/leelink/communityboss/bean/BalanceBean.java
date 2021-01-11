package net.leelink.communityboss.bean;

import java.util.List;

public class BalanceBean {

    private String entryAmount;
    private String outAmount;
    private String month;
    private List<AccountListBean> accountList;

    public String getEntryAmount() {
        return entryAmount;
    }

    public void setEntryAmount(String entryAmount) {
        this.entryAmount = entryAmount;
    }

    public String getOutAmount() {
        return outAmount;
    }

    public void setOutAmount(String outAmount) {
        this.outAmount = outAmount;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public List<AccountListBean> getAccountList() {
        return accountList;
    }

    public void setAccountList(List<AccountListBean> accountList) {
        this.accountList = accountList;
    }

    public static class AccountListBean {
        /**
         * accountId : 5
         * accountState : 1
         * amount : 2
         * historyAmount : 38
         * title : 订单收入
         * createTime : 2020-12-29 13:23:29
         * storeId : 55
         * orderId : 145
         */

        private String accountId;
        private String accountState;
        private String amount;
        private String historyAmount;
        private String title;
        private String createTime;
        private String storeId;
        private String orderId;

        public String getAccountId() {
            return accountId;
        }

        public void setAccountId(String accountId) {
            this.accountId = accountId;
        }

        public String getAccountState() {
            return accountState;
        }

        public void setAccountState(String accountState) {
            this.accountState = accountState;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getHistoryAmount() {
            return historyAmount;
        }

        public void setHistoryAmount(String historyAmount) {
            this.historyAmount = historyAmount;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getStoreId() {
            return storeId;
        }

        public void setStoreId(String storeId) {
            this.storeId = storeId;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }
    }
}
