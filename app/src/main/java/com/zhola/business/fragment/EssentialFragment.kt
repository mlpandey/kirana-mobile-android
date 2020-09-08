package com.zhola.business.fragment

import android.os.Bundle
import android.view.*
import android.widget.PopupWindow
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.DataBindingUtil
import com.zhola.R
import com.zhola.common.activity.LoginSignUpActivity
import com.zhola.common.extensions.replaceFragWithArgs
import com.zhola.common.fragment.BaseFragment
import com.zhola.common.model.SignUpData
import com.zhola.common.utils.ClickHandler
import com.zhola.common.utils.OnUpgradeDialogClick
import com.zhola.databinding.FragmentEssentialBinding


class EssentialFragment : BaseFragment(), ClickHandler, OnUpgradeDialogClick {

    private lateinit var binding: FragmentEssentialBinding
    private var canDeliverSelectedType = -1
    private var merchandiseSelectedType = -1
    private var managementSelectedType = -1
    private var signUpData: SignUpData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            signUpData = it.getParcelable("signUpData")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_essential, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (baseActivity as LoginSignUpActivity).setToolbar("Business Profile", true)
        initUI()
    }

    private fun initUI() {
        setPopUpValues()
        binding.clickHandler = this
    }

    private fun setPopUpValues() {
        if (canDeliverSelectedType != -1) {
            if (canDeliverSelectedType == 0) {
                binding.canDeliverTV.text = getString(R.string.yes)
            } else {
                binding.canDeliverTV.text = getString(R.string.no)
            }
        }

        if (merchandiseSelectedType != -1) {
            if (merchandiseSelectedType == 0) {
                binding.digitalTV.text = getString(R.string.yes)
            } else {
                binding.digitalTV.text = getString(R.string.no)
            }
        }

        if (managementSelectedType != -1) {
            if (canDeliverSelectedType == 0) {
                binding.managementSystemTV.text = getString(R.string.yes)
            } else {
                binding.managementSystemTV.text = getString(R.string.no)
            }
        }
    }

    override fun handleClick(view: View) {
        when (view.id) {
            R.id.saveNextBT -> {
                if (isValidate()) {
                    signUpData!!.companyType = binding.companyTypeET.text.toString()
                    signUpData!!.businessCategory = binding.businessCategoryET.text.toString()
                    signUpData!!.canDeliver = canDeliverSelectedType == 0
                    signUpData!!.isMerchantDigital = merchandiseSelectedType == 0
                    signUpData!!.haveInventoryManagement = managementSelectedType == 0
                    val bundle = Bundle()
                    bundle.putParcelable("signUpData", signUpData)
                    baseActivity!!.replaceFragWithArgs(DocumentFragment(), R.id.login_frame, bundle)
                }
            }

            R.id.canDeliverCV, R.id.canDeliverTIL, R.id.canDeliverET -> {
                openPopupWindow(binding.canDeliverCV)
            }

            R.id.merchandiseCV, R.id.digitalTIL, R.id.digitalET -> {
                openPopupWindow(binding.merchandiseCV)
            }

            R.id.managementSystemCV, R.id.managementSystemTIL, R.id.managementSystemET -> {
                openPopupWindow(binding.managementSystemCV)
            }
        }
    }

    fun openPopupWindow(buttonView: View) {
        val popUpView: View = layoutInflater.inflate(
            R.layout.profile_popup,
            null
        ) // inflating popup layout

        val popupWindow = PopupWindow(
            popUpView,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        popupWindow.isOutsideTouchable = true
        popupWindow.animationStyle = android.R.style.Animation_Dialog
        popupWindow.showAsDropDown(buttonView, -40, 18)
//        popupWindow.setOnDismissListener {
//            baseActivity!!.showToast("fsdfkjfk,k")
//        }
        val yesTV = popUpView.findViewById<AppCompatTextView>(R.id.yesTV)
        val noTV = popUpView.findViewById<AppCompatTextView>(R.id.noTV)
        yesTV.setOnClickListener {
            popupWindow.dismiss()
            when (buttonView.id) {
                R.id.canDeliverCV, R.id.canDeliverTIL -> {
                    canDeliverSelectedType = 0
                    binding.canDeliverET.setText(getString(R.string.yes))
                }

                R.id.merchandiseCV, R.id.digitalTIL -> {
                    merchandiseSelectedType = 0
                    binding.digitalET.setText(getString(R.string.yes))
                }

                R.id.managementSystemCV, R.id.managementSystemTIL -> {
                    managementSelectedType = 0
                    binding.managementSystemET.setText(getString(R.string.yes))
                }
            }
        }

        noTV.setOnClickListener {
            popupWindow.dismiss()
            when (buttonView.id) {
                R.id.canDeliverCV, R.id.canDeliverTIL -> {
                    canDeliverSelectedType = 1
                    binding.canDeliverET.setText(getString(R.string.no))
                }

                R.id.merchandiseCV, R.id.digitalTIL -> {
                    merchandiseSelectedType = 1
                    binding.digitalET.setText(getString(R.string.no))
                }

                R.id.managementSystemCV, R.id.managementSystemTIL -> {
                    managementSelectedType = 1
                    binding.managementSystemET.setText(getString(R.string.no))
                }
            }
        }
    }

    private fun isValidate(): Boolean {
        if (binding.companyTypeET.text.toString().trim().isEmpty()) {
            baseActivity!!.upgradeAlertBox(getString(R.string.please_enter_the_company_type), this)
        } else if (binding.businessCategoryET.text.toString().trim().isEmpty()) {
            baseActivity!!.upgradeAlertBox(
                getString(R.string.please_enter_the_business_category),
                this
            )
        } else if (canDeliverSelectedType == -1) {
            baseActivity!!.upgradeAlertBox(getString(R.string.please_select_the_can_deliver), this)
        } else if (merchandiseSelectedType == -1) {
            baseActivity!!.upgradeAlertBox(
                getString(R.string.please_select_the_is_merchandise_digital),
                this
            )
        } else if (managementSelectedType == -1) {
            baseActivity!!.upgradeAlertBox(
                getString(R.string.please_select_the_has_inventory_management_system),
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