package com.telpo.gx_social_security.activity

import android.content.Intent
import com.telpo.base.activity.BaseViewModelActivity
import com.telpo.gxss.databinding.ActivitySearchBinding
import com.telpo.searchview.ICallBack
import com.telpo.searchview.bCallBack

class SearchActivity : BaseViewModelActivity<ActivitySearchBinding>() {


    override fun initView() {
        super.initView()
    }

    override fun initListeners() {
        super.initListeners()
        binding.searchView.setOnClickSearch(object : ICallBack {
            override fun SearchAciton(string: String) {
                setResult(RESULT_OK, Intent().apply {
                    putExtra("searchContent", string)
                })
                finish()
            }
        })


        binding.searchView.setOnClickBack(object : bCallBack {
            override fun BackAciton() {
                finish()
            }
        })
    }
}