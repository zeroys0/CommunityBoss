package net.leelink.communityboss.utils;

public class Urls {
    public static final String WEBSITE = "http://122.225.60.118:6280/xdapp/api/Store/";
    public static final String IMAGEURL= "http://122.225.60.118:6280/xdapp/Image/";     //图片地址
    public static final String SENDSMSCODE = "http://122.225.60.118:6280/xdapp/api/Smscode/SendSmscode";    //发送验证码
    public static final String AUTHSMSCODE = "http://122.225.60.118:6280/xdapp/api/Smscode/AuthSmscode";    //验证短信验证码是否有效
    public static final String REGISTER = WEBSITE+"Register";   //注册
    public static final String LOGIN = WEBSITE+"Login";     //用户名密码登录
    public static final String LOGINBYCODE = WEBSITE +"LoginByCode";   //短信验证码登录
    public static final String STOREINFO = WEBSITE+"StoreInfo";     //修改商户信息
    public static final String UPLOADIMAGE = WEBSITE +"UploadImage";    //上传商家图片(商户头像,营业执照等)
    public static final String RESETPASSWORD = WEBSITE +"ResetPassword";    //忘记密码
    public static final String ORDERLIST = WEBSITE +"OrderList";    //获取订单列表
    public static final String COMMODITY = WEBSITE +"Commodity";    //新增商品信息,编辑商品信息,获取商品列表,删除商品
    public static final String STOREHOME = WEBSITE +"StoreHome";    //商户信息(我的)
}
