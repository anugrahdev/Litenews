package com.anugrahdev.litenews.data.network

import com.anugrahdev.litenews.utils.ApiException
import retrofit2.Response

abstract class SafeApiRequest {
    suspend fun <T:Any> apiRequest(call: suspend() -> Response<T>):T{
        val response = call.invoke()
        if(response.isSuccessful){
            return response.body()!!
        }else{
            throw ApiException(response.message())
        }
    }
}

