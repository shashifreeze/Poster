package com.shash.poster.views.ui.home

import android.content.Context
import com.shash.poster.base.BaseRepository
import com.shash.poster.data.Poster
import com.shash.poster.network.api.HomeApi
import com.shash.poster.preferences.UserPreferences
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 *@author = Shashi
 *@date = 01/08/21
 *@description = This class handles
 */
@Singleton
class HomeRepository @Inject constructor(private val mContext: Context, api: HomeApi) :
    BaseRepository() {

    suspend fun savePosterData(
        senderChannelId: String,
        receiverChannelId: String,
        receiverChannelApiKey: String,
        receiverChannelChatId: String
    ) = withContext(IO) {

        val poster =
            Poster(senderChannelId, receiverChannelId, receiverChannelApiKey, receiverChannelChatId)

        UserPreferences.setPosterData(mContext, poster)
    }

}