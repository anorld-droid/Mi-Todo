package com.anorlddroid.mi_todo.data.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import java.util.concurrent.Executors


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
        private var instance: MiTodoDatabase? = null
        fun getDatabase(context: Context): MiTodoDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): MiTodoDatabase {
            Log.d("ROOMBUILD", "Building Room Database")
            return Room.databaseBuilder(
                context, MiTodoDatabase::class.java, "mi_todo_database"
            ).addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    Log.d("ONCREATE", "On create called")
                    super.onCreate(db)
                    Executors.newSingleThreadExecutor().execute {
                        getDatabase(context).settingsDao().insert(
                            DataGenerator.insertSettings()[0],
                            DataGenerator.insertSettings()[1]
                        )
                        val rowsReturned = getDatabase(context).categoriesDao().insertAll(
                            CategoriesInitialData.data()[0],
                            CategoriesInitialData.data()[1],
                            CategoriesInitialData.data()[2]
                        )
                        Log.d("ONCREATE", " Inserting data $rowsReturned")
                    }
                }
            }).build()
        }
    }
}
