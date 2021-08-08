package com.shash.poster.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.shash.poster.views.ui.home.HomeRepository
import com.shash.poster.views.ui.home.HomeViewModel

/**
 * @author: Shashi
 * Model view factory to create view models */
@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val repository: BaseRepository) :
    ViewModelProvider.NewInstanceFactory() {
    /**
     * @author: Shashi
     * Creates objects of a view model specified*/
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(repository as HomeRepository) as T
            }

            else -> throw IllegalArgumentException("View model not found")
        }
    }
}