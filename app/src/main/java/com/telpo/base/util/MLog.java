package com.telpo.base.util;

/**
 * 日志输出工具
 */

public final class MLog {
    private static final String LOG_TAG = "----TP----";
    private static boolean DEBUG = true;

    private MLog() {
    }
    public static void setPrint(boolean print) {
        DEBUG = print;
    }

    public static void e(String log) {
//        log = "Thread-" + Thread.currentThread().getId() + "---" + log;
        log = getLogInfo("", log);
        if (DEBUG && !android.text.TextUtils.isEmpty(log)) {
            e(LOG_TAG, "" + log);
        }
    }

    public static void i(String log) {
//        log = "Thread-" + Thread.currentThread().getId() + "---" + log;
        log = getLogInfo("", log);
        if (DEBUG && !android.text.TextUtils.isEmpty(log)) {

            i(LOG_TAG, log);
        }
    }

    public static void d(String log) {
//        log = "Thread-" + Thread.currentThread().getId() + "---" + log;
        log = getLogInfo("", log);
        if (DEBUG && !android.text.TextUtils.isEmpty(log)) {
            d(LOG_TAG, log);
        }
    }

    public static void i(String tag, String log) {
        if (DEBUG && !android.text.TextUtils.isEmpty(tag) && !android.text.TextUtils.isEmpty(log)) {

            //信息太长,分段打印
            //因为String的length是字符数量不是字节数量所以为了防止中文字符过多，
            //  把4*1024的MAX字节打印长度改为2001字符数
            int max_str_length = 2001 - tag.length();
            //大于4000时
            while (log.length() > max_str_length) {
                android.util.Log.i(tag, log.substring(0, max_str_length));
                log = log.substring(max_str_length);
            }
            //剩余部分
            android.util.Log.i(tag, log);
        }
    }

    private static void d(String tag, String log) {
        if (DEBUG && !android.text.TextUtils.isEmpty(tag) && !android.text.TextUtils.isEmpty(log)) {
            //信息太长,分段打印
            //因为String的length是字符数量不是字节数量所以为了防止中文字符过多，
            //  把4*1024的MAX字节打印长度改为2001字符数
            int max_str_length = 2001 - tag.length();
            //大于4000时
            while (log.length() > max_str_length) {
                android.util.Log.d(tag, log.substring(0, max_str_length));
                log = log.substring(max_str_length);
            }
            //剩余部分
            android.util.Log.d(tag, log);
        }
    }


    public static void e(String tag, String log) {
        if (DEBUG && !android.text.TextUtils.isEmpty(tag) && !android.text.TextUtils.isEmpty(log)) {
            //信息太长,分段打印
            //因为String的length是字符数量不是字节数量所以为了防止中文字符过多，
            //  把4*1024的MAX字节打印长度改为2001字符数
            int max_str_length = 2001 - tag.length();
            //大于4000时
            while (log.length() > max_str_length) {
                android.util.Log.e(tag, log.substring(0, max_str_length));
                log = log.substring(max_str_length);
            }
            //剩余部分
            android.util.Log.e(tag, log);
        }
    }


    private static String getLogInfo(String tagStr, String msg) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

        int index = 4;
        String className = stackTrace[index].getFileName();
        String methodName = stackTrace[index].getMethodName();
        int lineNumber = stackTrace[index].getLineNumber();

        String tag = (tagStr == null ? className : tagStr);
        methodName = methodName.substring(0, 1).toUpperCase() + methodName.substring(1);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[ (").append(className).append(":").append(lineNumber).append(")#").append(methodName).append(" ] ");

        if (msg != null) {
            stringBuilder.append(msg);
        }

        String logStr = stringBuilder.toString();
        return logStr;

    }
}
