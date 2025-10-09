package com.telpo.pospay.db.bean

import androidx.room.Entity
import androidx.room.PrimaryKey

//使用 @Entity 注解定义数据库表：
@Entity(tableName = UserEntry.TABLE_NAME) // 指定表名
data class UserEntry(

    @PrimaryKey(autoGenerate = true) val id: Int = 0, // 主键，自动生成
    val userId: String,
    val userPwd: String,
) {
    companion object {
        const val TABLE_NAME = "user_entries"
    }
}