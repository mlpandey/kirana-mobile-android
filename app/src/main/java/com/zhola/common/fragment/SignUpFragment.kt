package com.zhola.common.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.zhola.business.fragment.BasicInfoFragment
import com.zhola.R
import com.zhola.common.activity.LoginSignUpActivity
import com.zhola.common.extensions.replaceFragWithArgs
import com.zhola.common.extensions.replaceFragment
import com.zhola.common.model.SignUpData
import com.zhola.common.utils.ClickHandler
import com.zhola.common.utils.Const
import com.zhola.common.utils.OnUpgradeDialogClick
import com.zhola.common.utils.SetData
import com.zhola.customer.fragment.CompleteProfileFragment
import com.zhola.databinding.FragmentSignUpBinding


class SignUpFragment : BaseFragment(), ClickHandler, OnUpgradeDialogClick, SetData.SendProfileData {

    private lateinit var binding: FragmentSignUpBinding
    private var type: Int = 0
    private var isChecked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            type = it.getInt("role_id")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (baseActivity as LoginSignUpActivity).setToolbar("", false)
        initUI()
    }

    private fun initUI() {
        SetData.data.setListener(this)
        binding.clickHandler = this
    }

    override fun onResume() {
        super.onResume()

        binding.termsConditionCB.isChecked = isChecked
    }

    override fun handleClick(view: View) {
        when (view.id) {
            R.id.signUpBT -> {
                if (isValide()) {
                    // send signup data to customer,vendor and driver
                    val bundle = Bundle()
                    val data = SignUpData()
                    data.name = binding.nameET.text.toString()
                    data.email = binding.emailET.text.toString()
                    data.phoneNumber = binding.phoneNumberET.text.toString()
                    data.password = binding.passwordET.text.toString()
                    bundle.putParcelable("signUpData", data)
                    if (type == Const.TYPE_CUSTOMER) {
                        baseActivity!!.replaceFragWithArgs(
                            CompleteProfileFragment(),
                            R.id.login_frame, bundle
                        )
                    } else if (type == Const.TYPE_VENDOR) {
                        baseActivity!!.replaceFragWithArgs(
                            BasicInfoFragment(),
                            R.id.login_frame, bundle
                        )
                    } else {
                        baseActivity!!.replaceFragWithArgs(
                            com.zhola.driver.fragment.CompleteProfileFragment(),
                            R.id.login_frame, bundle
                        )
                    }
                }
            }

            R.id.backIV -> {
                baseActivity!!.onBackPressed()
            }

            R.id.termsConditionTV -> {
                baseActivity!!.replaceFragment(TermsConditionFragment(), R.id.login_frame)
            }
        }
    }

    //SignUp Validation
    private fun isValide(): Boolean {
        if (binding.nameET.text!!.toString().trim().isEmpty()) {
            baseActivity!!.upgradeAlertBox(getString(R.string.please_enter_the_name), this)
        } else if (binding.emailET.text!!.isEmpty()) {
            baseActivity!!.upgradeAlertBox(getString(R.string.please_enter_the_email_address), this)
        } else if (!baseActivity!!.isValidMail(binding.emailET.text.toString())) {
            baseActivity!!.upgradeAlertBox(
                getString(R.string.please_enter_the_valid_email_address),
                this
            )
        } else if (binding.phoneNumberET.text!!.isEmpty()) {
            baseActivity!!.upgradeAlertBox(getString(R.string.please_enter_the_phone_number), this)
        } else if (binding.passwordET.text!!.isEmpty()) {
            baseActivity!!.upgradeAlertBox(getString(R.string.please_enter_the_password), this)
        } else if (binding.passwordET.text.toString().length < 6) {
            baseActivity!!.upgradeAlertBox(
                getString(R.string.password_must_be_of_six_character),
                this
            )
        } else if (binding.confirmPasswordET.text!!.isEmpty()) {
            baseActivity!!.upgradeAlertBox(
                getString(R.string.please_enter_the_confirm_password),
                this
            )
        } else if (binding.passwordET.text!!.toString() != binding.confirmPasswordET.text.toString()) {
            baseActivity!!.upgradeAlertBox(
                getString(R.string.confirm_password_is_not_matching_with_password),
                this
            )
        } else if (!binding.termsConditionCB.isChecked) {
            baseActivity!!.upgradeAlertBox(
                getString(R.string.please_agree_with_our_terms_and_conditions),
                this
            )
        } else {
            return true
        }
        return false
    }

    override fun onUpgradeDialogClick(action: String) {

    }

    override fun getProfileData(pos: Int) {
        isChecked = pos != 0
    }
}