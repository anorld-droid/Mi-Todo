package com.anorlddroid.mi_todo.ui.utils

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.anorlddroid.mi_todo.MainActivity
import com.anorlddroid.mi_todo.R
import com.anorlddroid.mi_todo.data.database.DateTimeTypeConverters
import com.anorlddroid.mi_todo.ui.CHANNEL_ID
import java.time.LocalDateTime

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        if (p0 != null) {
            if (p1 != null) {
                val date = p1.getStringExtra("Date")?.let { DateTimeTypeConverters.toLocalDate(it) }
                val time = p1.getStringExtra("Time")?.let { DateTimeTypeConverters.toLocalTime(it) }
                if (LocalDateTime.now() <= LocalDateTime.of(date, time)) {
                    p1.getStringExtra("Category")?.let { category ->
                        p1.getStringExtra("Todo")?.let { todo ->
                            notificationBuilder(
                                context = p0,
                                todo = todo,
                                title = "You have an upcoming ${category.lowercase()} task @$time",
                                notificationid = p1.getIntExtra("NotificationID", 2)
                            )
                        }
                    }
                } else if (LocalDateTime.now().plusMinutes(10) <= LocalDateTime.of(date, time)) {
                    p1.getStringExtra("Category")?.let { category ->
                        p1.getStringExtra("Todo")?.let { todo ->
                            notificationBuilder(
                                context = p0,
                                todo = todo,
                                title = category,
                                notificationid = p1.getIntExtra("NotificationID", 2)
                            )
                        }
                    }
                }
            }
        }
    }
}

fun notificationBuilder(context: Context, todo: String, title: String, notificationid: Int) {
    val intent = Intent(context, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    val alarmSound: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
    val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
    val builder = NotificationCompat.Builder(context, CHANNEL_ID)
        .setColor(context.resources.getColor(R.color.black))
        .setSmallIcon(R.drawable.mi_todo_icon)
        .setContentTitle(title)
        .setContentText(todo).setSound(alarmSound)
        .setStyle(NotificationCompat.BigTextStyle().bigText(todo))
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
        .setContentIntent(pendingIntent)
        .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
        .setAutoCancel(true)
    with(NotificationManagerCompat.from(context)) {
        //unique id for each notification , use primary id from the db for each task
        notify(notificationid, builder.build())
    }
}