package com.telpo.pospay.main

/**
 * 交易参数（封装所有交易所需输入，替代原分散变量）
 */
data class TransactionParams(
    var type: TransactionType? = null,       // 交易类型
    var cardNo: String? = null,              // 卡号（原始，未脱敏）
    var bankName: String? = null,            // 银行名称
    var amount: String? = null,              // 交易金额（元，如100.00）
    var originalTransNo: String? = null,     // 原交易号（撤销用）
    var pinBlock: String? = null,            // PIN块（加密后，POS特有）
    var trackNo: String? = null,             // 磁道信息（磁条卡用）
    var cardAtr: String? = null,             // IC卡ATR信息（POS特有）
    var selectedApp: String? = null          // 选中的IC卡应用（POS特有）
)

/**
 * 交易结果（封装所有交易输出，解耦原始SDK）
 */
data class TransactionResult(
    var balance: String? = null,             // 余额（查余额用）
    var transNo: String? = null,             // 交易号（消费/撤销用）
    var batchNo: String? = null,             // 批次号
    var totalTrans: Int? = null,             // 总笔数（结算用）
    var totalAmount: String? = null          // 总金额（结算用）
)

/**
 * 卡片信息（解耦POS硬件SDK的CardInfo）
 */
data class CardInfo(
    val trackNo: String,                     // 磁道信息
    val cardType: Int,                       // 卡片类型（磁条/IC/NFC）
    val atr: String? = null,                 // IC卡ATR
    val appList: List<String>? = null        // IC卡应用列表
)

/**
 * PIN输入结果（解耦POS硬件SDK的PinInfo）
 */
data class PinResult(
    val pinBlock: String,                    // 加密后的PIN块
    val isSuccess: Boolean,                  // 是否成功
    val errMsg: String? = null               // 错误信息
)