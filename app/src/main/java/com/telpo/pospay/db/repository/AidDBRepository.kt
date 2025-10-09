package com.telpo.pospay.db.repository

import com.telpo.pospay.db.bean.AIDDB
import com.telpo.pospay.db.bean.CAPKDB
import com.telpo.pospay.db.dao.AidDBDao
import com.telpo.pospay.db.dao.CAPKDBDao
import kotlinx.coroutines.runBlocking

class AidDBRepository(dao: AidDBDao) :
    BaseDBRepository<AIDDB, AidDBDao>(dao) {
    override fun getTableName(): String {
        return AidDBDao.getTableName()
    }


    fun list(): List<AIDDB> {
        return runBlocking {
            dao.list()
        }
    }




}