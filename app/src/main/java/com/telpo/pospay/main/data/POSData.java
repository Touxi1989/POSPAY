package com.telpo.pospay.main.data;


//打包报文所需参数类
public class POSData {
    //用法1:读卡前输入参数,根据读卡方式轮询卡片
    //用法2:读卡后输出参数,告知最终读到什么卡
    public int readCardType;   //POS读卡方式;非接、IC、磁条

    //交易输入
    public int transType;      //交易类型,参考TPPOSUtil.java
    public String transAmount; //交易金额, 单位元,保留小数点后两位; 如消费金额12元,则transAmount = "12.00"
    public String reqDate;     //交易日期, 格式为"YYYYMMDD";如2017年1月1日, 则reqDate = "20170101"
    public String reqTime;     //交易时间, 格式为"HHMMSS";如18点30分00秒, 则reqTime = "183000"
    public String merchantName;//商户名, 字符串
    public String merchantID;  //商户号, 长度为15的字符串
    public String terminalID;  //终端号, 长度为8的字符串
    public String terminalSN;  //终端序列号, 长度为14的字符串
    public String operaterID;  //操作员号, 长度为3的字符串,如"999"

    public String oldRefNo;    //原交易参考号
    public String oldBatchNo;  //原交易批次号,长度为6的字符串,前补0; 如“000001”;
    public String oldTraceID;  //原交易流水号,长度为6的字符串,前补0; 如“000001”;
    public String oldAuthCode; //原交易授权码
    public String oldTransDate;//原交易日期,格式:MMDD(月月日日)
    public String oldTransTime;//原交易时间,格式:HHMMSS(时时分分秒秒)
    //交易输出参数:
    public String orderNo;     //交易流水号, 打包报文时返回
    public String batchNo;     //交易批次号, 打包报文时返回

    public int creditNums;       //贷记总笔数,(打包结算报文时返回)
    public String creditAmounts; //贷记总金额,(打包结算报文时返回)
    public int debitNums;        //借记总笔数,(打包结算报文时返回)
    public String debitAmounts;  //借记总金额,(打包结算报文时返回)

    public int  bankConsumeNums;            //银行卡消费笔数
    public String bankConsumeAmounts;       //银行卡消费金额
    public int bankConsumeVoidNums;        //银行卡消费撤销笔数
    public String bankConsumeVoidAmounts;   //银行卡消费撤销金额
    public int bankRufundNums;              //银行卡退货笔数
    public String bankRufundAmounts;        //银行卡退货金额
    public  int bankPreAuthEndNums;         //银行卡预授权完成笔数
    public  String bankPreAuthEndAmounts;   //银行卡预授权完成金额
    public  int bankTotalNums;             //银行卡总笔数
    public  String bankTotalAmounts;       //银行卡总金额

    public int alipayConsumeNums;          //支付宝消费总笔数
    public String alipayConsumeAmounts;    //支付宝消费总金额
    public int alipayConsumeVoidNums;          //支付宝消费撤销总笔数
    public String alipayConsumeVoidAmounts;    //支付宝消费总撤销金额
    public int alipayTatolNums;             //支付宝总笔数
    public String alipayTatolAmounts;       //支付宝总金额


    public int wechatConsumeNums;          //微信消费总笔数
    public String wechatConsumeAmounts;    //微信消费总金额
    public int wechatConsumeVoidNums;          //微信消费撤销总笔数
    public String wechatConsumeVoidAmounts;    //微信消费总撤销金额
    public int wechatTatolNums;             //微信总笔数
    public String wechatTatolAmounts;       //微信总金额

    //非接免签免密相关
    public String noSignLimitAmount;       //免签限额(分)
    public String noPWDLimitAmount;        //免密限额(分)
    public boolean bAllowNoSign;           //是否允许免签
    public boolean bAllowNoPWD;            //是否允许免密

    //IC卡、NFC卡、磁条卡返回的参数
    public String sPAN;          //卡号
    public String sEXPDate;      //卡有效期(YYMM)
    public int cardProperty;     //标记卡片是信用卡还是借记卡(这个只是终端根据AID判断的,最终结果以PackData.cardProperty为准)

    //IC卡、NFC卡处理流程返回的参数
    public String sEMV_CID;     //EMV参数,密文信息数据,9F27
    public String sEMV_UNPR_NO; //EMV参数,不可预知数,9F37
    public String sEMV_TransDate;//EMV参数,交易日期,9A
    public String sEMV_TransTYPE;//EMV参数,交易类型,9C
    public String sEMV_AuthAmt;  //EMV参数,授权金额,9F02
    public String sEMV_TransCurCode;//EMV参数,交易货币代码,5F2A
    public String sEMV_AIP;     //EMV参数,应用交互特征,82
    public String sEMV_TerCtryCode; //EMV参数,终端国家代码,9F1A
    public String sEMV_OtherAmt;  //EMV参数,其它金额,9F03
    public String sEMV_TERM_CAPA;//EMV参数,终端性能,9F33
    public String sEMV_EC_IAC; //EMV参数,电子现金发卡行授权码,9F74
    public String sEMV_PAN;     //EMV参数,PAN(个人主账号),5A
    public String sEMV_CSN;     //EMV参数,应用主账号序列号,卡片序列号,5F34
    public String sEMV_CVMR;    //EMV参数,持卡人验证方法结果,9F34
    public String sEMV_APPLAB;   //EMV参数,应用标签,50
    public String sEMV_Track2Equal;  //EMV参数,来自芯片卡的2磁道等价数据,57
    public String sEMV_EXPDate;  //EMV参数,卡有效期,5F24
    public String sEMV_TVR;      //EMV参数,终端验证结果,95
    public String sEMV_ARQC;    //EMV参数,授权请求密文,9F26
    public String sEMV_TSI;     //EMV参数,交易状态信息,9B
    public String sEMV_AID;     //EMV参数,应用标识符,9F06,PBOC贷记应用:A000000333010102; PBOC借记应用:A000000333010101
    public String sEMV_ATC;     //EMV参数,应用交易计数器,9F36
    public String sEMV_IAD;      //EMV参数,发卡行应用数据,9F10

    //磁条卡处理流程返回的参数
    public String sTrack1;      //读自磁条卡的第一磁道数据
    public String sTrack2;      //读自磁条卡的第二磁道数据
    public String sTrack3;      //读自磁条卡的第三磁道数据


    /**
     * 域62：下载参数时使用 长度为3
     * 1、第一次查询写“100”。
     * 2、非第一次时第一个字节填“1”，后面两个表示前面收到的CAPK个数或者AID个数。
     */
    public String sFile62;

    /**
     * DF27: 下载AID参数时使用
     */
    //参数下装报文索引号,打包"AID应用参数版本查询"报文时使用
    //查询时需要发送多次报文,首次查询时赋值为"00",后续查询时根据unPackData()的返回值传参
    public String DF27Value;

    public String Reversal_Reason_Code;//冲正原因码,98:在时限内未收到pos中心的应答；96：由于POS故障无法完成交易；A0：验证MAC出错；06：其他情况引发冲正


    public String field53;
    public String field62; //电子签名用，存放电子签名压缩后的数据
    public String TransactionCharacteristicCode;
    public String payCodeInfo;//40字节支付码field59

    public Field57AdditionalData field57AdditionalData;

    public static com.telpo.pospay.main.data.POSData posData;//自己

    /**
     * 构造函数
     */
    public POSData() {
        this.transAmount = "";
        this.batchNo = "";
        this.orderNo = "";
        this.oldRefNo = "";
        this.oldTraceID = "";
        this.oldAuthCode = "";
        this.oldTransDate = "";
        this.oldTransTime = "";
        this.oldBatchNo = "";
        this.operaterID = "";
        this.bAllowNoSign = false;
        this.bAllowNoPWD = false;
    }


}
