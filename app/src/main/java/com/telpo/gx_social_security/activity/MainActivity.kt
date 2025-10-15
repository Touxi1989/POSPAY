package com.telpo.gx_social_security.activity

import com.telpo.base.activity.BaseViewModelActivity
import com.telpo.base.util.PermissionHelper
import com.telpo.gxss.databinding.ActivityMainBinding


/**
 * 公共招聘信息页面
 */
class MainActivity : BaseViewModelActivity<ActivityMainBinding>(),
    PermissionHelper.PermissionCallback {





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




    }

    override fun initListeners() {
        super.initListeners()

        binding.lyGgzpxx.setOnClickListener {
            startActivity(GgzpxxActivity::class.java)
        }


    }
}