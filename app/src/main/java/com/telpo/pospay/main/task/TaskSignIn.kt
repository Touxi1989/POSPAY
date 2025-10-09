package com.telpo.pospay.main.task

import android.content.Context
import com.telpo.base.util.MLog
import com.telpo.base.util.StringUtil
import com.telpo.pospay.db.repository.NetWorkRepository
import com.telpo.pospay.db.repository.SocketResult
import com.telpo.pospay.main.viewmodel.StartAppViewModel
import com.telpo.pospay.main.data.GlobalParams
import com.telpo.pospay.main.data.POSData
import com.telpo.pospay.main.data.PackData
import com.telpo.pospay.main.util.PackUtil
import com.telpo.pospay.main.util.TPPOSUtil.OK_SUCCESS
import com.telpo.pospay.main.util.UnPackUtil

class TaskSignIn(val viewModel: StartAppViewModel, val context: Context) {

    fun obtainSignInPackage(mPOSData: POSData, mPackData: PackData): Int {
        mPOSData.terminalID = GlobalParams.get_terminalNo(context)//"12345678"
        mPOSData.merchantID = GlobalParams.get_merchantNo(context)//"123456789012345"
        mPOSData.batchNo = GlobalParams.get_BatchNo(context)//"001686"
        mPOSData.orderNo = GlobalParams.get_transNo(context)//"000001"
        mPOSData.operaterID = "01"

        val i: Int = PackUtil.packMessage_SignIn(context, mPOSData, mPackData)

        MLog.i(
            "TPPOSUtil.packData: " + i + "\nData: " + StringUtil.bytesToHexString_upcase(
                mPackData.data_send
            )
        )

        return i
    }

    suspend fun doSignIn(): Boolean {
        val mPOSData = POSData()
        val mPackData = PackData()

        viewModel._loading.postValue("正在签到中")

        obtainSignInPackage(mPOSData, mPackData)

        var signInSuccess = false // 用变量保存结果


        NetWorkRepository.instance?.sendSocketData(mPackData.data_send)?.collect { result ->
            when (result) {
                is SocketResult.Success -> {
                    viewModel._loading.postValue("")
                    MLog.i("SocketResult.Success in")
                    mPackData.data_receive = result.data
                    val result = UnPackUtil.unpackMessage_LoginFinish(context,mPOSData,mPackData)
                    if (result == OK_SUCCESS){
                        MLog.i("签到处理成功")
                        //viewModel._msg.postValue("签到成功:" + mPackData.responseCode)
                        signInSuccess = true
                    } else {
                        viewModel._msg.postValue("签到失败:" + mPackData.responseCode + "\n"+ mPackData.unPackCode)
                        signInSuccess = false
                    }
                }
                is SocketResult.Error -> {
                    viewModel._loading.postValue("")
                    MLog.i("SocketResult.Error in" + result.message)
                    viewModel._msg.postValue("签到失败:" + result.message)
                }
            }
        }
        return signInSuccess
    }

}