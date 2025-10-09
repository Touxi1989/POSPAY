package com.telpo.pospay.db.bean

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = CAPKDB.TABLE_NAME) // 指定表名
class CAPKDB : BaseBean() {

    companion object {
        const val TABLE_NAME = "capk_table"
    }


    @PrimaryKey
    var CAPKID: String = "" //用RID和KeyID拼接而成的字符串作为键值

    var LatestBOCVersion: String? = null //中行最新AID参数版本号


    var LocalBOCVersion: String? = null //本地AID参数的版本号


    var RID: String? = null //注册的应用提供商标识, BCTC测试后台表示为AID(9F06)


    var keyID: String? = null //CA公钥索引(9F22)


    var keyPosition: String? = null //CA公钥存储位置(DF28)//// TODO: 2017/3/7 0007 中行。。。  9F22和DF28是不同的...


    var CAPKSN: String? = null //CA公钥序列号(DF08)


    var expDate: String? = null //CA公钥有效期(DF05)


    var hashInd: String? = null //CA公钥哈什算法标识(DF06)


    var arithInd: String? = null //CA公钥(签名)算法标识(DF07)


    var modul: String? = null //CA公钥模(DF02)


    var exponent: String? = null //CA公钥指数/椭圆曲线参数(DF04)


    var checkSum: String? = null //CA公钥校验值(DF03)


    var bPOSPExist: Boolean =
        false //用于标识，查询到POSP是否存在。   在第一次查询，即62域=100时，将所有CAPK置false， 在下载成功CAPK后置true。


    var bEnable: Boolean = false //状态属性,根据这个决定是否存到EMV库


}