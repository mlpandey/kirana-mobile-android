package com.zhola.customer.fragment

import android.os.Bundle
import android.os.Handler
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.*
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.zhola.R
import com.zhola.common.extensions.replaceFragment
import com.zhola.common.fragment.BaseFragment
import com.zhola.common.utils.ClickHandler
import com.zhola.customer.activity.CustomerMainActivity
import com.zhola.customer.adapter.CartAdapter
import com.zhola.databinding.FragmentSendRequestBinding
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter

class SendRequestFragment : BaseFragment(), ClickHandler {

    private lateinit var binding: FragmentSendRequestBinding
    private val adapter by lazy { CartAdapter(baseActivity!!) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_send_request, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (baseActivity as CustomerMainActivity).setToolbar("My Cart", true, false, false)
        initUI()
    }

    private fun initUI() {
        binding.clickHandler = this
        binding.cartRV.adapter = AlphaInAnimationAdapter(adapter)
    }

    override fun handleClick(view: View) {
        when (view.id) {

            R.id.sendOrderBT -> {
                binding.sendOrderBT.background =
                    ContextCompat.getDrawable(baseActivity!!, R.drawable.gray_button_bg)
                binding.sendOrderBT.text = "Wait For Response..."
                binding.sendOrderBT.setTextColor(
                    ContextCompat.getColor(
                        baseActivity!!,
                        R.color.black
                    )
                )
                binding.sendOrderBT.isEnabled = false
                setHasOptionsMenu(true)
                Handler().postDelayed({
                    baseActivity!!.replaceFragment(MyCartTabFragment(), R.id.container)
                }, 1000)
            }

            R.id.changeTV -> {
                baseActivity!!.replaceFragment(SavedAddressFragment(), R.id.container)
            }
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