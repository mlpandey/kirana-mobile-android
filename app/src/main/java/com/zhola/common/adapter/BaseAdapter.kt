package com.zhola.common.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

open class BaseAdapter : RecyclerView.Adapter<BaseViewHolder>() {

    private var onItemClickListener: OnItemClickListener? = null
    private var onPageEndListener: OnPageEndListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return BaseViewHolder(null)
    }

    override fun getItemCount(): Int {
        return 0
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {

    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    fun setOnPageEndListener(onPageEndListener: OnPageEndListener) {
        this.onPageEndListener = onPageEndListener
    }


    interface OnItemClickListener {
        fun onItemClick(vararg itemData: Any)
    }

    interface OnPageEndListener {
        fun onPageEnd()
    }

    fun onItemClick(vararg itemData: Any) {
        if (onItemClickListener != null) {
            onItemClickListener!!.onItemClick(*itemData)
        }
    }

    fun onPageEnd() {
        if (onPageEndListener != null) {
            onPageEndListener!!.onPageEnd()
        }
    }
}