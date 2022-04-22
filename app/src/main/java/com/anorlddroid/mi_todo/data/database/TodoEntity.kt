package com.anorlddroid.mi_todo.data.database

import androidx.compose.runtime.Immutable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

enum class MealType {
    BREAKFAST, LUNCH, SUPPER
}

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
    @ColumnInfo(name = "completed") var completed: Boolean,
    @ColumnInfo(name = "type") var type: MealType? = null,

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
    @ColumnInfo(name = "completed") var completed: Boolean,
    @ColumnInfo(name = "type") var type: MealType?

)

object TodoConverter {
    fun toMininal(entity: TodoEntity) = entity.date?.let {
        entity.time?.let { it1 ->
            TodoMinimal(
                id = entity.id,
                category = entity.category,
                name = entity.name,
                date = it,
                time = it1,
                repeat = entity.repeat,
                hide = entity.hide,
                delete = entity.delete,
                completed = entity.completed,
                type = entity.type
            )
        }
    }

    fun toEntity(todo: TodoMinimal) = TodoEntity(
        id = todo.id,
        category = todo.category,
        name = todo.name,
        date = todo.date,
        time = todo.time,
        repeat = todo.repeat,
        hide = todo.hide,
        delete = todo.delete,
        completed = todo.completed,
        type = todo.type
    )
}