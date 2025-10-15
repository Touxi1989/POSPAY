package com.telpo.pospay.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.telpo.pospay.db.bean.DiaryEntry
import kotlinx.coroutines.flow.Flow

//DAO 负责数据库的 CRUD 操作：
@Dao
interface DiaryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDiary(entry: DiaryEntry)

    @Update
    suspend fun updateDiary(entry: DiaryEntry)

    @Delete
    suspend fun deleteDiary(entry: DiaryEntry)

    @Query("SELECT * FROM diary_entries ORDER BY date DESC")
    fun getAllDiaries(): List<DiaryEntry> // 直接返回列表（不推荐，建议使用 Flow）

    @Query("SELECT * FROM diary_entries WHERE id = :id")
    suspend fun getDiaryById(id: Int): DiaryEntry?

    @Query("SELECT * FROM diary_entries WHERE title LIKE '%' || :query || '%'")
    fun searchDiaries(query: String): List<DiaryEntry>

    @Query("SELECT * FROM diary_entries ORDER BY date DESC")
    fun getAllDiariesFlow(): Flow<List<DiaryEntry>> // 使用 Flow 以支持实时监听
}
