package com.telpo.pospay.main.util;

import static com.telpo.emv.util.ByteArrayUtil.byteArrayTrimHead;
import static com.telpo.pospay.main.data.GlobalParams.key_isUpdateCapk;
import static com.telpo.pospay.main.data.GlobalParams.key_isUpdateAID;
import static com.telpo.pospay.main.util.StringUtil.Log;
import static com.telpo.pospay.main.util.TPPOSUtil.ERR_FIELD62;
import static com.telpo.pospay.main.util.TPPOSUtil.ERR_NOT_FINISHED;
import static com.telpo.pospay.main.util.TPPOSUtil.ERR_NO_FIELD62;
import static com.telpo.pospay.main.util.TPPOSUtil.ERR_NO_PARA;
import static com.telpo.pospay.main.util.TPPOSUtil.ERR_REDOWN_APP;
import static com.telpo.pospay.main.util.TPPOSUtil.ERR_REDOWN_CAPK;
import static com.telpo.pospay.main.util.TPPOSUtil.ERR_TRANS_FAILED;
import static com.telpo.pospay.main.util.TPPOSUtil.ERR_UNKNOW;
import static com.telpo.pospay.main.util.TPPOSUtil.ERR_UNPACK;
import static com.telpo.pospay.main.util.TPPOSUtil.OK_SUCCESS;
import static com.telpo.pospay.main.util.TPPOSUtil.TYPE_SIGN_IN;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import com.telpo.base.util.ByteArrayUtil;
import com.telpo.emv.EmvApp;
import com.telpo.pospay.db.bean.AIDDB;
import com.telpo.pospay.db.bean.NFCBlackListDB;
import com.telpo.pospay.db.bean.NFCParaData;
import com.telpo.pospay.main.data.GlobalParams;
import com.telpo.pospay.main.data.POSData;
import com.telpo.pospay.main.data.PackData;
import com.telpo.emv.util.StringUtil;
import com.telpo.emv.util.TLVData;
import com.telpo.emv.EmvCAPK;
import com.telpo.emv.EmvService;
import com.telpo.pospay.AppContext;
import com.telpo.pospay.db.bean.CAPKDB;
import com.telpo.base.util.MLog;

import android.content.Context;
import android.text.TextUtils;

import org.jetbrains.annotations.NotNull;

public class UnPackUtil {

    /**
     * 解包报文: CAPK应用参数版本查询
     *
     * @param context
     * @param packData 根据响应报文，更新CAPK
     * @return 出错时返回错误码。成功返回OK_SUCCESS，后续还有CAPK返回ERR_NOT_FINISHED，则还要继续发送查询报文。
     */
    public static int unpackMessage_QureyCAPKParaVer(Context context, POSData posData, PackData packData) {
        int ret = 0;
        String unpackResult;
        String responseCode;
        HashMap outMap = new HashMap();
        boolean bCheckMAC = false;
        try {
            outMap = ProtocolUtil.parse8583ReceiveMessage(packData.data_receive, 0x0830);
            outMap.put("tranType", posData.transType + "");
            // 打印输入HashMap内容
            MLog.i("接收数据：");
            StringUtil.printHashMapContent(outMap);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bCheckMAC = true;
            if (outMap == null) {
                return ERR_UNPACK;
            }

            //抽出数据,给应用参考交易信息,或打印小票
//            getTransactionDataFromOutMap(outMap, packData);

            unpackResult = (String) outMap.get("resultCode");
            packData.unPackCode = unpackResult;
            if (unpackResult.equals("995")) {
                return ERR_UNPACK;
            } else if (unpackResult.equals("994")) {
//            return TPPOSUtil.ERR_MAC_CHECK;//MAC校验错误,参数下载不校验MAC
            } else if (unpackResult.equals("999") || !unpackResult.equals("00")) {
                return ERR_UNKNOW;
            }
        }

        responseCode = (String) outMap.get("39");
        packData.responseCode = responseCode;
        MLog.i("平台应答码:" + responseCode);

        //判断应答码
        if (!responseCode.equals("00")) {
            //liguocai 直接返回失败，提示错误
            return ERR_TRANS_FAILED;
        }

        //处理CAPK版本
        //62域第一个字节为0：报文中没有公钥信息
        //62域第一个字节为1：报文中有公钥信息，且一个报文就放下了所有公钥信息。
        //62域第一个字节为2：报文中有公钥信息，且一个报文无法发下所有公钥信息。需要继续发送请求报文。
        //62域第一个字节为3：报文中有公钥信息，且是最后一组报文。
        //格式 一个字节 + RID1 +索引1+有效期1+ ...采用TLV格式
        String field62 = (String) outMap.get("62");
        if (field62 != null && field62.charAt(1) == '0') {
            return ERR_NO_PARA;
        } else if (field62 != null && field62.charAt(1) == '2') {
            ret = ERR_NOT_FINISHED;
        }
        if (field62 != null && !field62.equals(""))
            if (field62 != null && !field62.equals("")) {
                MLog.i("field62 ："+ field62.substring(2, field62.length()));
                byte[] field62Array = StringUtil.hexStringToByte(field62.substring(2, field62.length()));
                Boolean isSucc = updateCAPK(field62Array, context);
                if(!isSucc){
                    return ERR_FIELD62;
                }
            } else {
                return ERR_NO_FIELD62;
            }

        return ret;
    }


    /**
     * 更新CAPK的版本号,更新到CAPK数据库
     * DF24的内容是关于CAPK的列表，列表的元素是RID(9F06)和它的版本号(DF25)
     *
     * @param DF24
     * @param context
     */
    public static Boolean updateCAPK(byte[] DF24, Context context) {
        if (DF24 == null || DF24.length == 0) {
            MLog.i("update CAPK 【start】... 数据为空，直接返回");
            return false;
        }
        byte[] tmp = DF24;
        TLVData tag9F06 = new TLVData();
        TLVData tagDF05 = new TLVData();
        TLVData tagDF22 = new TLVData();

//        CAPKDBDao capkdbDao = new CAPKDBDao(context);


        MLog.i("update CAPK 【start】...");
        try {
            while (tmp != null && tmp.length > 0) {
                int len9F06 = TLVData.PraseTLV(tmp, tag9F06);
                if (len9F06 <= 0 || tag9F06.Value == null) {
                    MLog.i("解析9F06失败，长度=" + len9F06);
                    return false;
                }
                tmp = byteArrayTrimHead(tmp, tag9F06.WholeLen);

                int lenDF22 = TLVData.PraseTLV(tmp, tagDF22);
                if (lenDF22 <= 0 || tagDF22.Value == null) {
                    MLog.i("解析DF22失败，长度=" + lenDF22);
                    return false;
                }
                tmp = byteArrayTrimHead(tmp, tagDF22.WholeLen);

                int lenDF05 = TLVData.PraseTLV(tmp, tagDF05);
                if (lenDF05 <= 0 || tagDF05.Value == null) {
                    MLog.i("解析DF05失败，长度=" + lenDF05);
                    return false;
                }
                tmp = byteArrayTrimHead(tmp, tagDF05.WholeLen);


                MLog.i("9F06:\n" + tag9F06.toString());
                MLog.i("9F22:\n" + tagDF22.toString());
                MLog.i("DF25:\n" + tagDF05.toString() + "\n\n");

                String sAID = StringUtil.bytesToHexString(tag9F06.Value);
                String sVersion = StringUtil.bytesToHexString(tagDF05.Value);
                String sKeyId = StringUtil.bytesToHexString(tagDF22.Value);

                //更新
//            List<CAPKDB> capkdbs = capkdbDao.findAllByField("CAPKID", sAID + sKeyId);
                List<CAPKDB> capkdbs = AppContext.Companion.getInstance().getCapkRepository().findAllByField("CAPKID", sAID + sKeyId);
                if (capkdbs == null || capkdbs.size() == 0) {
                    MLog.i("RID: " + sAID + "  【Not found】... create:");
                    CAPKDB capkdb = new CAPKDB();
                    capkdb.setRID(sAID);
                    capkdb.setKeyID(sKeyId);
                    capkdb.setCAPKID(capkdb.getRID() + capkdb.getKeyID());
                    capkdb.setLatestBOCVersion(sVersion);
                    capkdb.setExpDate(sVersion);
                    capkdb.setBPOSPExist(true);
                    capkdb.setLocalBOCVersion("");
                    AppContext.Companion.getInstance().getCapkRepository().create(capkdb);
                    MLog.i("create CAPKDB: " + "\nRID:" + capkdb.getRID() + "\nkeyID" + capkdb.getKeyID());
                } else {
                    MLog.i("CAPKID: " + sAID + " " + sKeyId + "  【exist】...");
                    for (CAPKDB db : capkdbs) {
                        db.setLatestBOCVersion(sVersion);
                        AppContext.Companion.getInstance().getCapkRepository().update(db);
                    }
                }
            }

            MLog.i("update CAPK 【end】...");
            return true;
        } catch (Exception e) {
            MLog.i("updateCAPK 异常: " + e.getMessage());
            // 这里可以根据需要回滚数据库操作或清空本次已添加的数据
            return false;
        }
    }


    /**
     * 解包报文: 下载CAPK参数
     *
     * @param context
     * @param packData
     * @return
     */
    public static int unpackDownParaCAPKMessage(Context context, POSData posData, PackData packData) {
        HashMap outMap = new HashMap();
        String unpackResult;
        String responseCode;
        int ret = 0;
        GlobalParams.bCheckMAC = false;
        try {
            outMap = ProtocolUtil.parse8583ReceiveMessage(packData.data_receive, 0x0810);
            outMap.put("tranType", posData.transType + "");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outMap == null) {
                return ERR_UNPACK;
            }

            //抽出数据,给应用参考交易信息,或打印小票
//            getTransactionDataFromOutMap(outMap, packData);

            unpackResult = (String) outMap.get("resultCode");
            packData.unPackCode = unpackResult;
            if (unpackResult.equals("995")) {
                return ERR_UNPACK;
            } else if (unpackResult.equals("994")) {
//            return TPPOSUtil.ERR_MAC_CHECK;//MAC校验错误,参数下载不校验MAC
            } else if (unpackResult.equals("999") || !unpackResult.equals("00")) {
                return ERR_UNKNOW;
            }
        }

        responseCode = (String) outMap.get("39");
        packData.responseCode = responseCode;
        Log("平台应答码:" + responseCode);

        //判断应答码
        if (!responseCode.equals("00")) {
            //ligc 直接返回失败，提示错误
            return ERR_TRANS_FAILED;
        }

        String field62 = (String) outMap.get("62");
        if (field62 != null && !field62.equals("")) {
            if (field62.charAt(1) == '1') {
                //62域第一个字节表示本报文中是否有CAPK信息
                byte[] field62ArrayBuff = StringUtil.hexStringToByte(field62.substring(2, field62.length()));
                CAPKDB capkdb = new CAPKDB();
                while (field62ArrayBuff.length > 0) {
                    TLVData tlv = new TLVData();
                    TLVData.PraseTLV(field62ArrayBuff, tlv);
                    Log(tlv.toString());
                    field62ArrayBuff = byteArrayTrimHead(field62ArrayBuff, tlv.WholeLen);
                    Log(StringUtil.bytesToHexString(field62ArrayBuff));
                    DataExchange.putTLVToCAPKDB(tlv, capkdb);
                }

                //判断现在平台返回的CAPK,是不是请求下载的那个CAPK
                if (GlobalParams.currentDownloadingCAPK != null
                        && GlobalParams.currentDownloadingCAPK.getRID().equals(capkdb.getRID()) && GlobalParams.currentDownloadingCAPK.getKeyID().equals(capkdb.getKeyID())) {
                    capkdb.setCAPKID(GlobalParams.currentDownloadingCAPK.getCAPKID());
                    //保存到EMV库
                    EmvCAPK emvCAPK = new EmvCAPK();
                    DataExchange.DBtoCAPK(capkdb, emvCAPK);
                    ret = EmvService.Emv_AddCapk(emvCAPK);
                    Log("Emv_AddCapk:" + ret);

                    //更新数据库
                    capkdb.setLocalBOCVersion(capkdb.getLatestBOCVersion());//请求下载CAPK时依据这两个版本是否相同来找出需要下载的CAPK
//                    CAPKDBDao capkDBDao = new CAPKDBDao(context);

                    AppContext.Companion.getInstance().getCapkRepository().update(capkdb);
                    Log("update CAPK DB:" + "\nRID:" + capkdb.getRID() + "\nIndex:" + capkdb.getKeyID());

                } else {
                    //请重新下载
                    Log("平台返回的CAPK不是终端请求的CAPK,重新请求下载");
                    Log(GlobalParams.currentDownloadingCAPK.getRID() + " " + capkdb.getRID());
                    Log(GlobalParams.currentDownloadingCAPK.getKeyID() + " " + capkdb.getKeyID());
                    return ERR_REDOWN_CAPK;
                }
            } else {
                Log("POS 中心没有该公钥");
                return ERR_NO_PARA;
            }
        } else {
            Log("无62域, 无CAPK信息");
            //  return ERR_NO_FIELD62;
        }
        return OK_SUCCESS;
    }


    /**
     * 解包报文: CAPK应用参数下载结束
     *
     * @param context
     * @param packData
     * @return 出错时返回错误码。成功返回OK_SUCCESS，。
     */
    public static int unpackMessage_DownloadCAPKParaFinish(Context context, POSData posData, PackData packData) {
        int ret = 0;
        String unpackResult;
        String responseCode;
        HashMap outMap = new HashMap();
        boolean bCheckMAC = false;
        try {
            outMap = ProtocolUtil.parse8583ReceiveMessage(packData.data_receive, 0x0810);
            outMap.put("tranType", posData.transType + "");
            // 打印输入HashMap内容
            MLog.i("接收数据：");
            StringUtil.printHashMapContent(outMap);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bCheckMAC = true;
            if (outMap == null) {
                return ERR_UNPACK;
            }

            //抽出数据,给应用参考交易信息,或打印小票
//            getTransactionDataFromOutMap(outMap, packData);

            unpackResult = (String) outMap.get("resultCode");
            packData.unPackCode = unpackResult;
            if (unpackResult.equals("995")) {
                return ERR_UNPACK;
            } else if (unpackResult.equals("994")) {
//            return TPPOSUtil.ERR_MAC_CHECK;//MAC校验错误,参数下载不校验MAC
            } else if (unpackResult.equals("999") || !unpackResult.equals("00")) {
                return ERR_UNKNOW;
            }
        }

        responseCode = (String) outMap.get("39");
        packData.responseCode = responseCode;
        MLog.i("平台应答码:" + responseCode);

        //判断应答码
        if (!responseCode.equals("00")) {
            //ligc 直接返回失败，提示错误
            return ERR_TRANS_FAILED;
        }

        //更新下载公钥标识
        GlobalParams.set_isUpdateCapk(context,true);

        return ret;
    }


    /**
     * 解包报文: AID应用参数版本查询
     *
     * @param context
     * @param packData
     * @return
     */
    public static int unpackMessage_QureyAIDParaVer(Context context, POSData posData, PackData packData) {
        String unpackResult;
        String responseCode;
        int ret = OK_SUCCESS;
        HashMap outMap = new HashMap();
        GlobalParams.bCheckMAC = false;
        try {
            outMap = ProtocolUtil.parse8583ReceiveMessage(packData.data_receive, 0x0810);
            outMap.put("tranType", posData.transType + "");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            if (outMap == null) {
                return ERR_UNPACK;
            }
            //抽出数据,给应用参考交易信息,或打印小票
//            getTransactionDataFromOutMap(outMap, packData);

            unpackResult = (String) outMap.get("resultCode");
            packData.unPackCode = unpackResult;
            if (unpackResult.equals("995")) {
                return ERR_UNPACK;
            } else if (unpackResult.equals("994")) {
//            return TPPOSUtil.ERR_MAC_CHECK;//MAC校验错误
            } else if (unpackResult.equals("999") || !unpackResult.equals("00")) {
                return ERR_UNKNOW;
            }
        }
        responseCode = (String) outMap.get("39");
        packData.responseCode = responseCode;
        Log("平台应答码:" + responseCode);

        //判断应答码
        if (!responseCode.equals("00")) {
            //ligc 直接返回失败，提示错误
            return ERR_TRANS_FAILED;
        }

        //处理AID版本
        //62域第一个字节为0：报文中没有AID信息
        //62域第一个字节为1：报文中有AID信息，且一个报文就放下了所有AID信息。
        //62域第一个字节为2：报文中有AID信息，且一个报文无法发下所有AID信息。需要继续发送请求报文。
        //62域第一个字节为3：报文中有AID信息，且是最后一组报文。
        //格式 一个字节 + RID1 +索引1+有效期1+ ...采用TLV格式
        String field62 = (String) outMap.get("62");
        if (field62.charAt(1) == '0') {
            return ERR_NO_PARA;
        } else if (field62.charAt(1) == '2') {
            ret = ERR_NOT_FINISHED;
        }
        if (field62 != null && !field62.equals("")) {
            byte[] field62Array = StringUtil.hexStringToByte(field62.substring(2, field62.length()));
            Boolean isSucc = updateAID(field62Array, context);
            if(!isSucc){
                ret = ERR_FIELD62;
            }
        } else {
            Log("无62域, 无AID列表");
            ret = ERR_NO_FIELD62;
        }
        return ret;
    }

    /**
     * 更新AID的版本号,更新到AID数据库
     *
     * @param bt9F06
     * @param context
     */
    public static Boolean updateAID(byte[] bt9F06, Context context) {
        if (bt9F06 == null || bt9F06.length == 0) {
            MLog.i("update AID 【start】... 数据为空，直接返回");
            return false;
        }

        byte[] tmp = bt9F06;
//        AIDDBDao aiddbDao = new AIDDBDao(context);
        AIDDB aiddb;
        TLVData tag9F06 = new TLVData();
        Log("update AID 【start】...");
        try {
            while (tmp.length > 0) {
                int len9F06 = TLVData.PraseTLV(tmp, tag9F06);
                if (len9F06 <= 0 || tag9F06.Value == null) {
                    MLog.i("解析9F06失败，长度=" + len9F06);
                    return false;
                }
                tmp = byteArrayTrimHead(tmp, tag9F06.WholeLen);

                Log("9F06:\n" + tag9F06.toString());


                String sAID = StringUtil.bytesToHexString(tag9F06.Value);
                Log("sAID:" + sAID);

                List<AIDDB> aidRecode = AppContext.getInstance().getAidRepository().findAllByField("AID", sAID);
                Log("sAID1:" + sAID);
                boolean needCreate = false;
                if (aidRecode == null || aidRecode.isEmpty()) {
                    needCreate = true;
                } else {
                    AIDDB first = aidRecode.get(0);
                    needCreate = !Objects.equals(first.getLocalBOCVersion(), sAID);
                }
                if (needCreate) {
                    Log("sAID2:" + sAID);
                    aiddb = new AIDDB();
                    aiddb.setAID(sAID);
                    aiddb.setLatestBOCVersion(sAID);
                    aiddb.setLocalBOCVersion("");
                    Log("sAID3:" + sAID);
                    AppContext.Companion.getInstance().getAidRepository().create(aiddb);
                    Log("sAID4:" + sAID);
                }
            }
            Log("update AID 【end】...");
            return true;
        } catch (Exception e) {
            Log("updateAid 异常: " + e.getMessage());
            // 这里可以根据需要回滚数据库操作或清空本次已添加的数据
            return false;
        }
    }

    /**
     * 解包报文: 下载AID参数()
     *
     * @param context
     * @param packData
     * @return
     */
    public static int unpackDownAIDMessage(Context context, POSData posData, PackData packData) {
        String unpackResult;
        String responseCode;
        HashMap outMap = new HashMap();
        int ret = 0;
        GlobalParams.bCheckMAC = false;
        try {
            outMap = ProtocolUtil.parse8583ReceiveMessage(packData.data_receive, 0x0800);
            outMap.put("tranType", posData.transType + "");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outMap == null) {
                return ERR_UNPACK;
            }

            //抽出数据,给应用参考交易信息,或打印小票
//            getTransactionDataFromOutMap(outMap, packData);

            unpackResult = (String) outMap.get("resultCode");
            packData.unPackCode = unpackResult;
            if (unpackResult.equals("995")) {
                return ERR_UNPACK;
            } else if (unpackResult.equals("994")) {
//            return TPPOSUtil.ERR_MAC_CHECK;//MAC校验错误,参数下载不校验MAC
            } else if (unpackResult.equals("999") || !unpackResult.equals("00")) {
                return ERR_UNKNOW;
            }
        }

        responseCode = (String) outMap.get("39");
        packData.responseCode = responseCode;
        Log("平台应答码:" + responseCode);


        //判断应答码
        if (!responseCode.equals("00")) {
            //ligc 直接返回失败，提示错误
            return ERR_TRANS_FAILED;
        }

        //处理AID
        /**
         * 56域包含多个TLV,这里依次解出TAG,
         * 把每个TAG的值放在AIDDB的各个成员里,
         * 解析完后把AIDDB保存在数据库,
         * 再把AIDDB的成员的值转到EmvApp的成员里,保存到EMV的so库里
         */
        String field62 = (String) outMap.get("62");
        if (field62 != null && !field62.equals("")) {
            byte[] field62ArrayBuff = StringUtil.hexStringToByte(field62.substring(2, field62.length()));
//            AIDDBDao aidDBDao = new AIDDBDao(context);
            List<AIDDB> aiddbs = AppContext.Companion.getInstance().getAidRepository().findAllByField("AID", GlobalParams.currentDownloadingAID.getAID());
//            List<AIDDB> aiddbs = aidDBDao.findAllByField("AID", GlobalParams.currentDownloadingAID.getAID());
            AIDDB aiddb = aiddbs.get(0);
            while (field62ArrayBuff.length > 0) {
                TLVData tlv = new TLVData();
                TLVData.PraseTLV(field62ArrayBuff, tlv);

                field62ArrayBuff = byteArrayTrimHead(field62ArrayBuff, tlv.WholeLen);  //截掉已经处理的部分
                DataExchange.putTLVToAIDDB(tlv, aiddb);
            }

            //判断现在平台返回的AID,是不是请求下载的那个AID
            if (GlobalParams.currentDownloadingAID != null
                    && GlobalParams.currentDownloadingAID.getAID().equals(aiddb.getAID())) {
                //保存到EMV库
                EmvApp emvApp = new EmvApp();
                DataExchange.DBtoAID(aiddb, emvApp);
                ret = EmvService.Emv_AddApp(emvApp);
                Log("Emv_AddApp: " + ret + " AID:[" + StringUtil.bytesToHexString(emvApp.AID) + "]");

                //更新数据库
                AppContext.Companion.getInstance().getAidRepository().update(aiddb);
                Log("update AID :" + aiddb.getAID());

            } else {
                //请重新下载
                Log("平台返回的AID不是终端请求的AID,重新请求下载");
                return ERR_REDOWN_APP;
            }

        } else {
            Log("无62域, 无AID信息");
            //todo 待实现
            ret = ERR_NO_FIELD62;
        }
        return OK_SUCCESS;
    }

    /**
     * 解包报文: AID应用参数下载结束
     *
     * @param context
     * @param packData
     * @return 出错时返回错误码。成功返回OK_SUCCESS，。
     */
    public static int unpackMessage_DownloadAIDParaFinish(Context context, POSData posData, PackData packData) {
        int ret = 0;
        String unpackResult;
        String responseCode;
        HashMap outMap = new HashMap();
        boolean bCheckMAC = false;
        try {
            outMap = ProtocolUtil.parse8583ReceiveMessage(packData.data_receive, 0x0810);
            outMap.put("tranType", posData.transType + "");
            // 打印输入HashMap内容
            MLog.i("接收数据：");
            StringUtil.printHashMapContent(outMap);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bCheckMAC = true;
            if (outMap == null) {
                return ERR_UNPACK;
            }

            //抽出数据,给应用参考交易信息,或打印小票
//            getTransactionDataFromOutMap(outMap, packData);

            unpackResult = (String) outMap.get("resultCode");
            packData.unPackCode = unpackResult;
            if (unpackResult.equals("995")) {
                return ERR_UNPACK;
            } else if (unpackResult.equals("994")) {
//            return TPPOSUtil.ERR_MAC_CHECK;//MAC校验错误,参数下载不校验MAC
            } else if (unpackResult.equals("999") || !unpackResult.equals("00")) {
                return ERR_UNKNOW;
            }
        }

        responseCode = (String) outMap.get("39");
        packData.responseCode = responseCode;
        MLog.i("平台应答码:" + responseCode);

        //判断应答码
        if (!responseCode.equals("00")) {
            //ligc 直接返回失败，提示错误
            return ERR_TRANS_FAILED;
        }

        //更新下载参数标识
        GlobalParams.set_isUpdateAID(context,true);

        return ret;
    }

    public static int unpackMessage_DownParaNFC(Context context, POSData posData, PackData packData) {
        String unpackResult;
        String responseCode;
        int ret = OK_SUCCESS;
        HashMap outMap = new HashMap();
        GlobalParams.bCheckMAC = false;
        try {
            outMap = ProtocolUtil.parse8583ReceiveMessage(packData.data_receive, 0x0810);
            outMap.put("tranType", posData.transType + "");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            if (outMap == null) {
                return ERR_UNPACK;
            }
            //抽出数据,给应用参考交易信息,或打印小票
//            getTransactionDataFromOutMap(outMap, packData);

            unpackResult = (String) outMap.get("resultCode");
            packData.unPackCode = unpackResult;
            if (unpackResult.equals("995")) {
                return ERR_UNPACK;
            } else if (unpackResult.equals("994")) {
//            return TPPOSUtil.ERR_MAC_CHECK;//MAC校验错误
            } else if (unpackResult.equals("999") || !unpackResult.equals("00")) {
                return ERR_UNKNOW;
            }
        }
        responseCode = (String) outMap.get("39");
        packData.responseCode = responseCode;
        Log("平台应答码:" + responseCode);

//        TPPOSUtil.setMerchantName(context, (String) outMap.get("43"));
        Log("商户名称:" + outMap.get("43"));

        //判断是否更新批次号、转存旧批交易数据
        if (null != outMap.get("60.2")) {
            String onlineBatchNo = (String) outMap.get("60.2");
//            checkBatchNo(context, onlineBatchNo);
        }

        //判断应答码
        if (!responseCode.equals("00")) {
            //// TODO: 2017/3/3 0003  待实现
            //若应答码不为00的处理？？
        }

        byte field62[] = StringUtil.hexStringToByte(((String) outMap.get("62")));
        if (field62 != null && field62.length != 0) {
            NFCParaData nfcParaData = NFCParaData.getInstance(context);
            String sField = new String(field62);
            Log("NFCParaData: " + sField);
            while (!TextUtils.isEmpty(sField)) {

                if (sField.length() < 10) {
                    Log("ERROR NFCParaData sField.length<10: " + sField.length());
                    return ERR_FIELD62;
                }
                String tag = sField.substring(0, 6);
                sField = sField.substring(6, sField.length());
                int len = Integer.parseInt(sField.substring(0, 3));
                sField = sField.substring(3, sField.length());

                if (sField.length() < len) {
                    Log("ERROR NFCParaData: " + tag + " " + len + " " + sField.length());
                    return ERR_FIELD62;
                }
                String value = sField.substring(0, len);
                if (sField.length() >= len) {
                    sField = sField.substring(len, sField.length());
                    if (sField.length() == len) {
                        sField = null;
                    }
                }


                if ("FFF001".equals(tag)) {//                	001	0
                    if (value.length() != 1) {
                        return ERR_FIELD62;
                    }
                    Log("NFCParaDataFFF001: " + tag + " " + len + " " + value);
                    nfcParaData.setNfcPrioritySwitch(value);

                } else if ("FF805D".equals(tag)) {//                	001	1
                    if (value.length() != 1) {
                        return ERR_FIELD62;
                    }
                    Log("NFCParaDataFF805D: " + tag + " " + len + " " + value);
                    nfcParaData.setNfcDealSwitch(value);

                } else if ("FF803A".equals(tag)) {//                	003	010
                    if (value.length() != 3) {
                        return ERR_FIELD62;
                    }
                    Log("NFCParaDataFF803A: " + tag + " " + len + " " + value);
                    nfcParaData.setReDoTime(value);

                } else if ("FF803C".equals(tag)) {//                	003	060
                    if (value.length() != 3) {
                        return ERR_FIELD62;
                    }
                    Log("NFCParaDataFF803C: " + tag + " " + len + " " + value);
                    nfcParaData.setRecordTime(value);

                } else if ("FF8058".equals(tag)) {//                	012	000000030000
                    if (value.length() != 12) {
                        return ERR_FIELD62;
                    }
                    Log("NFCParaDataFF8058: " + tag + " " + len + " " + value);
                    nfcParaData.setQPSNoPWDLimitAmount(value);

                } else if ("FF8054".equals(tag)) {//                	001	1
                    if (value.length() != 1) {
                        return ERR_FIELD62;
                    }
                    Log("NFCParaDataFF8054: " + tag + " " + len + " " + value);
                    nfcParaData.setQPSIdentify(value);
                } else if ("FF8055".equals(tag)) {//                	001	0
                    if (value.length() != 1) {
                        return ERR_FIELD62;
                    }
                    Log("NFCParaDataFF8055: " + tag + " " + len + " " + value);
                    nfcParaData.setBinListAIdentify(value);
                } else if ("FF8056".equals(tag)) {//                	001	1
                    if (value.length() != 1) {
                        return ERR_FIELD62;
                    }
                    Log("NFCParaDataFF8056: " + tag + " " + len + " " + value);
                    nfcParaData.setBinListBIdentify(value);
                } else if ("FF8057".equals(tag)) {//                	001	0
                    if (value.length() != 1) {
                        return ERR_FIELD62;
                    }
                    Log("NFCParaDataFF8057: " + tag + " " + len + " " + value);
                    nfcParaData.setCDCVMIdentify(value);

                } else if ("FF8059".equals(tag)) {//                	012	000000020000
                    if (value.length() != 12) {
                        return ERR_FIELD62;
                    }
                    Log("NFCParaDataFF8059: " + tag + " " + len + " " + value);
                    nfcParaData.setQPSNoSignLimitAmount(value);
                } else if ("FF805A".equals(tag)) {//                	001	1
                    if (value.length() != 1) {
                        return ERR_FIELD62;
                    }
                    Log("NFCParaDataFF805A: " + tag + " " + len + " " + value);
                    nfcParaData.setNoSignIdentify(value);
                }
            }
        } else {
            Log("无62域");
            // ret = ERR_NO_FIELD62;
        }
        Log("NFCParaData3: " + NFCParaData.getInstance(context).toString());
        return ret;
    }


    /**
     * 解包报文: NFC参数下载结束
     *
     * @param context
     * @param packData
     * @return 出错时返回错误码。成功返回OK_SUCCESS，。
     */
    public static int unpackMessage_DownloadNFCParaFinish(Context context, POSData posData, PackData packData) {
        int ret = 0;
        String unpackResult;
        String responseCode;
        HashMap outMap = new HashMap();
        boolean bCheckMAC = false;
        try {
            outMap = ProtocolUtil.parse8583ReceiveMessage(packData.data_receive, 0x0810);
            outMap.put("tranType", posData.transType + "");
            // 打印输入HashMap内容
            MLog.i("接收数据：");
            StringUtil.printHashMapContent(outMap);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bCheckMAC = true;
            if (outMap == null) {
                return ERR_UNPACK;
            }

            //抽出数据,给应用参考交易信息,或打印小票
//            getTransactionDataFromOutMap(outMap, packData);

            unpackResult = (String) outMap.get("resultCode");
            packData.unPackCode = unpackResult;
            if (unpackResult.equals("995")) {
                return ERR_UNPACK;
            } else if (unpackResult.equals("994")) {
//            return TPPOSUtil.ERR_MAC_CHECK;//MAC校验错误,参数下载不校验MAC
            } else if (unpackResult.equals("999") || !unpackResult.equals("00")) {
                return ERR_UNKNOW;
            }
        }

        responseCode = (String) outMap.get("39");
        packData.responseCode = responseCode;
        MLog.i("平台应答码:" + responseCode);

        //判断是否更新批次号、转存旧批交易数据
        if (null != outMap.get("60.2")) {
            String onlineBatchNo = (String) outMap.get("60.2");
//            checkBatchNo(context, onlineBatchNo);
        }

        //判断应答码
        if (!responseCode.equals("00")) {
            //// TODO: 2017/3/3 0003  待实现
            //若应答码不为00的处理？？
        }

        return ret;
    }

    public static int unpackMessage_DawnParaNFCBlackList(Context context, POSData posData, PackData packData) {
        String unpackResult;
        String responseCode;
        int ret = OK_SUCCESS;
        HashMap outMap = new HashMap();
        GlobalParams.bCheckMAC = false;
        try {
            outMap = ProtocolUtil.parse8583ReceiveMessage(packData.data_receive, 0x0810);
            outMap.put("tranType", posData.transType + "");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            if (outMap == null) {
                return ERR_UNPACK;
            }
            //抽出数据,给应用参考交易信息,或打印小票
//            getTransactionDataFromOutMap(outMap, packData);

            unpackResult = (String) outMap.get("resultCode");
            packData.unPackCode = unpackResult;
            if (unpackResult.equals("995")) {
                return ERR_UNPACK;
            } else if (unpackResult.equals("994")) {
//            return TPPOSUtil.ERR_MAC_CHECK;//MAC校验错误
            } else if (unpackResult.equals("999") || !unpackResult.equals("00")) {
                return ERR_UNKNOW;
            }
        }
        responseCode = (String) outMap.get("39");
        packData.responseCode = responseCode;
        Log("平台应答码:" + responseCode);

        //判断是否更新批次号、转存旧批交易数据
        if (null != outMap.get("60.2")) {
            String onlineBatchNo = (String) outMap.get("60.2");
//            checkBatchNo(context, onlineBatchNo);
        }

        //判断应答码
        if (!responseCode.equals("00")) {
            //// TODO: 2017/3/3 0003  待实现
            //若应答码不为00的处理？？
        }

        //处理AID版本
        //62域第一个字节为0：报文中没有AID信息
        //62域第一个字节为1：报文中有AID信息，且一个报文就放下了所有AID信息。
        //62域第一个字节为2：报文中有AID信息，且一个报文无法发下所有AID信息。需要继续发送请求报文。
        //62域第一个字节为3：报文中有AID信息，且是最后一组报文。
        //格式 一个字节 + RID1 +索引1+有效期1+ ...采用TLV格式
        String field62 = (String) outMap.get("62");

        if (field62 != null && !field62.equals("")) {
            if (field62.charAt(1) == '0') {
                return ERR_NO_PARA;
            }
            byte[] endIndex = StringUtil.hexStringToByte(field62.substring(2, 8));
            byte[] blackList = StringUtil.hexStringToByte(field62.substring(8, field62.length()));

            while (blackList.length > 0) {
//                NFCBlackListDao nfcBlackListDao = new NFCBlackListDao(context);
                NFCBlackListDB nfcBlackListDB = new NFCBlackListDB();
                nfcBlackListDB.setBlackIndex(new String(endIndex)) ;

                int nLen = Integer.parseInt(new String(ByteArrayUtil.byteArrayGetHead(blackList, 2)));
                nfcBlackListDB.setBlackCardLen("" + nLen);
                blackList = byteArrayTrimHead(blackList, 2);
                nfcBlackListDB.setBlackCardID(new String(ByteArrayUtil.byteArrayGetHead(blackList, 6))) ;
                blackList = byteArrayTrimHead(blackList, 6);
                AppContext.getInstance().getNfcBlackRepository().create(nfcBlackListDB);
                Log("BlackListDBNFC :" + nfcBlackListDB.getBlackIndex() + "  " + nfcBlackListDB.getBlackCardID() + " " + nfcBlackListDB.getBlackCardLen());
            }
//            NFCBlackListDao nfcBlackListDao = new NFCBlackListDao(context);
            List list = AppContext.getInstance().getNfcBlackRepository().list();
            if (list != null && list.size() > 0) {
                Iterator<NFCBlackListDB> iterator = list.iterator();
                while (iterator.hasNext()) {
                    NFCBlackListDB nfcBlackListDB = iterator.next();
                    Log("BlackListDBNFC1 :" + nfcBlackListDB.getBlackIndex() + "  " + nfcBlackListDB.getBlackCardID()  + " " + nfcBlackListDB.getBlackCardLen());
                }
            }
            if (field62.charAt(1) == '2') {
                ret = ERR_NOT_FINISHED;
            }
        } else {
            Log("无62域, 无列表");
            //ret = ERR_NO_FIELD62;
        }

        return ret;
    }

    /**
     * 解包报文: nfc白名单参数下载结束
     *
     * @param context
     * @param packData
     * @return 出错时返回错误码。成功返回OK_SUCCESS，。
     */
    public static int unpackMessage_DownParaNFCBlackListFinish(Context context, POSData posData, PackData packData) {
        int ret = 0;
        String unpackResult;
        String responseCode;
        HashMap outMap = new HashMap();
        boolean bCheckMAC = false;
        try {
            outMap = ProtocolUtil.parse8583ReceiveMessage(packData.data_receive, 0x0810);
            outMap.put("tranType", posData.transType + "");
            // 打印输入HashMap内容
            MLog.i("接收数据：");
            StringUtil.printHashMapContent(outMap);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bCheckMAC = true;
            if (outMap == null) {
                return ERR_UNPACK;
            }

            //抽出数据,给应用参考交易信息,或打印小票
//            getTransactionDataFromOutMap(outMap, packData);

            unpackResult = (String) outMap.get("resultCode");
            packData.unPackCode = unpackResult;
            if (unpackResult.equals("995")) {
                return ERR_UNPACK;
            } else if (unpackResult.equals("994")) {
//            return TPPOSUtil.ERR_MAC_CHECK;//MAC校验错误,参数下载不校验MAC
            } else if (unpackResult.equals("999") || !unpackResult.equals("00")) {
                return ERR_UNKNOW;
            }
        }

        responseCode = (String) outMap.get("39");
        packData.responseCode = responseCode;
        MLog.i("平台应答码:" + responseCode);

        //判断是否更新批次号、转存旧批交易数据
        if (null != outMap.get("60.2")) {
            String onlineBatchNo = (String) outMap.get("60.2");
//            checkBatchNo(context, onlineBatchNo);
        }

        //判断应答码
        if (!responseCode.equals("00")) {
            //// TODO: 2017/3/3 0003  待实现
            //若应答码不为00的处理？？
        }

        return ret;
    }

    /**
     * 解包报文: 签到结束
     *
     * @param context
     * @param packData
     * @return 出错时返回错误码。成功返回OK_SUCCESS，。
     */
    public static int unpackMessage_LoginFinish(Context context, POSData posData, PackData packData) {
        int ret = 0;
        String unpackResult;
        String responseCode;
        HashMap outMap = new HashMap();
        boolean bCheckMAC = false;
        try {
            outMap = ProtocolUtil.parse8583ReceiveMessage(packData.data_receive, 0x0810);
            outMap.put("tranType", posData.transType + "");
            // 打印输入HashMap内容
            MLog.i("接收数据：");
            StringUtil.printHashMapContent(outMap);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bCheckMAC = true;
            if (outMap == null) {
                packData.unPackCode = "解包出错";
                return ERR_UNPACK;
            }

            //抽出数据,给应用参考交易信息,或打印小票
//            getTransactionDataFromOutMap(outMap, packData);

            unpackResult = (String) outMap.get("resultCode");
            packData.unPackCode = unpackResult;
            if (unpackResult.equals("995")) {
                packData.unPackCode = "解析返回信息异常";
                return ERR_UNPACK;
            } else if (unpackResult.equals("994")) {
//            return TPPOSUtil.ERR_MAC_CHECK;//MAC校验错误,参数下载不校验MAC
            } else if (unpackResult.equals("999") || !unpackResult.equals("00")) {
                return ERR_UNKNOW;
            }
        }

        responseCode = (String) outMap.get("39");
        packData.responseCode = responseCode;
        MLog.i("平台应答码:" + responseCode);

        //判断是否更新批次号、转存旧批交易数据
        if (null != outMap.get("60.2")) {
            String onlineBatchNo = (String) outMap.get("60.2");
//            checkBatchNo(context, onlineBatchNo);
        }

        //判断应答码
        if (!responseCode.equals("00")) {
            //// TODO: 2017/3/3 0003  待实现
            //若应答码不为00的处理？？
            return TYPE_SIGN_IN;
        }

        //签到成功校验PIN和MAC KEY,并写入


        return ret;
    }
}
