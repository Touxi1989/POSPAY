package com.telpo.gx_social_security.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.telpo.AppContext
import com.telpo.base.activity.BaseViewModelActivity
import com.telpo.base.util.TLog
import com.telpo.gx_social_security.adapter.MyAdapter
import com.telpo.gx_social_security.bean.BaseResponse
import com.telpo.gx_social_security.bean.GgzpxxRequest
import com.telpo.gx_social_security.bean.GgzpxxResult
import com.telpo.gxss.databinding.ActivityGgzpxxBinding
import com.telpo.pullrefreshswipelib.LoadingFooter
import com.telpo.pullrefreshswipelib.xRecyclerView
import kotlinx.coroutines.launch
import java.lang.reflect.Type


class GgzpxxActivity : BaseViewModelActivity<ActivityGgzpxxBinding>(),
    xRecyclerView.OnRefreshListener {

    private val viewModel: GgzpxxViewModel by viewModels {
        GgzpxxModelFactory(
            (application as AppContext).httpRepository
        )
    }


    lateinit var adapter: MyAdapter
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        resultLauncher =
                registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                    // 当 ActivityB 返回时，会走到这里
                    if (result.resultCode == RESULT_OK) {
                        // 用户点击了确定，返回了数据
                        val data: Intent? = result.data
                        val resultText = data?.getStringExtra("searchContent")
                        binding.tvInput.text = resultText
                    } else {
                        // 用户取消了，或者返回了 RESULT_CANCELED
                    }
                }
    }

    override fun onStop() {
        super.onStop()
        TLog.i("onStop")
    }

    override fun initView() {
        super.initView()
        //        binding.xrecyclerView.layoutManager = LinearLayoutManager(this)
    }


    override fun initListeners() {
        super.initListeners()

        binding.tvInput.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            // 使用 launcher 启动，而不是 startActivityForResult
            resultLauncher.launch(intent)
        }
        binding.tvSearch.setOnClickListener {
            lifecycleScope.launch {

                showLoading("查询公共招聘信息")
                viewModel.queryGgzpxx(
                    GgzpxxRequest(
                        current = 1,
                        size = 100,
                        aae397 = null,
                        acb217 = binding.tvInput.text.toString(),
                        acb202 = null)).collect {
                    hideLoading()

                    TLog.i("total=${it.total},recode=${it.records?.get(0)?.acb217}")
                }
            }
        }
    }

    override fun initDatum() {
        super.initDatum()
        initViewModel(viewModel)
        addTestData()


    }


    override fun refresh(refreshLayout: SwipeRefreshLayout) {

        Thread(object : Runnable {
            override fun run() {
                try {
                    Thread.sleep(1500)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
                //                rankList.clear()
                //                for (i in 0..19) {
                //                    rankList.add(0, "下拉刷新" + i)
                //                }
                runOnUiThread(object : Runnable {
                    override fun run() {
                        //                        recycleAdapter.notifyDataSetChanged()
                        binding.xrecyclerView.refreshComplete(LoadingFooter.State.Normal, "")
                    }
                })
            }
        }).start()

    }

    override fun loadMore(recyclerView: RecyclerView?) {
        Thread(object : Runnable {
            override fun run() {
                try {
                    Thread.sleep(1500)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
                runOnUiThread(object : Runnable {
                    override fun run() {
                        //                        if (rankList.size() > 40) {
                        //                            xrecyclerView.refreshComplete(LoadingFooter.State.TheEnd, "")
                        //                        } else {
                        //                            for (i in 0..8) {
                        //                                rankList.add("加载更多" + i)
                        //                            }
                        //                            recycleAdapter.notifyDataSetChanged()
                        //                            xrecyclerView.refreshComplete(LoadingFooter.State.Normal, "")
                        //                        }
                        binding.xrecyclerView.refreshComplete(LoadingFooter.State.Normal, "")
                    }
                })
            }
        }).start()
    }


    fun addTestData() {
        var jsonstr = "{\n" +
                "    \"result\":{\n" +
                "        \"total\":1,\n" +
                "        \"current\":1,\n" +
                "        \"hitCount\":false,\n" +
                "        \"pages\":1,\n" +
                "        \"size\":100,\n" +
                "        \"records\":[\n" +
                "            {\n" +
                "                \"ACB217\":\"小骑兵店长0\",\n" +
                "                \"ACB221\":18,\n" +
                "                \"ACB242\":\"\",\n" +
                "                \"ACA112\":\"住宿和餐饮服务人员\",\n" +
                "                \"ACB222\":35,\n" +
                "                \"DWLABEL\":\"五险,工龄,餐补\",\n" +
                "                \"ACB214\":\"7000\",\n" +
                "                \"ACB202\":\"南宁市兴宁区虎邱西路9号虎邱广场小骑兵\",\n" +
                "                \"ACB213\":\"5001\",\n" +
                "                \"AAB004\":\"广西小骑兵餐饮管理有限公司\",\n" +
                "                \"AAE005\":\"18877176881\",\n" +
                "                \"AAE159\":\"227827563@qq.com\",\n" +
                "                \"ROW_ID\":1,\n" +
                "                \"AAE004\":\"陈经理\",\n" +
                "                \"AAE398\":\"20230124\",\n" +
                "                \"AAC011\":\"大专\",\n" +
                "                \"AAE397\":\"20221228\",\n" +
                "                \"AAE031\":\"20230124\",\n" +
                "                \"ACB241\":\"\",\n" +
                "                \"AAE030\":\"20221225\",\n" +
                "                \"ACB240\":2\n" +
                "            },{\n" +
                "                \"ACB217\":\"小骑兵店长1\",\n" +
                "                \"ACB221\":18,\n" +
                "                \"ACB242\":\"\",\n" +
                "                \"ACA112\":\"住宿和餐饮服务人员\",\n" +
                "                \"ACB222\":35,\n" +
                "                \"DWLABEL\":\"五险,工龄,餐补\",\n" +
                "                \"ACB214\":\"7000\",\n" +
                "                \"ACB202\":\"南宁市兴宁区虎邱西路9号虎邱广场小骑兵\",\n" +
                "                \"ACB213\":\"5001\",\n" +
                "                \"AAB004\":\"广西小骑兵餐饮管理有限公司\",\n" +
                "                \"AAE005\":\"18877176881\",\n" +
                "                \"AAE159\":\"227827563@qq.com\",\n" +
                "                \"ROW_ID\":1,\n" +
                "                \"AAE004\":\"陈经理\",\n" +
                "                \"AAE398\":\"20230124\",\n" +
                "                \"AAC011\":\"大专\",\n" +
                "                \"AAE397\":\"20221228\",\n" +
                "                \"AAE031\":\"20230124\",\n" +
                "                \"ACB241\":\"\",\n" +
                "                \"AAE030\":\"20221225\",\n" +
                "                \"ACB240\":2\n" +
                "            },{\n" +
                "                \"ACB217\":\"小骑兵店长2\",\n" +
                "                \"ACB221\":18,\n" +
                "                \"ACB242\":\"\",\n" +
                "                \"ACA112\":\"住宿和餐饮服务人员\",\n" +
                "                \"ACB222\":35,\n" +
                "                \"DWLABEL\":\"五险,工龄,餐补\",\n" +
                "                \"ACB214\":\"7000\",\n" +
                "                \"ACB202\":\"南宁市兴宁区虎邱西路9号虎邱广场小骑兵\",\n" +
                "                \"ACB213\":\"5001\",\n" +
                "                \"AAB004\":\"广西小骑兵餐饮管理有限公司\",\n" +
                "                \"AAE005\":\"18877176881\",\n" +
                "                \"AAE159\":\"227827563@qq.com\",\n" +
                "                \"ROW_ID\":1,\n" +
                "                \"AAE004\":\"陈经理\",\n" +
                "                \"AAE398\":\"20230124\",\n" +
                "                \"AAC011\":\"大专\",\n" +
                "                \"AAE397\":\"20221228\",\n" +
                "                \"AAE031\":\"20230124\",\n" +
                "                \"ACB241\":\"\",\n" +
                "                \"AAE030\":\"20221225\",\n" +
                "                \"ACB240\":2\n" +
                "            },{\n" +
                "                \"ACB217\":\"小骑兵店长3\",\n" +
                "                \"ACB221\":18,\n" +
                "                \"ACB242\":\"\",\n" +
                "                \"ACA112\":\"住宿和餐饮服务人员\",\n" +
                "                \"ACB222\":35,\n" +
                "                \"DWLABEL\":\"五险,工龄,餐补\",\n" +
                "                \"ACB214\":\"7000\",\n" +
                "                \"ACB202\":\"南宁市兴宁区虎邱西路9号虎邱广场小骑兵\",\n" +
                "                \"ACB213\":\"5001\",\n" +
                "                \"AAB004\":\"广西小骑兵餐饮管理有限公司\",\n" +
                "                \"AAE005\":\"18877176881\",\n" +
                "                \"AAE159\":\"227827563@qq.com\",\n" +
                "                \"ROW_ID\":1,\n" +
                "                \"AAE004\":\"陈经理\",\n" +
                "                \"AAE398\":\"20230124\",\n" +
                "                \"AAC011\":\"大专\",\n" +
                "                \"AAE397\":\"20221228\",\n" +
                "                \"AAE031\":\"20230124\",\n" +
                "                \"ACB241\":\"\",\n" +
                "                \"AAE030\":\"20221225\",\n" +
                "                \"ACB240\":2\n" +
                "            },{\n" +
                "                \"ACB217\":\"小骑兵店长4\",\n" +
                "                \"ACB221\":18,\n" +
                "                \"ACB242\":\"\",\n" +
                "                \"ACA112\":\"住宿和餐饮服务人员\",\n" +
                "                \"ACB222\":35,\n" +
                "                \"DWLABEL\":\"五险,工龄,餐补\",\n" +
                "                \"ACB214\":\"7000\",\n" +
                "                \"ACB202\":\"南宁市兴宁区虎邱西路9号虎邱广场小骑兵\",\n" +
                "                \"ACB213\":\"5001\",\n" +
                "                \"AAB004\":\"广西小骑兵餐饮管理有限公司\",\n" +
                "                \"AAE005\":\"18877176881\",\n" +
                "                \"AAE159\":\"227827563@qq.com\",\n" +
                "                \"ROW_ID\":1,\n" +
                "                \"AAE004\":\"陈经理\",\n" +
                "                \"AAE398\":\"20230124\",\n" +
                "                \"AAC011\":\"大专\",\n" +
                "                \"AAE397\":\"20221228\",\n" +
                "                \"AAE031\":\"20230124\",\n" +
                "                \"ACB241\":\"\",\n" +
                "                \"AAE030\":\"20221225\",\n" +
                "                \"ACB240\":2\n" +
                "            },{\n" +
                "                \"ACB217\":\"小骑兵店长5\",\n" +
                "                \"ACB221\":18,\n" +
                "                \"ACB242\":\"\",\n" +
                "                \"ACA112\":\"住宿和餐饮服务人员\",\n" +
                "                \"ACB222\":35,\n" +
                "                \"DWLABEL\":\"五险,工龄,餐补\",\n" +
                "                \"ACB214\":\"7000\",\n" +
                "                \"ACB202\":\"南宁市兴宁区虎邱西路9号虎邱广场小骑兵\",\n" +
                "                \"ACB213\":\"5001\",\n" +
                "                \"AAB004\":\"广西小骑兵餐饮管理有限公司\",\n" +
                "                \"AAE005\":\"18877176881\",\n" +
                "                \"AAE159\":\"227827563@qq.com\",\n" +
                "                \"ROW_ID\":1,\n" +
                "                \"AAE004\":\"陈经理\",\n" +
                "                \"AAE398\":\"20230124\",\n" +
                "                \"AAC011\":\"大专\",\n" +
                "                \"AAE397\":\"20221228\",\n" +
                "                \"AAE031\":\"20230124\",\n" +
                "                \"ACB241\":\"\",\n" +
                "                \"AAE030\":\"20221225\",\n" +
                "                \"ACB240\":2\n" +
                "            },{\n" +
                "                \"ACB217\":\"小骑兵店长6\",\n" +
                "                \"ACB221\":18,\n" +
                "                \"ACB242\":\"\",\n" +
                "                \"ACA112\":\"住宿和餐饮服务人员\",\n" +
                "                \"ACB222\":35,\n" +
                "                \"DWLABEL\":\"五险,工龄,餐补\",\n" +
                "                \"ACB214\":\"7000\",\n" +
                "                \"ACB202\":\"南宁市兴宁区虎邱西路9号虎邱广场小骑兵\",\n" +
                "                \"ACB213\":\"5001\",\n" +
                "                \"AAB004\":\"广西小骑兵餐饮管理有限公司\",\n" +
                "                \"AAE005\":\"18877176881\",\n" +
                "                \"AAE159\":\"227827563@qq.com\",\n" +
                "                \"ROW_ID\":1,\n" +
                "                \"AAE004\":\"陈经理\",\n" +
                "                \"AAE398\":\"20230124\",\n" +
                "                \"AAC011\":\"大专\",\n" +
                "                \"AAE397\":\"20221228\",\n" +
                "                \"AAE031\":\"20230124\",\n" +
                "                \"ACB241\":\"\",\n" +
                "                \"AAE030\":\"20221225\",\n" +
                "                \"ACB240\":2\n" +
                "            },{\n" +
                "                \"ACB217\":\"小骑兵店长7\",\n" +
                "                \"ACB221\":18,\n" +
                "                \"ACB242\":\"\",\n" +
                "                \"ACA112\":\"住宿和餐饮服务人员\",\n" +
                "                \"ACB222\":35,\n" +
                "                \"DWLABEL\":\"五险,工龄,餐补\",\n" +
                "                \"ACB214\":\"7000\",\n" +
                "                \"ACB202\":\"南宁市兴宁区虎邱西路9号虎邱广场小骑兵\",\n" +
                "                \"ACB213\":\"5001\",\n" +
                "                \"AAB004\":\"广西小骑兵餐饮管理有限公司\",\n" +
                "                \"AAE005\":\"18877176881\",\n" +
                "                \"AAE159\":\"227827563@qq.com\",\n" +
                "                \"ROW_ID\":1,\n" +
                "                \"AAE004\":\"陈经理\",\n" +
                "                \"AAE398\":\"20230124\",\n" +
                "                \"AAC011\":\"大专\",\n" +
                "                \"AAE397\":\"20221228\",\n" +
                "                \"AAE031\":\"20230124\",\n" +
                "                \"ACB241\":\"\",\n" +
                "                \"AAE030\":\"20221225\",\n" +
                "                \"ACB240\":2\n" +
                "            },{\n" +
                "                \"ACB217\":\"小骑兵店长8\",\n" +
                "                \"ACB221\":18,\n" +
                "                \"ACB242\":\"\",\n" +
                "                \"ACA112\":\"住宿和餐饮服务人员\",\n" +
                "                \"ACB222\":35,\n" +
                "                \"DWLABEL\":\"五险,工龄,餐补\",\n" +
                "                \"ACB214\":\"7000\",\n" +
                "                \"ACB202\":\"南宁市兴宁区虎邱西路9号虎邱广场小骑兵\",\n" +
                "                \"ACB213\":\"5001\",\n" +
                "                \"AAB004\":\"广西小骑兵餐饮管理有限公司\",\n" +
                "                \"AAE005\":\"18877176881\",\n" +
                "                \"AAE159\":\"227827563@qq.com\",\n" +
                "                \"ROW_ID\":1,\n" +
                "                \"AAE004\":\"陈经理\",\n" +
                "                \"AAE398\":\"20230124\",\n" +
                "                \"AAC011\":\"大专\",\n" +
                "                \"AAE397\":\"20221228\",\n" +
                "                \"AAE031\":\"20230124\",\n" +
                "                \"ACB241\":\"\",\n" +
                "                \"AAE030\":\"20221225\",\n" +
                "                \"ACB240\":2\n" +
                "            },{\n" +
                "                \"ACB217\":\"小骑兵店长9\",\n" +
                "                \"ACB221\":18,\n" +
                "                \"ACB242\":\"\",\n" +
                "                \"ACA112\":\"住宿和餐饮服务人员\",\n" +
                "                \"ACB222\":35,\n" +
                "                \"DWLABEL\":\"五险,工龄,餐补\",\n" +
                "                \"ACB214\":\"7000\",\n" +
                "                \"ACB202\":\"南宁市兴宁区虎邱西路9号虎邱广场小骑兵\",\n" +
                "                \"ACB213\":\"5001\",\n" +
                "                \"AAB004\":\"广西小骑兵餐饮管理有限公司\",\n" +
                "                \"AAE005\":\"18877176881\",\n" +
                "                \"AAE159\":\"227827563@qq.com\",\n" +
                "                \"ROW_ID\":1,\n" +
                "                \"AAE004\":\"陈经理\",\n" +
                "                \"AAE398\":\"20230124\",\n" +
                "                \"AAC011\":\"大专\",\n" +
                "                \"AAE397\":\"20221228\",\n" +
                "                \"AAE031\":\"20230124\",\n" +
                "                \"ACB241\":\"\",\n" +
                "                \"AAE030\":\"20221225\",\n" +
                "                \"ACB240\":2\n" +
                "            }\n" +
                "        ],\n" +
                "        \"searchCount\":true,\n" +
                "        \"orders\":[\n" +
                "        ]\n" +
                "    },\n" +
                "    \"errorCode\":\"\",\n" +
                "    \"message\":\"\",\n" +
                "    \"uuid\":\"2548da49193b4871a48d5b0d9bca90c1\",\n" +
                "    \"statusCode\":200\n" +
                "}\n" +
                "\n" +
                "\n"

        var gson = Gson()
        val type: Type = object : TypeToken<BaseResponse<GgzpxxResult>>() {}.type
        val response: BaseResponse<GgzpxxResult> = gson.fromJson(jsonstr, type)
        TLog.i("total=${response.result.total},recode=${response.result.records.get(0).acb217}")
        TLog.i("total=${response.result.total},recode=${response.result.records.get(1).acb217}")
        TLog.i("total=${response.result.total},recode=${response.result.records.get(2).acb217}")
        TLog.i("records.size=${response.result.records.size}")
        adapter = MyAdapter(response.result.records)
        binding.xrecyclerView.setAdapter(this, adapter, LinearLayoutManager(this));
        binding.xrecyclerView.setOnRefreshListener(this)

        val header: View? = LayoutInflater.from(this)
            .inflate(com.telpo.pullrefreshswipelib.R.layout.header, binding.xrecyclerView, false)
        binding.xrecyclerView.addHeader(header)
        //        val dividerItemDecoration = DividerItemDecoration(
        //            binding.xrecyclerView.context,
        //            LinearLayoutManager.VERTICAL  // 如果是水平列表，用 HORIZONTAL
        //        )
        //
        //        // 可选：设置分割线颜色和粗细（通过自定义 Drawable，见方法 2）
        //        // 默认是系统主题的分割线样式（通常是浅灰色、1px）
        //        // 加载自定义分割线 Drawable
        //        val customDividerDrawable = ContextCompat.getDrawable(this, R.drawable.custom_divider)
        //        if (customDividerDrawable != null) {
        //            dividerItemDecoration.setDrawable(customDividerDrawable)
        //        }

        //        binding.xrecyclerView.addItemDecoration(dividerItemDecoration)
    }

}
