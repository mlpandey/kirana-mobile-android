package com.zhola.business.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.zhola.business.activity.BusinessMainActivity
import com.zhola.R
import com.zhola.common.fragment.BaseFragment
import com.zhola.databinding.FragmentSetupInventoryBinding

class SetupInventoryFragment : BaseFragment() {

    private lateinit var binding: FragmentSetupInventoryBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_setup_inventory, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (baseActivity as BusinessMainActivity).setToolbar("Setup Inventory", true,false,true)
    }
}