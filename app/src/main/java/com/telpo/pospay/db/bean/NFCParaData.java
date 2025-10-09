package com.telpo.pospay.db.bean;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Telpo on 2018/1/8.
 */

public class NFCParaData {
    private String nfcPrioritySwitch;       //非接优先开关FFF001  0-关闭非接优先   1-打开非接优先
    private String nfcDealSwitch;            // 非接交易通道开关 0-优先联机借贷记  1-优先电子现金 N1 FF805D
    private String reDoTime;                 //闪卡当笔重刷处理时间  N3	FF803A 	默认值10秒
    private String recordTime;               //闪卡记录可处理时间  N3	 FF803C	默认值60秒
    private String QPSNoPWDLimitAmount;      //非接快速业务（QPS）免密限额  N12	FF8058 	终端使用此数据元作为条件之一判断非接联机交易是否请求持卡人验证方法，默认值300元，精确至小数点后两位
    private String QPSIdentify;              //非接快速业务标识 N1	 FF8054	终端使用此数据元作为是否开启非接快速功能的判断条件。
    //1-启用
    //0-关闭

    private String BinListAIdentify;         //BIN表A标识 N1	FF8055	终端使用此数据元作为是否将BIN表A作为免密的判断条件，启用该标识意味着非接快速业务处于试点阶段。
    //1-启用
    //0-关闭

    private String BinListBIdentify;         //BIN表B标识 N1	 FF8056	在终端启用此数据元意味着非接快速业务试点结束，但仍处于试点结束后的初期阶段，即贷记卡实现全面支持，但此时境内借记卡尚未实现全面支持，借记卡依然根据BIN表判断
    // 1-启用
    // 0-关闭
    public String CDCVMIdentify;             //CDCVM标识  N1	FF8057 	终端使用此数据元作为是否将卡片CDCVM执行情况作为免密的判断条件。1-启用 0-关闭
    private String QPSNoSignLimitAmount;      //免签限额 N12	FF8059	终端使用此数据元作为判断交易凭证是否需要进行免签处理默认值为300元，精确至小数点后两位
    private String NoSignIdentify;            //     终端使用此数据元作为是否支持交易凭证免签处理的判断条件1，	启用0，关闭    N1	FF805A
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private static NFCParaData nfcParaData = null;
    private Context context = null;

    public static NFCParaData getInstance(Context context) {
        if (nfcParaData == null) {
            nfcParaData = new NFCParaData(context);
        }
        return nfcParaData;
    }

//    public static NFCParaData getInstance(Context context) {
//        if (nfcParaData == null) {
//            nfcParaData = new NFCParaData(context);
//        }
//        return nfcParaData;
//    }

    private NFCParaData(Context context) {
        this.context = context;
        sp = context.getSharedPreferences("NFCParaData", Context.MODE_PRIVATE);
        editor = sp.edit();
    }
//    private NFCParaData() {
//        sp = context.getSharedPreferences("NFCParaData", Context.MODE_PRIVATE);
//        editor = sp.edit();
//    }

    public String getNfcPrioritySwitch() {
        return sp.getString("nfcPrioritySwitch", "0");
    }

    public String getNfcDealSwitch() {
        return sp.getString("nfcDealSwitch", "0");
    }

    public String getReDoTime() {
        return sp.getString("reDoTime", "10");
    }

    public String getRecordTime() {
        return sp.getString("recordTime", "60");
    }

    public String getQPSNoPWDLimitAmount() {
        String str = sp.getString("QPSNoPWDLimitAmount", "30000");
        while (str.length() < 3) {
            str = "0" + str;
        }
        return str;
    }

    public String getQPSIdentify() {
        return sp.getString("QPSIdentify", "0");
    }

    public String getBinListAIdentify() {
        return sp.getString("BinListAIdentify", "0");
    }

    public String getBinListBIdentify() {
        return sp.getString("BinListBIdentify", "0");
    }

    public String getCDCVMIdentify() {
        return sp.getString("CDCVMIdentify", "0");
    }

    public String getQPSNoSignLimitAmount() {
        String str = sp.getString("QPSNoSignLimitAmount", "30000");
        while (str.length() < 3) {
            str = "0" + str;
        }
        return str;
    }

    public String getNoSignIdentify() {
        return sp.getString("NoSignIdentify", "0");
    }

    public void setNfcPrioritySwitch(String nfcPrioritySwitch) {
        editor.putString("nfcPrioritySwitch", nfcPrioritySwitch);
        editor.commit();
    }

    public void setNfcDealSwitch(String nfcDealSwitch) {
        editor.putString("nfcDealSwitch", nfcDealSwitch);
        editor.commit();
    }

    public void setReDoTime(String reDoTime) {
        editor.putString("reDoTime", reDoTime);
        editor.commit();
    }

    public void setRecordTime(String recordTime) {
        editor.putString("recordTime", recordTime);
        editor.commit();
    }

    public void setQPSNoPWDLimitAmount(String QPSNoPWDLimitAmount) {
        editor.putString("QPSNoPWDLimitAmount", QPSNoPWDLimitAmount);
        editor.commit();
    }

    public void setQPSIdentify(String QPSIdentify) {
        editor.putString("QPSIdentify", QPSIdentify);
        editor.commit();
    }

    public void setBinListAIdentify(String binListAIdentify) {
        editor.putString("BinListAIdentify", binListAIdentify);
        editor.commit();
    }

    public void setBinListBIdentify(String binListBIdentify) {
        editor.putString("BinListBIdentify", binListBIdentify);
        editor.commit();
    }

    public void setCDCVMIdentify(String CDCVMIdentify) {
        editor.putString("CDCVMIdentify", CDCVMIdentify);
        editor.commit();
    }

    public void setQPSNoSignLimitAmount(String QPSNoSignLimitAmount) {
        editor.putString("QPSNoSignLimitAmount", QPSNoSignLimitAmount);
        editor.commit();
    }

    public void setNoSignIdentify(String noSignIdentify) {
        editor.putString("NoSignIdentify", noSignIdentify);
        editor.commit();
    }

    @Override
    public String toString() {
        return "NFCParaData{" + '\n' +
                "nfcPrioritySwitch='" + getNfcPrioritySwitch() + '\n' +
                ", nfcDealSwitch='" + getNfcDealSwitch() + '\n' +
                ", reDoTime='" + getReDoTime() + '\n' +
                ", recordTime='" + getRecordTime() + '\n' +
                ", QPSNoPWDLimitAmount='" + getQPSNoPWDLimitAmount() + '\n' +
                ", QPSIdentify='" + getQPSIdentify() + '\n' +
                ", BinListAIdentify='" + getBinListAIdentify() + '\n' +
                ", BinListBIdentify='" + getBinListBIdentify() + '\n' +
                ", CDCVMIdentify='" + getCDCVMIdentify() + '\n' +
                ", QPSNoSignLimitAmount='" + getQPSNoSignLimitAmount() + '\n' +
                ", NoSignIdentify='" + getNoSignIdentify() + '\n' +
                '}';
    }
}
