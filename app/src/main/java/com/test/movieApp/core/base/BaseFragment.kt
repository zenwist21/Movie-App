package com.test.movieApp.core.base

import androidx.fragment.app.Fragment
import com.test.movieApp.feature_movie.presentation.component.adapter.AdapterGenre
import javax.inject.Inject

abstract class BaseFragment : Fragment() {
    @Inject
    lateinit var adapterGenre: AdapterGenre
    abstract fun initController()
    abstract fun observer()
    abstract fun initListener()
    abstract fun setupAdapter()

}