package com.telpo.pospay.db.bean

import androidx.room.Entity
import androidx.room.PrimaryKey
//使用 @Entity 注解定义数据库表：
@Entity(tableName = "diary_entries") // 指定表名
data class DiaryEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // 主键，自动生成
    val title: String,
    val content: String,
    val date: Long, // 以时间戳形式存储日期
    val weather: String? = null
)