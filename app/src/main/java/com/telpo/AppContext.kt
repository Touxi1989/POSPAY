package com.telpo

import android.app.Application
import android.content.Context
import com.telpo.base.util.TLog
import com.telpo.pospay.db.database.DiaryDatabase
import com.telpo.pospay.repository.DiaryRepository
import com.telpo.pospay.repository.HttpRepository
import com.telpo.pospay.repository.SocketRepository

/**
 * 全局Application
 */
class AppContext : Application() {

    val database by lazy { DiaryDatabase.getDatabase(this) }
    val diaryRepository by lazy { DiaryRepository(database.diaryDao()) }
    val socketRepository by lazy { SocketRepository.getInstance(this) }
    val httpRepository by lazy { HttpRepository.getInstance(this) }

    override fun onCreate() {
        super.onCreate()
        instance = this
//        AutoSizeConfig.getInstance().setDesignWidthInDp(1280).setDesignHeightInDp(720)
//        AutoSizeConfig.getInstance().setDesignWidthInDp(720).setDesignHeightInDp(1600)
        initLog()

    }

    private fun initLog() {
        // 日志打开开关，默认为false，不打开日志
        TLog.setPrint(true)
    }


    companion object {
        const val TAG = "AppContexts"

        //全局Context
        @JvmStatic
        lateinit var instance: AppContext

        @JvmStatic
        fun getContext(): Context {
            return instance.applicationContext
        }
    }


}