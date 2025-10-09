package com.telpo.pospay.main.util;


/**
 * 单位:广东天波信息技术股份有限公司 创建人:luyq 功能：通讯协议处理组件 日期:2014-1-20
 */
public class ProtocolUtil {

    //调试返回报文用
    public static boolean test = false;


    /**
     * 将inMap中数据转换成byte[]类型
     *
     * @param inMap 输入参数
     * @return 输出数据都存放到byte[]中
     */
    public static byte[] com8583SendMessage(android.content.Context context, java.util.HashMap inMap, int msgType)
            throws Exception {
        java.util.HashMap outMap = new java.util.HashMap();
        com.telpo.solab.iso8583.MessageFactory mfact = com.telpo.solab.iso8583.parse.ConfigParser.createFromClasspathConfig("config.xml");// 使用模板方式创建对象
        mfact.setAssignDate(false);// 去掉日期
        mfact.setMessageHeadBcd(true);
        mfact.setUseBinaryMessages(true);

        mfact.setIsoHeader(msgType, PackUtil.MESSAGE_TPDU + PackUtil.MESSAGE_Head);// 报文头

        com.telpo.solab.iso8583.IsoMessage reqIsoMessage = mfact.newMessage(msgType);//
        reqIsoMessage.setBinary(true);// NUMERIC数字都转BCD码
        com.telpo.base.util.MLog.i("nMap迭代");
        // inMap迭代
        java.util.Iterator iter = inMap.entrySet().iterator();
        while (iter.hasNext()) {
            java.util.Map.Entry e = (java.util.Map.Entry) iter.next();
            String key = (String) e.getKey();
            String value = "";
            if (!"MAK".equals(key)) {
                value = (String) e.getValue();
            }
            if (!"".equals(com.telpo.base.util.StringUtil.convertStringNull(value))) {
                if ("2".equals(key)) {
                    reqIsoMessage.setValue(2, value, com.telpo.solab.iso8583.IsoType.LLBIN, -1);// 主账号

                    int field2len = value.length();
                    String field2lenFormat = String.format("%02d", field2len);

//                    Log("field2:\nvalue: " + value + "\nvalue length: " +value.length() + "\nfield2lenFormat: " + field2lenFormat);

                    if (value.length() % 2 == 1) {//奇数右补零
                        reqIsoMessage.setValue(2, field2lenFormat + value + "0",
                                com.telpo.solab.iso8583.IsoType.BINARY, (field2len + 3) / 2);// IC卡数据域
                    } else {
                        reqIsoMessage.setValue(2, field2lenFormat + value,
                                com.telpo.solab.iso8583.IsoType.BINARY, (field2len + 2) / 2);
                    }

                } else if ("3".equals(key)) {
                    reqIsoMessage.setValue(3, Integer.parseInt(value),
                            com.telpo.solab.iso8583.IsoType.NUMERIC, 6);// 交易处理码
                } else if ("4".equals(key)) {
                    java.math.BigDecimal a = new java.math.BigDecimal(value);
                    reqIsoMessage.setValue(4, a,
                            com.telpo.solab.iso8583.IsoType.AMOUNT, -1);// Amount, Transaction  持卡人以收单行当地货币为货币单位的交易金额，但不包括与该交易相关的任何费用
                } else if ("11".equals(key)) {
                    reqIsoMessage.setValue(11, Integer.parseInt(value),
                            com.telpo.solab.iso8583.IsoType.NUMERIC, 6);// System Trace Audit Number 端交易流水号
                } else if ("12".equals(key)) {
                    reqIsoMessage.setValue(12, Integer.parseInt(value),
                            com.telpo.solab.iso8583.IsoType.NUMERIC, 6);// Time, Local Transaction 交易进入系统时当地时间 hhmmss
                } else if ("13".equals(key)) {
                    reqIsoMessage.setValue(13, Integer.parseInt(value),
                            com.telpo.solab.iso8583.IsoType.NUMERIC, 4);// Date，Local Transaction 交易进入系统的当地日期 MMDD
                } else if ("14".equals(key)) {
                    reqIsoMessage.setValue(14, Integer.parseInt(value),
                            com.telpo.solab.iso8583.IsoType.NUMERIC, 4);// Date Expiration 表示银行设计的在某年某月之后此卡“过期” YYMM
                } else if ("15".equals(key)) {
                    reqIsoMessage.setValue(15, Integer.parseInt(value),
                            com.telpo.solab.iso8583.IsoType.NUMERIC, 4);// 清算日期  MMDD
                } else if ("22".equals(key)) {
                    String sfile22 = value;
                    if (sfile22.length() < 4) {
                        sfile22 += "0";
                    }
                    reqIsoMessage.setValue(22, sfile22,
                            com.telpo.solab.iso8583.IsoType.NUMERIC, 4);// POS Entry Mode 使用数字编码指出PAN输入交换系统的方式以及POS终端PIN的输入能力
                } else if ("23".equals(key)) {
                    if (!"".equals(com.telpo.base.util.StringUtil.o2s(value))) {
                        reqIsoMessage.setValue(23, value,//Integer.parseInt(value),
                                com.telpo.solab.iso8583.IsoType.NUMERIC, 3);// Card Sequence Number 用于区分拥有相同Primary Account Number（PAN）（Field2）和Extended（Field34）不同的卡序列
                    }
                } else if ("25".equals(key)) {
                    reqIsoMessage.setValue(25, Integer.parseInt(value),
                            com.telpo.solab.iso8583.IsoType.NUMERIC, 2);//POS Condition Code 表示发生交易的终端状况， 用它可以表示交易来自什么终端
                } else if ("26".equals(key)) {
                    reqIsoMessage.setValue(26, Integer.parseInt(value),
                            com.telpo.solab.iso8583.IsoType.NUMERIC, 2);//服务点PIN获取码  该域描述了服务点设备所允许输入的个人密码明文的最大长度
                } else if ("35".equals(key)) {

                    int field35len = value.length();
                    String field35lenFormat = String.format("%02d", field35len);
                    if (value.length() % 2 == 1) {//奇数左补零
                        reqIsoMessage.setValue(35, field35lenFormat + value + "0",
                                com.telpo.solab.iso8583.IsoType.BINARY, (field35len + 3) / 2);// IC卡数据域
                    } else {
                        reqIsoMessage.setValue(35, field35lenFormat + value,
                                com.telpo.solab.iso8583.IsoType.BINARY, (field35len + 2) / 2);
                    }

                } else if ("36".equals(key)) {
                    int field36len = value.length();
                    String field36lenFormat = String.format("%04d", field36len);

                    if (value.length() % 2 == 1) {//奇数左补零
                        reqIsoMessage.setValue(36, field36lenFormat + value + "0",
                                com.telpo.solab.iso8583.IsoType.BINARY, (field36len + 5) / 2);// IC卡数据域
                    } else {
                        reqIsoMessage.setValue(36, field36lenFormat + value,
                                com.telpo.solab.iso8583.IsoType.BINARY, (field36len + 4) / 2);
                    }

                } else if ("37".equals(key)) {
                    reqIsoMessage.setValue(37, value, com.telpo.solab.iso8583.IsoType.ALPHA, 12);// 检索参考号(Retrieval Reference Number)
                } else if ("38".equals(key)) {
                    reqIsoMessage.setValue(38, value, com.telpo.solab.iso8583.IsoType.ALPHA, 6);// Authoritarian ID Response 授权标识应答码
                } else if ("39".equals(key)) {
                    reqIsoMessage.setValue(39, value, com.telpo.solab.iso8583.IsoType.ALPHA, 2);// Response Code 应答码
                } else if ("41".equals(key)) {
                    reqIsoMessage.setValue(41, value, com.telpo.solab.iso8583.IsoType.ALPHA, 8);// 受卡机终端标识码(Card Acceptor Terminal Identification)
                } else if ("42".equals(key)) {
                    reqIsoMessage.setValue(42, value, com.telpo.solab.iso8583.IsoType.ALPHA, 15);// 受卡方标识码(Card Acceptor Identification Code)
                    //com.telpo.base.util.MLog.i("field42:\nvalue: " + value + "\nvalue length: " +value.length() );
                } else if ("44".equals(key)) {
                    reqIsoMessage.setValue(44, value, com.telpo.solab.iso8583.IsoType.LLVAR, -1);// Additional Response Data 在响应或者授权其他交易需要时，提供额外的补充数据（在交易响应消息中返回接收机构和收单机构的标识码。）
                } else if ("48".equals(key)) {
                    reqIsoMessage.setValue(48, value, com.telpo.solab.iso8583.IsoType.LLLBIN, -1);// Additional Date Private Use

                } else if ("49".equals(key)) {
                    reqIsoMessage.setValue(49, value, com.telpo.solab.iso8583.IsoType.ALPHA, 3);// Currency Code ， Transaction  Field4使用的货币单位
                } else if ("52".equals(key)) {
                    reqIsoMessage.setValue(52, value, com.telpo.solab.iso8583.IsoType.BINARY, 8);//Personal ID Number（PIN）Data 此域中包含PIN自己或者包含其变异体
                } else if ("53".equals(key)) {
                    reqIsoMessage.setValue(53, value, com.telpo.solab.iso8583.IsoType.NUMERIC, 16);//安全控制信息(Security Related Control Information )
                } else if ("54".equals(key)) {
                    reqIsoMessage.setValue(54, value, com.telpo.solab.iso8583.IsoType.LLLVAR, -1);// Additional Amounts
                } else if ("55".equals(key)) {
                    int field55len = value.length() / 2;
                    String field55lenFormat = String.format("%04d", field55len);
                    reqIsoMessage.setValue(55, field55lenFormat + value,
                            com.telpo.solab.iso8583.IsoType.BINARY, field55len + 2);// IC卡数据域
                } else if ("57".equals(key)) {
                    //21号文，终端硬件序列号及密文数据
                    int field57len = value.length() / 2;
                    String field57lenFormat = String.format("%04d", field57len);
                    reqIsoMessage.setValue(57, field57lenFormat + value,
                            com.telpo.solab.iso8583.IsoType.BINARY, field57len + 2);// IC卡数据域
                } else if ("58".equals(key)) {
                    reqIsoMessage.setValue(58, value, com.telpo.solab.iso8583.IsoType.LLLVAR, -1);// PBOC电子钱包标准的交易信息（PBOC_ELECTRONIC_DATA）
                } else if ("59".equals(key)) {
                    //21号文，终端硬件序列号及密文数据
//                    int field59len = value.length() / 2;
//                    String field59lenFormat = String.format("%04d", field59len);
//                    reqIsoMessage.setValue(59, field59lenFormat + value,
//                            IsoType.BINARY, field59len + 2);// IC卡数据域
//                    if (inMap.containsKey("59_s") && inMap.get("59_s").equals("18")) {//如果是59域的用法18则用key=59_s区分
//                        reqIsoMessage.setValue(59, value, IsoType.LLLBIN, -1);
//                    } else {
                    reqIsoMessage.setValue(59, value, com.telpo.solab.iso8583.IsoType.LLLVAR, -1);
//                    }
                } else if ("60".equals(key)) {
                    //域 60

                    com.telpo.base.util.MLog.i("组60域");
                    String strField601 = "";
                    String strField602 = "";
                    String strField603 = "";
                    String strField604 = "";
                    String strField605 = "";
                    if (inMap.get("60.1") != null) {
                        com.telpo.solab.iso8583.IsoValue field601 = new com.telpo.solab.iso8583.IsoValue(com.telpo.solab.iso8583.IsoType.NUMERIC,
                                Integer.parseInt(inMap.get("60.1").toString()), 2); //消息类型码 N2
                        strField601 = ((Object) (field601.toString())).toString();
                    }

                    if (inMap.get("60.2") != null) {
                        com.telpo.solab.iso8583.IsoValue field602 = new com.telpo.solab.iso8583.IsoValue(com.telpo.solab.iso8583.IsoType.NUMERIC,
                                Integer.parseInt(inMap.get("60.2").toString()), 6); //批次号 N6
                        strField602 = ((Object) (field602.toString())).toString();
                    }

                    if (inMap.get("60.3") != null) {
                        com.telpo.solab.iso8583.IsoValue field603 = new com.telpo.solab.iso8583.IsoValue(com.telpo.solab.iso8583.IsoType.NUMERIC,
                                Integer.parseInt(inMap.get("60.3").toString()), 3); //网络管理信息码 N3
                        strField603 = ((Object) (field603.toString())).toString();
                    }

                    if (inMap.get("60.4") != null) {
                        com.telpo.solab.iso8583.IsoValue field604 = new com.telpo.solab.iso8583.IsoValue(com.telpo.solab.iso8583.IsoType.NUMERIC,
                                Integer.parseInt(inMap.get("60.4").toString().substring(0, 1)), 1); //终端读取能力 N1
                        strField604 = ((Object) (field604.toString())).toString();
                    }

                    if (inMap.get("60.5") != null) {
                        com.telpo.solab.iso8583.IsoValue field605 = new com.telpo.solab.iso8583.IsoValue(com.telpo.solab.iso8583.IsoType.NUMERIC,
                                Integer.parseInt(inMap.get("60.5").toString()), 1); //基于 PBOC 借/贷记标准的 IC 卡条件代码 N1
                        strField605 = ((Object) (field605.toString())).toString();
                    }
                    reqIsoMessage.setValue(60, strField601 + strField602 + strField603 + strField604 + strField605,
                            com.telpo.solab.iso8583.IsoType.LLLBIN, -1);
                } else if ("62".equals(key)) {
                    com.telpo.base.util.MLog.i("组62域");
                    if (value.equals("20")) {
                        value = (String) inMap.get("62.1");
                        int field62len = value.length() / 2;
                        String field62lenFormat = String.format("%04d", field62len);
                        reqIsoMessage.setValue(62, field62lenFormat + value,
                                com.telpo.solab.iso8583.IsoType.BINARY, field62len + 2);//
                    } else {
                        if (msgType == 0x0820) {
                            reqIsoMessage.setValue(62, value, com.telpo.solab.iso8583.IsoType.LLLVAR, -1);
                        } else {
                            int field62len = value.length() / 2;
                            String field62lenFormat = String.format("%04d", field62len);
                            reqIsoMessage.setValue(62, field62lenFormat + value,
                                    com.telpo.solab.iso8583.IsoType.BINARY, field62len + 2);//
                        }
                    }


                } else if ("61".equals(key)) {
//                处理61域，原始信息域(Original Message)
//                N...029(LLLVAR)，3个字节的长度值＋最大29个字节的数字字符域，
//                压缩时用右靠BCD码表示的2个字节的长度值＋用左靠BCD码表示的最大15个字节的数据。
                    String strField611 = "";
                    String strField612 = "";
                    String strField613 = "";
                    String strField614 = "";
                    String strField615 = "";

                    if (inMap.get("61.1") != null) {
                        com.telpo.solab.iso8583.IsoValue field611 = new com.telpo.solab.iso8583.IsoValue(com.telpo.solab.iso8583.IsoType.NUMERIC,
                                Integer.parseInt(inMap.get("61.1").toString()), 6); //原始交易批次号 N6
                        strField611 = ((Object) (field611.toString())).toString();
                    }
                    if (inMap.get("61.2") != null) {
                        com.telpo.solab.iso8583.IsoValue field612 = new com.telpo.solab.iso8583.IsoValue(com.telpo.solab.iso8583.IsoType.NUMERIC,
                                Integer.parseInt(inMap.get("61.2").toString()), 6); //原始交易 POS 流水号 N6
                        strField612 = ((Object) (field612.toString())).toString();
                    }
                    if (inMap.get("61.3") != null) {
                        com.telpo.solab.iso8583.IsoValue field613 = new com.telpo.solab.iso8583.IsoValue(com.telpo.solab.iso8583.IsoType.NUMERIC,
                                Integer.parseInt(inMap.get("61.3").toString()), 4); //原始交易日期 N4
                        strField613 = ((Object) (field613.toString())).toString();
                    }
                    if (inMap.get("61.4") != null) {
                        com.telpo.solab.iso8583.IsoValue field614 = new com.telpo.solab.iso8583.IsoValue(com.telpo.solab.iso8583.IsoType.NUMERIC,
                                Integer.parseInt(inMap.get("61.2").toString()), 2); //原交易授权方式 N2
                        strField614 = ((Object) (field614.toString())).toString();
                    }
                    if (inMap.get("61.5") != null) {
                        com.telpo.solab.iso8583.IsoValue field615 = new com.telpo.solab.iso8583.IsoValue(com.telpo.solab.iso8583.IsoType.NUMERIC,
                                Integer.parseInt(inMap.get("61.3").toString()), 11); //原交易授权机构代码 N11
                        strField615 = ((Object) (field615.toString())).toString();
                    }

                    reqIsoMessage.setValue(61, strField611 + strField612 + strField613 + strField614 + strField615,
                            com.telpo.solab.iso8583.IsoType.LLLBIN, -1);
                } else if ("63".equals(key)) {
                    //操作员代码
                    //      表示POS终端操作员代码，用作在POS签到和批结算交易中上送到POS中心，应答时原路返回。
                    String strField631 = "";
                    String strField632 = "";
                    if (inMap.get("63.1") != null) {
                        com.telpo.solab.iso8583.IsoValue field631 = new com.telpo.solab.iso8583.IsoValue(com.telpo.solab.iso8583.IsoType.NUMERIC,
                                Integer.parseInt(inMap.get("63.1").toString()), 3);
                        strField631 = ((Object) (field631.toString())).toString();
                    }
                    if (inMap.get("63.2") != null) {
                        com.telpo.solab.iso8583.IsoValue field632 = new com.telpo.solab.iso8583.IsoValue(com.telpo.solab.iso8583.IsoType.LLLVAR,
                                inMap.get("63.2").toString(), -1);
                        strField632 = ((Object) (field632.toString())).toString();
                    }
                    reqIsoMessage.setValue(63, strField631 + strField632, com.telpo.solab.iso8583.IsoType.LLLVAR, -1);
                } else if ("64".equals(key)) {
                    reqIsoMessage.setValue(64, value, com.telpo.solab.iso8583.IsoType.BINARY, 8);

                }
            }
        }


        //计算64域(MAC)
        if (inMap.containsKey("64")) {
            reqIsoMessage.setField64();
        }
        // 打印输入HashMap内容
        com.telpo.base.util.MLog.i("发送数据：");
        com.telpo.base.util.StringUtil.printHashMapContent(inMap);
        java.io.ByteArrayOutputStream bout = new java.io.ByteArrayOutputStream();
        reqIsoMessage.write(bout, 2);
        byte[] bytes = bout.toByteArray();
        //修改报文头的长度位值
        //byte[] temp = com.telpo.base.util.ByteArrayUtil.byteArrayTrimHead(bytes, 36);
        //byte[] len = com.telpo.base.util.DataProcessUtil.int2Byte(temp.length, 2);
        //bytes[0]=len[0];
        //bytes[1]=len[1];
        return bytes;
    }


    /**
     * 所有协议调用统一出入口
     *
     * @param data 输入参数
     * @return 输出数据都存放到HashMap中
     */
    public static java.util.HashMap<String, String> parse8583ReceiveMessage(byte[] data, int msgType)
            throws Exception {
        java.util.HashMap outMap = new java.util.HashMap();
        com.telpo.solab.iso8583.MessageFactory mfact = com.telpo.solab.iso8583.parse.ConfigParser.createFromClasspathConfig("config.xml");// 使用模板方式创建对象
        mfact.setAssignDate(false);// 去掉日期
        mfact.setMessageHeadBcd(true);
        mfact.setUseBinaryMessages(true);
//        mfact.setIsoHeader(msgType, GlobalParams.MESSAGE_TPDU + GlobalParams.MESSAGE_Head);// 报文头

        //       IsoMessage reqIsoMessage = mfact.newMessage(msgType);
//        reqIsoMessage.setBinary(true);// NUMERIC数字都转BCD码


        try {
            String respHeader = mfact.getIsoHeader(msgType);
            com.telpo.base.util.MLog.i("respHeader : " + respHeader);
            com.telpo.base.util.MLog.i("parasdata:"+ com.telpo.base.util.DataProcessUtil.bytesToHexString(data));
            com.telpo.solab.iso8583.IsoMessage respIsoMessage = mfact.parseMessage(data, 11);

            // 迭代输出map
            if (respIsoMessage == null) {
                com.telpo.base.util.MLog.i("ProtocolGetOutmap respIsoMessage: Null");
            }
            com.telpo.solab.iso8583.IsoValue returnFields[] = respIsoMessage.getFields();
            com.telpo.base.util.MLog.i("File:" + returnFields.length);
            // 设置只需解析64个域
            for (int i = 0; i < returnFields.length && i <= 64; i++) {
                com.telpo.solab.iso8583.IsoValue returnFieldsI = returnFields[i];
                if (returnFieldsI != null) {
                    com.telpo.base.util.MLog.i("File:" + i);
                }
                if (null != returnFieldsI && i != 28 && i != 60 && i != 9 && i != 62 && i != 61 && i != 53 && i != 52) {
                    com.telpo.solab.iso8583.IsoType dataType = respIsoMessage.getField(i).getType();
                    if (dataType == com.telpo.solab.iso8583.IsoType.DATE4 || dataType == com.telpo.solab.iso8583.IsoType.DATE10
                            || dataType == com.telpo.solab.iso8583.IsoType.DATE_EXP
                            || dataType == com.telpo.solab.iso8583.IsoType.TIME) {
                        outMap.put(i + "", com.telpo.solab.iso8583.util.IsoTyptFormatUtil.format(
                                (java.util.Date) respIsoMessage.getField(i).getValue(),
                                null, respIsoMessage.getField(i).getType()));
                    } else if (dataType == com.telpo.solab.iso8583.IsoType.LLLBIN
                            || dataType == com.telpo.solab.iso8583.IsoType.LLBIN) {
                        outMap.put(i + "", com.telpo.base.util.DataProcessUtil.bytesToHexString((byte[]) respIsoMessage.getField(i).getValue()));// LLLVAR-BINARY的输出格式
                    } else if (dataType == com.telpo.solab.iso8583.IsoType.BINARY) {
                        //outMap.put(i + "", (byte[]) respIsoMessage.getField(i).getValue());
                        outMap.put(i + "", com.telpo.base.util.DataProcessUtil.bytesToHexString((byte[]) respIsoMessage.getField(i).getValue()));// LLLVAR-BINARY的输出格式
                    } else if (dataType == com.telpo.solab.iso8583.IsoType.NUMERIC) {
                        com.telpo.base.util.MLog.i("" + i);
                        outMap.put(i + "", String.format("%0" + respIsoMessage.getField(i).getLength() + "d", Integer.parseInt(String.valueOf(respIsoMessage
                                .getField(i).getValue()))));
                    } else {
                        outMap.put(i + "", String.valueOf(respIsoMessage
                                .getField(i).getValue()));
                    }
                } else if (null != returnFieldsI
                        && (i == 9 || i == 61 || i == 28 || i == 54 || i == 60 || i == 62 || i == 53 | i == 52)) {
                    if (i == 9) {
                        Object objField9 = returnFieldsI.getValue();
                        String strField9 = String.valueOf(respIsoMessage.getField(i).getValue());
                        int len = strField9.charAt(0) - 48;
                        outMap.put("9", (len != 7 ? strField9.substring(1, 8 - len) : "0") + "." + strField9.substring(8 - len));
                    } else if (i == 28) {
                        String strField28 = String.valueOf(respIsoMessage.getField(i).getValue());
                        int len = strField28.charAt(0) - 48;
                        outMap.put("9", strField28.substring(1, 8 - len) + "." + strField28.substring(8 - len));
                    } else if (i == 52) {
                        outMap.put(i + "", String.valueOf(respIsoMessage.getField(i).getValue()));
                    } else if (i == 53) {
                        outMap.put(i + "", String.valueOf(respIsoMessage.getField(i).getValue()));
                    } else if (i == 54) {
                        Object objField54 = returnFieldsI.getValue();
                        String strField54 = (String) objField54;
                        int idx = 0;
                        outMap.put("54.1", strField54.substring(idx, idx + 2));
                        idx += 2;
                        outMap.put("54.2", strField54.substring(idx, idx + 2));
                        idx += 2;
                        outMap.put("54.3", strField54.substring(idx, idx + 3));
                        idx += 3;
                        outMap.put("54.4", strField54.substring(idx, idx + 1));
                        idx += 1;
                        String strfield545 = strField54
                                .substring(idx, idx + 12);
                        String strField545Change = strfield545.substring(0, 10)
                                + "." + strfield545.substring(10);
                        double dblField545Change = Double
                                .parseDouble(strField545Change);
                        java.text.DecimalFormat myformat = new java.text.DecimalFormat();
                        myformat.applyPattern("######.00");
                        outMap.put("54.5", myformat.format(dblField545Change)
                                + "");
                        idx += 12;
                    } else if (i == 60) {

                        Object objField60 = returnFieldsI.getValue();
                        byte[] f60 = (byte[]) objField60;
                        com.telpo.base.util.MLog.i("F60:" + com.telpo.base.util.StringUtil.bytesToHexString(f60));

                        byte[] head = com.telpo.base.util.ByteArrayUtil.byteArrayGetHead(f60, 1);
                        outMap.put("60.1", com.telpo.base.util.StringUtil.bytesToHexString(head));//交易类型码
                        byte[] f60temp = com.telpo.base.util.ByteArrayUtil.byteArrayTrimHead(f60, 1);

                        head = com.telpo.base.util.ByteArrayUtil.byteArrayGetHead(f60temp, 3);
                        outMap.put("60.2", com.telpo.base.util.StringUtil.bytesToHexString(head));//批次号
                        f60temp = com.telpo.base.util.ByteArrayUtil.byteArrayTrimHead(f60temp, 3);
                        head = com.telpo.base.util.ByteArrayUtil.byteArrayGetHead(f60temp, 2);
                        outMap.put("60.3", com.telpo.base.util.StringUtil.bytesToHexString(head).substring(0, 3));//网络管理信息码,003双倍长密钥算法
                    } else if (i == 62) {
                        Object objField62 = returnFieldsI.getValue();
                        byte[] f62 = (byte[]) objField62;
                        com.telpo.base.util.MLog.i("F62:" + com.telpo.base.util.StringUtil.bytesToHexString(f62));
                        outMap.put("62", com.telpo.base.util.StringUtil.bytesToHexString(f62));
                    }
                }
            }
            outMap.put("resultCode", "00");
            //下面判断MAC
            //暂时不检测MAC
            boolean bCheckMAC = false;
            if (bCheckMAC) {
                if (!outMap.containsKey("64")) {
                    com.telpo.base.util.MLog.i("无校验MAC--64域");
                    outMap.put("resultCode", "994");//校验MAC错误
                } else {
                    byte[] macIn = com.telpo.base.util.ByteArrayUtil.byteArrayTrimHead(data, (respHeader.length() + 10) / 2);
                    //MLog.i("去掉报文头"+StringUtil.bytesToHexString(macIn));
                    String macValueStr = ((String) outMap.get("64")).substring(0, 16);
                    //MLog.i("Mac:" + macValueStr);

                    String Temp = com.telpo.base.util.StringUtil.bytesToHexString(macIn);
                    int Index = Temp.indexOf(macValueStr);
                    Temp = Temp.substring(0, Index);
                    //MLog.i("去掉MAC后:" + Temp);
                    macIn = com.telpo.base.util.StringUtil.hexStringToByte(Temp);

                    byte[] macCal = new byte[8];
//                    int ret = PinpadService.TP_PinpadGetMac(GlobalParams.currMacKeyIndex, macIn, macCal, PinpadService.MAC_PBOC);
                    String macCalStr = com.telpo.base.util.StringUtil.bytesToHexString(macCal);
//                    MLog.i("ret=" + ret + "\nCheckMAC MacGenerate return:" + macCalStr);
                    if (macCalStr.equalsIgnoreCase(macValueStr)) {
                        outMap.put("resultCode", "00");// 通信、解包成功(只是通信解包成功,并不是说平台返回成功)
                    } else {
                        outMap.put("resultCode", "994");//校验MAC错误
                    }
                }
            }


            // 打印输出HashMap内容
            com.telpo.base.util.MLog.i("接收数据： ");
            com.telpo.base.util.StringUtil.printHashMapContent(outMap);


            //// TODO: 2017/3/17 0017
        } catch (java.text.ParseException e) {
            e.printStackTrace();
            outMap.put("resultCode", "995");// 解析返回信息异常
        } catch (Exception e) {
            e.printStackTrace();
            outMap.put("resultCode", "999");// 其他异常
        }

        return outMap;
    }


}
