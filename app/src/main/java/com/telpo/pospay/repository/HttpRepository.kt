package com.telpo.pospay.repository

import android.content.Context
import com.alibaba.fastjson.JSONObject
import com.telpo.base.internet.http.RetrofitHelper
import com.telpo.base.internet.http.ServerApi
import com.telpo.base.internet.response.TokenResponse
import kotlinx.coroutines.flow.first
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response

class HttpRepository private constructor(private val appContext: Context) :
    BaseRepository(appContext) {

    companion object {
        @Volatile
        private var instance: HttpRepository? = null

        fun getInstance(appContext: Context): HttpRepository {
            return instance?:synchronized(this) {
                instance?:HttpRepository(appContext).also { instance = it }
            }
        }
    }


    suspend fun deviceToken(): Response<TokenResponse>? {
        if (!networkUtils.isNetworkAvailable().first()) {
            throw Exception("Network is unAvailable") // 网络不可用
        }
        val jsonObject: JSONObject = JSONObject()
        jsonObject.put("appSecret", "c9537edd37521e415460b45b25a7ffdc")
        jsonObject.put("appKey", "telpo")
        jsonObject.put("grantType", "device_credentials")
        jsonObject.put("scope", "base")

        val body: RequestBody? = jsonObject.toJSONString()
            .toRequestBody("application/json; charset=utf-8".toMediaType())
        val url = "http://www.tpai-plat.com:8888"
        val response =
                RetrofitHelper.instance?.getApiService<ServerApi?>(
                    url,
                    ServerApi::class.java as Class<ServerApi?>)
                    ?.oauth(body)
        return response

    }


}