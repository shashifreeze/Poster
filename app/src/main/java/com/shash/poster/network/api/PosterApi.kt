package com.shash.poster.network.api

import com.shash.poster.utils.EndPoints
import okhttp3.ResponseBody
import retrofit2.http.*

/**
 * Interface for Home page calls
 */
interface PosterApi {
    //1947779589:AAEbhJVuyGH7fqehcI807Fk_ftCeHz_Awn0/sendMessage?chat_id=@Dealsofferwala&text=Testing
    /**
     * @param: ShopMasterID
     * @return: ProductListResponse
     * @author: Shashi
     * Get shop's featured product
     * */
    @GET("sendMessage")
    suspend fun postMessage(
        @Query("chat_id") chatId: String,
        @Query("text") message: String
    ): ResponseBody

}