package com.telpo.pospay.db.repository

import com.telpo.pospay.db.bean.CAPKDB
import com.telpo.pospay.db.dao.CAPKDBDao
import kotlinx.coroutines.runBlocking

class CapkDBRepository(dao: CAPKDBDao) :
    BaseDBRepository<CAPKDB, CAPKDBDao>(dao) {
    override fun getTableName(): String {
        return CAPKDBDao.getTableName()
    }


    fun list(): List<CAPKDB> {
        return runBlocking {
            dao.list()
        }
    }




}