package com.telpo.pospay.main.util;


import com.telpo.emv.EmvApp;
import com.telpo.emv.EmvCAPK;
import com.telpo.emv.util.ByteArrayUtil;
import com.telpo.emv.util.StringUtil;
import com.telpo.emv.util.TLVData;


import java.io.UnsupportedEncodingException;

/**
 * Created by yemiekai on 2016/9/30 0030.
 */

public class DataExchange {


    static public void DBtoAID(com.telpo.pospay.db.bean.AIDDB aiddb, com.telpo.emv.EmvApp emvapp) {
        try {
            if (aiddb.getAppName() == null || aiddb.getAppName().equals("")) {
                emvapp.AppName = new byte[32];
            } else {
                emvapp.AppName = aiddb.getAppName().getBytes("ascii");
            }

            if (aiddb.getAID() == null || aiddb.getAID().equals("")) {
                emvapp.AID = new byte[16];
            } else {
                emvapp.AID = com.telpo.emv.util.StringUtil.hexStringToByte(aiddb.getAID());
            }


            if (aiddb.getTerminalFloorLimit() == null || aiddb.getTerminalFloorLimit().equals("")) {
                emvapp.FloorLimit = new byte[6];
            } else {
                emvapp.FloorLimit = com.telpo.emv.util.StringUtil.hexStringToByte(aiddb.getTerminalFloorLimit());
            }

            if (aiddb.getRandSelectThreshold() == null || aiddb.getRandSelectThreshold().equals("")) {
                emvapp.Threshold = new byte[6];
            } else {
                emvapp.Threshold = com.telpo.emv.util.StringUtil.hexStringToByte(aiddb.getRandSelectThreshold());
            }

            if (aiddb.getTACDenial() == null || aiddb.getTACDenial().equals("")) {
                emvapp.TACDenial = new byte[6];
            } else {
                emvapp.TACDenial = com.telpo.emv.util.StringUtil.hexStringToByte(aiddb.getTACDenial());
            }

            if (aiddb.getTACOnline() == null || aiddb.getTACOnline().equals("")) {
                emvapp.TACOnline = new byte[6];
            } else {
                emvapp.TACOnline = com.telpo.emv.util.StringUtil.hexStringToByte(aiddb.getTACOnline());
            }

            if (aiddb.getTACDefault() == null || aiddb.getTACDefault().equals("")) {
                emvapp.TACDefault = new byte[6];
            } else {
                emvapp.TACDefault = com.telpo.emv.util.StringUtil.hexStringToByte(aiddb.getTACDefault());
            }

            if (aiddb.getAcquirerID() == null || aiddb.getAcquirerID().equals("")) {
                emvapp.AcquierId = new byte[6];
            } else {
                emvapp.AcquierId = com.telpo.emv.util.StringUtil.hexStringToByte(aiddb.getAcquirerID());
            }

            if (aiddb.getDDOL() == null || aiddb.getDDOL().equals("")) {
                emvapp.DDOL = new byte[256];
            } else {
                emvapp.DDOL = com.telpo.emv.util.StringUtil.hexStringToByte(aiddb.getDDOL());
            }

            if (aiddb.getTDOL() == null || aiddb.getTDOL().equals("") || aiddb.getTDOL().length() == 0) {
                emvapp.TDOL = new byte[256];
            } else {
                emvapp.TDOL = com.telpo.emv.util.StringUtil.hexStringToByte(aiddb.getTDOL());
            }

            if (aiddb.getAPPVersion() == null || aiddb.getAPPVersion().equals("")) {
                emvapp.Version = new byte[2];
            } else {
                emvapp.Version = com.telpo.emv.util.StringUtil.hexStringToByte(aiddb.getAPPVersion());
            }

            if (aiddb.getTerminalRiskData() == null || aiddb.getTerminalRiskData().equals("")) {
                emvapp.RiskManData = new byte[10];
            } else {
                emvapp.RiskManData = com.telpo.emv.util.StringUtil.hexStringToByte(aiddb.getTerminalRiskData());
            }

            if (aiddb.getECLimit() == null || aiddb.getECLimit().equals("")) {
                emvapp.EC_TermLimit = new byte[6];
            } else {
                emvapp.EC_TermLimit = com.telpo.emv.util.StringUtil.hexStringToByte(aiddb.getECLimit());
            }

            if (aiddb.getNFCLimit() == null || aiddb.getNFCLimit().equals("")) {
                emvapp.CL_TransLimit = new byte[6];
            } else {
                emvapp.CL_TransLimit = com.telpo.emv.util.StringUtil.hexStringToByte(aiddb.getNFCLimit());
            }

            if (aiddb.getNFCCVMLimit() == null || aiddb.getNFCCVMLimit().equals("")) {
                emvapp.CL_CVMLimit = new byte[6];
            } else {
                emvapp.CL_CVMLimit = com.telpo.emv.util.StringUtil.hexStringToByte(aiddb.getNFCLimit());
            }

            if (com.telpo.emv.util.StringUtil.hexStringToByte(aiddb.getSelectFlag()) != null) {
                emvapp.SelFlag = com.telpo.emv.util.StringUtil.hexStringToByte(aiddb.getSelectFlag())[0];
            }
            if (com.telpo.emv.util.StringUtil.hexStringToByte(aiddb.getRandSelectTargetPercent()) != null) {
                emvapp.TargetPer = com.telpo.emv.util.StringUtil.hexStringToByte(aiddb.getRandSelectTargetPercent())[0];
            }
            if (com.telpo.emv.util.StringUtil.hexStringToByte(aiddb.getRandSelectMAXPercent()) != null) {
                emvapp.MaxTargetPer = com.telpo.emv.util.StringUtil.hexStringToByte(aiddb.getRandSelectMAXPercent())[0];
            }

            //emvapp.Priority = aiddb.Priority;

            emvapp.FloorLimitCheck = 1;
            emvapp.RandTransSel = 1;
            emvapp.VelocityCheck = 1;
            emvapp.bOnlinePin = 1;
            emvapp.EC_bTermLimitCheck = 1;
            emvapp.CL_bStatusCheck = 1;


        } catch (java.io.UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    static public void DBtoCAPK(com.telpo.pospay.db.bean.CAPKDB capkdb, com.telpo.emv.EmvCAPK emvcapk) {
        if (capkdb.getRID() != null && capkdb.getRID().length() > 0) {
            emvcapk.RID = com.telpo.emv.util.StringUtil.hexStringToByte(capkdb.getRID());
        } else {
            emvcapk.RID = new byte[5];
        }
        if (com.telpo.emv.util.StringUtil.hexStringToByte(capkdb.getKeyID()) != null) {
            emvcapk.KeyID = com.telpo.emv.util.StringUtil.hexStringToByte(capkdb.getKeyID())[0];
        }
        if (com.telpo.emv.util.StringUtil.hexStringToByte(capkdb.getHashInd()) != null) {
            emvcapk.HashInd = com.telpo.emv.util.StringUtil.hexStringToByte(capkdb.getHashInd())[0];
        }
        if (com.telpo.emv.util.StringUtil.hexStringToByte(capkdb.getArithInd()) != null) {
            emvcapk.ArithInd = com.telpo.emv.util.StringUtil.hexStringToByte(capkdb.getArithInd())[0];
        }

        if (capkdb.getModul() != null && capkdb.getModul().length() > 0) {
            emvcapk.Modul = com.telpo.emv.util.StringUtil.hexStringToByte(capkdb.getModul());
        } else {
            emvcapk.Modul = new byte[248];
        }

        if (capkdb.getExponent() != null && capkdb.getExponent().length() > 0) {
            emvcapk.Exponent = com.telpo.emv.util.StringUtil.hexStringToByte(capkdb.getExponent());
        } else {
            emvcapk.Exponent = new byte[3];
        }

        if (capkdb.getExpDate() != null && capkdb.getExpDate().length() > 0) {
            emvcapk.ExpDate = com.telpo.emv.util.StringUtil.hexStringToByte(capkdb.getExpDate());
            emvcapk.ExpDate = com.telpo.emv.util.ByteArrayUtil.byteArrayTrimHead(emvcapk.ExpDate, 1);

        } else {
            emvcapk.ExpDate = new byte[3];
        }

        if (capkdb.getCheckSum() != null && capkdb.getCheckSum().length() > 0) {
            emvcapk.CheckSum = com.telpo.emv.util.StringUtil.hexStringToByte(capkdb.getCheckSum());
        } else {
            emvcapk.CheckSum = new byte[20];
        }

    }

    /**
     * 从BCTC后台下载AID时, 解析出每个TLV时, 就把对应TAG的值保存到"(AIDDB1)db"的对应参数上
     *
     * @param tlvData
     * @param db
     */
    static public void putTLVToAIDDB(com.telpo.emv.util.TLVData tlvData, com.telpo.pospay.db.bean.AIDDB db) {
        if (tlvData == null || db == null) {
            return;
        }

        if (tlvData.Len == 0) {
            return;
        }

        switch (tlvData.Tag) {

            case 0x9F06://应用标识符
                db.setAID(com.telpo.emv.util.StringUtil.bytesToHexString(tlvData.Value));
                db.setLocalBOCVersion(db.getLatestBOCVersion());
                break;

            case 0xDF01://应用选择指示
                db.setSelectFlag(com.telpo.emv.util.StringUtil.bytesToHexString(tlvData.Value));
                break;

            case 0x9F09://应用版本
                db.setAPPVersion(com.telpo.emv.util.StringUtil.bytesToHexString(tlvData.Value));
                break;

            case 0xDF11://缺省TAC
                db.setTACDefault(com.telpo.emv.util.StringUtil.bytesToHexString(tlvData.Value));
                break;

            case 0xDF12://联机TAC
                db.setTACOnline(com.telpo.emv.util.StringUtil.bytesToHexString(tlvData.Value));
                break;

            case 0xDF13://拒绝TAC
                db.setTACDenial(com.telpo.emv.util.StringUtil.bytesToHexString(tlvData.Value));
                break;

            case 0x9F1B://终端最低限额
                db.setTerminalFloorLimit(com.telpo.emv.util.StringUtil.bytesToHexString(tlvData.Value));
                break;

            case 0xDF15://随机选择阈值
                db.setRandSelectThreshold(com.telpo.emv.util.StringUtil.bytesToHexString(tlvData.Value));
                break;

            case 0xDF16://随机选择最大百分比数
                db.setRandSelectMAXPercent(com.telpo.emv.util.StringUtil.bytesToHexString(tlvData.Value));
                break;

            case 0xDF17://随机选择目标百分比数
                db.setRandSelectTargetPercent(com.telpo.emv.util.StringUtil.bytesToHexString(tlvData.Value));
                break;

            case 0xDF18://AID应用标志参数
                db.setAIDProperty(com.telpo.emv.util.StringUtil.bytesToHexString(tlvData.Value));
                break;

            case 0xDF14://缺省动态数据认证数据对象列表
                db.setDDOL(com.telpo.emv.util.StringUtil.bytesToHexString(tlvData.Value));
                break;

            case 0x9F35://终端类型、终端分类码、终端级别
                db.setDDOL(com.telpo.emv.util.StringUtil.bytesToHexString(tlvData.Value));
                break;

            case 0x9F15://商户类别码
                db.setMerchantCategoryCode(com.telpo.emv.util.StringUtil.bytesToHexString(tlvData.Value));
                break;

            case 0xDF25://第03块参数版本号
                db.setLatestBOCVersion(com.telpo.emv.util.StringUtil.bytesToHexString(tlvData.Value));
                break;

            case 0x9F7B://终端电子现金交易限额
                db.setECLimit(com.telpo.emv.util.StringUtil.bytesToHexString(tlvData.Value));
                break;

            case 0xDF40://非接触读卡器脱机限额
                db.setNFCOfflineLimit(com.telpo.emv.util.StringUtil.bytesToHexString(tlvData.Value));
                break;

            case 0xDF20://非接触读卡器限额
                db.setNFCLimit(com.telpo.emv.util.StringUtil.bytesToHexString(tlvData.Value));
                break;

            case 0xDF21://非接触读卡器持卡人验证限额
                db.setNFCCVMLimit(com.telpo.emv.util.StringUtil.bytesToHexString(tlvData.Value));
                break;

            case 0xDF41://NFC TAC缺省
                db.setNFCTACDefault(com.telpo.emv.util.StringUtil.bytesToHexString(tlvData.Value));
                break;

            case 0xDF42://NFC TAC联机
                db.setNFCTACOnline(com.telpo.emv.util.StringUtil.bytesToHexString(tlvData.Value));
                break;

            case 0xDF43://NFC TAC拒绝
                db.setNFCTACDenial(com.telpo.emv.util.StringUtil.bytesToHexString(tlvData.Value));
                break;

            case 0xDF69://国密算法指示
                db.setAlgorithmFlag(com.telpo.emv.util.StringUtil.bytesToHexString(tlvData.Value));
                break;

            case 0x9F16://商户标识
                db.setMerchantID(com.telpo.emv.util.StringUtil.bytesToHexString(tlvData.Value));
                break;

            case 0x9F4E://商户名称
                db.setMerchantName(com.telpo.emv.util.StringUtil.bytesToHexString(tlvData.Value));
                break;

            case 0x9F1C://终端标识(终端号)
                db.setTerminalID(com.telpo.emv.util.StringUtil.bytesToHexString(tlvData.Value));
                break;

            case 0x9F1D://终端风险数据管理
                db.setTerminalRiskData(com.telpo.emv.util.StringUtil.bytesToHexString(tlvData.Value));
                break;

            case 0x5F36://参考货币代码
                db.setTransCurrencyExp(com.telpo.emv.util.StringUtil.bytesToHexString(tlvData.Value));
                break;

            case 0x9F3C://参考货币代码
                db.setReferCurrencyCode(com.telpo.emv.util.StringUtil.bytesToHexString(tlvData.Value));
                break;

            case 0xDF8101://参考货币转换码
                db.setReferCurrencyTransCode(com.telpo.emv.util.StringUtil.bytesToHexString(tlvData.Value));
                break;

            case 0x9F3D://参考货币指数
                db.setReferCurrencyExp(com.telpo.emv.util.StringUtil.bytesToHexString(tlvData.Value));
                break;

            case 0xDF8102://缺省交易证书数据对象列表
                db.setTDOL(com.telpo.emv.util.StringUtil.bytesToHexString(tlvData.Value));
                break;

            case 0x5F2A://货币代码
                db.setCurrencyCode(com.telpo.emv.util.StringUtil.bytesToHexString(tlvData.Value));
                break;

            case 0x9F01://收单行标识
                db.setAcquirerID(com.telpo.emv.util.StringUtil.bytesToHexString(tlvData.Value));
                break;
            default:
                break;
        }
    }


    /**
     * 从BCTC后台下载CAPK时, 解析出每个TLV时, 就把对应TAG的值保存到"(CAPKDB1)db"的对应参数上
     *
     * @param tlvData
     * @param db
     */
    static public void putTLVToCAPKDB(com.telpo.emv.util.TLVData tlvData, com.telpo.pospay.db.bean.CAPKDB db) {
        if (tlvData == null || db == null) {
            return;
        }

        if (tlvData.Len == 0) {
            return;
        }

        switch (tlvData.Tag) {
            case 0x9F06:
                db.setRID(com.telpo.emv.util.StringUtil.bytesToHexString(tlvData.Value));
                break;

            case 0x9F22:
                db.setKeyID(com.telpo.emv.util.StringUtil.bytesToHexString(tlvData.Value));
                break;

            case 0xDF05:
                db.setExpDate(com.telpo.emv.util.StringUtil.bytesToHexString(tlvData.Value));
                db.setLatestBOCVersion(com.telpo.emv.util.StringUtil.bytesToHexString(tlvData.Value));
                break;

            case 0xDF06:
                db.setHashInd(com.telpo.emv.util.StringUtil.bytesToHexString(tlvData.Value));
                break;

            case 0xDF07:
                db.setArithInd(com.telpo.emv.util.StringUtil.bytesToHexString(tlvData.Value));
                break;

            case 0xDF02:
                db.setModul(com.telpo.emv.util.StringUtil.bytesToHexString(tlvData.Value));
                break;

            case 0xDF04:
                db.setExponent(com.telpo.emv.util.StringUtil.bytesToHexString(tlvData.Value));
                break;

            case 0xDF03:
                db.setCheckSum(com.telpo.emv.util.StringUtil.bytesToHexString(tlvData.Value));
                break;

            default:
                break;
        }
    }
//
//    /**
//     * 从BCTC后台下载黑名单时, 解析出每个TLV时, 就把对应TAG的值保存到"(BlackListDB)db"的对应参数上
//     * @param tlvData
//     * @param db
//     */
//    static public void putTLVToBlackListDB(TLVData tlvData, BlackListDB db) {
//        if(tlvData==null || db==null){
//            return;
//        }
//
//        if(tlvData.Len == 0){
//            return;
//        }
//        switch (tlvData.Tag){
//            case 0x5A://主账号
//                db.blackNumber = StringUtil.bytesToHexString(tlvData.Value);
//                break;
//
//            case 0x5F34://索引
//                db.blackIndex = StringUtil.bytesToHexString(tlvData.Value);
//                break;
//
//            default:
//                break;
//        }
//
//    }
}
