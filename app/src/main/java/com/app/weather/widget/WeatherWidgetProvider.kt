package com.app.weather.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.app.weather.R
import com.app.weather.presentation.main.MainActivity
import com.app.weather.utils.extensions.logE
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


/**
 * Implementation of App Widget functionality.
 */
@AndroidEntryPoint
class WeatherWidgetProvider : AppWidgetProvider() {
    companion object{
        const val ACTION_AUTO_UPDATE = "AUTO_UPDATE"
    }
    @Inject
    lateinit var viewModel: WeatherWidgetViewModel

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        if (ACTION_AUTO_UPDATE == intent?.action && context != null) {
            logE("onReceive")
            val manager = AppWidgetManager.getInstance(context)
            val ids = manager.getAppWidgetIds(ComponentName(context, WeatherWidgetProvider::class.java))
            onUpdate(context, manager, ids)
        }
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        logE("onUpdate")
        for (appWidgetId in appWidgetIds)
            viewModel.requestLocationData {
                updateViews(
                    context, appWidgetManager,
                    appWidgetId, it.name, it.main?.getTempString()
                )
            }

    }

    private fun updateViews(
        context: Context, appWidgetManager: AppWidgetManager,
        appWidgetId: Int, location: String? = null, temp: String? = null
    ) {
        val views = RemoteViews(context.packageName, R.layout.weather_app_widget)
        views.setOnClickPendingIntent(R.id.rootView, getPendingIntent(context))
        location?.let { views.setTextViewText(R.id.textViewLocation, it) }
        temp?.let { views.setTextViewText(R.id.textViewTemperature, it) }
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    private fun getPendingIntent(context: Context) = PendingIntent.getActivity(
        context, 220,
        Intent(context, MainActivity::class.java), 0
    )

    override fun onEnabled(context: Context) {// start alarm
        // start alarm
        logE("onEnabled")
        val appWidgetAlarm = WeatherWidgetAlarmHandler(context.applicationContext)
        appWidgetAlarm.startAlarm()
    }

    override fun onDisabled(context: Context) {
        // stop alarm only if all widgets have been disabled
        logE("onDisabled")
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val thisAppWidgetComponentName =
            ComponentName(context.packageName, javaClass.name)
        val appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidgetComponentName)
        if (appWidgetIds.isEmpty()) {
            // stop alarm
            val appWidgetAlarm = WeatherWidgetAlarmHandler(context.applicationContext)
            appWidgetAlarm.stopAlarm()
        }
    }
}


