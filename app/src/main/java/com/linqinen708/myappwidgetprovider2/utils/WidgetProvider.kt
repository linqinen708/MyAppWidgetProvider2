package com.linqinen708.myappwidgetprovider2.utils

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import android.widget.Toast
import com.linqinen708.myappwidgetprovider2.R
import com.linqinen708.myappwidgetprovider2.service.ColorService


/**
 * Created by Ian on 2019/2/25.
 */
class WidgetProvider : AppWidgetProvider() {

    companion object {
        private const val CLICK_NAME_ACTION = "com.action.widget.click"

        private var remoteViews: RemoteViews? = null

        private var componentName: ComponentName? = null

        var isChangeColor: Boolean = false
    }


    private fun getRemoteViews(context: Context): RemoteViews {
        if (remoteViews == null) {
            LogT.i("创建RemoteViews")
            remoteViews = RemoteViews(context.packageName, R.layout.widget_content)
        }
        return remoteViews as RemoteViews
    }

    private fun getComponentName(context: Context): ComponentName {
        if (componentName == null) {
            LogT.i("创建ComponentName")
            componentName = ComponentName(context, WidgetProvider::class.java)
        }
        return componentName as ComponentName
    }


    private fun updateAppWidget(context: Context,
                                appWidgeManger: AppWidgetManager) {

        LogT.i("刷新界面，remoteViews:$remoteViews,componentName:$componentName")

        val intentClick = Intent(CLICK_NAME_ACTION)
        intentClick.component = getComponentName(context)
        val pendingIntent = PendingIntent.getBroadcast(context, 0,
                intentClick, PendingIntent.FLAG_UPDATE_CURRENT)
        getRemoteViews(context).setOnClickPendingIntent(R.id.tv_content, pendingIntent)
        appWidgeManger.updateAppWidget(getComponentName(context), getRemoteViews(context))
    }

    /**
     * 当小组件被添加到屏幕上时回调
     *
     * 8.0 系统 必须使用startForegroundService方法开启service，否则报错
     */
    private fun startService(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(Intent(context, ColorService::class.java))
        } else {
            context.startService(Intent(context, ColorService::class.java))
        }
    }

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        startService(context)
        LogT.i("启动WidgetProvider")
    }

    /**
     * 当小组件被刷新时回调
     */
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        LogT.i("刷新WidgetProvider")
        updateAppWidget(context, appWidgetManager)
    }

    /**
     * 当widget小组件从屏幕移除时回调
     */
    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        super.onDeleted(context, appWidgetIds)
        LogT.i("移除")
    }

    /**
     * 当最后一个小组件被从屏幕中移除时回调
     */
    override fun onDisabled(context: Context) {
        super.onDisabled(context)
        context.stopService(Intent(context, ColorService::class.java))
        componentName = null
        remoteViews = null
        LogT.i("销毁WidgetProvider")
    }


    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)

        LogT.i("接收广播")
        if (context != null && intent != null && CLICK_NAME_ACTION == intent.action) {
            if (isChangeColor) {
                LogT.i("startService开始变色")
                startService(context)
                isChangeColor = false
                Toast.makeText(context, "触发点击事件", Toast.LENGTH_LONG).show()
            } else {
                isChangeColor = true
                LogT.i("stopService停止变色")
                context.stopService(Intent(context, ColorService::class.java))
                Toast.makeText(context, "触发点击事件", Toast.LENGTH_LONG).show()
            }
        }
    }
}