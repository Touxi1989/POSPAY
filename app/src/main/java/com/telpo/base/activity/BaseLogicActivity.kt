package com.telpo.base.activity

import android.util.Log
import android.widget.Toast
import com.loading.dialog.IOSLoadingDialog
import com.telpo.AppContext
import com.telpo.base.model.BaseViewModel
import com.telpo.base.util.TLog
import com.telpo.gxss.R
import org.apache.commons.lang3.StringUtils
import java.lang.ref.WeakReference

open class BaseLogicActivity : BaseCommonActivity() {
    private var loadingDialog: WeakReference<IOSLoadingDialog>? = null


    /**
     * 获取界面的方法
     * 全局 Content
     * @return
     */
    protected val hostActivity: BaseLogicActivity
        get() = this


    /**
     * 初始化通用ViewModel逻辑
     */
    protected fun initViewModel(viewModel: BaseViewModel) { //关闭界面
        viewModel.finishPage.observe(this) {
            hostActivity.finish()
        }

        //本地提示
        viewModel.tip.observe(this) {
            onTip(it)
        }

        //本地提示
        viewModel.msg.observe(this) {
            onTipMsg(it)
        }

        //异常
        viewModel.exception.observe(this) {
            onException(it)
        }

        //网络响应业务失败
        viewModel.response.observe(this) { //            onResponse(it)
        }

        //加载提示
        viewModel.loading.observe(this) {
            if (StringUtils.isNotBlank(it)) showLoading(it) else hideLoading()
        }
    }


    open fun onTip(data: Int) {
        hideLoading()
        data.shortToast()
        onError()
    }

    open fun onTipMsg(data: String) {
        hideLoading()
        data.longToast()
        onError()
    }


    private fun onException(data: Throwable) {
        onError()
    }

    open fun onError() {

    }


    //region 加载提示
    /**
     * 显示加载对话框
     */
    open fun showLoading(data: Int) {
        showLoading(getString(data))
    }

    /**
     * 显示加载对话框
     */
    open fun showLoading(data: String = getString(R.string.loading)) {
        TLog.d( "showLoading: " + data)
        if (loadingDialog == null || loadingDialog!!.get() == null) {
            loadingDialog = WeakReference(
                IOSLoadingDialog().setOnTouchOutside(false))
        }

        val dialogData = loadingDialog!!.get()
        dialogData?.setHintMsg(data)
        if (dialogData!!.dialog == null || !dialogData!!.dialog!!.isShowing) {
            dialogData!!.show(supportFragmentManager, "LoadingDialog")
        }
    }

    /**
     * 隐藏加载对话框
     */
    fun hideLoading() {
        loadingDialog?.get()?.dismiss()
    } //endregion

    /**
     * 短toast
     */
    fun Int.shortToast() {
        Toast.makeText(AppContext.instance, this, Toast.LENGTH_SHORT).show()
    }

    /**
     * 长toast
     */
    fun Int.longToast() {
        Toast.makeText(AppContext.instance, this, Toast.LENGTH_LONG).show()
    }

    fun String.longToast() {
        Toast.makeText(AppContext.instance, this, Toast.LENGTH_LONG).show()
    }

}