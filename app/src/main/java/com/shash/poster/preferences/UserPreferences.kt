package com.shash.poster.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.shash.poster.data.Poster
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Singleton

//top level
val Context.userDatastore: DataStore<Preferences> by preferencesDataStore(name = "PosterT")

/**
 *Singleton class
 *
 */
@Singleton
object UserPreferences {

    //Constants
    private val KEY_SENDER_CHANNEL_ID = stringPreferencesKey("KEY_FROM_CHANNEL")
    private val KEY_RECEIVER_CHANNEL_ID = stringPreferencesKey("KEY_RECEIVER_CHANNEL_ID")
    private val KEY_RECEIVER_CHANNEL_API = stringPreferencesKey("KEY_RECEIVER_API")
    private val KEY_RECEIVER_CHANNEL_CHAT_ID = stringPreferencesKey("KEY_RECEIVER_CHANNEL_CHAT_ID")


    /**set current logged in user details in datastore*/
    suspend fun setPosterData(context: Context, poser: Poster) {
        context.userDatastore.apply {
            edit { it[KEY_SENDER_CHANNEL_ID] = poser.sender_channel_id}
            edit { it[KEY_RECEIVER_CHANNEL_ID] = poser.receiver_channel_id}
            edit { it[KEY_RECEIVER_CHANNEL_API] = poser.receiver_channel_api_key}
            edit { it[KEY_RECEIVER_CHANNEL_CHAT_ID] = poser.receiver_channel_chat_id }
        }
    }

    /**Get current logged in user details from datastore*/
    fun getPosterData(context: Context): Flow<Poster> = context.userDatastore.data.map {

        val senderChannelId: String = if ((it[KEY_SENDER_CHANNEL_ID]) != null) it[KEY_SENDER_CHANNEL_ID]!! else "senderChannelId"
        val receiverChannelId = if (it[KEY_RECEIVER_CHANNEL_ID] != null) it[KEY_RECEIVER_CHANNEL_ID]!! else "receiverChannelId"
        val receiverChannelApi = if (it[KEY_RECEIVER_CHANNEL_API] != null) it[KEY_RECEIVER_CHANNEL_API]!! else "receiverChannelApi"
        val receiverChannelChatId =
            if (it[KEY_RECEIVER_CHANNEL_CHAT_ID] != null) it[KEY_RECEIVER_CHANNEL_CHAT_ID]!! else "receiverChannelChatId"

        Poster(
            sender_channel_id = senderChannelId,
            receiver_channel_id = receiverChannelId,
            receiver_channel_api_key = receiverChannelApi,
            receiver_channel_chat_id = receiverChannelChatId,
        )
    }


    /**CLEAR current logged in user details from datastore*/
    suspend fun clear(context: Context) =
        context.userDatastore.edit {
            it.clear()
        }
}