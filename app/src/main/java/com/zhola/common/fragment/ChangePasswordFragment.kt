package com.zhola.common.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.zhola.R
import com.zhola.customer.activity.CustomerMainActivity
import com.zhola.databinding.FragmentChangePasswordBinding
import com.zhola.driver.activity.DriverMainActivity

class ChangePasswordFragment : BaseFragment() {

    private lateinit var binding: FragmentChangePasswordBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_change_password, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (baseActivity is CustomerMainActivity)
            (baseActivity as CustomerMainActivity).setToolbar("Change Password", true, false, false)
        else
            (baseActivity as DriverMainActivity).setToolbar("Change Password", true, false, true)
    }
}