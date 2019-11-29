package net.leelink.communityboss.utils;

public class Urls {
    public static final String WEBSITE = "http://122.225.60.118:6280/xdapp/api/Store/";
    public static final String IMAGEHEAD = "http://122.225.60.118:6280/xdapp/Image/Customer/HeadImage/";    //用户端头像
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
    public static final String PHONENUMBER = WEBSITE +"PhoneNumber";    //修改绑定电话
    public static final String BANKCARD = WEBSITE +"BankCard";  //获取银行卡列表/添加绑定银行卡
    public static final String STOREINCOME = WEBSITE +"StoreIncome";    //商户收入查询
    public static final String ORDEROPERATION = WEBSITE +"OrderOperation";  //修改订单状态
    public static final String ORDERDETAILS = WEBSITE +"OrderDetails";    //获取订单详情
    public static final String CASHOUT = WEBSITE +"CashOut";    //申请提现
    public static final String LOGOUT =WEBSITE+"Logout";    //退出登录
    public static final String APPRAISELIST = WEBSITE +"AppraiseList";  //获取评价列表
}
