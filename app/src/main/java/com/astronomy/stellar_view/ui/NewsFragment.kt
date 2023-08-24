package com.astronomy.stellar_view.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import com.astronomy.stellar_view.databinding.FragmentNewsBinding
import com.astronomy.stellar_view.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsFragment : BaseFragment<FragmentNewsBinding>(){

    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentNewsBinding.inflate(inflater, container, false)
}