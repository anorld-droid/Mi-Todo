package com.anorlddroid.mi_todo.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


/**
 * The [RoomDatabase] we use in this app.
 */
@Database(
    entities = [
        CategoryEntity::class,
        TodoEntity::class,
        SettingsEntity::class,
    ],
    version = 1,
    exportSchema = false
)
abstract class MiTodoDatabase : RoomDatabase() {
    abstract fun categoriesDao(): CategoriesDao
    abstract fun TodosDao(): TodosDao
    abstract fun settingsDao(): SettingsDao

    companion object {
        @Volatile
        private var INSTANCE: MiTodoDatabase? = null
        fun getDatabase(context: Context): MiTodoDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MiTodoDatabase::class.java,
                    "mi_todo_db"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
