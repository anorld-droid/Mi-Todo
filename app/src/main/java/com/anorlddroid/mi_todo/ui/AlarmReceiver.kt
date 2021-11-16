package com.anorlddroid.mi_todo.ui

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.Intent.getIntent
import android.media.RingtoneManager
import android.net.Uri
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.anorlddroid.mi_todo.R
import com.anorlddroid.mi_todo.SplashScreenActivity

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        if (p0 != null) {
            p1?.getStringExtra("Todo")?.let { todo ->
                p1.getStringExtra("Category")?.let { category ->
                    notificationBuilder(
                        context = p0,
                        category = category,
                        todo = todo,
                        notificationid = p1.getIntExtra("NotificationID", 2)
                    )
                }
            }
        }
    }
}

fun notificationBuilder(context: Context, category: String, todo: String, notificationid: Int) {
    val intent = Intent(context, SplashScreenActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    val alarmSound : Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
    val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
    val builder = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.mi_todo_icon)
        .setContentTitle("Upcoming Task")
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