package net.leelink.communityboss.utils;

public class Urls {
    public static final String WEBSITE = "http://122.225.60.118:6280/xdapp/api/Store/";
    public static final String SENDSMSCODE = "http://122.225.60.118:6280/xdapp/api/Smscode/SendSmscode";    //发送验证码
    public static final String AUTHSMSCODE = "http://122.225.60.118:6280/xdapp/api/Smscode/AuthSmscode";    //验证短信验证码是否有效
    public static final String REGISTER = WEBSITE+"Register";   //注册
    public static final String LOGIN = WEBSITE+"Login";     //用户名密码登录
    public static final String LOGINBYCODE = WEBSITE +"LoginByCode";   //短信验证码登录
    public static final String STOREINFO = WEBSITE+"StoreInfo";     //修改商户信息
}
