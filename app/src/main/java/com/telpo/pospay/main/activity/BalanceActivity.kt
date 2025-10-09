package com.telpo.pospay.main.activity

import android.view.KeyEvent
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.telpo.base.activity.BaseViewModelActivity
import com.telpo.pospay.databinding.ActivityBalanceBinding
import com.telpo.pospay.main.viewmodel.BalanceState
import com.telpo.pospay.main.viewmodel.BalanceViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.getValue

class BalanceActivity() : BaseViewModelActivity<ActivityBalanceBinding>(){
    val viewModel by viewModels<BalanceViewModel>()

    override fun initView() {
        super.initView()

        // 设置标题
        binding.titleBar.titleText.text = "查余额1"
        
        // 初始化各页面的可见性
        updateUIForState(viewModel.state.value)
    }



    override fun initDatum() {
        super.initDatum()

        // 观察状态变化并更新UI
        lifecycleScope.launch {
            viewModel.state.collectLatest { state ->
                updateUIForState(state)
            }
        }

    }

    override fun initListeners() {
        super.initListeners()

        // 页面1 标题栏返回按钮
        binding.titleBar.btnBack.setOnClickListener {
            finish()
        }

        // 页面2 取消卡号按钮
        binding.cancelCardBtn.setOnClickListener {
            finish()
        }
        // 页面2 确认卡号按钮
        binding.confirmCardBtn.setOnClickListener {
            viewModel.confirmCard()
        }

        // 页面3 暂时不处理

        // 页面4 暂时不处理

        // 页面5 确认退出
        binding.resultBtn.setOnClickListener {
            finish()
        }
    }

    private fun updateUIForState(state: BalanceState) {
        // 重置所有页面为不可见
        binding.cardReadingView.visibility = android.view.View.GONE
        binding.confirmCardView.visibility = android.view.View.GONE
        binding.inputPasswordView.visibility = android.view.View.GONE
        binding.communicatingView.visibility = android.view.View.GONE
        binding.resultView.visibility = android.view.View.GONE

        // 根据状态显示对应的页面
        when (state) {
            is BalanceState.CardReading -> {
                binding.cardReadingView.visibility = android.view.View.VISIBLE
                // 显示读卡器状态
                //binding.readerStatus.text = if (viewModel.isReaderActive.value) "读卡器已打开，请刷银行卡" else "等待启动..."
            }
            is BalanceState.ConfirmCard -> {
                binding.confirmCardView.visibility = android.view.View.VISIBLE
                binding.cardNoText2.text = "123"
                //binding.bankInfoText.text = "${state.cardInfo.bankName} ${state.cardInfo.cardType}"
            }
            is BalanceState.InputPassword -> {
                binding.inputPasswordView.visibility = android.view.View.VISIBLE
                //binding.passwordInput.text.clear()
            }
            is BalanceState.Communicating -> {
                binding.communicatingView.visibility = android.view.View.VISIBLE
            }
            is BalanceState.Result -> {
                binding.resultView.visibility = android.view.View.VISIBLE
                binding.resultCardNo.text = "卡号: ${state.cardNo}"
                binding.balanceText.text = "当前余额: ¥${state.balance}"
            }
        }
    }

    // 处理返回键
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return if (viewModel.onBackPressed()) {
                true // 已处理返回逻辑
            } else {
                super.onKeyDown(keyCode, event) // 未处理，执行默认返回
            }
        }
        return super.onKeyDown(keyCode, event)
    }

}