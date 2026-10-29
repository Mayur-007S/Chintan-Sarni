package com.example.notification

import android.Manifest
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.MainActivity
import com.example.localization.AppLanguage
import java.util.Calendar

data class ReminderSettings(
    val enabled: Boolean = true,
    val hour: Int = 21,    // 9:00 PM default
    val minute: Int = 0
) {
    val formattedTime: String
        get() {
            val period = if (hour >= 12) "PM" else "AM"
            val displayHour = when {
                hour == 0 -> 12
                hour > 12 -> hour - 12
                else -> hour
            }
            return "%d:%02d %s".format(displayHour, minute, period)
        }
}

object ReminderManager {

    const val CHANNEL_ID = "chintan_sarani_daily_reminders"
    private const val PREFS_NAME = "chintan_reminder_preferences"
    private const val KEY_ENABLED = "key_reminder_enabled"
    private const val KEY_HOUR = "key_reminder_hour"
    private const val KEY_MINUTE = "key_reminder_minute"
    const val NOTIFICATION_ID = 1008
    const val REQUEST_CODE_ALARM = 2001

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "चिंतन सारणी स्मरणपत्र (Chintan Sarani Reminder)"
            val descriptionText = "दैनिक साधना व चिंतन सारणी भरण्यासाठी आठवण"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
                enableVibration(true)
            }
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun getSettings(context: Context): ReminderSettings {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return ReminderSettings(
            enabled = prefs.getBoolean(KEY_ENABLED, true),
            hour = prefs.getInt(KEY_HOUR, 21),
            minute = prefs.getInt(KEY_MINUTE, 0)
        )
    }

    fun saveSettings(context: Context, enabled: Boolean, hour: Int, minute: Int) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putBoolean(KEY_ENABLED, enabled)
            .putInt(KEY_HOUR, hour)
            .putInt(KEY_MINUTE, minute)
            .apply()

        if (enabled) {
            scheduleDailyReminder(context, hour, minute)
        } else {
            cancelDailyReminder(context)
        }
    }

    fun scheduleDailyReminder(context: Context, hour: Int, minute: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        val intent = Intent(context, ChintanReminderReceiver::class.java).apply {
            action = ChintanReminderReceiver.ACTION_REMINDER
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            REQUEST_CODE_ALARM,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            // If time has already passed today, schedule for tomorrow
            if (timeInMillis <= System.currentTimeMillis()) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }

        try {
            alarmManager.setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        } catch (_: SecurityException) {
            // Fallback for strict alarm restrictions
            alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        }
    }

    fun cancelDailyReminder(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        val intent = Intent(context, ChintanReminderReceiver::class.java).apply {
            action = ChintanReminderReceiver.ACTION_REMINDER
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            REQUEST_CODE_ALARM,
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent)
            pendingIntent.cancel()
        }
    }

    fun showReminderNotification(
        context: Context,
        isTest: Boolean = false,
        language: AppLanguage = AppLanguage.MARATHI
    ) {
        createNotificationChannel(context)

        // Verify POST_NOTIFICATIONS permission on Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
        }

        val launchIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val contentPendingIntent = PendingIntent.getActivity(
            context,
            0,
            launchIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val title = if (isTest) {
            if (language == AppLanguage.MARATHI) "🔔 चाचणी स्मरणपत्र ॐ" else "🔔 Test Reminder ॐ"
        } else {
            if (language == AppLanguage.MARATHI) "चिंतन सारणी भरण्याची वेळ झाली आहे ॐ" else "Time to complete Chintan Sarani ॐ"
        }

        val content = if (isTest) {
            if (language == AppLanguage.MARATHI)
                "हे स्मरणपत्र बरोबर काम करत आहे! आपण निवडलेल्या वेळेवर दररोज असेच स्मरणपत्र येईल."
            else
                "Reminder is working properly! You will receive daily reminders at your configured time."
        } else {
            if (language == AppLanguage.MARATHI)
                "आजची साधना, नामजप व चुकांची नोंद करून चिंतन सारणी पूर्ण करा."
            else
                "Log today's spiritual practices, Naamjap, and reflections to complete your Chintan Sarani."
        }

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_popup_reminder)
            .setContentTitle(title)
            .setContentText(content)
            .setStyle(NotificationCompat.BigTextStyle().bigText(content))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setContentIntent(contentPendingIntent)
            .setAutoCancel(true)

        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, builder.build())
    }
}
