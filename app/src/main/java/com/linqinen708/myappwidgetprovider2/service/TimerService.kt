package com.linqinen708.myappwidgetprovider2.service

import android.app.Service
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.os.IBinder
import android.widget.RemoteViews
import com.linqinen708.myappwidgetprovider2.R
import com.linqinen708.myappwidgetprovider2.utils.WidgetProvider
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by Ian on 2019/2/25.
 */
class TimerService: Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private var timer: Timer? = null
    private val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)

    override fun onCreate() {
        super.onCreate()
        timer = Timer()
        timer!!.schedule(object : TimerTask() {
            override fun run() {
                updateViews()
            }
        }, 0, 1000)
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
        timer = null
    }

    private fun updateViews() {
        val time = formatter.format(Date())
        val remoteView = RemoteViews(packageName, R.layout.widget_content)
        remoteView.setTextViewText(R.id.tv_content, time)
        val manager = AppWidgetManager.getInstance(applicationContext)

        val componentName = ComponentName(applicationContext, WidgetProvider::class.java)
        manager.updateAppWidget(componentName, remoteView)
    }
}