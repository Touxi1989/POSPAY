package com.telpo.pospay.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.telpo.pospay.main.Devices.CardReader
import com.telpo.pospay.main.Devices.PinPad
import com.telpo.pospay.main.Devices.TransactionRepository
import com.telpo.pospay.main.TransactionState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.telpo.pospay.main.TransactionParams
import com.telpo.pospay.main.TransactionType
import kotlinx.coroutines.launch

class TransactionViewModel(
    private val cardReader: CardReader,
    private val pinPad: PinPad,
    private val transactionRepo: TransactionRepository
) : ViewModel() {
    // 交易状态流（UI层观察，不可变）
    private val _state = MutableStateFlow<TransactionState>(TransactionState.CardReading)
    val state: StateFlow<TransactionState> = _state.asStateFlow()

    // 临时交易参数（避免状态切换时丢失数据）
    private var tempParams = TransactionParams()

    // 超时时间（从全局配置获取，POS特有）
    private val timeout = 6000

    /**
     * 设置交易类型（外部传入时调用）
     */
    fun setTransactionType(type: TransactionType) {
        tempParams.type = type
        // 初始状态：根据类型切换
        _state.value = when (type) {
            TransactionType.TRANSACTION_CANCEL -> TransactionState.ConfirmCancel("") // 撤销→输入原交易号
            TransactionType.SETTLE -> TransactionState.Communicating(type, "准备结算...") // 结算→直接通信
            else -> TransactionState.CardReading // 其他→读卡
        }
    }

    /**
     * 确认卡号（进入下一步：输入金额/PIN）
     */
    fun confirmCard() {
        when (tempParams.type) {
            TransactionType.CONSUMPTION -> {
                // 消费→输入金额
                _state.value = TransactionState.ConfirmConsumption(
                    cardNo = maskCardNo(tempParams.cardNo ?: ""),
                    bankName = tempParams.bankName ?: "未知银行",
                    amount = tempParams.amount ?: ""
                )
            }
            TransactionType.BALANCE_QUERY, TransactionType.EC_ENQUIRY -> {
                // 查余额/电子现金→直接输PIN（如有必要）
                startInputPin()
            }
            else -> {
                _state.value = TransactionState.Error("不支持的操作")
            }
        }
    }

    /**
     * 确认消费金额（进入输PIN）
     */
    fun confirmConsumptionAmount(amount: String) {
        if (amount.toDoubleOrNull() == null || amount.toDouble() <= 0) {
            _state.value = TransactionState.Error("请输入有效的金额")
            return
        }
        tempParams.amount = amount
        startInputPin() // 金额确认后→输PIN
    }

    /**
     * 确认撤销的原交易号（进入输PIN）
     */
    fun confirmCancelTransNo(transNo: String) {
        if (transNo.length != 12) {
            _state.value = TransactionState.Error("原交易号必须为12位")
            return
        }
        tempParams.originalTransNo = transNo
        startInputPin() // 交易号确认后→输PIN
    }

    /**
     * 开始输入PIN（POS特有：密码键盘）
     */
    private fun startInputPin() {
        viewModelScope.launch {
            val amount = tempParams.amount ?: "0.00"
            _state.value = TransactionState.PinInputting(amount)
            when (val result = pinPad.inputOnlinePin(timeout, amount, tempParams.cardNo ?: "")) {
                is Result.Success -> {
                    val pinResult = result.data
                    if (pinResult.isSuccess) {
                        tempParams.pinBlock = pinResult.pinBlock
                        submitTransaction() // PIN成功→提交交易
                    } else {
                        _state.value = TransactionState.Error(pinResult.errMsg ?: "PIN输入失败")
                    }
                }
                is Result.Failure -> {
                    _state.value = TransactionState.Error(result.exception.message ?: "PIN输入异常")
                }
            }
        }
    }

    /**
     * 提交交易（核心：调用仓库执行交易）
     */
    private fun submitTransaction() {
        viewModelScope.launch {
            _state.value = TransactionState.Communicating(
                type = tempParams.type ?: TransactionType.BALANCE_QUERY,
                message = getCommunicatingMsg(tempParams.type)
            )

            when (val result = transactionRepo.executeTransaction(tempParams)) {
                is Result.Success -> {
                    handleTransactionSuccess(result.data)
                }
                is Result.Failure -> {
                    _state.value = TransactionState.Error(result.exception.message ?: "交易失败")
                }
            }
        }
    }
    /**
     * 处理交易成功（切换到对应结果状态）
     */
    private fun handleTransactionSuccess(result: TransactionResult) {
        _state.value = when (tempParams.type) {
            TransactionType.BALANCE_QUERY -> TransactionState.BalanceResult(
                cardNo = maskCardNo(tempParams.cardNo ?: ""),
                balance = result.balance ?: "0.00"
            )
            TransactionType.CONSUMPTION -> TransactionState.ConsumptionResult(
                cardNo = maskCardNo(tempParams.cardNo ?: ""),
                amount = tempParams.amount ?: "0.00",
                transNo = result.transNo ?: "",
                batchNo = result.batchNo ?: ""
            )
            TransactionType.TRANSACTION_CANCEL -> TransactionState.CancelResult(
                originalTransNo = tempParams.originalTransNo ?: "",
                cancelSuccess = true,
                message = "撤销成功，资金将在1-3个工作日到账"
            )
            TransactionType.EC_ENQUIRY -> TransactionState.BalanceResult(
                cardNo = maskCardNo(tempParams.cardNo ?: ""),
                balance = result.balance ?: "0.00"
            )
            else -> TransactionState.Error("交易结果处理异常")
        }
    }

    /**
     * 获取通信中提示文本（根据交易类型）
     */
    private fun getCommunicatingMsg(type: TransactionType?): String {
        return when (type) {
            TransactionType.BALANCE_QUERY -> "查询余额中..."
            TransactionType.CONSUMPTION -> "确认消费中..."
            TransactionType.TRANSACTION_CANCEL -> "撤销交易中..."
            TransactionType.EC_ENQUIRY -> "查询电子现金余额中..."
            TransactionType.SETTLE -> "结算中..."
            else -> "处理中..."
        }
    }

    /**
     * 卡号脱敏（保留前6位和后4位）
     */
    private fun maskCardNo(cardNo: String): String {
        return if (cardNo.length >= 10) {
            "${cardNo.substring(0, 6)}****${cardNo.substring(cardNo.length - 4)}"
        } else cardNo
    }

    /**
     * 返回键逻辑（处理状态回退）
     */
    fun onBackPressed(): Boolean {
        return when (_state.value) {
            is TransactionState.CardReading -> false // 读卡中→退出
            is TransactionState.ConfirmCard -> {
                _state.value = TransactionState.CardReading // 确认卡号→返回读卡
                true
            }
            is TransactionState.ConfirmConsumption -> {
                _state.value = TransactionState.ConfirmCard( // 确认金额→返回确认卡号
                    cardNo = maskCardNo(tempParams.cardNo ?: ""),
                    bankName = tempParams.bankName ?: "未知银行"
                )
                true
            }
            is TransactionState.ConfirmCancel -> {
                _state.value = TransactionState.ConfirmCancel("") // 确认撤销号→清空重输
                true
            }
            is TransactionState.InputPassword, is TransactionState.PinInputting -> {
                // 输密码/PIN→返回上一步（如确认金额/卡号）
                when (tempParams.type) {
                    TransactionType.CONSUMPTION -> _state.value = TransactionState.ConfirmConsumption(
                        cardNo = maskCardNo(tempParams.cardNo ?: ""),
                        bankName = tempParams.bankName ?: "未知银行",
                        amount = tempParams.amount ?: ""
                    )
                    TransactionType.TRANSACTION_CANCEL -> _state.value = TransactionState.ConfirmCancel(
                        tempParams.originalTransNo ?: ""
                    )
                    else -> _state.value = TransactionState.ConfirmCard(
                        cardNo = maskCardNo(tempParams.cardNo ?: ""),
                        bankName = tempParams.bankName ?: "未知银行"
                    )
                }
                true
            }
            is TransactionState.Communicating -> true // 通信中→禁止返回
            else -> {
                // 结果页/错误页→重置状态
                _state.value = if (tempParams.type == TransactionType.TRANSACTION_CANCEL) {
                    TransactionState.ConfirmCancel("")
                } else {
                    TransactionState.CardReading
                }
                true
            }
        }
    }

    /**
     * 释放资源（ViewModel销毁时调用）
     */
    override fun onCleared() {
        super.onCleared()
        cardReader.release()
        pinPad.release()
    }
}
/**
 * ViewModel工厂（用于注入Repository依赖）
 */
class TransactionViewModelFactory(
    private val cardReader: CardReader,
    private val pinPad: PinPad,
    private val transactionRepo: TransactionRepository
) : androidx.lifecycle.ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransactionViewModel::class.java)) {
            return TransactionViewModel(cardReader, pinPad, transactionRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}