package com.zhola.customer.fragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.zhola.R
import com.zhola.common.extensions.replaceFragment
import com.zhola.common.fragment.BaseFragment
import com.zhola.common.utils.ClickHandler
import com.zhola.common.utils.OnUpgradeDialogClick
import com.zhola.customer.activity.CustomerMainActivity
import com.zhola.customer.adapter.CartAdapter
import com.zhola.databinding.FragmentMakePaymentBinding
import com.zhola.databinding.FragmentSendRequestBinding
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter
import kotlinx.android.synthetic.main.dialog_common.view.*

class MakePaymentFragment : BaseFragment(), ClickHandler {

    private lateinit var binding: FragmentMakePaymentBinding
    private val adapter by lazy { CartAdapter(baseActivity!!) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_make_payment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (baseActivity as CustomerMainActivity).setToolbar("My Cart", true, false, false)
        initUI()
    }

    private fun initUI() {
        setHasOptionsMenu(true)
        binding.clickHandler = this
        binding.cartRV.adapter = AlphaInAnimationAdapter(adapter)
    }

    override fun handleClick(view: View) {
        when (view.id) {
            R.id.makePaymentBT -> {
                openPaymentAlert()
            }

            R.id.changeTV -> {
                baseActivity!!.replaceFragment(SavedAddressFragment(), R.id.container)
            }
        }
    }

    private fun openPaymentAlert() {
        val mDialogView =
            LayoutInflater.from(baseActivity).inflate(R.layout.dialog_order_placed, null)
        val mBuilder = AlertDialog.Builder(baseActivity!!).setView(mDialogView)
        mBuilder.setCancelable(false)

        val mAlertDialog = mBuilder.show()
        mAlertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        mDialogView.txt_postive.setOnClickListener {
            mAlertDialog.dismiss()
            (baseActivity as CustomerMainActivity).gotoShopFragment()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.cancel_order_menu, menu)
        val item = menu.getItem(0)
        val s = SpannableString("Cancel Order")
        s.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(baseActivity!!, R.color.dark_gray)),
            0,
            s.length,
            0
        )
        item.title = s
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.cancelOrderMI -> {
                (baseActivity as CustomerMainActivity).gotoShopFragment()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}