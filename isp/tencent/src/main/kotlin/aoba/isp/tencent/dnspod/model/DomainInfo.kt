package aoba.isp.tencent.dnspod.model

import aoba.json.EnableDisableBooleanSerializer
import aoba.json.YesNoBooleanSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DomainInfo(
    // 系统分配给域名的唯一标识
    @SerialName("DomainId")
    val domainId: Int,
    // 域名的原始格式
    @SerialName("Name")
    val name: String,
    // 域名的状态，正常：ENABLE，暂停：PAUSE，封禁：SPAM
    @SerialName("Status")
    val status: DomainStatus,
    // 域名默认的解析记录默认TTL值
    @SerialName("TTL")
    val ttl: Int,
    @Serializable(EnableDisableBooleanSerializer::class)
    // 是否开启CNAME加速，开启：ENABLE，未开启：DISABLE
    @SerialName("CNAMESpeedup")
    val cnameSpeedup: Boolean,
    // DNS 设置状态，错误：DNSERROR，正常：空字符串
    @SerialName("DNSStatus")
    val dnsStatus: String,
    // 域名的套餐等级代码
    @SerialName("Grade")
    val grade: String,
    // 域名所属的分组Id
    @SerialName("GroupId")
    val groupId: Int,
    // 是否开启搜索引擎推送优化，是：YES，否：NO
    @SerialName("SearchEnginePush")
    @Serializable(YesNoBooleanSerializer::class)
    val searchEnginePush: Boolean,
    // 域名备注说明
    @SerialName("Remark")
    val remark: String,
    // 经过punycode编码后的域名格式
    @SerialName("Punycode")
    val punycode: String,
    // 系统为域名分配的有效DNS
    @SerialName("EffectiveDNS")
    val effectiveDNS: List<String>,
    // 域名套餐等级对应的序号
    @SerialName("GradeLevel")
    val gradeLevel: Int,
    @SerialName("GradeTitle")
    // 套餐名称
    val gradeTitle: String,
    // 是否是付费套餐
    @Serializable(YesNoBooleanSerializer::class)
    @SerialName("IsVip")
    val isVip: Boolean,
    // 付费套餐开通时间，示例值：2021-04-07 13:34:20
    @SerialName("VipStartAt")
    val vipStartAt: String,
    // 付费套餐到期时间，示例值：2022-04-07 13:34:20
    @SerialName("VipEndAt")
    val vipEndAt: String,
    // 域名是否开通VIP自动续费，是：YES，否：NO，默认：DEFAULT
    @SerialName("VipAutoRenew")
    val vipAutoRenew: String,
    // 域名下的记录数量
    @SerialName("RecordCount")
    val recordCount: Int,
    // 域名添加时间，示例值：2020-05-21 16:08:29
    @SerialName("CreatedOn")
    val createdOn: String,
    // 域名更新时间，示例值：2021-04-01 18:09:58
    @SerialName("UpdatedOn")
    val updatedOn: String,
    //域名所属账号，示例值：abc@tencent.com
    @SerialName("Owner")
    val owner: String,
    // 是否是子域名。
    @SerialName("IsSubDomain")
    val isSubDomain: Boolean? = null,
    // 共享模式。r-只读；w-可写；rw-可读写。
    @SerialName("Mode")
    val mode: String? = null,
    @SerialName("OwnerQcloudUin")
    val ownerQCloudUin: String? = null,
    @SerialName("TagList")
    val tagList: List<TagItem>,
    // 增值服务数量
    @SerialName("VASCount")
    val vasCount: Int,
)