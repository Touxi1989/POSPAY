package com.telpo.pospay.main.Devices


/**
 * PIN输入接口（定义规范，解耦密码键盘硬件）
 */
interface PinPad {
    // 在线PIN输入（amount：交易金额，cardNo：卡号）
    suspend fun inputOnlinePin(timeout: Int, amount: String, cardNo: String): Result<PinResult>
    // 释放PIN键盘资源
    fun release()
}

/**
 * POS PIN键盘实现（对接原PinpadManager）
 */
class PosPinPadImpl : PinPad {
    private var pinManager: com.pos.bankcard.transmanager.device.pinpad.PinpadManager? = null

    override suspend fun inputOnlinePin(timeout: Int, amount: String, cardNo: String): Result<PinResult> {
        return kotlinx.coroutines.suspendCancellableCoroutine { continuation ->
            // 初始化PIN键盘
            pinManager = com.pos.bankcard.transmanager.device.pinpad.PinpadManager.getInstance()
            // 调用原SDK输入PIN（POS特有：支持凭密/非凭密）
            val isNoPin = amount.isEmpty() // 金额为空时无需输密（如查余额）
            pinManager?.getPin(
                timeout = timeout,
                amount = amount,
                cardNo = cardNo,
                listener = object : com.pos.bankcard.transmanager.device.pinpad.PinpadListener {
                    override fun callback(sdkPinInfo: com.pos.bankcard.transmanager.device.pinpad.PinInfo) {
                        if (continuation.isActive) {
                            val customPinResult = PinResult(
                                pinBlock = sdkPinInfo.pinblock ?: "",
                                isSuccess = sdkPinInfo.isResultFlag,
                                errMsg = if (sdkPinInfo.isResultFlag) null else "PIN输入失败"
                            )
                            continuation.resume(Result.success(customPinResult), null)
                        }
                    }
                },
                title = "输入密码",
                isNoPin = isNoPin
            )

            // 取消处理
            continuation.invokeOnCancellation {
                pinManager?.cancel()
                continuation.resume(Result.failure(Exception("PIN输入取消")), null)
            }
        }
    }

    override fun release() {
        pinManager?.cancel()
    }
}