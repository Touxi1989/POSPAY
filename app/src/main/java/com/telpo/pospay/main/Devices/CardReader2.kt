package com.telpo.pospay.main.Devices

import com.telpo.base.util.MLog
import com.telpo.emv.EmvService
import com.telpo.emv.EmvServiceListener
import java.util.concurrent.locks.ReentrantLock

class CardReader2(
    private val emvService: EmvService,
    private val listener: CardDetectionListener
) {
    // 线程锁：确保检测过程线程安全（避免多线程同时操作读卡器）
    private val threadLock = ReentrantLock()
    // 检测停止标记（用于外部控制终止检测）
    @Volatile // 保证多线程可见性
    private var isDetectStopped = false
    // 上次操作结果码
    private var lastCode = 0

    /**
     * 开始卡片检测（启动新线程执行，避免阻塞主线程）
     */
    fun startDetection() {
        // 重置检测状态
        isDetectStopped = false
        lastCode = 0
        // 启动检测线程
        Thread(DetectionRunnable()).start()
    }

    /**
     * 停止卡片检测（外部调用，如用户取消检测）
     */
    fun stopDetection() {
        isDetectStopped = true
    }

    /**
     * 检测任务：实际执行读卡器操作的 Runnable
     */
    private inner class DetectionRunnable : Runnable {
        override fun run() {
            // 加锁：确保同一时间只有一个检测任务执行
            if (!threadLock.tryLock()) {
                listener.onDetectionError("检测任务已在执行中")
                return
            }

            try {
                // 1. 先关闭所有已打开的读卡器（避免上次未释放）
                closeAllReaders()
                // 2. 依次打开磁条卡/NFC/IC读卡器（链式判断，一个失败则终止）
                val isReaderReady = openMagStripeReader()
                        && openNfcReader()
                        && openIccReader()

                if (isReaderReady) {
                    // 3. 循环检测卡片（直到检测到卡片或外部停止）
                    listener.onDetectionLog("开始检测卡片...")
                    detectCardLoop()
                }
            } catch (e: InterruptedException) {
                // 线程中断处理（如页面销毁时终止检测）
                MLog.i( "检测线程被中断:" + e.message )
                listener.onDetectionError("检测已中断：${e.message}")
                Thread.currentThread().interrupt() // 恢复中断标记
            } finally {
                // 4. 无论成功/失败，都要清理资源（关闭读卡器、销毁对话框）
                cleanupResources()
                threadLock.unlock() // 释放锁
            }
        }
    }

    /**
     * 循环检测卡片：依次检查NFC/IC/磁条卡，直到检测到卡片或停止
     */
    private fun detectCardLoop() {
        while (!isDetectStopped) {
            // 1. 检测NFC卡（超时300ms）
            if (EmvService.NfcCheckCard(300) == EmvService.EMV_DEVICE_TRUE) {
                handleNfcCardDetected()
                break
            }

            // 2. 检测IC卡（超时300ms）
            if (EmvService.IccCheckCard(300) == EmvService.EMV_DEVICE_TRUE) {
                handleIcCardDetected()
                break
            }

            // 3. 检测磁条卡（超时300ms，磁条卡检测耗时稍长）
            if (EmvService.MagStripeCheckCard(300) == EmvService.EMV_DEVICE_TRUE) {
                handleMagStripeCardDetected()
                break
            }
        }
    }

    /**
     * 处理NFC卡检测到的逻辑
     */
    private fun handleNfcCardDetected() {
        isDetectStopped = true
        listener.onDetectionLog("发现NFC卡片，准备交易...")
        listener.onNfcCardDetected() // 通知外部处理NFC卡逻辑
    }

    /**
     * 处理IC卡检测到的逻辑（需上电/下电）
     */
    private fun handleIcCardDetected() {
        isDetectStopped = true
        EmvService.IccCard_Poweron() // IC卡上电
        emvService.setListener(listener.emvListener) // 设置EMV回调（外部传入）
        listener.onDetectionLog("发现IC卡片，准备交易...")
        listener.onIcCardDetected() // 通知外部处理IC卡逻辑
        EmvService.IccCard_Poweroff() // IC卡下电
    }

    /**
     * 处理磁条卡检测到的逻辑
     */
    private fun handleMagStripeCardDetected() {
        isDetectStopped = true
        listener.onDetectionLog("发现磁条卡，准备交易...")
        listener.onMagStripeCardDetected() // 通知外部处理磁条卡逻辑
    }

    /**
     * 打开磁条卡读卡器
     * @return true：打开成功；false：打开失败
     */
    private fun openMagStripeReader(): Boolean {
        lastCode = EmvService.MagStripeOpenReader()
        return if (lastCode == EmvService.EMV_DEVICE_TRUE) {
            true
        } else {
            listener.onDetectionError("磁条卡读卡器打开失败，错误码：$lastCode")
            false
        }
    }

    /**
     * 打开IC卡读卡器
     */
    private fun openIccReader(): Boolean {
        lastCode = EmvService.IccOpenReader()
        return if (lastCode == EmvService.EMV_DEVICE_TRUE) {
            true
        } else {
            listener.onDetectionError("IC卡读卡器打开失败，错误码：$lastCode")
            // 失败时关闭已打开的磁条卡/NFC读卡器
            EmvService.MagStripeCloseReader()
            EmvService.NfcCloseReader()
            false
        }
    }

    /**
     * 打开NFC读卡器（超时200ms）
     */
    private fun openNfcReader(): Boolean {
        lastCode = EmvService.NfcOpenReader(200)
        return if (lastCode == EmvService.EMV_DEVICE_TRUE) {
            true
        } else {
            listener.onDetectionError("NFC读卡器打开失败，错误码：$lastCode")
            EmvService.MagStripeCloseReader() // 失败时关闭已打开的磁条卡读卡器
            false
        }
    }

    /**
     * 清理资源：关闭所有读卡器 + 销毁加载对话框
     */
    private fun cleanupResources() {
        closeAllReaders()
        // 主线程更新对话框（避免UI线程异常）
        //processDialog.dismissOnMainThread()
    }

    /**
     * 关闭所有类型的读卡器
     */
    private fun closeAllReaders() {
        EmvService.MagStripeCloseReader()
        EmvService.NfcCloseReader()
        EmvService.IccCloseReader()
    }

    /**
     * 读卡器检测结果回调接口
     * （用接口而非Lambda，支持多回调类型，更易扩展）
     */
    interface CardDetectionListener {
        // EMV服务需要的回调（外部传入，避免CardDetector依赖EMV细节）
        val emvListener: EmvServiceListener
        // 检测到NFC卡
        fun onNfcCardDetected()
        // 检测到IC卡
        fun onIcCardDetected()
        // 检测到磁条卡
        fun onMagStripeCardDetected()
        // 检测日志（如"开始检测"、"发现卡片"）
        fun onDetectionLog(log: String)
        // 检测错误（如读卡器打开失败）
        fun onDetectionError(error: String)
    }
}