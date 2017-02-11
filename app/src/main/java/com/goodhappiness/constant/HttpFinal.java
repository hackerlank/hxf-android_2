package com.goodhappiness.constant;

/**
 * Created by 电脑 on 2016/4/9.
 */
public final class HttpFinal {

    //v1.5.0 2016.10.20 18：00
    public static final String DOMAIN_RELEASE = "mypuduo";
    public static final String DOMAIN_DEBUG = "hxfapp";

    public static String DOMAIN = DOMAIN_DEBUG;
    public static final String HTTP_HEAD = "http://apiv2."+DOMAIN+".com";//120.76.148.69:8083
    public static final String SHOP_URL = "http://wx."+DOMAIN+".com/v2/shop/index";//120.76.148.69:8083
    public static final String INSRALL_NORMAL = "http://wx."+DOMAIN+".com/v2/user/insrallnormal";//120.76.148.69:8083
    public static final String AUTH_KEY = "D6F8DEB21DE384DBBC095BC9EFB1B4159EA47E09";
    public static final String GOTO_LOGIN = "gotoLogin";
    public static final String GOTO_GOODS_DETAIL = "gotoGoodsDetail";
    public static final String GOTO_USER_INFO = "gotoUserInfo";
    public static final String GOTO_DONATE_SUCCESS = "gotoDonateSuccess";
    public static final String GOTO_EXCHANGE = "gotoExchange";
    public static final String GOTO_RECHARGE = "gotoRecharge";
    public static final String GOTO_SHOP = "gotoShop";
    public static final String GOTO_FEEDS = "gotoFeeds";
    public static final String GOTO_GOODS = "gotoGoods";
    public static final String GOTO_REGISTER = "gotoRegister";
    public static final String GOTO_BIND_USER_MOBILE = "gotoBindUserMobile";
    public static final String GOTO_WINNER = "gotoWinner";
    public static final String GOTO_PERIOD = "gotoPeriod";
    public static final String GOTO_REDBAG = "gotoRedbag";
    public static final String GOTO_TABBAR = "gotoTabbar";
    public static final String GOTO_WEIXINPAY = "gotoWeixinPay";
    public static final String GOTO_ALIYPAY = "gotoAlipay";
    public static final String GOTO_SHARE = "gotoShare";
    public static final String DETAIL_PRODUCT_ID = "wx."+DOMAIN+".com/v2/shop/detail?productId";
    public static final String SERVE_CLAUSE = "http://wx."+DOMAIN+".com/v2/login/serveclause";
    public static final String INVITE = "http://wx."+DOMAIN+".com/v2/activity/invite";//邀请注册
    public static final String SIGN = "http://wx."+DOMAIN+".com/v2/user/check";//邀请注册
    public static final String DONATE = "http://wx."+DOMAIN+".com/v2/activity/donate";//邀请送 花
    public static final String GET_IMG_CODE = "http://apiv2."+DOMAIN+".com/v2/user/getImgCode";//图形验证码


    public static final String SMS = HTTP_HEAD + "/v2/user/sms";
    public static final String LOTTERY_LIST = HTTP_HEAD + "/v2/goods/list";
    public static final String REGISTER = HTTP_HEAD + "/v2/user/register";
    public static final String BIND_USER_MOBILE = HTTP_HEAD + "/v2/user/bindUserMobile";
    public static final String PASSWORD_RESET = HTTP_HEAD + "/v2/user/passwordReset";
    public static final String INIT = HTTP_HEAD + "/v2/app/register";
    public static final String LOGIN = HTTP_HEAD + "/v2/user/login";
    public static final String LOGOUT = HTTP_HEAD + "/v2/user/logout";
    public static final String APP_THIRD_LOGIN = HTTP_HEAD + "/v2/user/appThirdLogin";
    public static final String HOME_INDEX = HTTP_HEAD + "/v2/home/index";
    public static final String LOTTERY_DETAIL = HTTP_HEAD + "/v2/goods/detail";
    public static final String CAR_ADD = HTTP_HEAD + "/v2/cart/add";
    public static final String SHARE = HTTP_HEAD + "/v2/app/share";
    public static final String CAR_LIST = HTTP_HEAD + "/v2/cart/list";
    public static final String CAR_DELETE = HTTP_HEAD + "/v2/cart/delete";
    public static final String GET_WINNER = HTTP_HEAD + "/v2/goods/getWinner";
    public static final String WIN_RECORD = HTTP_HEAD + "/v2/goods/getDuoQuanRecord";//夺券纪录
    public static final String CONFIRM = HTTP_HEAD + "/v2/order/confirm";//创建订单
    public static final String CART_SUBMIT = HTTP_HEAD + "/v2/cart/submit";//结算
    public static final String REVEAL_LIST = HTTP_HEAD + "/v2/goods/revealList";//最新揭晓
    public static final String CASHIER_CONFIRM = HTTP_HEAD + "/v2/cashier/confirm";//最新揭晓
    public static final String CASHIER_DONATE_CONFIRM = HTTP_HEAD + "/v2/cashier/donateConfirm";//最新揭晓
    public static final String DONATE_RESULT = HTTP_HEAD + "/v2/donate/result";//最新揭晓
    public static final String QINIU_TOKEN = HTTP_HEAD + "/v2/app/uploadToken";//七牛token获取接口
    public static final String PERIODS_AWARD = HTTP_HEAD + "/v2/user/periodsAward";//用户中奖记录
    public static final String USER_EXCHANGE = HTTP_HEAD + "/v2/user/userExchange";//用户中奖记录
    public static final String PERIODS = HTTP_HEAD + "/v2/user/periods";//用户夺宝记录
    public static final String RESULT = HTTP_HEAD + "/v2/order/result";//支付结果
    public static final String USER_ADDRESS = HTTP_HEAD + "/v2/user/userAddress";//用户地址列表
    public static final String USER_ADDRESS_SAVE = HTTP_HEAD + "/v2/user/userAddressSave";//用户地址保存
    public static final String USER_ADDRESS_DELETE = HTTP_HEAD + "/v2/user/userAddressDelete";//用户地址删除
    public static final String RECHARGE = HTTP_HEAD + "/v2/cashier/recharge";//充值
    public static final String RECHARGE_RESULT = HTTP_HEAD + "/v2/recharge/result";//充值结果
    public static final String UPDATE_USER_INFO = HTTP_HEAD + "/v2/user/updateUserInfo";//用户信息更新
    public static final String USER_INFO = HTTP_HEAD + "/v2/user/userInfo";//用户信息
    public static final String USER_RECHARGE = HTTP_HEAD + "/v2/user/userRecharge";//
    public static final String USER_PERIODS_CODE = HTTP_HEAD + "/v2/user/userPeriodsCode";//
    public static final String USER_EXCHANGE_DETAIL = HTTP_HEAD + "/v2/user/exchangeDetail";//关注
    public static final String REPORT = HTTP_HEAD + "/v2/post/report";//关注
    public static final String DONATE_CONFIRM = HTTP_HEAD + "/v2/donate/confirm";//提交送花订单
    public static final String TAG_SEARCH = HTTP_HEAD + "/v2/post/tagSearch";//发布时标签搜索
    public static final String SEARCH = HTTP_HEAD + "/v2/post/search";//搜索用户or标签列表V2
    public static final String DONATE_LIST = HTTP_HEAD + "/v2/donate/list";//提交送花订单

    public static final String POST_DELETE = HTTP_HEAD + "/v2/post/delete";//用户发布的朋友圈
    public static final String PUBLISH = HTTP_HEAD + "/v2/post/publish";//用户发布的朋友圈
    public static final String POST_LIST = HTTP_HEAD + "/v2/post/list";//朋友圈列表
    public static final String POST_INDEX = HTTP_HEAD + "/v2/post/index";//朋友圈详情
    public static final String POST_CREATE = HTTP_HEAD + "/v2/post/create";//发布朋友圈
    public static final String LIKE_CREATE = HTTP_HEAD + "/v2/like/create";//朋友圈列表点赞
    public static final String LIKE_DELETE = HTTP_HEAD + "/v2/like/delete";//朋友圈列表点赞
    public static final String COMMENT_CREATE = HTTP_HEAD + "/v2/comment/create";//朋友圈评论
    public static final String FRIENDSHIP_CREATE = HTTP_HEAD + "/v2/friendship/create";//关注
    public static final String FRIENDSHIP_DELETE = HTTP_HEAD + "/v2/friendship/delete";//取消关注
    public static final String COMMENT_DELETE = HTTP_HEAD + "/v2/comment/delete";//删除评论接口V2
    public static final String FRIENDSHIP = HTTP_HEAD + "/v2/friendship/list";//关注/粉丝列表
    public static final String COMMENT_TO_ME = HTTP_HEAD + "/v2/comment/tome";//评论/回复我的
    public static final String LIKE_TO_ME = HTTP_HEAD + "/v2/like/tome";//评论/回复我的
    public static final String RED_BAG = HTTP_HEAD + "/v2/user/redBag";//评论/回复我的
    public static final String USER_EXCHANGE_STATUS_CONFIRM = HTTP_HEAD + "/v2/user/exchangeStatusConfirm";//兑换详情信息确认
    public static final String CHANNEL = HTTP_HEAD + "/v2/post/channel";//频道列表
    public static final String ACTIVE = HTTP_HEAD + "/v2/app/active";//open or quit

    public static final String WECHAT_AUTH = "https://api.weixin.qq.com/sns/oauth2/access_token";//兑换详情信息确认
    public static final String WECHAT_USERINFO = "https://api.weixin.qq.com/sns/userinfo";//兑换详情信息确认
}
