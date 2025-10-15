package com.telpo.pospay.db.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.telpo.pospay.db.bean.DiaryEntry
import com.telpo.pospay.db.dao.DiaryDao

//使用 @Database 注解创建 Room 数据库：
// entities: 一个数组，列出数据库中所有的实体类（即表）。这些实体类必须用 @Entity 注解标记。
//version: 数据库的版本号。当数据库结构发生变化时，需要递增此版本号，并提供相应的迁移策略。
//exportSchema (可选): 布尔值，默认为 false。如果设置为 true，Room 会在编译时生成数据库模式的 JSON 文件，便于版本控制和调试。
@Database(entities = [DiaryEntry::class], version = 1, exportSchema = false)
abstract class DiaryDatabase : RoomDatabase() {
    abstract fun diaryDao(): DiaryDao

    companion object {
        @Volatile  //@Volatile 确保多个线程能正确访问 INSTANCE
        private var INSTANCE: DiaryDatabase? = null

        fun getDatabase(context: Context): DiaryDatabase {
            return INSTANCE ?: synchronized(this) {  //synchronized(this) 确保数据库实例的单例模式
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DiaryDatabase::class.java,
                    "diary_database.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
