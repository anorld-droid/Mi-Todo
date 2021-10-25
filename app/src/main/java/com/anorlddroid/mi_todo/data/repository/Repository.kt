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
    suspend fun getCategoryID(name: String) = dbInstance.categoriesDao().getCategoryId(name)
    fun getAllCategories() = dbInstance.categoriesDao().getAllCategories().distinctUntilChanged()
    fun getAllTodoByCategory(name: String) =
        dbInstance.TodosDao().getAllTodoByCategory(name).distinctUntilChanged()

    fun getTodoByCategory(
        name: String,
        hide: String
    ) = dbInstance.TodosDao().getTodoByCategory(name, hide).distinctUntilChanged()

    suspend fun insertCategory(name: CategoryEntity) = dbInstance.categoriesDao().insert(name)
    suspend fun getSetting(name: String) = dbInstance.settingsDao().getSetting(name)
    suspend fun updateSetting(name: String, setting: String) =
        dbInstance.settingsDao().updateSetting(name, setting)

    suspend fun insertSetting(entity: SettingsEntity) = dbInstance.settingsDao().insert(entity)

    fun getUnHiddenTodos(hide: String) =
        dbInstance.TodosDao().getUnhiddenTodos(hide).distinctUntilChanged()

}