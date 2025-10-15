package com.telpo.base.internet.http

import com.telpo.base.internet.response.TokenResponse
import com.telpo.gx_social_security.bean.DeviceTokenRequest
import com.telpo.gx_social_security.bean.GgzpxxRequest
import com.telpo.gx_social_security.bean.GrcbxxRequest
import com.telpo.gx_social_security.bean.GrjfmxRequest
import com.telpo.gx_social_security.bean.SbklsgsjgRequest
import com.telpo.gx_social_security.bean.SbkzkjdRequest
import com.telpo.gx_social_security.bean.TokenRequest
import com.telpo.gx_social_security.bean.UserInfosRequest
import com.telpo.gx_social_security.bean.YldyffxxRequest
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ServerApi {


    @POST("/api/third/v2/oauth/deviceToken") //中台获取token接口，测试用
    suspend fun oauth(@Body requestBody: DeviceTokenRequest?): Response<TokenResponse>

    @POST("/api/third/v2/oauth/deviceToken") //中台获取token接口，测试用
    suspend fun oauth(@Body requestBody: RequestBody?): Response<TokenResponse>


    @POST("/ecooppf/rest/apibuildService/queryGgzpxx") //公共招聘信息接口
    suspend fun queryGgzpxx(@Body requestBody: GgzpxxRequest): Response<String>

    @POST("/ecooppf/rest/neusoftUaaService/getToken") //一体机无密获取令牌token
    suspend fun getToken(@Body requestBody: TokenRequest): Response<String>


    @POST("/ecooppf/rest/neusoftUaaService/getUserInfos") //根据令牌获取用户信息
    suspend fun getUserInfos(@Body requestBody: UserInfosRequest): Response<String>


    @POST("/ecooppf/rest/neusoftUaaService/querySbkzkjd") //制卡进度查询
    suspend fun querySbkzkjd(@Body requestBody: SbkzkjdRequest): Response<String>

    @POST("/ecooppf/rest/neusoftUaaService/sbklsgsjg") //社保卡临时挂失、解挂
    suspend fun sbklsgsjg(@Body requestBody: SbklsgsjgRequest): Response<String>


    @POST("/ecooppf/rest/neusoftUaaService/queryYldyffxx") //养老待遇发放明细
    suspend fun queryYldyffxx(@Body requestBody: YldyffxxRequest): Response<String>


    @POST("/ecooppf/rest/neusoftUaaService/queryGrcbxx") //个人参保信息查询
    suspend fun queryGrcbxx(@Body requestBody: GrcbxxRequest): Response<String>


    @POST("/ecooppf/rest/neusoftUaaService/queryGrjfmx") //个人缴费信息查询
    suspend fun queryGrjfmx(@Body requestBody: GrjfmxRequest): Response<String>


}