package com.telpo.base.internet.response

class TokenResponse {
    private var access_token: String? = null
    private var expires_in = 0
    private var token_type: String? = null
    private var scope: String? = null

    fun getAccess_token(): String? {
        return access_token
    }

    fun setAccess_token(access_token: String?) {
        this.access_token = access_token
    }

    fun getExpires_in(): Int {
        return expires_in
    }

    fun setExpires_in(expires_in: Int) {
        this.expires_in = expires_in
    }

    fun getToken_type(): String? {
        return token_type
    }

    fun setToken_type(token_type: String?) {
        this.token_type = token_type
    }

    fun getScope(): String? {
        return scope
    }

    fun setScope(scope: String?) {
        this.scope = scope
    }
}