package com.telpo.pospay

import android.app.Application
import android.content.Context
import com.telpo.base.util.MLog
import com.telpo.pospay.db.database.PosPayDatabase
import com.telpo.pospay.db.repository.AidDBRepository
import com.telpo.pospay.db.repository.CapkDBRepository
import com.telpo.pospay.db.repository.DiaryDBRepository
import com.telpo.pospay.db.repository.NFCBlackDBRepository
import com.telpo.pospay.db.repository.UserRepository
import com.telpo.pospay.main.data.GlobalParams
import com.telpo.pospay.repository.HttpRepository
import com.telpo.pospay.repository.SocketRepository

/**
 * 全局Application
 */
class AppContext : Application() {

    val socketRepository by lazy { SocketRepository.getInstance(this) }
    val httpRepository by lazy { HttpRepository.getInstance(this) }

    val database by lazy { PosPayDatabase.getDatabase(this) }
    val diaryRepository by lazy { DiaryDBRepository(database.diaryDao()) }              //
    val userRepository by lazy { UserRepository(database.userDao()) }                   //操作员文件
    val capkRepository by lazy { CapkDBRepository(database.capkDao()) }                 //capk文件
    val aidRepository by lazy { AidDBRepository(database.aidDao()) }                    //adi文件
    val nfcBlackRepository by lazy { NFCBlackDBRepository(database.nfcBlackDao()) }     //nfc黑名单文件


    override fun onCreate() {
        super.onCreate()
        instance =
                this //        AutoSizeConfig.getInstance().setDesignWidthInDp(720).setDesignHeightInDp(1280)

        GlobalParams.systemParamPreferencesInit(this)
        initLog()

    }

    private fun initLog() { // 日志打开开关，默认为false，不打开日志
        MLog.setPrint(true)
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