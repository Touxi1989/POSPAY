package com.telpo.pospay.repository

import com.telpo.pospay.db.bean.DiaryEntry
import com.telpo.pospay.db.dao.DiaryDao
import kotlinx.coroutines.flow.Flow

class DiaryRepository(private val diaryDao: DiaryDao) {

    val allDiaries: Flow<List<DiaryEntry>> = diaryDao.getAllDiariesFlow()

    suspend fun addDiary(entry: DiaryEntry) {
        diaryDao.insertDiary(entry)
    }

    suspend fun deleteDiary(entry: DiaryEntry) {
        diaryDao.deleteDiary(entry)
    }
}