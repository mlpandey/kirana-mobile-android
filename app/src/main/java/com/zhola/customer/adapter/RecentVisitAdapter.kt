package com.zhola.customer.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.zhola.common.activity.BaseActivity
import com.zhola.common.adapter.BaseAdapter
import com.zhola.common.adapter.BaseViewHolder
import com.zhola.databinding.AdapterRecentVisitBinding

class RecentVisitAdapter(val baseActivity: BaseActivity) : BaseAdapter() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val binding =
            AdapterRecentVisitBinding.inflate(LayoutInflater.from(baseActivity), parent, false)
        return BaseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)

        val binding = holder.binding as AdapterRecentVisitBinding
        binding.root.setOnClickListener {
            onItemClick()
        }
    }

    override fun getItemCount(): Int {
        return 3
    }
}