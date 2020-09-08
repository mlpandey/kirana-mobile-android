package com.zhola.customer.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.zhola.R
import com.zhola.common.activity.BaseActivity
import com.zhola.common.adapter.BaseAdapter
import com.zhola.common.adapter.BaseViewHolder
import com.zhola.common.extensions.replaceFragment
import com.zhola.customer.fragment.AddToCartFragment
import com.zhola.databinding.AdapterItemBinding

class ItemAdapter(val baseActivity: BaseActivity) : BaseAdapter() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val binding = AdapterItemBinding.inflate(LayoutInflater.from(baseActivity), parent, false)
        return BaseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)

        val binding = holder.binding as AdapterItemBinding
        binding.root.setOnClickListener {
            baseActivity.replaceFragment(AddToCartFragment(), R.id.container)
        }
    }

    override fun getItemCount(): Int {
        return 5
    }
}