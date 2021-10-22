package com.anorlddroid.mi_todo.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * [Room] DAO for [Category] related operations.
 */
@Dao
abstract class TodosDao {
    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT * FROM todos")
    abstract fun getAllTodos(): Flow<List<TodoMinimal>>

    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT * FROM todos WHERE category_id = :id")
    abstract fun getTodoByCategory(id: Long): Flow<List<TodoMinimal>>

    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT * FROM todos WHERE hide = \"Off\" ")
    abstract fun getUnhiddenTodos(): Flow<List<TodoMinimal>>

    /**
     * The following methods should really live in a base interface. Unfortunately the Kotlin
     * Compiler which we need to use for Compose doesn't work with that.
     * TODO: remove this once we move to a more recent Kotlin compiler
     */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(entity: TodoEntity): Long

    @Query("DELETE FROM todos WHERE name= :name")
    abstract suspend fun delete(name: String): Int
}