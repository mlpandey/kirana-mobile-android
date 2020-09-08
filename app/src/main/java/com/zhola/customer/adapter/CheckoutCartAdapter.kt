package com.zhola.customer.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zhola.R
import com.zhola.common.activity.BaseActivity
import com.zhola.common.adapter.BaseAdapter
import com.zhola.common.adapter.BaseViewHolder
import com.zhola.databinding.AdapterCartBinding

class CheckoutCartAdapter(val baseActivity: BaseActivity) : BaseAdapter() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val binding = AdapterCartBinding.inflate(LayoutInflater.from(baseActivity), parent, false)
        return BaseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)

        val binding = holder.binding as AdapterCartBinding
        if (position == 2) {
            binding.outOfStockTV.visibility = View.VISIBLE
            binding.minusCV.visibility = View.INVISIBLE
            binding.addCV.visibility = View.INVISIBLE
            binding.valueTV.visibility = View.INVISIBLE
            binding.dotsIV.setImageResource(R.drawable.ic_red_cross)
        } else {
            binding.outOfStockTV.visibility = View.GONE
            binding.minusCV.visibility = View.VISIBLE
            binding.addCV.visibility = View.VISIBLE
            binding.valueTV.visibility = View.VISIBLE
            binding.dotsIV.setImageResource(R.drawable.ic_dots)
        }
    }

    override fun getItemCount(): Int {
        return 3
    }
}