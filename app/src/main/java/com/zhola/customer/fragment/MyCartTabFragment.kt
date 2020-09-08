package com.zhola.customer.fragment

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.*
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.zhola.R
import com.zhola.common.extensions.replaceFragWithoutTransition
import com.zhola.common.fragment.BaseFragment
import com.zhola.common.utils.ClickHandler
import com.zhola.customer.activity.CustomerMainActivity
import com.zhola.databinding.FragmentMyCartTabBinding


class MyCartTabFragment : BaseFragment(), ClickHandler {

    private lateinit var binding: FragmentMyCartTabBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_cart_tab, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (baseActivity as CustomerMainActivity).setToolbar("My Cart", true, false)
        initUI()
    }

    private fun initUI() {
        setHasOptionsMenu(true)
        baseActivity!!.replaceFragWithoutTransition(CheckoutFragment(), R.id.tab_container)
        binding.clickHandler = this
    }

    override fun handleClick(view: View) {
        when (view.id) {
            R.id.myItemsTV -> {
                reset()
                binding.myItemsTV.background =
                    ContextCompat.getDrawable(baseActivity!!, R.drawable.orange_selected_tab)
                binding.myItemsTV.setTextColor(
                    ContextCompat.getColor(
                        baseActivity!!,
                        R.color.black
                    )
                )
                baseActivity!!.replaceFragWithoutTransition(CheckoutFragment(), R.id.tab_container)
            }

            R.id.suggestionsTV -> {
                reset()
                binding.suggestionsTV.background =
                    ContextCompat.getDrawable(baseActivity!!, R.drawable.orange_selected_tab)
                binding.suggestionsTV.setTextColor(
                    ContextCompat.getColor(
                        baseActivity!!,
                        R.color.black
                    )
                )
                baseActivity!!.replaceFragWithoutTransition(
                    SuggestionsFragment(),
                    R.id.tab_container
                )
            }
        }
    }

    private fun reset() {
        binding.myItemsTV.setBackgroundColor(ContextCompat.getColor(baseActivity!!, R.color.white))
        binding.suggestionsTV.setBackgroundColor(
            ContextCompat.getColor(
                baseActivity!!,
                R.color.white
            )
        )
        binding.suggestionsTV.setTextColor(
            ContextCompat.getColor(
                baseActivity!!,
                R.color.dark_gray
            )
        )
        binding.myItemsTV.setTextColor(ContextCompat.getColor(baseActivity!!, R.color.dark_gray))
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