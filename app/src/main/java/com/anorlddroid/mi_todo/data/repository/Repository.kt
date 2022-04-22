package com.anorlddroid.mi_todo.data.repository

import android.content.Context
import android.widget.Toast
import com.anorlddroid.mi_todo.data.database.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.withContext

class Repository(private val dbInstance: MiTodoDatabase) {
    fun getAllTodos() = dbInstance.TodosDao().getAllTodos().distinctUntilChanged()
    suspend fun insertTodo(entity: TodoEntity): Long = dbInstance.TodosDao().insert(entity)
    suspend fun deleteTodo(id: Int) = dbInstance.TodosDao().delete(id)
    fun getAllCategories() = dbInstance.categoriesDao().getAllCategories().distinctUntilChanged()
    suspend fun insertCategory(name: CategoryEntity) = dbInstance.categoriesDao().insert(name)
    suspend fun updateTodo(date: String, id: Int) = dbInstance.TodosDao().updateTodo(date, id)
    suspend fun completedTodo(completed: Boolean, id: Int) =
        dbInstance.TodosDao().completedTodo(completed, id)

    suspend fun snoozeTodo(todoMinimal: TodoMinimal, snooze: Long, context: Context) {
        val time = DateTimeTypeConverters.toLocalTime(todoMinimal.time)
        val newTime = DateTimeTypeConverters.fromLocalTime(time.plusMinutes(snooze))
        withContext(Dispatchers.IO) {
            dbInstance.TodosDao().snoozeTodo(time = newTime, id = todoMinimal.id)
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Task snoozed for $snooze minutes", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    fun getAllMeals() = dbInstance.TodosDao().getAllMeals().distinctUntilChanged()

    fun getAllTodosByCategory(category: String) =
        dbInstance.TodosDao().getAllTodoByCategory(category).distinctUntilChanged()

}

