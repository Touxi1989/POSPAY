package com.telpo.pospay.main.viewmodel

import com.telpo.base.model.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow



class BalanceViewModel  : BaseViewModel(){




    // 状态流，管理当前页面状态
    private val _state = MutableStateFlow<BalanceState>(BalanceState.CardReading)
    val state: StateFlow<BalanceState> = _state.asStateFlow()


    init {
        //启动读卡器
        startCardReader()
    }

    private fun startCardReader() {

    }

    fun confirmCard() {
        _state.value = BalanceState.InputPassword
    }

    // 返回上一步
    fun onBackPressed(): Boolean {
        return when (_state.value) {
            is BalanceState.ConfirmCard -> {
                false
            }
            is BalanceState.InputPassword -> {
                false
            }
            is BalanceState.Communicating -> {
                // 通讯中不允许返回
                false
            }
            is BalanceState.Result -> {
                // 结果页返回重新查询
                false
            }
            is BalanceState.CardReading -> {
                // 第一页返回则退出整个流程
                false
            }
        }
    }


}

// 定义页面状态密封类
sealed class BalanceState {
    object CardReading : BalanceState() // 1. 请刷银行卡
    object ConfirmCard : BalanceState() // 2. 确认银行卡号
    object InputPassword : BalanceState() // 3. 输入密码
    object Communicating : BalanceState() // 4. 通讯中
    data class Result(val balance: String, val cardNo: String) : BalanceState() // 5. 显示结果
}