package com.telpo.pospay.main.Devices

/**
 * 交易仓库接口（定义交易执行规范）
 */
interface TransactionRepository {
    // 执行交易（通用入口）
    suspend fun executeTransaction(params: TransactionParams): Result<TransactionResult>
    // 结算（POS特有，独立方法）
    suspend fun settle(): Result<TransactionResult>
    // 解析银行名称（通过卡号前6位）
    fun getBankNameByCardNo(cardNo: String): String
}

/**
 * POS交易仓库实现（对接原TransPresenter子类）
 */
class PosTransactionRepoImpl : TransactionRepository {
    override suspend fun executeTransaction(params: TransactionParams): Result<TransactionResult> {
        return kotlinx.coroutines.suspendCoroutine { continuation ->
            // 根据交易类型创建原SDK的TransPresenter
            val sdkPresenter: com.pos.bankcard.transmanager.trans.TransPresenter? = when (params.type) {
                TransactionType.BALANCE_QUERY -> {
                    // 查余额：对接原EnquiryTrans
                    com.pos.bankcard.transmanager.trans.finace.query.EnquiryTrans(
                        context = null,
                        transType = com.pos.bankcard.transmanager.trans.Trans.Type.BALANCE,
                        para = com.pos.bankcard.transmanager.trans.TransInputPara(),
                        cardNo = params.cardNo
                    )
                }
                TransactionType.CONSUMPTION -> {
                    // 消费：对接原SaleTrans
                    com.pos.bankcard.transmanager.trans.finace.sale.SaleTrans(
                        context = null,
                        transType = com.pos.bankcard.transmanager.trans.Trans.Type.SALE,
                        para = com.pos.bankcard.transmanager.trans.TransInputPara(),
                        inMode = CardReader.INMODE_MAG or CardReader.INMODE_IC or CardReader.INMODE_NFC
                    )
                }
                TransactionType.TRANSACTION_CANCEL -> {
                    // 撤销：对接原VoidTrans
                    com.pos.bankcard.transmanager.trans.finace.revocation.VoidTrans(
                        context = null,
                        transType = com.pos.bankcard.transmanager.trans.Trans.Type.VOID,
                        para = com.pos.bankcard.transmanager.trans.TransInputPara(),
                        originalTransNo = params.originalTransNo
                    )
                }
                TransactionType.EC_ENQUIRY -> {
                    // 电子现金查询：对接原EC_EnquiryTrans
                    com.pos.bankcard.transmanager.trans.finace.ec_query.EC_EnquiryTrans(
                        context = null,
                        transType = com.pos.bankcard.transmanager.trans.Trans.Type.EC_ENQUIRY,
                        para = com.pos.bankcard.transmanager.trans.TransInputPara()
                    )
                }
                else -> null
            }

            // 执行交易（原SDK的异步逻辑）
            sdkPresenter?.let { presenter ->
                Thread {
                    try {
                        // 传递交易参数（如PIN块、卡号）到原Presenter
                        val sdkResult = presenter.start() // 假设改造原start()返回结果
                        // 转换为自定义TransactionResult
                        val customResult = TransactionResult(
                            balance = sdkResult.balance,
                            transNo = sdkResult.transNo,
                            batchNo = sdkResult.batchNo
                        )
                        continuation.resume(Result.success(customResult), null)
                    } catch (e: Exception) {
                        continuation.resume(Result.failure(Exception("交易失败：${e.message}")), null)
                    }
                }.start()
            } ?: run {
                continuation.resume(Result.failure(Exception("不支持的交易类型")), null)
            }
        }
    }

    override suspend fun settle(): Result<TransactionResult> {
        return kotlinx.coroutines.suspendCoroutine { continuation ->
            // 对接原SettleTrans（结算）
            val settlePresenter = com.pos.bankcard.transmanager.trans.finace.settle.SettleTrans(
                context = null,
                transType = com.pos.bankcard.transmanager.trans.Trans.Type.SETTLE,
                para = com.pos.bankcard.transmanager.trans.TransInputPara(),
                settleEntry = com.pos.bankcard.appmanager.trans.common.TransConst.SettleEntry.NORMAL
            )

            Thread {
                try {
                    val sdkResult = settlePresenter.start()
                    val customResult = TransactionResult(
                        totalTrans = sdkResult.totalTrans,
                        totalAmount = sdkResult.totalAmount
                    )
                    continuation.resume(Result.success(customResult), null)
                } catch (e: Exception) {
                    continuation.resume(Result.failure(Exception("结算失败：${e.message}")), null)
                }
            }.start()
        }
    }

    override fun getBankNameByCardNo(cardNo: String): String {
        // 解析卡号前6位获取银行名称（示例逻辑，实际需对接银行BIN库）
        return when (cardNo.takeIf { it.length >= 6 }?.substring(0, 6)) {
            "622600" -> "中国工商银行"
            "621700" -> "中国建设银行"
            "622580" -> "招商银行"
            else -> "未知银行"
        }
    }
}