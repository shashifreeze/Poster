package com.shash.poster.network.api

import com.shash.poster.utils.EndPoints
import okhttp3.ResponseBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * Interface for Home page calls
 */
interface HomeApi {

    /**
     * @param: ShopMasterID
     * @return: ProductListResponse
     * @author: Shashi
     * Get shop's featured product
     * */
    @FormUrlEncoded
    @POST(EndPoints.GET_SHOP_LIST_BY_CAT)
    suspend fun getShopListByCategory(
        @Field("CategoryID") catId: Int,
        @Field("lat") lat: Double,
        @Field("lng") lng: Double
    ): ResponseBody

}