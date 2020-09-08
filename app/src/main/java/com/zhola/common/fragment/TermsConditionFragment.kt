package com.zhola.common.fragment

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import com.zhola.R
import com.zhola.common.activity.LoginSignUpActivity
import com.zhola.common.utils.ClickHandler
import com.zhola.common.utils.SetData
import com.zhola.databinding.FragmentTermsConditionBinding

class TermsConditionFragment : BaseFragment(), ClickHandler {

    private lateinit var binding: FragmentTermsConditionBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_terms_condition, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (baseActivity as LoginSignUpActivity).setToolbar("", true)
        setHasOptionsMenu(true)
        initUI()
    }

    private fun initUI() {
        binding.clickHandler = this
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.cross_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.cancelMI -> {
                SetData.data.sendData(0)
                baseActivity!!.onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun handleClick(view: View) {
        when (view.id) {
            R.id.acceptBT -> {
                SetData.data.sendData(1)
                baseActivity!!.onBackPressed()
            }
        }
    }
}