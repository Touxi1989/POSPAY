package com.telpo.pospay.main.activity

import android.content.Intent
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.telpo.base.activity.BaseViewModelActivity
import com.telpo.base.util.MLog
import com.telpo.pospay.databinding.ActivityMainmenuBinding
import com.telpo.pospay.main.viewmodel.MainMenuViewModel
import com.telpo.pospay.main.viewmodel.NavigationEvent
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class MainMenuActivity() : BaseViewModelActivity<ActivityMainmenuBinding>(){

    val viewModel by viewModels<MainMenuViewModel>()

    override fun initView() {
        super.initView()
    }


    override fun initDatum() {
        super.initDatum()

        initViewModel(viewModel)

        // 观察时间Flow并更新UI
        observeTimeUpdates()

        // 观察导航事件并处理跳转
        observeNavigationEvents()
    }

    override fun initListeners() {
        super.initListeners()


        // 绑定所有按钮点击事件
        bindClickEvents()

    }

    private fun observeTimeUpdates() {
        lifecycleScope.launch {
            // 当Activity处于STARTED状态时才收集数据，避免内存泄漏
            repeatOnLifecycle(androidx.lifecycle.Lifecycle.State.STARTED) {
                viewModel.currentTimeFlow.collectLatest { time ->
                    // 更新TextView显示
                    binding.time.text = time
                }
            }
        }
    }

    private fun bindClickEvents() {
        // 微信消费按钮
        binding.bnWechat.setOnClickListener {
            viewModel.onWeiXinSaleClick()
        }
        // 支付宝消费按钮
        binding.bnAlipay.setOnClickListener {
            viewModel.onAlipaySaleClick()
        }
        // 消费按钮
        binding.bnSale.setOnClickListener {
            viewModel.onSaleClick()
        }
        // 撤销按钮
        binding.bnVoid.setOnClickListener {
            viewModel.onVoidClick()
        }
        // 退货按钮
        binding.bnRefund.setOnClickListener {
            val currentOrderId = "ORDER_123456" // 实际项目中可能从UI获取
            viewModel.onRefundClick(currentOrderId)
        }
        // 查余额按钮
        binding.bnBalance.setOnClickListener {
            viewModel.onBalanceClick()
        }
        // 管理按钮
        binding.bnManage.setOnClickListener {
            viewModel.onManageClick()
        }

    }

    private fun observeNavigationEvents() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.navigationEvents.collectLatest { event ->
                    when(event){
                        is NavigationEvent.ToSale -> {}
                        is NavigationEvent.ToHistory ->{}
                        is NavigationEvent.ToSettings ->{}
                        is NavigationEvent.ToRefund ->{}
                        is NavigationEvent.ToAlipaySale -> {}
                        is NavigationEvent.ToBalance -> {
                            MLog.i("点击了查余额");
                            // 执行跳转（Activity职责）
                            val intent = Intent(this@MainMenuActivity, BalanceActivity::class.java)
                            startActivity(intent)
                        }
                        is NavigationEvent.ToVoid -> {}
                        is NavigationEvent.ToWeiXinSale -> {}
                    }
                }
            }
        }
    }


}