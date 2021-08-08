package com.shash.poster.views.main

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 *@author = Shashi
 *@date = 01/08/21
 *@description = This class handles
 */
@HiltViewModel
class MainViewModel @Inject constructor(repository: MainRepository):ViewModel() {
}