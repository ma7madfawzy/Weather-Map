package com.app.weather.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.app.weather.R
import com.app.weather.presentation.main.MainActivity

object WidgetUpdater {
    fun updateViews(
        context: Context, appWidgetManager: AppWidgetManager,
        appWidgetId: Int, location: String? = null, temp: String? = null
    ) {
        val views = RemoteViews(context.packageName, R.layout.weather_app_widget)
        views.setOnClickPendingIntent(R.id.rootView, getPendingIntent(context))
        location?.let { views.setTextViewText(R.id.textViewLocation, it) }
        temp?.let { views.setTextViewText(R.id.textViewTemperature, it) }
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    private fun getPendingIntent(context: Context): PendingIntent {
        val intent = Intent(context, MainActivity::class.java)
        return PendingIntent.getActivity(context, 220, intent, 0)
    }
}