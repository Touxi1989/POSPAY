package com.telpo.pospay.main.activity

import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.telpo.base.activity.BaseViewModelActivity
import com.telpo.base.util.MLog
import com.telpo.base.util.PermissionHelper
import com.telpo.base.util.StringUtil
import com.telpo.pospay.AppContext
import com.telpo.pospay.databinding.ActivityMainBinding
import com.telpo.pospay.db.bean.DiaryEntry
import com.telpo.pospay.main.viewmodel.MainViewModel
import com.telpo.pospay.main.viewmodel.MainViewModelFactory
import com.telpo.pospay.main.util.PackUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : BaseViewModelActivity<ActivityMainBinding>(),
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

        //        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        initViewModel(viewModel)


        // 监听数据库变化
        lifecycleScope.launch {
            viewModel.allDiaries.collect { diaryList ->
                //更新UI
                MLog.i("allDiaries:" + diaryList.size)
            }


        }


    }

    override fun initListeners() {
        super.initListeners()
        binding.test.setOnClickListener {

            MLog.i("OnClick")
            val aa = byteArrayOf(0x00, 0x02, 0x11, 0x22)
            showLoading("测试socket")
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    viewModel.sendPackData(aa)?.collect {
                        MLog.i("OnClick-接收数据:" + StringUtil.bytesToHexString(it))
                        hideLoading()
                    }
                }

            }


        }

        binding.testDb.setOnClickListener {
            lifecycleScope.launch {
                val newEntry = DiaryEntry().also {
                    it.title = "新日记"
                    it.content = "今天很开心！"
                    it.date = System.currentTimeMillis()
                }

//                viewModel.addDiary(newEntry)


            }


            viewModel.findAllByField("id", 1)
            PackUtil.testDb()

        }

        binding.testHttp.setOnClickListener {
            showLoading("获取token")
            lifecycleScope.launch {
                viewModel.deviceToken().collect {
                    MLog.i("deviceToken:" + it.getAccess_token())
                    hideLoading()
                }
            }
        }
    }
}