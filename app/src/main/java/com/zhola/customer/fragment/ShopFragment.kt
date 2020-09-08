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
import com.zhola.common.adapter.BaseAdapter
import com.zhola.customer.adapter.CategoryAdapter
import com.zhola.customer.adapter.RecentVisitAdapter
import com.zhola.databinding.FragmentShopBinding
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter

class ShopFragment : BaseFragment(), BaseAdapter.OnItemClickListener, ClickHandler {

    private lateinit var binding: FragmentShopBinding
    private val recentAdapter by lazy { RecentVisitAdapter(baseActivity!!) }
    private val adapter by lazy { CategoryAdapter(baseActivity!!) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_shop, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (baseActivity as CustomerMainActivity).setToolbar("Shop", true, true, true)
        setHasOptionsMenu(true)
        initUI()
    }

    private fun initUI() {
        binding.clickHandler = this
        recentAdapter.setOnItemClickListener(this)
        binding.recentRV.adapter = AlphaInAnimationAdapter(recentAdapter)
        adapter.setOnItemClickListener(this)
        binding.storesRV.adapter = AlphaInAnimationAdapter(adapter)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.shop_menu, menu)

        val item = menu.findItem(R.id.cartMI)
        MenuItemCompat.setActionView(item, R.layout.cart_menu_layout)
        val actionView = MenuItemCompat.getActionView(item)
        val cartIV = actionView.findViewById<AppCompatImageView>(R.id.cartIV)
        cartIV.setOnClickListener {
            baseActivity!!.replaceFragment(SendRequestFragment(), R.id.container)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return false
    }

    override fun onItemClick(vararg itemData: Any) {
        baseActivity!!.replaceFragment(ItemDetailFragment(), R.id.container)
    }

    override fun handleClick(view: View) {
        when (view.id) {
            R.id.addressTV -> {
                baseActivity!!.replaceFragment(SavedAddressFragment(), R.id.container)
            }
        }
    }
}