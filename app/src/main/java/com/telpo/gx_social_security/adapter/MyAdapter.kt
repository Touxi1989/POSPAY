package com.telpo.gx_social_security.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.telpo.AppContext
import com.telpo.base.util.TLog
import com.telpo.gx_social_security.bean.RecordsDTO
import com.telpo.gxss.R

class MyAdapter(private var dataList: MutableList<RecordsDTO>?) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    // 1. 定义 ViewHolder
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvJob: TextView = itemView.findViewById(R.id.tv_job)
        val tvDaiYu: TextView = itemView.findViewById(R.id.tv_daiyu)
        val tvCompany: TextView = itemView.findViewById(R.id.tv_company)
        val tvZPDate: TextView = itemView.findViewById(R.id.tv_zpdate)
        val tvJobType: TextView = itemView.findViewById(R.id.tv_jobType)
        val tvAddr: TextView = itemView.findViewById(R.id.tv_addr)
        val tvWorKDate: TextView = itemView.findViewById(R.id.tv_work_date)
        val tvXueli: TextView = itemView.findViewById(R.id.tv_xueli)
        val tvAge: TextView = itemView.findViewById(R.id.tv_age)
        val tvPeople: TextView = itemView.findViewById(R.id.tv_people)
        val tvFuli: TextView = itemView.findViewById(R.id.tv_fuli)
        val tvContact: TextView = itemView.findViewById(R.id.tv_Contact)
        val tvContactPhone: TextView = itemView.findViewById(R.id.tv_Contact_Phone)
        val tvEmail: TextView = itemView.findViewById(R.id.tv_email)
        val lyDetail: LinearLayout = itemView.findViewById(R.id.ly_detail)
    }

    fun setData(data: MutableList<RecordsDTO>) {
        dataList = data;
        TLog.i("${dataList?.size}")

    }

    // 2. 创建 ViewHolder 并返回（加载 item 布局）
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_view_ggzpxx, parent, false)
        return MyViewHolder(view)
    }

    // 3. 返回数据条数
    override fun getItemCount(): Int = dataList?.size?:0


    // 4. 绑定数据到 ViewHolder
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = dataList?.get(position)
        holder.tvJob.text = "岗位: " + item?.acb217
        holder.tvDaiYu.text = "薪资: " + item?.acb213 + "-" + item?.acb214 + "元/月"
        holder.tvCompany.text = "单位: " + item?.aab004
        holder.tvZPDate.text = "招聘日期: " + item?.aae030 + "-" + item?.aae031
        holder.tvJobType.text = "岗位类型: " + item?.aca112
        holder.tvAddr.text = "工作地点: " + item?.acb202
        holder.tvWorKDate.text = "工作日期: " + item?.aae397 + "-" + item?.aae398
        holder.tvXueli.text = "学历: " + item?.aac011
        holder.tvAge.text = "年龄: " + item?.acb222 + "-" + item?.acb221 + "岁"
        holder.tvPeople.text =
                "招聘人数: " + item?.acb240 + ",男 " + item?.acb241 + ",女 " + item?.acb242
        holder.tvFuli.text = "福利: " + item?.dwlabel
        holder.tvContact.text = "联系人: " + item?.aae004
        holder.tvContactPhone.text = "联系电话: " + item?.aae005
        holder.tvEmail.text = "电子邮件: " + item?.aae159
        holder.itemView.setOnClickListener {
            if (holder.lyDetail.isVisible) {
                holder.lyDetail.visibility = View.GONE
                holder.itemView.setBackgroundColor(AppContext.getContext().resources.getColor(R.color.white))
            } else {
                holder.lyDetail.visibility = View.VISIBLE
                holder.itemView.setBackgroundColor(AppContext.getContext().resources.getColor(R.color.white_E1E1E1))
            }
        }

    }
}