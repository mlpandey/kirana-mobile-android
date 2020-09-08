package com.zhola.common.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.zhola.R
import com.zhola.common.activity.LoginSignUpActivity
import com.zhola.common.extensions.replaceFragment
import com.zhola.common.model.SignUpData
import com.zhola.common.utils.ClickHandler
import com.zhola.common.utils.OnUpgradeDialogClick
import com.zhola.common.viewmodel.LoginSignUpViewModel
import com.zhola.databinding.FragmentLoginBinding
import okhttp3.Headers
import kotlin.math.sign

class LoginFragment : BaseFragment(), ClickHandler, OnUpgradeDialogClick {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var mViewModel: LoginSignUpViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (baseActivity as LoginSignUpActivity).setToolbar("", false)
        initUI()
    }

    private fun initUI() {
        // initialize view model
        mViewModel = ViewModelProvider(this).get(LoginSignUpViewModel::class.java)
        mViewModel.setData(baseActivity!!)
        binding.clickHandler = this
    }

    override fun handleClick(view: View) {
        when (view.id) {
            R.id.forgotPasswordTV -> {
                baseActivity!!.replaceFragment(ForgotPasswordFragment(), R.id.login_frame)
            }

            R.id.loginBT -> {
                if (isValidate()) {
                    // hit the login api
                    val signUpData = SignUpData()
                    signUpData.email = binding.emailET.text!!.trim().toString()
                    signUpData.password = binding.passwordET.text!!.trim().toString()
                    mViewModel.hitLoginApi(signUpData,true)
                }
            }

            R.id.backIV -> {
                baseActivity!!.onBackPressed()
            }
        }
    }

    // check the validation
    private fun isValidate(): Boolean {
        if (binding.emailET.text!!.isEmpty()) {
            baseActivity!!.upgradeAlertBox(getString(R.string.please_enter_the_email_address), this)
        } else if (!baseActivity!!.isValidMail(binding.emailET.text.toString())) {
            baseActivity!!.upgradeAlertBox(
                getString(R.string.please_enter_the_valid_email_address),
                this
            )
        } else if (binding.passwordET.text!!.isEmpty()) {
            baseActivity!!.upgradeAlertBox(getString(R.string.please_enter_the_password), this)
        } else if (binding.passwordET.text.toString().length < 6) {
            baseActivity!!.upgradeAlertBox(
                getString(R.string.password_must_be_of_six_character),
                this
            )
        } else {
            return true
        }
        return false
    }

    override fun onUpgradeDialogClick(action: String) {

    }
}