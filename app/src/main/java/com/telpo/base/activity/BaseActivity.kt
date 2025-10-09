package com.telpo.base.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity : AppCompatActivity() {

    protected open fun initView() {}

    protected open fun initDatum() {}

    protected open fun initListeners() {}


    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        initView()
        initDatum()
        initListeners()
    }
}