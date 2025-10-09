package com.telpo.pospay.db.repository

import android.util.Log
import com.telpo.base.internet.socket.LongConnectSocket
import com.telpo.base.model.BaseViewModel.Companion.TAG
import com.telpo.base.util.MLog
import com.telpo.base.util.StringUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class NetWorkRepository {

    companion object{
        @Volatile
        private var sInstance: NetWorkRepository? = null //使线程共享一个实例
        val instance: NetWorkRepository?
            get() {
                if (sInstance == null) {
                    synchronized(NetWorkRepository::class.java) {
                        if (sInstance == null) {
                            sInstance = NetWorkRepository()
                        }
                    }
                }
                return sInstance
            }
    }

    fun sendSocketData(sendData: ByteArray): Flow<SocketResult> = flow {
        MLog.i("sendSocketData in")
        var dataLen = 0
        val dataLenBuff = ByteArray(2) //数据的长度
        var dataBuff: ByteArray? //数据
//        val socket = LongConnectSocket.getSocketInstance("182.61.131.14", 7905)
        //val socket = LongConnectSocket.getSocketInstance("192.168.222.99", 7905)
        val socket = try {
            LongConnectSocket.getSocketInstance("192.168.222.99", 7905)
        } catch (e: Exception) {
            MLog.i("创建Socket失败: ${e.message}")
            emit(SocketResult.Error("连接主机失败"))
            return@flow
        }
        val os = socket.getOutputStream()
        val ins = socket.getInputStream()

        os.write(sendData) //编码方式还有ASCII;UTF-8;UNICORE;GB2312?
        MLog.i("发送数据:" + StringUtil.bytesToHexString(sendData))
        os.flush()

        Thread.sleep(500)


//      UIUtil.updateProgressDialog(mContext, "签到", "正在接收")
        //ins.read(dataLenBuff) //先获取报文长度
        val lenRead = ins.read(dataLenBuff)
        if (lenRead != 2) {
            MLog.i("读取长度失败，实际读取: $lenRead")
            emit(SocketResult.Error("返回数据长度异常"))
            return@flow
        }
//      dataLen = ((dataLenBuff[0] and 0xff).toByte() << 8) or (dataLenBuff[1] and 0xff)

        dataLen = (dataLenBuff[0].toInt() and 0xFF) * 256 + (dataLenBuff[1].toInt() and 0xFF)
        MLog.i("dataLen:$dataLen")
        if (dataLen <= 0) {
            MLog.i("读取长度失败，实际读取: $lenRead")
            emit(SocketResult.Error("返回无效的数据长度: $dataLen"))
            return@flow
        }
        dataBuff = ByteArray(dataLen)

        var totalRead = 0
        while (totalRead < dataLen) {
            val read = ins.read(dataBuff, totalRead, dataLen - totalRead)
            if (read == -1) break
            totalRead += read
        }

        if (totalRead != dataLen) {
            MLog.i("数据读取不完整，期望: $dataLen, 实际: $totalRead")
            emit(SocketResult.Error("数据读取不完整，期望: $dataLen, 实际: $totalRead"))
            return@flow
        }

        os.close()
        ins.close()
        socket.close()
        // 成功返回
        emit(SocketResult.Success(dataBuff))
    }.catch { e ->
        // 处理异常
        Log.d(TAG, "sendPackData1: 数据请求失败")
//                _tip.value = R.string.request_false
        //e.printStackTrace()
        Log.d(TAG, "sendPackData1: ${e.message}")
        emit(SocketResult.Error("Socket通信异常: ${e.message}", e))
    }
}