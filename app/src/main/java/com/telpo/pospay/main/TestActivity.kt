package com.telpo.pospay.main

import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.telpo.AppContext
import com.telpo.base.activity.BaseViewModelActivity
import com.telpo.base.util.PermissionHelper
import com.telpo.base.util.StringUtil
import com.telpo.base.util.TLog
import com.telpo.gxss.databinding.ActivityTestBinding
import com.telpo.pospay.db.bean.DiaryEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class TestActivity : BaseViewModelActivity<ActivityTestBinding>(),
    PermissionHelper.PermissionCallback {


    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory(
            (application as AppContext).diaryRepository,
            (application as AppContext).socketRepository,
            (application as AppContext).httpRepository
        )
    }


    private lateinit var permissionHelper: PermissionHelper
    override fun onAllPermissionsGranted() {

    }

    override fun onPermissionDenied(deniedPermissions: List<String>) {

    }

    override fun initView() {
        super.initView()
    }

    override fun initDatum() {
        super.initDatum()

        permissionHelper = PermissionHelper(this)
        permissionHelper.checkAllPermissions(this)

        initViewModel(viewModel)

        // 监听数据库变化
        lifecycleScope.launch {
            viewModel.allDiaries.collect { diaryList ->
                //更新UI
                TLog.i("allDiaries:" + diaryList.size)
            }
        }

    }

    override fun initListeners() {
        super.initListeners()
        binding.test.setOnClickListener {

            TLog.i("OnClick")
            val aa = byteArrayOf(0x00, 0x02, 0x11, 0x22)

            showLoading("测试socket")
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    viewModel.sendPackData(aa).collect {
                        TLog.i("OnClick-接收数据:" + StringUtil.bytesToHexString(it))
                        hideLoading()
                    }
                }

            }

        }

        binding.testDb.setOnClickListener {
            lifecycleScope.launch {
                val newEntry = DiaryEntry(
                    title = "新日记",
                    content = "今天很开心！",
                    date = System.currentTimeMillis()
                )
                viewModel.addDiary(newEntry)
            }
        }

                binding.testHttp.setOnClickListener {
                    showLoading("获取token")
                    lifecycleScope.launch {
                        viewModel.deviceToken().collect {
                            TLog.i("deviceToken:" + it.getAccess_token())
                            hideLoading()
                        }
                    }
                }


    }
}