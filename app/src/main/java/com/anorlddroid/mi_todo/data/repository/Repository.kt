package com.anorlddroid.mi_todo.data.repository

import com.anorlddroid.mi_todo.data.database.CategoryEntity
import com.anorlddroid.mi_todo.data.database.MiTodoDatabase
import com.anorlddroid.mi_todo.data.database.TodoEntity

class Repository(private val dbInstance: MiTodoDatabase) {
    fun getAllTodos() = dbInstance.TodosDao().getAllTodos()
    suspend fun insertTodo(entity: TodoEntity) = dbInstance.TodosDao().insert(entity)
    suspend fun deleteTodo(name: String) = dbInstance.TodosDao().delete(name)
    suspend fun getCategoryID(name: String) = dbInstance.categoriesDao().getCategoryId(name)
    fun getAllCategories() = dbInstance.categoriesDao().getAllCategories()
    fun getTodoByCategory(id: Long) = dbInstance.TodosDao().getTodoByCategory(id)
    suspend fun insertCategory(name: CategoryEntity) = dbInstance.categoriesDao().insert(name)
    suspend fun getSetting(name: String) = dbInstance.settingsDao().getSetting(name)
    suspend fun updateSetting(name: String, setting: String) =
        dbInstance.settingsDao().updateSetting(name, setting)

    fun getUnHiddenTodos() = dbInstance.TodosDao().getUnhiddenTodos()
}