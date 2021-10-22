package com.anorlddroid.mi_todo.data.database

import androidx.compose.runtime.Immutable
import androidx.room.*

@Entity(
    tableName = "todos",
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["category_id"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("category_id")
    ],
)
@Immutable
data class TodoEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Long = 0,
    @ColumnInfo(name = "category_id") var categoryId: Long,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "date") var date: String? = null,
    @ColumnInfo(name = "time") var time: String? = null,
    @ColumnInfo(name = "repeat") var repeat: String,
    @ColumnInfo(name = "hide") var hide: Boolean,
    @ColumnInfo(name = "delete") var delete: Boolean
)

data class TodoMinimal(
    val name: String,
    val date: String?,
    val time: String?,
    val repeat: String,
    var hide: Boolean,
    val delete: Boolean
)