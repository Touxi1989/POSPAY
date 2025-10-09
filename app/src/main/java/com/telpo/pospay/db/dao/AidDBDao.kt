package com.telpo.pospay.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.telpo.pospay.db.bean.AIDDB
import com.telpo.pospay.db.bean.CAPKDB

@Dao
interface AidDBDao : BaseDao<AIDDB> {

    companion object {
        // 定义一个方法来获取关联的实体类
        fun getTableName() = AIDDB.TABLE_NAME
    }

    @Query("SELECT * FROM table_AID ")
    suspend fun list(): List<AIDDB>


}