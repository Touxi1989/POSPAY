package com.telpo.pospay.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.alibaba.fastjson.JSONObject
import com.telpo.base.internet.response.TokenResponse
import com.telpo.base.model.BaseViewModel
import com.telpo.base.util.TLog
import com.telpo.gxss.R
import com.telpo.pospay.db.bean.DiaryEntry
import com.telpo.pospay.repository.DiaryRepository
import com.telpo.pospay.repository.HttpRepository
import com.telpo.pospay.repository.SocketRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch


class MainViewModel(
    private val repository: DiaryRepository,
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


    fun sendPackData(sendData: ByteArray) = flow {
        var recvData = socketRepository.sendPackData(sendData)
        emit(recvData)
    }.catch { e ->
        // 处理异常
        TLog.d("sendPackData: 数据请求失败,${e.message}")
        _msg.postValue("sendPackData: 数据请求失败,${e.message}")
    }


    fun deviceToken(): Flow<TokenResponse> = flow {

        val response = httpRepository.deviceToken()
        val code = response?.code()
        if (code == 200) {
            TLog.i("response=" + JSONObject.toJSONString(response.body()))
            _tip.value = R.string.success
            emit(response.body() as TokenResponse)
        } else {
            _msg.value = "http:$code,${response?.errorBody()?.string()}"

        }
    }.catch { e ->
        // 处理异常
        TLog.d( "deviceToken: 数据请求失败,${e.message}")
        _msg.value = "deviceToken: 数据请求失败,${e.message}"
    }




    suspend fun addDiary(entry: DiaryEntry) {
        repository.addDiary(entry)
    }

    suspend fun deleteDiary(entry: DiaryEntry) {
        repository.deleteDiary(entry)
    }

    val allDiaries = repository.allDiaries


}

class MainViewModelFactory(
    private val repository: DiaryRepository,
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