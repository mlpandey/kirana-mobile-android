package com.zhola.common.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.zhola.R
import com.zhola.common.activity.LoginSignUpActivity
import com.zhola.common.extensions.replaceFragWithArgs
import com.zhola.common.extensions.replaceFragWithArgsWithoutStack
import com.zhola.common.extensions.replaceFragWithoutStack
import com.zhola.common.extensions.replaceFragment
import com.zhola.common.utils.ClickHandler
import com.zhola.common.utils.Const
import com.zhola.databinding.FragmentAuthenticationSelectionBinding

class AuthenticationSelectionFragment : BaseFragment(), ClickHandler {

    private lateinit var binding: FragmentAuthenticationSelectionBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_authentication_selection,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (baseActivity as LoginSignUpActivity).setToolbar("", false)
        initUI()
    }

    private fun initUI() {
        binding.clickHandler = this
    }

    override fun handleClick(view: View) {
        when (view.id) {
            R.id.loginBT -> {
                baseActivity!!.replaceFragment(
                    LoginFragment(),
                    R.id.login_frame
                )
            }

            R.id.createAccountBT -> {
                baseActivity!!.replaceFragment(
                    SelectionFragment(),
                    R.id.login_frame
                )
            }
        }
    }
}