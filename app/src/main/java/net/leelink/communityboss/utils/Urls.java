package net.leelink.communityboss.utils;

import net.leelink.communityboss.housekeep.HousekeepMainActivity;

import java.util.StringTokenizer;

public class Urls {
    //   public static final String WEBSITE = "http://122.225.60.118:6280/xdapp/api/Store/";
//    public static final String WEBSITE = "http://192.168.16.91:8888/sh/storeApp/";
    public static final String TEST_URL = "http://192.168.16.91";
    public static final String URL = "http://221.238.204.114";
//    public static final String URL = "http://api.llky.net";
    public static final String WEBSITE = URL+":8888/sh/storeApp/";
//    public static final String HS_WEBSITE = "http://192.168.16.91:8888/sh/homeStore/";
    public static final String HS_WEBSITE = URL+":8888/sh/homeStore/";


//    public static final String IMG_URL = "http://192.168.16.91:8888/files";
    public static final String IMG_URL = URL+":8888/files";
    public static final String SENDSMSCODE = URL+":8888/sh/user/send";      //发送验证码
    public static final String VERSION = URL+":8888/app/version";   //获取版本更新
//    public static final String SENDSMSCODE = "http://192.168.16.91:8888/sh/user/send";      //发送验证码
    public static final String REGIST = WEBSITE + "regist";   //注册
    public static final String LOGIN = WEBSITE + "login";     //用户名密码登录
    public static final String LOGINBYCODE = WEBSITE + "loginByCode";   //短信验证码登录
    public static final String STOREINFO = WEBSITE + "StoreInfo";     //修改商户信息
  //  public static final String UPLOADIMAGE = WEBSITE + "UploadImage";    //上传商家图片(商户头像,营业执照等)
    public static final String CHANGEPASSWORD = WEBSITE + "changePassword";    //忘记密码
    public static final String ORDERLIST = WEBSITE + "orderList";    //获取订单列表
    //  public static final String COMMODITY = WEBSITE +"Commodity";    //新增商品信息,编辑商品信息,获取商品列表,删除商品
    //public static final String STOREHOME = WEBSITE + "StoreHome";    //商户信息(我的)
  //  public static final String PHONENUMBER = WEBSITE + "PhoneNumber";    //修改绑定电话
  //  public static final String BANKCARD = WEBSITE + "BankCard";  //获取银行卡列表/添加绑定银行卡
//    public static final String STOREINCOME = WEBSITE + "StoreIncome";    //商户收入查询
    public static final String ORDEROPERATION = WEBSITE + "OrderOperation";  //修改订单状态
    public static final String ORDERDETAILS = WEBSITE + "orderDetails";    //获取订单详情
    public static final String CASHOUT = WEBSITE + "CashOut";    //申请提现
    public static final String LOGOUT = WEBSITE + "Logout";    //退出登录
//    public static final String APPRAISELIST = WEBSITE + "AppraiseList";  //获取评价列表
    public static final String STOREFAQ = WEBSITE + "StoreFaq";  //常见问题列表

    public static final String REGISTER = WEBSITE + "register"; //商家认证
    public static final String COMMODITY = WEBSITE + "commodity";   //新增上架物品
    public static final String COMMODITYIMG = WEBSITE + "commodityImg";    //修改上架物品
    public static final String STOREHOME = WEBSITE +"storeHome";    //商家个人信息
    public static final String BINDCARD = WEBSITE+"bindCard";   //绑定银行卡 /查看
    public static final String BANK = URL+":8888/sh/user/bank";    //查询支持的银行
    public static final String FAQ = WEBSITE+"faq"; //我的客服
    public static final String STOREINCOME = WEBSITE +"storeIncome";    //收入统计
    public static final String PHONENUMBER = WEBSITE+"phoneNumber"; //修改绑定手机号
    public static final String UPLOADIMAGE = WEBSITE +"uploadImage";    //修改商家头像
    public static final String APPRAISELIST = WEBSITE +"appraiseList";  //评价列表
    public static final String UPDATESTOREINGO = WEBSITE +"updateStoreInfo";    //修改商家信息
    public static final String ORDERSTATE = WEBSITE +"orderState";  //修改订单状态
    public static final String REPLYAPPRAISE = WEBSITE +"replyAppraise";    //商家回复评价
    public static final String REFUND = WEBSITE +"refund";  //订单退款
    public static final String QUICKLOGIN = WEBSITE +"quickLogin";  //快速登录
    public static final String TXAOUNT = WEBSITE +"txAount";    //提现

    //家政接口
    public static final String VERTIFYSER = HS_WEBSITE +"vertifySer";  //员工审核
    public static final String PRODUCT = HS_WEBSITE+"product";  //服务项目
    public static final String PRODUCTIMG = HS_WEBSITE +"productImg";   //修改服务项目
    public static final String SERPRODUCT = HS_WEBSITE +"serProduct";   //服务人员列表
    public static final String FINDSERALLBYUSERID = HS_WEBSITE+"findSerAllByUserId";    //查询已分配项目
    public static final String FINDSERBYUSERID = HS_WEBSITE+"findSerByUserId";  //查询所有项目
    public static final String HS_ORDERLIST = HS_WEBSITE +"orderList";  //订单列表
    public static final String HS_ORDERSTATE = HS_WEBSITE +"orderState";   //修改订单状态
    public static final String WORKLIST = HS_WEBSITE +"workList"; //工单列表
    public static final String ANALYSIS = HS_WEBSITE +"analysis";   //查询收入统计
    public static final String FINDSERWORKBYUSERID = HS_WEBSITE+"findSerWorkByUserId";  //查询员工已分配(未完成)订单
    public static final String PROVIDERINFO = HS_WEBSITE+"providerInfo";    //服务商个人信息
    public static final String WORKSER = HS_WEBSITE +"workSer"; //可分配人员列表


}
