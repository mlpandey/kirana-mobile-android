package com.zhola.customer.fragment

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.MenuItemCompat
import androidx.databinding.DataBindingUtil
import com.zhola.R
import com.zhola.common.extensions.replaceFragment
import com.zhola.common.fragment.BaseFragment
import com.zhola.customer.activity.CustomerMainActivity
import com.zhola.customer.adapter.MyOrdersAdapter
import com.zhola.databinding.FragmentMyOrdersBinding
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter


class MyOrdersFragment : BaseFragment() {

    private lateinit var binding: FragmentMyOrdersBinding
    private val adapter by lazy { MyOrdersAdapter(baseActivity!!) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_orders, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (baseActivity as CustomerMainActivity).setToolbar("My Orders", true, true, true)
        setHasOptionsMenu(true)
        initUI()
    }

    private fun initUI() {
        binding.ordersRV.adapter = AlphaInAnimationAdapter(adapter)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.shop_menu, menu)
        val item = menu.findItem(R.id.cartMI)
        MenuItemCompat.setActionView(item, R.layout.cart_menu_layout)
        val actionView = MenuItemCompat.getActionView(item)
        val cartIV = actionView.findViewById<AppCompatImageView>(R.id.cartIV)
        cartIV.setOnClickListener {
            baseActivity!!.replaceFragment(SendRequestFragment(), R.id.container)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return false
    }

}