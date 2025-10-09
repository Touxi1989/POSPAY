package com.telpo.pospay.main.util;



/**
 * Created by yemiekai on 2017/4/8 0008.
 */

public class UIUtil {



    public static String getVersion(android.content.Context context) {
        try {
            android.content.pm.PackageManager manager = context.getPackageManager();
            android.content.pm.PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "1.0";
        }
    }

    //在进程中去寻找当前APP的信息，判断是否在前台运行
    public static boolean isAppOnForeground(android.content.Context context) {
        android.app.ActivityManager activityManager = (android.app.ActivityManager) context.getApplicationContext().getSystemService(
                android.content.Context.ACTIVITY_SERVICE);
        String packageName = context.getApplicationContext().getPackageName();
        java.util.List<android.app.ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null)
            return false;
        for (android.app.ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }



    /**
     * @param context
     * @param yesBtnName YES按键名
     * @param noBtnName  NO按键名
     * @param title      标题
     * @param msg        提示信息
     * @param showInfo   是否显示终端号、序列号等信息 boolean
     * @param timeOut    对话款超时时间 int  ,0表示不设置超时取消，其余整数表示秒（单位）
     * @param listener   按键事件回调
     */
    public static android.app.Dialog showCustomDialog(final android.content.Context context, final String yesBtnName, final String noBtnName, final String title, final String msg, final boolean showInfo, final int timeOut, final com.telpo.pospay.main.dialog.CustomDialog.CustomDialogListener listener) {

        final com.telpo.pospay.main.dialog.CustomDialog customDialog = new com.telpo.pospay.main.dialog.CustomDialog(context);
        final java.util.Timer dialogTimer = new java.util.Timer();
//        String deviceinfo = "序列号:" + TPPOSUtil.getTerminalSN(mContext) + "\n终端号:" + TPPOSUtil.getTerminalID(mContext) + "\n应用版本:" + TPPOSUtil.getAPIVersion(context);
//        customDialog.setInfo(showInfo ? deviceinfo : "\n");
        customDialog.setTitle(title);
        customDialog.setMessage(msg);
        customDialog.setCancelable(false);
        customDialog.setOnYesClick(yesBtnName, new android.content.DialogInterface.OnClickListener() {
            @Override
            public void onClick(android.content.DialogInterface dialog, int which) {
                if (listener != null) {
                    listener.onYesClick();
                }
                if (customDialog != null) {
                    customDialog.cancel();
                }
            }
        });
        customDialog.setOnNoClick(noBtnName, new android.content.DialogInterface.OnClickListener() {
            @Override
            public void onClick(android.content.DialogInterface dialog, int which) {
                if (listener != null) {
                    listener.onNoClick();
                }
                if (customDialog != null) {
                    customDialog.cancel();
                }
            }
        });
        customDialog.setOnDismissListener(new android.content.DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(android.content.DialogInterface dialog) {
                if (customDialog != null) {
                    customDialog.cancel();
                }
                if (dialogTimer != null) {
                    dialogTimer.cancel();
                }
                if (listener != null && timeOut > 0) {
                    listener.timeOut();
                }
            }
        });
        if (timeOut > 0) {
            dialogTimer.schedule(new java.util.TimerTask() {
                @Override
                public void run() {
                    if (customDialog != null) {
                        customDialog.cancel();
                    }
//                    if (listener != null) {
//                        listener.timeOut();
//                    }
                }
            }, timeOut * 1000);
        }
        customDialog.show();
        return customDialog;
    }




}
