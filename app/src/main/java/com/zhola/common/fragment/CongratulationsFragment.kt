package com.zhola.common.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.zhola.R
import com.zhola.common.activity.LoginSignUpActivity
import com.zhola.common.utils.ClickHandler
import com.zhola.common.utils.Const
import com.zhola.databinding.FragmentCongratulationsBinding
import com.zhola.driver.activity.DriverMainActivity

class CongratulationsFragment : BaseFragment(), ClickHandler {

    private lateinit var binding: FragmentCongratulationsBinding
    private var type: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            type = it.getInt("roleId")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_congratulations, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (baseActivity is LoginSignUpActivity) {
            (baseActivity as LoginSignUpActivity).setToolbar("", false)
        } else if (baseActivity is DriverMainActivity) {
            (baseActivity as DriverMainActivity).setToolbar("", false, false)
        }
        initUI()
    }

    private fun initUI() {
        binding.clickHandler = this

        // set description on the basis of role
        if (type == Const.TYPE_VENDOR) {
            binding.descTV.text =
                "Your account has been successfully updated \nand setup your store"
            binding.doneBT.text = "Setup Store"
        } else if (type == Const.TYPE_DRIVER_ORDER) {
            binding.titleTV.text = "Order Deliver"
            binding.descTV.text = "Your order has been\nSuccessfully Delivered"
        }
    }

    override fun handleClick(view: View) {
        when (view.id) {
            R.id.doneBT -> {
                when (type) {
                    Const.TYPE_CUSTOMER -> {
                        baseActivity!!.gotoCustomerMainActivity()
                    }
                    Const.TYPE_VENDOR -> {
                        baseActivity!!.gotoBusinessMainActivity()
                    }
                    Const.TYPE_DRIVER_ORDER -> {
                        (baseActivity as DriverMainActivity).gotoOrdersFragment()
                    }
                    else -> {
                        baseActivity!!.gotoDriverMainActivity()
                    }
                }
            }
        }
    }
}