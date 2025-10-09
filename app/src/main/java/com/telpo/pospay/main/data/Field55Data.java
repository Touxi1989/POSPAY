package com.telpo.pospay.main.data;

import com.telpo.emv.util.TLVData;

/**
 * Created by yemiekai on 2017/3/3 0003.
 */

public class Field55Data {
    public com.telpo.emv.util.TLVData tag9F26;//应用密文
    public com.telpo.emv.util.TLVData tag9F27;//密文信息数据
    public com.telpo.emv.util.TLVData tag9F10;//发卡行应用数据
    public com.telpo.emv.util.TLVData tag9F37;//不可预知数
    public com.telpo.emv.util.TLVData tag9F36;//应用交易计数器
    public com.telpo.emv.util.TLVData tag95;  //终端验证结果
    public com.telpo.emv.util.TLVData tag9A;  //交易日期
    public com.telpo.emv.util.TLVData tag9C;  //交易类型
    public com.telpo.emv.util.TLVData tag9F02;//授权金额
    public com.telpo.emv.util.TLVData tag5F2A;//交易货币代码
    public com.telpo.emv.util.TLVData tag82;  //应用交互特征
    public com.telpo.emv.util.TLVData tag9F1A;//终端国家代码
    public com.telpo.emv.util.TLVData tag9F03;//其他金额
    public com.telpo.emv.util.TLVData tag9F33;//终端性能
//    public TLVData tag9F74;//电子现金发卡行授权码
    public com.telpo.emv.util.TLVData tag9F34;//持卡人验证方法结果CVM
    public com.telpo.emv.util.TLVData tag9F35;//终端类型
    public com.telpo.emv.util.TLVData tag9F1E;//接口设备序列号
    public com.telpo.emv.util.TLVData tag84;  //专用文件名称
    public com.telpo.emv.util.TLVData tag9F09;//应用版本号
    public com.telpo.emv.util.TLVData tag9F41;//ATC交易序列计数器
    public com.telpo.emv.util.TLVData tag9F63;//卡产品标识

    public Field55Data() {
        tag9F26 = new com.telpo.emv.util.TLVData();
        tag9F27 = new com.telpo.emv.util.TLVData();
        tag9F10 = new com.telpo.emv.util.TLVData();
        tag9F37 = new com.telpo.emv.util.TLVData();
        tag9F36 = new com.telpo.emv.util.TLVData();
        tag95 = new com.telpo.emv.util.TLVData();
        tag9A = new com.telpo.emv.util.TLVData();
        tag9C = new com.telpo.emv.util.TLVData();
        tag9F02 = new com.telpo.emv.util.TLVData();
        tag5F2A = new com.telpo.emv.util.TLVData();
        tag82 = new com.telpo.emv.util.TLVData();
        tag9F1A = new com.telpo.emv.util.TLVData();
        tag9F03 = new com.telpo.emv.util.TLVData();
        tag9F33 = new com.telpo.emv.util.TLVData();
//        tag9F74 = new TLVData();
        tag9F34 = new com.telpo.emv.util.TLVData();
        tag9F35 = new com.telpo.emv.util.TLVData();
        tag9F1E = new com.telpo.emv.util.TLVData();
        tag84 = new com.telpo.emv.util.TLVData();
        tag9F09 = new com.telpo.emv.util.TLVData();
        tag9F41 = new com.telpo.emv.util.TLVData();
        tag9F63 = new com.telpo.emv.util.TLVData();
    }

    /**
     * 把55域的TLV组合起来,返回HexString, 例如“9F2701009F25081122334455667788"
     *
     * @param field55Data
     * @return
     */
    public static String prepareField55(com.telpo.pospay.main.data.Field55Data field55Data) {
        String ret = "";

        ret += com.telpo.emv.util.TLVData.generateTLVHexString(field55Data.tag9F26);
        ret += com.telpo.emv.util.TLVData.generateTLVHexString(field55Data.tag9F27);
        ret += com.telpo.emv.util.TLVData.generateTLVHexString(field55Data.tag9F10);
        ret += com.telpo.emv.util.TLVData.generateTLVHexString(field55Data.tag9F37);
        ret += com.telpo.emv.util.TLVData.generateTLVHexString(field55Data.tag9F36);
        ret += com.telpo.emv.util.TLVData.generateTLVHexString(field55Data.tag95);
        ret += com.telpo.emv.util.TLVData.generateTLVHexString(field55Data.tag9A);
        ret += com.telpo.emv.util.TLVData.generateTLVHexString(field55Data.tag9C);
        ret += com.telpo.emv.util.TLVData.generateTLVHexString(field55Data.tag9F02);
        ret += com.telpo.emv.util.TLVData.generateTLVHexString(field55Data.tag5F2A);
        ret += com.telpo.emv.util.TLVData.generateTLVHexString(field55Data.tag82);
        ret += com.telpo.emv.util.TLVData.generateTLVHexString(field55Data.tag9F1A);
        ret += com.telpo.emv.util.TLVData.generateTLVHexString(field55Data.tag9F03);
        ret += com.telpo.emv.util.TLVData.generateTLVHexString(field55Data.tag9F33);
//        ret += TLVData.generateTLVHexString(field55Data.tag9F74);
        ret += com.telpo.emv.util.TLVData.generateTLVHexString(field55Data.tag9F34);
        ret += com.telpo.emv.util.TLVData.generateTLVHexString(field55Data.tag9F35);
        ret += com.telpo.emv.util.TLVData.generateTLVHexString(field55Data.tag9F1E);
        ret += com.telpo.emv.util.TLVData.generateTLVHexString(field55Data.tag84);
        ret += com.telpo.emv.util.TLVData.generateTLVHexString(field55Data.tag9F09);
        ret += com.telpo.emv.util.TLVData.generateTLVHexString(field55Data.tag9F41);
        ret += com.telpo.emv.util.TLVData.generateTLVHexString(field55Data.tag9F63);
        return ret;
    }
}
