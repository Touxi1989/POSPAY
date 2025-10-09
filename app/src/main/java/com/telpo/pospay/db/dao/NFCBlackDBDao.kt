package com.telpo.pospay.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.telpo.pospay.db.bean.CAPKDB
import com.telpo.pospay.db.bean.NFCBlackListDB

@Dao
interface NFCBlackDBDao : BaseDao<NFCBlackListDB> {

    companion object {
        // 定义一个方法来获取关联的实体类
        fun getTableName() = NFCBlackListDB.TABLE_NAME
    }

    @Query("SELECT * FROM nfc_black_List_table ")
    suspend fun list(): List<NFCBlackListDB>


    @Query("select case when max(id) is null then 0 else max(id) end as maxid from nfc_black_List_table ")
    suspend fun findByMaxId(): Int
}