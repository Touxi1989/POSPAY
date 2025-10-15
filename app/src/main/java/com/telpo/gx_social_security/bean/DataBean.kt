package com.telpo.gx_social_security.bean

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal


data class DeviceTokenRequest(
    val appSecret: String,
    val appKey: String,
    val grantType: String,
    val scope: String
)

data class GgzpxxRequest(
    val aae397: String?, //招聘开始时间	String	aae397	否 格式yyyymm 不输入默认返回全部
    val current: Int, //页数	Integer	current	是
    val size: Int,  //每页条数	Integer	size	是
    val acb217: String?, //岗位名称	String	acb217	否	模糊搜索
    val acb202: String?, //工作地点	String	acb202	否	模糊搜索
)

data class BaseResponse<T>(
    val statusCode: String, //状态码	string	statusCode	返回值：200，300，400，401，403，500。200表示成功，其他均为失败
    val errorCode: String, //错误类型代码	string	errorCode
    val message: String, //错误信息	string	message
    val result: T, //返回结果集	string	result
    val uuid: String?,
)

data class GgzpxxResult(
    val total: Int?, //总条数
    val current: Int?, //当前页数
    val hitCount: Boolean?,
    val pages: Int?,
    val size: Int?, //每页条数
    val records: MutableList<RecordsDTO>,
    val searchCount: Boolean?,
    val orders: MutableList<*>?,
)

data class RecordsDTO(
    @SerializedName("ACB217")
    val acb217: String?, //岗位名称

    @SerializedName("ACB221")
    val acb221: Int?, //年龄上限

    @SerializedName("ACB242")
    val acb242: String?, //招聘女性人数

    @SerializedName("ACA112")
    val aca112: String?, //岗位类别

    @SerializedName("ACB222")
    val acb222: Int?, //年龄下限

    @SerializedName("DWLABEL")
    val dwlabel: String?, //食宿福利

    @SerializedName("ACB214")
    val acb214: String?, //月薪上限

    @SerializedName("ACB202")
    val acb202: String?, //工作地点

    @SerializedName("ACB213")
    val acb213: String?, //月薪下限

    @SerializedName("AAB004")
    val aab004: String?, //单位名称

    @SerializedName("AAE005")
    val aae005: String?, //联系电话

    @SerializedName("AAE159")
    val aae159: String?, //电子邮箱

    @SerializedName("ROW_ID")
    val rowId: Int?,

    @SerializedName("AAE004")
    val aae004: String?, //联系人

    @SerializedName("AAE398")
    val aae398: String?, //岗位截至日期

    @SerializedName("AAC011")
    val aac011: String?, //学历要求

    @SerializedName("AAE397")
    val aae397: String?, //岗位开始日期

    @SerializedName("AAE031")
    val aae031: String?, //单位招聘截止日期

    @SerializedName("ACB241")
    val acb241: String?, //招聘男性人数

    @SerializedName("AAE030")
    val aae030: String?, //单位招聘登记日期

    @SerializedName("ACB240")
    val acb240: Int? //招聘人数
)

data class TokenRequest(
    val username: String, //接口调用用户名	String		是
    val accesskey: String,    //接口调用密码	String 是
    val idNumber: String, //身份证	String	是
    val name: String, // 姓名	String	 是

)

data class TokenRespone(
    val access_token: String, //令牌
    val token_type: String, //令牌类型
    val expires_in: String, //过期时间
    val scope: String, //权限
    val girder_user: String,
    val jti: String,
)

data class UserInfosRequest(
    val username: String, //接口调用用户名	String		是
    val accesskey: String,    //接口调用密码	String 是
    val token: String, //令牌

)

data class UserInfosResponse(
    val associatedPersons: MutableList<AssociatedPersons>,

    )

data class AssociatedPersons(
    val id: Long,
    val personNumber: String, // 个人标号	String
    val name: String, //姓名	String
    val idNumber: String, // 证件号码	String
    val cardNumber: String, // 社保卡号	String
    val retireStatus: String, //
)

data class SbkzkjdRequest(
    val username: String, //	接口调用用户名	String是	联系人：王经理 18577797377
    val accesskey: String,  //接口调用密码	String是
    val token: String, // 令牌	String	是
    val personId: Long, //	个人编号	Long是
    val applySrc: Long,    //申报来源	Long	是	制卡申报来源（单位0、个人制卡1、app2、微信3、一体机4、学校6
    val idNumber: String, //	身份证号	String	是
    val name: String    //姓名	String是
)

data class SbkzkjdResponse(
    val cardProgress: String, //制卡进度（中心节点代码）1、提交制卡申请，2、数据采集完成，3、市级审批完成，4、省级复核完成，5、银行开户中，6、银行开户完成，7、卡片生产中，8、卡片生成完成，
    //    9、物流发出，10、经办机构签收，物流网点中，11、银行网点发放，12、本人领卡完成
    val receiveWay: String, //领取方式 银行领卡1或邮寄2
    val siteNumber: String, //领卡网点编号
    val siteName: String, //领卡网点名称
    val siteAddress: String, //领卡网点地址
    val mailingStatus: String, //邮寄状态 领卡方式为2必传,0未邮寄、1已邮寄
    val courierNumber: String, //快递单号
    val cardType: String, //卡类型  本地1，异地2
    val cardStatus: String, //卡状态
    val cardCity: String, //制卡城市
    val appcode: Int, //是否成功 1 成功 0 不成功
    val errmsg: String, //校验错误信息
    val cardBank: String, //开户银行

)

data class SbklsgsjgRequest(
    val username: String,    //接口调用用户名	String	是	联系人：王经理 18577797377
    val accesskey: String, //	接口调用密码	String是
    val token: String,    //令牌	String 是
    val personId: String, // 个人编号	Long	是
    val buzztType: String, //	业务类型	String 是	挂失1 解挂2
    val cardNumber: String,    // 社保卡号	String	是
    val applySrc: Long,    // 制卡申报来源	Long是	单位0、个人制卡1、app2、微信3、一体机4、学校6
    val idNumber: String, // 身份证号	String	是
    val name: String, //  姓名	String	是
    val cardType: String, //   制卡类型	String	是
    val guardianName: String?, //  监护人或代办人姓名	String	否
    val guardianIdNumber: String?, // 监护人或代办人身份证	String	否
    val guardianRelation: String?, // 	 监护人或代办人与被申请人关系	String否
    val guardianMobile: String?, //  监护人或代办人电话	String	否
    val mobile: String?,    //手机号码	String	否
)

data class SbklsgsjgResponse(
    val appcode: Int, // 成功标识	Integer1成功0不成功
    val errmsg: String, //  错误信息	String
    val eventId: String, //   eventId	String用于评价传递参数
    val applyType: String, //  业务类型	String
    val ok: Boolean,
)

data class YldyffxxRequest(
    val username: String,    //	接口调用用户名	String	 是	联系人：王经理 18577797377
    val accesskey: String,    // 接口调用密码	String		是
    val token: String,    //令牌	String		是
    val personId: Long,    //个人编号	Long	是
    val year: Long,    //	 年度	Long 是
    val applySrc: Long,    //	制卡申报来源	Long	 是	单位0、个人制卡1、app2、微信3、一体机4、学校6
)

data class YldyffxxResponse(
    val pensionTreatmentListDTO: MutableList<PensionTreatmentListDTO>,
)

data class PensionTreatmentListDTO(
    val grantDate: Long,    //	发放年月	Long
    val grantType: String,    //	发放途径	String
    val grantBank: String,    //	发放银行	String
    val bankAccount: String,    //	 银行账号	String
    val actualPensionAmount: BigDecimal,    //	养老待遇实发金额	BigDecimal
    val supplyFee: BigDecimal,    //	 补发金额	BigDecimal
    val refundAmount: BigDecimal,    //	 退发待遇金额	BigDecimal
    val grantState: String,    //	 发放状态	String
    val treatmentTypeNumber: String,    //	待遇类别编码	String
    val pensionAmount: BigDecimal,    //	养老待遇金额	BigDecimal
    val regularAmount: BigDecimal,    //	 社保定期代发金额	BigDecimal
    val evaluteType: String,    //	 核定方式	String
    val payType: String,    //	 支付类型	String
    val everyAmount: BigDecimal,    //	 定期待遇金额	BigDecimal
    val agency: String,    //	 发放机构名称	String
    val agencyCode: String,    //	 发放机构编码	String
)


data class GrcbxxRequest(
    val username: String,    //  接口调用用户名	String	是	联系人：王经理 18577797377
    val accesskey: String,    // 接口调用密码	String	 是
    val token: String,    // 令牌	String	是
    val personId: Long,    //个人编号	Long	是
    val applySrc: Long,    //	制卡申报来源	Long 是	单位0、个人制卡1、app2、微信3、一体机4、学校6
)

data class GrcbxxResponse(
    val personInsuredDTO: MutableList<PersonInsuredDTO>,
)

data class PersonInsuredDTO(
    val companyId: String,    // 参保单位代码	String
    val name: String,    // 参保单位名称	String
    val insuredCode: String,    // 险种类型	String
    val paymentState: String,    // 参保状态	String
    val startDate: Long,    // 首次参保日期	Long
    val startTime: String,    // 开始时间	String
    val endTime: String,    //终止时间	String
    val month: String,    //月数	String
    val personNumber: String,    // 个人编号	String
    val payState: String,    // 缴费状态	String
    val agency: String,    //  所属机构	String
    val agencyCode: String,    // 所属机构代码	String
)


data class GrjfmxRequest(
    val username: String, //接口调用用户名	String	是	联系人：王经理 18577797377
    val accesskey: String,  //接口调用密码	String	是
    val token: String, // 令牌	String 是
    val personId: Long, //个人编号	Long	是
    val startDate: Long, //开始日期	Long	是
    val endDate: Long,    // 终止日期	Long	是
    val insureType: String?,    // 险种类型	String	否
    val paymentState: String?, //	缴费状态	String	否
    val cpage: String?,    // 第几页	String	否
    val rows: String?, //  每页条数	String	否
    val accountSign: String?, //	到账标志	String	否	1-已足额到账 2-中断 3-在途
    val applySrc: Long, //	制卡申报来源	Long	是	单位0、个人制卡1、app2、微信3、一体机4、学校6
)

data class GrjfmxResponse(

    val items: MutableList<GrjfmxItem>,
)

data class GrjfmxItem(
    val companyName: String?, //  单位名称	String
    val paymentDate: Long?,  //  费款所属期	Long
    val insuranceType: String?, //  险种类型	String
    val paymentType: String?,  // 缴费类型	String
    val payBase: BigDecimal?, // 缴费基数	BigDecimal
    val personalPayment: BigDecimal?,  //个人缴纳	BigDecimal
    val personalInterestAmount: BigDecimal?,  // 个人缴利息	BigDecimal
    val companyPayment: BigDecimal?,  //  单位缴纳	BigDecimal
    val companyInterestAmount: BigDecimal?,  // 单位缴利息	BigDecimal
    val lateFee: BigDecimal?, //  滞纳金	BigDecimal
    val paymentSign: String?, //  缴费标志	String
    val totalAmount: BigDecimal?, // 缴费合计	BigDecimal
    val includedAmount: BigDecimal?,  //  划入账户金额	BigDecimal
    val arrivalDate: Long?, //   到账日期	Long
    val months: String?, //  缴费月数	String
    val accountSign: String?, //  到账标志	String
)






