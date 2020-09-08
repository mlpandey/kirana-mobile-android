package com.zhola.driver.fragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.zhola.R
import com.zhola.common.extensions.replaceFragment
import com.zhola.common.fragment.BaseFragment
import com.zhola.common.fragment.ChangePasswordFragment
import com.zhola.common.utils.ClickHandler
import com.zhola.databinding.FragmentDriverMyProfileBinding
import com.zhola.driver.activity.DriverMainActivity
import kotlinx.android.synthetic.main.dialog_common.view.*
import kotlinx.android.synthetic.main.dialog_common.view.txt_postive
import kotlinx.android.synthetic.main.dialog_logout.view.*

class DriverMyProfileFragment : BaseFragment(), ClickHandler {

    private lateinit var binding: FragmentDriverMyProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_driver_my_profile, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (baseActivity as DriverMainActivity).setToolbar("My Profile", true, false, true)
        initUI()
    }

    private fun initUI() {
        binding.clickHandler = this
    }

    override fun handleClick(view: View) {
        when (view.id) {
            R.id.changePasswordCV -> {
                baseActivity!!.replaceFragment(ChangePasswordFragment(), R.id.driver_container)
            }

            R.id.logoutTV -> {
                logoutAlertDialog()
            }
        }
    }

    private fun logoutAlertDialog() {
        val mDialogView = LayoutInflater.from(baseActivity).inflate(R.layout.dialog_logout, null)
        val mBuilder = AlertDialog.Builder(baseActivity!!).setView(mDialogView)
        mBuilder.setCancelable(false)

        val mAlertDialog = mBuilder.show()
        mAlertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        mDialogView.txt_postive.setOnClickListener {
            mAlertDialog.dismiss()
            baseActivity!!.prefStore!!.cleanPref()
            baseActivity!!.retrofitClient!!.setLoginStatus(null)
            baseActivity!!.gotoLoginSignUpActivity()
        }

        mDialogView.txt_negative.setOnClickListener {
            mAlertDialog.dismiss()
        }
    }
}