package com.zhola.customer.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.zhola.R
import com.zhola.common.fragment.BaseFragment
import com.zhola.customer.adapter.SuggestedItemsAdapter
import com.zhola.databinding.FragmentSuggestionsBinding
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter

class SuggestionsFragment : BaseFragment() {

    private lateinit var binding: FragmentSuggestionsBinding
    private val adapter by lazy { SuggestedItemsAdapter(baseActivity!!) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_suggestions, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
    }

    private fun initUI() {
        binding.suggestionRV.adapter = AlphaInAnimationAdapter(adapter)
    }
}