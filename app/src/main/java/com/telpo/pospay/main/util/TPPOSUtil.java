package com.telpo.pospay.main.util;


import static com.telpo.pospay.main.data.GlobalParams.strFormat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;

import com.telpo.emv.util.ByteArrayUtil;
import com.telpo.emv.util.DataProcessUtil;
import com.telpo.pinpad.PinpadService;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class TPPOSUtil {
    public static int pinpadOpenTimes = 0;

    public final static int OK_SUCCESS = 0;       //成功

    //PackData.needActionType和
    //POSData.transType交易类型相关定义:

    /**
     * 下载主密钥
     */
    public final static int TYPE_DOWN_MASTERKEY = 101;    //请求 下载主密钥

    /**
     * 下载磁条卡参数
     */
    public final static int TYPE_DOWN_PARAMETER_TERMINAL = 102;    //请求 下载磁条卡参数

    /**
     * 下载CAPK参数
     */
    public final static int TYPE_DOWN_PARAMETER_CAPK = 103;    //请求 下载第CAPK参数

    /**
     * 下载AID参数
     */
    public final static int TYPE_DOWN_PARAMETER_AID = 104;    //请求 下载第AID参数

    /**
     * 查询AID应用参数版本
     */
    public final static int TYPE_QUERY_AID_VERSION = 105;    //请求 查询AID应用参数版本

    /**
     * 查询公钥参数版本
     */
    public final static int TYPE_QUERY_CAPK_VERSION = 106;    //请求 查询公钥参数版本

    /**
     * 签到
     */
    public final static int TYPE_SIGN_IN = 107;    //请求 签到

    /**
     * 消费
     */
    public final static int TYPE_CONSUME = 108;    //请求 消费

    /**
     * 消费撤销
     */
    public final static int TYPE_CONSUME_VOID = 109;    //请求 消费撤销

    /**
     * 预授权
     */
    public final static int TYPE_PRE_AUTHORIZE = 110;    //请求 预授权

    /**
     * 预授权撤销
     */
    public final static int TYPE_PRE_AUTHORIZE_VOID = 111;    //请求 预授权撤销

    /**
     * 预授权完成
     */
    public final static int TYPE_PRE_AUTHORIZE_FINISH = 112;    //请求 预授权完成

    /**
     * 预授权完成通知
     */
    public final static int TYPE_PRE_AUTHORIZE_FINISH_ADVICE = 113;    //请求 预授权完成通知

    /**
     * 预授权完成撤销
     */
    public final static int TYPE_PRE_AUTHORIZE_FINISH_VOID = 114;    //请求 预授权完成撤销

    /**
     * 冲正
     */
    public final static int TYPE_REVERSAL = 115;    //请求 冲正

    /**
     * 消费退货
     */
    public final static int TYPE_CONSUME_REFUND = 116;    //请求 消费退货

    /**
     * 结算
     */
    public final static int TYPE_SETTLE = 117;    //请求 结算

    /**
     * 查询余额
     */
    public final static int TYPE_BALANCE = 118;    //请求 查询余额

    /**
     * pos状态上送
     */
    public final static int TYPE_POS_STATE_UPLOAD = 119;    //pos状态上送

    /**
     * 预授权冲正
     */
    public final static int TYPE_PRE_AUTHORIZE_REVERSAL = 120;    //预授权冲正

    /**
     * 预授权完成冲正
     */
    public final static int TYPE_PRE_AUTHORIZE_END_REVERSAL = 121;    //预授权完成冲正

    /**
     * 下载AID完成
     */
    public final static int TYPE_DOWN_PARAMETER_AID_FINISHED = 123;    //请求 下载完成

    /**
     * 下载capk完成
     */
    public final static int TYPE_DOWN_PARAMETER_CAPK_FINISHED = 124;    //请求 下载完成

    /**
     * 下载黑名单完成
     */
    public final static int TYPE_DOWN_PARAMETER_BLACKLIST_FINISHED = 125;    //请求 下载完成

    /**
     * 下载黑名单
     */
    public final static int TYPE_DOWN_PARAMETER_BLACKLIST = 130;    //请求 下载黑名单

    /**
     * 下载TMS参数
     */
    public final static int TYPE_DOWN_PARAMETER_NFC = 131;    //请求 下载TMS参数

    /**
     * 下载NFC参数完成
     */
    public final static int TYPE_DOWN_PARAMETER_NFC_FINISHED = 132;    //请求 下载完成

    /**
     * NFC黑名单下载完成
     */
    public final static int TYPE_DOWN_PARAMETER_NFC_BLACKLIST_FINISHED = 133;    //请求NFC黑名单下载完成

    /**
     * NFC下载黑名单
     */
    public final static int TYPE_DOWN_PARAMETER_NFC_BLACKLIST = 134;    //请求NFC下载黑名单

    /**
     * NFC白名单下载完成
     */
    public final static int TYPE_DOWN_PARAMETER_NFC_WHITELIST_FINISHED = 135;    //请求NFC白名单下载完成

    /**
     * NFC下载白名单
     */
    public final static int TYPE_DOWN_PARAMETER_NFC_WHITELIST = 136;    //请求NFC下载白名单

    /**
     * 签退
     */
    public final static int TYPE_SIGN_OUT = 138;    //请求 签退
    public final static int TYPE_DOWN_PARAMETER_TMS = 139;    //请求 下载TMS参数

    /**
     * 电子化凭条
     */
    public final static int TYPE_ELEC_SIGN = 140;    //电子化凭条

    /**
     * 部分电子签字
     */
    public final static int TYPE_ELEC_SIGN_PART = 141;    //部分电子签字


    //二维码支付相关
    /**
     * 请求微信支付交易，手机微信二维码被扫（被扫模式）
     */
    public final static int TYPE_WECHAT_CODE_PAY = 201;    //请求微信支付交易，手机微信二维码被扫（被扫模式）

    /**
     * 请求微信支付交易撤销（被扫模式）
     */
    public final static int TYPE_WECHAT_CODE_PAY_VOID = 202;    //请求微信支付交易撤销（被扫模式）

    /**
     * 请求微信支付交易查询（被扫模式）
     */
    public final static int TYPE_WECHAT_CODE_PAY_QUERY = 203;    //请求微信支付交易查询（被扫模式）

    /**
     * 微信请求下单交易，生成二维码(主扫模式)
     */
    public final static int TYPE_WECHAT_PREORDER_PAY = 204;   //微信请求下单交易，生成二维码(主扫模式)

    /**
     * 微信请求下单交易撤销(主扫模式)
     */
    public final static int TYPE_WECHAT_PREORDER_PAY_VOID = 205;   //微信请求下单交易撤销(主扫模式)

    /**
     * 微信请求下单交易查询(主扫模式)
     */
    public final static int TYPE_WECHAT_PREORDER_PAY_QUERY = 206;   //微信请求下单交易查询(主扫模式)

    /**
     * 请求支付宝支付交易，手机支付宝二维码被扫（被扫模式）
     */
    public final static int TYPE_ALIPAY_CODE_PAY = 207;    //请求支付宝支付交易，手机支付宝二维码被扫（被扫模式）

    /**
     * 请求支付宝支付交易撤销（被扫模式）
     */
    public final static int TYPE_ALIPAY_CODE_PAY_VOID = 208;    //请求支付宝支付交易撤销（被扫模式）

    /**
     * 请求支付宝支付交易查询（被扫模式）
     */
    public final static int TYPE_ALIPAY_CODE_PAY_QUERY = 209;    //请求支付宝支付交易查询（被扫模式）

    /**
     * 支付宝请求下单交易，生成二维码(主扫模式)
     */
    public final static int TYPE_ALIPAY_PREORDER_PAY = 210;   //支付宝请求下单交易，生成二维码(主扫模式)

    /**
     * 支付宝请求下单交易撤销(主扫模式)
     */
    public final static int TYPE_ALIPAY_PREORDER_PAY_VOID = 211;   //支付宝请求下单交易撤销(主扫模式)

    /**
     * 支付宝请求下单交易查询(主扫模式)
     */
    public final static int TYPE_ALIPAY_PREORDER_PAY_QUERY = 212;   //支付宝请求下单交易查询(主扫模式)

    /**
     * 支付宝微信扫码退款
     */
    public final static int TYPE_SCAN_REFUND = 213;//支付宝微信扫码退款

    //操作员相关
    public final static int OPERATOR_NO_EXIST = 300;           //该操作员不存在
    public final static int OPERATOR_PWD_ERR = 301;           //操作员密码错误
    public final static int OPERATOR_ID_INVALID = 302;           //操作员号格式不合法
    public final static int OPERATOR_PWD_INVALID = 304;           //操作员密码不合法,可以为"",但不能为null,
    public final static int OPERATOR_IS_EXIST = 305;           //增加操作员时,操作员已存在
    public final static int OPERATOR_ADD_ERR = 306;           //增加操作员时失败,(数据库失败)
    public final static int OPERATOR_DATABASE_ERR = 307;           //操作员(数据库失败)

    //packData返回值定义:
    public final static int ERR_TRANSTYPE_UNKNOW = 1000;   //未知的交易类型(无法处理当前POSData.transType的值)
    public final static int ERR_LACK_TERMINALID = 1001;   //输入参数(POSData)缺少终端号
    public final static int ERR_LACK_MERCHANTID = 1002;   //输入参数(POSData)缺少商户号
    public final static int ERR_LENGTH_TERMINALID = 1003;   //输入参数(POSData)的终端号长度错误(正确长度应该是8)
    public final static int ERR_LENGTH_TERMINALSN = 1012;   //输入参数(POSData)的终端序列号长度错误(正确长度应该是14)
    public final static int ERR_LENGTH_MERCHANTID = 1004;   //输入参数(POSData)的商户号长度错误(正确长度应该是15)
    public final static int ERR_INPUT_DATE_ERR = 1005;   //输入参数(POSData)的日期格式错误
    public final static int ERR_INPUT_TIME_ERR = 1006;   //输入参数(POSData)的时间格式错误
    public final static int ERR_DF27 = 1007;   //输入参数(POSData)的DF27有误(在查询AID参数版本时需要用到DF27)
    public final static int ERR_NO_NEED_UPDATE = 1008;   //packData()打包报文下载AID或CAPK时,无需下载了,参数已更新完毕
    public final static int ERR_OPERATORID = 1009;   //消费时,posdata.operatorID(操作员号)错误
    public final static int ERR_NO_AID_LIST = 1010;   //“请求下载第03块参数”时数据库错误,终端没有AID列表,需要先使用“请求查询AID参数版本”
    public final static int ERR_NO_CAPK_LIST = 1011;   //“请求下载第04块参数”时数据库错误,终端没有CAPK列表,需要先使用“请求查询CAPK参数版本”
    public final static int ERR_DOWNMASTERKEY_CHECKTRANSKEY = 1015;   //“请求下载主密钥”校验传输密钥出错
    public final static int ERR_DOWNMASTERKEY_CHECKMASTERKEY = 1013;   //“请求下载主密钥”校验主密钥和写出密钥出错
    public final static int ERR_DOWNMASTERKEY = 1014;   //“请求下载主密钥”出错

    public final static int ERR_NO_NEED_SETTLE = 1016;    //结算时发现不存在交易记录
    public final static int ERR_REVERSAL_MAX = 1017;   //冲正次数已达到最大
    public final static int ERR_NO_NEED_REVERSAL = 1018;   //冲正时发现不需要冲正
    public final static int ERR_NEED_REVERSAL = 1019;   //打包时发现存在待冲正记录
    public final static int ERR_NEED_SETTLE = 1020;   //打包时发现交易达到上限,需要结算
    public final static int ERR_PACK_UNKNOWERR = 1021;   //打包过程出现未知异常
    public final static int ERR_VOID_NO_OLD_TRANCEID = 1022;   //撤销时，未输入原流水号
    public final static int ERR_VOID_NOT_FOUND_RECORD = 1023;   //撤销时，未发现该交易记录
    public final static int ERR_VOID_PAN_NOT_MATCH = 1024;   //撤销时，发现卡号与原来的不对
    public final static int ERR_VOID_TRANS_NOT_MATCH = 1025;   //撤销时,找到原交易,交易类型不匹配,(例如当前撤销预授权完成,但是从输入的流水号找到原交易是消费)
    public final static int ERR_VOID_TRANS_HAS_VOID = 1026;   //撤销时,找到原交易,发现原交易已撤销
    public final static int ERR_REFUND_AMOUNT = 1027;   //退货时,需要输入退货金额
    public final static int ERR_REFUND_AUTHNO = 1028;   //退货时,需要输入原授权码
    public final static int ERR_REFUND_DATE = 1029;   //退货时,需要输入原日期(月月日日)
    public final static int ERR_REFUND_TIME = 1030;   //退货时,需要输入原时间(时时分分秒秒)
    public final static int ERR_PREAUTH_VOID_AMOUNT = 1031;   //预授权撤销时,需要输入金额
    public final static int ERR_PREAUTH_VOID_AUTHNO = 1032;   //预授权撤销时,需要输入原授权码
    public final static int ERR_PREAUTH_VOID_DATE = 1033;   //预授权撤销时,需要输入原日期(月月日日)
    public final static int ERR_PREAUTH_VOID_TIME = 1034;   //预授权撤销时,需要输入原时间(时时分分秒秒)
    public final static int ERR_PREAUTHFINISH_AMOUNT = 1035;   //预授权完成时,需要输入金额
    public final static int ERR_PREAUTHFINISH_AUTHNO = 1036;   //预授权完成时,需要输入原授权码
    public final static int ERR_NO_ELECT_SIGN = 1037;          //上传电子签名报文时，没有发现需要上传的记录

    //unPackData返回值定义:
    public final static int ERR_UNPACK = 2000;//解包错误或者数据包错误
    public final static int ERR_NO_RECEIVE_DATA = 2001;//解包时发现没有数据包
    public final static int ERR_RECEIVE_DATA_LENGTH = 2002;//解包时发现长度错误
    public final static int ERR_UNKNOW = 2003;//解包未知错误
    public final static int ERR_PINKEY_CHECK = 2005;//签到时,PIN工作密钥校验错误
    public final static int ERR_MACKEY_CHECK = 2006;//签到时,MAC工作密钥校验错误
    public final static int ERR_REDOWN_APP = 2007;//下载AID解包时,发现平台返回的AID与请求的AID不同,需要重新下载
    public final static int ERR_REDOWN_CAPK = 2008;//下载CAPK解包时,发现平台返回的CAPK与请求的CAPK不同,需要重新下载
    public final static int ERR_CHECK_PIN = 2009;//消费时平台返回“PIN密钥同步错误”
    public final static int ERR_TRANSACTION_NO_MATCH = 2010;//解包时发现,打包的类型不对, 打包和解包要一对, 打什么包,就要接着解什么包
    public final static int ERR_PARSE_BALANCE = 2011;//查余额的解包时,按照规范的格式,不能正确解析余额信息
    public final static int ERR_MAC_CHECK = 2012;//解包时,MAC校验错误
    public final static int ERR_WRITE_WORK_KEY = 2013;//签到解包时,写密钥失败
    public final static int ERR_RESPONSE_CODE = 2014;//解包成功但是平台应答码错误
    public final static int ERR_FIELD48 = 2030;//48域解析错误
    public final static int ERR_FIELD56 = 2031;//56域解析错误
    public final static int ERR_FIELD62 = 2032;//62域解析错误
    public final static int ERR_NO_FIELD62 = 2033;//没有62域
    public final static int ERR_NO_PARA = 2036;//POS中心没有要下载的参数

    public final static int ERR_NOT_FINISHED = 2034;//下载CAPK或AID一个报文装不下，要继续发送查询报文
    public final static int ERR_NO_FIELD48 = 2035;//结算报文没有48域
    //处理流程返回值:
    public final static int ERR_TRANS_FAILED = 3000;//交易失败
    public final static int ERR_NEED_EXPDATE_PAN = 3001;//手输卡号的交易流程必须要有卡号和卡有效期
    public final static int ERR_TRANS_OFFLINE_SUCCESS = 3002;//交易成功,离线交易成功
    public final static int ERR_TRANS_CARD_REJECT = 3003;//交易失败,卡片拒绝交易
    public final static int ERR_TRANS_OVERTRANS = 3004;//交易笔数达到上限，需要结算

    //刷卡类型:可组合使用
    public final static int MODE_MANUAL = 0x01;  //手工输入卡号  (二进制:00000001)
    public final static int MODE_MAGNETIC = 0x02;  //磁条卡        (二进制:00000010)
    public final static int MODE_INSER_IC = 0x04;  //接触式IC卡    (二进制:00000100)
    public final static int MODE_NFC = 0x08;  //非接触式IC卡  (二进制:00001000)

    //卡片类型标志,用于POSData.cardProperty,应用开发者在onlineCallback时可取
    public final static int CARD_CREDIT = 2;  //信用卡
    public final static int CARD_DEBIT = 1;  //借记卡
    public final static int CARD_UNKNOW = 0;  //未知卡片账户类型
    public final static int ERR_ICCCMD = (-20);//IC命令失败
    public final static int READCARD_SUCCESS = 0;  //成功
    public final static int READCARD_TIMEOUT = -100;  //超时
    public final static int READCARD_USER_CANCEL = -101;  //寻卡时,用户取消寻卡
    public final static int READCARD_INIT_FAILED = -102;  //初始化失败
    public final static int READCARD_FORCE_IC = -103;  //强制使用IC卡
    public final static int READCARD_GET_TRACKDATA_FAILED = -104;  //获取磁道数据失败
    public final static int READCARD_TRACKDATA2_ERROR = -105;  //第二磁道数据错误

    public final static int READCARD_ERROR_IC_EMVINIT = -106;  //EMV库初始化错误
    public final static int READCARD_ERROR_IC_SELECT_APP = -107; //IC卡选择应用错误
    public final static int READCARD_ERROR_IC_READ_DATA = -108; //IC卡读数据错误
    public final static int READCARD_ERROR_IC_AUTHEN = -109; //IC卡数据认证错误

    public final static int READCARD_ERROR_IC_PAN = -110; //IC卡账号错误
    public final static int READCARD_ERROR_IC_UNKNOW = -111; //IC卡未知错误
    public final static int READCARD_down_to_MagStripe = -112; //降级使用磁条卡

    public final static int ERROR_OPEN_IC = -113; //打开IC模块错误
    public final static int ERROR_OPEN_NFC = -114; //打开NFC模块错误
    public final static int ERROR_OPEN_MAGNETIC = -115; //打开磁条卡模块错误
    public final static int ERROR_READ_CARD_MODE = -116; //读卡模式错误
    public final static int ERROR_CLOSE_IC = -117; //关闭IC模块错误
    public final static int ERROR_CLOSE_NFC = -118; //关闭NFC模块错误
    public final static int ERROR_CLOSE_MAGNETIC = -119; //关闭磁条卡模块错误

    public final static int ERROR_OPEN_EMVSERVICE = -200; //打开EMV库失败
    public final static int ERROR_OPEN_DEVICE = -201; //打开EMV设备失败
    public final static int ERROR_OPEN_PINPAD = -202; //打开PINPAD失败
    public final static int ERROR_CLOSE_DEVICE = -203; //关闭EMV设备失败
    public final static int ERROR_CLOSE_PINPAD = -204; //关闭PINPAD失败
    public final static int ERROR_PROHIBIT_ICCARD = -206; //pos禁止IC卡交易


    static private android.content.SharedPreferences sp;
    static private android.content.SharedPreferences.Editor editor;

    private static android.content.Context mContext;


    /**
     * 添加默认的AID
     *
     * @return
     */
    public static int addDefaultAID() {
        com.telpo.emv.EmvService.Emv_RemoveAllApp();
        com.telpo.pospay.main.data.GlobalParams.Add_Default_APP();
        return 0;
    }

    /**
     * 添加默认的CAPK
     *
     * @return
     */
    public static int addDefaultCAPK() {
        com.telpo.emv.EmvService.Emv_RemoveAllCapk();
        com.telpo.pospay.main.data.GlobalParams.Add_Default_CAPK();
        return 0;
    }


    /**
     * 各类交易前,初始化数据,使整个流程使用同一份数据
     */
    public static void initPosData() {
        com.telpo.base.util.MLog.i("【initPosData】");
        com.telpo.pospay.main.data.GlobalParams.gPosData = new com.telpo.pospay.main.data.POSData();
        com.telpo.pospay.main.data.GlobalParams.gPackData = new com.telpo.pospay.main.data.PackData();
    }

    /**
     * 获取当前交易正在使用的那个POSData
     *
     * @return
     */
    public static com.telpo.pospay.main.data.POSData getCurrentPosData() {
        return com.telpo.pospay.main.data.GlobalParams.gPosData;
    }

    /**
     * 获取当前交易正在使用的那个PackData
     *
     * @return
     */
    public static com.telpo.pospay.main.data.PackData getCurrentPackData() {
        return com.telpo.pospay.main.data.GlobalParams.gPackData;
    }

    /**
     * 检查是否存在需要冲正的记录
     *
     * @param context
     * @return
     */
//    public static boolean checkIfNeedReversal(android.content.Context context) {
//        com.telpo.base.util.MLog.i("开始查询冲正");
//        TranDBReversalDao tranDBReversalDao = new TranDBReversalDao(context);
//
//        //数据库没记录
//        java.util.List<TranDBReversal> list = tranDBReversalDao.list();
//        if (list == null || list.size() == 0) {
//            com.telpo.base.util.MLog.i("数据库没冲正");
//            return false;
//        }
//
//        TranDBReversal tranDBReversal = tranDBReversalDao.findByMaxId();
//        if (tranDBReversal != null && tranDBReversal.isNeedReversal == 0) {
//            com.telpo.base.util.MLog.i("找到冲正,交易类型: " + tranDBReversal.tranType +
//                    "\n流水号  : " + tranDBReversal.field_11 +
//                    "\n时间    : " + tranDBReversal.field_12 +
//                    "\n日期    : " + tranDBReversal.field_13 +
//                    "\n冲正次数: " + tranDBReversal.reversalTimes);
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    /**
//     * 检查是否需要结算, 交易记录满300笔需要结算
//     *
//     * @param context
//     * @return
//     */
//    public static boolean checkIfNeedSettle(android.content.Context context) {
//        TranDBDao tranDBDao = new TranDBDao(context);
//        if (tranDBDao.getTotalRecordNum() > 300) {
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    /**
//     * 若存在冲正记录,返回那条需要冲正的记录的信息
//     *
//     * @param context
//     * @return
//     */
//    public static TranDBReversal getExistedReversal(android.content.Context context) {
//        TranDBReversalDao tranDBDao = new TranDBReversalDao(context);
//        TranDBReversal tranDB = tranDBDao.findByMaxId();
//        if (tranDB != null && tranDB.isNeedReversal == 0) {
//            return tranDB;
//        } else {
//            return null;
//        }
//    }
//
//    /**
//     * 查询是否需要下载第03块参数
//     *
//     * @param context
//     * @return
//     */
//    public static boolean checkIfNeedDown03(android.content.Context context) {
//        return PackUtil.checkIfNeedDownAID(context);
//    }
//
//    /**
//     * 查询是否需要下载第04块参数
//     *
//     * @param context
//     * @return
//     */
//    public static boolean checkIfNeedDown04(android.content.Context context) {
//        return PackUtil.checkIfNeedDownCAPK(context);
//    }
//
//    /**
//     * 生成请求报文
//     *
//     * @param posData  输入参数
//     * @param packData 输出报文
//     * @return 见packData返回值定义
//     */
//    public static int packData(android.content.Context context, com.telpo.pospay.main.data.POSData posData, com.telpo.pospay.main.data.PackData packData) {
//        int ret = 0;
//        com.telpo.pospay.main.data.GlobalParams.gContext = context;
//
//        TranDBReversal tranDBReversal = new TranDBReversal();
//        TranDBReversalDao tranDBReversalDao = new TranDBReversalDao(context);
//
//        //检查输入参数是否合法、是否缺少什么
//        ret = PackUtil.checkPOSData(posData, context);
//        if (ret != OK_SUCCESS) {
//            return ret;
//        }
//
//        //检查上一笔交易是否需要冲正
////        if (posData.transType != TYPE_REVERSAL && posData.transType != TYPE_PRE_AUTHORIZE_REVERSAL && posData.transType != TYPE_PRE_AUTHORIZE_END_REVERSAL
////        && posData.transType !=  TYPE_DOWN_MASTERKEY   ) {
////            tranDBReversal = tranDBReversalDao.findByMaxId();
////            if (tranDBReversal != null && tranDBReversal.isNeedReversal == 0) {
////                return ERR_NEED_REVERSAL;
////            }
////        }
//
//        switch (posData.transType) {
//            case TYPE_CONSUME://请求消费
//            case TYPE_CONSUME_VOID://请求消费撤销
//            case TYPE_SETTLE://请求结算
//            case TYPE_CONSUME_REFUND://消费退货
//            case TYPE_PRE_AUTHORIZE://预授权
//            case TYPE_PRE_AUTHORIZE_VOID://预授权撤销
//            case TYPE_PRE_AUTHORIZE_FINISH://预授权完成
//            case TYPE_PRE_AUTHORIZE_FINISH_VOID://预授权完成撤销
//            case TYPE_BALANCE://余额查询
//            case TYPE_WECHAT_CODE_PAY://微信二维码支付(被扫模式)
//            case TYPE_WECHAT_CODE_PAY_VOID://微信二维码支付撤销(被扫模式)
//            case TYPE_WECHAT_CODE_PAY_QUERY://微信二维码支付结果查询(被扫模式)
//            case TYPE_WECHAT_PREORDER_PAY://微信预下单生成二维码(主扫模式)
//            case TYPE_WECHAT_PREORDER_PAY_QUERY://微信请求下单交易查询(主扫模式)
//            case TYPE_WECHAT_PREORDER_PAY_VOID://微信请求下单交易撤销(主扫模式)
//            case TYPE_ALIPAY_CODE_PAY://支付宝二维码支付(被扫模式)
//            case TYPE_ALIPAY_CODE_PAY_VOID://支付宝二维码支付撤销(被扫模式)
//            case TYPE_ALIPAY_CODE_PAY_QUERY://支付宝二维码支付结果查询(被扫模式)
//            case TYPE_ALIPAY_PREORDER_PAY://支付宝预下单生成二维码(主扫模式)
//            case TYPE_ALIPAY_PREORDER_PAY_QUERY://支付宝请求下单交易查询(主扫模式)
//            case TYPE_ALIPAY_PREORDER_PAY_VOID://支付宝请求下单交易撤销(主扫模式)
//            case TYPE_SCAN_REFUND://支付宝微信扫码退款
//                tranDBReversal = tranDBReversalDao.findByMaxId();
//                if (tranDBReversal != null && tranDBReversal.isNeedReversal == 0) {
//                    return ERR_NEED_REVERSAL;
//                }
//                break;
//            default:
//
//        }
//
//        //设置批次号流水号
//        if (posData.transType != TYPE_ELEC_SIGN) {
//            posData.orderNo = com.telpo.pospay.main.data.GlobalParams.get_transNo(context);
//        }
//        posData.batchNo = com.telpo.pospay.main.data.GlobalParams.get_BatchNo(context);
//        posData.operaterID = com.telpo.pospay.main.data.GlobalParams.get_operatorId(context);
//
//
//        //设置53域
//        posData.field53 = "2600000000000000";
//
//        switch (posData.transType) {
//            case TYPE_DOWN_MASTERKEY://请求下载主密钥
//                ret = PackUtil.packMessage_DownMasterKey(context, posData, packData);
//                if (ret != OK_SUCCESS) {
//                    return ret;
//                }
//                break;
//
//            case TYPE_DOWN_PARAMETER_BLACKLIST://请求下载黑名单
//                ret = PackUtil.packMessage_DownParaBlankList(context, posData, packData);
//                if (ret != OK_SUCCESS) {
//                    return ret;
//                }
//                break;
//            case TYPE_DOWN_PARAMETER_TERMINAL://终端参数下载
//                ret = PackUtil.packMessage_DownTerminalPara(context, posData, packData);
//                if (ret != OK_SUCCESS) {
//                    return ret;
//                }
//                break;
//            case TYPE_DOWN_PARAMETER_NFC_BLACKLIST://请求下载NFC黑名单
//                ret = PackUtil.packMessage_DownParaNFCBlankList(context, posData, packData);
//                if (ret != OK_SUCCESS) {
//                    return ret;
//                }
//                break;
//            case TYPE_DOWN_PARAMETER_NFC_WHITELIST://请求下载白名单
//                ret = PackUtil.packMessage_DownParaNFCWHITEList(context, posData, packData);
//                if (ret != OK_SUCCESS) {
//                    return ret;
//                }
//                break;
//            case TYPE_DOWN_PARAMETER_NFC://请求下载非接参数
//                ret = PackUtil.packMessage_DownParaNFC(context, posData, packData);
//                if (ret != OK_SUCCESS) {
//                    return ret;
//                }
//                break;
//            case TYPE_DOWN_PARAMETER_AID://请求下载第(AID)
//                ret = PackUtil.packMessage_DownParaAID(context, posData, packData);
//                if (ret != OK_SUCCESS) {
//                    return ret;
//                }
//                break;
////
//            case TYPE_DOWN_PARAMETER_CAPK://请求下载(CAPK公钥)
//                ret = PackUtil.packMessage_DownParaCAPK(context, posData, packData);
//                if (ret != OK_SUCCESS) {
//                    return ret;
//                }
//                break;
//
//            case TYPE_QUERY_AID_VERSION://请求查询AID参数版本
//                ret = PackUtil.packMessage_QureyAIDParaVer(context, posData, packData);
//                if (ret != OK_SUCCESS) {
//                    return ret;
//                }
//                break;
//
//            case TYPE_DOWN_PARAMETER_NFC_FINISHED://请求查询AID参数版本
//                posData.transType = TYPE_DOWN_PARAMETER_NFC;
//                ret = PackUtil.packMessage_DownParaEnd(context, posData, packData);
//                if (ret != OK_SUCCESS) {
//                    return ret;
//                }
//                break;
//            case TYPE_DOWN_PARAMETER_NFC_BLACKLIST_FINISHED://
//                posData.transType = TYPE_DOWN_PARAMETER_NFC_BLACKLIST;
//                ret = PackUtil.packMessage_DownParaEnd(context, posData, packData);
//                if (ret != OK_SUCCESS) {
//                    return ret;
//                }
//                break;
//            case TYPE_DOWN_PARAMETER_BLACKLIST_FINISHED://
//                posData.transType = TYPE_DOWN_PARAMETER_BLACKLIST;
//                ret = PackUtil.packMessage_DownParaEnd(context, posData, packData);
//                if (ret != OK_SUCCESS) {
//                    return ret;
//                }
//                break;
//            case TYPE_DOWN_PARAMETER_NFC_WHITELIST_FINISHED://请求查询AID参数版本
//                posData.transType = TYPE_DOWN_PARAMETER_NFC_WHITELIST;
//                ret = PackUtil.packMessage_DownParaEnd(context, posData, packData);
//                if (ret != OK_SUCCESS) {
//                    return ret;
//                }
//                break;
//            case TYPE_DOWN_PARAMETER_AID_FINISHED://请求查询AID参数版本
//                posData.transType = TYPE_DOWN_PARAMETER_AID;
//                ret = PackUtil.packMessage_DownParaEnd(context, posData, packData);
//                if (ret != OK_SUCCESS) {
//                    return ret;
//                }
//                break;
//            case TYPE_DOWN_PARAMETER_CAPK_FINISHED://请求查询AID参数版本
//                posData.transType = TYPE_DOWN_PARAMETER_CAPK;
//                ret = PackUtil.packMessage_DownParaEnd(context, posData, packData);
//                if (ret != OK_SUCCESS) {
//                    return ret;
//                }
//                break;
//            case TYPE_QUERY_CAPK_VERSION://请求查询CAPK参数版本
//                ret = PackUtil.packMessage_QureyCAPKParaVer(context, posData, packData);
//                if (ret != OK_SUCCESS) {
//                    return ret;
//                }
//                break;
//            case TYPE_POS_STATE_UPLOAD://POS状态上送
//                ret = PackUtil.packMessage_Pos_State_Upload(context, posData, packData);
//                if (ret != OK_SUCCESS) {
//                    return ret;
//                }
//                break;
//
//            case TYPE_SIGN_IN://请求签到
//                ret = PackUtil.packMessage_SignIn(context, posData, packData);
//                if (ret != OK_SUCCESS) {
//                    return ret;
//                }
//                break;
//
//            case TYPE_SIGN_OUT://请求签退
//                ret = PackUtil.packMessage_SignOut(context, posData, packData);
//                if (ret != OK_SUCCESS) {
//                    return ret;
//                }
//                break;
//
//            case TYPE_CONSUME://请求消费
//                ret = PackUtil.packMessage_Consume(context, posData, packData);
//                if (ret != OK_SUCCESS) {
//                    return ret;
//                }
//                break;
//
//            case TYPE_CONSUME_VOID://请求消费撤销
//                ret = PackUtil.packMessage_ConsumeVoid(context, posData, packData);
//                if (ret != OK_SUCCESS) {
//                    return ret;
//                }
//                break;
//
//            case TYPE_REVERSAL://请求冲正
//                ret = PackUtil.packMessage_Reversal(context, posData, packData);
//                if (ret != OK_SUCCESS) {
//                    return ret;
//                }
//                break;
//
//            case TYPE_SETTLE://请求结算
//                ret = PackUtil.packMessage_Settle(context, posData, packData);
//                if (ret != OK_SUCCESS) {
//                    return ret;
//                }
//                break;
//
//            case TYPE_CONSUME_REFUND://消费退货
//                ret = PackUtil.packMessage_ConsumeRefund(context, posData, packData);
//                if (ret != OK_SUCCESS) {
//                    return ret;
//                }
//
//                break;
//
//            case TYPE_PRE_AUTHORIZE://预授权
//                ret = PackUtil.packMessage_preAuth(context, posData, packData);
//                if (ret != OK_SUCCESS) {
//                    return ret;
//                }
//                break;
//            case TYPE_PRE_AUTHORIZE_REVERSAL://预授权冲正
//                ret = PackUtil.packMessage_PreAuth_Reversal(context, posData, packData);
//                if (ret != OK_SUCCESS) {
//                    return ret;
//                }
//                break;
//
//            case TYPE_PRE_AUTHORIZE_VOID://预授权撤销
//                ret = PackUtil.packMessage_preAuthVoid(context, posData, packData);
//                if (ret != OK_SUCCESS) {
//                    return ret;
//                }
//                break;
//
//            case TYPE_PRE_AUTHORIZE_FINISH://预授权完成
//                ret = PackUtil.packMessage_preAuthEnd(context, posData, packData);
//                if (ret != OK_SUCCESS) {
//                    return ret;
//                }
//                break;
//            case TYPE_ELEC_SIGN://电子话凭条报文
//                ret = PackUtil.packMessage_ElectSign(context, posData, packData);
//                if (ret != OK_SUCCESS) {
//                    return ret;
//                }
//                break;
//            case TYPE_PRE_AUTHORIZE_END_REVERSAL://预授权完成冲正
//                ret = PackUtil.packMessage_preAuthEnd_Reversal(context, posData, packData);
//                if (ret != OK_SUCCESS) {
//                    return ret;
//                }
//                break;
//
//            case TYPE_PRE_AUTHORIZE_FINISH_VOID://预授权完成撤销
//                ret = PackUtil.packMessage_preAuthEndVoid(context, posData, packData);
//                if (ret != OK_SUCCESS) {
//                    return ret;
//                }
//                break;
//
//            case TYPE_BALANCE://余额查询
//                ret = PackUtil.packMessage_balance(context, posData, packData);
//                if (ret != OK_SUCCESS) {
//                    return ret;
//                }
//                break;
//            case TYPE_WECHAT_CODE_PAY://微信二维码支付(被扫模式)
//                ret = PackUtil.packWechatCodePayMessage(context, posData, packData);
//                if (ret != OK_SUCCESS) {
//                    return ret;
//                }
//                break;
//            case TYPE_WECHAT_CODE_PAY_VOID://微信二维码支付撤销(被扫模式)
//                ret = PackUtil.packWechatCodePayVoidMessage(context, posData, packData);
//                if (ret != OK_SUCCESS) {
//                    return ret;
//                }
//                break;
//            case TYPE_WECHAT_CODE_PAY_QUERY://微信二维码支付结果查询(被扫模式)
//                ret = PackUtil.packWechatCodePayQueryMessage(context, posData, packData);
//                if (ret != OK_SUCCESS) {
//                    return ret;
//                }
//                break;
//            case TYPE_WECHAT_PREORDER_PAY://微信预下单生成二维码(主扫模式)
//                ret = PackUtil.packWechatPreOrderMessage(context, posData, packData);
//                if (ret != OK_SUCCESS) {
//                    return ret;
//                }
//                break;
//            case TYPE_WECHAT_PREORDER_PAY_QUERY://微信请求下单交易查询(主扫模式)
//                ret = PackUtil.packWechatPreOrderQueryMessage(context, posData, packData);
//                if (ret != OK_SUCCESS) {
//                    return ret;
//                }
//                break;
//            case TYPE_WECHAT_PREORDER_PAY_VOID://微信请求下单交易撤销(主扫模式)
//                ret = PackUtil.packWechatPreOrderVoidMessage(context, posData, packData);
//                if (ret != OK_SUCCESS) {
//                    return ret;
//                }
//                break;
//
//            case TYPE_ALIPAY_CODE_PAY://支付宝二维码支付(被扫模式)
//                ret = PackUtil.packAlipayCodePayMessage(context, posData, packData);
//                if (ret != OK_SUCCESS) {
//                    return ret;
//                }
//                break;
//            case TYPE_ALIPAY_CODE_PAY_VOID://支付宝二维码支付撤销(被扫模式)
//                ret = PackUtil.packAlipayCodePayVoidMessage(context, posData, packData);
//                if (ret != OK_SUCCESS) {
//                    return ret;
//                }
//                break;
//            case TYPE_ALIPAY_CODE_PAY_QUERY://支付宝二维码支付结果查询(被扫模式)
//                ret = PackUtil.packAlipayCodePayQueryMessage(context, posData, packData);
//                if (ret != OK_SUCCESS) {
//                    return ret;
//                }
//                break;
//            case TYPE_ALIPAY_PREORDER_PAY://支付宝预下单生成二维码(主扫模式)
//                ret = PackUtil.packAlipayPreOrderMessage(context, posData, packData);
//                if (ret != OK_SUCCESS) {
//                    return ret;
//                }
//                break;
//            case TYPE_ALIPAY_PREORDER_PAY_QUERY://支付宝请求下单交易查询(主扫模式)
//                ret = PackUtil.packAlipayPreOrderQueryMessage(context, posData, packData);
//                if (ret != OK_SUCCESS) {
//                    return ret;
//                }
//                break;
//            case TYPE_ALIPAY_PREORDER_PAY_VOID://支付宝请求下单交易撤销(主扫模式)
//                ret = PackUtil.packAlipayPreOrderVoidMessage(context, posData, packData);
//                if (ret != OK_SUCCESS) {
//                    return ret;
//                }
//                break;
//            case TYPE_SCAN_REFUND://支付宝微信扫码退款
//                ret = PackUtil.packScanRefundMessage(context, posData, packData);
//                if (ret != OK_SUCCESS) {
//                    return ret;
//                }
//                break;
//            default:
//                return ERR_TRANSTYPE_UNKNOW;
//        }
//
//        switch (posData.transType) {
//            case TYPE_CONSUME://请求消费
//            case TYPE_CONSUME_VOID://请求消费撤销
//            case TYPE_REVERSAL://请求冲正
////            case TYPE_SETTLE://请求结算
//            case TYPE_CONSUME_REFUND://消费退货
//            case TYPE_PRE_AUTHORIZE://预授权
//            case TYPE_PRE_AUTHORIZE_REVERSAL://预授权冲正
//            case TYPE_PRE_AUTHORIZE_VOID://预授权撤销
//            case TYPE_PRE_AUTHORIZE_FINISH://预授权完成
//            case TYPE_PRE_AUTHORIZE_END_REVERSAL://预授权完成冲正
//            case TYPE_PRE_AUTHORIZE_FINISH_VOID://预授权完成撤销
//            case TYPE_BALANCE://余额查询
//            case TYPE_WECHAT_CODE_PAY://微信二维码支付(被扫模式)
//            case TYPE_WECHAT_CODE_PAY_VOID://微信二维码支付撤销(被扫模式)
//            case TYPE_WECHAT_CODE_PAY_QUERY://微信二维码支付结果查询(被扫模式)
//            case TYPE_WECHAT_PREORDER_PAY://微信预下单生成二维码(主扫模式)
//            case TYPE_WECHAT_PREORDER_PAY_QUERY://微信预下单生成二维码查询(主扫模式)
//            case TYPE_WECHAT_PREORDER_PAY_VOID: //微信请求下单交易撤销(主扫模式)
//            case TYPE_ALIPAY_CODE_PAY://支付宝二维码支付(被扫模式)
//            case TYPE_ALIPAY_CODE_PAY_VOID://支付宝二维码支付撤销(被扫模式)
//            case TYPE_ALIPAY_CODE_PAY_QUERY://支付宝二维码支付结果查询(被扫模式)
//            case TYPE_ALIPAY_PREORDER_PAY://支付宝预下单生成二维码(主扫模式)
//            case TYPE_ALIPAY_PREORDER_PAY_QUERY://支付宝预下单生成二维码查询(主扫模式)
//            case TYPE_ALIPAY_PREORDER_PAY_VOID: //支付宝请求下单交易撤销(主扫模式)
//            case TYPE_SCAN_REFUND://支付宝微信扫码退款
//                TPPOSOpen(context);
//                String strDataSend = com.telpo.emv.util.StringUtil.bytesToHexString(packData.data_send);
//                String strEncrypdata = strDataSend.substring(36, strDataSend.length());
//                String strHead = strDataSend.substring(4, 35) + "1";
//                int nencrypData = strEncrypdata.length() / 2;
//                byte[] encryptData = new byte[((nencrypData % 8) == 0) ? nencrypData : (nencrypData + 8 - (nencrypData % 8))];
//                byte[] decryptData = new byte[((nencrypData % 8) == 0) ? nencrypData : (nencrypData + 8 - (nencrypData % 8))];
//                com.telpo.base.util.MLog.i("报文加密       原始报文: " + strDataSend);
//                com.telpo.base.util.MLog.i("报文加密             头: " + strHead);
//                com.telpo.base.util.MLog.i("报文加密   要加密的数据: " + strEncrypdata);
//                com.telpo.base.util.MLog.i("报文加密   加密前的长度: " + nencrypData);
//                com.telpo.base.util.MLog.i("报文加密   加密后的长度: " + encryptData.length);
//                com.telpo.pinpad.PinpadService.TP_DesByKeyIndex(com.telpo.pospay.main.data.GlobalParams.currMacKeyIndex, com.telpo.emv.util.StringUtil.hexStringToByte(strEncrypdata), encryptData, com.telpo.pinpad.PinpadService.PIN_DES_ENCRYPT);
//                com.telpo.base.util.MLog.i("报文加密   加密后的数据: " + com.telpo.emv.util.StringUtil.bytesToHexString(encryptData));
//                com.telpo.pinpad.PinpadService.TP_DesByKeyIndex(com.telpo.pospay.main.data.GlobalParams.currMacKeyIndex, encryptData, decryptData, com.telpo.pinpad.PinpadService.PIN_DES_DECRYPT);
//                com.telpo.base.util.MLog.i("报文还原       还原报文: " + com.telpo.emv.util.StringUtil.bytesToHexString(decryptData));
//                strDataSend = strHead + com.telpo.emv.util.StringUtil.bytesToHexString(encryptData);
//                strDataSend = strFormat(String.format("%X", strDataSend.length() / 2).trim(), 4) + strDataSend;
//                com.telpo.base.util.MLog.i("报文加密       最终报文: " + strDataSend);
//
//                packData.data_send = com.telpo.emv.util.StringUtil.hexStringToByte(strDataSend);
//                com.telpo.base.util.MLog.i("【packData】交易类型: " + posData.transType + "  报文: " + com.telpo.emv.util.StringUtil.bytesToHexString(packData.data_send));
//
//                break;
//
//            default:
//                break;
//
//        }
//
//        return OK_SUCCESS;
//    }
//
//    /**
//     * 解析返回的报文
//     * 报文数据不带长度，记得去掉2个字节的长度
//     *
//     * @param context
//     * @param posData
//     * @param packData
//     * @return
//     */
//    public static int unPackData(android.content.Context context, com.telpo.pospay.main.data.POSData posData, com.telpo.pospay.main.data.PackData packData) {
//        int ret = 0;
//        boolean mBHaveLength;
//        if (packData.data_receive == null || packData.data_receive.length < 2) {
//            return ERR_NO_RECEIVE_DATA;
//        }
//
//        android.content.SharedPreferences sp = context.getSharedPreferences(com.telpo.pospay.main.data.GlobalParams.TPParaPreferences, android.content.Context.MODE_PRIVATE);
//        mBHaveLength = sp.getBoolean(com.telpo.pospay.main.data.GlobalParams.key_bReceiveDataHaveLength, true);
//        com.telpo.base.util.MLog.i("bReceiveDataHaveLength: " + mBHaveLength);
//        com.telpo.base.util.MLog.i("bReceiveData:" + com.telpo.emv.util.DataProcessUtil.bytesToHexString(packData.data_receive));
//        if (mBHaveLength) {
//            //去掉报文前2个字节(报文长度)
//            byte[] dataLen = com.telpo.emv.util.ByteArrayUtil.byteArrayGetHead(packData.data_receive, 2);
//            int iDataLen = ((dataLen[0] & 0xff) << 8) | (dataLen[1] & 0xff);
//            com.telpo.base.util.MLog.i("unPackData Length : " + iDataLen);
//
//            packData.data_receive = com.telpo.emv.util.ByteArrayUtil.byteArrayTrimHead(packData.data_receive, 2);
//            if (packData.data_receive.length != iDataLen) {
//                return ERR_RECEIVE_DATA_LENGTH;
//            }
//        }
//
//        com.telpo.base.util.MLog.i("【unPackData】交易类型: " + posData.transType + "\n报文: " + com.telpo.emv.util.StringUtil.bytesToHexString(packData.data_receive));
//
//        switch (posData.transType) {
//            case TYPE_CONSUME://请求消费
//            case TYPE_CONSUME_VOID://请求消费撤销
//            case TYPE_REVERSAL://请求冲正
////            case TYPE_SETTLE://请求结算
//            case TYPE_CONSUME_REFUND://消费退货
//            case TYPE_PRE_AUTHORIZE://预授权
//            case TYPE_PRE_AUTHORIZE_REVERSAL://预授权冲正
//            case TYPE_PRE_AUTHORIZE_VOID://预授权撤销
//            case TYPE_PRE_AUTHORIZE_FINISH://预授权完成
//            case TYPE_PRE_AUTHORIZE_END_REVERSAL://预授权完成冲正
//            case TYPE_PRE_AUTHORIZE_FINISH_VOID://预授权完成撤销
//            case TYPE_BALANCE://余额查询
//            case TYPE_WECHAT_CODE_PAY://微信二维码支付(被扫模式)
//            case TYPE_WECHAT_CODE_PAY_VOID://微信二维码支付撤销(被扫模式)
//            case TYPE_WECHAT_CODE_PAY_QUERY://微信二维码支付结果查询(被扫模式)
//            case TYPE_WECHAT_PREORDER_PAY://微信预下单生成二维码(主扫模式)
//            case TYPE_WECHAT_PREORDER_PAY_QUERY://微信预下单生成二维码查询(主扫模式)
//            case TYPE_WECHAT_PREORDER_PAY_VOID: //微信请求下单交易撤销(主扫模式)
//            case TYPE_ALIPAY_CODE_PAY://支付宝二维码支付(被扫模式)
//            case TYPE_ALIPAY_CODE_PAY_VOID://支付宝二维码支付撤销(被扫模式)
//            case TYPE_ALIPAY_CODE_PAY_QUERY://支付宝二维码支付结果查询(被扫模式)
//            case TYPE_ALIPAY_PREORDER_PAY://支付宝预下单生成二维码(主扫模式)
//            case TYPE_ALIPAY_PREORDER_PAY_QUERY://支付宝预下单生成二维码查询(主扫模式)
//            case TYPE_ALIPAY_PREORDER_PAY_VOID: //支付宝请求下单交易撤销(主扫模式)
//            case TYPE_SCAN_REFUND://支付宝微信扫码退款
//
//                String strDataSend = com.telpo.emv.util.StringUtil.bytesToHexString(packData.data_receive);
//                String strEncrypdata = strDataSend.substring(32, strDataSend.length());
//                String strHead = strDataSend.substring(0, 32);
//                int nencrypData = strEncrypdata.length() / 2;
//                byte[] encryptData = new byte[((nencrypData % 8) == 0) ? nencrypData : (nencrypData + 8 - (nencrypData % 8))];
//                com.telpo.base.util.MLog.i("报文解密       原始报文: " + strDataSend);
//                com.telpo.base.util.MLog.i("报文解密             头: " + strHead);
//                com.telpo.base.util.MLog.i("报文解密   要解密的数据: " + strEncrypdata);
//                com.telpo.base.util.MLog.i("报文解密   解密前的长度: " + nencrypData);
//                com.telpo.base.util.MLog.i("报文解密   解密后的长度: " + encryptData.length);
//                com.telpo.pinpad.PinpadService.TP_DesByKeyIndex(com.telpo.pospay.main.data.GlobalParams.currMacKeyIndex, com.telpo.emv.util.StringUtil.hexStringToByte(strEncrypdata), encryptData, com.telpo.pinpad.PinpadService.PIN_DES_ENCRYPT);
//                com.telpo.base.util.MLog.i("报文解密   解密后的数据: " + com.telpo.emv.util.StringUtil.bytesToHexString(encryptData));
//                strDataSend = strHead + com.telpo.emv.util.StringUtil.bytesToHexString(encryptData);
//                com.telpo.base.util.MLog.i("报文解密       最终报文: " + strDataSend);
//                packData.data_receive = com.telpo.emv.util.StringUtil.hexStringToByte(strDataSend);
//
//
//                break;
//
//            default:
//
//        }
//
//
//        switch (posData.transType) {
//            case TYPE_DOWN_MASTERKEY://请求下载主密钥
//                ret = UnPackUtil.unpackDownMasterKey(context, posData, packData);
//                if (ret != OK_SUCCESS) {
//                    return ret;
//                }
//                break;
//            case TYPE_DOWN_PARAMETER_BLACKLIST://请求下载黑名单
//                ret = UnPackUtil.unpackMessage_DawnParaBlackList(context, posData, packData);
//                if (ret != OK_SUCCESS) {
//                    return ret;
//                }
//                break;
//            case TYPE_DOWN_PARAMETER_TERMINAL://终端参数下载
//                ret = UnPackUtil.unpackMessage_DownTerminalPara(context, posData, packData);
//                if (ret != OK_SUCCESS) {
//                    return ret;
//                }
//                break;
//
//            case TYPE_DOWN_PARAMETER_NFC://请求下载NFC
//                ret = UnPackUtil.unpackMessage_DownParaNFC(context, posData, packData);
//                if (ret != OK_SUCCESS) {
//                    return ret;
//                }
//                break;
//            case TYPE_DOWN_PARAMETER_NFC_BLACKLIST://请求下载NFC
//                ret = UnPackUtil.unpackMessage_DawnParaNFCBlackList(context, posData, packData);
//                if (ret != OK_SUCCESS) {
//                    return ret;
//                }
//                break;
//            case TYPE_DOWN_PARAMETER_NFC_WHITELIST://请求下载NFC
//                ret = UnPackUtil.unpackMessage_DawnParaNFCWhiteList(context, posData, packData);
//                if (ret != OK_SUCCESS) {
//                    return ret;
//                }
//                break;
//
//            case TYPE_DOWN_PARAMETER_AID://请求下载第03块参数(AID)
//                ret = UnPackUtil.unpackDownAIDMessage(context, posData, packData);
//                if (ret != OK_SUCCESS) {
//                    return ret;
//                }
//                break;
//
//            case TYPE_DOWN_PARAMETER_CAPK://请求下载第04块参数(CAPK公钥)
//                ret = UnPackUtil.unpackDownParaCAPKMessage(context, posData, packData);
//                if (ret != OK_SUCCESS) {
//                    return ret;
//                }
//                break;
////            case TYPE_DOWN_PARAMETER_FINISHED://请求下载第04块参数(CAPK公钥)
////                ret = UnPackUtil.unpackDownPara(context, posData, packData);
////                if (ret != OK_SUCCESS) {
////                    return ret;
////                }
////                break;
//            case TYPE_QUERY_AID_VERSION://请求查询AID参数版本
//                ret = UnPackUtil.unpackMessage_QureyAIDParaVer(context, posData, packData);
//                if (ret != OK_SUCCESS) {
//                    return ret;
//                }
//                break;
//
//            case TYPE_QUERY_CAPK_VERSION://请求查询CAPK参数版本
//                ret = UnPackUtil.unpackMessage_QureyCAPKParaVer(context, posData, packData);
//                if (ret != OK_SUCCESS) {
//                    return ret;
//                }
//                break;
//            case TYPE_POS_STATE_UPLOAD:
//                ret = UnPackUtil.unpackMessage_PosStateUpload(context, posData, packData);
//                if (ret != OK_SUCCESS) {
//                    return ret;
//                }
//                break;
//
//            case TYPE_SIGN_IN://签到
//                ret = UnPackUtil.unpackSignInMessage(context, posData, packData);
//                if (ret != OK_SUCCESS) {
//                    return ret;
//                }
//                break;
//
//            case TYPE_SIGN_OUT://签退
//                ret = UnPackUtil.unpackSignOutMessage(context, posData, packData);
//                if (ret != OK_SUCCESS) {
//                    return ret;
//                }
//                break;
//
//            case TYPE_CONSUME://消费
//                ret = UnPackUtil.unpackConsumeMessage(context, posData, packData);
//                if (ret != OK_SUCCESS) {
//                    return ret;
//                }
//                break;
//
//            case TYPE_CONSUME_VOID://消费撤销
//                ret = UnPackUtil.unpackConsumeVoidMessage(context, posData, packData);
//                if (ret != OK_SUCCESS) {
//                    return ret;
//                }
//                break;
//
//            case TYPE_CONSUME_REFUND://消费退货
//                ret = UnPackUtil.unpackConsumeRefundMessage(context, posData, packData);
//                if (ret != OK_SUCCESS) {
//                    return ret;
//                }
//                break;
//
//            case TYPE_REVERSAL://冲正
//                ret = UnPackUtil.unpackReversalMessage(context, posData, packData);
//                if (ret != OK_SUCCESS) {
//                    return ret;
//                }
//                break;
//
//            case TYPE_SETTLE://结算
//                ret = UnPackUtil.unpackSettleMessage(context, posData, packData);
//                if (ret != OK_SUCCESS) {
//                    return ret;
//                }
//                break;
//
//            case TYPE_PRE_AUTHORIZE://预授权
//                ret = UnPackUtil.unpackPreAuthMessage(context, posData, packData);
//                if (ret != OK_SUCCESS) {
//                    return ret;
//                }
//                break;
//
//            case TYPE_PRE_AUTHORIZE_VOID://预授权撤销
//                ret = UnPackUtil.unpackPreAuthVoidMessage(context, posData, packData);
//                if (ret != OK_SUCCESS) {
//                    return ret;
//                }
//                break;
//
//            case TYPE_PRE_AUTHORIZE_FINISH://预授权完成
//                ret = UnPackUtil.unpackPreAuthEndMessage(context, posData, packData);
//                if (ret != OK_SUCCESS) {
//                    return ret;
//                }
//                break;
//
//            case TYPE_PRE_AUTHORIZE_FINISH_VOID://预授权完成撤销
//                ret = UnPackUtil.unpackPreAuthEndVoidMessage(context, posData, packData);
//                if (ret != OK_SUCCESS) {
//                    return ret;
//                }
//                break;
//
//            case TYPE_BALANCE://余额查询
//                ret = UnPackUtil.unpackBalanceMessage(context, posData, packData);
//                if (ret != OK_SUCCESS) {
//                    return ret;
//                }
//                break;
//            case TYPE_PRE_AUTHORIZE_REVERSAL://预授权冲正
//                ret = UnPackUtil.unpackPreAuthReversalMessage(context, posData, packData);
//                if (ret != OK_SUCCESS) {
//                    return ret;
//                }
//                break;
//            case TYPE_PRE_AUTHORIZE_END_REVERSAL://预授权完成冲正
//                ret = UnPackUtil.unpackPreAuthEndReversalMessage(context, posData, packData);
//                if (ret != OK_SUCCESS) {
//                    return ret;
//                }
//                break;
//            case TYPE_ELEC_SIGN://电子话凭条报文
//                ret = UnPackUtil.unpackMessage_ElectSign(context, posData, packData);
//                if (ret != OK_SUCCESS) {
//                    return ret;
//                }
//                break;
//            case TYPE_WECHAT_CODE_PAY://微信二维码支付(被扫模式)
//                ret = UnPackUtil.unpackWechatCodePayMessage(context, posData, packData);
//                if (ret != OK_SUCCESS) {
//                    return ret;
//                }
//                break;
//            case TYPE_WECHAT_CODE_PAY_VOID://微信二维码支付撤销(被扫模式)
//                ret = UnPackUtil.unpackWechatCodePayVoidMessage(context, posData, packData);
//                if (ret != OK_SUCCESS) {
//                    return ret;
//                }
//                break;
//
//            case TYPE_WECHAT_CODE_PAY_QUERY://微信二维码支付结果查询(被扫模式)
//                ret = UnPackUtil.unpackWechatCodePayQueryMessage(context, posData, packData);
//                if (ret != OK_SUCCESS) {
//                    return ret;
//                }
//                break;
//            case TYPE_WECHAT_PREORDER_PAY://微信预下单生成二维码(主扫模式)
//                ret = UnPackUtil.unpackWechatPreOrderMessage(context, posData, packData);
//                if (ret != OK_SUCCESS) {
//                    return ret;
//                }
//                break;
//            case TYPE_WECHAT_PREORDER_PAY_QUERY://微信请求下单交易查询(主扫模式)
//                ret = UnPackUtil.unpackWechatPreOrderQueryMessage(context, posData, packData);
//                if (ret != OK_SUCCESS) {
//                    return ret;
//                }
//                break;
//            case TYPE_WECHAT_PREORDER_PAY_VOID://微信请求下单交易撤销(主扫模式)
//                ret = UnPackUtil.unpackWechatPreOrderVoidMessage(context, posData, packData);
//                if (ret != OK_SUCCESS) {
//                    return ret;
//                }
//                break;
//
//            case TYPE_ALIPAY_CODE_PAY://支付宝二维码支付(被扫模式)
//                ret = UnPackUtil.unpackAlipayCodePayMessage(context, posData, packData);
//                if (ret != OK_SUCCESS) {
//                    return ret;
//                }
//                break;
//            case TYPE_ALIPAY_CODE_PAY_VOID://支付宝二维码支付撤销(被扫模式)
//                ret = UnPackUtil.unpackAlipayCodePayVoidMessage(context, posData, packData);
//                if (ret != OK_SUCCESS) {
//                    return ret;
//                }
//                break;
//
//            case TYPE_ALIPAY_CODE_PAY_QUERY://支付宝二维码支付结果查询(被扫模式)
//                ret = UnPackUtil.unpackAlipayCodePayQueryMessage(context, posData, packData);
//                if (ret != OK_SUCCESS) {
//                    return ret;
//                }
//                break;
//            case TYPE_ALIPAY_PREORDER_PAY://支付宝预下单生成二维码(主扫模式)
//                ret = UnPackUtil.unpackAlipayPreOrderMessage(context, posData, packData);
//                if (ret != OK_SUCCESS) {
//                    return ret;
//                }
//                break;
//            case TYPE_ALIPAY_PREORDER_PAY_QUERY://支付宝请求下单交易查询(主扫模式)
//                ret = UnPackUtil.unpackAlipayPreOrderQueryMessage(context, posData, packData);
//                if (ret != OK_SUCCESS) {
//                    return ret;
//                }
//                break;
//            case TYPE_ALIPAY_PREORDER_PAY_VOID://支付宝请求下单交易撤销(主扫模式)
//                ret = UnPackUtil.unpackAlipayPreOrderVoidMessage(context, posData, packData);
//                if (ret != OK_SUCCESS) {
//                    return ret;
//                }
//                break;
//            case TYPE_SCAN_REFUND://支付宝微信扫码退款
//                ret = UnPackUtil.unpackScanRefundMessage(context, posData, packData);
//                if (ret != OK_SUCCESS) {
//                    return ret;
//                }
//                break;
//            default:
//                return ERR_TRANSTYPE_UNKNOW;
//        }
//        String log = "本次交易信息:";
//        log += "\n交易类型: " + posData.transType;
//        log += "\n读卡类型: " + posData.readCardType;
//        log += "\n卡类型  : " + packData.cardProperty;
//        log += "\n商户号  : " + posData.merchantID;
//        log += "\n终端号  : " + posData.terminalID;
//        log += "\n卡号    : " + posData.sPAN;
//        log += "\n卡有效期: " + posData.sEXPDate;
//        log += "\n交易金额: " + posData.transAmount;
//        log += "\n时间    : " + packData.time;
//        log += "\n日期    : " + packData.date;
//        log += "\n清算日期: " + packData.clean_date;
//        log += "\n流水号  : " + packData.orderNo;
//        log += "\n批次号  : " + packData.batchNo;
//        log += "\n参考号  : " + packData.reference;
//        log += "\n授权码  : " + packData.authorizeCode;
//        log += "\n发卡行  : " + packData.issuerName;
//        log += "\n响应码  : " + packData.responseCode;
//        if (posData.readCardType == com.telpo.pospay.main.util.TPPOSUtil.MODE_INSER_IC
//                || posData.readCardType == com.telpo.pospay.main.util.TPPOSUtil.MODE_NFC) {
//            log += "\nIAD     : " + posData.sEMV_IAD;
//            log += "\nARQC    : " + posData.sEMV_ARQC;
//            log += "\nUNPR_NO : " + posData.sEMV_UNPR_NO;
//            log += "\nATC     : " + posData.sEMV_ATC;
//            log += "\nAIP     : " + posData.sEMV_AIP;
//            log += "\nCID     : " + posData.sEMV_CID;
//            log += "\nTSI     : " + posData.sEMV_TSI;
//            log += "\nTVR     : " + posData.sEMV_TVR;
//            log += "\nAID     : " + posData.sEMV_AID;
//            log += "\nAPPLLAB : " + posData.sEMV_APPLAB;
//        }
//        com.telpo.base.util.MLog.i(log);
//        if (!android.text.TextUtils.isEmpty(packData.reference)) {
//            posData.TransactionCharacteristicCode = (android.text.TextUtils.isEmpty(packData.clean_date) ? "0000" : packData.clean_date) + packData.reference;
//            com.telpo.base.util.MLog.i("交易特征码1：" + posData.TransactionCharacteristicCode);
//            byte bt1[] = com.telpo.emv.util.StringUtil.hexStringToByte(posData.TransactionCharacteristicCode.substring(0, 8));
//            byte bt2[] = com.telpo.emv.util.StringUtil.hexStringToByte(posData.TransactionCharacteristicCode.substring(8, 16));
//            byte bt[] = new byte[4];
//            for (int i = 0; i < 4; i++) {
//                bt[i] = (byte) (bt1[i] ^ bt2[i]);
//            }
//            posData.TransactionCharacteristicCode = com.telpo.emv.util.StringUtil.bytesToHexString(bt);
//            com.telpo.base.util.MLog.i("交易特征码2：" + posData.TransactionCharacteristicCode);
//        }
//        //组交易特征码
//
////        //组电子凭条报文
//
//
////        if (posData.transType == TYPE_CONSUME || posData.transType == TYPE_CONSUME_VOID || posData.transType == TYPE_CONSUME_REFUND
////                || posData.transType == TYPE_PRE_AUTHORIZE || posData.transType == TYPE_PRE_AUTHORIZE_VOID || posData.transType == TYPE_PRE_AUTHORIZE_FINISH
////                || posData.transType == TYPE_PRE_AUTHORIZE_FINISH_VOID){
////            if((!TextUtils.isEmpty(packData.responseCode))&&packData.responseCode.equals("00")){
////                if (!posData.bAllowNoSign){
////                   //组电子凭条报文
////                   TranElectronicSlipDB tranElectronicSlipDB = new TranElectronicSlipDB();
////                   tranElectronicSlipDB.field_02 = packData.tranDB.field_02;
////                   tranElectronicSlipDB.field_04 = packData.tranDB.field_04;
////                   tranElectronicSlipDB.field_11 = packData.tranDB.field_11;
////                   tranElectronicSlipDB.field_15 = packData.clean_date;         //清算日期
////                   tranElectronicSlipDB.field_37 = packData.tranDB.field_37;
////                   tranElectronicSlipDB.field_39 = "";
////                   tranElectronicSlipDB.field_41 = packData.tranDB.field_41;
////                   tranElectronicSlipDB.field_42 = packData.tranDB.field_42;
////                   tranElectronicSlipDB.field_55 = "";
////                   TLVData tag = new TLVData();
////                   tag.Tag = 0xFF00;                //商户名称
////                    try {
////                        tag.Value = TPPOSUtil.getMerchantName(context).getBytes("GBK");
////                    } catch (UnsupportedEncodingException e) {
////                        e.printStackTrace();
////                    }
////                    tranElectronicSlipDB.field_55 += TLVData.generateTLVHexString(tag);
////
////                   tag = new TLVData();
////                   tag.Tag = 0xFF01;                //交易类型
////                   switch (posData.transType) {
////                       case TPPOSUtil.TYPE_CONSUME:
////                           try {
////                               tag.Value = "消费".getBytes("GBK");
////                           } catch (UnsupportedEncodingException e) {
////                               e.printStackTrace();
////                           }
////                           break;
////                       case TPPOSUtil.TYPE_CONSUME_VOID:
////                           try {
////                               tag.Value = "消费撤销".getBytes("GBK");
////                           } catch (UnsupportedEncodingException e) {
////                               e.printStackTrace();
////                           }
////                           break;
////                       case TPPOSUtil.TYPE_CONSUME_REFUND:
////                           try {
////                               tag.Value = "退货".getBytes("GBK");
////                           } catch (UnsupportedEncodingException e) {
////                               e.printStackTrace();
////                           }
////                           break;
////                       case TPPOSUtil.TYPE_PRE_AUTHORIZE:
////                           try {
////                               tag.Value = "预授权".getBytes("GBK");
////                           } catch (UnsupportedEncodingException e) {
////                               e.printStackTrace();
////                           }
////                           break;
////                       case TPPOSUtil.TYPE_PRE_AUTHORIZE_FINISH:
////                           try {
////                               tag.Value = "预授权完成".getBytes("GBK");
////                           } catch (UnsupportedEncodingException e) {
////                               e.printStackTrace();
////                           }
////                           break;
////                       case TPPOSUtil.TYPE_PRE_AUTHORIZE_VOID:
////                           try {
////                               tag.Value = "预授权撤销".getBytes("GBK");
////                           } catch (UnsupportedEncodingException e) {
////                               e.printStackTrace();
////                           }
////                           break;
////                       case TPPOSUtil.TYPE_PRE_AUTHORIZE_FINISH_VOID:
////                           try {
////                               tag.Value = "预授权撤销".getBytes("GBK");
////                           } catch (UnsupportedEncodingException e) {
////                               e.printStackTrace();
////                           }
////                           break;
////                       default:
////                           tag = null;
////                   }
////                   tranElectronicSlipDB.field_55 += TLVData.generateTLVHexString(tag);
////
////                   tag = new TLVData();
////                   tag.Tag = 0xFF02;                        //操作员号
////                   tag.Value = TPPOSUtil.getOperator(context).getBytes();
////                   tranElectronicSlipDB.field_55 += TLVData.generateTLVHexString(tag);
////                   tag = new TLVData();
////                   tag.Tag = 0xFF03;                        //收单机构
////                   if (!TextUtils.isEmpty(packData.tranDB.field_44)){
////                       String strs[] = packData.tranDB.field_44.split("\\s+");
////                       if (strs != null && strs.length>1){
////                           tag.Value = strs[1].getBytes();
////                           tranElectronicSlipDB.field_55 += TLVData.generateTLVHexString(tag);
////                       }
////                   }
////
////                   tag = new TLVData();
////                   tag.Tag = 0xFF04;                    //发卡机构
////                   if (!TextUtils.isEmpty(packData.tranDB.field_44)){
////                       String strs[] = packData.tranDB.field_44.split("\\s+");
////                       if (strs != null && strs.length>1){
////                           tag.Value = strs[0].getBytes();
////                           tranElectronicSlipDB.field_55 += TLVData.generateTLVHexString(tag);
////                       }
////                   }
////                   tag = new TLVData();
////                   tag.Tag = 0xFF05;                //有效期
////                   tag.Value = (packData.tranDB.field_14!=null)?packData.tranDB.field_14.getBytes():null;
////                   tranElectronicSlipDB.field_55 += TLVData.generateTLVHexString(tag);
////                   tag = new TLVData();
////                   Calendar date = Calendar.getInstance();
////                   String year = String.valueOf(date.get(Calendar.YEAR));
////                   tag.Tag = 0xFF06;                //日期时间
////                   tag.Value = (year + packData.tranDB.field_13+packData.tranDB.field_12).getBytes();
////                   tranElectronicSlipDB.field_55 += TLVData.generateTLVHexString(tag);
////
////                   tag = new TLVData();
////                   tag.Tag = 0xFF07;                //授权码
////                   tag.Value = (packData.tranDB.field_38!=null)?(packData.tranDB.field_38.getBytes()):null;
////                   tranElectronicSlipDB.field_55 += TLVData.generateTLVHexString(tag);
////                   tag = new TLVData();
////                   tag.Tag = 0xFF09;                //交易币种
////                   tag.Value = packData.tranDB.field_49.getBytes();
////                   tranElectronicSlipDB.field_55 += TLVData.generateTLVHexString(tag);
////
////                   tag = new TLVData();
////                   tag.Tag = 0xFF30;                //应用标签
////                   tag.Value = (posData.sEMV_APPLAB!=null)?posData.sEMV_APPLAB.getBytes():null;
////                   tranElectronicSlipDB.field_55 += TLVData.generateTLVHexString(tag);
////
////                   tag = new TLVData();
////                   tag.Tag = 0xFF31;                //应用标识
////                   tag.Value = (posData.sEMV_AID!=null)?posData.sEMV_AID.getBytes():null;
////                   tranElectronicSlipDB.field_55 += TLVData.generateTLVHexString(tag);
////                   tag = new TLVData();
////                   tag.Tag = 0xFF23;                //应用密文
////                   tag.Value = (posData.sEMV_ARQC!=null)?posData.sEMV_ARQC.getBytes():null;
////                   tranElectronicSlipDB.field_55 += TLVData.generateTLVHexString(tag);
////
////                   //原交易信息
////                   if ((posData.transType == TPPOSUtil.TYPE_CONSUME_VOID)||(posData.transType == TPPOSUtil.TYPE_PRE_AUTHORIZE_VOID)||(posData.transType == TPPOSUtil.TYPE_PRE_AUTHORIZE_FINISH_VOID) ){
////                       tag = new TLVData();
////                       tag.Tag = 0xFF60;      //原凭证号   撤销类交易和电子现金退货交易时出现
////                       tag.Value = packData.tranDB.field_612.getBytes();
////                       tranElectronicSlipDB.field_55 += TLVData.generateTLVHexString(tag);
////                   }
////                   if (posData.transType == TPPOSUtil.TYPE_CONSUME_REFUND){
////                       tag = new TLVData();
////                       tag.Tag = 0xFF62;    //原参考号    退货类（不含电子现金退货）交易出现
////                       tag.Value = packData.tranDB.field_37.getBytes();
////                       tranElectronicSlipDB.field_55 += TLVData.generateTLVHexString(tag);
////
////                       tag = new TLVData();
////                       tag.Tag = 0xFF63;    //原交易日期 退货类交易（包括电子现金退货）出现
////                       tag.Value = packData.tranDB.field_613.getBytes();
////                       tranElectronicSlipDB.field_55 += TLVData.generateTLVHexString(tag);
////                   }
////
////                   if ((posData.transType == TPPOSUtil.TYPE_PRE_AUTHORIZE_FINISH) || (posData.transType == TPPOSUtil.TYPE_PRE_AUTHORIZE_FINISH_VOID)
////                           || (posData.transType == TPPOSUtil.TYPE_PRE_AUTHORIZE_VOID)){
////                       tag = new TLVData();
////                       tag.Tag = 0xFF64;
////                       tag.Value = (packData.tranDB.field_38!=null)?(packData.tranDB.field_38.getBytes()):null;
////                       tranElectronicSlipDB.field_55 += TLVData.generateTLVHexString(tag);
////                   }
////                   tag = new TLVData();
////                   tag.Tag = 0xFF70;    //当前交易打印张数
////                   tag.Value = getReceiptNo(context).getBytes();
////                   tranElectronicSlipDB.field_55 += TLVData.generateTLVHexString(tag);
////                   tranElectronicSlipDB.field_60 = "1";
////                   tranElectronicSlipDB.field_601 = "07";
////                   tranElectronicSlipDB.field_602 = packData.batchNo;
////                   tranElectronicSlipDB.field_603 = "800";
////                   tranElectronicSlipDB.field_604 = "";
////                   tranElectronicSlipDB.field_62 = "";
////                   tranElectronicSlipDB.field_64 = "1";
////                   tranElectronicSlipDB.isNeedReversal = 0;
////                   tranElectronicSlipDB.reversalTimes = 0;
////                   TranElectronicSlipDBDao tranElectronicSlipDBDao = new TranElectronicSlipDBDao(context);
////                   tranElectronicSlipDBDao.deleteList(tranElectronicSlipDBDao.list());
////                   com.telpo.base.util.MLog.i("tranElectronicSlipDBDao"+tranElectronicSlipDBDao.create(tranElectronicSlipDB));
////               }
////            }
////        }
//        return OK_SUCCESS;
//    }
//
//    /**
//     * 获取当前批次的所有交易记录
//     *
//     * @param context
//     * @return
//     */
//    public static java.util.List<TranDB> getTradeRecordToday(android.content.Context context) {
//        TranDBDao tranDBDao = new TranDBDao(context);
//        return tranDBDao.list();
//    }
//
//    /**
//     * 获取存放在tran_table_history表中所有交易记录
//     */
//    public static java.util.List<TranDBHistory> getTradeRecordHistory(android.content.Context context) {
//        TranDBHistoryDao tranDBHistoryDao = new TranDBHistoryDao(context);
//        return tranDBHistoryDao.list();
//    }
//
//    /**
//     * 获取今天的所有冲正交易记录
//     *
//     * @param context
//     * @return
//     */
//    public static java.util.List<TranDBReversal> getReversalRecordToday(android.content.Context context) {
//        TranDBReversalDao tranDBDao = new TranDBReversalDao(context);
//        return tranDBDao.list();
//    }
//
//    public static java.util.List<TranDB> getTradeRecordByDate(String date, android.content.Context context) {
//        TranDBDao tranDBDao = new TranDBDao(context);
//        return tranDBDao.findByDate(date);
//    }
//
//    public static java.util.List<TranDBHistory> getTradeHistoryRecordByDate(String date, android.content.Context context) {
//        TranDBHistoryDao tranDBHistoryDao = new TranDBHistoryDao(context);
//        return tranDBHistoryDao.findByDate(date);
//    }
//
//    /**
//     * 根据流水号查找交易记录
//     *
//     * @param no      流水号
//     * @param context
//     * @return
//     */
//    public static TranDB getTradeRecordByNo(String no, android.content.Context context) {
//        TranDBDao tranDBDao = new TranDBDao(context);
//        return tranDBDao.findByTranNo(strFormat(no));
//    }
//
//    /**
//     * 根据流水号和批号查找交易记录
//     *
//     * @param traceno 流水号
//     * @param batchno 批次号
//     * @param context
//     * @return
//     */
//    public static TranDB getTradeRecordByTranNoAndBatchNo(String traceno, String batchno, android.content.Context context) {
//        TranDBDao tranDBDao = new TranDBDao(context);
//        return tranDBDao.findByTranNoAndBatchNo(strFormat(traceno), strFormat(batchno));
//
//    }
//
//    public static java.util.List<TranDB> findTranDBAll(android.content.Context context) {
//        TranDBDao tranDBDao = new TranDBDao(context);
//        java.util.List<TranDB> list = tranDBDao.list();
//        return list;
//    }
//
//    //查找全部历史交易记录
//    public static java.util.List<TranDBHistory> findTranDBHistoryAll(android.content.Context context) {
//        TranDBHistoryDao tranDBHistoryDao = new TranDBHistoryDao(context);
//        java.util.List<TranDBHistory> list = tranDBHistoryDao.list();
//        return list;
//    }
//
//    public static void updateTrandDB(android.content.Context context, TranDB tranDB) {
//        TranDBDao tranDBDao = new TranDBDao(context);
//        tranDBDao.update(tranDB);
//    }
//
//    public static void moveTrandDB2TradeHistoryRecord(android.content.Context context) {
//        TranDBDao tranDBDao = new TranDBDao(context);
//        TranDBHistoryDao tranDBHistoryDao = new TranDBHistoryDao(context);
//        java.util.List<TranDB> dbList = tranDBDao.list();
//        if (dbList != null) {
//            for (TranDB db : dbList) {
//                TranDBHistory tranDBHistory = new TranDBHistory();
//                TranDBDao.copyToHistoryDB(db, tranDBHistory);
//                tranDBHistoryDao.create(tranDBHistory);
//                tranDBDao.delete(db);
//            }
//        }
//    }
//
//    public static TranDBHistory getTradeHistoryRecordByTranNoAndBatchNo(String traceno, String batchno, android.content.Context context) {
//        TranDBHistoryDao tranDBHistoryDao = new TranDBHistoryDao(context);
//        return tranDBHistoryDao.findByTranNoAndBatchNo(strFormat(traceno), strFormat(batchno));
//    }
//
//    public static void updateTrandHistoryDB(android.content.Context context, TranDBHistory tranDBHistory) {
//        TranDBHistoryDao tranDBHistoryDao = new TranDBHistoryDao(context);
//        tranDBHistoryDao.update(tranDBHistory);
//    }
//
//    /**
//     * 找到某个交易类型的最后一笔记录(主要用于微信、支付宝的查询)
//     *
//     * @param context
//     * @param tranType
//     */
//    public static TranDB findMaxTranDBbyTranType(android.content.Context context, int tranType) {
//        TranDBDao tranDBDao = new TranDBDao(context);
//        return tranDBDao.findByTranTypeMaxId(tranType);
//    }
//
//    /**
//     * 找到微信交易类型的最后一笔记录(主要用于微信、支付宝的查询)
//     *
//     * @param context
//     */
//    public static TranDB findByWechatorAliPayMaxId(android.content.Context context, boolean isWechat) {
//        TranDBDao tranDBDao = new TranDBDao(context);
//        TranDB tranDB = tranDBDao.findByWechatorAliPayMaxId(isWechat);
//        if (tranDB == null) {
//            TranDBHistoryDao tranDBHistoryDao = new TranDBHistoryDao(context);
//            TranDBHistory tranDBHistory = tranDBHistoryDao.findByWechatorAliPayMaxId(isWechat);
//            if (tranDBHistory != null) {
//                return tranDBHistoryDao.copyToTranDB(tranDBHistory);
//            } else {
//                return null;
//            }
//        }
//        return tranDB;
//    }
//
//    /**
//     * 找到微信交易类型的最后一笔记录(主要用于微信、支付宝的查询)
//     *
//     * @param context
//     */
//    public static void deleteTranDB(android.content.Context context) {
//        TranDBDao tranDBDao = new TranDBDao(context);
//        TranDBHistoryDao tranDBHistoryDao = new TranDBHistoryDao(context);
//        java.util.List<TranDB> dbList = tranDBDao.list();
//        if (dbList != null) {
//            for (TranDB db : dbList) {
//                TranDBHistory tranDBHistory = new TranDBHistory();
//                TranDBDao.copyToHistoryDB(db, tranDBHistory);
//                tranDBHistoryDao.create(tranDBHistory);
//                tranDBDao.delete(db);
//            }
//        }
//    }
//
//    /**
//     * 找到最近那个需要冲正的记录,修改冲正标志位
//     *
//     * @param context
//     */
//    public static void cleanReversal(android.content.Context context) {
//
//        TranDBReversalDao tranDBDao2 = new TranDBReversalDao(context);
//        TranDBReversal tranDB2 = tranDBDao2.findByMaxId();
//        if (tranDB2 != null) {
//            tranDB2.isNeedReversal = 3;
//            tranDBDao2.update(tranDB2);
//        }
//    }

    /**
     * 获取当前流水号
     *
     * @param context
     * @return
     */
    public static String getCurrentTransNo(android.content.Context context) {
        return com.telpo.pospay.main.data.GlobalParams.get_TransNoIncrease(context);
    }

    /**
     * 获取当前批次号
     *
     * @param context
     * @return
     */
    public static String getCurrentBatchNo(android.content.Context context) {
        return com.telpo.pospay.main.data.GlobalParams.get_BatchNo(context);
    }

    /**
     * 设置终端号
     *
     * @param ID 终端号
     * @return
     */
    public static int setTerminalID(android.content.Context context, String ID) {
        com.telpo.pospay.main.data.GlobalParams.set_terminalNo(context, ID);
        return OK_SUCCESS;
    }

    /**
     * 设置商户号
     *
     * @param ID 商户号
     * @return
     */
    public static int setMerchantID(android.content.Context context, String ID) {
        com.telpo.pospay.main.data.GlobalParams.set_merchantNo(context, ID);
        return OK_SUCCESS;
    }

    /**
     * @param context
     * @param no      银行卡交易打印联数
     */
    public static void setReceiptNo(android.content.Context context, String no) {
        android.content.SharedPreferences sp = context.getSharedPreferences(com.telpo.pospay.main.data.GlobalParams.TPParaPreferences, android.content.Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = sp.edit();
        int nNo = Integer.parseInt(no);
        if (nNo < 10) {
            editor.putString("ReceiptNo", "0" + nNo);
        } else {
            editor.putString("ReceiptNo", no);
        }

        editor.commit();
    }

    public static String getReceiptNo(android.content.Context context) {
        android.content.SharedPreferences sp = context.getSharedPreferences(com.telpo.pospay.main.data.GlobalParams.TPParaPreferences, android.content.Context.MODE_PRIVATE);
        return sp.getString("ReceiptNo", "02");
    }

    /**
     * 设置终端序列号
     *
     * @param ID 终端序列号
     * @return
     */
    public static int setTerminalSN(android.content.Context context, String ID) {
        com.telpo.pospay.main.data.GlobalParams.set_terminalSN(context, ID);
        return OK_SUCCESS;
    }

    /**
     * 设置商户名称
     *
     * @param name 商户名称
     * @return
     */
    public static int setMerchantName(android.content.Context context, String name) {
        com.telpo.pospay.main.data.GlobalParams.set_merchantName(context, name);
        return OK_SUCCESS;
    }

    public static int setOperator(android.content.Context context, String operatorID) {
        com.telpo.pospay.main.data.GlobalParams.set_operatorId(context, operatorID);
        return OK_SUCCESS;
    }

    public static String getOperator(android.content.Context context) {
        return com.telpo.pospay.main.data.GlobalParams.get_operatorId(context);
    }

    public static int setTranceNo(android.content.Context context, String tranceNo) {
        android.content.SharedPreferences sp = context.getSharedPreferences(com.telpo.pospay.main.data.GlobalParams.TPParaPreferences, android.content.Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = sp.edit();
        tranceNo = String.valueOf(tranceNo);
        tranceNo = strFormat(tranceNo);
        editor.putString(com.telpo.pospay.main.data.GlobalParams.key_currentTranceNo, tranceNo);
        editor.commit();
        try {
            StringUtil.writeFile(com.telpo.pospay.main.data.GlobalParams.TPTraceNoFile, tranceNo.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return OK_SUCCESS;
    }

    /**
     * 获取终端号
     *
     * @param context
     * @return 终端号
     */
    public static String getTerminalID(android.content.Context context) {
        return com.telpo.pospay.main.data.GlobalParams.get_terminalNo(context);
    }

    /**
     * 获取终端序列号
     *
     * @param context
     * @return 终端序列号
     */
    public static String getTerminalSN(android.content.Context context) {
        return com.telpo.pospay.main.data.GlobalParams.get_terminalSN(context);
    }

    /**
     * 获取商户号
     *
     * @param context
     * @return 商户号
     */
    public static String getMerchantID(android.content.Context context) {
        return com.telpo.pospay.main.data.GlobalParams.get_merchantNo(context);
    }

    /**
     * 获取商户名称
     *
     * @param context
     * @return 商户名称
     */
    public static String getMerchantName(android.content.Context context) {
        return com.telpo.pospay.main.data.GlobalParams.get_merchantName(context);
    }

    /**
     * 获取最大冲正次数
     *
     * @param context
     */
    public static String get_MaxReversalTimes(android.content.Context context) {
        return com.telpo.pospay.main.data.GlobalParams.get_MaxReversalTimes(context);
    }

    /**
     * 设置最大冲正次数
     *
     * @param context
     */
    public static void set_MaxReversalTimes(android.content.Context context, String no) {
        com.telpo.pospay.main.data.GlobalParams.set_MaxReversalTimes(context, no);
    }

    /**
     * 设置最大交易笔数
     *
     * @param context
     */
    public static void set_MaxTransTimes(android.content.Context context, int times) {
        android.content.SharedPreferences sp = context.getSharedPreferences(com.telpo.pospay.main.data.GlobalParams.TPParaPreferences, android.content.Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = sp.edit();
        editor.putInt(com.telpo.pospay.main.data.GlobalParams.key_currentMaxTransTimes, times);
        editor.commit();
    }

    /**
     * 获取最大交易笔数
     *
     * @param context
     */
    public static int get_MaxTransTimes(android.content.Context context) {
        android.content.SharedPreferences sp = context.getSharedPreferences(com.telpo.pospay.main.data.GlobalParams.TPParaPreferences, android.content.Context.MODE_PRIVATE);
        return sp.getInt(com.telpo.pospay.main.data.GlobalParams.key_currentMaxTransTimes, 300);


    }

//    public static boolean isPassMaxTransTimes(android.content.Context context) {
//        TranDBDao tranDBDao = new TranDBDao(context);
//        long nTransTimes = tranDBDao.getTotalRecordNum();
//        return (nTransTimes >= com.telpo.pospay.main.util.TPPOSUtil.get_MaxTransTimes(context));
//
//    }

    /**
     * 设置主密钥索引
     *
     * @param context
     * @param index
     */
    public static void setMasterKeyIndex(android.content.Context context, int index) {
        com.telpo.pospay.main.data.GlobalParams.setMasterKeyIndex(context, index);

    }

    /**
     * 获取主密钥索引
     *
     * @param context
     */
    public static int getMasterKeyIndex(android.content.Context context) {
        return com.telpo.pospay.main.data.GlobalParams.getMasterKeyIndex(context);

    }


    /**
     * 设置8583报文的TPDU和报文头
     *
     * @param context
     * @param TPDU    TPDU, 不设置TPDU就传null
     * @param head    报文头, 不设置报文头就传null
     */
    public static int set8583MessageFormat(android.content.Context context, String TPDU, String head) {
        com.telpo.pospay.main.data.GlobalParams.set_8583MessageFormat(context, TPDU, head);
        return OK_SUCCESS;
    }

    /**
     * 获取8583报文的TPDU
     *
     * @param context
     */
    public static String getTPDU(android.content.Context context) {
        return com.telpo.pospay.main.data.GlobalParams.MESSAGE_TPDU;
    }


    /**
     * 获取设备序列号(SN号)
     *
     * @return SN号
     */
    public static String getSN() {
        return android.os.Build.SERIAL.startsWith("00003804") ? android.os.Build.SERIAL : ("00003804" + android.os.Build.SERIAL);
    }

    /**
     * 获取所有操作员信息
     *
     * @return 操作员列表
     */
//    public static java.util.List<OperatorDB> getOperatorList(android.content.Context context) {
//        OperatorDBDao operatorDBDao = new OperatorDBDao(context);
//        return operatorDBDao.list();
//    }
//
//    /**
//     * 校验操作员密码,并记录当前校验时间
//     *
//     * @param ID  操作员号
//     * @param PWD 密码
//     * @return
//     */
//    public static int checkOperator(android.content.Context context, String ID, String PWD) {
//        if (ID == null) {
//            return OPERATOR_NO_EXIST;
//        }
//
//        OperatorDBDao operatorDBDao = new OperatorDBDao(context);
//        if (operatorDBDao == null) {
//            return OPERATOR_DATABASE_ERR;
//        }
//
//        OperatorDB operatorDB = operatorDBDao.findById(ID);
//        if (operatorDB == null) {
//            return OPERATOR_NO_EXIST;
//        }
//
//        if (operatorDB.ID.equals(ID) && operatorDB.OperatorPWD.equals(OperatorDBDao.getMd5Value(PWD))) {
//            java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//            java.util.Date curDate = new java.util.Date(System.currentTimeMillis());//获取当前时间
//            final String str = formatter.format(curDate);
//            operatorDB.loginRecord = str;
//            operatorDBDao.update(operatorDB);
//            return OK_SUCCESS;
//        } else {
//            return OPERATOR_PWD_ERR;
//        }
//    }
//
//    /**
//     * 增加操作员
//     *
//     * @param ID  操作员号,长度为3
//     * @param PWD 密码
//     * @return
//     */
//    public static int addOperator(android.content.Context context, String ID, @NonNull String PWD) {
//        if (ID == null) {
//            return OPERATOR_ID_INVALID;
//        }
//
//        if (PWD == null) {
//            return OPERATOR_PWD_INVALID;
//        }
//
//        try {
//            OperatorDBDao operatorDBDao = new OperatorDBDao(context);
//            OperatorDB operatorDB = operatorDBDao.findById(ID);
//            if (operatorDB != null) {
//                return OPERATOR_IS_EXIST;
//            } else {
//                operatorDB = new OperatorDB(ID, PWD);
//                boolean ret = operatorDBDao.create(operatorDB);
//                if (ret) {
//                    return OK_SUCCESS;
//                } else {
//                    return OPERATOR_ADD_ERR;
//                }
//            }
//        } catch (Exception e) {
//            com.telpo.base.util.MLog.i("Operator operation ERR:\n" + e.toString());
//            return OPERATOR_ADD_ERR;
//        }
//
//    }
//
//    /**
//     * 删除操作员
//     *
//     * @param ID 操作员号,长度为3
//     * @return
//     */
//    public static int deleteOperator(android.content.Context context, String ID) {
//        if (ID == null || ID.equals("99") || ID.equals("000")) {
//            return OPERATOR_ID_INVALID;
//        }
//
//        OperatorDBDao operatorDBDao = new OperatorDBDao(context);
//        OperatorDB operatorDB = operatorDBDao.findById(ID);
//        if (operatorDB == null) {
//            return OPERATOR_NO_EXIST;
//        } else {
//            operatorDBDao.delete(operatorDB);
//        }
//
//        return OK_SUCCESS;
//    }
//
//    /**
//     * 修改操作员密码
//     *
//     * @param ID     操作员号,长度为3
//     * @param newPWD 新操作员密码
//     * @return
//     */
//    public static int modifyOperatorPWD(android.content.Context context, String ID, @NonNull String newPWD) {
//        if (ID == null) {
//            return OPERATOR_ID_INVALID;
//        }
//
//        OperatorDBDao operatorDBDao = new OperatorDBDao(context);
//        OperatorDB operatorDB = operatorDBDao.findById(ID);
//        if (operatorDB == null) {
//            return OPERATOR_NO_EXIST;
//        }
//
//        if (newPWD == null) {
//            return OPERATOR_PWD_INVALID;
//        }
//
//        operatorDB.OperatorPWD = OperatorDBDao.getMd5Value(newPWD);
//        operatorDBDao.update(operatorDB);
//        return OK_SUCCESS;
//    }
//
//    /**
//     * 设置密钥、商户号、终端号(弹出一个界面设)
//     *
//     * @param context
//     */
//    public static void TP_ShowManageSettingUI(android.content.Context context) {
//        android.content.Intent intent = new android.content.Intent(context, AARBocParamterMenuActivity.class);
//        context.startActivity(intent);
//    }
//
//    /**
//     * 下载主密钥(用户调用这个接口来设)
//     *
//     * @param context
//     * @return
//     */
//    public static int downloadMK(android.content.Context context) {
//        com.telpo.base.util.MLog.i("on downloadMK");
//        int i = LoadMkeyFrSerial.LoadMkeyFrSerial(context);
//        return i;
//    }
//
//    /**
//     * 取消下载主密钥(用户调用这个接口来设)
//     */
//    public static void downloadMKCancel() {
//        com.telpo.base.util.MLog.i("on downloadMKCancel");
//        LoadMkeyFrSerial.CancelLoadMkeyFrSerial();
//    }

    /**
     * 获取当前API版本号
     */
    public static String getAPIVersion(android.content.Context context) {
        try {
            android.content.pm.PackageManager manager = context.getPackageManager();
            android.content.pm.PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "1.0";
        }
    }

    /**
     * 获取当前报文头
     */
    public static String getMESSAGE_Head() {
        return com.telpo.pospay.main.data.GlobalParams.MESSAGE_Head;
    }

    /**
     * 解包数据是否含2个字节的长度位
     * 默认是含长度位
     * <p>
     * 去哪儿传过来的PackData.data_receive是含2个字节长度位的
     *
     * @param bHaveLengthBytes true: 包含长度位。在解包时，我会先截掉前2个字节
     *                         false： 不含长度位。 在解包时，直接解包
     */
    public static void setReceiveDataType(android.content.Context context, boolean bHaveLengthBytes) {
        com.telpo.pospay.main.data.GlobalParams.set_bReceiveDataHaveLength(context, bHaveLengthBytes);
    }


    /**
     * 设置MAC(64域)的用法
     * <p>
     * 默认是
     *
     * @param bCheckMAC 接收报文是否校验MAC(64域)
     * @param bUseMAC   发送报文是否包含MAC(64域)
     */
    public static void setMACUsage(android.content.Context context, boolean bCheckMAC, boolean bUseMAC) {
        com.telpo.pospay.main.data.GlobalParams.set_MACUsage(context, bCheckMAC, bUseMAC);
    }

    /**
     * 设置是否强制免密免签(测试用)
     *
     * @param context
     * @param bForcePassPWD
     */
    public static void setForcePassPWD(android.content.Context context, boolean bForcePassPWD) {
        com.telpo.pospay.main.data.GlobalParams.set_userForcePassPWD(context, bForcePassPWD);
    }

//    public static void TestSm4() {
//        Field59Data field59Data = new Field59Data();
//        field59Data.get_TLV_A2(null);
//    }


    public static byte[] com8583SendMessage(android.content.Context context, java.util.HashMap inMap, int msgType) throws Exception {
        return ProtocolUtil.com8583SendMessage(context, inMap, msgType);
    }

    public static java.util.HashMap<String, String> parse8583ReceiveMessage(byte[] data, int msgType)
            throws Exception {
        return ProtocolUtil.parse8583ReceiveMessage(data, msgType);
    }

    public static boolean isTestVersion() {
        return true;
    }

//    /**********************
//     * 设备打开
//     **************************/
//    public static int Poweroff_Suspend() {
//        return com.telpo.emv.EmvService.Poweroff_Suspend();
//    }
//
//    public static int Poweroff_Resume() {
//        return com.telpo.emv.EmvService.Poweroff_Resume();
//    }
//
//    public static int deleteThreeMonthsAgo(android.content.Context context) {
//        TranDBHistoryDao tranDBHistoryDao = new TranDBHistoryDao(context);
//        return tranDBHistoryDao.deleteThreeMonthsAgo();
//    }
//
//    //用于数据库测试，把测试用的数据保存到数据库
//    public static void DBTest_InputData(TranDB tranDB, java.util.HashMap outMap) {
//        TranDBDao tranDBDao = new TranDBDao(mContext);
//        TranDBDao.updateData(outMap, tranDB);
//        tranDBDao.create(tranDB);
//    }
//
//    public static boolean getMasterKeyFlag() {
//        try {
//            String flag = new String(com.telpo.emv.util.StringUtil.readFile(com.telpo.pospay.main.data.GlobalParams.TPmasterKeyFile));
//            return Boolean.valueOf(flag.trim());
//        } catch (java.io.IOException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }

}
