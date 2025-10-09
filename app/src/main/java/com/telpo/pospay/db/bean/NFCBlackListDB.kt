package com.telpo.pospay.db.bean

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = NFCBlackListDB.TABLE_NAME) // 指定表名
class NFCBlackListDB : BaseBean() {

    companion object {
        const val TABLE_NAME = "nfc_black_List_table"
    }


    var blackIndex: String? = null

    //id从1开始自动增长
    @PrimaryKey
    var id: Int = 0

    var BlackCardLen: String? = null

    var BlackCardID: String? = null

    @Ignore
    var maxid: Int = 0


}