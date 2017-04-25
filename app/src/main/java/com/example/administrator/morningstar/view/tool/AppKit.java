package com.example.administrator.morningstar.view.tool;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.pm.ServiceInfo;
import android.util.Log;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by anson on 2017/4/5.
 */

class AppKit {
    /**
     * 重启App
     */
    static public void kill(Context context, boolean restart, Class<? extends Activity> launchClz) {
        //重启
        if (restart) {
            //关闭除了首页以外的页面
            Intent intent = new Intent(context, launchClz);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
        //关闭数据库
        /*{
            ApplicationIo.getInstance(context).closePublicModule();
            ApplicationIo.getInstance(context).closePrivateModule();
        }*/

        //停止后台 service
        try {
            //读取所有的SERVICE信息
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SERVICES);
            if (packageInfo != null && packageInfo.services != null) {
                for (ServiceInfo serviceInfo : packageInfo.services) {
                    try {
                        Class cls = Class.forName(serviceInfo.name);
                        context.stopService(new Intent(context, cls));
                    } catch (ClassNotFoundException e) {
                    }
                }
            }
        } catch (Exception e) {
            Log.e(AppKit.class.getName(), e.getMessage(), e);
        }
        //杀死所有进程
        try {
            Set<String> processNameSet = new HashSet<String>();
            //读取 所有的Process
            {
                //读取Activity
                {
                    PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
                    if (packageInfo != null && packageInfo.activities != null) {
                        for (ActivityInfo activityInfo : packageInfo.activities) {
                            processNameSet.add(activityInfo.processName);
                        }
                    }
                }
                //读取Service
                {
                    PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SERVICES);
                    if (packageInfo != null && packageInfo.services != null) {
                        for (ServiceInfo serviceInfo : packageInfo.services) {
                            processNameSet.add(serviceInfo.processName);
                        }
                    }
                }
                //读取 RECEIVERS
                {
                    PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_RECEIVERS);
                    if (packageInfo != null && packageInfo.receivers != null) {
                        for (ActivityInfo activityInfo : packageInfo.receivers) {
                            processNameSet.add(activityInfo.processName);
                        }
                    }
                }
                //读取 PROVIDERS
                {
                    PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_PROVIDERS);
                    if (packageInfo != null && packageInfo.providers != null) {
                        for (ProviderInfo providerInfo : packageInfo.providers) {
                            processNameSet.add(providerInfo.processName);
                        }
                    }
                }
            }
            //关闭所有进程
            {
                int myPid = android.os.Process.myPid();
                ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                Iterator iterator = mActivityManager.getRunningAppProcesses().iterator();
                while (iterator.hasNext()) {
                    ActivityManager.RunningAppProcessInfo appProcess = (ActivityManager.RunningAppProcessInfo) iterator.next();
                    if (processNameSet.contains(appProcess.processName)) {
                        //先KILL其他进程
                        if (appProcess.pid != myPid) {
                            android.os.Process.killProcess(appProcess.pid);
                        }
                    }
                }

                //杀死自己
                android.os.Process.killProcess(myPid);
            }
        } catch (Exception e) {
            Log.e(AppKit.class.getName(), e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }


}
