package com.app.weather.widget

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.app.weather.widget.WeatherWidgetProvider.Companion.ACTION_AUTO_UPDATE
import java.util.*

class WeatherWidgetAlarmHandler(private val context: Context) {
    private val alarmId = 94
    private val intervalMillis = 1800000//30 mins

    @SuppressLint("ShortAlarm")
    fun startAlarm() {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MILLISECOND, intervalMillis)
        val alarmIntent = Intent(context, WeatherWidgetProvider::class.java)
        alarmIntent.action = ACTION_AUTO_UPDATE
        val pendingIntent: PendingIntent = PendingIntent.getBroadcast(context, alarmId,
            alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT
        )
        val alarmManager: AlarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        // RTC does not wake the device up
        alarmManager.setRepeating(AlarmManager.RTC, calendar.timeInMillis,
            intervalMillis.toLong(), pendingIntent
        )
    }

    fun stopAlarm() {
        val alarmIntent = Intent(ACTION_AUTO_UPDATE)
        val pendingIntent: PendingIntent = PendingIntent.getBroadcast(context,
            alarmId, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT
        )
        val alarmManager: AlarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }

}