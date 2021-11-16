package com.anorlddroid.mi_todo.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class WakeUpAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        if (p1?.action.equals("android.intent.action.BOOT_COMPLETED")){
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
}