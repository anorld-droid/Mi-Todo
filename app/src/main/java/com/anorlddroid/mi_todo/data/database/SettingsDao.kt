package com.anorlddroid.mi_todo.data.database

import androidx.room.*

@Dao
abstract class SettingsDao {
    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(vararg entity: SettingsEntity): List<Long>

    @Query("SELECT setting FROM settings WHERE name = :name")
    abstract suspend fun getSetting(name: String): String

    @Query("UPDATE settings SET setting = :setting WHERE name = :name")
    abstract suspend fun updateSetting(name: String, setting: String): Int
}