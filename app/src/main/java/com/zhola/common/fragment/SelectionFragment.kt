package com.zhola.common.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.zhola.R
import com.zhola.common.activity.LoginSignUpActivity
import com.zhola.common.extensions.replaceFragWithArgs
import com.zhola.common.extensions.replaceFragWithoutStack
import com.zhola.common.extensions.replaceFragment
import com.zhola.common.utils.ClickHandler
import com.zhola.common.utils.Const
import com.zhola.databinding.FragmentSelectionBinding

class SelectionFragment : BaseFragment(), ClickHandler {

    private lateinit var binding: FragmentSelectionBinding
    private var type = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            type = it.getInt("authenticationType")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_selection, container, false)
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
            R.id.driverBT -> {
                val bundle = Bundle()
                bundle.putInt("role_id", Const.TYPE_DRIVER)
                baseActivity!!.replaceFragWithArgs(SignUpFragment(), R.id.login_frame, bundle)
            }

            R.id.customerBT -> {
                val bundle = Bundle()
                bundle.putInt("role_id", Const.TYPE_CUSTOMER)
                baseActivity!!.replaceFragWithArgs(SignUpFragment(), R.id.login_frame, bundle)
            }

            R.id.vendorBT -> {
                val bundle = Bundle()
                bundle.putInt("role_id", Const.TYPE_VENDOR)
                baseActivity!!.replaceFragWithArgs(SignUpFragment(), R.id.login_frame, bundle)
            }
        }
    }
}