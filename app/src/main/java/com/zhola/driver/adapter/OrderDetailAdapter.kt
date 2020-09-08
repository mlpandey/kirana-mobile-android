package com.zhola.driver.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.zhola.common.activity.BaseActivity
import com.zhola.common.adapter.BaseAdapter
import com.zhola.common.adapter.BaseViewHolder
import com.zhola.databinding.AdapterOrderDetailBinding

class OrderDetailAdapter(val baseActivity: BaseActivity) : BaseAdapter() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val binding =
            AdapterOrderDetailBinding.inflate(LayoutInflater.from(baseActivity), parent, false)
        return BaseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
    }

    override fun getItemCount(): Int {
        return 2
    }
}