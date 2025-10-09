package com.telpo.pospay.main.task

import android.content.Context
import android.text.TextUtils
import com.telpo.base.util.MLog
import com.telpo.base.util.StringUtil
import com.telpo.pospay.db.repository.NetWorkRepository
import com.telpo.pospay.db.repository.SocketResult
import com.telpo.pospay.main.viewmodel.StartAppViewModel
import com.telpo.pospay.main.data.GlobalParams
import com.telpo.pospay.main.data.POSData
import com.telpo.pospay.main.data.PackData
import com.telpo.pospay.main.util.ErrorMsg
import com.telpo.pospay.main.util.PackUtil
import com.telpo.pospay.main.util.TPPOSUtil
import com.telpo.pospay.main.util.TPPOSUtil.ERR_NO_NEED_UPDATE
import com.telpo.pospay.main.util.TPPOSUtil.OK_SUCCESS
import com.telpo.pospay.main.util.UnPackUtil
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class TaskDownParameter(val viewModel: StartAppViewModel, val context: Context) {


    companion object {
        const val TYPE_DOWN_PARAMETERTERMINAL: Int = 200 //下载终端参数

        const val TYPE_DOWN_PARAMETERAID: Int = 201 //下载03块参数

        const val TYPE_DOWN_PARAMETERCAPK: Int = 202 //下载04块参数

        const val TYPE_QUERY_AID_VERSION: Int = 204 //查询AID版本

        const val TYPE_QUERY_CAPK_VERSION: Int = 2034 //查询CAPK版本

        const val TYPE_DOWN_MASTERKEY: Int = 203 //下载主密钥

        const val TYPE_DOWN_PARAMETERAIDFINISH: Int = 205 //下载AID结束

        const val TYPE_DOWN_PARAMETERCAPKFINISH: Int = 206 //下载CAPK结束

        const val TYPE_DOWN_PARAMETERBLACKLISTFINISH: Int = 208 //下载黑名单结束

        const val TYPE_DOWN_PARAMETERBLACKLIST: Int = 209 //下载黑名单

        const val TYPE_DOWN_PARAMETENFC: Int = 210 //下载NFC参数

        const val TYPE_DOWN_PARAMETENFCFINISH: Int = 211 //下载NFC参数结束

        const val TYPE_DOWN_PARAMETENFCBLACKNAMELIST: Int = 212 //下载NFC黑名单参数

        const val TYPE_DOWN_PARAMETENFCBLACKNAMELISTFINISH: Int = 213 //下载NFC黑名单参数结束

        const val TYPE_DOWN_PARAMETENFCWHITENAMELIST: Int = 214 //下载NFC白名单参数

        const val TYPE_DOWN_PARAMETENFCWHITENAMELISTFINISH: Int = 215 //下载NFC白名单参数结束
    }

    var isQureyFinished: Boolean = false
    var isQureyFirst: Boolean = true

    suspend fun downParam(nowTaskType: Int): Boolean = coroutineScope { // 使用coroutineScope创建子协程作用域
        val mPOSData = POSData()
        val mPackData = PackData()

        // 用于跟踪最终结果
        val success = MutableStateFlow<Boolean?>(null)

        val packResult: Int? = when (nowTaskType) {
            TYPE_QUERY_CAPK_VERSION -> {
                viewModel._loading.postValue("查询IC卡公钥")
                obtainQueryCAPKVersion(mPOSData, mPackData)
            }
            TYPE_DOWN_PARAMETERCAPK -> {
                val i = obtainDownParameterCAPK(mPOSData, mPackData)
                viewModel._loading.postValue("下载IC卡公钥" + mPackData.downloadParamIndex + "/" + mPackData.downloadParamCount)
                i
            }
            TYPE_DOWN_PARAMETERCAPKFINISH -> {
                viewModel._loading.postValue("下载IC卡公钥")
                obtainDownParameterCAPKFinished(mPOSData, mPackData)
            }

            TYPE_QUERY_AID_VERSION -> {
                viewModel._loading.postValue("查询IC卡参数")
                obtainQueryAIDVersion(mPOSData, mPackData)
            }
            TYPE_DOWN_PARAMETERAID -> {
                val i = obtainDownParameterAID(mPOSData, mPackData)
                viewModel._loading.postValue("下载IC卡参数" + mPackData.downloadParamIndex + "/" + mPackData.downloadParamCount)
                i
            }
            TYPE_DOWN_PARAMETERAIDFINISH -> {
                viewModel._loading.postValue("下载IC卡参数")
                obtainDownParameterAIDFinished(mPOSData, mPackData)
            }
            TYPE_DOWN_PARAMETENFC -> {
                viewModel._loading.postValue("下载非接参数")
                obtainDownParameterNFC(mPOSData, mPackData)
            }
            TYPE_DOWN_PARAMETENFCFINISH -> {
                viewModel._loading.postValue("下载非接参数结束")
                obtainDownParameterCAPKFinished(mPOSData, mPackData)

            }
            TYPE_DOWN_PARAMETERBLACKLIST -> {
                viewModel._loading.postValue("下载非接黑名单")
                obtainNFCBlackList(mPOSData, mPackData)

            }
            TYPE_DOWN_PARAMETERBLACKLISTFINISH->{
                viewModel._loading.postValue("下载非接黑名单结束")
                obtainNFCBlackListFinish(mPOSData, mPackData)

            }
            else -> null
        }

        MLog.i("packResult=$packResult")
        if (packResult == ERR_NO_NEED_UPDATE) {
            if (nowTaskType == TYPE_DOWN_PARAMETERCAPK) {
                launch { success.value = downParam(TYPE_DOWN_PARAMETERCAPKFINISH) }
            } else if (nowTaskType == TYPE_DOWN_PARAMETERAID) {
                launch { success.value = downParam(TYPE_DOWN_PARAMETERAIDFINISH) }
            } else {
                success.value = true // 无需更新也算成功
            }
        } else {
            isQureyFinished = true;
            NetWorkRepository.instance?.sendSocketData(mPackData.data_send)?.collect { result ->
                when (result) {
                    is SocketResult.Success -> {
                        //这里判断是否继续下载参数
                        mPackData.data_receive = result.data
                        val result = when (nowTaskType) {
                            TYPE_QUERY_CAPK_VERSION -> UnPackUtil.unpackMessage_QureyCAPKParaVer(
                                context,
                                mPOSData,
                                mPackData
                            )

                            TYPE_DOWN_PARAMETERCAPK -> UnPackUtil.unpackDownParaCAPKMessage(
                                context,
                                mPOSData,
                                mPackData
                            )

                            TYPE_DOWN_PARAMETERCAPKFINISH -> UnPackUtil.unpackMessage_DownloadCAPKParaFinish(
                                context,
                                mPOSData,
                                mPackData
                            )

                            TYPE_QUERY_AID_VERSION -> UnPackUtil.unpackMessage_QureyAIDParaVer(
                                context,
                                mPOSData,
                                mPackData
                            )

                            TYPE_DOWN_PARAMETERAID -> UnPackUtil.unpackDownAIDMessage(
                                context,
                                mPOSData,
                                mPackData
                            )

                            TYPE_DOWN_PARAMETERAIDFINISH -> UnPackUtil.unpackMessage_DownloadAIDParaFinish(
                                context,
                                mPOSData,
                                mPackData
                            )

                            TYPE_DOWN_PARAMETENFC -> UnPackUtil.unpackMessage_DownParaNFC(
                                context,
                                mPOSData,
                                mPackData
                            )

                            TYPE_DOWN_PARAMETENFCFINISH -> UnPackUtil.unpackMessage_DownloadNFCParaFinish(
                                context,
                                mPOSData,
                                mPackData
                            )

                            TYPE_DOWN_PARAMETERBLACKLIST -> UnPackUtil.unpackMessage_DawnParaNFCBlackList(
                                context,
                                mPOSData,
                                mPackData
                            )

                            TYPE_DOWN_PARAMETERBLACKLISTFINISH -> UnPackUtil.unpackMessage_DownParaNFCBlackListFinish(
                                context,
                                mPOSData,
                                mPackData
                            )

                            else -> {}
                        }

                        MLog.i("unpackMessage_result--$result")
                        isQureyFinished = true;
                        if (nowTaskType == TYPE_DOWN_MASTERKEY) {
                            isQureyFinished = false;
                        }
                        if (nowTaskType == TYPE_DOWN_PARAMETERCAPKFINISH && result == TPPOSUtil.ERR_NO_FIELD62) {
                            //                result = TPPOSUtil.OK_SUCCESS;
                        } else if (nowTaskType == TYPE_DOWN_PARAMETERTERMINAL && result == TPPOSUtil.OK_SUCCESS) {

                        } else if (result == TPPOSUtil.ERR_NOT_FINISHED) {
                            isQureyFinished = false;
                            isQureyFirst = false;
                        }
                        // 关键修复：用launch启动新协程调用挂起函数
                        launch {
                            if (result == OK_SUCCESS || result == ERR_NO_NEED_UPDATE || result == TPPOSUtil.ERR_NOT_FINISHED) {
                                if (!TextUtils.isEmpty(mPackData.responseCode) && "00" != mPackData.responseCode) {
                                    val msg =
                                        "失败,应答码: " + mPackData.responseCode + "\n" + ErrorMsg.getesponseCodeMsgInf(
                                            mPackData.responseCode
                                        );
                                    viewModel._msg.postValue(msg)
                                    success.value = false
                                } else if (isQureyFinished && (nowTaskType == TYPE_DOWN_PARAMETERCAPKFINISH || nowTaskType == TYPE_DOWN_PARAMETERAIDFINISH || nowTaskType == TYPE_DOWN_PARAMETERBLACKLISTFINISH
                                            || nowTaskType == TYPE_DOWN_PARAMETENFCFINISH || nowTaskType == TYPE_DOWN_PARAMETENFCBLACKNAMELISTFINISH || nowTaskType == TYPE_DOWN_PARAMETENFCWHITENAMELISTFINISH)
                                ) {
                                    //提示成功
                                    MLog.i("下载成功")
                                    success.value = true
                                } else {
                                    val nextSuccess = when (nowTaskType) {
                                        TYPE_QUERY_CAPK_VERSION -> downParam(if(isQureyFinished) TYPE_DOWN_PARAMETERCAPK else TYPE_QUERY_CAPK_VERSION)
                                        TYPE_DOWN_PARAMETERCAPK -> if(packResult != ERR_NO_NEED_UPDATE) downParam(TYPE_DOWN_PARAMETERCAPK) else true
                                        TYPE_QUERY_AID_VERSION  -> downParam(if (isQureyFinished) TYPE_DOWN_PARAMETERAID else TYPE_QUERY_AID_VERSION)
                                        TYPE_DOWN_PARAMETERAID  -> if(packResult != ERR_NO_NEED_UPDATE) downParam(TYPE_DOWN_PARAMETERAID) else true
                                        TYPE_DOWN_PARAMETENFC   -> downParam(TYPE_DOWN_PARAMETENFCFINISH)
                                        TYPE_DOWN_PARAMETERBLACKLIST   -> downParam(TYPE_DOWN_PARAMETERBLACKLISTFINISH)
                                        else -> true
                                    }
                                    success.value = nextSuccess
                                }

                            } else if (!TextUtils.isEmpty(mPackData.responseCode) && "00" == mPackData.responseCode && result == TPPOSUtil.ERR_NO_PARA) {
                                //resultMsg = "下载完成";
                                MLog.i("没参数--" + nowTaskType)
                                if (nowTaskType == TYPE_DOWN_PARAMETERBLACKLIST) {
                                    downParam(TYPE_DOWN_PARAMETERBLACKLISTFINISH)
                                } else {
                                    //val noParam = "失败,未找到该参数" ;
                                    viewModel._msg.postValue("失败,未找到该参数")
                                    success.value = false
                                }
                            } else {
                                if (!TextUtils.isEmpty(mPackData.responseCode) && "00" != mPackData.responseCode) {
                                    val resultMsg =
                                        "失败,应答码: " + mPackData.responseCode + "\n" + ErrorMsg.getesponseCodeMsgInf(
                                            mPackData.responseCode
                                        );
                                    viewModel._msg.postValue(resultMsg)
                                } else {
                                    //val resultMsg = "下载异常,请重试";
                                    viewModel._msg.postValue("下载异常,请重试")
                                }
                                success.value = false
                            }
                        }
                    }

                    is SocketResult.Error -> {
                        viewModel._loading.postValue("")
                        MLog.i("SocketResult.Error in" + result.message)
                        viewModel._msg.postValue("下载失败:" + result.message)
                        success.value = false
                    }
                }
            }?: run {
                // 网络库未初始化
                success.value = false
            }
        }

        // 等待结果
        success.filterNotNull().first()
    }


    /**
     * 获取《查询CAPK参数版本》的8583数据包
     *
     * @return
     */
    fun obtainQueryCAPKVersion(mPOSData: POSData, mPackData: PackData): Int {
        mPOSData.terminalID = GlobalParams.get_terminalNo(context)//"05315812"
        mPOSData.merchantID = GlobalParams.get_merchantNo(context)//"898330160120021"
        mPOSData.batchNo = GlobalParams.get_BatchNo(context)//"001686"
        if (isQureyFirst){
            mPOSData.sFile62 = "100"
        }else {
            mPOSData.sFile62 = "000"
        }

        val i: Int = PackUtil.packMessage_QureyCAPKParaVer(context, mPOSData, mPackData)

        MLog.i(
            "TPPOSUtil.packData: " + i + "\nData: " + StringUtil.bytesToHexString_upcase(
                mPackData.data_send
            )
        )

        return i
    }

    /**
     * 获取《下载capk》的8583数据包
     *
     * @return
     */
    fun obtainDownParameterCAPK(mPOSData: POSData, mPackData: PackData): Int {
        mPOSData.terminalID = GlobalParams.get_terminalNo(context)//"05315812"
        mPOSData.merchantID = GlobalParams.get_merchantNo(context)//"898330160120021"
        mPOSData.batchNo = GlobalParams.get_BatchNo(context)//"001686"
        val i = PackUtil.packMessage_DownParaCAPK(context, mPOSData, mPackData);
        return i
    }

    fun obtainDownParameterCAPKFinished(mPOSData: POSData, mPackData: PackData): Int {
        mPOSData.terminalID = GlobalParams.get_terminalNo(context)//"05315812"
        mPOSData.merchantID = GlobalParams.get_merchantNo(context)//"898330160120021"
        mPOSData.batchNo = GlobalParams.get_BatchNo(context)//"001686"
        val i: Int = PackUtil.packMessage_DownLoadCAPKParaFinish(context, mPOSData, mPackData)
        MLog.i(
            "TPPOSUtil.packData: " + i + "\nData: " + StringUtil.bytesToHexString_upcase(
                mPackData.data_send
            )
        )
        return i
    }

    fun obtainDownParameterAIDFinished(mPOSData: POSData, mPackData: PackData): Int {
        mPOSData.terminalID = GlobalParams.get_terminalNo(context)//"05315812"
        mPOSData.merchantID = GlobalParams.get_merchantNo(context)//"898330160120021"
        mPOSData.batchNo = GlobalParams.get_BatchNo(context)//"001686"
        val i: Int = PackUtil.packMessage_DownLoadAIDParaFinish(context, mPOSData, mPackData)
        MLog.i(
            "TPPOSUtil.packData: " + i + "\nData: " + StringUtil.bytesToHexString_upcase(
                mPackData.data_send
            )
        )
        return i
    }

    /**
     * 获取《查询AID参数版本》的8583数据包
     *
     * @return
     */
    fun obtainQueryAIDVersion(mPOSData: POSData, mPackData: PackData): Int {
        mPOSData.terminalID = GlobalParams.get_terminalNo(context)//"05315812"
        mPOSData.merchantID = GlobalParams.get_merchantNo(context)//"898330160120021"
        mPOSData.batchNo = GlobalParams.get_BatchNo(context)//"001686"
        if (isQureyFirst) {
            mPOSData.sFile62 = "100"
        } else {
            mPOSData.sFile62 = "000"
        }

        val i: Int = PackUtil.packMessage_QureyAIDParaVer(context, mPOSData, mPackData)
        MLog.i(
            "TPPOSUtil.packData: " + i + "\nData: " + StringUtil.bytesToHexString_upcase(
                mPackData.data_send))
        if (i == TPPOSUtil.ERR_DF27) {
            MLog.i("缺少DF27")
        }

        return i
    }

    /**
     * 获取《下载IC卡参数AID》的8583数据包
     *
     * @return
     */
    fun obtainDownParameterAID(mPOSData: POSData, mPackData: PackData): Int {
        mPOSData.terminalID = GlobalParams.get_terminalNo(context)//"05315812"
        mPOSData.merchantID = GlobalParams.get_merchantNo(context)//"898330160120021"
        mPOSData.batchNo = GlobalParams.get_BatchNo(context)//"001686"
        val i: Int = PackUtil.packMessage_DownParaAID(context, mPOSData, mPackData)
        MLog.i(
            "TPPOSUtil.packData: " + i + "\nData: " + StringUtil.bytesToHexString_upcase(
                mPackData.data_send))

        return i
    }

    fun obtainDownParameterNFC(mPOSData: POSData, mPackData: PackData): Int {
        mPOSData.terminalID = GlobalParams.get_terminalNo(context)//"05315812"
        mPOSData.merchantID = GlobalParams.get_merchantNo(context)//"898330160120021"
        mPOSData.batchNo = GlobalParams.get_BatchNo(context)//"001686"
        val i: Int = PackUtil.packMessage_DownParaNFC(context, mPOSData, mPackData)
        MLog.i(
            "TPPOSUtil.packData: " + i + "\nData: " + StringUtil.bytesToHexString_upcase(
                mPackData.data_send
            )
        )
        return i
    }

    fun obtainNFCBlackList(mPOSData: POSData, mPackData: PackData): Int {
        mPOSData.terminalID = GlobalParams.get_terminalNo(context)//"05315812"
        mPOSData.merchantID = GlobalParams.get_merchantNo(context)//"898330160120021"
        mPOSData.batchNo = GlobalParams.get_BatchNo(context)//"001686"
        val i: Int = PackUtil.packMessage_DownParaNFCBlackList(context, mPOSData, mPackData)
        MLog.i(
            "TPPOSUtil.packData: " + i + "\nData: " + StringUtil.bytesToHexString_upcase(
                mPackData.data_send
            )
        )
        return i
    }

    fun obtainNFCBlackListFinish(mPOSData: POSData, mPackData: PackData): Int {
        mPOSData.terminalID = GlobalParams.get_terminalNo(context)//"05315812"
        mPOSData.merchantID = GlobalParams.get_merchantNo(context)//"898330160120021"
        mPOSData.batchNo = GlobalParams.get_BatchNo(context)//"001686"
        val i: Int = PackUtil.packMessage_DownParaNFCBlackListFinish(context, mPOSData, mPackData)
        MLog.i(
            "TPPOSUtil.packData: " + i + "\nData: " + StringUtil.bytesToHexString_upcase(
                mPackData.data_send
            )
        )
        return i
    }




}