package com.anorlddroid.mi_todo

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import com.anorlddroid.mi_todo.data.database.CategoriesInitialData
import com.anorlddroid.mi_todo.data.database.DataGenerator
import com.anorlddroid.mi_todo.data.database.MiTodoDatabase
import kotlinx.coroutines.launch

const val DATABASE_CREATED: String = "Database created"
class MainActivity : ComponentActivity() {
    @ExperimentalMaterialApi
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        val context = this
        lifecycleScope.launch {
            PreferenceManager.getDefaultSharedPreferences(context).apply {
                if (!getBoolean(DATABASE_CREATED, false)) {
                    val dbInstance = MiTodoDatabase.getDatabase(context)
                    dbInstance.settingsDao().insert(
                        DataGenerator.insertSettings()[0],
                        DataGenerator.insertSettings()[1]
                    )
                    dbInstance.categoriesDao().insert(
                        CategoriesInitialData.data()[0]
                    )
                    PreferenceManager.getDefaultSharedPreferences(context).edit().apply {
                        putBoolean(DATABASE_CREATED, true).apply()
                    }
                }
            }
        }
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            MiTodoApp()
        }
    }
}
