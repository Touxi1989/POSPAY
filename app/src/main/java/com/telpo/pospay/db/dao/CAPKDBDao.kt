package com.telpo.pospay.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.telpo.pospay.db.bean.CAPKDB

@Dao
interface CAPKDBDao : BaseDao<CAPKDB> {

    companion object {
        // 定义一个方法来获取关联的实体类
        fun getTableName() = CAPKDB.TABLE_NAME
    }

    @Query("SELECT * FROM capk_table ")
    suspend fun list(): List<CAPKDB>

}