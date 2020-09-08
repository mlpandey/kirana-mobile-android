package com.zhola.driver.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.zhola.R
import com.zhola.common.extensions.replaceFragment
import com.zhola.common.fragment.BaseFragment
import com.zhola.common.utils.ClickHandler
import com.zhola.databinding.FragmentOrderDetailBinding
import com.zhola.driver.activity.DriverMainActivity
import com.zhola.driver.adapter.OrderDetailAdapter
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter

class OrderDetailFragment : BaseFragment(), ClickHandler {

    private lateinit var binding: FragmentOrderDetailBinding
    private val adapter by lazy { OrderDetailAdapter(baseActivity!!) }
    private var isPickup = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_order_detail, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        isPickup = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (baseActivity as DriverMainActivity).setToolbar("Order Details", true, false)
        initUI()
    }

    private fun initUI() {
        binding.itemsRV.adapter = AlphaInAnimationAdapter(adapter)
        binding.clickHandler = this
    }

    override fun handleClick(view: View) {
        when (view.id) {
            R.id.orderBT -> {
                if (isPickup) {
                    baseActivity!!.replaceFragment(CompleteOrderFragment(), R.id.driver_container)
                } else {
                    isPickup = true
                    binding.orderBT.text = "Deliver Order"
                    binding.pickUpIV.setImageResource(R.drawable.ic_circle_green)
                    binding.tickIV.visibility = View.VISIBLE
                }
            }
        }
    }
}