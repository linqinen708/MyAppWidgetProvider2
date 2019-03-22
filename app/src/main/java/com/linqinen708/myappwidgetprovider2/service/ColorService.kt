package com.linqinen708.myappwidgetprovider2.service

import android.app.*
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.widget.RemoteViews
import com.linqinen708.myappwidgetprovider2.PendingIntentActivity
import com.linqinen708.myappwidgetprovider2.R
import com.linqinen708.myappwidgetprovider2.utils.LogT
import com.linqinen708.myappwidgetprovider2.utils.WidgetProvider
import java.util.*


/**
 * Created by Ian on 2019/2/25.
 *
 */
class ColorService : Service() {

    private var manager: AppWidgetManager? = null

    private var componentName: ComponentName? = null

    //    private val action = "ACTION_ON_CLICK"

    private val colors = intArrayOf(Color.BLUE, Color.DKGRAY, Color.GREEN, Color.RED, Color.CYAN,
            Color.WHITE, Color.GRAY, Color.MAGENTA, Color.LTGRAY, Color.YELLOW)
    private var remoteView: RemoteViews? = null

//    private var onClickIntent = Intent()

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private var timer: Timer? = null

    private var index = 0

    override fun onCreate() {
        super.onCreate()
        initData()
        timer = Timer()
        timer!!.schedule(object : TimerTask() {
            override fun run() {
                updateViews()
            }
        }, 0, 1000)

        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        LogT.i("onStartCommand开启服务")

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        LogT.i("onDestroy服务终止")
        timer?.cancel()
        timer = null
        WidgetProvider.isChangeColor = true

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            stopForeground(true)
        }

        super.onDestroy()
    }

    private fun initData() {
        remoteView = RemoteViews(packageName, R.layout.widget_content)

        manager = AppWidgetManager.getInstance(applicationContext)

//        componentName = ComponentName(applicationContext, WidgetProvider2::class.java)
        componentName = ComponentName(applicationContext, WidgetProvider::class.java)

        LogT.i("初始化")
    }

    private fun updateViews() {

//        LogT.i(" index：$index ， colors[index]：${colors[index]} " + ",remoteView:"+remoteView + "manager:"+manager)
        remoteView?.setTextColor(R.id.tv_content, colors[index])
        index++
        index %= colors.size
//        remoteView.setTextViewText(R.id.tv_content, time)

        manager?.updateAppWidget(componentName, remoteView)
//        reflectOnePrivateVariable(manager)
    }

    /**https://blog.csdn.net/sinat_20059415/article/details/80584487
     *
     * 其中9.0版本需要增加权限
     * <uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
     * */
    private fun createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
////            // 通知渠道的id
            val channelId = "my_channel_01"
//            // 用户可以看到的通知渠道的名字.
            val channelName = "HoneyCat"
//            //         用户可以看到的通知渠道的描述
//            val description = "HoneyCat祝上"
            val mChannel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
//            //         配置通知渠道的属性
//            mChannel.description = description
//            //         设置通知出现时的闪灯（如果 android 设备支持的话）
//            mChannel.enableLights(true)
//            mChannel.lightColor = Color.RED
//
//            //         设置通知出现时的震动（如果 android 设备支持的话）
//            mChannel.enableVibration(true)
//            mChannel.vibrationPattern = longArrayOf(100, 200, 300, 400)
//            //         最后在notificationmanager中创建该通知渠道 //
            mNotificationManager.createNotificationChannel(mChannel)

            val intent = Intent(this, PendingIntentActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

            // 为该通知设置一个id
            val notifyID = 1
            // Create a notification and set the notification channel.
            val notification = Notification.Builder(this,channelId)
                    .setContentTitle("通知标题")
                    .setContentText("通知内容")
//                    .setContentIntent()//设置通知栏意图
                    .setSmallIcon(R.mipmap.ic_launcher)
                    //设置通知的大图标
//                    .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher))
                    .setChannelId(channelId)
                    .setContentIntent(pendingIntent)
                    //实现点击跳转后关闭通知
                    .setAutoCancel(true)
                    .setOngoing(true)//设置让通知左右滑动删除通知
                    .build()
            notification.flags = Notification.FLAG_AUTO_CANCEL// 该通知能被状态栏的清除按钮给清除掉
            startForeground(notifyID, notification)
            LogT.i("开启8.0通知")
        }
    }

}