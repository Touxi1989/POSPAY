package com.telpo.gx_social_security.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.alibaba.fastjson.JSONObject
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.telpo.base.internet.response.TokenResponse
import com.telpo.base.model.BaseViewModel
import com.telpo.base.util.TLog
import com.telpo.gx_social_security.bean.BaseResponse
import com.telpo.gx_social_security.bean.GgzpxxRequest
import com.telpo.gx_social_security.bean.GgzpxxResult
import com.telpo.gxss.R
import com.telpo.pospay.repository.HttpRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.lang.reflect.Type


class GgzpxxViewModel (
    private val httpRepository: HttpRepository
) : BaseViewModel() {




    private val _count = MutableSharedFlow<Int>()
    val count: Flow<Int> = _count




    init {
        viewModelScope.launch {
            httpRepository.getNetworkState().collect { isConnected ->
                _networkState.value = isConnected
            }
        }
    }


    fun deviceToken(): Flow<TokenResponse> = flow {

        val response = httpRepository.deviceToken1()
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
        TLog.d("deviceToken: 数据请求失败,${e.message}")
        _msg.value = "deviceToken: 数据请求失败,${e.message}"
    }


    fun queryGgzpxx(ggzpxxRequest: GgzpxxRequest): Flow<GgzpxxResult> = flow {

        val response = httpRepository.queryGgzpxx(ggzpxxRequest)
        val code = response?.code()
        if (code == 200) {
            TLog.i("response=" + JSONObject.toJSONString(response.body()))
            var gson = Gson()
            val type: Type = object : TypeToken<BaseResponse<GgzpxxResult>>() {}.type
            val response: BaseResponse<GgzpxxResult> =
                    gson.fromJson(response.body() as String, type)
            if (response.errorCode == "200") {
                _tip.value = R.string.success
                emit(response.result)
            } else {
                _msg.value = response.message
            }
        } else {
            _msg.value = "http:$code,${response?.errorBody()?.string()}"

        }
    }.catch { e ->
        // 处理异常
        TLog.d("queryGgzpxx: 数据请求失败,${e.message}")
        _msg.value = "queryGgzpxx: 数据请求失败,${e.message}"
    }

}

class GgzpxxModelFactory(
    private val httpRepository: HttpRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GgzpxxViewModel::class.java)) {
            return GgzpxxViewModel(httpRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}