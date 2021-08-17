package com.shash.poster.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
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
 */
@Singleton
object UserPreferences {

    //Constants
    private val KEY_COPY_LINKS_ONLY = booleanPreferencesKey("KEY_COPY_LINKS_ONLY")
    private val KEY_RECEIVER_CHANNEL_ID = stringPreferencesKey("KEY_RECEIVER_CHANNEL_ID")
    private val KEY_RECEIVER_CHANNEL_API = stringPreferencesKey("KEY_RECEIVER_API")
    private val KEY_RECEIVER_CHANNEL_CHAT_ID = stringPreferencesKey("KEY_RECEIVER_CHANNEL_CHAT_ID")


    /**set current logged in user details in datastore*/
    suspend fun setPosterData(context: Context, poser: Poster) {
        context.userDatastore.apply {
            edit { it[KEY_COPY_LINKS_ONLY] = poser.copy_links_only}
            edit { it[KEY_RECEIVER_CHANNEL_ID] = poser.receiver_channel_name}
            edit { it[KEY_RECEIVER_CHANNEL_API] = poser.receiver_channel_api_key}
            edit { it[KEY_RECEIVER_CHANNEL_CHAT_ID] = poser.receiver_channel_chat_id }
        }
    }

    /**Get current logged in user details from datastore*/
    fun getPosterData(context: Context): Flow<Poster> = context.userDatastore.data.map {

        val copyLinksOnly: Boolean = if ((it[KEY_COPY_LINKS_ONLY]) != null) it[KEY_COPY_LINKS_ONLY]!! else false
        val receiverChannelId = if (it[KEY_RECEIVER_CHANNEL_ID] != null) it[KEY_RECEIVER_CHANNEL_ID]!! else "receiverChannelId"
        val receiverChannelApi = if (it[KEY_RECEIVER_CHANNEL_API] != null) it[KEY_RECEIVER_CHANNEL_API]!! else "receiverChannelApi"
        val receiverChannelChatId =
            if (it[KEY_RECEIVER_CHANNEL_CHAT_ID] != null) it[KEY_RECEIVER_CHANNEL_CHAT_ID]!! else "receiverChannelChatId"

        Poster(
            copy_links_only = copyLinksOnly,
            receiver_channel_name = receiverChannelId,
            receiver_channel_api_key = receiverChannelApi,
            receiver_channel_chat_id = receiverChannelChatId,
        )
    }


    /**CLEAR current logged in user details from datastore*/
    suspend fun clear(context: Context) =
        context.userDatastore.edit {
            it.clear()
        }

    suspend fun saveCopyLinksOnly(mContext: Context, copyLinksOnly: Boolean) {
        mContext.userDatastore.apply {
            edit { it[KEY_COPY_LINKS_ONLY] = copyLinksOnly}
             }
        }

}