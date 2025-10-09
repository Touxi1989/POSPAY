package com.telpo.pospay.db.repository

import com.telpo.pospay.db.bean.NFCBlackListDB
import com.telpo.pospay.db.dao.NFCBlackDBDao
import kotlinx.coroutines.runBlocking

class NFCBlackDBRepository(dao: NFCBlackDBDao) :
    BaseDBRepository<NFCBlackListDB, NFCBlackDBDao>(dao) {
    override fun getTableName(): String {
        return NFCBlackDBDao.getTableName()
    }


    fun list(): List<NFCBlackListDB> {
        return runBlocking {
            dao.list()
        }
    }

    fun findByMaxIdNfcBlackDB(): NFCBlackListDB? {
        return runBlocking {
            dao.findByMaxId().let {
                var blackListDBS = findAllByField("id", it)
                if (blackListDBS.size > 0) {
                    blackListDBS.get(0)
                } else null


            }


        }
    }


}