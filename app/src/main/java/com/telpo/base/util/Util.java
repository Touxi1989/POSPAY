package com.telpo.base.util;

import org.apache.http.conn.util.InetAddressUtils;

import java.io.IOException;
import java.text.ParseException;

/**
 * 工具类
 */
public class Util {
    public static final int NETTYPE_WIFI = 0x01;
    public static final int NETTYPE_CMWAP = 0x02;
    public static final int NETTYPE_CMNET = 0x03;
    public static final String SOURSEFROM = "SourceFromTelpo";
    private static long lastClickTime;

    /**
     * 获取屏幕宽
     */
    public static int screenWidth(android.content.Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取屏幕高
     */
    public static int screenHeight(android.content.Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 判断控件点击事件的触发事件间隔，防止用户多次点击
     */
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 800) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     * @param chars
     * @param length
     * @return String
     * @description: 获得随机字符串random，可控制生成长度
     */
    public static String getRandomString(String chars, int length) {
        StringBuffer sb = new StringBuffer();
        java.util.Random random = new java.util.Random();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(android.content.Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * @param context
     * @param dpValue
     * @return int
     * @description: 单位转换：dp转px
     */
    public static int dp2px(android.content.Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * @param str
     * @param compile
     * @return Matcher
     * @description: 正则匹配
     */
    public static java.util.regex.Matcher pattern(String str, String compile) {
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(compile);

        return pattern.matcher(str);
    }

    /**
     * 去除一个字符串中的数据
     *
     * @param source
     * @param orther
     * @return Create at 2014-8-7 下午04:24:25
     */
    public static String splitString(String source, String... orther) {

        for (int i = 0; i < orther.length; i++) {
            source = source.replace(orther[i], "");
        }

        return source;
    }

    public static String getResuorceString(android.content.Context context, int id) {
        return context.getResources().getString(id);
    }

    /**
     * @param context  上下文
     * @param activity 目标activity
     * @param bundle   携带的数据
     * @param ifLogin  跳转前是否需要登录
     * @return void
     * @description: Activity跳转
     */
    public static void go2Activity(android.content.Context context, Class<?> activity, android.os.Bundle bundle, boolean ifLogin) {
        android.content.Intent intent = new android.content.Intent();
        if (ifLogin /* && AppContext.getUser() == null */) {
            // CustomToast.showText("请先登录！");
            // intent.setClass(context, LoginActivity.class);
        } else {
            intent.setClass(context, activity);
            if (bundle == null) {
                bundle = new android.os.Bundle();
            }
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
    }

    /**
     * @param context     上下文
     * @param activity    目标的activity
     * @param bundle      携带的数据
     * @param requestCode 请求码
     * @param ifLogin     跳转前是否需要登录
     * @return void
     * @description: Activity跳转, 带返回结果
     */
    public static void go2ActivityForResult(android.content.Context context, Class<?> activity, android.os.Bundle bundle, int requestCode,
                                            boolean ifLogin) {
        android.content.Intent intent = new android.content.Intent();
        if (ifLogin/* && AppContext.getUser() == null */) {
            // CustomToast.showText("请先登录！");
            // intent.setClass(context, RegeditAcountActivity.class);
            context.startActivity(intent);
        } else {
            intent.setClass(context, activity);
            if (bundle != null) {
                intent.putExtras(bundle);
            }
            ((android.app.Activity) context).startActivityForResult(intent, requestCode);
        }
    }

    public static void go2Activity(android.content.Context context, String packageName, String activity) {
        android.content.Intent intent = new android.content.Intent();

        intent.setFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClassName(packageName, activity);
        context.startActivity(intent);

    }

    public static boolean checkPackage(android.content.Context context, String packageName) {
        try {
            if (android.text.TextUtils.isEmpty(packageName)) return false;
            context.getPackageManager().getApplicationInfo(packageName, android.content.pm.PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    /**
     * 加载本地图片
     *
     * @param url
     * @return
     */
    public static android.graphics.Bitmap getLoacalBitmap(String url) {
        try {
            java.io.FileInputStream fis = new java.io.FileInputStream(url);
            return android.graphics.BitmapFactory.decodeStream(fis); // /把流转化为Bitmap图片

        } catch (java.io.FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 复制文件
     *
     * @param sourceLocation
     * @param targetLocation
     * @throws IOException Create at 2014-8-21 上午10:47:55
     */
    public static void copyDirectory(java.io.File sourceLocation, java.io.File targetLocation) throws java.io.IOException {
        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists()) {
                targetLocation.mkdir();
            }

            String[] children = sourceLocation.list();
            for (int i = 0; i < sourceLocation.listFiles().length; i++) {

                copyDirectory(new java.io.File(sourceLocation, children[i]), new java.io.File(targetLocation, children[i]));
            }
        } else {

            java.io.InputStream in = new java.io.FileInputStream(sourceLocation);

            java.io.OutputStream out = new java.io.FileOutputStream(targetLocation);

            // Copy the bits from instream to outstream
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        }

    }

    /**
     * 删除指定文件夹下所有文件 param path 文件夹完整绝对路径
     */
    public static boolean delAllFile(String path) {
        boolean flag = false;
        java.io.File file = new java.io.File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        java.io.File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(java.io.File.separator)) {
                temp = new java.io.File(path + tempList[i]);
            } else {
                temp = new java.io.File(path + java.io.File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件

                java.io.File myFilePath = new java.io.File(path + "/" + tempList[i]);
                myFilePath.delete(); // 再删除空文件夹

                flag = true;
            }
        }
        return flag;
    }

    /**
     * 设置margin
     *
     * @param v
     * @param l
     * @param t
     * @param r
     * @param b Create at 2014-9-1 下午03:36:11
     */
    public static void setMargins(android.view.View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof android.view.ViewGroup.MarginLayoutParams) {
            android.view.ViewGroup.MarginLayoutParams p = (android.view.ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }

    /**
     * @param is
     * @return
     * @throws IOException
     * @Title: Inputstream2String
     * @author zhenglinfa
     * @Description: Inputstream转换成String, 不转换编码格式
     */
    public static String inputstream2String(java.io.InputStream is) throws java.io.IOException {
        if (is != null) {
            int BUFFER_SIZE = 4096;
            java.io.ByteArrayOutputStream outStream = new java.io.ByteArrayOutputStream();
            byte[] data = new byte[BUFFER_SIZE];
            int count = -1;
            while ((count = is.read(data, 0, BUFFER_SIZE)) != -1) {
                outStream.write(data, 0, count);
            }
            data = null;
            String outString = new String(outStream.toByteArray());
            outStream.close();

            return outString;
        }

        return null;
    }

    public static void hideKeyboard(android.content.Context context, android.view.View view) {
        android.view.inputmethod.InputMethodManager imm = (android.view.inputmethod.InputMethodManager) context.getSystemService(android.app.Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void showKeybroad(android.content.Context context, android.widget.EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        editText.requestFocusFromTouch();
        android.view.inputmethod.InputMethodManager imm = (android.view.inputmethod.InputMethodManager) context.getSystemService(android.app.Activity.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, 0);
    }

    /**
     * 获取当前网络类型
     *
     * @return 0：没有网络 1：WIFI网络 2：WAP网络 3：NET网络
     */
    @android.annotation.SuppressLint("DefaultLocale")
    public static int getNetworkType(android.content.Context context) {
        int netType = 0;
        android.net.ConnectivityManager connectivityManager = (android.net.ConnectivityManager) context
                .getSystemService(android.content.Context.CONNECTIVITY_SERVICE);
        android.net.NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }
        int nType = networkInfo.getType();
        if (nType == android.net.ConnectivityManager.TYPE_MOBILE) {
            String extraInfo = networkInfo.getExtraInfo();
            if (!android.text.TextUtils.isEmpty(extraInfo)) {
                if (("cmnet".equals(extraInfo.toLowerCase()))) {
                    netType = NETTYPE_CMNET;
                } else {
                    netType = NETTYPE_CMWAP;
                }
            }
        } else if (nType == android.net.ConnectivityManager.TYPE_WIFI) {
            netType = NETTYPE_WIFI;
        }
        return netType;
    }

    /**
     * 检测网络是否可用
     *
     * @return
     */
    public static boolean isNetworkConnected(android.content.Context context) {
        android.net.ConnectivityManager cm = (android.net.ConnectivityManager) context.getSystemService(android.content.Context.CONNECTIVITY_SERVICE);
        android.net.NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }

    /**
     * @return String
     * @description: 获取设备唯一标识
     */
    public static String getDeviceId(android.content.Context context) {
        android.telephony.TelephonyManager telephonyManager = (android.telephony.TelephonyManager) context.getSystemService(android.content.Context.TELEPHONY_SERVICE);
        // String device = Secure.getString(mContext.getContentResolver(),
        // Secure.ANDROID_ID); //需要设备注册谷歌账户，跟上面的方法获取的值不一样
        // TLog.i(TAG, "Secure device：" + device);
        return telephonyManager.getDeviceId();
    }

    /**
     * @return String
     * @description: 获取设备机型
     */
    public static String getDeviceModel() {
        return android.os.Build.MODEL;
    }

    /**
     * @description: 获取App唯一标识, 重装应用以后会生成不一样的id
     * @return
     * @return String
     */
    // public static String getAppId(Context context) {
    // String uniqueID = SharedPreferencesUtil.getString(context,
    // AppConfig.SP_CONF_APP_UNIQUEID, null);
    // if (TextUtils.isEmpty(uniqueID)) {
    // uniqueID = UUID.randomUUID().toString();
    // SharedPreferencesUtil.putString(context, AppConfig.SP_CONF_APP_UNIQUEID,
    // uniqueID);
    // }
    // return uniqueID;
    // }

    /**
     * 获取App安装包信息
     *
     * @return
     */
    public static android.content.pm.PackageInfo getPackageInfo(android.content.Context context) {
        android.content.pm.PackageInfo info = null;
        try {
            info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            e.printStackTrace(System.err);
        }
        if (info == null)
            info = new android.content.pm.PackageInfo();
        return info;
    }

    /**
     * 获取sd卡路径 双sd卡时，获得的是外置sd卡
     *
     * @return
     */
    public static String getSDCardPath() {
        String cmd = "cat /proc/mounts";
        Runtime run = Runtime.getRuntime();// 返回与当前 Java 应用程序相关的运行时对象
        java.io.BufferedInputStream in = null;
        java.io.BufferedReader inBr = null;
        try {
            Process p = run.exec(cmd);// 启动另一个进程来执行命令
            in = new java.io.BufferedInputStream(p.getInputStream());
            inBr = new java.io.BufferedReader(new java.io.InputStreamReader(in));

            String lineStr;
            while ((lineStr = inBr.readLine()) != null) {
                // 获得命令执行后在控制台的输出信息
                android.util.Log.i("CommonUtil", lineStr);
                if (lineStr.contains("sdcard") && lineStr.contains(".android_secure")) {
                    String[] strArray = lineStr.split(" ");
                    if (strArray != null && strArray.length >= 5) {
                        String result = strArray[1].replace("/.android_secure", "");
                        return result;
                    }
                }
                // 检查命令是否执行失败。
                if (p.waitFor() != 0 && p.exitValue() == 1) {
                    // p.exitValue()==0表示正常结束，1：非正常结束
                    android.util.Log.e("CommonUtil", "命令执行失败!");
                }
            }
        } catch (Exception e) {
            android.util.Log.e("CommonUtil", e.toString());
            // return Environment.getExternalStorageDirectory().getPath();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (java.io.IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                if (inBr != null) {
                    inBr.close();
                }
            } catch (java.io.IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return android.os.Environment.getExternalStorageDirectory().getPath();
    }

    /**
     * 判断是否有SD卡
     *
     * @return 有：true 没有：false
     */
    public static boolean hasSDCard() {
        return android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment.getExternalStorageState());
    }

    // 判断SIM卡状态是否可用
    public static boolean SIMisEnable(android.content.Context context) {
        android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context.getSystemService(android.content.Context.TELEPHONY_SERVICE);// 取得相关系统服务
        boolean enable = false;
        switch (tm.getSimState()) { // getSimState()取得sim的状态 有下面6中状态
            case android.telephony.TelephonyManager.SIM_STATE_ABSENT:
                TLog.i("simstatus", "无卡");
                enable = false;
                break;
            case android.telephony.TelephonyManager.SIM_STATE_UNKNOWN:
                TLog.i("simstatus", "未知状态");
                enable = false;
                break;
            case android.telephony.TelephonyManager.SIM_STATE_NETWORK_LOCKED:
                TLog.i("simstatus", "需要NetworkPIN解锁");
                enable = false;
                break;
            case android.telephony.TelephonyManager.SIM_STATE_PIN_REQUIRED:
                TLog.i("simstatus", "需要PIN解锁");
                enable = false;
                break;
            case android.telephony.TelephonyManager.SIM_STATE_PUK_REQUIRED:
                TLog.i("simstatus", "需要PUK解锁");
                enable = false;
                break;
            case android.telephony.TelephonyManager.SIM_STATE_READY:
                TLog.i("simstatus", "良好");
                enable = true;
                break;
        }

        return enable;

    }

    // 是否处于飞行模式
    public static boolean isAirplaneModeOn(android.content.Context context) {
        return android.provider.Settings.System.getInt(context.getContentResolver(), android.provider.Settings.System.AIRPLANE_MODE_ON, 0) != 0;
    }

    /**
     * 判断当前版本是否兼容目标版本的方法
     *
     * @param VersionCode
     * @return
     */
    public static boolean isMethodsCompat(int VersionCode) {
        int currentVersion = android.os.Build.VERSION.SDK_INT;
        return currentVersion >= VersionCode;

    }

    /**
     * 时间转换为long数据
     *
     * @param timeString "yyyy/MM/dd HH:mm:ss"
     * @return
     */
    public static long date2long(String timeString) {
        String temp = 0 + "";
        try {
            long epoch = new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(timeString).getTime();
            temp = "" + epoch;
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return Long.valueOf(temp.trim().substring(0, 10));

    }

    /**
     * 获取当前的日期和时间
     *
     * @return yyyy-MM-dd HH:mm:ss格式的当前的24小时制的日期和时间
     */
    public static String getDateAndTime() {
        java.util.Date date = new java.util.Date(new java.util.Date().getTime());

        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return format.format(date);
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
        /*
         * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
         * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
         * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
         */
        String telRegex = "[1][358]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (android.text.TextUtils.isEmpty(mobiles))
            return false;
        else
            return mobiles.matches(telRegex);
    }

    public static double add(double v1, double v2) {
        java.math.BigDecimal b1 = new java.math.BigDecimal(Double.toString(v1));
        java.math.BigDecimal b2 = new java.math.BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }

    public static double sub(double v1, double v2) {
        java.math.BigDecimal b1 = new java.math.BigDecimal(Double.toString(v1));
        java.math.BigDecimal b2 = new java.math.BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }

    public static double mul(double v1, double v2) {
        java.math.BigDecimal b1 = new java.math.BigDecimal(Double.toString(v1));
        java.math.BigDecimal b2 = new java.math.BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }

    public static double div(double v1, double v2) {
        int DEF_DIV_SCALE = 10;// 默认除法的精度是10
        return div(v1, v2, DEF_DIV_SCALE);
    }

    public static double div(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The   scale   must   be   a   positive   integer   or   zero");
        }
        java.math.BigDecimal b1 = new java.math.BigDecimal(Double.toString(v1));
        java.math.BigDecimal b2 = new java.math.BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, java.math.BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static double round(double v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The   scale   must   be   a   positive   integer   or   zero");
        }
        java.math.BigDecimal b = new java.math.BigDecimal(Double.toString(v));
        java.math.BigDecimal one = new java.math.BigDecimal("1");
        return b.divide(one, scale, java.math.BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static boolean saveBitmap2file(android.graphics.Bitmap bmp, String filename) {
        android.graphics.Bitmap.CompressFormat format = android.graphics.Bitmap.CompressFormat.PNG;
        int quality = 100;
        java.io.OutputStream stream = null;
        try {
            stream = new java.io.FileOutputStream(filename);
        } catch (java.io.FileNotFoundException e) {
            e.printStackTrace();
        }

        return bmp.compress(format, quality, stream);
    }

    /**
     * @param context
     * @param tel       电话号码
     * @param isservice 是否是服务热线
     */
    public static void callTelDialog(final android.content.Context context, final String tel, final boolean isservice) {

        android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(context);
        if (isservice) {
            dialog.setTitle("确定拨打客服热线" + tel + "吗？");
        } else {
            dialog.setTitle("确定拨打" + tel + "吗？");
        }
        dialog.setPositiveButton("确认", new android.content.DialogInterface.OnClickListener() {
            @Override
            public void onClick(android.content.DialogInterface arg0, int arg1) {
                arg0.dismiss();
                android.content.Intent intent = new android.content.Intent(android.content.Intent.ACTION_CALL, android.net.Uri.parse("tel:" + tel));
                context.startActivity(intent);
            }
        });
        dialog.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {
            @Override
            public void onClick(android.content.DialogInterface arg0, int arg1) {
                arg0.dismiss();
            }
        });
        dialog.show();
    }

    @androidx.annotation.RequiresApi(api = android.os.Build.VERSION_CODES.O)
    public static String getIMEI(android.content.Context context) {
        return ((android.telephony.TelephonyManager) context.getSystemService(android.content.Context.TELEPHONY_SERVICE)).getImei(0);
    }

    @androidx.annotation.RequiresApi(api = android.os.Build.VERSION_CODES.O)
    public static String getMEID(android.content.Context context) {
        return ((android.telephony.TelephonyManager) context.getSystemService(android.content.Context.TELEPHONY_SERVICE)).getMeid(0);
    }

    public static String getAndroidId(android.content.Context context) {
        return android.provider.Settings.Secure.getString(context.getContentResolver(),
                android.provider.Settings.Secure.ANDROID_ID);
    }

    public static String getWlanMac() {
        String result = runcmd("ifconfig");
        String wlan0 = result.substring(result.indexOf("wlan0"));
        wlan0 = wlan0.substring(wlan0.indexOf("HWaddr ") + 7, wlan0.indexOf("HWaddr ") + 25).replace(" ", "").replace("\t", "");
        TLog.i(wlan0);
        return wlan0;
    }

    // 读取sim卡号
    public static String getSimSerialNumber(android.content.Context context) {
        return ((android.telephony.TelephonyManager) context.getSystemService(android.content.Context.TELEPHONY_SERVICE)).getSimSerialNumber();
    }

    // 读取sn号


    public static String getSerialNumber(android.content.Context context) {
        String serial = null;
        try {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//                // 9.0 +
//                serial = Build.getSerial();
//            } else
            if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.O) {
                // 8.0 +

                Class<?> c = Class.forName("android.os.SystemProperties");
                java.lang.reflect.Method get = c.getMethod("get", String.class);
                serial = (String) get.invoke(c, "ro.serialno");
            } else {
                // 8.0 -
                serial = android.os.Build.SERIAL;
            }
        } catch (Exception e) {
            e.printStackTrace();
            android.util.Log.e("setSerialNumber", "获取设备序列号失败");
        }
        return serial;
    }

    // /**
    // * getSerialNumber
    // *
    // * @return result is same to getSerialNumber1()
    // */
    // public static String getSerialNumber() {
    // String serial = null;
    // try {
    // Class<?> c = Class.forName("android.os.SystemProperties");
    // Method get = c.getMethod("get", String.class);
    // serial = (String) get.invoke(c, "ro.serialno");
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // return serial;
    // }

    public static void displayDevice(android.content.Context context) {
        String dest_imei = getIMEI(context);
        String androidId = getAndroidId(context);
        android.util.Log.i("util", "isTestDevice: " + " \nIMEI:" + dest_imei + " \nANDROID ID:" + androidId + " \nSerialNumber:"
                + getSerialNumber(context) + " \nSimSerialNumber:" + getSimSerialNumber(context));
    }

    /**
     * 跳转到网络设置页面
     *
     * @param context
     * @param netType 网络类型；1、以太网，2、WiFi，3、移动网络
     */
    public static void go2NetSetting(android.content.Context context, int netType) {
        android.content.Intent androidIntent;
        switch (netType) {
            case 1:
                // 以太网设置
                androidIntent = new android.content.Intent("android.net.ethernet.ETHERNET_SETTINGS");
                context.startActivity(androidIntent);
                break;
            case 2:
                // wifi设置
                androidIntent = new android.content.Intent(android.provider.Settings.ACTION_WIFI_SETTINGS);
                context.startActivity(androidIntent);
                break;
            case 3:
                // 移动网络设置
                androidIntent = new android.content.Intent("android.settings.DATA_ROAMING_SETTINGS");
                context.startActivity(androidIntent);
                break;
        }

    }

    // 显示状态栏
    public static void showBar() {
        try {
            Process process = Runtime.getRuntime().exec(new String[]{"am", "startservice", "--user", "0", "-n",
                    "com.android.systemui/.SystemUIService"});
            process.waitFor();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 关闭状态栏
    public static void closeBar() {
        try {
            android.os.Build.VERSION_CODES vc = new android.os.Build.VERSION_CODES();
            android.os.Build.VERSION vr = new android.os.Build.VERSION();
            String ProcID = "79";
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                ProcID = "42"; // ICS AND NEWER
            }
            Process proc = Runtime.getRuntime()
                    .exec(new String[]{"su", "-c", "service call activity " + ProcID + " s16 com.android.systemui"}); // WAS
            // 79
            proc.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 得到本机ip地址
     *
     * @return
     */
    public static String getLocalHostIp() {
        String ipaddress = "";
        try {
            java.util.Enumeration<java.net.NetworkInterface> en = java.net.NetworkInterface.getNetworkInterfaces();
            // 遍历所用的网络接口
            while (en.hasMoreElements()) {
                java.net.NetworkInterface nif = en.nextElement();// 得到每一个网络接口绑定的所有ip
                java.util.Enumeration<java.net.InetAddress> inet = nif.getInetAddresses();
                // 遍历每一个接口绑定的所有ip
                while (inet.hasMoreElements()) {
                    java.net.InetAddress ip = inet.nextElement();
                    if (!ip.isLoopbackAddress() && InetAddressUtils.isIPv4Address(ip.getHostAddress())) {
                        return ipaddress = ip.getHostAddress();
                    }
                }

            }
        } catch (java.net.SocketException e) {
            android.util.Log.e("feige", "获取本地ip地址失败");
            e.printStackTrace();
        }
        return ipaddress;

    }

    /**
     * 时间戳转换成日期格式字符串
     *
     * @param seconds 精确到秒的字符串
     * @param format
     * @return
     */
    public static String timeStamp2Date(String seconds, String format) {
        if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
            return "";
        }
        if (format == null || format.isEmpty())
            format = "yyyy-MM-dd HH:mm:ss";
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(format);
        return sdf.format(new java.util.Date(Long.valueOf(seconds + "000")));
    }

    /**
     * 日期格式字符串转换成时间戳
     *
     * @param date_str 字符串日期
     * @param format   如：yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String date2TimeStamp(String date_str, String format) {
        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(format);
            return String.valueOf(sdf.parse(date_str).getTime() / 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 取得当前时间戳（精确到秒）
     *
     * @return
     */
    public static String timeStamp() {
        long time = System.currentTimeMillis();
        String t = String.valueOf(time / 1000);
        return t;
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public static int daysBetween(java.util.Date smdate, java.util.Date bdate) throws java.text.ParseException {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        smdate = sdf.parse(sdf.format(smdate));
        bdate = sdf.parse(sdf.format(bdate));
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 字符串的日期格式的计算
     */
    public static int daysBetween(String smdate, String bdate) throws java.text.ParseException {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(sdf.parse(smdate));
        long time1 = cal.getTimeInMillis();
        cal.setTime(sdf.parse(bdate));
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days));
    }

    // 获取Client_secret
    public static String getSecret() {
        Class<?> classType;
        String modelValue = "";
        try {
            classType = Class.forName("android.os.SystemProperties");
            java.lang.reflect.Method getMethod = classType.getDeclaredMethod("get", String.class);
            modelValue = (String) getMethod.invoke(classType, new Object[]{"ro.client.secret"});
        } catch (Exception e) {
            android.util.Log.e("", e.toString());
        }
        // Debug
        if (modelValue.equalsIgnoreCase("")) {
            modelValue = "bf579895ae4e5964bb4e78a09c01551b";
        }
        return modelValue;
    }

    /**
     * 获取机器中所有的应用
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isAvilible(android.content.Context context, String packageName) {
        final android.content.pm.PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        java.util.List<android.content.pm.PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        java.util.List<String> pName = new java.util.ArrayList<String>();// 用于存储所有已安装程序的包名
        // 从pinfo中将包名字逐一取出，压入pName list中
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                pName.add(pn);
            }
        }
        return pName.contains(packageName);// 判断pName中是否有目标程序的包名，有TRUE，没有FALSE
    }

    /**
     * 通过包名获取版本名
     *
     * @param context
     * @param packageName
     * @return
     */
    public static String getVersion(android.content.Context context, String packageName) {
        String version = "";
        final android.content.pm.PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        java.util.List<android.content.pm.PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        // 从pinfo中将包名字逐一取出，压入pName list中
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                if (pinfo.get(i).packageName.equals(packageName)) {
                    version = pinfo.get(i).versionName;
                    return version;
                }
            }
        }
        return version;//
    }

    /**
     * 安装apk并打开app
     *
     * @param context
     * @param file
     */
    public static void installAPK(android.content.Context context, java.io.File file) {
        // setIsAllowInstall();

        com.telpo.base.util.Util.setProperty("rw.telpo.isAllowInstall", "true");
        String isTrue = getProperty("");
        android.util.Log.i("isTrue-------------->", "" + isTrue);
        if (isTrue.equals("true")) {
            android.content.Intent intent = new android.content.Intent();
            intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(android.content.Intent.ACTION_VIEW);
            android.net.Uri uri = android.net.Uri.fromFile(file);
            intent.setData(uri);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
            ((android.app.Activity) context).startActivityForResult(intent, 0);
        } else {
            android.content.Intent intent = new android.content.Intent();
            intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(android.content.Intent.ACTION_VIEW);
            android.net.Uri uri = android.net.Uri.fromFile(file);
            intent.setData(uri);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
            context.startActivity(intent);
        }

    }

    // 设置系统属性，允许可以安装apk
    public static void setIsAllowInstall() {
        Process process = null;
        java.io.DataOutputStream os = null;
        try {
            String cmd = "adb shell";
            String cmd0 = "setprop rw.telpo.isAllowInstall true";
            process = Runtime.getRuntime().exec("su");
            os = new java.io.DataOutputStream(process.getOutputStream());
            os.writeBytes(cmd + "\n");
            os.writeBytes(cmd0 + "\n");
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                process.destroy();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 设置系统属性
    public static boolean setProperty(String key, String value) {
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            java.lang.reflect.Method set = c.getMethod("set", String.class, String.class);
            set.invoke(c, key, value);
            android.util.Log.e("setProperty-", "成功");

            return true;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return false;
    }

    public static String getProperty(String key, String defaultValue) {
        String value = defaultValue;

        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            java.lang.reflect.Method get = c.getMethod("get", String.class, String.class);
            value = (String) (get.invoke(c, key, defaultValue));
            android.util.Log.i("getProperty", "---" + value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    // 在没root情况下，利用反射来获取SystemProperties属性
    public static String getProperty(String defaultValue) {
        String value = defaultValue;

        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            java.lang.reflect.Method get = c.getMethod("get", String.class, String.class);
            value = (String) (get.invoke(c, "rw.telpo.isAllowInstall", defaultValue));
            android.util.Log.i("getProperty", "---" + value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    public static boolean checkOpenApk(android.content.Context context, String pkg, String version) {
        java.util.List<android.content.pm.ResolveInfo> ResolveInfos = new java.util.ArrayList<>();
        android.content.Intent mainIntent = new android.content.Intent(android.content.Intent.ACTION_MAIN, null);
        mainIntent.addCategory(android.content.Intent.CATEGORY_LAUNCHER);
        android.content.pm.PackageManager packageManager = context.getPackageManager();
        //获取到所有的安装包
        java.util.List<android.content.pm.ResolveInfo> installedPackages = packageManager.queryIntentActivities(mainIntent, android.content.pm.PackageManager.GET_UNINSTALLED_PACKAGES);
        for (android.content.pm.ResolveInfo resolveInfos : installedPackages) {
            if (resolveInfos.activityInfo.packageName.equals(pkg)) {
                if (version.equals(getVersion(context, pkg))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获取字符串指定字符第n次出现的位置
     *
     * @param string 字符串
     * @param index  第n次出现
     * @return 返回出现的位置
     */
    public static int getCharacterPosition(String string, int index) {
        //这里是获取"/"符号的位置
        java.util.regex.Matcher slashMatcher = java.util.regex.Pattern.compile("/").matcher(string);
        int mIdx = 0;
        while (slashMatcher.find()) {
            mIdx++;
            if (mIdx == index) {
                break;
            }
        }
        return slashMatcher.start();
    }

    public static boolean isValidDateTimeFormat(String input) {
        if (input == null || input.length() != 14) {
            return false;
        }

        // 正则表达式确保 14 位数字
        if (!input.matches("\\d{14}")) {
            return false;
        }

        // 尝试解析日期
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyyMMddHHmmss");
        sdf.setLenient(false); // 严格模式，防止自动修正（如 20231301 → 20230101）

        try {
            sdf.parse(input); // 如果解析失败，抛出 ParseException
            return true;
        } catch (java.text.ParseException e) {
            return false;
        }
    }

    public static void updateLanguage(java.util.Locale locale) {
        try {
            Object objIActMag, objActMagNative;
            Class clzIActMag = Class.forName("android.app.IActivityManager");
            Class clzActMagNative = Class.forName("android.app.ActivityManagerNative");
            java.lang.reflect.Method mtdActMagNative$getDefault = clzActMagNative.getDeclaredMethod("getDefault");
            objIActMag = mtdActMagNative$getDefault.invoke(clzActMagNative);
            java.lang.reflect.Method mtdIActMag$getConfiguration = clzIActMag.getDeclaredMethod("getConfiguration");
            android.content.res.Configuration config = (android.content.res.Configuration) mtdIActMag$getConfiguration.invoke(objIActMag);
            config.locale = locale;
            Class clzConfig = Class.forName("android.content.res.Configuration");
            java.lang.reflect.Field userSetLocale = clzConfig.getField("userSetLocale");
            userSetLocale.set(config, true);
            // 此处需要声明权限:android.permission.CHANGE_CONFIGURATION
            // 会重新调用 onCreate();
            Class[] clzParams = {android.content.res.Configuration.class};
            java.lang.reflect.Method mtdIActMag$updateConfiguration = clzIActMag.getDeclaredMethod("updateConfiguration", clzParams);
            mtdIActMag$updateConfiguration.invoke(objIActMag, config);
            android.app.backup.BackupManager.dataChanged("com.android.providers.settings");
            TLog.i("updateLanguage end");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (java.lang.reflect.InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static boolean isAmount(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        // 正则表达式：可选正负号 + 整数部分 + 可选小数部分（最多2位）
        String regex = "^[+-]?\\d+(\\.\\d{1,2})?$";
        return str.matches(regex);
    }

    public static void findSoFilesWithNativeLibraryHelper(android.content.Context context) {
        try {
            android.content.pm.ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 0);
            java.io.File apkFile = new java.io.File(appInfo.sourceDir);
            java.io.File libDir = new java.io.File(appInfo.nativeLibraryDir);

            if (libDir.exists()) {
                android.util.Log.i("xuxl", "Native library directory: " + libDir.getAbsolutePath());
                // 这里可以列出 libDir 下的 .so 文件
                for (int i = 0; i < libDir.list().length; i++) {
                    android.util.Log.i("xuxl", "libDir:" + libDir.list()[i]);

                }
            } else {
                android.util.Log.i("xuxl", "Native library directory does not exist.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String runcmd(String cmd) {
//        String msgSuccess = ShellUtils.execCommand("cat /proc/bus/usb/devices", false).successMsg;

//        String msgSuccess = ShellUtils.execCommand("cat /sys/kernel/debug/usb/devices", false).successMsg;
        Process process = null;
        java.io.BufferedReader successResult = null;
        StringBuilder successMsg = new StringBuilder();
        String msgSuccess = "";
        String[] cammand = new String[]{"/bin/sh", "-c", cmd};
        try {
            process = Runtime.getRuntime().exec(cammand);
            process.waitFor();
            successResult = new java.io.BufferedReader(new java.io.InputStreamReader(process.getInputStream()));
            String s;
            while ((s = successResult.readLine()) != null) {
                successMsg.append(s);
            }
            msgSuccess = successMsg.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (successResult != null) {
                    successResult.close();
                    successResult = null;
                }
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
            if (process != null) {
                process.destroy();
            }

        }
        TLog.i(msgSuccess);
        return msgSuccess;
    }

    // 返回开机时间，单位微妙
    public static String bootTime() {
        long time = android.os.SystemClock.elapsedRealtime() / 1000;
//        MLog.i("" + time);
        int hour = (int) (time / (60 * 60));
        int min = (int) ((time % (60 * 60)) / 60);
        int sec = (int) (time % (60));
        return hour + ":" + (min < 10 ? ("0" + min) : min) + ":" + (sec < 10 ? ("0" + sec) : sec);
    }

    /**
     * 获取android当前可用运行内存大小
     */
    public static long getAvailMemory(android.content.Context context) {
        android.app.ActivityManager am = (android.app.ActivityManager) context.getSystemService(android.content.Context.ACTIVITY_SERVICE);
        android.app.ActivityManager.MemoryInfo mi = new android.app.ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        // mi.availMem; 当前系统的可用内存
        TLog.i(android.text.format.Formatter.formatFileSize(context, mi.availMem));// 将获取的内存大小规格化
        return mi.availMem;
    }

    /**
     * 获取android总运行内存大小
     */
    public static String getTotalMemory(android.content.Context context) {
        String str1 = "/proc/meminfo";// 系统内存信息文件
        String str2;
        String[] arrayOfString;
        long initial_memory = 0;
        try {
            java.io.FileReader localFileReader = new java.io.FileReader(str1);
            java.io.BufferedReader localBufferedReader = new java.io.BufferedReader(localFileReader, 8192);
            str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小
            arrayOfString = str2.split("\\s+");
            for (String num : arrayOfString) {
                android.util.Log.i(str2, num + "\t");
            }
            // 获得系统总内存，单位是KB
            int i = Integer.valueOf(arrayOfString[1]).intValue();
            //int值乘以1024转换为long类型
            initial_memory = new Long((int) i * 1024);
            localBufferedReader.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        return android.text.format.Formatter.formatFileSize(context, initial_memory);// Byte转换为KB或者MB，内存大小规格化
    }

    @androidx.annotation.RequiresApi(api = android.os.Build.VERSION_CODES.O)
    public static long queryStorage(android.content.Context context) {
        try {
//        Log.d("statfs", Environment.getExternalStorageDirectory().getPath());
            android.os.StatFs statFs = new android.os.StatFs(android.os.Environment.getDataDirectory().getPath());
            //存储块总数量
            long blockCount = statFs.getBlockCount();
            //块大小
            long blockSize = statFs.getBlockSize();
            //可用块数量
            long availableCount = statFs.getAvailableBlocks();
            //剩余块数量，注：这个包含保留块（including reserved blocks）即应用无法使用的空间
            long freeBlocks = statFs.getFreeBlocks();
            //这两个方法是直接输出总内存和可用空间，也有getFreeBytes//API level 18（JELLY_BEAN_MR2）引入
//        long totalSize = statFs.getTotalBytes();
            android.app.usage.StorageStatsManager storageStatsManager = (android.app.usage.StorageStatsManager) context.getSystemService(android.content.Context.STORAGE_STATS_SERVICE);
            long totalBytes = storageStatsManager.getTotalBytes(android.os.storage.StorageManager.UUID_DEFAULT);
//        Log.d("statfs", "totalBytes = " + Formatter.formatFileSize(context, totalBytes));
            long totalSize = blockSize * blockCount;
            String totalStr = android.text.format.Formatter.formatFileSize(context, totalSize);
//        Log.d("statfs", "totalStr = " + totalStr);
//        Log.d("statfs", "totalStr = " + Formatter.formatFileSize(context, statFs.getTotalBytes()));
            long availableSize = statFs.getAvailableBytes();
//        Log.d("statfs", "total = " + Formatter.formatFileSize(context, totalSize));
//        Log.d("statfs", "availableSize = " + Formatter.formatFileSize(context, availableSize));
            //这里可以看出 available 是小于 free ,free 包括保留块。
//        Log.d("statfs", "total = " + Formatter.formatFileSize(context, blockSize * blockCount));
//        Log.d("statfs", "available = " + Formatter.formatFileSize(context, blockSize * availableCount));
//        Log.d("statfs", "free = " + Formatter.formatFileSize(context, blockSize * freeBlocks));
            TLog.i(android.text.format.Formatter.formatFileSize(context, totalBytes));
            return totalSize;
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        return 0L;
    }

    @androidx.annotation.RequiresApi(api = android.os.Build.VERSION_CODES.O)
    public static String getRomUsed(android.content.Context context) {
        try {
            android.app.usage.StorageStatsManager storageStatsManager = (android.app.usage.StorageStatsManager) context.getSystemService(android.content.Context.STORAGE_STATS_SERVICE);
            long totalBytes = storageStatsManager.getTotalBytes(android.os.storage.StorageManager.UUID_DEFAULT);
            long freeBytes = storageStatsManager.getFreeBytes(android.os.storage.StorageManager.UUID_DEFAULT);
            long usage = (totalBytes - freeBytes);
            return android.text.format.Formatter.formatFileSize(context, usage);
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @androidx.annotation.RequiresApi(api = android.os.Build.VERSION_CODES.O)
    public static long getRomAvail(android.content.Context context) {
        try {
            android.app.usage.StorageStatsManager storageStatsManager = (android.app.usage.StorageStatsManager) context.getSystemService(android.content.Context.STORAGE_STATS_SERVICE);
            long freeBytes = storageStatsManager.getFreeBytes(android.os.storage.StorageManager.UUID_DEFAULT);
            TLog.i(android.text.format.Formatter.formatFileSize(context, freeBytes));
            return freeBytes;
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        return 0L;
    }


    /**
     * 获取内存的容量
     */
    public static long getRamStorage(android.content.Context context) {
        android.app.ActivityManager activityManager = (android.app.ActivityManager) context.getSystemService(android.content.Context.ACTIVITY_SERVICE);
        android.app.ActivityManager.MemoryInfo memoryInfo = new android.app.ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        long totalMem = memoryInfo.totalMem;
        TLog.i(android.text.format.Formatter.formatFileSize(context, totalMem));
//        String ret = android.text.format.Formatter.formatFileSize(context, totalMem);
//        if (ret.startsWith("2.0")) {
//            return "2.00 GB";
//        } else {
//            return ret;
//        }
        return totalMem;
    }

    public static int getUidByPackageName(android.content.Context context, String packageName) {
        int uid = -1;
        android.content.pm.PackageManager packageManager = context.getPackageManager();
        try {
            android.content.pm.PackageInfo packageInfo = packageManager.getPackageInfo(packageName, android.content.pm.PackageManager.GET_META_DATA);
            uid = packageInfo.applicationInfo.uid;
        } catch (android.content.pm.PackageManager.NameNotFoundException ex) {
            ex.printStackTrace();
        }
        return uid;
    }


    @androidx.annotation.RequiresApi(api = android.os.Build.VERSION_CODES.M)
    public static long getMobileNetwork_flow(android.content.Context context) {
        String result = "";

        android.app.usage.NetworkStatsManager statsManager = (android.app.usage.NetworkStatsManager) context.getSystemService(android.content.Context.NETWORK_STATS_SERVICE);


        long summaryTotal = 0;

        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.set(java.util.Calendar.DAY_OF_MONTH, 1);
        calendar.set(java.util.Calendar.HOUR_OF_DAY, 0);
        calendar.set(java.util.Calendar.MINUTE, 0);
        calendar.set(java.util.Calendar.SECOND, 0);
        calendar.set(java.util.Calendar.MILLISECOND, 0);
        long beginTime = calendar.getTimeInMillis();

        long endTime = System.currentTimeMillis();

        TLog.i("time=" + beginTime + "-" + endTime);

        android.app.usage.NetworkStats summaryStats;

        android.app.usage.NetworkStats.Bucket summaryBucket = new android.app.usage.NetworkStats.Bucket();

        try {

            summaryStats = statsManager.querySummary(android.net.ConnectivityManager.TYPE_MOBILE, null, beginTime, endTime);

            do {

                summaryStats.getNextBucket(summaryBucket);
                long summaryRx = 0;

                long summaryTx = 0;
                long summary = 0;
                int summaryUid = summaryBucket.getUid();

//                if (TrafficInfo.getUidByPackageName(getActivity(),getActivity().getPackageName()) == summaryUid) {

                summaryRx = summaryBucket.getRxBytes();

                summaryTx = summaryBucket.getTxBytes();

                summary = summaryRx + summaryTx;
                summaryTotal += summary;


            } while (summaryStats.hasNextBucket());

        } catch (android.os.RemoteException e) {

            e.printStackTrace();

        }
        result = android.text.format.Formatter.formatFileSize(context, summaryTotal);

        TLog.i(result);
        return summaryTotal;

    }

    public static int getPidByPackageName(android.content.Context context, String packageName) {
        android.app.ActivityManager activityManager = (android.app.ActivityManager) context.getSystemService(android.content.Context.ACTIVITY_SERVICE);
        if (activityManager == null) {
            return -1;
        }

        // 获取所有运行中的进程
        java.util.List<android.app.ActivityManager.RunningAppProcessInfo> runningProcesses = activityManager.getRunningAppProcesses();
        if (runningProcesses == null) {
            return -1;
        }

        // 遍历进程列表，匹配包名
        for (android.app.ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
            String[] packageNames = processInfo.pkgList;
            if (packageNames != null) {
                for (String pkg : packageNames) {
                    if (pkg.equals(packageName)) {
                        return processInfo.pid; // 返回匹配的 PID
                    }
                }
            }
        }

        return -1; // 未找到目标包名的进程
    }

    /**
     * 获取指定包名的应用的内存使用情况
     * 需要高权限（如系统签名）
     */
    public static long getAppMemoryUsage(android.content.Context context, String packageName) {
        android.app.ActivityManager activityManager = (android.app.ActivityManager) context.getSystemService(android.content.Context.ACTIVITY_SERVICE);
        if (activityManager == null) {
            return -1;
        }

        // 获取所有运行中的进程信息
        java.util.List<android.app.ActivityManager.RunningAppProcessInfo> runningApps = activityManager.getRunningAppProcesses();
        if (runningApps == null) {
            return -1;
        }

        int[] pids = new int[runningApps.size()];
        for (int i = 0; i < runningApps.size(); i++) {
            pids[i] = runningApps.get(i).pid;
        }

        // 获取每个进程的内存信息
        android.os.Debug.MemoryInfo[] memoryInfoArray = activityManager.getProcessMemoryInfo(pids);
        if (memoryInfoArray == null) {
            return -1;
        }

        for (android.os.Debug.MemoryInfo memoryInfo : memoryInfoArray) {
            // 需要根据包名匹配 PID，这需要额外的逻辑
            // 由于普通方法无法直接通过包名获取 PID，此方法受限
            TLog.i("" + memoryInfo.getTotalPss());
        }

        return -1; // 无法直接通过包名获取内存信息
    }

    /**
     * 获取应用包占用的内存
     *
     * @param packageName
     * @return
     */
    public static String getRss(String packageName) {
        //或者 执行这个命令 cat /proc/pid/statm
        String rss = com.telpo.base.util.Util.runcmd("dumpsys meminfo " + packageName);
        rss = rss.substring(rss.indexOf("TOTAL RSS:") + 10, rss.indexOf("TOTAL SWAP PSS:")).replace(" ", "");
        return rss;
    }


    /**
     * 获取应用包占用的cpu
     *
     * @param packageName
     * @return
     */
    public static String getCPUUsage(android.content.Context context, String packageName) {
        // 执行这个命令 //ps -A -o pid,user,%cpu,%mem,cmd | grep pid
        int pid = getPidByPackageName(context, packageName);
        TLog.i("pid=" + pid);
        String res = com.telpo.base.util.Util.runcmd("top -n 1 ");
        res = res.substring(res.indexOf(pid + ""));
        TLog.i(res);
        return res;
    }
}
