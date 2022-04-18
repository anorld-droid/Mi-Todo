package com.anorlddroid.mi_todo.data.repository

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

    suspend fun snoozeTodo(todoMinimal: TodoMinimal, snooze: Long) =
        withContext(Dispatchers.Default) {
            val time = DateTimeTypeConverters.toLocalTime(todoMinimal.time)
            time.plusMinutes(snooze)
            val newTime = DateTimeTypeConverters.fromLocalTime(time)
            dbInstance.TodosDao().snoozeTodo(time = newTime, id = todoMinimal.id)
        }

    suspend fun update(todoMinimal: TodoMinimal) =
        withContext(Dispatchers.IO) {
            dbInstance.TodosDao().update(TodoConverter.toEntity(todoMinimal))
        }

    suspend fun getAllMeals() = withContext(Dispatchers.IO) {
        dbInstance.TodosDao().getAllMeals().distinctUntilChanged()
    }

    fun getAllTodosByCategory(category: String) =
        dbInstance.TodosDao().getAllTodoByCategory(category).distinctUntilChanged()

}

