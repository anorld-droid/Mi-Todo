package com.anorlddroid.mi_todo.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * [Room] DAO for [Category] related operations.
 */
@Dao
abstract class TodosDao {

    @Query("SELECT id, category, name, date, time, repeat, hide, `delete`, completed FROM todos")
    abstract fun getAllTodos(): Flow<List<TodoMinimal>>


    @Query(
        "SELECT * FROM todos WHERE category = :name AND todos.hide = :hide"
    )
    abstract fun getTodoByCategoryHide(name: String, hide: String): Flow<List<TodoMinimal>>

    @Query(
        "SELECT * FROM todos  WHERE category = :name "
    )
    abstract fun getAllTodoByCategory(name: String): Flow<List<TodoMinimal>>

    @Query("SELECT * FROM todos WHERE category = :meal")
    abstract fun getAllMeals(meal: String = "Meal"): Flow<List<TodoMinimal>>

    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT * FROM todos WHERE hide = :hide ")
    abstract fun getUnhiddenTodos(hide: String): Flow<List<TodoMinimal>>


    /**
     * The following methods should really live in a base interface. Unfortunately the Kotlin
     * Compiler which we need to use for Compose doesn't work with that.
     *
     */
    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun update(entity: TodoEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(entity: TodoEntity): Long

    @Query("DELETE FROM todos WHERE id= :id")
    abstract suspend fun delete(id: Int): Int

    @Query("UPDATE todos SET date = :date WHERE id = :id")
    abstract suspend fun updateTodo(date: String, id: Int): Int

    @Query("UPDATE todos SET completed = :completed WHERE id = :id")
    abstract suspend fun completedTodo(completed: Boolean = true, id: Int): Int

    @Query("UPDATE todos SET time = :time WHERE id = :id")
    abstract suspend fun snoozeTodo(time: String, id: Int): Int
}