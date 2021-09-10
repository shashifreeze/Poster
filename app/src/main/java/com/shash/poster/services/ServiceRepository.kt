package com.shash.poster.services

import com.shash.poster.base.BaseRepository
import com.shash.poster.data.Poster
import com.shash.poster.network.api.PosterApi
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
class ServiceRepository @Inject constructor(private val posterApi: PosterApi):BaseRepository() {

    suspend fun postMessage(message:String, chatId:String) = withContext(IO)
    {

        safeCall { posterApi.postMessage(message = message, chatId = chatId)  }
    }

}