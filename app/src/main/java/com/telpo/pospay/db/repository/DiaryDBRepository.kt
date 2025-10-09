package com.telpo.pospay.db.repository

import com.telpo.pospay.db.bean.DiaryEntry
import com.telpo.pospay.db.dao.DiaryDao
import kotlinx.coroutines.flow.Flow

class DiaryDBRepository(dao: DiaryDao) :
    BaseDBRepository<DiaryEntry, DiaryDao>(dao) {

    val allDiaries: Flow<List<DiaryEntry>> = dao.getAllDiariesFlow()

    suspend fun addDiary(entry: DiaryEntry) {
        dao.insertDiary(entry)
    }

    suspend fun deleteDiary(entry: DiaryEntry) {
        dao.deleteDiary(entry)

    }


    override fun getTableName(): String {
        return DiaryDao.getTableName()
    }



}