package com.shash.poster.views.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shash.poster.data.Poster
import com.shash.poster.network.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: HomeRepository) : ViewModel() {

    fun savePosterData(copy_links_only:Boolean,receiverChannelName:String,receiverChannelApiKey:String,receiverChannelChatId:String)
    {
        viewModelScope.launch {
            repository.savePosterData(copy_links_only,receiverChannelName,receiverChannelApiKey,receiverChannelChatId)
        }
    }

    fun saveCopyLinksOnly(copy_links_only:Boolean)
    {
        viewModelScope.launch {
            repository.saveCopyLinksOnly(copy_links_only)
        }
    }

}