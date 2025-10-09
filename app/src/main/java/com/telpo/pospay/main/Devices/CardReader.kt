package com.telpo.pospay.main.Devices

import com.telpo.pospay.main.CardInfo

/**
 * 读卡接口（定义规范，解耦硬件实现）
 */
interface CardReader {
    // 读卡（支持磁条/IC/NFC，timeout：超时时间ms）
    suspend fun readCard(timeout: Int, mode: Int): Result<CardInfo>
    // 释放读卡器资源
    fun release()

    // 读卡模式常量（POS特有）
    companion object {
        const val INMODE_MAG = 0x02    // 磁条卡
        const val INMODE_IC = 0x08     // IC卡
        const val INMODE_NFC = 0x10    // NFC（闪付）
    }
}

/**
 * POS读卡器实现（对接原CardManager）
 */
class PosCardReaderImpl : CardReader {
    private var cardManager: com.pos.bankcard.transmanager.device.card.CardManager? = null

    override suspend fun readCard(timeout: Int, mode: Int): Result<CardInfo> {
        return kotlinx.coroutines.suspendCancellableCoroutine { continuation ->
            // 初始化硬件读卡器
            cardManager = com.pos.bankcard.transmanager.device.card.CardManager.getInstance(mode)
            // 调用原硬件SDK读卡
            cardManager?.getCard(timeout - 1000, object : com.pos.bankcard.transmanager.device.card.CardListener {
                override fun callback(sdkCardInfo: com.pos.bankcard.transmanager.device.card.CardInfo) {
                    if (continuation.isActive) {
                        // 转换为自定义CardInfo（解耦SDK）
                        val customCardInfo = CardInfo(
                            trackNo = sdkCardInfo.trackNo ?: "",
                            cardType = sdkCardInfo.cardType,
                            atr = sdkCardInfo.cardAtr,
                            appList = sdkCardInfo.appList ?: emptyList()
                        )
                        continuation.resume(Result.success(customCardInfo), null)
                    }
                }
            })

            // 超时/取消处理
            continuation.invokeOnCancellation {
                cardManager?.releaseAll()
                continuation.resume(Result.failure(Exception("读卡超时或取消")), null)
            }
        }
    }

    override fun release() {
        // cardManager?.releaseAll()
    }
}