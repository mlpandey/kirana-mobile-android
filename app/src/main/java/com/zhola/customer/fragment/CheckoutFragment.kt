package com.zhola.customer.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.zhola.R
import com.zhola.common.extensions.replaceFragment
import com.zhola.common.fragment.BaseFragment
import com.zhola.common.utils.ClickHandler
import com.zhola.customer.activity.CustomerMainActivity
import com.zhola.customer.adapter.CheckoutCartAdapter
import com.zhola.databinding.FragmentCheckoutBinding
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter

class CheckoutFragment : BaseFragment(), ClickHandler {

    private lateinit var binding: FragmentCheckoutBinding
    private val adapter by lazy { CheckoutCartAdapter(baseActivity!!) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_checkout, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (baseActivity as CustomerMainActivity).setToolbar("My Cart", true, false, false)
        initUI()
    }

    private fun initUI() {
        binding.clickHandler = this
        binding.cartRV.adapter = AlphaInAnimationAdapter(adapter)
    }

    override fun handleClick(view: View) {
        when (view.id) {
            R.id.checkoutBT -> {
                baseActivity!!.replaceFragment(MakePaymentFragment(), R.id.container)
            }
        }
    }
}