package com.telpo.base.internet.response

/**
 * 通用网络请求响应模型
 */
open class BaseResponse {
    /**
     * 状态码
     * 等于0表示成功
     */
    var return_code = 0

    /**
     * 出错的提示信息
     * 发生了错误不一定有
     */
    var message: String? = null

    /**
     * 是否成功
     *
     * @return
     */
    val isSucceeded: Boolean
        get() = return_code == 2000
}