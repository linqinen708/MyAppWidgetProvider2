package com.linqinen708.myappwidgetprovider2.utils;//package com.linqinen708.myremoteviews.utils;
//
//import android.app.PendingIntent;
//import android.appwidget.AppWidgetManager;
//import android.appwidget.AppWidgetProvider;
//import android.content.ComponentName;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Build;
//import android.widget.RemoteViews;
//import android.widget.Toast;
//
//import com.linqinen708.myremoteviews.R;
//import com.linqinen708.myappwidgetprovider2.service.ColorService;
//
///**
// * Created by Ian on 2019/2/26.
// * <p>
// * 进阶版的可以看https://www.cnblogs.com/joy99/p/6346829.html
// */
//public class WidgetProvider2 extends AppWidgetProvider {
//
//    private static final String CLICK_NAME_ACTION = "com.action.widget.click";
//
//    private static RemoteViews remoteViews;
//
//    private static ComponentName componentName;
//
//    public static boolean isChangeColor;
//
//    public static RemoteViews getRemoteViews(Context context) {
//        if (remoteViews == null) {
//            LogT.i("创建RemoteViews");
//            remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_content);
//        }
//        return remoteViews;
//    }
//
//    public static ComponentName getComponentName(Context context) {
//        if (componentName == null) {
//            LogT.i("创建ComponentName");
//            componentName = new ComponentName(context, WidgetProvider2.class);
//        }
//        return componentName;
//    }
//
//    public void updateAppWidget(Context context,
//                                AppWidgetManager appWidgeManger) {
//
//        LogT.i("刷新界面，remoteViews:" + remoteViews + ",componentName:" + componentName);
//
//        if (remoteViews == null || componentName == null) {
//            Intent intentClick = new Intent(CLICK_NAME_ACTION);
//            intentClick.setComponent(getComponentName(context));
//            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
//                    intentClick, PendingIntent.FLAG_UPDATE_CURRENT);
//            getRemoteViews(context).setOnClickPendingIntent(R.id.tv_content, pendingIntent);
//            appWidgeManger.updateAppWidget(getComponentName(context), getRemoteViews(context));
//        }
//
//    }
//
//    private void startService(Context context) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            context.startForegroundService(new Intent(context, ColorService.class));
//        } else {
//            context.startService(new Intent(context, ColorService.class));
//        }
//    }
//
//    @Override
//    public void onEnabled(Context context) {
//        super.onEnabled(context);
//        startService(context);
//        LogT.i("启动WidgetProvider2");
//    }
//
//    @Override
//    public void onDisabled(Context context) {
//        super.onDisabled(context);
//        context.stopService(new Intent(context, ColorService.class));
//        componentName = null;
//        remoteViews = null;
//        LogT.i("销毁WidgetProvider2");
//    }
//
//    @Override
//    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
//                         int[] appWidgetIds) {
//        LogT.i("刷新WidgetProvider2");
//        updateAppWidget(context, appWidgetManager);
//
//    }
//
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        super.onReceive(context, intent);
//
//        LogT.i("接收广播");
//        if (intent != null && CLICK_NAME_ACTION.equals(intent.getAction())) {
//            if (isChangeColor) {
//                LogT.i("startService开始变色");
//                startService(context);
//                isChangeColor = false;
//                Toast.makeText(context, "触发点击事件",
//                        Toast.LENGTH_LONG).show();
//            } else {
//                isChangeColor = true;
//                LogT.i("stopService停止变色");
//                context.stopService(new Intent(context, ColorService.class));
//                Toast.makeText(context, "触发点击事件",
//                        Toast.LENGTH_LONG).show();
//            }
//
//        }
////        AppWidgetManager appWidgetManger = AppWidgetManager.getInstance(context);
////        int[] appIds = appWidgetManger.getAppWidgetIds(new ComponentName(context, WidgetProvider2.class));
////        appWidgetManger.updateAppWidget(appIds, remoteViews);
////        updateAppWidget(context, appWidgetManger);
////        appWidgetManger.updateAppWidget(getComponentName(context), getRemoteViews(context));
//    }
//}
