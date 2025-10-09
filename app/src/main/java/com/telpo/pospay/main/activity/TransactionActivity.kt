package com.telpo.pospay.main.activity

import android.content.Intent
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.telpo.base.activity.BaseCommonActivity
import com.telpo.base.activity.BaseViewModelActivity
import com.telpo.pospay.databinding.ActivityTransactionBinding
import com.telpo.pospay.main.Devices.PosCardReaderImpl
import com.telpo.pospay.main.Devices.PosPinPadImpl
import com.telpo.pospay.main.Devices.PosTransactionRepoImpl
import com.telpo.pospay.main.TransactionState
import com.telpo.pospay.main.TransactionType
import com.telpo.pospay.main.viewmodel.TransactionViewModel
import com.telpo.pospay.main.viewmodel.TransactionViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class TransactionActivity : BaseViewModelActivity<ActivityTransactionBinding>() {

    // ViewModel初始化（通过工厂注入依赖）
    private val viewModel: TransactionViewModel by viewModels {
        TransactionViewModelFactory(
            cardReader = PosCardReaderImpl(),
            pinPad = PosPinPadImpl(),
            transactionRepo = PosTransactionRepoImpl()
        )
    }

    // 外部传参Key（静态常量，供外部页面调用）
    companion object {
        const val EXTRA_TRANSACTION_TYPE = "com.pos.bankcard.EXTRA_TRANSACTION_TYPE"
        fun start(context: BaseCommonActivity<*>, type: TransactionType) {
            val intent = Intent(context, TransactionActivity::class.java)
            intent.putExtra(EXTRA_TRANSACTION_TYPE, type)
            context.startActivity(intent)
        }
    }
    /**
     * 初始化数据（观察状态流）
     */
    private fun initDatum() {
        lifecycleScope.launch {
            // 观察交易状态变化，更新UI
            viewModel.state.collectLatest { state ->
                updateUIForState(state)
                // 错误提示
                if (state is TransactionState.Error) {
                    Toast.makeText(this@TransactionActivity, state.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    /**
     * 初始化点击事件
     */
    private fun initListeners() {
        // 返回按钮
        binding.titleBar.btnBack.setOnClickListener { onBackPressed() }

        // 1. 读卡相关：重新读卡
        binding.cardReadingView.reReadCardBtn.setOnClickListener {
            viewModel.startReadCard()
        }

        // 2. 确认卡号：下一步
        binding.confirmCardView.confirmBtn.setOnClickListener {
            viewModel.confirmCard()
        }
        binding.confirmCardView.cancelBtn.setOnClickListener { onBackPressed() }

        // 3. IC卡应用选择：列表点击
        binding.selectAppView.appListView.onItemClickListener = { _, _, position, _ ->
            val appName = (binding.selectAppView.appListView.adapter as ArrayAdapter).getItem(position) as String
            viewModel.selectCardApp(appName)
        }

        // 4. 消费金额确认
        binding.confirmConsumptionView.confirmAmountBtn.setOnClickListener {
            val amount = binding.confirmConsumptionView.amountEdit.text.toString().trim()
            viewModel.confirmConsumptionAmount(amount)
        }
        binding.confirmConsumptionView.cancelBtn.setOnClickListener { onBackPressed() }

        // 5. 撤销交易号确认
        binding.confirmCancelView.confirmTransNoBtn.setOnClickListener {
            val transNo = binding.confirmCancelView.transNoEdit.text.toString().trim()
            viewModel.confirmCancelTransNo(transNo)
        }
        binding.confirmCancelView.cancelBtn.setOnClickListener { onBackPressed() }

        // 6. 结果页：完成
        binding.resultView.finishBtn.setOnClickListener { finish() }

        // 7. 缺纸处理：继续打印
        binding.printLackView.continueBtn.setOnClickListener {
            // 触发重新打印（需结合POS打印逻辑扩展）
            binding.printLackView.visibility = View.GONE
        }
    }

    /**
     * 根据交易类型获取标题
     */
    private fun getTransTitle(type: TransactionType): String {
        return when (type) {
            TransactionType.BALANCE_QUERY -> "查余额"
            TransactionType.CONSUMPTION -> "银行卡消费"
            TransactionType.TRANSACTION_CANCEL -> "交易撤销"
            TransactionType.EC_ENQUIRY -> "电子现金查询"
            TransactionType.SETTLE -> "交易结算"
        }
    }

    /**
     * 根据状态更新UI（核心：状态驱动UI）
     */
    private fun updateUIForState(state: TransactionState) {
        // 先隐藏所有视图
        hideAllViews()

        when (state) {
            // 1. 读卡中
            is TransactionState.CardReading -> {
                binding.cardReadingView.root.visibility = View.VISIBLE
                binding.cardReadingView.readerStatus.text = "请刷银行卡（支持磁条/IC/NFC）"
            }

            // 2. 确认卡号
            is TransactionState.ConfirmCard -> {
                binding.confirmCardView.root.visibility = View.VISIBLE
                binding.confirmCardView.cardNoText.text = state.cardNo
                binding.confirmCardView.bankNameText.text = state.bankName
            }

            // 3. 选择IC卡应用
            is TransactionState.SelectCardApp -> {
                binding.selectAppView.root.visibility = View.VISIBLE
                val adapter = ArrayAdapter(
                    this,
                    android.R.layout.simple_list_item_1,
                    state.appList
                )
                binding.selectAppView.appListView.adapter = adapter
            }

            // 4. 确认消费金额
            is TransactionState.ConfirmConsumption -> {
                binding.confirmConsumptionView.root.visibility = View.VISIBLE
                binding.confirmConsumptionView.cardNoText.text = state.cardNo
                binding.confirmConsumptionView.bankNameText.text = state.bankName
                binding.confirmConsumptionView.amountEdit.setText(state.amount)
            }

            // 5. 确认撤销交易号
            is TransactionState.ConfirmCancel -> {
                binding.confirmCancelView.root.visibility = View.VISIBLE
                binding.confirmCancelView.transNoEdit.setText(state.originalTransNo)
            }

            // 6. PIN输入中
            is TransactionState.PinInputting -> {
                binding.pinInputView.root.visibility = View.VISIBLE
                binding.pinInputView.amountText.text = "交易金额：¥${state.amount}"
            }

            // 7. 通信中
            is TransactionState.Communicating -> {
                binding.communicatingView.root.visibility = View.VISIBLE
                binding.communicatingView.msgText.text = state.message
                // 启动加载动画（假设布局中有ProgressBar）
                binding.communicatingView.loadingBar.visibility = View.VISIBLE
            }

            // 8. 查余额结果
            is TransactionState.BalanceResult -> {
                binding.resultView.root.visibility = View.VISIBLE
                binding.resultView.titleText.text = "查询成功"
                binding.resultView.detailText.text = "卡号：${state.cardNo}\n当前余额：¥${state.balance}"
            }

            // 9. 消费结果
            is TransactionState.ConsumptionResult -> {
                binding.resultView.root.visibility = View.VISIBLE
                binding.resultView.titleText.text = "消费成功"
                binding.resultView.detailText.text =
                    "卡号：${state.cardNo}\n消费金额：¥${state.amount}\n交易号：${state.transNo}\n批次号：${state.batchNo}"
            }

            // 10. 撤销结果
            is TransactionState.CancelResult -> {
                binding.resultView.root.visibility = View.VISIBLE
                binding.resultView.titleText.text = if (state.cancelSuccess) "撤销成功" else "撤销失败"
                binding.resultView.detailText.text = "原交易号：${state.originalTransNo}\n提示：${state.message}"
            }

            // 11. 缺纸提示
            is TransactionState.PrintLack -> {
                binding.printLackView.root.visibility = View.VISIBLE
            }

            // 12. 错误状态（仅提示，不单独显示视图）
            is TransactionState.Error -> {}
        }
    }

    /**
     * 隐藏所有视图容器
     */
    private fun hideAllViews() {
        binding.cardReadingView.root.visibility = View.GONE
        binding.confirmCardView.root.visibility = View.GONE
        binding.selectAppView.root.visibility = View.GONE
        binding.confirmConsumptionView.root.visibility = View.GONE
        binding.confirmCancelView.root.visibility = View.GONE
        binding.pinInputView.root.visibility = View.GONE
        binding.communicatingView.root.visibility = View.GONE
        binding.resultView.root.visibility = View.GONE
        binding.printLackView.root.visibility = View.GONE
    }

    /**
     * 返回键处理（委托给ViewModel）
     */
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return if (viewModel.onBackPressed()) {
                true // ViewModel已处理回退
            } else {
                super.onKeyDown(keyCode, event) // 退出页面
            }
        }
        return super.onKeyDown(keyCode, event)
    }




}