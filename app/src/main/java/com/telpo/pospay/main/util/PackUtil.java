package com.telpo.pospay.main.util;

import static com.telpo.pospay.main.util.TPPOSUtil.ERR_NO_AID_LIST;
import static com.telpo.pospay.main.util.TPPOSUtil.ERR_NO_CAPK_LIST;
import static com.telpo.pospay.main.util.TPPOSUtil.ERR_NO_NEED_UPDATE;
import static com.telpo.pospay.main.util.TPPOSUtil.ERR_PACK_UNKNOWERR;
import static com.telpo.pospay.main.util.TPPOSUtil.OK_SUCCESS;

import android.content.Context;
import android.util.Log;

import java.util.List;
import java.util.HashMap;

import com.telpo.emv.EmvService;
import com.telpo.pospay.db.bean.AIDDB;
import com.telpo.pospay.db.bean.NFCBlackListDB;
import com.telpo.pospay.main.data.GlobalParams;
import com.telpo.pospay.main.data.POSData;
import com.telpo.pospay.main.data.PackData;
import com.telpo.pospay.db.bean.CAPKDB;
import com.telpo.pospay.db.dao.CAPKDBDao;
import com.telpo.pospay.db.bean.DiaryEntry;
import com.telpo.pospay.AppContext;
import com.telpo.base.util.MLog;
import com.telpo.emv.util.TLVData;
import com.telpo.emv.util.StringUtil;

import org.jetbrains.annotations.NotNull;

public class PackUtil {

    public static String MESSAGE_Head = "603300337034 ";//报文头(中国银联标准12个字节)
    public static String MESSAGE_TPDU = "6000030000";//TPDU
    public static String MESSAGE_INFO = "303120202020202020202020706F2D50563230313931322D30303030333830320039";

    public static int packMessage_QureyCAPKParaVer(android.content.Context context, POSData posData, PackData packData) {
        int ret = 0;
        HashMap inMap = new HashMap();
        inMap.put("41", posData.terminalID);    //受卡机终端标识码(终端号)
        inMap.put("42", posData.merchantID);    //受卡方的标识码(商户号)
        inMap.put("60", "1");  // 自定义域61

        inMap.put("60.1", "00");// 交易类型码
        inMap.put("60.2", posData.batchNo);//批次号
        inMap.put("60.3", "372");//N3,网管管理信息码,IC 卡公钥下载交易采用 "372" 文档12.4.9；

//       db.dao.CAPKDBDao capkdbDao = new db.dao.CAPKDBDao(context);
        List<CAPKDB> list = AppContext.Companion.getInstance().getCapkRepository().list();
        //第一次查询，将所有CAPK标记为false
        if ("100".equals(posData.sFile62) || list.size() > 99) {

            AppContext.Companion.getInstance().getCapkRepository().deleteList(list);
            com.telpo.emv.EmvService.Emv_RemoveAllCapk();
            posData.sFile62 = "100";
        } else {
            posData.sFile62 = "1" + String.format("%02d", list.size());
        }

        inMap.put("62", posData.sFile62);

        try {
            packData.data_send = ProtocolUtil.com8583SendMessage(context, inMap, 0x0820);
        } catch (Exception e) {
            e.printStackTrace();
            android.util.Log.e("TPPOSUtil", "packMessage_QureyCAPKParaVer error");
            return ERR_PACK_UNKNOWERR;
        }

        return OK_SUCCESS;
    }


    /**
     * 打包报文: 下载参数(CAPK参数)
     *
     * @param context
     * @param posData
     * @param packData
     * @return
     */
    public static int packMessage_DownParaCAPK(android.content.Context context, POSData posData, PackData packData) {
        MLog.i("----packMessage_DownParaCAPK----");

        int ret = 0;
        boolean bFindCAPK = false;//是否从数据库找到需要更新的CAPK
        HashMap inMap = new HashMap();
        List<CAPKDB> capkdbList = AppContext.Companion.getInstance().getCapkRepository().list();

        packData.downloadParamCount = capkdbList.size();

        //todo 待实现
        //从数据库中找出本地CAPK版本与平台CAPK版本不同的CAPK
        TLVData tag9F06 = new TLVData();//RID
        TLVData tagDF22 = new TLVData();//索引
        if (capkdbList != null && capkdbList.size() > 0) {
            for (int i = 0; i < capkdbList.size(); i++) {
                CAPKDB db = capkdbList.get(i);
                packData.downloadParamIndex = i + 1;
                if (db.getLatestBOCVersion().equals(db.getLocalBOCVersion())) {
                    continue;//如果版本相同,则不用下载
                }
                MLog.i("找到需要下载的CAPK:\nRID:" + db.getRID() + "\nCAPKID:" + db.getCAPKID()
                        + "\nLocal Version:" + db.getLocalBOCVersion() + "\nBOC Version:" + db.getLatestBOCVersion());

                //版本不同:
                tag9F06.Tag = 0x9F06;
                tag9F06.Value = StringUtil.hexStringToByte(db.getRID());
                tag9F06.Len = tag9F06.Value.length;

                tagDF22.Tag = 0x9F22;
                tagDF22.Value = StringUtil.hexStringToByte(db.getKeyID());
                tagDF22.Len = tagDF22.Value.length;


                com.telpo.pospay.main.data.GlobalParams.currentDownloadingCAPK = db;//解包时判断平台返回AID的是不是现在请求的AID
                bFindCAPK = true;
                break;
            }
            if (bFindCAPK == false) {
                MLog.e("ERR_NO_NEED_UPDATE");
                return ERR_NO_NEED_UPDATE;
            }
        } else {
            //数据库错误,终端没有CAPK列表,需要先使用“请求查询CAPK参数版本”
            MLog.e("ERR_NO_CAPK_LIST");
            return ERR_NO_CAPK_LIST;
        }
        inMap.put("41", posData.terminalID);    //受卡机终端标识码(终端号)
        inMap.put("42", posData.merchantID);    //受卡方的标识码(商户号)
        inMap.put("60", "1");
        inMap.put("60.1", "00");// 交易类型码
        inMap.put("60.2", posData.batchNo);//批次号
        inMap.put("60.3", "370");//N3,网管管理信息码,IC 卡公钥下载交易采用 "372" 文档12.4.9；
        inMap.put("62", TLVData.generateTLVHexString(tag9F06) + TLVData.generateTLVHexString(tagDF22));

        try {
            packData.data_send = ProtocolUtil.com8583SendMessage(context, inMap, 0x0800);
            if (packData.data_send != null && packData.data_send.length > 0) {
                MLog.i("--packMessage_DownParaCAPK data_send-- " + StringUtil.bytesToHexString(packData.data_send));
            }
        } catch (Exception e) {
            MLog.e("TPPOSUtil", "packMessage_DownMasterKey error");
            return ERR_PACK_UNKNOWERR;
        }

        return OK_SUCCESS;
    }


    public static int packMessage_DownLoadCAPKParaFinish(Context context, POSData posData, PackData packData) {
        int ret = 0;
        HashMap inMap = new HashMap();
        inMap.put("41", posData.terminalID);    //受卡机终端标识码(终端号)
        inMap.put("42", posData.merchantID);    //受卡方的标识码(商户号)
        inMap.put("60", "1");  // 自定义域61

        inMap.put("60.1", "00");// 交易类型码
        inMap.put("60.2", posData.batchNo);//批次号
        inMap.put("60.3", "371");//N3,网管管理信息码,IC 卡公钥下载结束交易采用 "371" 文档12.4.10；


        try {
            packData.data_send = ProtocolUtil.com8583SendMessage(context, inMap, 0x0800);
        } catch (Exception e) {
            e.printStackTrace();
            android.util.Log.e("TPPOSUtil", "packMessage_DownLoadCAPKParaFinish error");
            return ERR_PACK_UNKNOWERR;
        }

        return OK_SUCCESS;
    }

    /**
     * 打包报文: AID应用参数版本查询
     *
     * @param context
     * @param posData
     * @param packData
     * @return
     */
    public static int packMessage_QureyAIDParaVer(Context context, POSData posData, PackData packData) {
        HashMap inMap = new HashMap();
        inMap.put("41", posData.terminalID);    //受卡机终端标识码(终端号)
        inMap.put("42", posData.merchantID);    //受卡方的标识码(商户号)
        inMap.put("60", "1");  // 自定义域61

        inMap.put("60.1", "00");// 交易类型码
        inMap.put("60.2", posData.batchNo);//批次号
        inMap.put("60.3", "382");//N3,网管管理信息码,IC 卡公钥下载交易采用 "372" 文档12.4.9；


        List<AIDDB> aiddbList = AppContext.Companion.getInstance().getAidRepository().list();

//        AIDDBDao aiddbDao = new AIDDBDao(context);
        //第一次查询 62域填充100
        if (posData.sFile62.equals("100") || (aiddbList.size() > 99)) {

            if (aiddbList != null && aiddbList.size() > 0) {
//                aiddbDao.deleteList(aiddbDao.list());
                AppContext.Companion.getInstance().getAidRepository().deleteList(aiddbList);
                EmvService.Emv_RemoveAllApp();
            }
            posData.sFile62 = "100";
        } else {
            //不是第一次查询，1XX  XX表示前面收到的AID个数
            posData.sFile62 = "1" + String.format("%02d", aiddbList.size());
        }
        inMap.put("62", posData.sFile62);
        try {
            packData.data_send = ProtocolUtil.com8583SendMessage(context, inMap, 0x0820);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("TPPOSUtil", "packMessage_QureyCAPKParaVer error");
            return ERR_PACK_UNKNOWERR;
        }

        return OK_SUCCESS;
    }

    /**
     * 打包报文: 下载AID参数(AID应用参数)
     *
     * @param context
     * @param posData
     * @param packData
     * @return
     */
    public static int packMessage_DownParaAID(Context context, POSData posData, PackData packData) {
        int ret = 0;
        boolean bFindAID = false;//是否从数据库找到需要更新的AID
//        AIDDBDao aiddbDao = new AIDDBDao(context);
        List<AIDDB> aiddbList = AppContext.Companion.getInstance().getAidRepository().list();
//        List<AIDDB> aiddbList = aiddbDao.list();
        HashMap inMap = new HashMap();

        packData.downloadParamCount = aiddbList.size();
        //todo 待实现
        //从数据库中找出本地AID版本与平台AID版本不同的AID
        TLVData tag9F06 = new TLVData();
        if (aiddbList != null && aiddbList.size() > 0) {
            for (int i = 0; i < aiddbList.size(); i++) {
                AIDDB db = aiddbList.get(i);
                packData.downloadParamIndex = i + 1;
                if (db.getLatestBOCVersion().equals(db.getLocalBOCVersion())) {
                    continue;
                }
                MLog.i("找到需要下载的AID:" + db.getAID());
                tag9F06.Tag = 0x9F06;
                tag9F06.Value = StringUtil.hexStringToByte(db.getAID());
                tag9F06.Len = tag9F06.Value.length;

                GlobalParams.currentDownloadingAID = db;//解包时判断平台返回AID的是不是现在请求的AID
                bFindAID = true;
                break;
            }
            if (bFindAID == false) {
                return ERR_NO_NEED_UPDATE;
            }
        } else {
            //数据库错误,终端没有AID列表,需要先使用“请求查询AID参数版本”
            return ERR_NO_AID_LIST;
        }

        inMap.put("41", posData.terminalID);    //受卡机终端标识码(终端号)
        inMap.put("42", posData.merchantID);    //受卡方的标识码(商户号)
        inMap.put("60", "1");
        inMap.put("60.1", "00");// 交易类型码
        inMap.put("60.2", posData.batchNo);//批次号
        inMap.put("60.3", "380");//N3,网管管理信息码,IC 卡AID下载交易采用 "372" 文档12.4.7；
        inMap.put("62", TLVData.generateTLVHexString(tag9F06));

        try {
            packData.data_send = ProtocolUtil.com8583SendMessage(context, inMap, 0x0800);
        } catch (Exception e) {
            Log.e("TPPOSUtil", "packMessage_DownPara03 error");
            return ERR_PACK_UNKNOWERR;
        }

        return OK_SUCCESS;
    }

    public static int packMessage_DownLoadAIDParaFinish(Context context, POSData posData, PackData packData) {
        int ret = 0;
        HashMap inMap = new HashMap();
        inMap.put("41", posData.terminalID);    //受卡机终端标识码(终端号)
        inMap.put("42", posData.merchantID);    //受卡方的标识码(商户号)
        inMap.put("60", "1");  // 自定义域61

        inMap.put("60.1", "00");// 交易类型码
        inMap.put("60.2", posData.batchNo);//批次号
        inMap.put("60.3", "381");//N3,网管管理信息码,IC 卡参数下载结束交易采用 "381" 文档12.4.10；


        try {
            packData.data_send = ProtocolUtil.com8583SendMessage(context, inMap, 0x0800);
        } catch (Exception e) {
            e.printStackTrace();
            android.util.Log.e("TPPOSUtil", "packMessage_DownLoadCAPKParaFinish error");
            return ERR_PACK_UNKNOWERR;
        }

        return OK_SUCCESS;
    }


    public static int packMessage_DownParaNFC(Context context, POSData posData, PackData packData) {
        int ret = 0;
        HashMap inMap = new HashMap();

        inMap.put("41", posData.terminalID);    //受卡机终端标识码(终端号)
        inMap.put("42", posData.merchantID);    //受卡方的标识码(商户号)

        inMap.put("60", "1");
        inMap.put("60.1", "00");// 交易类型码
        inMap.put("60.2", posData.batchNo);//批次号
        inMap.put("60.3", "394");//N3,非接参数下载采用394

        try {
            packData.data_send = ProtocolUtil.com8583SendMessage(context, inMap, 0x0800);
        } catch (Exception e) {
            Log.e("TPPOSUtil", "packMessage_DownMasterKey error");
            return ERR_PACK_UNKNOWERR;
        }

        return OK_SUCCESS;
    }

    /**
     * 打包报文：IC卡公钥/AID/TMS 参数下载结束
     *
     * @param context
     * @param posData
     * @param packData
     * @return
     */
    public static int packMessage_DownParaNFCFinish(Context context, POSData posData, PackData packData) {
        int ret = 0;
        HashMap inMap = new HashMap();
        inMap.put("41", posData.terminalID);    //受卡机终端标识码(终端号)
        inMap.put("42", posData.merchantID);    //受卡方的标识码(商户号)
        inMap.put("60", "1");  // 自定义域61

        inMap.put("60.1", "00");// 交易类型码
        inMap.put("60.2", posData.batchNo);//批次号

        inMap.put("60.3", "395");//N3,网管管理信息码,


        try {
            packData.data_send = ProtocolUtil.com8583SendMessage(context, inMap, 0x0800);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("TPPOSUtil", "packMessage_DownParaEnd error");
            return ERR_PACK_UNKNOWERR;
        }

        return OK_SUCCESS;
    }

    //免密卡 bin 黑名单更新下载
    public static int packMessage_DownParaNFCBlackList(Context context, POSData posData, PackData packData) {
        HashMap inMap = new HashMap();
        String sFile62;
        if ("000".equals(posData.sFile62)) {

            List<NFCBlackListDB> list = AppContext.getInstance().getNfcBlackRepository().list();
            AppContext.getInstance().getNfcBlackRepository().deleteList(list);
            sFile62 = "000";
        } else {
//            NFCBlackListDB nfcBlackListDB = nfcBlackListDao.findByMaxId();
            var nfcBlackListDB = AppContext.getInstance().getNfcBlackRepository().findByMaxIdNfcBlackDB();
            if (nfcBlackListDB == null) {
                sFile62 = "000";
            } else {
                sFile62 = GlobalParams.strFormat("" + (Integer.parseInt(nfcBlackListDB.getBlackIndex()) + 1), 3);

            }
        }
        inMap.put("41", posData.terminalID);    //受卡机终端标识码(终端号)
        inMap.put("42", posData.merchantID);    //受卡方的标识码(商户号)
        inMap.put("60", "1");
        inMap.put("60.1", "00");// 交易类型码
        inMap.put("60.2", posData.batchNo);//批次号
        inMap.put("60.3", "398");//N3,网管管理信息码,
        inMap.put("62", StringUtil.bytesToHexString(sFile62.getBytes()));

        try {
            packData.data_send = ProtocolUtil.com8583SendMessage(context, inMap, 0x0800);
        } catch (Exception e) {
            Log.e("TPPOSUtil", "packMessage_DownPara03 error");
            return ERR_PACK_UNKNOWERR;
        }

        return OK_SUCCESS;
    }

    //免密卡 bin 黑名单更新下载结束
    public static int packMessage_DownParaNFCBlackListFinish(Context context, POSData posData, PackData packData) {
        int ret = 0;
        HashMap inMap = new HashMap();
        inMap.put("41", posData.terminalID);    //受卡机终端标识码(终端号)
        inMap.put("42", posData.merchantID);    //受卡方的标识码(商户号)
        inMap.put("60", "1");  // 自定义域61

        inMap.put("60.1", "00");// 交易类型码
        inMap.put("60.2", posData.batchNo);//批次号

        inMap.put("60.3", "399");//N3,网管管理信息码,

        try {
            packData.data_send = ProtocolUtil.com8583SendMessage(context, inMap, 0x0800);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("TPPOSUtil", "packMessage_DownParaEnd error");
            return ERR_PACK_UNKNOWERR;
        }

        return OK_SUCCESS;
    }


    /**
     * 查询数据库,看是否有CAPK需要更新
     *
     * @param context
     * @return
     */
    public static boolean checkIfNeedDownCAPK(android.content.Context context) {
        boolean bFindCAPK = false;//是否从数据库找到需要更新的AID
//        CAPKDBDao capkdbDao = new CAPKDBDao(context);
        List<CAPKDB> capkdbList = AppContext.Companion.getInstance().getCapkRepository().list();

        if (capkdbList != null && capkdbList.size() > 0) {
            for (CAPKDB db : capkdbList) {
                if (db.getLatestBOCVersion().equals(db.getLocalBOCVersion())) {
                    continue;//如果版本相同,则不用下载
                }
                bFindCAPK = true;
                break;
            }
        } else {
            MLog.i("Terminal No CAPK List");
            return false;
        }
        return bFindCAPK;
    }


    /**
     * 打包报文: 签到
     *
     * @param context
     * @param posData
     * @param packData
     * @return
     */
    public static int packMessage_SignIn(Context context, POSData posData, PackData packData) {
        int ret = 0;
        HashMap inMap = new HashMap();


        inMap.put("11", posData.orderNo);       //终端交易流水号
        inMap.put("41", posData.terminalID);    //受卡机终端标识码(终端号)
        inMap.put("42", posData.merchantID);    //受卡方的标识码(商户号)
        inMap.put("60", "1");
        inMap.put("60.1", "00");
        inMap.put("60.2", posData.batchNo);// 批次号
        inMap.put("60.3", "003");//单倍长001，双倍长003
        inMap.put("63", "1");
        inMap.put("63.1", posData.operaterID);

        try {
            packData.data_send = ProtocolUtil.com8583SendMessage(context, inMap, 0x0800);
        } catch (Exception e) {
            Log.e("TPPOSUtil", "packMessage_SignIn error");
            return ERR_PACK_UNKNOWERR;
        }


        return OK_SUCCESS;
    }


    public static void testDb() {
        List<DiaryEntry> field = AppContext.Companion.getInstance().getDiaryRepository().findAllByField("id", 1);

        MLog.i("field=" + field.get(0).getTitle());

        String aa = "0064303120202020202020202020706F2D50563230313931322D30303030333830320039600003000060330033703408000000000000C000143035333135383132383938333330313630313230303231001100001686370000129F0605A0000007909F220120";
        String bb = "0064303120202020202020202020706F2D50563230313931322D30303030333830320042600003000060330033703408000000000000C000143035333135383132383938333330313630313230303231001100001686370000129F0605A0000007909F220120";

    }
}
