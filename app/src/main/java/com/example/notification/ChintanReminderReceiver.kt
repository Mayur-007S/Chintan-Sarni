package com.example.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ChintanReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        val action = intent?.action
        val settings = ReminderManager.getSettings(context)

        if (Intent.ACTION_BOOT_COMPLETED == action) {
            // Re-schedule alarm on device boot
            if (settings.enabled) {
                ReminderManager.scheduleDailyReminder(context, settings.hour, settings.minute)
            }
        } else if (ACTION_REMINDER == action || action == null) {
            // Alarm triggered - show reminder notification
            if (settings.enabled) {
                ReminderManager.showReminderNotification(context, isTest = false)
                // Schedule next day's reminder
                ReminderManager.scheduleDailyReminder(context, settings.hour, settings.minute)
            }
        }
    }

    companion object {
        const val ACTION_REMINDER = "com.example.notification.ACTION_CHINTAN_REMINDER"
    }
}
