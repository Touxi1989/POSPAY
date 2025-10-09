package com.telpo.pospay.main.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.alibaba.fastjson.JSONObject
import com.telpo.base.internet.response.TokenResponse
import com.telpo.base.model.BaseViewModel
import com.telpo.base.util.MLog
import com.telpo.base.util.StringUtil
import com.telpo.pospay.R
import com.telpo.pospay.db.bean.DiaryEntry
import com.telpo.pospay.db.repository.DiaryDBRepository
import com.telpo.pospay.db.repository.NetWorkRepository
import com.telpo.pospay.db.repository.SocketResult
import com.telpo.pospay.repository.HttpRepository
import com.telpo.pospay.repository.SocketRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch


class MainViewModel(
    private val repository: DiaryDBRepository,
    private val socketRepository: SocketRepository,
    private val httpRepository: HttpRepository
) : BaseViewModel() {


    private val _count = MutableSharedFlow<Int>()
    val count: Flow<Int> = _count

    init {
        viewModelScope.launch {
            socketRepository.getNetworkState().collect { isConnected ->
                _networkState.value = isConnected
            }
        }
    }

    suspend fun sendPackData(sendData: ByteArray): Flow<ByteArray> = flow {
        // 初始化默认的错误数据（可选）
        var outdata: ByteArray = byteArrayOf()

        NetWorkRepository.instance?.sendSocketData(sendData)?.collect { result ->
            when (result) {
                is SocketResult.Success -> {
                    outdata = result.data
                    // 发射成功数据
                    emit(outdata)
                }
                is SocketResult.Error -> {
                    // 将错误信息转为字节数组（根据实际需求调整转换逻辑）
                    outdata = StringUtil.hexStringToByte(result.message) ?: byteArrayOf()
                    // 发射错误数据
                    emit(outdata)
                }
            }
        } ?: run {
            // 处理NetWorkRepository实例为null的情况
            val errorData = StringUtil.hexStringToByte("Network repository is null") ?: byteArrayOf()
            emit(errorData)
        }
    }


    fun deviceToken(): Flow<TokenResponse> = flow {
        val response = httpRepository.deviceToken()
        val code = response?.code()
        if (code == 200) {
            Log.i("xuxl", "response=" + JSONObject.toJSONString(response.body()))
            _tip.value = R.string.success
            emit(response.body() as TokenResponse)
        } else {
            //            Log.i("xuxl", "response=" + response?.errorBody()?.string())
            //            _tip.value = R.string.fail
            _msg.value = "http:$code,${response?.errorBody()?.string()}"

        }
    }.catch { e ->
        // 处理异常
        Log.d(TAG, "deviceToken: 数据请求失败")
        //                _tip.value = R.string.request_false
        Log.d(TAG, "deviceToken: ${e.message}")
        _msg.value = "deviceToken: 数据请求失败,${e.message}"
    }


    suspend fun addDiary(entry: DiaryEntry) {
        repository.addDiary(entry)
    }

    suspend fun deleteDiary(entry: DiaryEntry) {
        repository.deleteDiary(entry)
    }

    val allDiaries = repository.allDiaries

    fun findAllByField(fieldName: String, value: Any) {
        val list = repository.findAllByField(fieldName, value)
        MLog.i(list[0].title)

    }


}

class MainViewModelFactory(
    private val repository: DiaryDBRepository,
    private val socketRepository: SocketRepository,
    private val httpRepository: HttpRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(repository, socketRepository, httpRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}