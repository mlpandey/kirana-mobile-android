package com.zhola.customer.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.zhola.R
import com.zhola.common.activity.BaseActivity
import com.zhola.common.adapter.BaseAdapter
import com.zhola.common.adapter.BaseViewHolder
import com.zhola.databinding.AdapterCartBinding

class SavedAdapter(val baseActivity: BaseActivity) : BaseAdapter() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val binding = AdapterCartBinding.inflate(LayoutInflater.from(baseActivity), parent, false)
        return BaseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)

        val binding = holder.binding as AdapterCartBinding
        binding.dotsIV.setImageResource(R.drawable.ic_red_cross)
    }

    override fun getItemCount(): Int {
        return 2
    }
}