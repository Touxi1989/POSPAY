package com.telpo.pospay.main.data;

/**
 * Created by xuxl150808 on 2018/1/6.
 * 域57附加交易信息
 */

public class Field57AdditionalData {
    //<Id>打印终端号</Id><Cd>打印用商户号</Cd><Nm>打印用商户名称</Nm><Desc>应答码解释</Desc>
    public String Id = "";//打印终端号
    public String Cd = "";//打印用商户号
    public String Nm = "";//打印用商户名称
    public String Desc = "";//应答码解释

    public void paraseAdditionalData(String field57) {
        if (field57.contains("<Id>") && field57.contains("</Id>")) {
            Id = field57.substring(field57.indexOf("<Id>") + 4, field57.indexOf("</Id>"));
        }
        if (field57.contains("<Cd>") && field57.contains("</Cd>")) {
            Cd = field57.substring(field57.indexOf("<Cd>") + 4, field57.indexOf("</Cd>"));
        }
        if (field57.contains("<Nm>") && field57.contains("</Nm>")) {
            Nm = field57.substring(field57.indexOf("<Nm>") + 4, field57.indexOf("</Nm>"));
        }
        if (field57.contains("<Desc>") && field57.contains("</Desc>")) {
            Desc = field57.substring(field57.indexOf("<Desc>") + 6, field57.indexOf("</Desc>"));

        }

    }
}
