package com.zhola.driver.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import com.zhola.R
import com.zhola.common.activity.BaseActivity
import com.zhola.common.extensions.replaceFragWithoutStack
import com.zhola.common.extensions.replaceFragWithoutTransition
import com.zhola.common.extensions.replaceFragment
import com.zhola.common.fragment.CongratulationsFragment
import com.zhola.common.utils.ClickHandler
import com.zhola.customer.fragment.MyProfileFragment
import com.zhola.databinding.ActivityDriverMainBinding
import com.zhola.driver.fragment.DriverMyProfileFragment
import com.zhola.driver.fragment.OrdersFragment

class DriverMainActivity : BaseActivity(), ClickHandler {

    private lateinit var binding: ActivityDriverMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_driver_main)

        initUI()
    }

    private fun initUI() {
        setSupportActionBar(binding.toolbarTB)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        binding.clickHandler = this
        replaceFragWithoutTransition(OrdersFragment(), R.id.driver_container)
    }

    fun gotoOrdersFragment() {
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        replaceFragWithoutTransition(OrdersFragment(), R.id.driver_container)
    }

    fun setToolbar(
        title: String,
        isVisible: Boolean,
        isProfileVisible: Boolean = false,
        isBack: Boolean = true
    ) {
        binding.titleTV.text = title

        if (isVisible) {
            binding.toolbarTB.visibility = View.VISIBLE
        } else {
            binding.toolbarTB.visibility = View.GONE
        }

        if (isProfileVisible) {
            binding.profileCIV.visibility = View.VISIBLE
        } else {
            binding.profileCIV.visibility = View.GONE
        }

        if (!isBack) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(false)
            supportActionBar!!.setDisplayShowHomeEnabled(false)
        } else {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
            supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val fragment = supportFragmentManager.findFragmentById(R.id.driver_container)
        if (fragment is OrdersFragment || fragment is CongratulationsFragment) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(false)
            supportActionBar!!.setDisplayShowHomeEnabled(false)
        } else {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
            supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.driver_container)
        if (fragment is OrdersFragment) {
            backAction()
        } else if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            gotoOrdersFragment()
        }
    }

    override fun handleClick(view: View) {
        when (view.id) {
            R.id.profileCIV -> {
                replaceFragment(DriverMyProfileFragment(), R.id.driver_container)
            }
        }
    }
}