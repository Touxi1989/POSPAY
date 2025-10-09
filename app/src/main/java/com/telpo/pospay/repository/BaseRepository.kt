package com.telpo.pospay.repository

import android.content.Context
import com.telpo.base.util.NetworkUtils
import kotlinx.coroutines.flow.Flow

open class BaseRepository public constructor(private val appContext: Context) {

    protected val networkUtils = NetworkUtils(appContext)

    fun getNetworkState(): Flow<Boolean> {
        return networkUtils.isNetworkAvailable()
    }
}