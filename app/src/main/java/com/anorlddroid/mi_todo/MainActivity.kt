package com.anorlddroid.mi_todo

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import com.anorlddroid.mi_todo.data.database.CategoriesInitialData
import com.anorlddroid.mi_todo.data.database.DataGenerator
import com.anorlddroid.mi_todo.data.database.MiTodoDatabase
import com.anorlddroid.mi_todo.ui.CHANNEL_ID
import kotlinx.coroutines.launch

const val DATABASE_CREATED: String = "Database created"
class MainActivity : ComponentActivity() {
    @ExperimentalAnimationApi
    @ExperimentalMaterialApi
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        val context = this
        createNotificationChannel()
        lifecycleScope.launch {
            val dbInstance = MiTodoDatabase.getDatabase(context)
            PreferenceManager.getDefaultSharedPreferences(context).apply {
                if (!getBoolean(DATABASE_CREATED, false)) {
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
    private fun createNotificationChannel(){
        //notification channel on API 26+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            //register the channel with the system
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
