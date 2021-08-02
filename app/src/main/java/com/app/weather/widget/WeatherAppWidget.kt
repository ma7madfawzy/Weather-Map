package com.app.weather.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetManager.ACTION_APPWIDGET_UPDATE
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import com.app.weather.utils.extensions.logE
import com.app.weather.utils.extensions.updateWidget
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Implementation of App Widget functionality.
 */
@AndroidEntryPoint
class WeatherAppWidget : AppWidgetProvider() {
    @Inject
    lateinit var viewModel: WeatherAppWidgetViewModel
    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        if (ACTION_APPWIDGET_UPDATE == intent?.action)
            context?.updateWidget()
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        logE("onUpdate")
        for (appWidgetId in appWidgetIds) {
            viewModel.requestLocationData {
                WidgetUpdater.updateViews(
                    context, appWidgetManager, appWidgetId,
                    it.name,
                    it.main?.getTempString()
                )
            }
        }
    }

    override fun onEnabled(context: Context) {}

    override fun onDisabled(context: Context) {}
}


