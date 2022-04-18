package com.anorlddroid.mi_todo.ui.utils

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.anorlddroid.mi_todo.data.database.DateTimeTypeConverters
import com.anorlddroid.mi_todo.data.database.MiTodoDatabase
import com.anorlddroid.mi_todo.data.repository.Repository
import com.anorlddroid.mi_todo.ui.setAlarm
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MiTodoWorker(appContext: Context, workerParameters: WorkerParameters) :
    Worker(
        appContext,
        workerParameters
    ) {
    val context = appContext

    @OptIn(DelicateCoroutinesApi::class)
    override fun doWork(): Result {
        val dbInstance = MiTodoDatabase.getDatabase(context)
        val repository = Repository(dbInstance)
        GlobalScope.launch(Dispatchers.IO) {
            val todos = repository.getAllTodos()
            todos.collect { todoList ->
                todoList.forEach { todo ->
                    setAlarm(
                        date = DateTimeTypeConverters.toLocalDate(todo.date),
                        time = DateTimeTypeConverters.toLocalTime(todo.time),
                        context = context,
                        category = todo.category,
                        todo = todo.name,
                        notificationID = todo.id
                    )
                }
            }
        }
        return Result.success()
    }

}