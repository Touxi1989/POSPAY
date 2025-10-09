package com.telpo.pospay.main.data;


import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.telpo.emv.EmvApp;
import com.telpo.emv.EmvCAPK;
import com.telpo.emv.EmvService;
import com.telpo.emv.util.ByteArrayUtil;
import com.telpo.emv.util.StringUtil;
import com.telpo.emv.util.TLVData;
import com.telpo.pinpad.PinpadService;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * Created by yemiekai on 2017/2/18.
 * <p>
 * 目前作为测试用的东西,请找到其引用,正式使用需要删掉:
 * 1.bUserForcePassPWD
 */
public class GlobalParams {



    //全局变量
    public static Field55Data field55Data;
    public static com.telpo.pospay.db.bean.AIDDB currentDownloadingAID;//在下载AID的packData里,从数据库中找到需要下载的AID,赋值到这里,然后unpackData时更新这里
    public static com.telpo.pospay.db.bean.CAPKDB currentDownloadingCAPK;//在下载CAPK的packData里,从数据库中找到需要下载的CAPK,赋值到这里,然后unpackData时更新这里
    public static byte[] PINBlock;
    public static boolean bIsNFCPassPWD;//本次交易是否免密(NFC流程、组报文时使用。每次交易前置为false,根据是否符合免密条件置位)
    public static android.content.Context gContext;
    public static POSData gPosData;
    public static PackData gPackData;

    //全局常量
    public final static String TPParaPreferences = "TP_ParamPreferences";// 终端参数SharedPreferences变量标记
    public final static String TPDataPata = "/mnt/sdcard/pos/";
    public final static String TPTraceNoFile = TPDataPata + "traceno.file";//保存流水号
    public final static String TPmasterKeyFile = TPDataPata + "keyFlag.file";//保存是否成功下载了主密钥

    public static String MESSAGE_Head = "602100819916";//报文头(中国银联标准12个字节)
    public static String MESSAGE_TPDU = "6000030000";//富友TPDU
    public static String MESSAGE_PLAINTEXT_CLEAR = "00";
    //    public static String MESSAGE_Head = "603200320000";//报文头(中国银联标准12个字节)
//    public static String MESSAGE_TPDU = "6000000000";//九江银行TPDU
    public static int currMasterKeyIndex;
    public static int currMasterKeyLeft;  //双倍长主密钥的左半部分
    public static int currMasterKeyRight; //双倍长主密钥的右半部分
    public static int currPinKeyIndex;
    public static int currMacKeyIndex;
    public static int currMacKeyLeft;     //MAC KEY 的左半部分
    public static int currMacKeyRight;    //MAC KEY 的右半部分
    public static int currDesKeyIndex;
    public static int currSingleDESPINKey;    //单DES的PIN密钥索引
    public static int currSingleDESMACKey;    //单DES的MAC密钥索引
    public static boolean bUse3DESWorkKey;    //工作密钥是否为3DES
    public static boolean bUseMAC;      //是否使用MAC(发送报文是否有64域)
    public static boolean bCheckMAC;    //是否校验MAC(接收报文是否校验64域)


    //免密免签相关
    public static boolean bUserForcePassPWD;//用户强制执行免密(测试用)
    public static boolean bAllowPassSign; //是否允许免签
    public static boolean bAllowPassPWD;  //是否允许免密
    public static boolean bUseCardBIN;    //是否使用卡BIN( 贷记卡不用判断卡BIN; TRUE:借记卡要判断卡BIN; FALSE:借记卡不用判断卡BIN)
    public static String passSignLimitAmount; //免签限额(单位分),交易金额小于这个就不用签名
    public static String passPWDLimitAmount;  //免密限额(单位分),交易金额小于这个就不用密码

    public static final int tempKeyIndex = 9;       //临时存放密钥的地方


    public final static String key_paraInit = "TP_ParamHasInit";
    public final static String key_masterKeyIndex = "TP_MasterKeyIndex";   //主密钥索引

    public final static String key_messageTPDU = "TP_messageTPDU"; //TPDU
    public final static String key_messageHead = "TP_messageHead "; //报文头（版本号）
    public final static String key_bReceiveDataHaveLength = "TP_bReceiveDataHaveLength"; //解包时PackData.data_receive是否含2个字节长度位
    public final static String key_currentTranceNo = "TP_currentTranceNo";//当前流水号
    public final static String key_currentBatchNo = "TP_currentBatchNo"; //当前批次号
    public final static String key_currentMerchantNo = "TP_currentMerchantNo";//当前商户号
    public final static String key_currentTerminalSN = "TP_currentTerminalSN";//当前终端序列号
    public final static String key_currentTerminalNo = "TP_currentTerminalNo"; //当前终端号
    public final static String key_currentMerchantName = "TP_currentMerchantName";//当前商户名称
    public final static String key_currentOperatorId = "TP_currentOperatorId";//当前操作员id
    public final static String key_currentMaxReversalTimes = "TP_currentMaxReversalTimes";//当前最大冲正次数
    public final static String key_currentProgramParaVer = "TP_currentProgramParaVer"; //当前程序参数版本号
    public final static String key_bUpdatedBatchNo = "TP_bUpdatedBatchNo"; //是否换批了(支付宝根据这个来换批,并转存交易记录)
    public final static String key_bUse3DESWorkKey = "TP_bUse3DESWorkKey"; //根据签到的时候下发的工作密钥,确定用3DES算法还是单DES算法
    public final static String key_currentMaxTransTimes = "TP_currentMaxTransTimes";//当前最大交易次数

    public final static String key_bCheckMAC = "TP_bCheckMAC"; //接收报文是否校验MAC
    public final static String key_bUserForcePassPWD = "TP_bUserForcePassPWD";//用户强制免密(测试用)

    //--------下载参数块01的内容------↓↓↓
    public final static String key_bUseMAC = "TP_bUseMAC";//终端是否使用MAC标志
    public final static String key_bAllowPOSUse = "TP_bAllowPOSUse";   //是否允许终端使用标志
    public final static String key_bAutoSettle = "TP_bAutoSettle";    //终端自动结账标志
    public final static String key_bBatchUpload = "TP_bBatchUpload";   //批上送标志
    public final static String key_bAllowRefund = "TP_bAllowRefund";   //允许退货标志

    public final static String key_bManualPAN = "TP_bManualPAN";     //手工键入卡号标志
    public final static String key_bMOTO = "TP_bMOTO";          //允许MO/TO交易标志
    public final static String key_bOfflineTrans = "TP_bOfflineTrans";  //离线交易标志
    public final static String key_b3DES = "TP_b3DES";          //使用3DES加密标志
    public final static String key_bAllowBalance = "TP_bAllowBalance";  //允许余额查询交易标志

    public final static String key_bAllowConsume = "TP_bAllowConsume";  //允许消费交易标志
    public final static String key_bAllowAuth = "TP_bAllowAuth";     //允许预授权标志
    public final static String key_bAllowGetCash = "TP_bAllowGetCash";  //允许取现交易标志
    public final static String key_bAllowAdjust = "TP_bAllowAdjust";   //允许调整交易标志
    public final static String key_bAllowTip = "TP_bAllowTip";      //允许小费交易标志

    public final static String key_bAllowInstalments = "TP_bAllowInstalments";// 允许分期付款标志
    public final static String key_bAllowAmass = "TP_bAllowAmass";    //允许积分交易标志
    public final static String key_bAllowAnotherPay = "TP_bAllowAnotherPay"; //允许代付标志
    public final static String key_bAllowAnotherGain = "TP_bAllowAnotherGain";//允许代收标志
    public final static String key_bUseOperator = "TP_bUseOperator";   //终端使用操作员标志

    public final static String key_bAllowChinese = "TP_bAllowChinese";  //允许POS使用汉字标志
    public final static String key_bSupportEMV = "TP_bSupportEMV";    //标志终端是否支持EMV
    public final static String key_bAllowCardholderSelectAID = "TP_bAllowCardholderSelectAID";//是否允许持卡人自己选择应用
    public final static String key_bAllowFallback = "TP_bAllowFallback"; //是否支持FALLBACK
    public final static String key_bAllowDownAPP = "TP_bAllowDownAPP";  //允许终端支持程序下装

    public final static String key_bAllowEC = "TP_bAllowEC";       //允许终端支持PBOC电子现金
    public final static String key_bSpecifyAccountLoad = "TP_bSpecifyAccountLoad";//允许指定账户圈存
    public final static String key_bUnspecifyAccountLoad = "TP_bUnspecifyAccountLoad";//允许非指定账户圈存
    public final static String key_bRegisterLoad = "TP_bRegisterLoad";  //允许补登账户圈存


    public final static String key_isUpdateCapk = "TP_isUpdateCapk";//终端是否已下载IC卡公钥
    public final static String key_isUpdateAID = "TP_isUpdateAID";//终端是否已下载IC卡参数

    public final static String key_bQucikNFC = "TP_bQucikNFC";        //非接快速启用标志,打开了才允许使用免密功能
    public final static String key_bUseBIN_B = "TP_bUseBIN_B";        //是否启用BIN表B,是则根据BIN表B判断该卡是否可以免密免签,否则所有卡片都免密免签
    public final static String key_bCDCVM = "TP_bCDCVM";            //是否将卡片CDCVM执行情况作为免密判断条件
    public final static String key_bPassSign = "TP_bPassSign";         //是否启用免签

    public final static String key_settleType = "TP_settleType";         //结账方式:N-1  0：以主机为主; 1:以终端能为主; 2:不平不结;
    public final static String key_unSendNums = "TP_unSendNums";         //POS中允许保存的未上送交易笔数: N-2 00~99
    //public final static String key_备用;         //N-1
    public final static String key_offlineAmountLimit = "TP_offlineAmountLimit"; //离线交易金额上限 N-12 (离线交易允许的最大金额)
    public final static String key_adjustLimit = "TP_adjustLimit";        //调整交易上限 N-4  值为原交易金额的 百分比, 例如原交易金额为300元,设置的上限值为0200,则调整交易最多可以将金额调整为900元
    public final static String key_tipLimit = "TP_tipLimit";           //小费交易金额最大允许值,值为元交易金额的百分比
    public final static String key_currency1 = "TP_currency1";          //交易币种1, N-3
    public final static String key_currency2 = "TP_currency2";          //交易币种2, N-3
    public final static String key_currency3 = "TP_currency3";          //交易币种3, N-3
    public final static String key_dialPrefix = "TP_dialPrefix";         //电话拨号前缀, AN-4
    public final static String key_defaultPhone = "TP_defaultPhone";       //缺省电话号码, AN-16
    public final static String key_backupDialPrefix = "TP_backupDialPrefix";   //备用电话拨号前缀, AN-4
    public final static String key_backupDefaultPhone = "TP_backupDefaultPhone"; //备用缺省电话号码, AN-16
    public final static String key_printNums = "TP_printNums";          //票据打印份数, N-2
    public final static String key_dividePayID1 = "TP_dividePayID1";       //分期付款计划ID1 AN-4
    public final static String key_dividePayID2 = "TP_dividePayID2";       //分期付款计划ID2 AN-4
    public final static String key_dividePayID3 = "TP_dividePayID3";       //分期付款计划ID3 AN-4
    public final static String key_dividePayID4 = "TP_dividePayID4";       //分期付款计划ID4 AN-4
    public final static String key_dividePayID5 = "TP_dividePayID5";       //分期付款计划ID5 AN-4
    public final static String key_dividePayID6 = "TP_dividePayID6";       //分期付款计划ID6 AN-4
    public final static String key_dividePayID7 = "TP_dividePayID7";       //分期付款计划ID7 AN-4
    public final static String key_dividePayID8 = "TP_dividePayID8";       //分期付款计划ID8 AN-4
    public final static String key_dividePayID9 = "TP_dividePayID9";       //分期付款计划ID9 AN-4
    public final static String key_overTime = "TP_overTime";           //超时时间 N-2
    // public final static String key_merchantName         =   "TP_merchantName";       //商户名称 ANS-50 前25个字节为英文,后20个字节为中文简称
    public final static String key_merchantENName = "TP_merchantENName";     //商户名称(英文) ANS-25
    public final static String key_merchantCNName = "TP_merchantCNName";     //商户名称(中文) ANS-28
    public final static String key_passPWDLimit = "TP_passPasswordLimit";  //免密限额, N-12
    public final static String key_passSignLimit = "TP_passSignLimit";      //免签限额, N-12

    //------------下载参数块01的内容----------↑↑↑


    public final static String key_receivedDF27 = "TP_receivedDF27";   //最近一次接收的DF27的值,用于判断下一次发送"AID应用参数版本查询报文"如何赋值


    public static void systemParamPreferencesInit(android.content.Context context) {
        int i;
        android.content.SharedPreferences sp = context.getSharedPreferences(com.telpo.pospay.main.data.GlobalParams.TPParaPreferences, android.content.Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = sp.edit();

        boolean isInit = sp.getBoolean(com.telpo.pospay.main.data.GlobalParams.key_paraInit, false);
        com.telpo.base.util.MLog.i("Parameter has Initialed?: " + isInit);


        if (!isInit) {//如果没有初始化过
            com.telpo.base.util.MLog.i("APP first Init");

            editor.putInt(com.telpo.pospay.main.data.GlobalParams.key_masterKeyIndex, getMasterKeyIndex(context));
            byte[] traceNoBytes = "".getBytes();
            try {
                traceNoBytes = com.telpo.pospay.main.util.StringUtil.readFile(com.telpo.pospay.main.data.GlobalParams.TPTraceNoFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
            String trannoFile = new String(traceNoBytes);
            editor.putString(com.telpo.pospay.main.data.GlobalParams.key_currentTranceNo, android.text.TextUtils.isEmpty(trannoFile) ? "000001" : trannoFile);
            editor.putString(com.telpo.pospay.main.data.GlobalParams.key_currentBatchNo, "000001");
            editor.putString(com.telpo.pospay.main.data.GlobalParams.key_currentProgramParaVer, "00000000000000");
            editor.putString(com.telpo.pospay.main.data.GlobalParams.key_messageTPDU, "6000120000");
            editor.putString(com.telpo.pospay.main.data.GlobalParams.key_messageHead, "1307");
            editor.putString(com.telpo.pospay.main.data.GlobalParams.key_currentMerchantNo, "123456789012345");
            editor.putString(com.telpo.pospay.main.data.GlobalParams.key_currentTerminalSN, com.telpo.pospay.main.util.TPPOSUtil.getSN());
            editor.putString(com.telpo.pospay.main.data.GlobalParams.key_currentTerminalNo, "12345678");
            editor.putString(com.telpo.pospay.main.data.GlobalParams.key_currentMerchantName, "测试商户");
            editor.putBoolean(com.telpo.pospay.main.data.GlobalParams.key_bReceiveDataHaveLength, true);//解包传进来的响应数据带2个字节byte的长度位
            editor.putBoolean(com.telpo.pospay.main.data.GlobalParams.key_paraInit, true);
//            editor.putBoolean(GlobalParams.key_bUpdatedBatchNo, false);
            editor.putBoolean(com.telpo.pospay.main.data.GlobalParams.key_bUse3DESWorkKey, true);
            editor.putBoolean(com.telpo.pospay.main.data.GlobalParams.key_bUseMAC, true);//默认使用MAC(发送报文含64域)
            editor.putBoolean(com.telpo.pospay.main.data.GlobalParams.key_bCheckMAC, true);//默认校验MAC(接收报文校验64域)


            //免密免签相关
            editor.putBoolean(com.telpo.pospay.main.data.GlobalParams.key_bUserForcePassPWD, false);
            editor.putString(com.telpo.pospay.main.data.GlobalParams.key_passPWDLimit, "1");
            editor.putString(com.telpo.pospay.main.data.GlobalParams.key_passSignLimit, "1");
            editor.putBoolean(com.telpo.pospay.main.data.GlobalParams.key_bQucikNFC, false);
            editor.putBoolean(com.telpo.pospay.main.data.GlobalParams.key_bPassSign, false);
            //初装机需要下载一次IC卡公钥和参数
            editor.putBoolean(com.telpo.pospay.main.data.GlobalParams.key_isUpdateCapk, false);
            editor.putBoolean(com.telpo.pospay.main.data.GlobalParams.key_isUpdateAID, false);

            //OperatorDatabaseInit.OperatorDBInit(context);

            com.telpo.pospay.main.data.GlobalParams.currMasterKeyIndex = getMasterKeyIndex(context);
            com.telpo.pospay.main.data.GlobalParams.currMasterKeyLeft = com.telpo.pospay.main.data.GlobalParams.currMasterKeyIndex + 1;
            com.telpo.pospay.main.data.GlobalParams.currMasterKeyRight = com.telpo.pospay.main.data.GlobalParams.currMasterKeyIndex + 2;
            com.telpo.pospay.main.data.GlobalParams.currPinKeyIndex = com.telpo.pospay.main.data.GlobalParams.currMasterKeyIndex + 3;
            com.telpo.pospay.main.data.GlobalParams.currMacKeyIndex = com.telpo.pospay.main.data.GlobalParams.currMasterKeyIndex + 4;
            com.telpo.pospay.main.data.GlobalParams.currDesKeyIndex = com.telpo.pospay.main.data.GlobalParams.currMasterKeyIndex + 5;
            com.telpo.pospay.main.data.GlobalParams.currMacKeyLeft = com.telpo.pospay.main.data.GlobalParams.currMasterKeyIndex + 6;
            com.telpo.pospay.main.data.GlobalParams.currMacKeyRight = com.telpo.pospay.main.data.GlobalParams.currMasterKeyIndex + 7;
            com.telpo.pospay.main.data.GlobalParams.currSingleDESPINKey = com.telpo.pospay.main.data.GlobalParams.currMasterKeyIndex + 8;
            com.telpo.pospay.main.data.GlobalParams.currSingleDESMACKey = com.telpo.pospay.main.data.GlobalParams.currMasterKeyIndex + 9;
            com.telpo.pospay.main.data.GlobalParams.bUse3DESWorkKey = true;
            com.telpo.pospay.main.data.GlobalParams.bUserForcePassPWD = false;
            com.telpo.pospay.main.data.GlobalParams.bAllowPassSign = false;
            com.telpo.pospay.main.data.GlobalParams.bAllowPassPWD = false;
            com.telpo.pospay.main.data.GlobalParams.bUseCardBIN = false;
            com.telpo.pospay.main.data.GlobalParams.passSignLimitAmount = "1";
            com.telpo.pospay.main.data.GlobalParams.passPWDLimitAmount = "1";

            com.telpo.pospay.main.data.GlobalParams.bUseMAC = true;
            com.telpo.pospay.main.data.GlobalParams.bCheckMAC = true;




//            //测试密钥
//            String sMasterKey = "31313131313131313131313131313131";
//            i = PinpadService.TP_WriteMasterKey(GlobalParams.currMasterKeyIndex, StringUtil.hexStringToByte(sMasterKey), PinpadService.KEY_WRITE_DIRECT);
//            com.telpo.base.util.MLog.i("write Master Key: " + i);
//            i = PinpadService.TP_WriteMasterKey(GlobalParams.currMasterKeyLeft, StringUtil.hexStringToByte(sMasterKey.substring(0, 16)), PinpadService.KEY_WRITE_DIRECT);
//            com.telpo.base.util.MLog.i("write MasterLeft Key: " + i);
//            i = PinpadService.TP_WriteMasterKey(GlobalParams.currMasterKeyRight, StringUtil.hexStringToByte(sMasterKey.substring(16, 32)), PinpadService.KEY_WRITE_DIRECT);
//            com.telpo.base.util.MLog.i("write MasterRight Key: " + i);
//
//            i = PinpadService.TP_WritePinKey(GlobalParams.currPinKeyIndex, StringUtil.hexStringToByte(sMasterKey), PinpadService.KEY_WRITE_DECRYPT, GlobalParams.currMasterKeyIndex);
//            com.telpo.base.util.MLog.i("write PIN Key: " + i);
//
//            i = PinpadService.TP_WriteMacKey(GlobalParams.currMacKeyIndex, StringUtil.hexStringToByte(sMasterKey), PinpadService.KEY_WRITE_DECRYPT, GlobalParams.currMasterKeyIndex);
//            com.telpo.base.util.MLog.i("write TP_WriteMacKey Key: " + i);
//            i = PinpadService.TP_WriteMacKey(GlobalParams.currMacKeyLeft, StringUtil.hexStringToByte(sMasterKey.substring(0, 16)), PinpadService.KEY_WRITE_DECRYPT, GlobalParams.currMasterKeyIndex);
//            com.telpo.base.util.MLog.i("write Mac Left Key: " + i);
//            i = PinpadService.TP_WriteMacKey(GlobalParams.currMacKeyRight, StringUtil.hexStringToByte(sMasterKey.substring(16, 32)), PinpadService.KEY_WRITE_DECRYPT, GlobalParams.currMasterKeyIndex);
//            com.telpo.base.util.MLog.i("write Mac Right Key: " + i);

            editor.commit();
        } else {

            com.telpo.base.util.MLog.i("APP is not first Init");
            com.telpo.pospay.main.data.GlobalParams.currMasterKeyIndex = getMasterKeyIndex(context);
            com.telpo.pospay.main.data.GlobalParams.currMasterKeyLeft = com.telpo.pospay.main.data.GlobalParams.currMasterKeyIndex + 1;
            com.telpo.pospay.main.data.GlobalParams.currMasterKeyRight = com.telpo.pospay.main.data.GlobalParams.currMasterKeyIndex + 2;
            com.telpo.pospay.main.data.GlobalParams.currPinKeyIndex = com.telpo.pospay.main.data.GlobalParams.currMasterKeyIndex + 3;
            com.telpo.pospay.main.data.GlobalParams.currMacKeyIndex = com.telpo.pospay.main.data.GlobalParams.currMasterKeyIndex + 4;
            com.telpo.pospay.main.data.GlobalParams.currDesKeyIndex = com.telpo.pospay.main.data.GlobalParams.currMasterKeyIndex + 5;
            com.telpo.pospay.main.data.GlobalParams.currMacKeyLeft = com.telpo.pospay.main.data.GlobalParams.currMasterKeyIndex + 6;
            com.telpo.pospay.main.data.GlobalParams.currMacKeyRight = com.telpo.pospay.main.data.GlobalParams.currMasterKeyIndex + 7;
            com.telpo.pospay.main.data.GlobalParams.currSingleDESPINKey = com.telpo.pospay.main.data.GlobalParams.currMasterKeyIndex + 8;
            com.telpo.pospay.main.data.GlobalParams.currSingleDESMACKey = com.telpo.pospay.main.data.GlobalParams.currMasterKeyIndex + 9;

//            GlobalParams.MESSAGE_TPDU = sp.getString(GlobalParams.key_messageTPDU, "6000120000");
//            GlobalParams.MESSAGE_Head = sp.getString(GlobalParams.key_messageHead, "6000000000");
            com.telpo.pospay.main.data.GlobalParams.bUse3DESWorkKey = sp.getBoolean(com.telpo.pospay.main.data.GlobalParams.key_bUse3DESWorkKey, true);
            com.telpo.pospay.main.data.GlobalParams.bUseMAC = sp.getBoolean(com.telpo.pospay.main.data.GlobalParams.key_bUseMAC, true);
            com.telpo.pospay.main.data.GlobalParams.bCheckMAC = sp.getBoolean(com.telpo.pospay.main.data.GlobalParams.key_bCheckMAC, true);
            com.telpo.pospay.main.data.GlobalParams.bUserForcePassPWD = sp.getBoolean(com.telpo.pospay.main.data.GlobalParams.key_bUserForcePassPWD, false);
            com.telpo.pospay.main.data.GlobalParams.bAllowPassPWD = sp.getBoolean(com.telpo.pospay.main.data.GlobalParams.key_bQucikNFC, false);
            com.telpo.pospay.main.data.GlobalParams.bAllowPassSign = sp.getBoolean(com.telpo.pospay.main.data.GlobalParams.key_bPassSign, false);
            com.telpo.pospay.main.data.GlobalParams.bUseCardBIN = sp.getBoolean(com.telpo.pospay.main.data.GlobalParams.key_bUseBIN_B, false);
            com.telpo.pospay.main.data.GlobalParams.passSignLimitAmount = sp.getString(com.telpo.pospay.main.data.GlobalParams.key_passSignLimit, "1");
            com.telpo.pospay.main.data.GlobalParams.passPWDLimitAmount = sp.getString(com.telpo.pospay.main.data.GlobalParams.key_passPWDLimit, "1");
        }
    }


    /**
     * 获取当前流水号
     *
     * @param context
     * @return 6位字符串流水号
     */
    public static String get_TransNoIncrease(android.content.Context context) {

        android.content.SharedPreferences sp = context.getSharedPreferences(com.telpo.pospay.main.data.GlobalParams.TPParaPreferences, android.content.Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = sp.edit();
        String transNo = sp.getString(com.telpo.pospay.main.data.GlobalParams.key_currentTranceNo, "000001");

        return transNo;
    }

    /**
     * 获取当前流水号，并加一
     *
     * @param context
     * @return 6位字符串流水号
     */
    public static String get_transNo(android.content.Context context) {
        String TranNo;
        String strTmp1;
        int Tmp;
        android.content.SharedPreferences sp = context.getSharedPreferences(com.telpo.pospay.main.data.GlobalParams.TPParaPreferences, android.content.Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = sp.edit();
        byte[] traceNoBytes = "".getBytes();
        try {
            traceNoBytes = com.telpo.pospay.main.util.StringUtil.readFile(com.telpo.pospay.main.data.GlobalParams.TPTraceNoFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String trannoFile = new String(traceNoBytes);
        TranNo = sp.getString(com.telpo.pospay.main.data.GlobalParams.key_currentTranceNo, android.text.TextUtils.isEmpty(trannoFile) ? "000001" : trannoFile);
        // 加1 保存
        Tmp = Integer.parseInt(TranNo);
        if (Tmp >= 999999) {
            Tmp = 1;
        } else {
            Tmp++;
        }
        strTmp1 = String.valueOf(Tmp);
        strTmp1 = strFormat(strTmp1);
        editor.putString(com.telpo.pospay.main.data.GlobalParams.key_currentTranceNo, strTmp1);
        editor.commit();
        try {
            com.telpo.pospay.main.util.StringUtil.writeFile(com.telpo.pospay.main.data.GlobalParams.TPTraceNoFile, strTmp1.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return TranNo;

    }

    /**
     * 清空当前流水号
     *
     * @param context
     * @return 6位字符串流水号
     */
    public static void cleanTranceNo(android.content.Context context) {
        android.content.SharedPreferences sp = context.getSharedPreferences(com.telpo.pospay.main.data.GlobalParams.TPParaPreferences, android.content.Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = sp.edit();

        editor.putString(com.telpo.pospay.main.data.GlobalParams.key_currentTranceNo, "000001");
        editor.commit();
    }

    /**
     * 获取当前批次号
     *
     * @param context
     * @return 6位字符串批次号
     */
    public static String get_BatchNo(android.content.Context context) {
        String sBatchNo = "";
        android.content.SharedPreferences sp = context.getSharedPreferences(com.telpo.pospay.main.data.GlobalParams.TPParaPreferences, android.content.Context.MODE_PRIVATE);

        sBatchNo = sp.getString(com.telpo.pospay.main.data.GlobalParams.key_currentBatchNo, "000001");

        return strFormat(sBatchNo);
    }

    /**
     * 设置当前批次号
     *
     * @param context
     */
    public static void set_BatchNo(android.content.Context context, String no) {
        String sBatchNo = "";
        android.content.SharedPreferences sp = context.getSharedPreferences(com.telpo.pospay.main.data.GlobalParams.TPParaPreferences, android.content.Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = sp.edit();
        editor.putString(com.telpo.pospay.main.data.GlobalParams.key_currentBatchNo, no);
        editor.commit();

    }

    /**
     * 设置商户号
     *
     * @param context
     */
    public static void set_merchantNo(android.content.Context context, String no) {
        android.content.SharedPreferences sp = context.getSharedPreferences(com.telpo.pospay.main.data.GlobalParams.TPParaPreferences, android.content.Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = sp.edit();
        editor.putString(com.telpo.pospay.main.data.GlobalParams.key_currentMerchantNo, no);
        editor.commit();

    }

    /**
     * 获取商户号
     *
     * @param context
     */
    public static String get_merchantNo(android.content.Context context) {
        android.content.SharedPreferences sp = context.getSharedPreferences(com.telpo.pospay.main.data.GlobalParams.TPParaPreferences, android.content.Context.MODE_PRIVATE);
        return sp.getString(com.telpo.pospay.main.data.GlobalParams.key_currentMerchantNo, "NULL");
    }


    /**
     * 获取最大冲正次数
     *
     * @param context
     */
    public static String get_MaxReversalTimes(android.content.Context context) {
        android.content.SharedPreferences sp = context.getSharedPreferences(com.telpo.pospay.main.data.GlobalParams.TPParaPreferences, android.content.Context.MODE_PRIVATE);
        return sp.getString(com.telpo.pospay.main.data.GlobalParams.key_currentMaxReversalTimes, "3");
    }

    /**
     * 设置最大冲正次数
     *
     * @param context
     */
    public static void set_MaxReversalTimes(android.content.Context context, String no) {
        android.content.SharedPreferences sp = context.getSharedPreferences(com.telpo.pospay.main.data.GlobalParams.TPParaPreferences, android.content.Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = sp.edit();
        editor.putString(com.telpo.pospay.main.data.GlobalParams.key_currentMaxReversalTimes, no);
        editor.commit();
    }

    /**
     * 设置主密钥索引
     *
     * @param context
     * @param index
     */
    public static void setMasterKeyIndex(android.content.Context context, int index) {
        android.content.SharedPreferences sp = context.getSharedPreferences(com.telpo.pospay.main.data.GlobalParams.TPParaPreferences, android.content.Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = sp.edit();
        editor.putInt(com.telpo.pospay.main.data.GlobalParams.key_masterKeyIndex, index);
        editor.commit();
    }

    /**
     * 获取主密钥索引
     *
     * @param context
     * @return
     */
    public static int getMasterKeyIndex(android.content.Context context) {
        android.content.SharedPreferences sp = context.getSharedPreferences(com.telpo.pospay.main.data.GlobalParams.TPParaPreferences, android.content.Context.MODE_PRIVATE);
        return sp.getInt(com.telpo.pospay.main.data.GlobalParams.key_masterKeyIndex, 10);
    }


    /**
     * 设置终端号
     *
     * @param context
     */
    public static void set_terminalNo(android.content.Context context, String no) {
        android.content.SharedPreferences sp = context.getSharedPreferences(com.telpo.pospay.main.data.GlobalParams.TPParaPreferences, android.content.Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = sp.edit();
        editor.putString(com.telpo.pospay.main.data.GlobalParams.key_currentTerminalNo, no);
        editor.commit();

    }

    /**
     * 设置终端序列号
     *
     * @param context
     */
    public static void set_terminalSN(android.content.Context context, String no) {
        android.content.SharedPreferences sp = context.getSharedPreferences(com.telpo.pospay.main.data.GlobalParams.TPParaPreferences, android.content.Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = sp.edit();
        editor.putString(com.telpo.pospay.main.data.GlobalParams.key_currentTerminalSN, no);
        editor.commit();

    }

    /**
     * 获取终端号
     *
     * @param context
     */
    public static String get_terminalNo(android.content.Context context) {
        android.content.SharedPreferences sp = context.getSharedPreferences(com.telpo.pospay.main.data.GlobalParams.TPParaPreferences, android.content.Context.MODE_PRIVATE);
        return sp.getString(com.telpo.pospay.main.data.GlobalParams.key_currentTerminalNo, "00000000");
    }

    /**
     * 获取终端号
     *
     * @param context
     */
    public static String get_terminalSN(android.content.Context context) {
        android.content.SharedPreferences sp = context.getSharedPreferences(com.telpo.pospay.main.data.GlobalParams.TPParaPreferences, android.content.Context.MODE_PRIVATE);
        return sp.getString(com.telpo.pospay.main.data.GlobalParams.key_currentTerminalSN, com.telpo.pospay.main.util.TPPOSUtil.getSN());
    }

    /**
     * 设置商户名称
     *
     * @param context
     */
    public static void set_merchantName(android.content.Context context, String name) {
        android.content.SharedPreferences sp = context.getSharedPreferences(com.telpo.pospay.main.data.GlobalParams.TPParaPreferences, android.content.Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = sp.edit();
        editor.putString(com.telpo.pospay.main.data.GlobalParams.key_currentMerchantName, name);
        editor.commit();

    }

    /**
     * 设置当前操作员ID
     *
     * @param context
     * @param operatorId
     */
    public static void set_operatorId(android.content.Context context, String operatorId) {
        android.content.SharedPreferences sp = context.getSharedPreferences(com.telpo.pospay.main.data.GlobalParams.TPParaPreferences, android.content.Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = sp.edit();
        editor.putString(com.telpo.pospay.main.data.GlobalParams.key_currentOperatorId, operatorId);
        editor.commit();

    }

    /**
     * 获取当前操作员ID
     *
     * @param context
     */
    public static String get_operatorId(android.content.Context context) {
        android.content.SharedPreferences sp = context.getSharedPreferences(com.telpo.pospay.main.data.GlobalParams.TPParaPreferences, android.content.Context.MODE_PRIVATE);
        return sp.getString(com.telpo.pospay.main.data.GlobalParams.key_currentOperatorId, "01");

    }

    /**
     * 获取商户名称
     *
     * @param context
     */
    public static String get_merchantName(android.content.Context context) {
        android.content.SharedPreferences sp = context.getSharedPreferences(com.telpo.pospay.main.data.GlobalParams.TPParaPreferences, android.content.Context.MODE_PRIVATE);
        return sp.getString(com.telpo.pospay.main.data.GlobalParams.key_currentMerchantName, "NULL");
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
    public static void set_bReceiveDataHaveLength(android.content.Context context, boolean bHaveLengthBytes) {
        android.content.SharedPreferences sp = context.getSharedPreferences(com.telpo.pospay.main.data.GlobalParams.TPParaPreferences, android.content.Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(com.telpo.pospay.main.data.GlobalParams.key_bReceiveDataHaveLength, bHaveLengthBytes);
        editor.commit();

    }

    /**
     * 设置8583报文头的TPDU和版本号
     *
     * @param context
     * @param TPDU    TPDU, 不设就传null
     * @param Ver     版本号, 不设就传null
     */
    public static void set_8583MessageFormat(android.content.Context context, String TPDU, String Ver) {
        android.content.SharedPreferences sp = context.getSharedPreferences(com.telpo.pospay.main.data.GlobalParams.TPParaPreferences, android.content.Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = sp.edit();

        if (TPDU != null && TPDU.length() > 0) {
            editor.putString(com.telpo.pospay.main.data.GlobalParams.key_messageTPDU, TPDU);
            com.telpo.pospay.main.data.GlobalParams.MESSAGE_TPDU = TPDU;
        }

        if (Ver != null && Ver.length() > 0) {
            editor.putString(com.telpo.pospay.main.data.GlobalParams.key_messageHead, Ver);
            com.telpo.pospay.main.data.GlobalParams.MESSAGE_Head = Ver;
        }

        editor.commit();
    }

    /**
     * 设置MAC(64域)的用法
     *
     * @param bCheckMAC 接收报文是否校验MAC(64域)
     * @param bUseMAC   发送报文是否包含MAC(64域)
     */
    public static void set_MACUsage(android.content.Context context, boolean bCheckMAC, boolean bUseMAC) {
        android.content.SharedPreferences sp = context.getSharedPreferences(com.telpo.pospay.main.data.GlobalParams.TPParaPreferences, android.content.Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = sp.edit();


        com.telpo.pospay.main.data.GlobalParams.bCheckMAC = bCheckMAC;
        com.telpo.pospay.main.data.GlobalParams.bUseMAC = bUseMAC;

        editor.putBoolean(com.telpo.pospay.main.data.GlobalParams.key_bCheckMAC, bCheckMAC);
        editor.putBoolean(com.telpo.pospay.main.data.GlobalParams.key_bUseMAC, bUseMAC);
        editor.commit();
    }

    /**
     * 设置是否强制免密免签(测试用)
     *
     * @param context
     * @param bForcePassPWD
     */
    public static void set_userForcePassPWD(android.content.Context context, boolean bForcePassPWD) {
        android.content.SharedPreferences sp = context.getSharedPreferences(com.telpo.pospay.main.data.GlobalParams.TPParaPreferences, android.content.Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = sp.edit();

        com.telpo.pospay.main.data.GlobalParams.bUserForcePassPWD = bForcePassPWD;

        editor.putBoolean(com.telpo.pospay.main.data.GlobalParams.key_bUserForcePassPWD, bForcePassPWD);
        editor.commit();

    }

    /**
     * 获取是否已下载IC卡公钥
     *
     * @param context
     * @return
     */
    public static Boolean get_isUpdateCapk(android.content.Context context) {
        Boolean isUpdateCapk = false;
        android.content.SharedPreferences sp = context.getSharedPreferences(com.telpo.pospay.main.data.GlobalParams.TPParaPreferences, android.content.Context.MODE_PRIVATE);

        isUpdateCapk = sp.getBoolean(com.telpo.pospay.main.data.GlobalParams.key_isUpdateCapk, false);

        return isUpdateCapk;
    }

    /**
     * 设置是否已下载IC卡公钥
     *
     * @param context
     */
    public static void set_isUpdateCapk(android.content.Context context, Boolean no) {
        android.content.SharedPreferences sp = context.getSharedPreferences(com.telpo.pospay.main.data.GlobalParams.TPParaPreferences, android.content.Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(com.telpo.pospay.main.data.GlobalParams.key_isUpdateCapk, no);
        editor.commit();
    }

    /**
     * 获取是否已下载IC卡参数
     *
     * @param context
     * @return
     */
    public static Boolean get_isUpdateAID(android.content.Context context) {
        Boolean isUpdateAID = false;
        android.content.SharedPreferences sp = context.getSharedPreferences(com.telpo.pospay.main.data.GlobalParams.TPParaPreferences, android.content.Context.MODE_PRIVATE);

        isUpdateAID = sp.getBoolean(com.telpo.pospay.main.data.GlobalParams.key_isUpdateAID, false);

        return isUpdateAID;
    }

    /**
     * 设置是否已下载IC卡参数
     *
     * @param context
     */
    public static void set_isUpdateAID(android.content.Context context, Boolean no) {
        android.content.SharedPreferences sp = context.getSharedPreferences(com.telpo.pospay.main.data.GlobalParams.TPParaPreferences, android.content.Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(com.telpo.pospay.main.data.GlobalParams.key_isUpdateAID, no);
        editor.commit();
    }

    /**
     * 前补"0",使字符串长度为6
     *
     * @param strTmp
     * @return
     */
    public static String strFormat(String strTmp) {
        int len = strTmp.length();
        switch (len) {
            case 1:
                strTmp = "00000" + strTmp;
                break;
            case 2:
                strTmp = "0000" + strTmp;
                break;
            case 3:
                strTmp = "000" + strTmp;
                break;
            case 4:
                strTmp = "00" + strTmp;
                break;
            case 5:
                strTmp = "0" + strTmp;
                break;
            case 6:
                break;
        }
        return strTmp;
    }

    /**
     * 前补"0",使字符串长度为length
     *
     * @param strTmp
     * @return
     */
    public static String strFormat(String strTmp, int length) {
        String str = strTmp;
        if (str == null) {
            str = "0";
        }
        if (strTmp.length() >= length) {
            return str;
        }

        while (str.length() < length) {
            str = "0" + str;
        }

        return str;
    }

    /**
     * 获取当前程序参数版本号
     *
     * @param context
     * @return "YYYYMMDDHHMMSS"
     */
    public static String get_programParaVer(android.content.Context context) {
        String sVersion = "";
        android.content.SharedPreferences sp = context.getSharedPreferences(com.telpo.pospay.main.data.GlobalParams.TPParaPreferences, android.content.Context.MODE_PRIVATE);

        sVersion = sp.getString(com.telpo.pospay.main.data.GlobalParams.key_currentProgramParaVer, "00000000000000");

        return sVersion;
    }

    /**
     * 设置密钥组, 根据密钥组设置主密钥索引, 并根据主密钥索引设置其他密钥.
     *
     * @param context
     * @param KeyIndexGroup 取值：0、1、2
     */
    public static void systemSetKeyGroup(android.content.Context context, int KeyIndexGroup) {
        android.content.SharedPreferences sp = context.getSharedPreferences(com.telpo.pospay.main.data.GlobalParams.TPParaPreferences, android.content.Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = sp.edit();

        //密钥组分0,1,2三组，分别以主密钥索引0,10,20区别
        if (KeyIndexGroup != 0 && KeyIndexGroup != 1 && KeyIndexGroup != 2) {
            KeyIndexGroup = 0;
        }
        KeyIndexGroup *= 10;

        int index = KeyIndexGroup;
        editor.putInt(com.telpo.pospay.main.data.GlobalParams.key_masterKeyIndex, index);
        editor.commit();

        com.telpo.pospay.main.data.GlobalParams.currMasterKeyIndex = KeyIndexGroup;
        com.telpo.pospay.main.data.GlobalParams.currMasterKeyLeft = com.telpo.pospay.main.data.GlobalParams.currMasterKeyIndex + 1;
        com.telpo.pospay.main.data.GlobalParams.currMasterKeyRight = com.telpo.pospay.main.data.GlobalParams.currMasterKeyIndex + 2;
        com.telpo.pospay.main.data.GlobalParams.currPinKeyIndex = com.telpo.pospay.main.data.GlobalParams.currMasterKeyIndex + 3;
        com.telpo.pospay.main.data.GlobalParams.currMacKeyIndex = com.telpo.pospay.main.data.GlobalParams.currMasterKeyIndex + 4;
        com.telpo.pospay.main.data.GlobalParams.currDesKeyIndex = com.telpo.pospay.main.data.GlobalParams.currMasterKeyIndex + 5;
        com.telpo.pospay.main.data.GlobalParams.currMacKeyLeft = com.telpo.pospay.main.data.GlobalParams.currMasterKeyIndex + 6;
        com.telpo.pospay.main.data.GlobalParams.currMacKeyRight = com.telpo.pospay.main.data.GlobalParams.currMasterKeyIndex + 7;
        com.telpo.pospay.main.data.GlobalParams.currSingleDESPINKey = com.telpo.pospay.main.data.GlobalParams.currMasterKeyIndex + 8;
        com.telpo.pospay.main.data.GlobalParams.currSingleDESMACKey = com.telpo.pospay.main.data.GlobalParams.currMasterKeyIndex + 9;
    }

    /**
     * 设置工作密钥是否为3DES
     *
     * @param b3DES
     * @param context
     */
    public static void setWorkKeyType(boolean b3DES, android.content.Context context) {
        android.content.SharedPreferences sp = context.getSharedPreferences(com.telpo.pospay.main.data.GlobalParams.TPParaPreferences, android.content.Context.MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = sp.edit();

        com.telpo.pospay.main.data.GlobalParams.bUse3DESWorkKey = b3DES;

        if (b3DES) {
            editor.putBoolean(com.telpo.pospay.main.data.GlobalParams.key_bUse3DESWorkKey, true);
        } else {
            editor.putBoolean(com.telpo.pospay.main.data.GlobalParams.key_bUse3DESWorkKey, false);
        }

        editor.commit();
    }

    public static void Add_Default_APP() {
        String name = "";
        int result = 0;
        boolean dbResult = false;

 /*----------------------------------------------------------------------- division line-----------------------------------------------------------------------------------------*/

        com.telpo.emv.EmvApp APP_CUP_01 = new com.telpo.emv.EmvApp();
        name = "China Union Pay";
        try {
            APP_CUP_01.AppName = name.getBytes("ascii");
        } catch (java.io.UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        APP_CUP_01.AID = new byte[]{(byte) 0xA0, (byte) 0x00, (byte) 0x00, (byte) 0x03, (byte) 0x33, (byte) 0x01, (byte) 0x01, (byte) 0x01};
        APP_CUP_01.SelFlag = (byte) 0x00;
        APP_CUP_01.Priority = (byte) 0x00;
        APP_CUP_01.TargetPer = (byte) 0;
        APP_CUP_01.MaxTargetPer = (byte) 0;
        APP_CUP_01.FloorLimitCheck = (byte) 1;
        APP_CUP_01.RandTransSel = (byte) 1;
        APP_CUP_01.VelocityCheck = (byte) 1;
        APP_CUP_01.FloorLimit = new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x50, (byte) 0x00};//9F1B:FloorLimit
        APP_CUP_01.Threshold = new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};
        APP_CUP_01.TACDenial = new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};
        APP_CUP_01.TACOnline = new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};
        APP_CUP_01.TACDefault = new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};
        APP_CUP_01.AcquierId = new byte[]{(byte) 0x01, (byte) 0x23, (byte) 0x45, (byte) 0x67, (byte) 0x89, (byte) 0x10};
        APP_CUP_01.DDOL = new byte[]{(byte) 0x03, (byte) 0x9F, (byte) 0x37, (byte) 0x04};
        APP_CUP_01.TDOL = new byte[]{(byte) 0x0F, (byte) 0x9F, (byte) 0x02, (byte) 0x06, (byte) 0x5F, (byte) 0x2A, (byte) 0x02, (byte) 0x9A, (byte) 0x03, (byte) 0x9C, (byte) 0x01, (byte) 0x95, (byte) 0x05, (byte) 0x9F, (byte) 0x37, (byte) 0x04};
        APP_CUP_01.Version = new byte[]{(byte) 0x00, (byte) 0x30};

        result = com.telpo.emv.EmvService.Emv_AddApp(APP_CUP_01);
        com.telpo.base.util.MLog.i("ADD APP_CUP_01:" + result);

 /*----------------------------------------------------------------------- division line-----------------------------------------------------------------------------------------*/

        com.telpo.emv.EmvApp APP_CUP_02 = new com.telpo.emv.EmvApp();
        name = "China Union Pay";
        try {
            APP_CUP_02.AppName = name.getBytes("ascii");
        } catch (java.io.UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        APP_CUP_02.AID = new byte[]{(byte) 0xA0, (byte) 0x00, (byte) 0x00, (byte) 0x03, (byte) 0x33, (byte) 0x01, (byte) 0x01, (byte) 0x02};
        APP_CUP_02.SelFlag = (byte) 0x00;
        APP_CUP_02.Priority = (byte) 0x00;
        APP_CUP_02.TargetPer = (byte) 0;
        APP_CUP_02.MaxTargetPer = (byte) 0;
        APP_CUP_02.FloorLimitCheck = (byte) 1;
        APP_CUP_02.RandTransSel = (byte) 1;
        APP_CUP_02.VelocityCheck = (byte) 1;
        APP_CUP_02.FloorLimit = new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x50, (byte) 0x00};//9F1B:FloorLimit
        APP_CUP_02.Threshold = new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};
        APP_CUP_02.TACDenial = new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};
        APP_CUP_02.TACOnline = new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};
        APP_CUP_02.TACDefault = new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};
        APP_CUP_02.AcquierId = new byte[]{(byte) 0x01, (byte) 0x26, (byte) 0x69, (byte) 0x66, (byte) 0x78, (byte) 0x90};
        APP_CUP_02.DDOL = new byte[]{(byte) 0x03, (byte) 0x9F, (byte) 0x37, (byte) 0x04};
        APP_CUP_02.TDOL = new byte[]{(byte) 0x0F, (byte) 0x9F, (byte) 0x02, (byte) 0x06, (byte) 0x5F, (byte) 0x2A, (byte) 0x02, (byte) 0x9A, (byte) 0x03, (byte) 0x9C, (byte) 0x01, (byte) 0x95, (byte) 0x05, (byte) 0x9F, (byte) 0x37, (byte) 0x04};
        APP_CUP_02.Version = new byte[]{(byte) 0x00, (byte) 0x30};

        result = com.telpo.emv.EmvService.Emv_AddApp(APP_CUP_02);
        com.telpo.base.util.MLog.i("ADD APP_CUP_02:" + result);
 /*----------------------------------------------------------------------- division line-----------------------------------------------------------------------------------------*/

        com.telpo.emv.EmvApp APP_CUP_03 = new com.telpo.emv.EmvApp();
        name = "China Union Pay";
        try {
            APP_CUP_03.AppName = name.getBytes("ascii");
        } catch (java.io.UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        APP_CUP_03.AID = new byte[]{(byte) 0xA0, (byte) 0x00, (byte) 0x00, (byte) 0x03, (byte) 0x33, (byte) 0x01, (byte) 0x01, (byte) 0x03};
        APP_CUP_03.SelFlag = (byte) 0x00;
        APP_CUP_03.Priority = (byte) 0x00;
        APP_CUP_03.TargetPer = (byte) 0;
        APP_CUP_03.MaxTargetPer = (byte) 0;
        APP_CUP_03.FloorLimitCheck = (byte) 1;
        APP_CUP_03.RandTransSel = (byte) 1;
        APP_CUP_03.VelocityCheck = (byte) 1;
        APP_CUP_03.FloorLimit = new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x50};//9F1B:FloorLimit
        APP_CUP_03.Threshold = new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};
        APP_CUP_03.TACDenial = new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};
        APP_CUP_03.TACOnline = new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};
        APP_CUP_03.TACDefault = new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};
        APP_CUP_03.AcquierId = new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x12, (byte) 0x34, (byte) 0x56};
        APP_CUP_03.DDOL = new byte[]{(byte) 0x03, (byte) 0x9F, (byte) 0x37, (byte) 0x04};
        APP_CUP_03.TDOL = new byte[]{(byte) 0x0F, (byte) 0x9F, (byte) 0x02, (byte) 0x06, (byte) 0x5F, (byte) 0x2A, (byte) 0x02, (byte) 0x9A, (byte) 0x03, (byte) 0x9C, (byte) 0x01, (byte) 0x95, (byte) 0x05, (byte) 0x9F, (byte) 0x37, (byte) 0x04};
        APP_CUP_03.Version = new byte[]{(byte) 0x00, (byte) 0x30};

        result = com.telpo.emv.EmvService.Emv_AddApp(APP_CUP_03);
        com.telpo.base.util.MLog.i("ADD APP_CUP_03:" + result);

 /*----------------------------------------------------------------------- division line-----------------------------------------------------------------------------------------*/
        com.telpo.emv.EmvApp PBOC_TEST_APP = new com.telpo.emv.EmvApp();
        PBOC_TEST_APP.AID = new byte[]{(byte) 0xA0, (byte) 0x00, (byte) 0x00, (byte) 0x03, (byte) 0x33, (byte) 0x01, (byte) 0x01};
        PBOC_TEST_APP.Priority = (byte) 0x00;
        PBOC_TEST_APP.TargetPer = (byte) 0;
        PBOC_TEST_APP.MaxTargetPer = (byte) 0;
        PBOC_TEST_APP.FloorLimitCheck = (byte) 1;
        PBOC_TEST_APP.RandTransSel = (byte) 1;
        PBOC_TEST_APP.VelocityCheck = (byte) 1;
        PBOC_TEST_APP.FloorLimit = new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x20, (byte) 0x00};//9F1B:FloorLimit
        PBOC_TEST_APP.Threshold = new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};
        PBOC_TEST_APP.TACDenial = new byte[]{(byte) 0x00, (byte) 0x10, (byte) 0x00, (byte) 0x00, (byte) 0x00};
        PBOC_TEST_APP.TACOnline = new byte[]{(byte) 0xD8, (byte) 0x40, (byte) 0x04, (byte) 0xF8, (byte) 0x00};
        PBOC_TEST_APP.TACDefault = new byte[]{(byte) 0xD8, (byte) 0x40, (byte) 0x00, (byte) 0xA8, (byte) 0x00};
        PBOC_TEST_APP.AcquierId = new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x12, (byte) 0x34, (byte) 0x56};
        PBOC_TEST_APP.DDOL = new byte[]{(byte) 0x03, (byte) 0x9F, (byte) 0x37, (byte) 0x04};
        PBOC_TEST_APP.TDOL = new byte[]{(byte) 0x0F, (byte) 0x9F, (byte) 0x02, (byte) 0x06, (byte) 0x5F, (byte) 0x2A, (byte) 0x02, (byte) 0x9A, (byte) 0x03, (byte) 0x9C, (byte) 0x01, (byte) 0x95, (byte) 0x05, (byte) 0x9F, (byte) 0x37, (byte) 0x04};
        PBOC_TEST_APP.Version = new byte[]{(byte) 0x00, (byte) 0x8C};

        result = com.telpo.emv.EmvService.Emv_AddApp(PBOC_TEST_APP);
        com.telpo.base.util.MLog.i("ADD PBOC_TEST_APP:" + result);

    }

    public static void Add_Default_CAPK() {
        int result = 0;
/*----------------------------------------------------------------------- division line-----------------------------------------------------------------------------------------*/

        com.telpo.emv.EmvCAPK capk_pobc_80 = new com.telpo.emv.EmvCAPK();
        capk_pobc_80.RID = new byte[]{(byte) 0xA0, (byte) 0x00, (byte) 0x00, (byte) 0x03, (byte) 0x33};
        capk_pobc_80.KeyID = (byte) 0x80;
        capk_pobc_80.HashInd = (byte) 0x01;
        capk_pobc_80.ArithInd = (byte) 0x01;
        capk_pobc_80.Modul = new byte[]{
                (byte) 0xCC, (byte) 0xDB, (byte) 0xA6, (byte) 0x86, (byte) 0xE2, (byte) 0xEF, (byte) 0xB8, (byte) 0x4C, (byte)
                0xE2, (byte) 0xEA, (byte) 0x01, (byte) 0x20, (byte) 0x9E, (byte) 0xEB, (byte) 0x53, (byte) 0xBE, (byte)
                0xF2, (byte) 0x1A, (byte) 0xB6, (byte) 0xD3, (byte) 0x53, (byte) 0x27, (byte) 0x4F, (byte) 0xF8, (byte)
                0x39, (byte) 0x1D, (byte) 0x70, (byte) 0x35, (byte) 0xD7, (byte) 0x6E, (byte) 0x21, (byte) 0x56, (byte)
                0xCA, (byte) 0xED, (byte) 0xD0, (byte) 0x75, (byte) 0x10, (byte) 0xE0, (byte) 0x7D, (byte) 0xAF, (byte)
                0xCA, (byte) 0xCA, (byte) 0xBB, (byte) 0x7C, (byte) 0xCB, (byte) 0x09, (byte) 0x50, (byte) 0xBA, (byte)
                0x2F, (byte) 0x0A, (byte) 0x3C, (byte) 0xEC, (byte) 0x31, (byte) 0x3C, (byte) 0x52, (byte) 0xEE, (byte)
                0x6C, (byte) 0xD0, (byte) 0x9E, (byte) 0xF0, (byte) 0x04, (byte) 0x01, (byte) 0xA3, (byte) 0xD6, (byte)
                0xCC, (byte) 0x5F, (byte) 0x68, (byte) 0xCA, (byte) 0x5F, (byte) 0xCD, (byte) 0x0A, (byte) 0xC6, (byte)
                0x13, (byte) 0x21, (byte) 0x41, (byte) 0xFA, (byte) 0xFD, (byte) 0x1C, (byte) 0xFA, (byte) 0x36, (byte)
                0xA2, (byte) 0x69, (byte) 0x2D, (byte) 0x02, (byte) 0xDD, (byte) 0xC2, (byte) 0x7E, (byte) 0xDA, (byte)
                0x4C, (byte) 0xD5, (byte) 0xBE, (byte) 0xA6, (byte) 0xFF, (byte) 0x21, (byte) 0x91, (byte) 0x3B, (byte)
                0x51, (byte) 0x3C, (byte) 0xE7, (byte) 0x8B, (byte) 0xF3, (byte) 0x3E, (byte) 0x68, (byte) 0x77, (byte)
                0xAA, (byte) 0x5B, (byte) 0x60, (byte) 0x5B, (byte) 0xC6, (byte) 0x9A, (byte) 0x53, (byte) 0x4F, (byte)
                0x37, (byte) 0x77, (byte) 0xCB, (byte) 0xED, (byte) 0x63, (byte) 0x76, (byte) 0xBA, (byte) 0x64, (byte)
                0x9C, (byte) 0x72, (byte) 0x51, (byte) 0x6A, (byte) 0x7E, (byte) 0x16, (byte) 0xAF, (byte) 0x85
        };
        capk_pobc_80.Exponent = new byte[]{0x01, 0x00, 0x01};
        capk_pobc_80.ExpDate = new byte[]{0x15, 0x12, 0x31};
        capk_pobc_80.CheckSum = new byte[]{
                (byte) 0xCC, (byte) 0x95, (byte) 0x85, (byte) 0xE8, (byte) 0xE6, (byte) 0x37, (byte) 0x19, (byte) 0x1C, (byte) 0x10, (byte) 0xFC, (byte) 0xEC, (byte) 0xB3, (byte) 0x2B, (byte) 0x5A, (byte) 0xE1, (byte) 0xB9, (byte) 0xD4, (byte) 0x10, (byte) 0xB5, (byte) 0x2D
        };
        result = com.telpo.emv.EmvService.Emv_AddCapk(capk_pobc_80);
        com.telpo.base.util.MLog.i("Add CAPK capk_pobc_80:" + result + " ID:" + capk_pobc_80.KeyID);


/*----------------------------------------------------------------------- division line-----------------------------------------------------------------------------------------*/

        com.telpo.emv.EmvCAPK capk_pobc_C0 = new com.telpo.emv.EmvCAPK();
        capk_pobc_C0.RID = new byte[]{(byte) 0xA0, (byte) 0x00, (byte) 0x00, (byte) 0x03, (byte) 0x33};
        capk_pobc_C0.KeyID = (byte) 0xC0;
        capk_pobc_C0.HashInd = (byte) 0x01;
        capk_pobc_C0.ArithInd = (byte) 0x01;
        capk_pobc_C0.Modul = new byte[]{
                (byte) 0xC7, (byte) 0xCD, (byte) 0xB6, (byte) 0xF2, (byte) 0xA3, (byte) 0xFE, (byte) 0x80, (byte) 0xA8, (byte)
                0x83, (byte) 0x4C, (byte) 0xDD, (byte) 0xDD, (byte) 0x32, (byte) 0x6E, (byte) 0x10, (byte) 0x82, (byte)
                0xAA, (byte) 0x22, (byte) 0x88, (byte) 0xF4, (byte) 0x7C, (byte) 0x46, (byte) 0x4D, (byte) 0x57, (byte)
                0xB3, (byte) 0x47, (byte) 0x18, (byte) 0x19, (byte) 0x34, (byte) 0x31, (byte) 0x71, (byte) 0x1A, (byte)
                0x44, (byte) 0x11, (byte) 0x91, (byte) 0x48, (byte) 0x05, (byte) 0x50, (byte) 0x44, (byte) 0xCF, (byte)
                0xE3, (byte) 0x31, (byte) 0x37, (byte) 0x08, (byte) 0xBE, (byte) 0xD0, (byte) 0xC9, (byte) 0x8E, (byte)
                0x1C, (byte) 0x58, (byte) 0x9B, (byte) 0x0F, (byte) 0x53, (byte) 0xCF, (byte) 0x6D, (byte) 0x7E, (byte)
                0x82, (byte) 0x9F, (byte) 0xCD, (byte) 0x90, (byte) 0x6D, (byte) 0x21, (byte) 0xA9, (byte) 0x0F, (byte)
                0xD4, (byte) 0xCB, (byte) 0x6B, (byte) 0xAF, (byte) 0x13, (byte) 0x11, (byte) 0x0C, (byte) 0x46, (byte)
                0x85, (byte) 0x10, (byte) 0x7C, (byte) 0x27, (byte) 0xE0, (byte) 0x09, (byte) 0x81, (byte) 0xDB, (byte)
                0x29, (byte) 0xDC, (byte) 0x0A, (byte) 0xC1, (byte) 0x86, (byte) 0xE6, (byte) 0xD7, (byte) 0x01, (byte)
                0x57, (byte) 0x7F, (byte) 0x23, (byte) 0x86, (byte) 0x56, (byte) 0x26, (byte) 0x24, (byte) 0x4E, (byte)
                0x1F, (byte) 0x9B, (byte) 0x2C, (byte) 0xD1, (byte) 0xDD, (byte) 0xFC, (byte) 0xB9, (byte) 0xE8, (byte)
                0x99, (byte) 0xB4, (byte) 0x1F, (byte) 0x50, (byte) 0x84, (byte) 0xD8, (byte) 0xCC, (byte) 0xC1, (byte)
                0x78, (byte) 0xA7, (byte) 0xC3, (byte) 0xF4, (byte) 0x54, (byte) 0x6C, (byte) 0xF9, (byte) 0x31, (byte)
                0x87, (byte) 0x10, (byte) 0x6F, (byte) 0xAB, (byte) 0x05, (byte) 0x5A, (byte) 0x7A, (byte) 0xC6, (byte)
                0x7D, (byte) 0xF6, (byte) 0x2E, (byte) 0x77, (byte) 0x8C, (byte) 0xB8, (byte) 0x88, (byte) 0x23, (byte)
                0xBA, (byte) 0x58, (byte) 0xCF, (byte) 0x75, (byte) 0x46, (byte) 0xC2, (byte) 0xB0, (byte) 0x9F
        };
        capk_pobc_C0.Exponent = new byte[]{0x01, 0x00, 0x01};
        capk_pobc_C0.ExpDate = new byte[]{0x15, 0x12, 0x31};
        capk_pobc_C0.CheckSum = new byte[]{
                (byte) 0xCC, (byte) 0x95, (byte) 0x85, (byte) 0xE8, (byte) 0xE6, (byte) 0x37, (byte) 0x19, (byte) 0x1C, (byte) 0x10, (byte) 0xFC, (byte) 0xEC, (byte) 0xB3, (byte) 0x2B, (byte) 0x5A, (byte) 0xE1, (byte) 0xB9, (byte) 0xD4, (byte) 0x10, (byte) 0xB5, (byte) 0x2D
        };

        result = com.telpo.emv.EmvService.Emv_AddCapk(capk_pobc_C0);
        com.telpo.base.util.MLog.i("Add CAPK capk_pobc_C0:" + result + " ID:" + capk_pobc_C0.KeyID);


/*----------------------------------------------------------------------- division line-----------------------------------------------------------------------------------------*/

        com.telpo.emv.EmvCAPK capk_pobc_C1 = new com.telpo.emv.EmvCAPK();
        capk_pobc_C1.RID = new byte[]{(byte) 0xA0, (byte) 0x00, (byte) 0x00, (byte) 0x03, (byte) 0x33};
        capk_pobc_C1.KeyID = (byte) 0xC1;
        capk_pobc_C1.HashInd = (byte) 0x01;
        capk_pobc_C1.ArithInd = (byte) 0x01;
        capk_pobc_C1.Modul = new byte[]{
                (byte) 0x92, (byte) 0xF0, (byte) 0x83, (byte) 0xCB, (byte) 0xE4, (byte) 0x6F, (byte) 0x8D, (byte) 0xCC, (byte)
                0x0C, (byte) 0x04, (byte) 0xE4, (byte) 0x98, (byte) 0xBA, (byte) 0x99, (byte) 0x52, (byte) 0xBA, (byte)
                0x9D, (byte) 0x4C, (byte) 0x09, (byte) 0xC8, (byte) 0x0D, (byte) 0xD2, (byte) 0x77, (byte) 0xE5, (byte)
                0x79, (byte) 0xF0, (byte) 0x7E, (byte) 0x45, (byte) 0x77, (byte) 0x28, (byte) 0x46, (byte) 0xFA, (byte)
                0x43, (byte) 0xDD, (byte) 0x3A, (byte) 0xB3, (byte) 0x1C, (byte) 0xC6, (byte) 0xB0, (byte) 0x8D, (byte)
                0xD1, (byte) 0x86, (byte) 0x95, (byte) 0x71, (byte) 0x59, (byte) 0x49, (byte) 0xFB, (byte) 0x10, (byte)
                0x8E, (byte) 0x53, (byte) 0xA0, (byte) 0x71, (byte) 0xD3, (byte) 0x93, (byte) 0xA7, (byte) 0xFD, (byte)
                0xDB, (byte) 0xF9, (byte) 0xC5, (byte) 0xFB, (byte) 0x0B, (byte) 0x05, (byte) 0x07, (byte) 0x13, (byte)
                0x87, (byte) 0x97, (byte) 0x31, (byte) 0x74, (byte) 0x80, (byte) 0xFC, (byte) 0x48, (byte) 0xD6, (byte)
                0x33, (byte) 0xED, (byte) 0x38, (byte) 0xB4, (byte) 0x01, (byte) 0xA4, (byte) 0x51, (byte) 0x44, (byte)
                0x3A, (byte) 0xD7, (byte) 0xF1, (byte) 0x5F, (byte) 0xAC, (byte) 0xDA, (byte) 0x45, (byte) 0xA6, (byte)
                0x2A, (byte) 0xBE, (byte) 0x24, (byte) 0xFF, (byte) 0x63, (byte) 0x43, (byte) 0xAD, (byte) 0xD0, (byte)
                0x90, (byte) 0x9E, (byte) 0xA8, (byte) 0x38, (byte) 0x93, (byte) 0x48, (byte) 0xE5, (byte) 0x4E, (byte)
                0x26, (byte) 0xF8, (byte) 0x42, (byte) 0x88, (byte) 0x0D, (byte) 0x1A, (byte) 0x69, (byte) 0xF9, (byte)
                0x21, (byte) 0x43, (byte) 0x68, (byte) 0xBA, (byte) 0x30, (byte) 0xC1, (byte) 0x8D, (byte) 0xE5, (byte)
                0xC5, (byte) 0xE0, (byte) 0xCB, (byte) 0x92, (byte) 0x53, (byte) 0xB5, (byte) 0xAB, (byte) 0xC5, (byte)
                0x5F, (byte) 0xB6, (byte) 0xEF, (byte) 0x0A, (byte) 0x73, (byte) 0x8D, (byte) 0x92, (byte) 0x74, (byte)
                0x94, (byte) 0xA3, (byte) 0x0B, (byte) 0xBF, (byte) 0x82, (byte) 0xE3, (byte) 0x40, (byte) 0x28, (byte)
                0x53, (byte) 0x63, (byte) 0xB6, (byte) 0xFA, (byte) 0xA1, (byte) 0x56, (byte) 0x73, (byte) 0x82, (byte)
                0x9D, (byte) 0xBB, (byte) 0x21, (byte) 0x0E, (byte) 0x71, (byte) 0x0D, (byte) 0xA5, (byte) 0x8E, (byte)
                0xE9, (byte) 0xE5, (byte) 0x78, (byte) 0xE7, (byte) 0xCE, (byte) 0x55, (byte) 0xDC, (byte) 0x81, (byte)
                0x2A, (byte) 0xB7, (byte) 0xD6, (byte) 0xDC, (byte) 0xCE, (byte) 0x0E, (byte) 0x3B, (byte) 0x1A, (byte)
                0xE1, (byte) 0x79, (byte) 0xD6, (byte) 0x64, (byte) 0xF3, (byte) 0x35, (byte) 0x6E, (byte) 0xB9, (byte)
                0x51, (byte) 0xE3, (byte) 0xC9, (byte) 0x1A, (byte) 0x1C, (byte) 0xBB, (byte) 0xF6, (byte) 0xA7, (byte)
                0xCA, (byte) 0x8D, (byte) 0x0C, (byte) 0x7E, (byte) 0xC9, (byte) 0xC6, (byte) 0xAF, (byte) 0x7A, (byte)
                0x49, (byte) 0x41, (byte) 0xC5, (byte) 0x05, (byte) 0x10, (byte) 0x99, (byte) 0xB9, (byte) 0x78, (byte)
                0x4E, (byte) 0x56, (byte) 0xC9, (byte) 0x16, (byte) 0x20, (byte) 0x67, (byte) 0xB8, (byte) 0xC3, (byte)
                0xB1, (byte) 0x5C, (byte) 0x5F, (byte) 0xA4, (byte) 0x48, (byte) 0x0A, (byte) 0x64, (byte) 0x5C, (byte)
                0xD2, (byte) 0x52, (byte) 0x6A, (byte) 0x69, (byte) 0xC8, (byte) 0x0B, (byte) 0xA8, (byte) 0xEF, (byte)
                0x36, (byte) 0x1B, (byte) 0xE2, (byte) 0xAA, (byte) 0x94, (byte) 0x17, (byte) 0xDE, (byte) 0xFC, (byte)
                0xE3, (byte) 0x5B, (byte) 0x62, (byte) 0xB0, (byte) 0xC9, (byte) 0xCF, (byte) 0x09, (byte) 0x7D
        };
        capk_pobc_C1.Exponent = new byte[]{0x01, 0x00, 0x01};
        capk_pobc_C1.ExpDate = new byte[]{0x15, 0x12, 0x31};
        capk_pobc_C1.CheckSum = new byte[]{
                (byte) 0xCC, (byte) 0x95, (byte) 0x85, (byte) 0xE8, (byte) 0xE6, (byte) 0x37, (byte) 0x19, (byte) 0x1C, (byte) 0x10, (byte) 0xFC, (byte) 0xEC, (byte) 0xB3, (byte) 0x2B, (byte) 0x5A, (byte) 0xE1, (byte) 0xB9, (byte) 0xD4, (byte) 0x10, (byte) 0xB5, (byte) 0x2D
        };

        result = com.telpo.emv.EmvService.Emv_AddCapk(capk_pobc_C1);
        com.telpo.base.util.MLog.i("Add CAPK capk_pobc_C1:" + result + " ID:" + capk_pobc_C1.KeyID);


/*----------------------------------------------------------------------- division line-----------------------------------------------------------------------------------------*/

        com.telpo.emv.EmvCAPK capk_pobc_61 = new com.telpo.emv.EmvCAPK();
        capk_pobc_61.RID = new byte[]{(byte) 0xA0, (byte) 0x00, (byte) 0x00, (byte) 0x03, (byte) 0x33};
        capk_pobc_61.KeyID = (byte) 0x61;
        capk_pobc_61.HashInd = (byte) 0x01;
        capk_pobc_61.ArithInd = (byte) 0x01;
        capk_pobc_61.Modul = new byte[]{
                (byte) 0x83, (byte) 0x4D, (byte) 0x2A, (byte) 0x38, (byte) 0x7C, (byte) 0x5A, (byte) 0x5F, (byte) 0x17, (byte)
                0x6E, (byte) 0xF3, (byte) 0xE6, (byte) 0x6C, (byte) 0xAA, (byte) 0xF8, (byte) 0x3F, (byte) 0x19, (byte)
                0x4B, (byte) 0x15, (byte) 0xAA, (byte) 0xD2, (byte) 0x47, (byte) 0x0C, (byte) 0x78, (byte) 0xC7, (byte)
                0x7D, (byte) 0x6E, (byte) 0xB3, (byte) 0x8E, (byte) 0xDA, (byte) 0xE3, (byte) 0xA2, (byte) 0xF9, (byte)
                0xBA, (byte) 0x16, (byte) 0x23, (byte) 0xF6, (byte) 0xA5, (byte) 0x8C, (byte) 0x89, (byte) 0x2C, (byte)
                0xC9, (byte) 0x25, (byte) 0x63, (byte) 0x2D, (byte) 0xFF, (byte) 0x48, (byte) 0xCE, (byte) 0x95, (byte)
                0x4B, (byte) 0x21, (byte) 0xA5, (byte) 0x3E, (byte) 0x1F, (byte) 0x1E, (byte) 0x43, (byte) 0x66, (byte)
                0xBE, (byte) 0x40, (byte) 0x3C, (byte) 0x27, (byte) 0x9B, (byte) 0x90, (byte) 0x02, (byte) 0x7C, (byte)
                0xBC, (byte) 0x72, (byte) 0x60, (byte) 0x5D, (byte) 0xB6, (byte) 0xC7, (byte) 0x90, (byte) 0x49, (byte)
                0xB8, (byte) 0x99, (byte) 0x2C, (byte) 0xB4, (byte) 0x91, (byte) 0x2E, (byte) 0xFA, (byte) 0x27, (byte)
                0x0B, (byte) 0xEC, (byte) 0xAB, (byte) 0x3A, (byte) 0x7C, (byte) 0xEF, (byte) 0xE0, (byte) 0x5B, (byte)
                0xFA, (byte) 0x46, (byte) 0xE4, (byte) 0xC7, (byte) 0xBB, (byte) 0xCF, (byte) 0x7C, (byte) 0x7A, (byte)
                0x17, (byte) 0x3B, (byte) 0xD9, (byte) 0x88, (byte) 0xD9, (byte) 0x89, (byte) 0xB3, (byte) 0x2C, (byte)
                0xB7, (byte) 0x9F, (byte) 0xAC, (byte) 0x8E, (byte) 0x35, (byte) 0xFB, (byte) 0xE1, (byte) 0x86, (byte)
                0x0E, (byte) 0x7E, (byte) 0xA9, (byte) 0xF2, (byte) 0x38, (byte) 0xA9, (byte) 0x2A, (byte) 0x35, (byte)
                0x93, (byte) 0x55, (byte) 0x2D, (byte) 0x03, (byte) 0xD1, (byte) 0xE3, (byte) 0x86, (byte) 0x01
        };
        capk_pobc_61.Exponent = new byte[]{0x03};
        capk_pobc_61.ExpDate = new byte[]{0x15, 0x12, 0x31};
        capk_pobc_61.CheckSum = new byte[]{
                (byte) 0xCC, (byte) 0x95, (byte) 0x85, (byte) 0xE8, (byte) 0xE6, (byte) 0x37, (byte) 0x19, (byte) 0x1C, (byte) 0x10, (byte) 0xFC, (byte) 0xEC, (byte) 0xB3, (byte) 0x2B, (byte) 0x5A, (byte) 0xE1, (byte) 0xB9, (byte) 0xD4, (byte) 0x10, (byte) 0xB5, (byte) 0x2D
        };

        result = com.telpo.emv.EmvService.Emv_AddCapk(capk_pobc_61);
        com.telpo.base.util.MLog.i("Add CAPK capk_pobc_61:" + result + " ID:" + capk_pobc_61.KeyID);

/*----------------------------------------------------------------------- division line-----------------------------------------------------------------------------------------*/

        com.telpo.emv.EmvCAPK capk_pobc_62 = new com.telpo.emv.EmvCAPK();
        capk_pobc_62.RID = new byte[]{(byte) 0xA0, (byte) 0x00, (byte) 0x00, (byte) 0x03, (byte) 0x33};
        capk_pobc_62.KeyID = (byte) 0x62;
        capk_pobc_62.HashInd = (byte) 0x01;
        capk_pobc_62.ArithInd = (byte) 0x01;
        capk_pobc_62.Modul = new byte[]{
                (byte) 0xB5, (byte) 0xCD, (byte) 0xD1, (byte) 0xE5, (byte) 0x36, (byte) 0x88, (byte) 0x19, (byte) 0xFC, (byte)
                0x3E, (byte) 0xA6, (byte) 0x5B, (byte) 0x80, (byte) 0xC6, (byte) 0x81, (byte) 0x17, (byte) 0xBB, (byte)
                0xC2, (byte) 0x9F, (byte) 0x90, (byte) 0x96, (byte) 0xEB, (byte) 0xD2, (byte) 0x17, (byte) 0x26, (byte)
                0x9B, (byte) 0x58, (byte) 0x3B, (byte) 0x07, (byte) 0x45, (byte) 0xE0, (byte) 0xC1, (byte) 0x64, (byte)
                0x33, (byte) 0xD5, (byte) 0x4B, (byte) 0x8E, (byte) 0xF3, (byte) 0x87, (byte) 0xB1, (byte) 0xE6, (byte)
                0xCD, (byte) 0xDA, (byte) 0xED, (byte) 0x49, (byte) 0x23, (byte) 0xC3, (byte) 0x9E, (byte) 0x37, (byte)
                0x0E, (byte) 0x5C, (byte) 0xAD, (byte) 0xFE, (byte) 0x04, (byte) 0x17, (byte) 0x73, (byte) 0x02, (byte)
                0x3A, (byte) 0x6B, (byte) 0xC0, (byte) 0xA0, (byte) 0x33, (byte) 0xB0, (byte) 0x03, (byte) 0x1B, (byte)
                0x00, (byte) 0x48, (byte) 0xF1, (byte) 0x8A, (byte) 0xC1, (byte) 0x59, (byte) 0x77, (byte) 0x3C, (byte)
                0xB6, (byte) 0x69, (byte) 0x5E, (byte) 0xE9, (byte) 0x9F, (byte) 0x55, (byte) 0x1F, (byte) 0x41, (byte)
                0x48, (byte) 0x83, (byte) 0xFB, (byte) 0x05, (byte) 0xE5, (byte) 0x26, (byte) 0x40, (byte) 0xE8, (byte)
                0x93, (byte) 0xF4, (byte) 0x81, (byte) 0x60, (byte) 0x82, (byte) 0x24, (byte) 0x1D, (byte) 0x7B, (byte)
                0xFA, (byte) 0x36, (byte) 0x40, (byte) 0x96, (byte) 0x00, (byte) 0x03, (byte) 0xAD, (byte) 0x75, (byte)
                0x17, (byte) 0x89, (byte) 0x5C, (byte) 0x50, (byte) 0xE1, (byte) 0x84, (byte) 0xAA, (byte) 0x95, (byte)
                0x63, (byte) 0x67, (byte) 0xB7, (byte) 0xBF, (byte) 0xFC, (byte) 0x6D, (byte) 0x86, (byte) 0x16, (byte)
                0xA7, (byte) 0xB5, (byte) 0x7E, (byte) 0x2D, (byte) 0x44, (byte) 0x7A, (byte) 0xB3, (byte) 0xE1
        };
        capk_pobc_62.Exponent = new byte[]{0x01, 0x00, 0x01};
        capk_pobc_62.ExpDate = new byte[]{0x15, 0x12, 0x31};
        capk_pobc_62.CheckSum = new byte[]{
                (byte) 0xCC, (byte) 0x95, (byte) 0x85, (byte) 0xE8, (byte) 0xE6, (byte) 0x37, (byte) 0x19, (byte) 0x1C, (byte) 0x10, (byte) 0xFC, (byte) 0xEC, (byte) 0xB3, (byte) 0x2B, (byte) 0x5A, (byte) 0xE1, (byte) 0xB9, (byte) 0xD4, (byte) 0x10, (byte) 0xB5, (byte) 0x2D
        };

        result = com.telpo.emv.EmvService.Emv_AddCapk(capk_pobc_62);
        com.telpo.base.util.MLog.i("Add CAPK capk_pobc_62:" + result);

/*----------------------------------------------------------------------- division line-----------------------------------------------------------------------------------------*/

        com.telpo.emv.EmvCAPK capk_pobc_63 = new com.telpo.emv.EmvCAPK();
        capk_pobc_63.RID = new byte[]{(byte) 0xA0, (byte) 0x00, (byte) 0x00, (byte) 0x03, (byte) 0x33};
        capk_pobc_63.KeyID = (byte) 0x63;
        capk_pobc_63.HashInd = (byte) 0x01;
        capk_pobc_63.ArithInd = (byte) 0x01;
        capk_pobc_63.Modul = new byte[]{
                (byte) 0x86, (byte) 0x7E, (byte) 0xCA, (byte) 0x26, (byte) 0xA5, (byte) 0x74, (byte) 0x72, (byte) 0xDE, (byte)
                0xFB, (byte) 0x6C, (byte) 0xA9, (byte) 0x42, (byte) 0x89, (byte) 0x31, (byte) 0x2B, (byte) 0xA3, (byte)
                0x9C, (byte) 0x63, (byte) 0x05, (byte) 0x25, (byte) 0x18, (byte) 0xDC, (byte) 0x48, (byte) 0x0B, (byte)
                0x6E, (byte) 0xD4, (byte) 0x91, (byte) 0xAC, (byte) 0xC3, (byte) 0x7C, (byte) 0x02, (byte) 0x88, (byte)
                0x46, (byte) 0xF4, (byte) 0xD7, (byte) 0xB7, (byte) 0x9A, (byte) 0xFA, (byte) 0xEE, (byte) 0xFA, (byte)
                0x07, (byte) 0xFB, (byte) 0x01, (byte) 0x1D, (byte) 0xAA, (byte) 0x46, (byte) 0xC0, (byte) 0x60, (byte)
                0x21, (byte) 0xE9, (byte) 0x32, (byte) 0xD5, (byte) 0x01, (byte) 0xBF, (byte) 0x52, (byte) 0xF2, (byte)
                0x83, (byte) 0x4A, (byte) 0xDE, (byte) 0x3A, (byte) 0xC7, (byte) 0x68, (byte) 0x9E, (byte) 0x94, (byte)
                0xB2, (byte) 0x48, (byte) 0xB2, (byte) 0x8F, (byte) 0x3F, (byte) 0xE2, (byte) 0x80, (byte) 0x36, (byte)
                0x69, (byte) 0xDE, (byte) 0xDA, (byte) 0x00, (byte) 0x09, (byte) 0x88, (byte) 0xDA, (byte) 0x12, (byte)
                0x49, (byte) 0xF9, (byte) 0xA8, (byte) 0x91, (byte) 0x55, (byte) 0x8A, (byte) 0x05, (byte) 0xA1, (byte)
                0xE5, (byte) 0xA7, (byte) 0xBD, (byte) 0x2C, (byte) 0x28, (byte) 0x2F, (byte) 0xE1, (byte) 0x8D, (byte)
                0x20, (byte) 0x41, (byte) 0x89, (byte) 0xA9, (byte) 0x99, (byte) 0x4D, (byte) 0x4A, (byte) 0xDD, (byte)
                0x86, (byte) 0xC0, (byte) 0xCE, (byte) 0x50, (byte) 0x95, (byte) 0x2E, (byte) 0xD8, (byte) 0xBC, (byte)
                0xEC, (byte) 0x0C, (byte) 0xE6, (byte) 0x33, (byte) 0x67, (byte) 0x91, (byte) 0x88, (byte) 0x28, (byte)
                0x5E, (byte) 0x51, (byte) 0xE1, (byte) 0xBE, (byte) 0xD8, (byte) 0x40, (byte) 0xFC, (byte) 0xBF, (byte)
                0xC1, (byte) 0x09, (byte) 0x53, (byte) 0x93, (byte) 0x9A, (byte) 0xF4, (byte) 0x9D, (byte) 0xB9, (byte)
                0x00, (byte) 0x48, (byte) 0x91, (byte) 0x2E, (byte) 0x48, (byte) 0xB4, (byte) 0x41, (byte) 0x81
        };
        capk_pobc_63.Exponent = new byte[]{0x03};
        capk_pobc_63.ExpDate = new byte[]{0x15, 0x12, 0x31};
        capk_pobc_63.CheckSum = new byte[]{
                (byte) 0xCC, (byte) 0x95, (byte) 0x85, (byte) 0xE8, (byte) 0xE6, (byte) 0x37, (byte) 0x19, (byte) 0x1C, (byte) 0x10, (byte) 0xFC, (byte) 0xEC, (byte) 0xB3, (byte) 0x2B, (byte) 0x5A, (byte) 0xE1, (byte) 0xB9, (byte) 0xD4, (byte) 0x10, (byte) 0xB5, (byte) 0x2D
        };

        result = com.telpo.emv.EmvService.Emv_AddCapk(capk_pobc_63);
        com.telpo.base.util.MLog.i("Add CAPK capk_pobc_63:" + result + " ID:" + capk_pobc_63.KeyID);

/*----------------------------------------------------------------------- division line-----------------------------------------------------------------------------------------*/

        com.telpo.emv.EmvCAPK capk_pobc_64 = new com.telpo.emv.EmvCAPK();
        capk_pobc_64.RID = new byte[]{(byte) 0xA0, (byte) 0x00, (byte) 0x00, (byte) 0x03, (byte) 0x33};
        capk_pobc_64.KeyID = (byte) 0x64;
        capk_pobc_64.HashInd = (byte) 0x01;
        capk_pobc_64.ArithInd = (byte) 0x01;
        capk_pobc_64.Modul = new byte[]{
                (byte) 0x91, (byte) 0x12, (byte) 0x3E, (byte) 0xCF, (byte) 0x02, (byte) 0x30, (byte) 0xE3, (byte) 0xCB, (byte)
                0x24, (byte) 0x5C, (byte) 0x88, (byte) 0xDD, (byte) 0xFA, (byte) 0x3E, (byte) 0xE5, (byte) 0x7B, (byte)
                0xC5, (byte) 0x8E, (byte) 0xD0, (byte) 0x0B, (byte) 0x36, (byte) 0x7B, (byte) 0x38, (byte) 0x75, (byte)
                0xFC, (byte) 0xB7, (byte) 0x95, (byte) 0x48, (byte) 0x87, (byte) 0x26, (byte) 0x80, (byte) 0xF6, (byte)
                0x01, (byte) 0xE8, (byte) 0xC8, (byte) 0x39, (byte) 0xAC, (byte) 0x07, (byte) 0x21, (byte) 0xBA, (byte)
                0xB3, (byte) 0xB8, (byte) 0x9E, (byte) 0xD2, (byte) 0x16, (byte) 0x07, (byte) 0x28, (byte) 0x1C, (byte)
                0x89, (byte) 0x19, (byte) 0xBF, (byte) 0x72, (byte) 0x62, (byte) 0x66, (byte) 0xEA, (byte) 0xB8, (byte)
                0x48, (byte) 0x50, (byte) 0x2A, (byte) 0xD8, (byte) 0x74, (byte) 0xB5, (byte) 0x10, (byte) 0x7A, (byte)
                0x4E, (byte) 0x65, (byte) 0x4E, (byte) 0xF6, (byte) 0xD3, (byte) 0x77, (byte) 0x73, (byte) 0x34, (byte)
                0x3F, (byte) 0x46, (byte) 0x14, (byte) 0x35, (byte) 0xC8, (byte) 0x6E, (byte) 0x4A, (byte) 0x8F, (byte)
                0x86, (byte) 0x6F, (byte) 0xB1, (byte) 0x8C, (byte) 0x7C, (byte) 0xBA, (byte) 0x49, (byte) 0x7B, (byte)
                0x42, (byte) 0x62, (byte) 0x90, (byte) 0xC3, (byte) 0x8D, (byte) 0x19, (byte) 0x6E, (byte) 0x2A, (byte)
                0xFF, (byte) 0x33, (byte) 0xC0, (byte) 0x90, (byte) 0x6F, (byte) 0x92, (byte) 0x96, (byte) 0xF2, (byte)
                0x97, (byte) 0xE1, (byte) 0x56, (byte) 0xDC, (byte) 0x60, (byte) 0x2A, (byte) 0x5E, (byte) 0x65, (byte)
                0x3C, (byte) 0xA1, (byte) 0x16, (byte) 0x8F, (byte) 0x11, (byte) 0x09, (byte) 0x26, (byte) 0x11, (byte)
                0x14, (byte) 0xBF, (byte) 0x7B, (byte) 0xE8, (byte) 0x12, (byte) 0x7A, (byte) 0x3E, (byte) 0x80, (byte)
                0x07, (byte) 0x19, (byte) 0x18, (byte) 0x30, (byte) 0x13, (byte) 0x42, (byte) 0x99, (byte) 0x39, (byte)
                0x5C, (byte) 0xE2, (byte) 0xB3, (byte) 0x22, (byte) 0x22, (byte) 0x86, (byte) 0x67, (byte) 0xB7, (byte)
                0x6E, (byte) 0x07, (byte) 0x2E, (byte) 0xB7, (byte) 0xFD, (byte) 0x5D, (byte) 0x0F, (byte) 0xB3, (byte)
                0xA8, (byte) 0x3E, (byte) 0x8A, (byte) 0xD1, (byte) 0xD7, (byte) 0xF6, (byte) 0xFD, (byte) 0x81
        };
        capk_pobc_64.Exponent = new byte[]{0x03};
        capk_pobc_64.ExpDate = new byte[]{0x15, 0x12, 0x31};
        capk_pobc_64.CheckSum = new byte[]{
                (byte) 0xCC, (byte) 0x95, (byte) 0x85, (byte) 0xE8, (byte) 0xE6, (byte) 0x37, (byte) 0x19, (byte) 0x1C, (byte) 0x10, (byte) 0xFC, (byte) 0xEC, (byte) 0xB3, (byte) 0x2B, (byte) 0x5A, (byte) 0xE1, (byte) 0xB9, (byte) 0xD4, (byte) 0x10, (byte) 0xB5, (byte) 0x2D
        };

        result = com.telpo.emv.EmvService.Emv_AddCapk(capk_pobc_64);
        com.telpo.base.util.MLog.i("Add CAPK capk_pobc_64:" + result + " ID:" + capk_pobc_64.KeyID);

/*----------------------------------------------------------------------- division line-----------------------------------------------------------------------------------------*/

        com.telpo.emv.EmvCAPK capk_pobc_65 = new com.telpo.emv.EmvCAPK();
        capk_pobc_65.RID = new byte[]{(byte) 0xA0, (byte) 0x00, (byte) 0x00, (byte) 0x03, (byte) 0x33};
        capk_pobc_65.KeyID = (byte) 0x65;
        capk_pobc_65.HashInd = (byte) 0x01;
        capk_pobc_65.ArithInd = (byte) 0x01;
        capk_pobc_65.Modul = new byte[]{
                (byte) 0x81, (byte) 0xBA, (byte) 0x1E, (byte) 0x6B, (byte) 0x9F, (byte) 0x67, (byte) 0x1C, (byte) 0xFC, (byte)
                0x84, (byte) 0x8C, (byte) 0xA2, (byte) 0xAC, (byte) 0xD8, (byte) 0xE1, (byte) 0x7A, (byte) 0xF4, (byte)
                0x06, (byte) 0xB4, (byte) 0xD3, (byte) 0x29, (byte) 0xD1, (byte) 0xEC, (byte) 0xA5, (byte) 0xD0, (byte)
                0x1B, (byte) 0xC0, (byte) 0x94, (byte) 0xA8, (byte) 0x7C, (byte) 0x30, (byte) 0xAF, (byte) 0x49, (byte)
                0x86, (byte) 0x79, (byte) 0x44, (byte) 0xC6, (byte) 0x32, (byte) 0xE8, (byte) 0x18, (byte) 0x50, (byte)
                0x74, (byte) 0x65, (byte) 0x5F, (byte) 0xA5, (byte) 0x35, (byte) 0xAD, (byte) 0x8C, (byte) 0xA4, (byte)
                0x2A, (byte) 0x83, (byte) 0xB4, (byte) 0x1A, (byte) 0xAA, (byte) 0xEA, (byte) 0x85, (byte) 0x9F, (byte)
                0x43, (byte) 0x2F, (byte) 0xA0, (byte) 0xB8, (byte) 0x18, (byte) 0xE7, (byte) 0x2D, (byte) 0xC0, (byte)
                0x7E, (byte) 0xD3, (byte) 0xF7, (byte) 0x7F, (byte) 0xB3, (byte) 0x18, (byte) 0xA4, (byte) 0x75, (byte)
                0xA2, (byte) 0x61, (byte) 0xC0, (byte) 0x76, (byte) 0x0A, (byte) 0x15, (byte) 0x6E, (byte) 0x5D, (byte)
                0xDC, (byte) 0x15, (byte) 0x7A, (byte) 0xE8, (byte) 0xB7, (byte) 0x9B, (byte) 0xA7, (byte) 0x2D, (byte)
                0x89, (byte) 0xD6, (byte) 0x9F, (byte) 0xFF, (byte) 0x75, (byte) 0x46, (byte) 0x19, (byte) 0xE9, (byte)
                0x28, (byte) 0xF1, (byte) 0x51, (byte) 0x6A, (byte) 0x2A, (byte) 0x72, (byte) 0xC0, (byte) 0xF8, (byte)
                0x6B, (byte) 0x09, (byte) 0xB8, (byte) 0xEA, (byte) 0x25, (byte) 0xF8, (byte) 0x6D, (byte) 0xC5, (byte)
                0xA4, (byte) 0x8E, (byte) 0xBC, (byte) 0x5A, (byte) 0x16, (byte) 0xF8, (byte) 0x3F, (byte) 0xBA, (byte)
                0x8F, (byte) 0xC4, (byte) 0xE3, (byte) 0xA9, (byte) 0x82, (byte) 0x78, (byte) 0x91, (byte) 0x22, (byte)
                0x49, (byte) 0xF4, (byte) 0xE0, (byte) 0x79, (byte) 0xBC, (byte) 0xBC, (byte) 0x06, (byte) 0xE7, (byte)
                0xBE, (byte) 0xD9, (byte) 0xAE, (byte) 0xD3, (byte) 0x97, (byte) 0x87, (byte) 0x9D, (byte) 0x27, (byte)
                0x9E, (byte) 0xD9, (byte) 0x19, (byte) 0x25, (byte) 0x39, (byte) 0x49, (byte) 0x01, (byte) 0x26, (byte)
                0x09, (byte) 0x49, (byte) 0xBC, (byte) 0xCE, (byte) 0x6F, (byte) 0xA1, (byte) 0x16, (byte) 0x97, (byte)
                0x98, (byte) 0xA2, (byte) 0x71, (byte) 0x5D, (byte) 0xAE, (byte) 0x32, (byte) 0x98, (byte) 0x8B, (byte)
                0xEF, (byte) 0xBE, (byte) 0x96, (byte) 0x21, (byte) 0xAE, (byte) 0x15, (byte) 0xE0, (byte) 0xC1
        };
        capk_pobc_65.Exponent = new byte[]{0x01, 0x00, 0x01};
        capk_pobc_65.ExpDate = new byte[]{0x15, 0x12, 0x31};
        capk_pobc_65.CheckSum = new byte[]{
                (byte) 0xCC, (byte) 0x95, (byte) 0x85, (byte) 0xE8, (byte) 0xE6, (byte) 0x37, (byte) 0x19, (byte) 0x1C, (byte) 0x10, (byte) 0xFC, (byte) 0xEC, (byte) 0xB3, (byte) 0x2B, (byte) 0x5A, (byte) 0xE1, (byte) 0xB9, (byte) 0xD4, (byte) 0x10, (byte) 0xB5, (byte) 0x2D
        };

        result = com.telpo.emv.EmvService.Emv_AddCapk(capk_pobc_65);
        com.telpo.base.util.MLog.i("Add CAPK capk_pobc_65:" + result + " ID:" + capk_pobc_65.KeyID);


/*----------------------------------------------------------------------- division line-----------------------------------------------------------------------------------------*/

        com.telpo.emv.EmvCAPK capk_pobc_66 = new com.telpo.emv.EmvCAPK();
        capk_pobc_66.RID = new byte[]{(byte) 0xA0, (byte) 0x00, (byte) 0x00, (byte) 0x03, (byte) 0x33};
        capk_pobc_66.KeyID = (byte) 0x66;
        capk_pobc_66.HashInd = (byte) 0x01;
        capk_pobc_66.ArithInd = (byte) 0x01;
        capk_pobc_66.Modul = new byte[]{
                (byte) 0x7F, (byte) 0x5A, (byte) 0x39, (byte) 0x45, (byte) 0x79, (byte) 0x4D, (byte) 0x6B, (byte) 0x15, (byte)
                0xF5, (byte) 0xF2, (byte) 0x6B, (byte) 0x4A, (byte) 0x21, (byte) 0xA6, (byte) 0x3A, (byte) 0x5E, (byte)
                0xF3, (byte) 0x55, (byte) 0x40, (byte) 0xD8, (byte) 0xC8, (byte) 0xC0, (byte) 0x99, (byte) 0x15, (byte)
                0x1F, (byte) 0x22, (byte) 0x79, (byte) 0x78, (byte) 0x0A, (byte) 0x5C, (byte) 0x18, (byte) 0xA3, (byte)
                0x17, (byte) 0x70, (byte) 0x3C, (byte) 0x98, (byte) 0x63, (byte) 0x2E, (byte) 0x80, (byte) 0x4D, (byte)
                0x25, (byte) 0x57, (byte) 0x6A, (byte) 0x7B, (byte) 0x46, (byte) 0x0C, (byte) 0x05, (byte) 0x06, (byte)
                0x1E, (byte) 0x03, (byte) 0x97, (byte) 0x5E, (byte) 0x50, (byte) 0xFB, (byte) 0xD7, (byte) 0x49, (byte)
                0x5B, (byte) 0x3A, (byte) 0xDC, (byte) 0x8E, (byte) 0x42, (byte) 0x5E, (byte) 0x53, (byte) 0xDF, (byte)
                0x76, (byte) 0xFA, (byte) 0x40, (byte) 0xB0, (byte) 0x35, (byte) 0xE8, (byte) 0x7F, (byte) 0x69, (byte)
                0xAB, (byte) 0xF8, (byte) 0x76, (byte) 0x5A, (byte) 0x52, (byte) 0x52, (byte) 0x3F, (byte) 0x3B, (byte)
                0x1A, (byte) 0x39, (byte) 0xB1, (byte) 0x95, (byte) 0x28, (byte) 0xB0, (byte) 0x02, (byte) 0x23, (byte)
                0x90, (byte) 0x15, (byte) 0xFA, (byte) 0xDB, (byte) 0xA5, (byte) 0x92, (byte) 0x10, (byte) 0x51
        };
        capk_pobc_66.Exponent = new byte[]{0x01, 0x00, 0x01};
        capk_pobc_66.ExpDate = new byte[]{0x15, 0x12, 0x31};
        capk_pobc_66.CheckSum = new byte[]{
                (byte) 0xCC, (byte) 0x95, (byte) 0x85, (byte) 0xE8, (byte) 0xE6, (byte) 0x37, (byte) 0x19, (byte) 0x1C, (byte) 0x10, (byte) 0xFC, (byte) 0xEC, (byte) 0xB3, (byte) 0x2B, (byte) 0x5A, (byte) 0xE1, (byte) 0xB9, (byte) 0xD4, (byte) 0x10, (byte) 0xB5, (byte) 0x2D
        };

        result = com.telpo.emv.EmvService.Emv_AddCapk(capk_pobc_66);
        com.telpo.base.util.MLog.i("Add CAPK capk_pobc_66:" + result + " ID:" + capk_pobc_66.KeyID);

/*----------------------------------------------------------------------- division line-----------------------------------------------------------------------------------------*/

        com.telpo.emv.EmvCAPK capk_pobc_05 = new com.telpo.emv.EmvCAPK();
        capk_pobc_05.RID = new byte[]{(byte) 0xA0, (byte) 0x00, (byte) 0x00, (byte) 0x03, (byte) 0x33};
        capk_pobc_05.KeyID = (byte) 0x05;
        capk_pobc_05.HashInd = (byte) 0x01;
        capk_pobc_05.ArithInd = (byte) 0x01;
        capk_pobc_05.Modul = new byte[]{
                (byte) 0x97, (byte) 0xCF, (byte) 0x8B, (byte) 0xAD, (byte) 0x30, (byte) 0xCA, (byte) 0xE0, (byte) 0xF9, (byte)
                0xA8, (byte) 0x92, (byte) 0x85, (byte) 0x45, (byte) 0x4D, (byte) 0xDD, (byte) 0xE9, (byte) 0x67, (byte)
                0xAA, (byte) 0xFB, (byte) 0xCD, (byte) 0x4B, (byte) 0xC0, (byte) 0xB7, (byte) 0x8F, (byte) 0x29, (byte)
                0xEC, (byte) 0xB1, (byte) 0x00, (byte) 0x52, (byte) 0x86, (byte) 0xF1, (byte) 0x5F, (byte) 0x6D, (byte)
                0x75, (byte) 0x32, (byte) 0xA9, (byte) 0xC4, (byte) 0x76, (byte) 0x60, (byte) 0x7C, (byte) 0x73, (byte)
                0xFF, (byte) 0x74, (byte) 0x24, (byte) 0x31, (byte) 0x6D, (byte) 0xFC, (byte) 0x74, (byte) 0x18, (byte)
                0x94, (byte) 0xAA, (byte) 0x52, (byte) 0xED, (byte) 0xBA, (byte) 0xF9, (byte) 0x09, (byte) 0x71, (byte)
                0x9C, (byte) 0x7B, (byte) 0x53, (byte) 0x44, (byte) 0x83, (byte) 0x43, (byte) 0xB4, (byte) 0x5C, (byte)
                0xF2, (byte) 0xF0, (byte) 0x0A, (byte) 0x8A, (byte) 0xBF, (byte) 0xB7, (byte) 0x8C, (byte) 0xEE, (byte)
                0xBE, (byte) 0x84, (byte) 0x89, (byte) 0x33, (byte) 0xAA, (byte) 0xED, (byte) 0x97, (byte) 0xDB, (byte)
                0xE8, (byte) 0x4F, (byte) 0x07, (byte) 0x30, (byte) 0xF3, (byte) 0x4F, (byte) 0xB1, (byte) 0xAA, (byte)
                0x15, (byte) 0x28, (byte) 0xD3, (byte) 0xD6, (byte) 0xEC, (byte) 0x75, (byte) 0xB7, (byte) 0x32, (byte)
                0x52, (byte) 0xA3, (byte) 0x0D, (byte) 0x0C, (byte) 0x71, (byte) 0x75, (byte) 0x18, (byte) 0xBE, (byte)
                0x36, (byte) 0x45, (byte) 0x8A, (byte) 0xDD, (byte) 0x0F, (byte) 0xBF, (byte) 0x85, (byte) 0x4C, (byte)
                0x65, (byte) 0x49, (byte) 0x7F, (byte) 0x3F, (byte) 0x54, (byte) 0x08, (byte) 0x41, (byte) 0x54, (byte)
                0xB6, (byte) 0x0F, (byte) 0x51, (byte) 0x56, (byte) 0x13, (byte) 0x61, (byte) 0xEE, (byte) 0x8E, (byte)
                0x85, (byte) 0xF7, (byte) 0x42, (byte) 0xA5, (byte) 0x40, (byte) 0x05, (byte) 0x52, (byte) 0x4C, (byte)
                0xB0, (byte) 0x0F, (byte) 0xEB, (byte) 0xC3, (byte) 0x34, (byte) 0x27, (byte) 0x6E, (byte) 0x0E, (byte)
                0x63, (byte) 0xDA, (byte) 0xD8, (byte) 0x6C, (byte) 0x07, (byte) 0x9A, (byte) 0x9A, (byte) 0x3D, (byte)
                0xF5, (byte) 0xDD, (byte) 0x32, (byte) 0xBE, (byte) 0xCA, (byte) 0xDE, (byte) 0x1A, (byte) 0xB2, (byte)
                0xB7, (byte) 0x1F, (byte) 0x5F, (byte) 0x0A, (byte) 0x0E, (byte) 0x95, (byte) 0xA4, (byte) 0x00, (byte)
                0x0D, (byte) 0x01, (byte) 0xF1, (byte) 0x04, (byte) 0x4A, (byte) 0x57, (byte) 0x8A, (byte) 0xAD, (byte)
                0x92, (byte) 0xE9, (byte) 0xFD, (byte) 0xE9, (byte) 0x2E, (byte) 0x3C, (byte) 0x6A, (byte) 0xA3, (byte)
                0xDC, (byte) 0xD4, (byte) 0x91, (byte) 0x3D, (byte) 0xFA, (byte) 0x55, (byte) 0x52, (byte) 0x53, (byte)
                0x7E, (byte) 0x7D, (byte) 0xE7, (byte) 0x5E, (byte) 0x24, (byte) 0x1F, (byte) 0xAE, (byte) 0xD4, (byte)
                0x55, (byte) 0xD7, (byte) 0x6C, (byte) 0xB8, (byte) 0xFC, (byte) 0xAF, (byte) 0xEE, (byte) 0xD3, (byte)
                0xFD, (byte) 0x6D, (byte) 0xAB, (byte) 0x24, (byte) 0xD7, (byte) 0xA9, (byte) 0xC3, (byte) 0x28, (byte)
                0x52, (byte) 0xF8, (byte) 0x66, (byte) 0xC7, (byte) 0x51, (byte) 0xD7, (byte) 0x71, (byte) 0x0F, (byte)
                0x49, (byte) 0x4A, (byte) 0x0D, (byte) 0xF1, (byte) 0x1B, (byte) 0x67, (byte) 0xFA, (byte) 0xEC, (byte)
                0xDD, (byte) 0x87, (byte) 0xA9, (byte) 0xA4, (byte) 0xE2, (byte) 0xCC, (byte) 0x44, (byte) 0xF6, (byte)
                0xF2, (byte) 0x7E, (byte) 0x46, (byte) 0xE3, (byte) 0xC0, (byte) 0xCC, (byte) 0xCD, (byte) 0x0F
        };
        capk_pobc_05.Exponent = new byte[]{0x03};
        capk_pobc_05.ExpDate = new byte[]{0x15, 0x12, 0x31};
        capk_pobc_05.CheckSum = new byte[]{
                (byte) 0xCC, (byte) 0x95, (byte) 0x85, (byte) 0xE8, (byte) 0xE6, (byte) 0x37, (byte) 0x19, (byte) 0x1C, (byte) 0x10, (byte) 0xFC, (byte) 0xEC, (byte) 0xB3, (byte) 0x2B, (byte) 0x5A, (byte) 0xE1, (byte) 0xB9, (byte) 0xD4, (byte) 0x10, (byte) 0xB5, (byte) 0x2D
        };

        result = com.telpo.emv.EmvService.Emv_AddCapk(capk_pobc_05);
        com.telpo.base.util.MLog.i("Add CAPK capk_pobc_05:" + result + " ID:" + capk_pobc_05.KeyID);


/*----------------------------------------------------------------------- division line-----------------------------------------------------------------------------------------*/

        com.telpo.emv.EmvCAPK capk_pobc_08 = new com.telpo.emv.EmvCAPK();
        capk_pobc_08.RID = new byte[]{(byte) 0xA0, (byte) 0x00, (byte) 0x00, (byte) 0x03, (byte) 0x33};
        capk_pobc_08.KeyID = (byte) 0x08;
        capk_pobc_08.HashInd = (byte) 0x01;
        capk_pobc_08.ArithInd = (byte) 0x01;
        capk_pobc_08.Modul = new byte[]{
                (byte) 0xB6, (byte) 0x16, (byte) 0x45, (byte) 0xED, (byte) 0xFD, (byte) 0x54, (byte) 0x98, (byte) 0xFB, (byte)
                0x24, (byte) 0x64, (byte) 0x44, (byte) 0x03, (byte) 0x7A, (byte) 0x0F, (byte) 0xA1, (byte) 0x8C, (byte)
                0x0F, (byte) 0x10, (byte) 0x1E, (byte) 0xBD, (byte) 0x8E, (byte) 0xFA, (byte) 0x54, (byte) 0x57, (byte)
                0x3C, (byte) 0xE6, (byte) 0xE6, (byte) 0xA7, (byte) 0xFB, (byte) 0xF6, (byte) 0x3E, (byte) 0xD2, (byte)
                0x1D, (byte) 0x66, (byte) 0x34, (byte) 0x08, (byte) 0x52, (byte) 0xB0, (byte) 0x21, (byte) 0x1C, (byte)
                0xF5, (byte) 0xEE, (byte) 0xF6, (byte) 0xA1, (byte) 0xCD, (byte) 0x98, (byte) 0x9F, (byte) 0x66, (byte)
                0xAF, (byte) 0x21, (byte) 0xA8, (byte) 0xEB, (byte) 0x19, (byte) 0xDB, (byte) 0xD8, (byte) 0xDB, (byte)
                0xC3, (byte) 0x70, (byte) 0x6D, (byte) 0x13, (byte) 0x53, (byte) 0x63, (byte) 0xA0, (byte) 0xD6, (byte)
                0x83, (byte) 0xD0, (byte) 0x46, (byte) 0x30, (byte) 0x4F, (byte) 0x5A, (byte) 0x83, (byte) 0x6B, (byte)
                0xC1, (byte) 0xBC, (byte) 0x63, (byte) 0x28, (byte) 0x21, (byte) 0xAF, (byte) 0xE7, (byte) 0xA2, (byte)
                0xF7, (byte) 0x5D, (byte) 0xA3, (byte) 0xC5, (byte) 0x0A, (byte) 0xC7, (byte) 0x4C, (byte) 0x54, (byte)
                0x5A, (byte) 0x75, (byte) 0x45, (byte) 0x62, (byte) 0x20, (byte) 0x41, (byte) 0x37, (byte) 0x16, (byte)
                0x96, (byte) 0x63, (byte) 0xCF, (byte) 0xCC, (byte) 0x0B, (byte) 0x06, (byte) 0xE6, (byte) 0x7E, (byte)
                0x21, (byte) 0x09, (byte) 0xEB, (byte) 0xA4, (byte) 0x1B, (byte) 0xC6, (byte) 0x7F, (byte) 0xF2, (byte)
                0x0C, (byte) 0xC8, (byte) 0xAC, (byte) 0x80, (byte) 0xD7, (byte) 0xB6, (byte) 0xEE, (byte) 0x1A, (byte)
                0x95, (byte) 0x46, (byte) 0x5B, (byte) 0x3B, (byte) 0x26, (byte) 0x57, (byte) 0x53, (byte) 0x3E, (byte)
                0xA5, (byte) 0x6D, (byte) 0x92, (byte) 0xD5, (byte) 0x39, (byte) 0xE5, (byte) 0x06, (byte) 0x43, (byte)
                0x60, (byte) 0xEA, (byte) 0x48, (byte) 0x50, (byte) 0xFE, (byte) 0xD2, (byte) 0xD1, (byte) 0xBF
        };
        capk_pobc_08.Exponent = new byte[]{0x03};
        capk_pobc_08.ExpDate = new byte[]{0x15, 0x12, 0x31};
        capk_pobc_08.CheckSum = new byte[]{
                (byte) 0xCC, (byte) 0x95, (byte) 0x85, (byte) 0xE8, (byte) 0xE6, (byte) 0x37, (byte) 0x19, (byte) 0x1C, (byte) 0x10, (byte) 0xFC, (byte) 0xEC, (byte) 0xB3, (byte) 0x2B, (byte) 0x5A, (byte) 0xE1, (byte) 0xB9, (byte) 0xD4, (byte) 0x10, (byte) 0xB5, (byte) 0x2D
        };

        result = com.telpo.emv.EmvService.Emv_AddCapk(capk_pobc_08);
        com.telpo.base.util.MLog.i("Add CAPK capk_pobc_08:" + result + " ID:" + capk_pobc_08.KeyID);


/*----------------------------------------------------------------------- division line-----------------------------------------------------------------------------------------*/

        com.telpo.emv.EmvCAPK capk_pobc_09 = new com.telpo.emv.EmvCAPK();
        capk_pobc_09.RID = new byte[]{(byte) 0xA0, (byte) 0x00, (byte) 0x00, (byte) 0x03, (byte) 0x33};
        capk_pobc_09.KeyID = (byte) 0x08;
        capk_pobc_09.HashInd = (byte) 0x01;
        capk_pobc_09.ArithInd = (byte) 0x01;
        capk_pobc_09.Modul = new byte[]{
                (byte) 0xEB, (byte) 0x37, (byte) 0x4D, (byte) 0xFC, (byte) 0x5A, (byte) 0x96, (byte) 0xB7, (byte) 0x1D, (byte)
                0x28, (byte) 0x63, (byte) 0x87, (byte) 0x5E, (byte) 0xDA, (byte) 0x2E, (byte) 0xAF, (byte) 0xB9, (byte)
                0x6B, (byte) 0x1B, (byte) 0x43, (byte) 0x9D, (byte) 0x3E, (byte) 0xCE, (byte) 0x0B, (byte) 0x18, (byte)
                0x26, (byte) 0xA2, (byte) 0x67, (byte) 0x2E, (byte) 0xEE, (byte) 0xFA, (byte) 0x79, (byte) 0x90, (byte)
                0x28, (byte) 0x67, (byte) 0x76, (byte) 0xF8, (byte) 0xBD, (byte) 0x98, (byte) 0x9A, (byte) 0x15, (byte)
                0x14, (byte) 0x1A, (byte) 0x75, (byte) 0xC3, (byte) 0x84, (byte) 0xDF, (byte) 0xC1, (byte) 0x4F, (byte)
                0xEF, (byte) 0x92, (byte) 0x43, (byte) 0xAA, (byte) 0xB3, (byte) 0x27, (byte) 0x07, (byte) 0x65, (byte)
                0x9B, (byte) 0xE9, (byte) 0xE4, (byte) 0x79, (byte) 0x7A, (byte) 0x24, (byte) 0x7C, (byte) 0x2F, (byte)
                0x0B, (byte) 0x6D, (byte) 0x99, (byte) 0x37, (byte) 0x2F, (byte) 0x38, (byte) 0x4A, (byte) 0xF6, (byte)
                0x2F, (byte) 0xE2, (byte) 0x3B, (byte) 0xC5, (byte) 0x4B, (byte) 0xCD, (byte) 0xC5, (byte) 0x7A, (byte)
                0x9A, (byte) 0xCD, (byte) 0x1D, (byte) 0x55, (byte) 0x85, (byte) 0xC3, (byte) 0x03, (byte) 0xF2, (byte)
                0x01, (byte) 0xEF, (byte) 0x4E, (byte) 0x8B, (byte) 0x80, (byte) 0x6A, (byte) 0xFB, (byte) 0x80, (byte)
                0x9D, (byte) 0xB1, (byte) 0xA3, (byte) 0xDB, (byte) 0x1C, (byte) 0xD1, (byte) 0x12, (byte) 0xAC, (byte)
                0x88, (byte) 0x4F, (byte) 0x16, (byte) 0x4A, (byte) 0x67, (byte) 0xB9, (byte) 0x9C, (byte) 0x7D, (byte)
                0x6E, (byte) 0x5A, (byte) 0x8A, (byte) 0x6D, (byte) 0xF1, (byte) 0xD3, (byte) 0xCA, (byte) 0xE6, (byte)
                0xD7, (byte) 0xED, (byte) 0x3D, (byte) 0x5B, (byte) 0xE7, (byte) 0x25, (byte) 0xB2, (byte) 0xDE, (byte)
                0x4A, (byte) 0xDE, (byte) 0x23, (byte) 0xFA, (byte) 0x67, (byte) 0x9B, (byte) 0xF4, (byte) 0xEB, (byte)
                0x15, (byte) 0xA9, (byte) 0x3D, (byte) 0x8A, (byte) 0x6E, (byte) 0x29, (byte) 0xC7, (byte) 0xFF, (byte)
                0xA1, (byte) 0xA7, (byte) 0x0D, (byte) 0xE2, (byte) 0xE5, (byte) 0x4F, (byte) 0x59, (byte) 0x3D, (byte)
                0x90, (byte) 0x8A, (byte) 0x3B, (byte) 0xF9, (byte) 0xEB, (byte) 0xBD, (byte) 0x76, (byte) 0x0B, (byte)
                0xBF, (byte) 0xDC, (byte) 0x8D, (byte) 0xB8, (byte) 0xB5, (byte) 0x44, (byte) 0x97, (byte) 0xE6, (byte)
                0xC5, (byte) 0xBE, (byte) 0x0E, (byte) 0x4A, (byte) 0x4D, (byte) 0xAC, (byte) 0x29, (byte) 0xE5
        };
        capk_pobc_09.Exponent = new byte[]{0x03};
        capk_pobc_09.ExpDate = new byte[]{0x15, 0x12, 0x31};
        capk_pobc_09.CheckSum = new byte[]{
                (byte) 0xCC, (byte) 0x95, (byte) 0x85, (byte) 0xE8, (byte) 0xE6, (byte) 0x37, (byte) 0x19, (byte) 0x1C, (byte) 0x10, (byte) 0xFC, (byte) 0xEC, (byte) 0xB3, (byte) 0x2B, (byte) 0x5A, (byte) 0xE1, (byte) 0xB9, (byte) 0xD4, (byte) 0x10, (byte) 0xB5, (byte) 0x2D
        };

        result = com.telpo.emv.EmvService.Emv_AddCapk(capk_pobc_09);
        com.telpo.base.util.MLog.i("Add CAPK capk_pobc_09:" + result + " ID:" + capk_pobc_09.KeyID);


/*----------------------------------------------------------------------- division line-----------------------------------------------------------------------------------------*/

        com.telpo.emv.EmvCAPK capk_pobc_0B = new com.telpo.emv.EmvCAPK();
        capk_pobc_0B.RID = new byte[]{(byte) 0xA0, (byte) 0x00, (byte) 0x00, (byte) 0x03, (byte) 0x33};
        capk_pobc_0B.KeyID = (byte) 0x0B;
        capk_pobc_0B.HashInd = (byte) 0x01;
        capk_pobc_0B.ArithInd = (byte) 0x01;
        capk_pobc_0B.Modul = new byte[]{
                (byte) 0xCF, (byte) 0x9F, (byte) 0xDF, (byte) 0x46, (byte) 0xB3, (byte) 0x56, (byte) 0x37, (byte) 0x8E, (byte)
                0x9A, (byte) 0xF3, (byte) 0x11, (byte) 0xB0, (byte) 0xF9, (byte) 0x81, (byte) 0xB2, (byte) 0x1A, (byte)
                0x1F, (byte) 0x22, (byte) 0xF2, (byte) 0x50, (byte) 0xFB, (byte) 0x11, (byte) 0xF5, (byte) 0x5C, (byte)
                0x95, (byte) 0x87, (byte) 0x09, (byte) 0xE3, (byte) 0xC7, (byte) 0x24, (byte) 0x19, (byte) 0x18, (byte)
                0x29, (byte) 0x34, (byte) 0x83, (byte) 0x28, (byte) 0x9E, (byte) 0xAE, (byte) 0x68, (byte) 0x8A, (byte)
                0x09, (byte) 0x4C, (byte) 0x02, (byte) 0xC3, (byte) 0x44, (byte) 0xE2, (byte) 0x99, (byte) 0x9F, (byte)
                0x31, (byte) 0x5A, (byte) 0x72, (byte) 0x84, (byte) 0x1F, (byte) 0x48, (byte) 0x9E, (byte) 0x24, (byte)
                0xB1, (byte) 0xBA, (byte) 0x00, (byte) 0x56, (byte) 0xCF, (byte) 0xAB, (byte) 0x3B, (byte) 0x47, (byte)
                0x9D, (byte) 0x0E, (byte) 0x82, (byte) 0x64, (byte) 0x52, (byte) 0x37, (byte) 0x5D, (byte) 0xCD, (byte)
                0xBB, (byte) 0x67, (byte) 0xE9, (byte) 0x7E, (byte) 0xC2, (byte) 0xAA, (byte) 0x66, (byte) 0xF4, (byte)
                0x60, (byte) 0x1D, (byte) 0x77, (byte) 0x4F, (byte) 0xEA, (byte) 0xEF, (byte) 0x77, (byte) 0x5A, (byte)
                0xCC, (byte) 0xC6, (byte) 0x21, (byte) 0xBF, (byte) 0xEB, (byte) 0x65, (byte) 0xFB, (byte) 0x00, (byte)
                0x53, (byte) 0xFC, (byte) 0x5F, (byte) 0x39, (byte) 0x2A, (byte) 0xA5, (byte) 0xE1, (byte) 0xD4, (byte)
                0xC4, (byte) 0x1A, (byte) 0x4D, (byte) 0xE9, (byte) 0xFF, (byte) 0xDF, (byte) 0xDF, (byte) 0x13, (byte)
                0x27, (byte) 0xC4, (byte) 0xBB, (byte) 0x87, (byte) 0x4F, (byte) 0x1F, (byte) 0x63, (byte) 0xA5, (byte)
                0x99, (byte) 0xEE, (byte) 0x39, (byte) 0x02, (byte) 0xFE, (byte) 0x95, (byte) 0xE7, (byte) 0x29, (byte)
                0xFD, (byte) 0x78, (byte) 0xD4, (byte) 0x23, (byte) 0x4D, (byte) 0xC7, (byte) 0xE6, (byte) 0xCF, (byte)
                0x1A, (byte) 0xBA, (byte) 0xBA, (byte) 0xA3, (byte) 0xF6, (byte) 0xDB, (byte) 0x29, (byte) 0xB7, (byte)
                0xF0, (byte) 0x5D, (byte) 0x1D, (byte) 0x90, (byte) 0x1D, (byte) 0x2E, (byte) 0x76, (byte) 0xA6, (byte)
                0x06, (byte) 0xA8, (byte) 0xCB, (byte) 0xFF, (byte) 0xFF, (byte) 0xEC, (byte) 0xBD, (byte) 0x91, (byte)
                0x8F, (byte) 0xA2, (byte) 0xD2, (byte) 0x78, (byte) 0xBD, (byte) 0xB4, (byte) 0x3B, (byte) 0x04, (byte)
                0x34, (byte) 0xF5, (byte) 0xD4, (byte) 0x51, (byte) 0x34, (byte) 0xBE, (byte) 0x1C, (byte) 0x27, (byte)
                0x81, (byte) 0xD1, (byte) 0x57, (byte) 0xD5, (byte) 0x01, (byte) 0xFF, (byte) 0x43, (byte) 0xE5, (byte)
                0xF1, (byte) 0xC4, (byte) 0x70, (byte) 0x96, (byte) 0x7C, (byte) 0xD5, (byte) 0x7C, (byte) 0xE5, (byte)
                0x3B, (byte) 0x64, (byte) 0xD8, (byte) 0x29, (byte) 0x74, (byte) 0xC8, (byte) 0x27, (byte) 0x59, (byte)
                0x37, (byte) 0xC5, (byte) 0xD8, (byte) 0x50, (byte) 0x2A, (byte) 0x12, (byte) 0x52, (byte) 0xA8, (byte)
                0xA5, (byte) 0xD6, (byte) 0x08, (byte) 0x8A, (byte) 0x25, (byte) 0x9B, (byte) 0x69, (byte) 0x4F, (byte)
                0x98, (byte) 0x64, (byte) 0x8D, (byte) 0x9A, (byte) 0xF2, (byte) 0xCB, (byte) 0x0E, (byte) 0xFD, (byte)
                0x9D, (byte) 0x94, (byte) 0x3C, (byte) 0x69, (byte) 0xF8, (byte) 0x96, (byte) 0xD4, (byte) 0x9F, (byte)
                0xA3, (byte) 0x97, (byte) 0x02, (byte) 0x16, (byte) 0x2A, (byte) 0xCB, (byte) 0x5A, (byte) 0xF2, (byte)
                0x9B, (byte) 0x90, (byte) 0xBA, (byte) 0xDE, (byte) 0x00, (byte) 0x5B, (byte) 0xC1, (byte) 0x57
        };
        capk_pobc_0B.Exponent = new byte[]{0x03};
        capk_pobc_0B.ExpDate = new byte[]{0x15, 0x12, 0x31};
        capk_pobc_0B.CheckSum = new byte[]{
                (byte) 0xCC, (byte) 0x95, (byte) 0x85, (byte) 0xE8, (byte) 0xE6, (byte) 0x37, (byte) 0x19, (byte) 0x1C, (byte) 0x10, (byte) 0xFC, (byte) 0xEC, (byte) 0xB3, (byte) 0x2B, (byte) 0x5A, (byte) 0xE1, (byte) 0xB9, (byte) 0xD4, (byte) 0x10, (byte) 0xB5, (byte) 0x2D
        };

        result = com.telpo.emv.EmvService.Emv_AddCapk(capk_pobc_0B);
        com.telpo.base.util.MLog.i("Add CAPK capk_pobc_0B:" + result + " ID:" + capk_pobc_0B.KeyID);


/*----------------------------------------------------------------------- division line-----------------------------------------------------------------------------------------*/

        com.telpo.emv.EmvCAPK capk_pobc_84 = new com.telpo.emv.EmvCAPK();
        capk_pobc_84.RID = new byte[]{(byte) 0xA0, (byte) 0x00, (byte) 0x00, (byte) 0x03, (byte) 0x33};
        capk_pobc_84.KeyID = (byte) 0x84;
        capk_pobc_84.HashInd = (byte) 0x01;
        capk_pobc_84.ArithInd = (byte) 0x01;
        capk_pobc_84.Modul = new byte[]{
                (byte) 0xF9, (byte) 0xEA, (byte) 0x55, (byte) 0x03, (byte) 0xCF, (byte) 0xE4, (byte) 0x30, (byte) 0x38, (byte)
                0x59, (byte) 0x6C, (byte) 0x72, (byte) 0x06, (byte) 0x45, (byte) 0xA9, (byte) 0x4E, (byte) 0x01, (byte)
                0x54, (byte) 0x79, (byte) 0x3D, (byte) 0xE7, (byte) 0x3A, (byte) 0xE5, (byte) 0xA9, (byte) 0x35, (byte)
                0xD1, (byte) 0xFB, (byte) 0x9D, (byte) 0x0F, (byte) 0xE7, (byte) 0x72, (byte) 0x86, (byte) 0xB6, (byte)
                0x12, (byte) 0x61, (byte) 0xE3, (byte) 0xBB, (byte) 0x1D, (byte) 0x3D, (byte) 0xFE, (byte) 0xC5, (byte)
                0x47, (byte) 0x44, (byte) 0x99, (byte) 0x92, (byte) 0xE2, (byte) 0x03, (byte) 0x7C, (byte) 0x01, (byte)
                0xFF, (byte) 0x4E, (byte) 0xFB, (byte) 0x88, (byte) 0xDA, (byte) 0x8A, (byte) 0x82, (byte) 0xF3, (byte)
                0x0F, (byte) 0xEA, (byte) 0x31, (byte) 0x98, (byte) 0xD5, (byte) 0xD1, (byte) 0x67, (byte) 0x54, (byte)
                0x24, (byte) 0x7A, (byte) 0x16, (byte) 0x26, (byte) 0xE9, (byte) 0xCF, (byte) 0xFB, (byte) 0x4C, (byte)
                0xD9, (byte) 0xE3, (byte) 0x13, (byte) 0x99, (byte) 0x99, (byte) 0x0E, (byte) 0x43, (byte) 0xFC, (byte)
                0xA7, (byte) 0x7C, (byte) 0x74, (byte) 0x4A, (byte) 0x93, (byte) 0x68, (byte) 0x5A, (byte) 0x26, (byte)
                0x0A, (byte) 0x20, (byte) 0xE6, (byte) 0xA6, (byte) 0x07, (byte) 0xF3, (byte) 0xEE, (byte) 0x3F, (byte)
                0xAE, (byte) 0x2A, (byte) 0xBB, (byte) 0xE9, (byte) 0x96, (byte) 0x78, (byte) 0xC9, (byte) 0xF1, (byte)
                0x9D, (byte) 0xFD, (byte) 0x2D, (byte) 0x8E, (byte) 0xA7, (byte) 0x67, (byte) 0x89, (byte) 0x23, (byte)
                0x9D, (byte) 0x13, (byte) 0x36, (byte) 0x9D, (byte) 0x7D, (byte) 0x2D, (byte) 0x56, (byte) 0xAF, (byte)
                0x3F, (byte) 0x27, (byte) 0x93, (byte) 0x06, (byte) 0x89, (byte) 0x50, (byte) 0xB5, (byte) 0xBD, (byte)
                0x80, (byte) 0x8C, (byte) 0x46, (byte) 0x25, (byte) 0x71, (byte) 0x66, (byte) 0x2D, (byte) 0x43, (byte)
                0x64, (byte) 0xB3, (byte) 0x0A, (byte) 0x25, (byte) 0x82, (byte) 0x95, (byte) 0x9D, (byte) 0xB2, (byte)
                0x38, (byte) 0x33, (byte) 0x3B, (byte) 0xAD, (byte) 0xAC, (byte) 0xB4, (byte) 0x42, (byte) 0xF9, (byte)
                0x51, (byte) 0x6B, (byte) 0x5C, (byte) 0x33, (byte) 0x6C, (byte) 0x8A, (byte) 0x61, (byte) 0x3F, (byte)
                0xE0, (byte) 0x14, (byte) 0xB7, (byte) 0xD7, (byte) 0x73, (byte) 0x58, (byte) 0x1A, (byte) 0xE1, (byte)
                0x0F, (byte) 0xDF, (byte) 0x7B, (byte) 0xDB, (byte) 0x26, (byte) 0x69, (byte) 0x01, (byte) 0x2D
        };
        capk_pobc_84.Exponent = new byte[]{0x03};
        capk_pobc_84.ExpDate = new byte[]{0x15, 0x12, 0x31};
        capk_pobc_84.CheckSum = new byte[]{
                (byte) 0xCC, (byte) 0x95, (byte) 0x85, (byte) 0xE8, (byte) 0xE6, (byte) 0x37, (byte) 0x19, (byte) 0x1C, (byte) 0x10, (byte) 0xFC, (byte) 0xEC, (byte) 0xB3, (byte) 0x2B, (byte) 0x5A, (byte) 0xE1, (byte) 0xB9, (byte) 0xD4, (byte) 0x10, (byte) 0xB5, (byte) 0x2D
        };

        result = com.telpo.emv.EmvService.Emv_AddCapk(capk_pobc_84);
        com.telpo.base.util.MLog.i("Add CAPK capk_pobc_84:" + result + " ID:" + capk_pobc_84.KeyID);


/*----------------------------------------------------------------------- division line-----------------------------------------------------------------------------------------*/

        com.telpo.emv.EmvCAPK capk_pobc_85 = new com.telpo.emv.EmvCAPK();
        capk_pobc_85.RID = new byte[]{(byte) 0xA0, (byte) 0x00, (byte) 0x00, (byte) 0x03, (byte) 0x33};
        capk_pobc_85.KeyID = (byte) 0x85;
        capk_pobc_85.HashInd = (byte) 0x01;
        capk_pobc_85.ArithInd = (byte) 0x01;
        capk_pobc_85.Modul = new byte[]{
                (byte) 0xC9, (byte) 0x24, (byte) 0x2E, (byte) 0xC6, (byte) 0x03, (byte) 0x0F, (byte) 0x10, (byte) 0xE5, (byte)
                0x22, (byte) 0x5E, (byte) 0x72, (byte) 0x2A, (byte) 0xA1, (byte) 0x7D, (byte) 0x9D, (byte) 0xC8, (byte)
                0x94, (byte) 0x29, (byte) 0x92, (byte) 0x33, (byte) 0xAE, (byte) 0xC3, (byte) 0x21, (byte) 0x9B, (byte)
                0x95, (byte) 0x0D, (byte) 0x4F, (byte) 0x24, (byte) 0x3A, (byte) 0xF5, (byte) 0x30, (byte) 0xFA, (byte)
                0x13, (byte) 0xE3, (byte) 0xA3, (byte) 0x1A, (byte) 0xFA, (byte) 0xA0, (byte) 0xD4, (byte) 0xBF, (byte)
                0x4D, (byte) 0xE5, (byte) 0x62, (byte) 0xB6, (byte) 0xB4, (byte) 0xC3, (byte) 0x10, (byte) 0x8A, (byte)
                0xEB, (byte) 0xBC, (byte) 0x6C, (byte) 0xB0, (byte) 0x80, (byte) 0xF9, (byte) 0x07, (byte) 0x70, (byte)
                0xD5, (byte) 0x32, (byte) 0xF2, (byte) 0x41, (byte) 0xBC, (byte) 0x15, (byte) 0x36, (byte) 0x40, (byte)
                0x1E, (byte) 0x1B, (byte) 0xF7, (byte) 0x2F, (byte) 0x9D, (byte) 0xC1, (byte) 0xB0, (byte) 0x89, (byte)
                0x33, (byte) 0xB9, (byte) 0xBF, (byte) 0x77, (byte) 0x40, (byte) 0x3F, (byte) 0x6A, (byte) 0x0F, (byte)
                0xB5, (byte) 0x77, (byte) 0x7B, (byte) 0xAA, (byte) 0x4C, (byte) 0x9B, (byte) 0xE9, (byte) 0x15, (byte)
                0x74, (byte) 0xBB, (byte) 0xBF, (byte) 0xB5, (byte) 0x21, (byte) 0x34, (byte) 0x2A, (byte) 0x20, (byte)
                0x38, (byte) 0x67, (byte) 0x90, (byte) 0x51, (byte) 0x22, (byte) 0x21, (byte) 0xF4, (byte) 0x77, (byte)
                0xFB, (byte) 0xC5, (byte) 0x3F, (byte) 0xF1, (byte) 0xB6, (byte) 0x53, (byte) 0x3A, (byte) 0x01, (byte)
                0x58, (byte) 0x15, (byte) 0x43, (byte) 0x54, (byte) 0x10, (byte) 0xEC, (byte) 0x27, (byte) 0x2F, (byte)
                0x0A, (byte) 0x34, (byte) 0xEA, (byte) 0x07, (byte) 0x35, (byte) 0xC4, (byte) 0x39, (byte) 0x67, (byte)
                0x7D, (byte) 0x7E, (byte) 0x46, (byte) 0xFB, (byte) 0xA7, (byte) 0x66, (byte) 0xEC, (byte) 0x00, (byte)
                0xCE, (byte) 0xD5, (byte) 0x9B, (byte) 0x67, (byte) 0x15, (byte) 0xE3, (byte) 0x41, (byte) 0x2D, (byte)
                0x6F, (byte) 0xB8, (byte) 0xA9, (byte) 0x34, (byte) 0xBF, (byte) 0x9D, (byte) 0x14, (byte) 0x97, (byte)
                0xA2, (byte) 0x4A, (byte) 0x62, (byte) 0x52, (byte) 0xC5, (byte) 0x2D, (byte) 0x75, (byte) 0x86, (byte)
                0xFD, (byte) 0x66, (byte) 0xA4, (byte) 0x50, (byte) 0xFB, (byte) 0x5D, (byte) 0x2B, (byte) 0x44, (byte)
                0x84, (byte) 0xEC, (byte) 0x92, (byte) 0x30, (byte) 0x61, (byte) 0x43, (byte) 0x96, (byte) 0x22, (byte)
                0xBC, (byte) 0x05, (byte) 0x35, (byte) 0x31, (byte) 0x6C, (byte) 0xD4, (byte) 0x23, (byte) 0x1C, (byte)
                0x13, (byte) 0xC6, (byte) 0x27, (byte) 0xBF, (byte) 0x4D, (byte) 0x2E, (byte) 0xDE, (byte) 0x1C, (byte)
                0x02, (byte) 0xC8, (byte) 0x02, (byte) 0x46, (byte) 0x46, (byte) 0x58, (byte) 0xF1, (byte) 0xB9, (byte)
                0xD7, (byte) 0xFF, (byte) 0x23, (byte) 0xA3, (byte) 0x69, (byte) 0x85, (byte) 0x10, (byte) 0xFA, (byte)
                0x90, (byte) 0xD0, (byte) 0xC3, (byte) 0x16, (byte) 0x49, (byte) 0x42, (byte) 0xFB, (byte) 0x35, (byte)
                0x92, (byte) 0x55, (byte) 0xCD, (byte) 0x82, (byte) 0x3C, (byte) 0xB2, (byte) 0x63, (byte) 0x5B, (byte)
                0x3F, (byte) 0x16, (byte) 0x7F, (byte) 0xBD, (byte) 0xFC, (byte) 0x90, (byte) 0x06, (byte) 0x41, (byte)
                0xB9, (byte) 0x70, (byte) 0xD6, (byte) 0x02, (byte) 0xA2, (byte) 0x77, (byte) 0x1A, (byte) 0x7F, (byte)
                0x4F, (byte) 0x94, (byte) 0xDF, (byte) 0x6D, (byte) 0x34, (byte) 0xBE, (byte) 0x8B, (byte) 0xBB
        };
        capk_pobc_85.Exponent = new byte[]{0x03};
        capk_pobc_85.ExpDate = new byte[]{0x15, 0x12, 0x31};
        capk_pobc_85.CheckSum = new byte[]{
                (byte) 0xCC, (byte) 0x95, (byte) 0x85, (byte) 0xE8, (byte) 0xE6, (byte) 0x37, (byte) 0x19, (byte) 0x1C, (byte) 0x10, (byte) 0xFC, (byte) 0xEC, (byte) 0xB3, (byte) 0x2B, (byte) 0x5A, (byte) 0xE1, (byte) 0xB9, (byte) 0xD4, (byte) 0x10, (byte) 0xB5, (byte) 0x2D
        };

        result = com.telpo.emv.EmvService.Emv_AddCapk(capk_pobc_85);
        com.telpo.base.util.MLog.i("Add CAPK capk_pobc_85:" + result + " ID:" + capk_pobc_85.KeyID);
/*----------------------------------------------------------------------- division line-----------------------------------------------------------------------------------------*/
    }

    //21号文 59域 A2
    //设备类型 01 AN2
    //终端硬件序列号 02 ANS...50
    //加密随机因子  03 AN...10
    //硬件序列号密文数据 04 B64
    //应用版本序列号 05  ANS8
    //strPan 是卡号
    public static String get_TlvA2(android.content.Context context, String strPan) {
        String ret = "";
        String deviceType = "04";//2位设备类型
        com.telpo.emv.util.TLVData tlvData = new com.telpo.emv.util.TLVData();
        tlvData.Tag = 01;
        tlvData.Value = deviceType.getBytes();
        tlvData.Len = tlvData.Value.length;
        //ret = StringUtil.bytesToHexString(TLVData.generateTLVArray(tlvData));
        String factoryNo = "000038";//6位厂商号
        ret = "30313030323034";
        //设备序列号自定义部分是机器的IMEI码，当有两个IMEI码时，比大小，小的在前，大的在后
        com.telpo.pospay.main.util.CTelephoneInfo cTelephoneInfo = com.telpo.pospay.main.util.CTelephoneInfo.getInstance(context);
        cTelephoneInfo.setCTelephoneInfo();
        String strIMEI2 = cTelephoneInfo.getImeiSIM1();
        String strIMEI1 = cTelephoneInfo.getImeiSIM2();
        String seriel = com.telpo.pospay.main.util.TPPOSUtil.getSN();
//        if (strIMEI1.compareTo(strIMEI2)==0 ){
//            seriel = strIMEI1;
//        }else if (strIMEI1.compareTo(strIMEI2)<0){
//            seriel = strIMEI1 + strIMEI2;
//        }else {
//            seriel = strIMEI2 + strIMEI1;
//        }
//        if (TextUtils.isEmpty(strIMEI1)||strIMEI1.length()<strIMEI2.length()){
//            seriel = strIMEI2;
//        }
//        if (TextUtils.isEmpty(strIMEI2)||strIMEI2.length()<strIMEI1.length()){
//            seriel = strIMEI1;
//        }
        com.telpo.base.util.MLog.i("SN " + seriel);

        //终端硬件序列号 由三部分组成：
        tlvData.Tag = 02;
        tlvData.Value = (seriel).getBytes();
        tlvData.Len = tlvData.Value.length;
        //ret += StringUtil.bytesToHexString(TLVData.generateTLVArray(tlvData));
        ret += ("3032" + com.telpo.emv.util.StringUtil.bytesToHexString(String.format("%03d", tlvData.Len).getBytes()) + com.telpo.emv.util.StringUtil.bytesToHexString(com.telpo.emv.util.TLVData.generateTLVArray(tlvData)).substring(4));
        tlvData.Tag = 03;
        tlvData.Value = strPan.substring(strPan.length() - 6, strPan.length()).getBytes();
        tlvData.Len = tlvData.Value.length;
        // ret += StringUtil.bytesToHexString(TLVData.generateTLVArray(tlvData));
        ret += ("3033" + com.telpo.emv.util.StringUtil.bytesToHexString(String.format("%03d", tlvData.Len).getBytes()) + com.telpo.emv.util.StringUtil.bytesToHexString(com.telpo.emv.util.TLVData.generateTLVArray(tlvData)).substring(4));
        String strTerminalEncryptData = seriel + strPan.substring(strPan.length() - 6, strPan.length());
        try {
            String sSnEncryptDataMD5 = com.telpo.emv.util.StringUtil.bytesToHexString(java.security.MessageDigest.getInstance("MD5").digest((seriel).getBytes())).toUpperCase();
            com.telpo.base.util.MLog.i("strTerminalEncryptData :" + strTerminalEncryptData);
            com.telpo.base.util.MLog.i("sSnEncryptDataMD5 :" + sSnEncryptDataMD5);

            byte[] snkey = com.telpo.emv.util.StringUtil.hexStringToByte(sSnEncryptDataMD5);
            int nret = com.telpo.pinpad.PinpadService.TP_PinpadWriteSNKey(snkey);
            com.telpo.base.util.MLog.i("TP_PinpadWriteSNKey :" + nret);
            byte[] indata = strTerminalEncryptData.getBytes();
            com.telpo.base.util.MLog.i("indata :" + com.telpo.emv.util.StringUtil.bytesToHexString(indata));
            byte[] outdata = new byte[8];
            nret = com.telpo.pinpad.PinpadService.TP_PinpadGetSNEncryptData(indata, outdata);
            com.telpo.base.util.MLog.i("TP_PinpadGetSNEncryptData :" + nret);
            com.telpo.base.util.MLog.i("outdata 000:" + com.telpo.emv.util.StringUtil.bytesToHexString(outdata));
            tlvData.Tag = 04;
            tlvData.Value = outdata;
            tlvData.Len = tlvData.Value.length;
            // ret += StringUtil.bytesToHexString(TLVData.generateTLVArray(tlvData));
        } catch (java.security.NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        ret += ("3034" + com.telpo.emv.util.StringUtil.bytesToHexString(String.format("%03d", tlvData.Len).getBytes()) + com.telpo.emv.util.StringUtil.bytesToHexString(com.telpo.emv.util.TLVData.generateTLVArray(tlvData)).substring(4));
        tlvData.Tag = 05;
        tlvData.Value = new byte[]{0x38, 0x31, 0x39, 0x39, 0x31, 0x36, 0x20, 0x20};
        tlvData.Len = tlvData.Value.length;

        //ret += StringUtil.bytesToHexString(TLVData.generateTLVArray(tlvData));
        ret += ("3035" + com.telpo.emv.util.StringUtil.bytesToHexString(String.format("%03d", tlvData.Len).getBytes()) + com.telpo.emv.util.StringUtil.bytesToHexString(com.telpo.emv.util.TLVData.generateTLVArray(tlvData)).substring(4));
        tlvData.Tag = 0xA2;
        tlvData.Value = com.telpo.emv.util.StringUtil.hexStringToByte(ret);
        tlvData.Len = tlvData.Value.length;
        // ret = StringUtil.bytesToHexString(TLVData.generateTLVArray(tlvData));
        ret = "4132" + com.telpo.emv.util.StringUtil.bytesToHexString(String.format("%03d", tlvData.Value.length).getBytes()) + ret;
        com.telpo.base.util.MLog.i("out :" + ret);
        return ret;
    }

    /**
     * 通过so库的方法获取21号文得57域
     *
     * @param context
     * @param strPan
     * @return
     */
//    public static String get_TlvA2_by_so(android.content.Context context, String strPan) {
//        Field57Data field57Data = new Field57Data();
//        int ret = MasterKeyTool.getTwentyFirstMsg(get_terminalSN(context), strPan.substring(strPan.length() - 6, strPan.length()), "819916", field57Data.outdata);
//        com.telpo.base.util.MLog.i("ret :" + ret);
//        if (ret > 0) {
//            String out = com.telpo.emv.util.StringUtil.bytesToHexString(com.telpo.emv.util.ByteArrayUtil.byteArrayGetHead(field57Data.outdata, ret));
//            com.telpo.base.util.MLog.i("out:" + out);
//            return out;
//        } else {
//            return null;
//        }
//    }


}
