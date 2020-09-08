package com.zhola.driver.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.zhola.R
import com.zhola.common.extensions.replaceFragment
import com.zhola.common.fragment.BaseFragment
import com.zhola.common.adapter.BaseAdapter
import com.zhola.databinding.FragmentOrdersBinding
import com.zhola.driver.activity.DriverMainActivity
import com.zhola.driver.adapter.OrdersAdapter
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter

class OrdersFragment : BaseFragment(), BaseAdapter.OnItemClickListener {

    private lateinit var binding: FragmentOrdersBinding
    private val adapter by lazy { OrdersAdapter(baseActivity!!) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_orders, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (baseActivity as DriverMainActivity).setToolbar("Orders", true, true,false)
        initUI()
    }

    private fun initUI() {
        adapter.setOnItemClickListener(this)
        binding.ordersRV.adapter = AlphaInAnimationAdapter(adapter)
    }

    override fun onItemClick(vararg itemData: Any) {
        baseActivity!!.replaceFragment(OrderDetailFragment(), R.id.driver_container)
    }
}