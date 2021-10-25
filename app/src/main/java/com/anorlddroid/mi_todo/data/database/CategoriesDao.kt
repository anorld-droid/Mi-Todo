package com.anorlddroid.mi_todo.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow


/**
 * [Room] DAO for [Category] related operations.
 */
@Dao
abstract class CategoriesDao {
    @Query("SELECT id FROM categories WHERE name = :name")
    abstract suspend fun getCategoryId(name: String): Long?

    @Query("SELECT name FROM categories")
    abstract fun getAllCategories(): Flow<List<String>>

    /**
     * The following methods should really live in a base interface. Unfortunately the Kotlin
     * Compiler which we need to use for Compose doesn't work with that.
     * TODO: remove this once we move to a more recent Kotlin compiler
     */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(name: CategoryEntity): Long

    @Delete
    abstract suspend fun delete(entity: CategoryEntity)
}