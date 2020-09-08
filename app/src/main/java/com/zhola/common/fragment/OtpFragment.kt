package com.zhola.common.fragment

import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.zhola.R
import com.zhola.common.activity.LoginSignUpActivity
import com.zhola.common.extensions.replaceFragWithArgs
import com.zhola.common.model.SignUpData
import com.zhola.common.utils.ClickHandler
import com.zhola.common.utils.Const
import com.zhola.common.utils.OnUpgradeDialogClick
import com.zhola.common.viewmodel.LoginSignUpViewModel
import com.zhola.databinding.FragmentOtpBinding
import okhttp3.Headers


class OtpFragment : BaseFragment(), ClickHandler, OnUpgradeDialogClick {

    private lateinit var binding: FragmentOtpBinding
    private var type: Int = 0
    private var counter = 0
    private var otpValue: String? = ""
    private var signUpData: SignUpData? = null
    private lateinit var mViewModel: LoginSignUpViewModel
    private var countDownTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            type = it.getInt("roleId")
            signUpData = it.getParcelable("signUpData")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_otp, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (baseActivity as LoginSignUpActivity).setToolbar("", false)
        initUI()
    }

    private fun initUI() {
        startTimer()
        mViewModel = ViewModelProvider(this).get(LoginSignUpViewModel::class.java)
        mViewModel.setData(baseActivity!!)

        mViewModel.otpSent.observe(viewLifecycleOwner, Observer {
            if (it) {
                startTimer()
            }
        })

        binding.mobileTV.text =
            "Please enter the 4-digit code sent to you at \n" + signUpData!!.phoneNumber

        // set the button color
        binding.otpPV.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                otpValue = s.toString()
                if (count == 1) {
                    counter++
                } else {
                    counter--
                }

                if (counter == 4) {
                    binding.verifyBT.background =
                        ContextCompat.getDrawable(baseActivity!!, R.drawable.button_bg)
                    binding.verifyBT.isEnabled = true
                } else {
                    binding.verifyBT.background =
                        ContextCompat.getDrawable(baseActivity!!, R.drawable.gray_button_bg)
                    binding.verifyBT.isEnabled = false
                }
            }
        })
        binding.clickHandler = this
    }

    override fun handleClick(view: View) {
        when (view.id) {
            R.id.verifyBT -> {
                mViewModel.hitOtpValidateApi(signUpData!!, otpValue!!)
//                hitOtpValidateApi()
            }

            R.id.reSendTV -> {
                mViewModel.hitOtpApi(signUpData!!, true)
            }

            R.id.backIV -> {
                baseActivity!!.onBackPressed()
            }
        }
    }

    override fun onUpgradeDialogClick(action: String) {

    }

    private fun startTimer() {
        if (countDownTimer != null) {
            countDownTimer!!.cancel()
        }
        countDownTimer = object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val time = millisUntilFinished / 1000
                if (time > 9) {
                    binding.timeTV.text = "00:$time"
                } else {
                    binding.timeTV.text = "00:0$time"
                }
            }

            override fun onFinish() {
                binding.timeTV.text = "Time Out.Please click on resend option!"
            }
        }.start()
    }
}