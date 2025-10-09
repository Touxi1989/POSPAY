package com.telpo.pospay.main

import java.io.Serializable

/**
 * 支持的POS交易类型（需序列化，用于外部传参）
 */
enum class TransactionType : Serializable {
    BALANCE_QUERY,    // 查余额
    CONSUMPTION,      // 消费（含普通消费、快速消费）
    TRANSACTION_CANCEL,// 交易撤销
    SETTLE,           // 结算
    EC_ENQUIRY        // 电子现金余额查询
}