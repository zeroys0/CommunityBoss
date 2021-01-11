package net.leelink.communityboss.utils;


public class Urls {


    public static Urls getInstance() {
        return new Urls();
    }

    public static String IP = "";
    public static String H5_IP = "";

    public String WEBSITE = IP + "/sh/storeApp/";

    public String HS_WEBSITE = IP + "/sh/homeStore/";

    //获取版本信息
    public static String VERSION = "http://api.llky.net:8888/app/version";   //获取版本更新

    //获取商户编码
    public static String PARTNER_CODE = "http://api.llky.net:8888/partner/user/";

    public String IMG_URL = IP + "/files";
    public String SENDSMSCODE = IP + "/sh/user/send";      //发送验证码
    public String REGIST = WEBSITE + "regist";   //注册
    public String LOGIN = WEBSITE + "login";     //用户名密码登录
    public String LOGINBYCODE = WEBSITE + "loginByCode";   //短信验证码登录
    public String STOREINFO = WEBSITE + "StoreInfo";     //修改商户信息
    public String CHANGEPASSWORD = WEBSITE + "changePassword";    //忘记密码
    public String ORDERLIST = WEBSITE + "orderList";    //获取订单列表
    public String ORDEROPERATION = WEBSITE + "OrderOperation";  //修改订单状态
    public String ORDERDETAILS = WEBSITE + "orderDetails";    //获取订单详情
    public String CASHOUT = WEBSITE + "CashOut";    //申请提现
    public String LOGOUT = WEBSITE + "Logout";    //退出登录
    public String STOREFAQ = WEBSITE + "StoreFaq";  //常见问题列表

    public String REGISTER = WEBSITE + "register"; //商家认证
    public String COMMODITY = WEBSITE + "commodity";   //新增上架物品
    public String COMMODITYIMG = WEBSITE + "commodityImg";    //修改上架物品
    public String STOREHOME = WEBSITE + "storeHome";    //商家个人信息
    public String BINDCARD = WEBSITE + "bindCard";   //绑定银行卡 /查看
    public String BANK = IP + "/sh/user/bank";    //查询支持的银行
    public String FAQ = WEBSITE + "faq"; //我的客服
    public String STOREINCOME = WEBSITE + "storeIncome";    //收入统计
    public String PHONENUMBER = WEBSITE + "phoneNumber"; //修改绑定手机号
    public String UPLOADIMAGE = WEBSITE + "uploadImage";    //修改商家头像
    public String APPRAISELIST = WEBSITE + "appraiseList";  //评价列表
    public String UPDATESTOREINGO = WEBSITE + "updateStoreInfo";    //修改商家信息
    public String STORE_INCOME = WEBSITE +"store-Income";   //商家收入统计(新)
    public String ACCOUNT = WEBSITE +"account"; //查询账户余额(新)
    public String STORE_TX = WEBSITE +"store-tx";   //商家提现统计(新)
    public String ORDERSTATE = WEBSITE + "orderState";  //修改订单状态
    public String REPLYAPPRAISE = WEBSITE + "replyAppraise";    //商家回复评价
    public String REFUND = WEBSITE + "refund";  //订单退款
    public String QUICKLOGIN = WEBSITE + "quickLogin";  //快速登录
    public String TXAOUNT = WEBSITE + "txAount";    //提现
    public String ORDER_ONLY = WEBSITE + "order-only";      //确认用户自取
    //获取街道
    public String GETTOWN = IP + "/sysDict/getTown";

    //家政接口
    public String VERTIFYSER = HS_WEBSITE + "vertifySer";  //员工审核
    public String PRODUCT = HS_WEBSITE + "product";  //服务项目
    public String PRODUCTIMG = HS_WEBSITE + "productImg";   //修改服务项目
    public String SERPRODUCT = HS_WEBSITE + "serProduct";   //服务人员列表
    public String FINDSERALLBYUSERID = HS_WEBSITE + "findSerAllByUserId";    //查询已分配项目
    public String FINDSERBYUSERID = HS_WEBSITE + "findSerByUserId";  //查询所有项目
    public String HS_ORDERLIST = HS_WEBSITE + "orderList";  //订单列表
    public String HS_ORDERSTATE = HS_WEBSITE + "orderState";   //修改订单状态
    public String WORKLIST = HS_WEBSITE + "workList"; //工单列表
    public String ANALYSIS = HS_WEBSITE + "analysis";   //查询收入统计
    public String FINDSERWORKBYUSERID = HS_WEBSITE + "findSerWorkByUserId";  //查询员工已分配(未完成)订单
    public String PROVIDERINFO = HS_WEBSITE + "providerInfo";    //服务商个人信息
    public String WORKSER = HS_WEBSITE + "workSer"; //可分配人员列表


}
