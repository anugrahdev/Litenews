package com.anugrahdev.litenews.data.network

import com.anugrahdev.litenews.utils.*
import retrofit2.Response

abstract class SafeApiRequest {
    suspend fun <T:Any> apiRequest(call: suspend() -> Response<T>):T{
        val response = call.invoke()
        val responseCode = response.code()
        if(response.isSuccessful){
            return response.body()!!
        }else{
            when (responseCode) {
                400 -> throw BusinessException(response.errorBody())
                401 -> throw UnauthorizedException()
                403 -> throw ForbiddenException()
                404 -> throw NotFoundException()
                422 -> throw BusinessException(response.errorBody())
                429 -> throw BusinessException(response.errorBody())
                500 -> throw ServerErrorException()
                502 -> throw BadGatewayException()
                573 -> throw BusinessException(response.errorBody())
            }
            throw Exception()
        }
    }
}

