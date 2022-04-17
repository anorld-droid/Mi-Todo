package com.anorlddroid.mi_todo.data.repository

import com.anorlddroid.mi_todo.data.database.CategoryEntity
import com.anorlddroid.mi_todo.data.database.MiTodoDatabase
import com.anorlddroid.mi_todo.data.database.SettingsEntity
import com.anorlddroid.mi_todo.data.database.TodoEntity
import kotlinx.coroutines.flow.distinctUntilChanged

class Repository(private val dbInstance: MiTodoDatabase) {
    fun getAllTodos() = dbInstance.TodosDao().getAllTodos().distinctUntilChanged()
    suspend fun insertTodo(entity: TodoEntity): Long = dbInstance.TodosDao().insert(entity)
    suspend fun deleteTodo(name: String) = dbInstance.TodosDao().delete(name)
    fun getAllCategories() = dbInstance.categoriesDao().getAllCategories().distinctUntilChanged()
    suspend fun insertCategory(name: CategoryEntity) = dbInstance.categoriesDao().insert(name)
    suspend fun getSetting(name: String) = dbInstance.settingsDao().getSetting(name)
    suspend fun insertSetting(entity: SettingsEntity) = dbInstance.settingsDao().insert(entity)
    suspend fun updateTodo(date: String, id: Int) = dbInstance.TodosDao().updateTodo(date, id)
    suspend fun completedTodo(completed: Boolean, id: Int) =
        dbInstance.TodosDao().completedTodo(completed, id)

}