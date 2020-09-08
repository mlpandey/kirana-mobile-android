package com.zhola.customer.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.ybs.countrypicker.CountryPicker
import com.zhola.R
import com.zhola.common.fragment.BaseFragment
import com.zhola.common.utils.ClickHandler
import com.zhola.customer.activity.CustomerMainActivity
import com.zhola.databinding.FragmentAddAddressBinding
import kotlinx.android.synthetic.main.fragment_add_address.*

class AddAddressFragment : BaseFragment(), ClickHandler {

    private lateinit var binding: FragmentAddAddressBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_address, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (baseActivity as CustomerMainActivity).setToolbar("Add New Address", true, false, false)

        initUI()
    }

    private fun initUI() {
        binding.clickHandler = this
    }

    override fun handleClick(view: View) {
        when (view.id) {
            R.id.saveAddressBT -> {
                baseActivity!!.onBackPressed()
            }

            R.id.countryTIL,R.id.countryCV,R.id.countryET -> {
                val picker =
                    CountryPicker.newInstance("Select Country") // dialog title

                picker.setListener { name, code, dialCode, flagDrawableResID ->
                    picker.dismiss()
                    binding.countryET.setText(name)
                }
                picker.show(baseActivity!!.supportFragmentManager, "COUNTRY_PICKER")
            }
        }
    }
}