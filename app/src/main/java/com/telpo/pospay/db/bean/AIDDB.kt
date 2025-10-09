package com.telpo.pospay.db.bean

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = AIDDB.TABLE_NAME) // 指定表名
class AIDDB : BaseBean() {

    companion object {
        const val TABLE_NAME = "table_AID"
    }

    /**
     * 关于AID参数下载
     * 首先发报文查询AID列表, 得到最新版本号, 赋值到LatestBOCVersion
     *
     * 下载完AID列表后,判断LatestBOCVersion与LocalBOCVersion,向平台请求下载版本号不同的AID
     */

    var LatestBOCVersion: String? = null //中行最新AID参数版本号


    var LocalBOCVersion: String? = null //本地AID参数的版本号


    @PrimaryKey
    var AID: String = "" //(9F06)AID


    var appName: String? = null


    var algorithmFlag: String? = null //(DF69)国密算法指示


    var selectFlag: String? = null //(DF01)应用选择指示


    var TACDefault: String? = null //(DF11)TAC缺省


    var TACOnline: String? = null //(DF12)TAC联机


    var TACDenial: String? = null //(DF13)TAC拒绝


    var DDOL: String? = null //(DF14)缺省DDOL


    var randSelectThreshold: String? = null //(DF15)随机选择阈值


    var randSelectMAXPercent: String? = null //(DF16)随机选择最大百分比数


    var randSelectTargetPercent: String? = null //(DF17)随机选择目标百分比数


    var terminalFloorLimit: String? = null //(9F1B)终端最低限额


    var APPVersion: String? = null //(9F09)应用版本


    var merchantCategoryCode: String? = null //(9F15)商户类别码


    var merchantID: String? = null //(9F16)商户标识（商户号）


    var merchantName: String? = null //(9F4E)商户名称


    var terminalID: String? = null //(9F1C)终端标识(终端号)


    var terminalRiskData: String? = null //(9F1D)终端风险数据管理


    var transCurrencyExp: String? = null //(5F36)交易货币指数  //transaction currency exponent


    var referCurrencyCode: String? = null //(9F3C)参考货币代码  //reference currency code


    var referCurrencyTransCode: String? = null //(DF8101)参考货币转换码  //reference currency transfer code


    var referCurrencyExp: String? = null //(9F3D)参考货币指数  //reference currency exponent


    var bTDOL: Boolean = false //是否支持缺省TDOL


    var TDOL: String? = null //(DF8102)缺省TDOL


    var currencyCode: String? = null //(5F2A)货币代码


    var acquirerID: String? = null //(9F01)收单行标识


    var AIDProperty: String? = null //(df18)AID应用标志参数//todo 中行标志,详见中行接口规范6.2.2.2


    var NFC: String? = null //(df19)AID应用标志参数//todo 中行标志,详见中行接口规范6.2.2.2


    var terminalType: String? = null //(9f35)终端类型、终端分类码、终端级别//todo 中行标志


    var ECLimit: String? = null //(9F7B)终端电子现金交易限额//todo 中行标志


    var NFCOfflineLimit: String? = null //(DF40)非接触读卡器脱机限额//todo 中行标志


    var NFCLimit: String? = null //(DF20)非接触读卡器限额//todo 中行标志


    var NFCCVMLimit: String? = null //(DF21)非接触读卡器持卡人验证限额//todo 中行标志


    /*如果非接触交易金额数值大于此数值，读写器要求一个持卡人验证方法，联机 PIN 和签名是本部分定义的持卡人验证方法（CVM）。*/

    var NFCTACDefault: String? = null //(DF41)NFC TAC缺省//todo 中行标志


    var NFCTACOnline: String? = null //(DF42)NFC TAC联机//todo 中行标志


    var NFCTACDenial: String? = null //(DF43)NFC TAC拒绝//todo 中行标志


    var bEnable: Boolean = false //状态属性,根据这个决定是否存到EMV库


}