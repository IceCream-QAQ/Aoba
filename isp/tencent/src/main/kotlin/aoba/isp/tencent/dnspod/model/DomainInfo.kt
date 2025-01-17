package aoba.isp.tencent.dnspod.model

import aoba.json.EnableDisableBooleanSerializer
import aoba.json.YesNoBooleanSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** 域名详情
 * @see https://cloud.tencent.com/document/api/1427/56185#DomainInfo
 */
@Serializable
data class DomainInfo(
    // 系统分配给域名的唯一标识
    @SerialName("DomainId")
    val domainId: Int,
    // 域名的状态，正常：ENABLE，暂停：PAUSE，封禁：SPAM
    @SerialName("Status")
    @Serializable(DomainStatus.Serializer::class)
    val status: DomainStatus,
    // 域名的套餐等级代码
    @SerialName("Grade")
    val grade: String,
    // 域名所属的分组Id
    @SerialName("GroupId")
    val groupId: Int,
    @SerialName("IsMark")
    @Serializable(YesNoBooleanSerializer::class)
    val isMark: Boolean,
    // 域名默认的解析记录默认TTL值
    @SerialName("TTL")
    val ttl: Int,
    @Serializable(EnableDisableBooleanSerializer::class)
    // 是否开启CNAME加速，开启：ENABLE，未开启：DISABLE
    @SerialName("CnameSpeedup")
    val cnameSpeedup: Boolean,
    // 域名备注说明
    @SerialName("Remark")
    val remark: String,
    // 经过punycode编码后的域名格式
    @SerialName("Punycode")
    val punycode: String,
    // DNS 设置状态，错误：DNSERROR，正常：空字符串
    @SerialName("DnsStatus")
    val dnsStatus: String,
    // 域名的NS列表
    @SerialName("DnspodNsList")
    val dnsPodNsList: List<String>,
    @SerialName("Domain")
    // 域名
    val domain: String,
    // 域名套餐等级对应的序号
    @SerialName("GradeLevel")
    val gradeLevel: Int,
    // 域名所属的用户ID
    @SerialName("UserId")
    var userId: Int,
    // 是否是付费套餐
    @Serializable(YesNoBooleanSerializer::class)
    @SerialName("IsVip")
    val isVip: Boolean,
    //域名所属账号，示例值：abc@tencent.com
    @SerialName("Owner")
    val owner: String,
    @SerialName("GradeTitle")
    // 套餐名称
    val gradeTitle: String,
    // 域名添加时间，示例值：2020-05-21 16:08:29
    @SerialName("CreatedOn")
    val createdOn: String,
    // 域名更新时间，示例值：2021-04-01 18:09:58
    @SerialName("UpdatedOn")
    val updatedOn: String,
    // 域名所属的用户ID
    @SerialName("Uin")
    var uin: String,
    // 域名实际使用的NS列表
    @SerialName("ActualNsList")
    val actualNsList: List<String>? = null,
    // 域名下的记录数量
    @SerialName("RecordCount")
    val recordCount: Int,
    // 域名所有者的账户昵称
    @SerialName("OwnerNick")
    val ownerNick: String,
    // 是否在付费套餐宽限期
    @SerialName("IsGracePeriod")
    @Serializable(YesNoBooleanSerializer::class)
    val isGracePeriod: Boolean,
    // 是否在付费套餐宽限期
    @SerialName("VipBuffered")
    @Serializable(YesNoBooleanSerializer::class)
    val vipBuffered: Boolean,
    // 付费套餐开通时间，示例值：2021-04-07 13:34:20
    @SerialName("VipStartAt")
    val vipStartAt: String? = null,
    // 付费套餐到期时间，示例值：2022-04-07 13:34:20
    @SerialName("VipEndAt")
    val vipEndAt: String? = null,
    // 域名是否开通VIP自动续费，是：YES，否：NO，默认：DEFAULT
    @SerialName("VipAutoRenew")
    val vipAutoRenew: String? = null,
    // 是否是子域名。
    @SerialName("IsSubDomain")
    val isSubDomain: Boolean? = null,
    @SerialName("TagList")
    val tagList: List<TagItem>? = null,
    // 是否开启搜索引擎推送优化，是：YES，否：NO
    @SerialName("SearchEnginePush")
    @Serializable(YesNoBooleanSerializer::class)
    val searchEnginePush: Boolean,
    // 是否开启辅助 DNS
    @SerialName("SlaveDNS")
    @Serializable(YesNoBooleanSerializer::class)
    val slaveDNS: Boolean,
)