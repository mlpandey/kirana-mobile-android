package com.zhola.common.adapter

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

open class BaseViewHolder(var binding: ViewDataBinding?) :
    RecyclerView.ViewHolder(binding?.root!!)