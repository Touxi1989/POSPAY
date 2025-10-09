package com.telpo.pospay.main

/**
 * 统一交易状态密封类：驱动UI更新，涵盖所有POS交易场景
 */
sealed class TransactionState {
    // 1. 通用基础状态
    object CardReading : TransactionState() // 读卡中（磁条/IC/NFC）
    data class ConfirmCard(
        val cardNo: String,       // 脱敏卡号（6226****1234）
        val bankName: String      // 银行名称（如中国工商银行）
    ) : TransactionState()
    data class ConfirmConsumption(
        val cardNo: String,
        val bankName: String,
        val amount: String        // 消费金额（如100.00）
    ) : TransactionState()
    data class ConfirmCancel(
        val originalTransNo: String // 撤销的原交易号（12位）
    ) : TransactionState()
    object InputPassword : TransactionState() // 输入银行卡密码
    data class Communicating(
        val type: TransactionType,
        val message: String = "处理中..." // 通信提示（如“查询余额中”）
    ) : TransactionState()

    // 2. POS特有状态
    data class SelectCardApp(val appList: List<String>) : TransactionState() // 选择IC卡应用
    data class PinInputting(val amount: String) : TransactionState() // PIN输入中（密码键盘）
    object InputMasterPassword : TransactionState() // 输入主管密码（如撤销时）
    data class PrintLack(val needRetry: Boolean) : TransactionState() // 打印机缺纸

    // 3. 结果状态
    data class BalanceResult(
        val cardNo: String,
        val balance: String       // 余额（如12345.67）
    ) : TransactionState()
    data class ConsumptionResult(
        val cardNo: String,
        val amount: String,
        val transNo: String,      // 交易号（如202405201234）
        val batchNo: String       // 批次号（如000001）
    ) : TransactionState()
    data class CancelResult(
        val originalTransNo: String,
        val cancelSuccess: Boolean,
        val message: String
    ) : TransactionState()
    data class SettleResult(
        val totalTrans: Int,      // 总交易笔数
        val totalAmount: String   // 总交易金额
    ) : TransactionState()

    // 4. 异常状态
    data class Error(val message: String) : TransactionState() // 通用错误（如读卡失败）
}