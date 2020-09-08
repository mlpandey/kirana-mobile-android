package com.zhola.business.activity

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.zhola.business.fragment.SetupInventoryFragment
import com.zhola.R
import com.zhola.common.activity.BaseActivity
import com.zhola.common.extensions.replaceFragWithoutStack
import com.zhola.common.extensions.replaceFragment
import com.zhola.common.utils.ClickHandler
import com.zhola.customer.fragment.MyProfileFragment
import com.zhola.databinding.ActivityBusinessMainBinding

class BusinessMainActivity : BaseActivity(), ClickHandler {

    private lateinit var binding: ActivityBusinessMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_business_main)

        initUI()
    }

    private fun initUI() {
        binding.clickHandler = this
        replaceFragWithoutStack(SetupInventoryFragment(), R.id.business_container)
    }

    fun setToolbar(
        title: String,
        isVisible: Boolean = false,
        isNavigationVisible: Boolean = false,
        isProfileVisible: Boolean = false
    ) {
        binding.titleTV.text = title
        if (isVisible) {
            binding.toolbarTB.visibility = View.VISIBLE
        } else {
            binding.toolbarTB.visibility = View.GONE
        }

        if (isNavigationVisible) {
            binding.navigation.visibility = View.VISIBLE
        } else {
            binding.navigation.visibility = View.GONE
        }

        if (isProfileVisible) {
            binding.profileCIV.visibility = View.VISIBLE
        } else {
            binding.profileCIV.visibility = View.GONE
        }
    }

    override fun handleClick(view: View) {
        when (view.id) {
            R.id.profileCIV -> {
                replaceFragment(MyProfileFragment(), R.id.business_container)
            }
        }
    }
}