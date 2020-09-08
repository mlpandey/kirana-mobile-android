package com.zhola.customer.fragment

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.MenuItemCompat
import androidx.databinding.DataBindingUtil
import com.zhola.R
import com.zhola.common.extensions.replaceFragment
import com.zhola.common.fragment.BaseFragment
import com.zhola.common.utils.ClickHandler
import com.zhola.customer.activity.CustomerMainActivity
import com.zhola.customer.adapter.ImagesAdapter
import com.zhola.databinding.FragmentAddToCartBinding
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter

class AddToCartFragment : BaseFragment(), ClickHandler {

    private lateinit var binding: FragmentAddToCartBinding
    private val adapter by lazy { ImagesAdapter(baseActivity!!) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_to_cart, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (baseActivity as CustomerMainActivity).setToolbar("Blue Berries", true, false)
        setHasOptionsMenu(true)
        initUI()
    }

    private fun initUI() {
        binding.clickHandler = this
        binding.imagesRV.adapter = AlphaInAnimationAdapter(adapter)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.shop_filled_menu, menu)
        val item = menu.findItem(R.id.cartMI)
        MenuItemCompat.setActionView(item, R.layout.cart_filled_menu_layout)
        val actionView = MenuItemCompat.getActionView(item)
        val cartIV = actionView.findViewById<AppCompatImageView>(R.id.cartIV)
        cartIV.setOnClickListener {
            baseActivity!!.replaceFragment(SendRequestFragment(), R.id.container)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return false
    }

    override fun handleClick(view: View) {
        when (view.id) {
            R.id.addCartBT -> {
                baseActivity!!.replaceFragment(SendRequestFragment(), R.id.container)
            }
        }
    }
}