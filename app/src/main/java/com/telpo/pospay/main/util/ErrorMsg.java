package com.telpo.pospay.main.util;

import static com.telpo.pospay.main.util.TPPOSUtil.ERR_DOWNMASTERKEY;
import static com.telpo.pospay.main.util.TPPOSUtil.ERR_DOWNMASTERKEY_CHECKMASTERKEY;
import static com.telpo.pospay.main.util.TPPOSUtil.ERR_DOWNMASTERKEY_CHECKTRANSKEY;

/**
 * Created by Telpo on 2018/1/12.
 * 输入startTransactionProcess 返回的错误码和平台返回的响应码 返回具体说明
 */

public class ErrorMsg {
    /**
     * @param errorCode    startTransactionProcess 返回的错误码
     * @param responseCode 平台返回的响应码
     * @return
     */
    public static String getErrorMsgInf(int errorCode, String responseCode) {
        String str = "";
        return str;
    }


    /***
     * @param errorCode 返回的错误码
     * @return
     */
    public static String getErrorCodeMsgInf(int errorCode) {
        String strErrMsg = "";
        switch (errorCode) {
            //操作员相关
            case TPPOSUtil.ERROR_PROHIBIT_ICCARD:
                strErrMsg = "pos禁止IC卡交易";
                break;
            case TPPOSUtil.OPERATOR_NO_EXIST:
                strErrMsg = "该操作员不存在";
                break;
            case TPPOSUtil.OPERATOR_PWD_ERR:
                strErrMsg = "操作员密码错误";
                break;
            case TPPOSUtil.OPERATOR_ID_INVALID:
                strErrMsg = "操作员号格式不合法";
                break;
            case TPPOSUtil.OPERATOR_PWD_INVALID:
                strErrMsg = "操作员密码不合法";
                break;
            case TPPOSUtil.OPERATOR_IS_EXIST:
                strErrMsg = "增加操作员时,操作员已存在";
                break;
            case TPPOSUtil.OPERATOR_ADD_ERR:
                strErrMsg = "增加操作员时失败";
                break;
            case TPPOSUtil.OPERATOR_DATABASE_ERR:
                strErrMsg = "操作员操作失败";
                break;
            //packData返回值定义:
            case TPPOSUtil.ERR_TRANSTYPE_UNKNOW:
                strErrMsg = "未知的交易类型";
                break;
            case TPPOSUtil.ERR_LACK_TERMINALID:
                strErrMsg = "缺少终端号";
                break;
            case TPPOSUtil.ERR_LACK_MERCHANTID:
                strErrMsg = "缺少商户号";
                break;
            case TPPOSUtil.ERR_LENGTH_TERMINALID:
                strErrMsg = "的终端号长度错误";
                break;
            case TPPOSUtil.ERR_LENGTH_TERMINALSN:
                strErrMsg = "终端序列号长度错误";
                break;

            case TPPOSUtil.ERR_LENGTH_MERCHANTID:
                strErrMsg = "商户号长度错误";
                break;
            case TPPOSUtil.ERR_INPUT_DATE_ERR:
                strErrMsg = "日期格式错误";
                break;
            case TPPOSUtil.ERR_INPUT_TIME_ERR:
                strErrMsg = "时间格式错误";
                break;
            case TPPOSUtil.ERR_DF27:
                strErrMsg = "AID_DF27有误";
                break;
            case TPPOSUtil.ERR_NO_NEED_UPDATE:
                strErrMsg = "参数已更新完毕";
                break;
            case TPPOSUtil.ERR_OPERATORID:
                strErrMsg = "操作员号错误";
                break;
            case TPPOSUtil.ERR_NO_AID_LIST:
                strErrMsg = "终端没有IC卡参数";
                break;
            case TPPOSUtil.ERR_NO_CAPK_LIST:
                strErrMsg = "终端没有IC卡公钥数据";
                break;
            case TPPOSUtil.ERR_NO_NEED_SETTLE:
                strErrMsg = "不存在交易记录";
                break;
            case TPPOSUtil.ERR_REVERSAL_MAX:
                strErrMsg = "冲正次数已达到最大";
                break;
            case TPPOSUtil.ERR_NO_NEED_REVERSAL:
                strErrMsg = "不需要冲正";
                break;
            case TPPOSUtil.ERR_NEED_REVERSAL:
                strErrMsg = "存在待冲正记录";
                break;
            case TPPOSUtil.ERR_NEED_SETTLE:
                strErrMsg = "交易达到上限,需要结算";
                break;
            case TPPOSUtil.ERR_PACK_UNKNOWERR:
                strErrMsg = "打包过程出现未知异常";
                break;
            case TPPOSUtil.ERR_VOID_NO_OLD_TRANCEID:
                strErrMsg = "未输入原流水号";
                break;
            case TPPOSUtil.ERR_VOID_NOT_FOUND_RECORD:
                strErrMsg = "未发现该交易记录";
                break;
            case TPPOSUtil.ERR_VOID_PAN_NOT_MATCH:
                strErrMsg = "交易卡号不符";
                break;
            case TPPOSUtil.ERR_VOID_TRANS_NOT_MATCH:
                strErrMsg = "找到原交易,交易类型不匹配";
                break;
            case TPPOSUtil.ERR_VOID_TRANS_HAS_VOID:
                strErrMsg = "找到原交易,发现原交易已撤销";
                break;
            case TPPOSUtil.ERR_REFUND_AMOUNT:
                strErrMsg = "需要输入退货金额";
                break;
            case TPPOSUtil.ERR_REFUND_AUTHNO:
                strErrMsg = "需要输入原授权码";
                break;
            case TPPOSUtil.ERR_REFUND_DATE:
                strErrMsg = "需要输入原日期(MMDD)";
                break;
            case TPPOSUtil.ERR_REFUND_TIME:
                strErrMsg = "需要输入原时间(HHmmss)";
                break;
            case TPPOSUtil.ERR_PREAUTH_VOID_AMOUNT:
                strErrMsg = "需要输入金额";
                break;
            case TPPOSUtil.ERR_PREAUTH_VOID_AUTHNO:
                strErrMsg = "需要输入原授权码";
                break;
            case TPPOSUtil.ERR_PREAUTH_VOID_DATE:
                strErrMsg = "需要输入原日期(MMDD)";
                break;
            case TPPOSUtil.ERR_PREAUTH_VOID_TIME:
                strErrMsg = "需要输入原时间(HHmmss)";
                break;
            case TPPOSUtil.ERR_PREAUTHFINISH_AMOUNT:
                strErrMsg = "需要输入金额";
                break;
            case TPPOSUtil.ERR_PREAUTHFINISH_AUTHNO:
                strErrMsg = "需要输入原授权码";
                break;
            case TPPOSUtil.ERR_TRANS_FAILED:
                strErrMsg = "交易失败";
                break;
            case TPPOSUtil.ERR_NEED_EXPDATE_PAN:
                strErrMsg = "手输卡号的交易流程必须要有卡号和卡有效期";
                break;
            case TPPOSUtil.ERR_TRANS_OFFLINE_SUCCESS:
                strErrMsg = "交易成功,离线交易成功";
                break;
            case TPPOSUtil.ERR_TRANS_CARD_REJECT:
                strErrMsg = "交易失败,卡片拒绝交易";
                break;
            case TPPOSUtil.READCARD_SUCCESS:
                strErrMsg = "成功";
                break;
            case TPPOSUtil.READCARD_TIMEOUT:
                strErrMsg = "超时";
                break;
            case TPPOSUtil.READCARD_USER_CANCEL:
                strErrMsg = "寻卡时,用户取消寻卡";
                break;
            case TPPOSUtil.READCARD_INIT_FAILED:
                strErrMsg = "初始化失败";
                break;
            case TPPOSUtil.READCARD_FORCE_IC:
                strErrMsg = "强制使用IC卡";
                break;
            case TPPOSUtil.READCARD_GET_TRACKDATA_FAILED:
                strErrMsg = "读卡失败，请重试";
                break;
            case TPPOSUtil.READCARD_TRACKDATA2_ERROR:
                strErrMsg = "第二磁道数据错误";
                break;
            case TPPOSUtil.READCARD_ERROR_IC_EMVINIT:
                strErrMsg = "EMV库初始化错误";
                break;
            case TPPOSUtil.READCARD_ERROR_IC_SELECT_APP:
                strErrMsg = "IC卡选择应用错误";
                break;
            case TPPOSUtil.READCARD_ERROR_IC_READ_DATA:
                strErrMsg = "IC卡读数据错误";
                break;
            case TPPOSUtil.READCARD_ERROR_IC_AUTHEN:
                strErrMsg = "IC卡数据认证错误";
                break;
            case TPPOSUtil.READCARD_ERROR_IC_PAN:
                strErrMsg = "IC卡账号错误";
                break;
            case TPPOSUtil.READCARD_ERROR_IC_UNKNOW:
                strErrMsg = "IC卡未知错误";
                break;
            case TPPOSUtil.READCARD_down_to_MagStripe:
                strErrMsg = "降级使用磁条卡";
                break;
            case TPPOSUtil.ERROR_OPEN_IC:
                strErrMsg = "打开IC模块错误";
                break;
            case TPPOSUtil.ERROR_OPEN_NFC:
                strErrMsg = "打开NFC模块错误";
                break;
            case TPPOSUtil.ERROR_OPEN_MAGNETIC:
                strErrMsg = "打开磁条卡模块错误";
                break;
            case TPPOSUtil.ERROR_READ_CARD_MODE:
                strErrMsg = "读卡模式错误";
                break;
            case TPPOSUtil.ERROR_CLOSE_IC:
                strErrMsg = "关闭IC模块错误";
                break;
            case TPPOSUtil.ERROR_CLOSE_NFC:
                strErrMsg = "关闭NFC模块错误";
                break;
            case TPPOSUtil.ERROR_CLOSE_MAGNETIC:
                strErrMsg = "关闭磁条卡模块错误";
                break;
            case TPPOSUtil.ERROR_OPEN_EMVSERVICE:
                strErrMsg = "打开EMV库失败";
                break;
            case TPPOSUtil.ERROR_OPEN_DEVICE:
                strErrMsg = "打开EMV设备失败";
                break;
            case TPPOSUtil.ERROR_OPEN_PINPAD:
                strErrMsg = "打开PINPAD失败";
                break;
            case TPPOSUtil.ERROR_CLOSE_DEVICE:
                strErrMsg = "关闭EMV设备失败";
                break;
            case TPPOSUtil.ERROR_CLOSE_PINPAD:
                strErrMsg = "关闭PINPAD失败";
                break;
            case TPPOSUtil.ERR_UNPACK:
                strErrMsg = "解包错误或者数据包错误";
                break;
            case TPPOSUtil.ERR_NO_RECEIVE_DATA:
                strErrMsg = "解包时发现没有数据包";
                break;
            case TPPOSUtil.ERR_RECEIVE_DATA_LENGTH:
                strErrMsg = "解包时发现长度错误";
                break;
            case TPPOSUtil.ERR_UNKNOW:
                strErrMsg = "解包未知错误";
                break;
            case TPPOSUtil.ERR_PINKEY_CHECK:
                strErrMsg = "PIN工作密钥校验错误";
                break;
            case TPPOSUtil.ERR_MACKEY_CHECK:
                strErrMsg = "MAC工作密钥校验错误";
                break;
            case TPPOSUtil.ERR_REDOWN_APP:
                strErrMsg = "需要重新下载IC卡参数 ";
                break;
            case TPPOSUtil.ERR_REDOWN_CAPK:
                strErrMsg = "需要重新下载IC卡公钥数据";
                break;
            case TPPOSUtil.ERR_CHECK_PIN:
                strErrMsg = "PIN密钥同步错误";
                break;
            case TPPOSUtil.ERR_TRANSACTION_NO_MATCH:
                strErrMsg = "交易类型不匹配";
                break;
            case ERR_DOWNMASTERKEY_CHECKTRANSKEY:
                strErrMsg = "校验传输密钥错误";
                break;
            case ERR_DOWNMASTERKEY_CHECKMASTERKEY:
                strErrMsg = "主密钥校验错误";
                break;
            case ERR_DOWNMASTERKEY:
                strErrMsg = "主密钥校验错误";
                break;
            case TPPOSUtil.ERR_PARSE_BALANCE:
                strErrMsg = "解析余额信息出错";
                break;
            case TPPOSUtil.ERR_MAC_CHECK:
                strErrMsg = "MAC校验错误";
                break;
            case TPPOSUtil.ERR_WRITE_WORK_KEY:
                strErrMsg = "写密钥失败";
                break;
            case TPPOSUtil.ERR_RESPONSE_CODE:
                strErrMsg = "平台应答码错误";
                break;
            case TPPOSUtil.ERR_FIELD48:
                strErrMsg = "48域解析错误";
                break;
            case TPPOSUtil.ERR_FIELD56:
                strErrMsg = "56域解析错误";
                break;
            case TPPOSUtil.ERR_FIELD62:
                strErrMsg = "62域解析错误";
                break;
            case TPPOSUtil.ERR_NO_FIELD62:
                strErrMsg = "没有62域";
                break;
            case TPPOSUtil.ERR_NO_PARA:
                strErrMsg = "POS中心没有要下载的数据";
                break;
            case TPPOSUtil.ERR_NOT_FINISHED:
                strErrMsg = "下载IC卡参数和IC卡公钥数据未完成";
                break;
            case TPPOSUtil.ERR_NO_FIELD48:
                strErrMsg = "报文没有48域";
                break;
            case TPPOSUtil.ERR_ICCCMD:
                strErrMsg = "读卡失败，请重试";
                break;
            case TPPOSUtil.ERR_TRANS_OVERTRANS:
                strErrMsg = "交易已达最大笔数，请先结算";
                break;
            default:
                strErrMsg = "未知错误,请重试.";
        }

        //处理流程返回值:
        return strErrMsg;
    }

    /**
     * @param responseCode 平台返回的响应码
     * @return
     */
    public static String getesponseCodeMsgInf(String responseCode) {
        String str = "";
        switch (responseCode) {
            case "00":
                str = "交易失败，卡片拒绝交易";
                break;
            case "01":
                str = "交易失败，请联系发卡行";
                break;
            case "02":
                str = "交易失败，请联系发卡行";
                break;
            case "03":
                str = "商户未登记";
                break;
            case "04":
                str = "没收卡，请联系收单行";
                break;
            case "05":
                str = "交易失败，请联系发卡行";
                break;
            case "06":
                str = "交易失败，请联系发卡行";
                break;
            case "07":
                str = "没收卡，请联系收单行";
                break;
            case "09":
                str = "交易失败，请重试";
                break;
            case "12":
                str = "交易失败，请重试";
                break;
            case "13":
                str = "交易金额超限，请重试";
                break;
            case "14":
                str = "无效卡号，请联系发卡行";
                break;
            case "15":
                str = "此卡不能受理";
                break;
            case "19":
                str = "交易失败，请联系发卡行";
                break;
            case "20":
                str = "交易失败，请联系发卡行";
                break;
            case "21":
                str = "交易失败，请联系发卡行";
                break;
            case "22":
                str = "操作有误，请重试";
                break;
            case "23":
                str = "交易失败，请联系发卡行";
                break;
            case "25":
                str = "交易失败，请联系发卡行";
                break;
            case "30":
                str = "交易失败，请重试";
                break;
            case "31":
                str = "此卡不能受理";
                break;
            case "32":
                str = "清分记账失败";
                break;
            case "33":
                str = "过期卡，请联系发卡行";
                break;
            case "34":
                str = "没收卡，请联系收单行";
                break;
            case "35":
                str = "没收卡，请联系收单行";
                break;
            case "36":
                str = "此卡有误，请换卡重试";
                break;
            case "37":
                str = "没收卡，请联系收单行";
                break;
            case "38":
                str = "密码错误次数超限";
                break;
            case "40":
                str = "交易失败，请联系发卡行";
                break;
            case "41":
                str = "没收卡，请联系收单行";
                break;
            case "42":
                str = "交易失败，请联系发卡方";
                break;
            case "43":
                str = "没收卡，请联系收单行";
                break;
            case "44":
                str = "交易失败，请联系发卡行";
                break;
            case "51":
                str = "余额不足，请查询";
                break;
            case "52":
                str = "交易失败，请联系发卡行";
                break;
            case "53":
                str = "交易失败，请联系发卡行";
                break;
            case "54":
                str = "过期卡，请联系发卡行";
                break;
            case "55":
                str = "密码错，请重试";
                break;
            case "56":
                str = "交易失败，请联系发卡行";
                break;
            case "57":
                str = "交易失败，请联系发卡行";
                break;
            case "58":
                str = "无效终端";
                break;
            case "59":
                str = "交易失败，请联系发卡行";
                break;
            case "60":
                str = "交易失败，请联系发卡行";
                break;
            case "61":
                str = "金额太大";
                break;
            case "62":
                str = "交易失败，请联系发卡行";
                break;
            case "63":
                str = "交易失败，请联系发卡行";
                break;
            case "64":
                str = "交易失败，请联系发卡行";
                break;
            case "65":
                str = "超出取款次数限制";
                break;
            case "66":
                str = "交易失败";
                break;
            case "67":
                str = "没收卡";
                break;
            case "68":
                str = "交易超时，请重试";
                break;
            case "75":
                str = "密码错误次数超限";
                break;
            case "77":
                str = "请向网络中心签到";
                break;
            case "79":
                str = "POS终端重传脱机数据";
                break;
            case "90":
                str = "交易失败，请稍后重试";
                break;
            case "91":
                str = "交易失败，请稍后重试";
                break;
            case "92":
                str = "交易失败，请稍后重试";
                break;
            case "93":
                str = "交易失败，请联系发卡行";
                break;
            case "94":
                str = "交易失败，请稍后重试";
                break;
            case "95":
                str = "交易失败，请稍后重试";
                break;
            case "96":
                str = "交易失败，请稍后重试";
                break;
            case "97":
                str = "终端未登记";
                break;
            case "98":
                str = "交易超时，请重试";
                break;
            case "99":
                str = "校验错，请重新签到";
                break;
            case "A0":
                str = "校验错，请重新签到";
                break;
            default:
                str = "未知返回码";
                break;
        }
        return str;
    }

}
