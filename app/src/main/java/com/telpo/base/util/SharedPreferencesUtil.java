package com.telpo.base.util;


/**
 * @Description: SharedPreferences工具类
 */
public class SharedPreferencesUtil {

    private static android.content.SharedPreferences mSharedPreferences;

    private static android.content.SharedPreferences getSharedPreferences(android.content.Context context) {
        if (mSharedPreferences == null) {
//			mSharedPreferences = context.getSharedPreferences("telpo_pos", 0);
            mSharedPreferences = context.getSharedPreferences(context.getPackageName(), android.content.Context.MODE_PRIVATE);
        }
        return mSharedPreferences;

    }

    public static void putString(android.content.Context context, String key, String value) {
        android.content.SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getString(android.content.Context context, String key, String value) {
        return getSharedPreferences(context).getString(key, value);
    }

    public static void putBoolean(android.content.Context context, String key, boolean value) {
        android.content.SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static boolean getBoolean(android.content.Context context, String key, boolean value) {
        return getSharedPreferences(context).getBoolean(key, value);
    }

    public static void putLong(android.content.Context context, String key, Long value) {
        android.content.SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public static Long getLong(android.content.Context context, String key, Long value) {
        return getSharedPreferences(context).getLong(key, value);
    }

    public static void remove(android.content.Context context, String key) {
        android.content.SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.remove(key);
        editor.commit();
    }
}
