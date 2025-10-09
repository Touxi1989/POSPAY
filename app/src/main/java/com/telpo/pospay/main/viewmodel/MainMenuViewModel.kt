package com.telpo.pospay.main.viewmodel

import androidx.lifecycle.viewModelScope
import com.telpo.base.model.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainMenuViewModel() : BaseViewModel(){

    // 私有可修改的Flow，用于发送导航事件
    private val _navigationEvents = MutableSharedFlow<NavigationEvent>()

    // 暴露给外部的不可修改Flow，供View观察
    val navigationEvents: SharedFlow<NavigationEvent> = _navigationEvents.asSharedFlow()

    // 格式化时间显示，例如: "HH:mm:ss"
    private val timeFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())
    // 暴露当前时间的Flow
    val currentTimeFlow = flow {
        while (true) {
            // 获取当前时间并格式化
            val currentTime = timeFormat.format(Date())
            // 发送当前时间
            emit(currentTime)
            // 每秒发送一次
            delay(1000)
        }
    }

    fun onSaleClick() {
        viewModelScope.launch {
            // 这里可以添加业务逻辑校验（如检查网络、权限等）
            _msg.value = "点击了消费"
            _navigationEvents.emit(NavigationEvent.ToSale)
        }
    }

    fun onWeiXinSaleClick() {
        viewModelScope.launch {
            // 这里可以添加业务逻辑校验（如检查网络、权限等）
            _navigationEvents.emit(NavigationEvent.ToWeiXinSale)
        }
    }

    fun onAlipaySaleClick() {
        viewModelScope.launch {
            // 这里可以添加业务逻辑校验（如检查网络、权限等）
            _navigationEvents.emit(NavigationEvent.ToAlipaySale)
        }
    }

    fun onVoidClick() {
        viewModelScope.launch {
            // 这里可以添加业务逻辑校验（如检查网络、权限等）
            _navigationEvents.emit(NavigationEvent.ToVoid)
        }
    }

    fun onRefundClick(orderId: String) {
        viewModelScope.launch {
            // 这里可以添加业务逻辑校验（如检查网络、权限等）
            _navigationEvents.emit(NavigationEvent.ToRefund(orderId))
        }
    }

    fun onBalanceClick() {
        viewModelScope.launch {
            // 这里可以添加业务逻辑校验（如检查网络、权限等）
            _navigationEvents.emit(NavigationEvent.ToBalance)
        }
    }

    fun onManageClick() {
        viewModelScope.launch {
            // 这里可以添加业务逻辑校验（如检查网络、权限等）
            _navigationEvents.emit(NavigationEvent.ToSettings)
        }
    }


}

// 定义所有可能的页面跳转事件（密封类确保类型安全）
sealed class NavigationEvent {
    // 跳转到微信消费页面
    object ToWeiXinSale : NavigationEvent()
    // 跳转到支付宝消费页面
    object ToAlipaySale : NavigationEvent()
    // 跳转到消费页面
    object ToSale : NavigationEvent()
    // 跳转到撤销页面
    object ToVoid : NavigationEvent()
    // 跳转到退款页面（带参数示例）
    data class ToRefund(val orderId: String) : NavigationEvent()
    // 跳转到查余额页面
    object ToBalance : NavigationEvent()
    // 跳转到设置页面
    object ToSettings : NavigationEvent()
    // 跳转到历史记录页面
    object ToHistory : NavigationEvent()
}
