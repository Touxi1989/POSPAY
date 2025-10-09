package com.telpo.pospay.db.bean

import androidx.room.Entity
import androidx.room.PrimaryKey

//使用 @Entity 注解定义数据库表：
@Entity(tableName = DiaryEntry.TABLE_NAME) // 指定表名
class DiaryEntry : BaseBean() {
    companion object {
        const val TABLE_NAME = "diary_entries"
    }



    @PrimaryKey(autoGenerate = true)
    var id: Int = 0 // 主键，自动生成
    var title: String? = null
    var content: String? = null
    var date: Long? = 0 // 以时间戳形式存储日期
    var weather: String? = null


}



