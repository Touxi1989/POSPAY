package com.telpo.pospay.repository

import android.content.Context
import com.telpo.base.internet.socket.LongConnectSocket
import com.telpo.base.util.TLog
import com.telpo.base.util.StringUtil
import kotlinx.coroutines.flow.first
import kotlin.experimental.and

class SocketRepository private constructor(private val appContext: Context) :
    BaseRepository(appContext) {

    companion object {
        @Volatile
        private var instance: SocketRepository? = null

        fun getInstance(appContext: Context): SocketRepository {
            return instance?:synchronized(this) {
                instance?:SocketRepository(appContext).also { instance = it }
            }
        }
    }


    suspend fun sendPackData(sendData: ByteArray): ByteArray {
        if (!networkUtils.isNetworkAvailable().first()) {
            throw Exception("Network is unAvailable") // 网络不可用
        }
        var dataLen = 0
        val dataLenBuff = ByteArray(2) //数据的长度
        var dataBuff: ByteArray? //数据
        val socket = LongConnectSocket.getSocketInstance("192.168.1.144", 9999)
        val os = socket.getOutputStream()
        os.write(sendData) //编码方式还有ASCII;UTF-8;UNICORE;GB2312?
        TLog.i("发送数据:" + StringUtil.bytesToHexString(sendData))
        os.flush()
        Thread.sleep(500)
        val ins = socket.getInputStream()

        //                    UIUtil.updateProgressDialog(mContext, "签到", "正在接收")
        ins.read(dataLenBuff) //先获取报文长度
        //                    dataLen = ((dataLenBuff[0] and 0xff).toByte() << 8) or (dataLenBuff[1] and 0xff)
        dataLen =
                ((dataLenBuff[0] and 0xff.toByte()).toInt() shl 8) or (dataLenBuff[1] and 0xff.toByte()).toInt()
        dataBuff = ByteArray(dataLen)

        TLog.i("dataLen:$dataLen")

        if (ins.read(dataBuff) == dataLen) {
            val recevieData: String =
                    StringUtil.bytesToHexString(dataLenBuff) + StringUtil.bytesToHexString(
                        dataBuff
                    )
            TLog.i("接收数据:" + recevieData)
        } else {
        }

        os.close()
        ins.close()
        socket.close()
        return dataBuff
    }
}