package com.telpo.pospay.main.activity

import android.content.Intent
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.telpo.base.activity.BaseViewModelActivity
import com.telpo.base.util.PermissionHelper
import com.telpo.pospay.AppContext
import com.telpo.pospay.R
import com.telpo.pospay.databinding.ActivityStartappBinding
import com.telpo.pospay.main.viewmodel.StartAppViewModel
import com.telpo.pospay.main.viewmodel.StartAppViewModel.StartAppViewModelFactory
import com.telpo.pospay.main.dialog.CustomDialog.CustomDialogListener
import com.telpo.pospay.main.util.UIUtil
import kotlinx.coroutines.launch


class StartAppActivity : BaseViewModelActivity<ActivityStartappBinding>(),
    PermissionHelper.PermissionCallback {
    private lateinit var permissionHelper: PermissionHelper
    private val viewModel: StartAppViewModel by viewModels {
        StartAppViewModelFactory((application as AppContext).userRepository)
    }

    override fun onAllPermissionsGranted() {

    }

    override fun onPermissionDenied(deniedPermissions: List<String>) {

    }

    override fun initView() {
        super.initView()
    }

    override fun onResume() {
        super.onResume()
        binding.edittextUserPwd.setText("");
        binding.edittextUserNo.requestFocus();
    }

    override fun initDatum() {
        super.initDatum()

        permissionHelper = PermissionHelper(this)
        permissionHelper.checkAllPermissions(this)

//        viewModel = ViewModelProvider(this)[StartAppViewModel::class.java]
        initViewModel(viewModel)
        viewModel.initUser()

        // 监听登录结果
        lifecycleScope.launch {
            viewModel.loginResult.collect { success ->
                success?.let {
                    if (it) {
                        // 登录/签到成功，跳转到MainActivity
                        startActivity(Intent(this@StartAppActivity, MainMenuActivity::class.java))
                        finish()
                    } else {
                        // 失败情况，提示信息已经通过_msg发送
                    }
                }
            }
        }

        // 监听消息提示
//        lifecycleScope.launch {
//            viewModel._msg.collect { msg ->
//                msg?.let {
//                    Toast.makeText(this@StartAppActivity, it, Toast.LENGTH_SHORT).show()
//                }
//            }
//        }

    }

    override fun initListeners() {
        super.initListeners()
        binding.login.setOnClickListener {

            viewModel.login(
                binding.edittextUserNo.text.toString(),
                binding.edittextUserPwd.text.toString()
            )
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.edittextUserPwd.windowToken, 0)


        }

        binding.exit.setOnClickListener {
            UIUtil.showCustomDialog(
                hostActivity,
                getString(R.string.bn_confirm),
                getString(R.string.bn_cancel),
                getString(
                    R.string.exit_warning
                ),
                getString(R.string.exit_isexit),
                false,
                0,
                object : CustomDialogListener {
                    override fun onYesClick() {

                        viewModel.finish()
                    }

                    override fun onNoClick() {
                        return
                    }

                    override fun timeOut() {
                    }
                })
        }
    }


}