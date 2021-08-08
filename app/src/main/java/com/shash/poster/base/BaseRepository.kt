package com.shash.poster.base

import com.shash.poster.network.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

/***
 * @author: Shashi
 * Base repository for all the repository   */
abstract class BaseRepository {

    /**
     * @param: apiCall: suspend () -> T
     * @return: Resource<T>
     * @author: Shashi
     * Makes safe API call and wraps the response in Resource<T> */
    suspend fun <T> safeApiCall(
        apiCall: suspend () -> T
    ): Resource<T> {
        return withContext(IO) {
            try {
                Resource.Success(apiCall.invoke())

            } catch (throwable: Throwable) {
                when (throwable) {
                    is HttpException -> {

                        Resource.Failure(isNetworkError = false, errorCode = throwable.code(), errorBody = throwable.response()?.errorBody(),message = throwable.message)
                    }
                    else -> {
                        Resource.Failure(isNetworkError = true, errorCode = null, errorBody = null,message = throwable.message)
                    }
                }
            }
        }
    }

    /**
     * @param: apiCall: suspend () -> T
     * @return: Resource<T>
     * @author: Shashi
     * Makes safe DB call and wraps the response in Resource<T> */
    suspend fun <T> safeCall(
        call: suspend () -> T
    ): Resource<T> {
        return withContext(Dispatchers.IO) {
            try {
                Resource.Success(call.invoke())
            } catch (throwable: Throwable) {
                when (throwable) {
                    is IOException -> {
                        Resource.Failure(true, null, null)
                    }
                    else -> {
                        Resource.Failure(false, null, null)
                    }
                }
            }
        }
    }


}