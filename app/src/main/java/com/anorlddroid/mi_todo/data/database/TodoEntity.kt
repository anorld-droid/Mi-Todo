package com.anorlddroid.mi_todo.data.database

import androidx.compose.runtime.Immutable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "todos",
)
@Immutable
data class TodoEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "category") var category: String,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "date") var date: String? = null,
    @ColumnInfo(name = "time") var time: String? = null,
    @ColumnInfo(name = "repeat") var repeat: String,
    @ColumnInfo(name = "hide") var hide: Boolean,
    @ColumnInfo(name = "delete") var delete: Boolean,
    @ColumnInfo(name = "completed") var completed: Boolean
)

data class TodoMinimal(
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "category") var category: String,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "date") var date: String,
    @ColumnInfo(name = "time") var time: String,
    @ColumnInfo(name = "repeat") var repeat: String,
    @ColumnInfo(name = "hide") var hide: Boolean,
    @ColumnInfo(name = "delete") var delete: Boolean,
    @ColumnInfo(name = "completed") var completed: Boolean

)