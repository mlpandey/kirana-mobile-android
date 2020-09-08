package com.zhola.common.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import com.zhola.R
import com.zhola.common.extensions.replaceFragWithoutStack
import com.zhola.common.fragment.AuthenticationSelectionFragment
import com.zhola.common.fragment.ForgotPasswordFragment
import com.zhola.common.fragment.SelectionFragment
import com.zhola.customer.fragment.CompleteProfileFragment
import com.zhola.customer.fragment.MyOrdersFragment
import com.zhola.customer.fragment.SavedFragment
import com.zhola.customer.fragment.ShopFragment
import com.zhola.databinding.ActivityLoginSignupBinding

class LoginSignUpActivity : BaseActivity() {

    private lateinit var binding: ActivityLoginSignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login_signup)

        setSupportActionBar(binding.toolBarTB)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        replaceFragWithoutStack(AuthenticationSelectionFragment(), R.id.login_frame)
    }

    // set toolbar visiblity and its title
    fun setToolbar(title: String, isVisible: Boolean = false) {
        binding.titleTV.text = title
        if (isVisible) {
            binding.toolBarTB.visibility = View.VISIBLE
        } else {
            binding.toolBarTB.visibility = View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back)
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

    // handle backpress
    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.login_frame)

        if (fragment is AuthenticationSelectionFragment) {
            backAction()
        }  else if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            replaceFragWithoutStack(AuthenticationSelectionFragment(), R.id.login_frame)
        }
    }
}