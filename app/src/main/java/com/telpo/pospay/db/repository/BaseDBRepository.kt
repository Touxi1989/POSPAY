package com.telpo.pospay.db.repository

import com.telpo.pospay.db.bean.BaseBean
import com.telpo.pospay.db.dao.BaseDao
import com.telpo.pospay.db.dao.buildDynamicQuery
import kotlinx.coroutines.runBlocking

open abstract class BaseDBRepository<T : BaseBean, D : BaseDao<T>>(internal val dao: D) {
    abstract fun getTableName(): String;

    fun getDao(): D {
        return dao;
    }

    fun findAllByField(fieldName: String, value: Any): List<T> {
        return runBlocking {
            val conditions = mapOf(
                fieldName to value,
            )
            val query = dao.buildDynamicQuery(
                tableName = getTableName(),
                conditions = conditions
            )
            dao.getFieldsDynamic(query)
        }


    }

    fun deleteList(list: List<T>) {
        return runBlocking {
            dao.deleteList(list)
        }
    }

    fun create(db: T) {
        return runBlocking {
            dao.create(db)
        }
    }

    fun update(db: T) {
        return runBlocking {
            dao.update(db)
        }
    }

}