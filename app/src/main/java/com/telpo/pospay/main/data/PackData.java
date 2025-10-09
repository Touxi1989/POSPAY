package com.telpo.pospay.main.data;


//组装8583报文类
public class PackData {
    public byte[] data_send;     // 发送的报文
    public byte[] data_receive;  // 接收的报文

    public String responseCode;  //响应码,天波返回(根据响应报文)
    public String unPackCode;    //解包结果码,天波返回(根据解包结果返回)
    public String authorizeCode; //授权码,天波返回(根据响应报文)

    public String reference;     //参考号,天波返回(根据响应报文)
    public String qrcode;     //支付二维码（根据返回报文的59域）
    public String orderState;     //微信、支付宝查询的订单状态（根据返回报文的59域）
    public String outtradeno;     //商户订单号（根据返回报文的59域）
    public Field57AdditionalData additionalData;//域57附加交易信息
    public String openId;//域55返回了微信、支付宝支付用户的openid

    public String orderNo;       //本次交易流水号,天波返回(根据响应报文)
    public String batchNo;       //本次交易批次号,天波返回(根据响应报文)
    public String date;          //本次交易日期,天波返回(根据响应报文)
    public String time;          //本次交易时间,天波返回(根据响应报文)
    public String clean_date;          //本次交易清算时间,天波返回(根据响应报文)

    //public String acquirerName;   //收单行名称,天波返回(根据响应报文)
    public String issuerName;        //发卡行名称,天波返回(根据响应报文)
    public String issuerCardType;    //卡类型,天波返回(根据响应报文)(61.4域,具体含义请参考中行规范)
    public int cardProperty;         //标记卡片是信用卡还是借记卡(根据61.4域解出来)


    public String availableBalance;   //可用余额,查余额时天波返回(根据响应报文)
    public String ledgeBalance;       //账户余额,查余额时天波返回(根据响应报文)

    //需要上层发起什么操作,上层根据这个马上发起对应的报文打包操作
    //例如平台要求签到后立即更新AID参数
    //例如需要马上发起冲正
    public int needActionType;


    //结算报文信息
    public int creditNums;       //贷记总笔数,(根据响应报文)
    public String creditAmounts; //贷记总金额,(根据响应报文)
    public int debitNums;        //借记总笔数,(根据响应报文)
    public String debitAmounts;  //借记总金额,(根据响应报文)
    public boolean isBalance;           //对账是否平,(根据响应报文)

    /**
     * 下载AID参数时使用
     */
    public String DF27;//参数下装报文索引号,打包"AID应用参数版本查询"报文时使用,首次查询时赋值为"00",后续查询时根据unPackData()的返回值传参


    public int downloadParamCount = 0;
    public int downloadParamIndex = 0;


    //下载参数块01的结果
//    public TerminalData terminalData;
//
//    //本次交易记录
//    public TranDB tranDB;
//
//    //存放本批次的打印记录
//    public java.util.List<TranDB> tranDBList;


}
