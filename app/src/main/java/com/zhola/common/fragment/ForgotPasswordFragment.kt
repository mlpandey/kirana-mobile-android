package com.zhola.common.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.zhola.R
import com.zhola.common.activity.LoginSignUpActivity
import com.zhola.common.utils.ClickHandler
import com.zhola.databinding.FragmentForgotBinding

class ForgotPasswordFragment : BaseFragment(), ClickHandler {

    private lateinit var binding: FragmentForgotBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_forgot, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (baseActivity as LoginSignUpActivity).setToolbar("Forgot Password", true)
        initUI()
    }

    private fun initUI() {
        binding.clickHandler = this
    }

    override fun handleClick(view: View) {
        when (view.id) {

            R.id.forgotPasswordTV -> {
                baseActivity!!.onBackPressed()
            }
        }
    }
}