package com.telpo.pospay.db.dao

import android.text.TextUtils
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.RawQuery
import androidx.room.Update
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQueryBuilder
import com.telpo.pospay.db.bean.BaseBean


@Dao
interface BaseDao<T : BaseBean> {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun create(t: T)

    @Delete
    suspend fun deleteList(t: List<T>)


    @Update
    suspend fun update(t: T)

    @RawQuery
    suspend fun getFieldsDynamic(query: SupportSQLiteQuery): List<T>


}


// 扩展函数，用于构建动态查询
fun <T : BaseBean> BaseDao<T>.buildDynamicQuery(
    tableName: String,
    conditions: Map<String, Any?>,
    columns: Array<String> = arrayOf("*")  //要查询输出的列
): SimpleSQLiteQuery {
    val qb = SupportSQLiteQueryBuilder.builder(tableName).columns(columns)
    val args = mutableListOf<Any>()

    val whereClauses = mutableListOf<String>()

    conditions.forEach { (key, value) ->
        if (value != null) {
            whereClauses.add("$key = ?")
            args.add(value)
        }
    }

    if (whereClauses.isNotEmpty()) {
        qb.selection(TextUtils.join(" AND ", whereClauses), args.toTypedArray())
    }

    return qb.create() as SimpleSQLiteQuery
}



