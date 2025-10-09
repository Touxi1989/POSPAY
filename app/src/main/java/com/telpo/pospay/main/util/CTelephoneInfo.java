package com.telpo.pospay.main.util;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.Method;

public class CTelephoneInfo {
    private String imeiSIM1;// IMEI
    private String imeiSIM2;//IMEI
    private String iNumeric1;//sim1 code number
    private String iNumeric2;//sim2 code number
    private boolean isSIM1Ready;//sim1
    private boolean isSIM2Ready;//sim2
    private String iDataConnected1 = "0";//sim1 0 no, 1 connecting, 2 connected, 3 suspended.
    private String iDataConnected2 = "0";//sim2
    private static com.telpo.pospay.main.util.CTelephoneInfo CTelephoneInfo;
    private static android.content.Context mContext;

    private CTelephoneInfo() {
    }

    public synchronized static com.telpo.pospay.main.util.CTelephoneInfo getInstance(android.content.Context context) {
        if (CTelephoneInfo == null) {
            CTelephoneInfo = new com.telpo.pospay.main.util.CTelephoneInfo();
        }
        mContext = context;
        return CTelephoneInfo;
    }

    public String getImeiSIM1() {
        return imeiSIM1;
    }

    public String getImeiSIM2() {
        return imeiSIM2;
    }

    public boolean isSIM1Ready() {
        return isSIM1Ready;
    }

    public boolean isSIM2Ready() {
        return isSIM2Ready;
    }

    public boolean isDualSim() {
        return imeiSIM2 != null;
    }

    public boolean isDataConnected1() {
        if (android.text.TextUtils.equals(iDataConnected1, "2") || android.text.TextUtils.equals(iDataConnected1, "1"))
            return true;
        else
            return false;
    }

    public boolean isDataConnected2() {
        if (android.text.TextUtils.equals(iDataConnected2, "2") || android.text.TextUtils.equals(iDataConnected2, "1"))
            return true;
        else
            return false;
    }

    public String getINumeric1() {
        return iNumeric1;
    }

    public String getINumeric2() {
        return iNumeric2;
    }

    public String getINumeric() {
        if (imeiSIM2 != null) {
            if (iNumeric1 != null && iNumeric1.length() > 1)
                return iNumeric1;

            if (iNumeric2 != null && iNumeric2.length() > 1)
                return iNumeric2;
        }
        return iNumeric1;
    }


    public void setCTelephoneInfo() {
        android.telephony.TelephonyManager telephonyManager = ((android.telephony.TelephonyManager)
                mContext.getSystemService(android.content.Context.TELEPHONY_SERVICE));
        try {
            CTelephoneInfo.imeiSIM1 = telephonyManager.getDeviceId();
            android.util.Log.d("CTelephoneInfo","getDeviceId=" + CTelephoneInfo.imeiSIM1);
        } catch (SecurityException se) {

        }
        CTelephoneInfo.imeiSIM2 = null;
        try {
            CTelephoneInfo.imeiSIM1 = getOperatorBySlot(mContext, "getDeviceIdGemini", 0);
            CTelephoneInfo.imeiSIM2 = getOperatorBySlot(mContext, "getDeviceIdGemini", 1);
            android.util.Log.d("CTelephoneInfo", "getDeviceIdGemini=" + imeiSIM1);
            android.util.Log.d("CTelephoneInfo","getDeviceIdGemini=" + CTelephoneInfo.imeiSIM2);
            CTelephoneInfo.iNumeric1 = getOperatorBySlot(mContext, "getSimOperatorGemini", 0);
            CTelephoneInfo.iNumeric2 = getOperatorBySlot(mContext, "getSimOperatorGemini", 1);
            CTelephoneInfo.iDataConnected1 = getOperatorBySlot(mContext, "getDataStateGemini", 0);
            CTelephoneInfo.iDataConnected2 = getOperatorBySlot(mContext, "getDataStateGemini", 1);
        } catch (com.telpo.pospay.main.util.CTelephoneInfo.GeminiMethodNotFoundException e) {
            try {
                CTelephoneInfo.imeiSIM1 = getOperatorBySlot(mContext, "getDeviceId", 0);
                CTelephoneInfo.imeiSIM2 = getOperatorBySlot(mContext, "getDeviceId", 1);
                android.util.Log.d("CTelephoneInfo","getOperatorBySlot=" + CTelephoneInfo.imeiSIM1);
                android.util.Log.d("CTelephoneInfo","getOperatorBySlot=" + imeiSIM2);
                CTelephoneInfo.iNumeric1 = getOperatorBySlot(mContext, "getSimOperator", 0);
                CTelephoneInfo.iNumeric2 = getOperatorBySlot(mContext, "getSimOperator", 1);
                CTelephoneInfo.iDataConnected1 = getOperatorBySlot(mContext, "getDataState", 0);
                CTelephoneInfo.iDataConnected2 = getOperatorBySlot(mContext, "getDataState", 1);
            } catch (com.telpo.pospay.main.util.CTelephoneInfo.GeminiMethodNotFoundException e1) {

            }
        }
        CTelephoneInfo.isSIM1Ready = telephonyManager.getSimState() == android.telephony.TelephonyManager.SIM_STATE_READY;
        CTelephoneInfo.isSIM2Ready = false;
        try {
            CTelephoneInfo.isSIM1Ready = getSIMStateBySlot(mContext, "getSimStateGemini", 0);
            CTelephoneInfo.isSIM2Ready = getSIMStateBySlot(mContext, "getSimStateGemini", 1);
        } catch (com.telpo.pospay.main.util.CTelephoneInfo.GeminiMethodNotFoundException e) {
            try {
                CTelephoneInfo.isSIM1Ready = getSIMStateBySlot(mContext, "getSimState", 0);
                CTelephoneInfo.isSIM2Ready = getSIMStateBySlot(mContext, "getSimState", 1);
            } catch (com.telpo.pospay.main.util.CTelephoneInfo.GeminiMethodNotFoundException e1) {
            }
        }
    }

    private static String getOperatorBySlot(android.content.Context context, String predictedMethodName, int slotID)
            throws com.telpo.pospay.main.util.CTelephoneInfo.GeminiMethodNotFoundException {
        String inumeric = null;
        android.telephony.TelephonyManager telephony = (android.telephony.TelephonyManager) context.getSystemService(android.content.Context.TELEPHONY_SERVICE);
        try {
            Class<?> telephonyClass = Class.forName(telephony.getClass().getName());
            Class<?>[] parameter = new Class[1];
            parameter[0] = int.class;
            java.lang.reflect.Method getSimID = telephonyClass.getMethod(predictedMethodName, parameter);
            Object[] obParameter = new Object[1];
            obParameter[0] = slotID;
            Object ob_phone = getSimID.invoke(telephony, obParameter);
            if (ob_phone != null) {
                inumeric = ob_phone.toString();
            }
        } catch (Exception e) {
            throw new com.telpo.pospay.main.util.CTelephoneInfo.GeminiMethodNotFoundException(predictedMethodName);
        }
        return inumeric;
    }

    private static boolean getSIMStateBySlot(android.content.Context context, String predictedMethodName, int slotID) throws com.telpo.pospay.main.util.CTelephoneInfo.GeminiMethodNotFoundException {

        boolean isReady = false;

        android.telephony.TelephonyManager telephony = (android.telephony.TelephonyManager) context.getSystemService(android.content.Context.TELEPHONY_SERVICE);

        try {

            Class<?> telephonyClass = Class.forName(telephony.getClass().getName());

            Class<?>[] parameter = new Class[1];
            parameter[0] = int.class;
            java.lang.reflect.Method getSimStateGemini = telephonyClass.getMethod(predictedMethodName, parameter);

            Object[] obParameter = new Object[1];
            obParameter[0] = slotID;
            Object ob_phone = getSimStateGemini.invoke(telephony, obParameter);

            if (ob_phone != null) {
                int simState = Integer.parseInt(ob_phone.toString());
                if (simState == android.telephony.TelephonyManager.SIM_STATE_READY) {
                    isReady = true;
                }
            }
        } catch (Exception e) {
            //e.printStackTrace();
            throw new com.telpo.pospay.main.util.CTelephoneInfo.GeminiMethodNotFoundException(predictedMethodName);
        }

        return isReady;
    }

    private static class GeminiMethodNotFoundException extends Exception {
        /**
         *
         */
        private static final long serialVersionUID = -3241033488141442594L;

        public GeminiMethodNotFoundException(String info) {
            super(info);
        }
    }
}