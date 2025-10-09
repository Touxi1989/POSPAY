package com.telpo.base.activity

import android.content.Intent
import com.telpo.base.util.Constant


/**
 * 通用界面逻辑
 */
open class BaseCommonActivity : BaseActivity() {
    /**
     * 启动界面
     * @param clazz
     */
    protected fun startActivity(clazz: Class<*>) {
        //创建Intent
        val intent = Intent(this, clazz)

        //启动界面
        startActivity(intent)
    }


    /**
     * 启动界面并关闭当前界面
     * @param clazz
     */
    fun startActivityAfterFinishThis(clazz: Class<*>) {
        startActivity(clazz)

        //关闭当前界面
        finish()
    }

    /**
     * 启动界面，可以传递一个字符串参数
     *
     * @param clazz
     * @param id
     */
    protected fun startActivityExtraId(
        clazz: Class<*>,
        id: String
    ) {
        //创建Intent
        val intent = Intent(this, clazz).apply {
            //传递数据
            //不为空才传递
            putExtra(Constant.ID, id)
        }

        //启动界面
        startActivity(intent)
    }

    /**
     * 获取字符串
     *
     * @param key
     * @return
     */
    protected fun extraString(key: String): String {
        return extraStringOrNull(key)!!
    }

    /**
     * 获取字符串
     *
     * @param key
     * @return
     */
    protected fun extraStringOrNull(key: String): String? {
        return intent.getStringExtra(key)
    }

    /**
     * 获取字符串类型Id
     *
     * @return
     */
    protected fun extraId(): String {
        return extraString(Constant.ID)
    }

    /**
     * 获取int值
     *
     * @param key
     * @return
     */
    protected fun extraInt(key: String): Int {
        return intent.getIntExtra(key, -1)
    }




}