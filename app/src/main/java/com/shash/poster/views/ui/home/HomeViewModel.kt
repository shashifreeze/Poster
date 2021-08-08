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

    fun savePosterData(senderChannelId:String,receiverChannelId:String,receiverChannelApiKey:String,receiverChannelChatId:String)
    {
        viewModelScope.launch {
            repository.savePosterData(senderChannelId,receiverChannelId,receiverChannelApiKey,receiverChannelChatId)
        }
    }

}