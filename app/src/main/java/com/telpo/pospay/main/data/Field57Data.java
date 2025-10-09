package com.telpo.pospay.main.data;

import android.util.Log;

import com.telpo.emv.util.DataProcessUtil;
import com.telpo.emv.util.StringUtil;

//import com.telpo.emv.protocol.SMS4;

/**
 * Created by xuxl150808 on 2017/6/16.
 */

public class Field57Data {

    public byte[] outdata = new byte[512];


    public String prepareField57(POSData posData) {
        String ret = "";
        if (posData.field53.charAt(1) != '3') {
            ret += get_TLV_A1();//当存在交易密码，且53.2 域加密算法标识为SM4 算法时，该域上送，为国密算法PIN 的加密密文
        }
        ret += get_TLV_A2(posData);

        //扫码支付才需增加A3、A4
//        ret += get_TLV_A3();
//        ret += get_TLV_A4();
        return ret;
    }

    private String get_TLV_A0Child(String t, String v) {
        String ret = "";
        com.telpo.pospay.main.data.Field57Data.Field57TLVData tlv = new com.telpo.pospay.main.data.Field57Data.Field57TLVData();
        tlv.setTag(t);
        tlv.setLen(v.length());
        tlv.setVaule(v.getBytes());
        ret = tlv.tlvData2HexString(tlv);
        return ret;
    }

    public String get_TLV_A0() {
        String ret = "";
        String tag = "A0";

        ret += get_TLV_A0Child("01", "1");
        ret += get_TLV_A0Child("02", "1");
        ret += get_TLV_A0Child("03", "0");
        ret += get_TLV_A0Child("04", "1");
        ret += get_TLV_A0Child("05", "1");
        ret += get_TLV_A0Child("06", "0");
        ret += get_TLV_A0Child("07", "819916");

        com.telpo.pospay.main.data.Field57Data.Field57TLVData tlvData = new com.telpo.pospay.main.data.Field57Data.Field57TLVData();
        tlvData.setTag(tag);
        tlvData.setLen(com.telpo.emv.util.DataProcessUtil.hexStringToByte(ret).length);
        tlvData.setVaule(com.telpo.emv.util.DataProcessUtil.hexStringToByte(ret));
        ret = tlvData.tlvData2HexString(tlvData);

        return ret;
    }

    private String get_TLV_A1() {
        String ret = "";
        String tag = "A1";
        int len = 16;
        byte[] vaule = new byte[16];

        com.telpo.pospay.main.data.Field57Data.Field57TLVData tlvData = new com.telpo.pospay.main.data.Field57Data.Field57TLVData();
        tlvData.setTag(tag);
        tlvData.setLen(len);
        tlvData.setVaule(vaule);
        ret = tlvData.tlvData2HexString(tlvData);

        return ret;
    }

    public String get_TLV_A2(POSData posData) {
        String ret = "";
        String tag = "A2";
        com.telpo.pospay.main.data.Field57Data.TlvA2 tlvA2 = new com.telpo.pospay.main.data.Field57Data.TlvA2();
        int len = tlvA2.getTlvA2Child(posData).length();
        byte[] vaule = tlvA2.getTlvA2Child(posData).getBytes();

        com.telpo.pospay.main.data.Field57Data.Field57TLVData tlvData = new com.telpo.pospay.main.data.Field57Data.Field57TLVData();
        tlvData.setTag(tag);
        tlvData.setLen(len);
        tlvData.setVaule(vaule);
        ret = tlvData.tlvData2HexString(tlvData);

        return ret;
    }

    private String get_TLV_A3() {
        String ret = "";
        String tag = "A3";
        int len = 16;
        byte[] vaule = new byte[16];

        com.telpo.pospay.main.data.Field57Data.Field57TLVData tlvData = new com.telpo.pospay.main.data.Field57Data.Field57TLVData();
        tlvData.setTag(tag);
        tlvData.setLen(len);
        tlvData.setVaule(vaule);
        ret = tlvData.tlvData2HexString(tlvData);

        return ret;
    }

    private String get_TLV_A4() {
        String ret = "";
        String tag = "A4";
        int len = 16;
        byte[] vaule = new byte[16];

        com.telpo.pospay.main.data.Field57Data.Field57TLVData tlvData = new com.telpo.pospay.main.data.Field57Data.Field57TLVData();
        tlvData.setTag(tag);
        tlvData.setLen(len);
        tlvData.setVaule(vaule);
        ret = tlvData.tlvData2HexString(tlvData);

        return ret;
    }

    class Field57TLVData {
        private String tag;
        private int len;
        private byte[] vaule;

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public int getLen() {
            return len;
        }

        public void setLen(int len) {
            this.len = len;
        }

        public byte[] getVaule() {
            return vaule;
        }

        public void setVaule(byte[] vaule) {
            this.vaule = vaule;
        }

        public String tlvData2HexString(com.telpo.pospay.main.data.Field57Data.Field57TLVData tlvData) {
            byte[] tlv = new byte[2 + 3 + tlvData.len];
            System.arraycopy(tlvData.tag.getBytes(), 0, tlv, 0, 2);
            String lenStr = tlvData.len + "";
            for (; lenStr.length() < 3; ) {
                lenStr = "0" + lenStr;
            }
            System.arraycopy(lenStr.getBytes(), 0, tlv, 2, 3);
            System.arraycopy(vaule, 0, tlv, 5, len);

            return com.telpo.emv.util.StringUtil.bytesToHexString(tlv);
        }
    }

    class TlvA2 extends com.telpo.pospay.main.data.Field57Data.Field57TLVData {

        private String getTlvA2Child(POSData posData) {
            String ret = "";
            ret += get_TlvA2_01();
            //ret += get_TlvA2_02();
//            String last6pan = posData.sPAN.substring(posData.sPAN.length() - 6, posData.sPAN.length());
            String last6pan = "123456";
            ret += get_TlvA2_03(last6pan);
            ret += get_TlvA2_04(last6pan);
            ret += get_TlvA2_05();
            return ret;
        }

        private String get_TlvA2_01() {
            String ret = "";
            String tag = "01";
            int len = 2;
            byte[] vaule = "04".getBytes();

            com.telpo.pospay.main.data.Field57Data.TlvA2 tlvData = new com.telpo.pospay.main.data.Field57Data.TlvA2();
            tlvData.setTag(tag);
            tlvData.setLen(len);
            tlvData.setVaule(vaule);
            ret = tlvData.tlvData2HexString(tlvData);

            android.util.Log.i("xuxl", "01:  " + ret);

            return ret;
        }

//        private String get_TlvA2_02(Context context) {
//            String ret = "";
//            String tag = "02";
//
//            String productNo = "000038";//6位厂商号
//            String deviceNo = "04";//2位设备类型
//
//            CTelephoneInfo cTelephoneInfo = CTelephoneInfo.getInstance(context);
//            cTelephoneInfo.setCTelephoneInfo();
//            GlobalParams.Log("IMEI1 "+cTelephoneInfo.getImeiSIM1());
//            GlobalParams.Log("IMEI2 "+ cTelephoneInfo.getImeiSIM2());
//            String strIMEI2 = cTelephoneInfo.getImeiSIM1();
//            String strIMEI1 = cTelephoneInfo.getImeiSIM2();
//            String seriel;
//            if (strIMEI1.compareTo(strIMEI2)==0 ){
//                seriel = strIMEI1;
//            }else if (strIMEI1.compareTo(strIMEI2)<0){
//                seriel = strIMEI1 + strIMEI2;
//            }else {
//                seriel = strIMEI2 + strIMEI1;
//            }
//            if (TextUtils.isEmpty(strIMEI1)||strIMEI1.length()<strIMEI2.length()){
//                seriel = strIMEI2;
//            }
//            if (TextUtils.isEmpty(strIMEI2)||strIMEI2.length()<strIMEI1.length()){
//                seriel = strIMEI1;
//            }
//            GlobalParams.Log("SN "+ seriel);
//            int len = (productNo + deviceNo + seriel).length();
//            byte[] vaule = (productNo + deviceNo + seriel).getBytes();
//
//
//            TlvA2 tlvData = new TlvA2();
//            tlvData.setTag(tag);
//            tlvData.setLen(len);
//            tlvData.setVaule(vaule);
//            ret = tlvData.tlvData2HexString(tlvData);
//            Log.i("xuxl", "02:  " + ret);
//            return ret;
//        }

        //传入卡号的后6位
        private String get_TlvA2_03(String last6CardNo) {
            String ret = "";
            String tag = "03";
            int len = last6CardNo.length();
            byte[] vaule = last6CardNo.getBytes();

            com.telpo.pospay.main.data.Field57Data.TlvA2 tlvData = new com.telpo.pospay.main.data.Field57Data.TlvA2();
            tlvData.setTag(tag);
            tlvData.setLen(len);
            tlvData.setVaule(vaule);
            ret = tlvData.tlvData2HexString(tlvData);
            android.util.Log.i("xuxl", "03:  " + ret);
            return ret;
        }

        private String get_TlvA2_04(String last6CardNo) {
            String ret = "";
            String tag = "04";
            int len = 8;
            byte[] vaule = new byte[8];

            String MAB = com.telpo.pospay.main.util.TPPOSUtil.getSN() + last6CardNo;
            byte[] data = MAB.getBytes();
            android.util.Log.i("xuxl", "MAB：" + com.telpo.emv.util.DataProcessUtil.bytesToHexString(data));
            String xorResult = XorUtil(data);
            try {
                byte[] key = {0x01, 0x23, 0x45, 0x67, (byte) 0x89, (byte) 0xab, (byte) 0xcd, (byte) 0xef, (byte) 0xfe, (byte) 0xdc, (byte) 0xba, (byte) 0x98, 0x76, 0x54, 0x32, 0x10};
//                SMS4 sm4 = new SMS4();
                int inLen = 16, ENCRYPT = 1, DECRYPT = 0;
                byte[] out = new byte[16];

//                sm4.sms4(xorResult.substring(0, 16).getBytes(), inLen, key, out, ENCRYPT);

                android.util.Log.i("xuxl", "前16字节SM4加密密文：" + com.telpo.emv.util.DataProcessUtil.bytesToHexString(out));
                byte[] temp = XOR(out, xorResult.substring(16, 32).getBytes());
                android.util.Log.i("xuxl", "前16字节SM4加密密文与后16字节异或结果：" + com.telpo.emv.util.DataProcessUtil.bytesToHexString(temp));
//                sm4.sms4(temp, inLen, key, out, ENCRYPT);
                android.util.Log.i("xuxl", "异或结果结果SM4加密密文：" + com.telpo.emv.util.DataProcessUtil.bytesToHexString(out));
                vaule = com.telpo.emv.util.DataProcessUtil.bytesToHexString(out).substring(0, 8).getBytes();
            } catch (Exception e) {
                android.util.Log.i("xuxl", e.getMessage());
            }


            com.telpo.pospay.main.data.Field57Data.TlvA2 tlvData = new com.telpo.pospay.main.data.Field57Data.TlvA2();
            tlvData.setTag(tag);
            tlvData.setLen(len);
            tlvData.setVaule(vaule);
            ret = tlvData.tlvData2HexString(tlvData);
            android.util.Log.i("xuxl", "04:  " + ret);
            return ret;
        }

        private String get_TlvA2_05() {
            String ret = "";
            String tag = "05";
            int len = 8;
            byte[] vaule = new byte[8];

            com.telpo.pospay.main.data.Field57Data.TlvA2 tlvData = new com.telpo.pospay.main.data.Field57Data.TlvA2();
            tlvData.setTag(tag);
            tlvData.setLen(len);
            tlvData.setVaule("00000001".getBytes());
            ret = tlvData.tlvData2HexString(tlvData);

            android.util.Log.i("xuxl", "05:  " + ret);
            return ret;
        }

    }

    private String XorUtil(byte[] data) {
        String result = "";
        int group = (data.length + (16 - 1)) / 16;
        // 偏移量
        int offset = 0;
        // 输入计算数据
        byte[] edata = new byte[16];
        for (int i = 0; i < group; i++) {
            byte[] temp = new byte[16];
            if (i != group - 1) {
                System.arraycopy(data, offset, temp, 0, 16);
                offset += 16;
            } else {// 只有最后一组数据才进行填充0x00
                System.arraycopy(data, offset, temp, 0, data.length - offset);
            }

            if (i != 0) {// 只有第一次不做异或
                temp = XOR(edata, temp);
            }
            System.arraycopy(temp, 0, edata, 0, 16);
            result = com.telpo.emv.util.DataProcessUtil.bytesToHexString(edata);
            android.util.Log.i("xuxl", "异或结果：" + result);
        }

        return result;
    }

    public byte[] XOR(byte[] edata, byte[] temp) {
        byte[] result = new byte[16];
        for (int i = 0, j = result.length; i < j; i++) {
            result[i] = (byte) (edata[i] ^ temp[i]);
        }
        return result;
    }
}
