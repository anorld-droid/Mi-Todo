package com.anorlddroid.mi_todo.data.database

import androidx.compose.runtime.Immutable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "categories",
    indices = [
        Index("name", unique = true)
    ]
)
@Immutable
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Long = 0,
    @ColumnInfo(name = "name") var name: String
)

class CategoriesInitialData {
    companion object {
        fun data(): List<CategoryEntity> = listOf(
            CategoryEntity(name = "All"),
        )
    }
}