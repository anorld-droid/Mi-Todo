package com.anorlddroid.mi_todo

import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.core.view.WindowCompat
import com.anorlddroid.mi_todo.data.database.MiTodoDatabase

const val DATABASE_CREATED: String = "Database created"
class MainActivity : ComponentActivity() {
    @ExperimentalMaterialApi
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        val context = this
        PreferenceManager.getDefaultSharedPreferences(this).apply {
            if (!getBoolean(DATABASE_CREATED, false)) {
                val dbInstance = MiTodoDatabase.getDatabase(context)
                dbInstance.openHelper.writableDatabase
                PreferenceManager.getDefaultSharedPreferences(context).edit().apply {
                    putBoolean(DATABASE_CREATED, true).apply()
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
