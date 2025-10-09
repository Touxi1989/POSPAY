package com.telpo.pospay.db.repository

import com.telpo.pospay.db.bean.UserEntry
import com.telpo.pospay.db.dao.UserDao
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao) {

    val allUsers: Flow<List<UserEntry>> = userDao.getAllUsersFlow()
    val allUserIds: Flow<List<String>> = userDao.getAllUserIdsFlow()

    suspend fun addUser(entry: UserEntry) {
        userDao.insertUser(entry)
    }

    suspend fun deleteUser(entry: UserEntry) {
        userDao.deleteUser(entry)
    }
    suspend fun getUserById(userId: String): UserEntry? {
       return userDao.getUserById(userId)
    }



}