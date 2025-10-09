package com.telpo.pospay.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.telpo.pospay.db.bean.UserEntry
import kotlinx.coroutines.flow.Flow

//DAO 负责数据库的 CRUD 操作：
@Dao
interface UserDao {

    companion object {
        // 定义一个方法来获取关联的实体类
        fun getTableName() = UserEntry.TABLE_NAME
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(entry: UserEntry)

    @Update
    suspend fun updateUser(entry: UserEntry)

    @Delete
    suspend fun deleteUser(entry: UserEntry)

    @Query("SELECT * FROM user_entries ORDER BY userId DESC")
    fun getAllUsers(): List<UserEntry> // 直接返回列表（不推荐，建议使用 Flow）

    @Query("SELECT * FROM user_entries WHERE userId = :userId")
    suspend fun getUserById(userId: String): UserEntry?


    @Query("SELECT * FROM user_entries ORDER BY userId DESC")
    fun getAllUsersFlow(): Flow<List<UserEntry>> // 使用 Flow 以支持实时监听


    @Query("SELECT userId FROM user_entries ORDER BY userId DESC")
    fun getAllUserIdsFlow(): Flow<List<String>> // 使用 Flow 以支持实时监听


}
