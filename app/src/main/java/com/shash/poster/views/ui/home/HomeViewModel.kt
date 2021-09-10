package com.shash.poster.views.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: HomeRepository) : ViewModel() {

    fun savePosterData(
        copy_links_only: Boolean,
        receiverChannelName: String,
        receiverChannelApiKey: String,
        receiverChannelChatId: String,
        excludeWords: String,
        convertAffiliate: Boolean
    ) {
        viewModelScope.launch {
            repository.savePosterData(
                copy_links_only,
                receiverChannelName,
                receiverChannelApiKey,
                receiverChannelChatId,
                excludeWords = excludeWords,
                convertAffiliate = convertAffiliate
            )
        }
    }

    fun saveCopyLinksOnly(copy_links_only: Boolean) {
        viewModelScope.launch {
            repository.saveCopyLinksOnly(copy_links_only)
        }
    }

    fun saveConvertAffiliate(checked: Boolean) {

        viewModelScope.launch {
            repository.saveConvertAffiliate(checked)
        }

    }

    fun saveExcludeWords(ex: String) {

        viewModelScope.launch {
            repository.saveExcludeWords(ex)
        }

    }

}