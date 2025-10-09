package com.telpo.base.internet.http

import com.telpo.base.internet.response.TokenResponse
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ServerApi {


    @POST("/api/third/v2/oauth/deviceToken")
    suspend fun oauth(@Body requestBody: RequestBody?): Response<TokenResponse>

}