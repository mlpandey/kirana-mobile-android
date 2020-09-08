package com.zhola.customer.fragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.zhola.business.activity.BusinessMainActivity
import com.zhola.R
import com.zhola.common.extensions.replaceFragment
import com.zhola.common.fragment.BaseFragment
import com.zhola.common.fragment.ChangePasswordFragment
import com.zhola.common.utils.ClickHandler
import com.zhola.customer.activity.CustomerMainActivity
import com.zhola.databinding.FragmentMyProfileBinding
import com.zhola.driver.activity.DriverMainActivity
import kotlinx.android.synthetic.main.dialog_common.view.txt_postive
import kotlinx.android.synthetic.main.dialog_logout.view.*

class MyProfileFragment : BaseFragment(), ClickHandler {

    private lateinit var binding: FragmentMyProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_profile, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (baseActivity is CustomerMainActivity) {
            (baseActivity as CustomerMainActivity).setToolbar("My Profile", true, false, false)
        } else if (baseActivity is BusinessMainActivity) {
            (baseActivity as BusinessMainActivity).setToolbar("My Profile", true, false, false)
        } else if (baseActivity is DriverMainActivity) {
            (baseActivity as DriverMainActivity).setToolbar("My Profile", true, false)
        }
        initUI()
    }

    private fun initUI() {
        setHasOptionsMenu(true)
        binding.clickHandler = this
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.profile_menu, menu)
    }

    override fun handleClick(view: View) {
        when (view.id) {
            R.id.changeTV -> {
                if (baseActivity is CustomerMainActivity) {
                    baseActivity!!.replaceFragment(SavedAddressFragment(), R.id.container)
                }
            }

            R.id.changePasswordCV -> {
                if (baseActivity is CustomerMainActivity) {
                    baseActivity!!.replaceFragment(ChangePasswordFragment(), R.id.container)
                }
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