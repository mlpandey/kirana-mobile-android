package com.zhola.customer.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.zhola.R
import com.zhola.common.activity.BaseActivity
import com.zhola.common.extensions.replaceFragWithoutStack
import com.zhola.common.extensions.replaceFragWithoutTransition
import com.zhola.common.extensions.replaceFragment
import com.zhola.common.fragment.AuthenticationSelectionFragment
import com.zhola.common.utils.ClickHandler
import com.zhola.customer.fragment.MyOrdersFragment
import com.zhola.customer.fragment.MyProfileFragment
import com.zhola.customer.fragment.SavedFragment
import com.zhola.customer.fragment.ShopFragment
import com.zhola.databinding.ActivityCustomerMainBinding

class CustomerMainActivity : BaseActivity(), BottomNavigationView.OnNavigationItemSelectedListener,
    ClickHandler {

    private lateinit var binding: ActivityCustomerMainBinding
    private var selectedPosition = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_customer_main)

        initUI()
    }

    private fun initUI() {
        binding.clickHandler = this
        setSupportActionBar(binding.toolbarTB)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        binding.navigation.setOnNavigationItemSelectedListener(this)
        binding.navigation.selectedItemId = R.id.shopMI
    }

    fun setToolbar(
        title: String,
        isVisible: Boolean = false,
        isNavigationVisible: Boolean = true,
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

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.shopMI -> {
                if (selectedPosition != 0) {
                    selectedPosition = 0
                    supportFragmentManager.popBackStack(
                        null,
                        FragmentManager.POP_BACK_STACK_INCLUSIVE
                    )
                    replaceFragWithoutTransition(ShopFragment(), R.id.container)
                }
            }

            R.id.saveMI -> {
                if (selectedPosition != 1) {
                    selectedPosition = 1
                    supportFragmentManager.popBackStack(
                        null,
                        FragmentManager.POP_BACK_STACK_INCLUSIVE
                    )
                    replaceFragWithoutTransition(SavedFragment(), R.id.container)
                }
            }

            R.id.ordersMI -> {
                if (selectedPosition != 2) {
                    selectedPosition = 2
                    supportFragmentManager.popBackStack(
                        null,
                        FragmentManager.POP_BACK_STACK_INCLUSIVE
                    )
                    replaceFragWithoutTransition(MyOrdersFragment(), R.id.container)
                }
            }
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val fragment = supportFragmentManager.findFragmentById(R.id.container)
        if (fragment !is ShopFragment && fragment !is SavedFragment && fragment !is MyOrdersFragment) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
            supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back)
        } else {
            supportActionBar!!.setDisplayHomeAsUpEnabled(false)
            supportActionBar!!.setDisplayShowHomeEnabled(false)
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
        val fragment = supportFragmentManager.findFragmentById(R.id.container)

        if (fragment is ShopFragment || fragment is SavedFragment || fragment is MyOrdersFragment) {
            backAction()
        } else if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            binding.navigation.selectedItemId = R.id.shopMI
        }
    }

    fun gotoShopFragment() {
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        replaceFragWithoutTransition(ShopFragment(), R.id.container)
    }

    override fun handleClick(view: View) {
        when (view.id) {
            R.id.profileCIV -> {
                replaceFragment(MyProfileFragment(), R.id.container)
            }
        }
    }
}