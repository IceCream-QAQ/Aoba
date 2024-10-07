package aoba.isp.ct.cdn

import aoba.isp.ctyun.CtResp
import aoba.isp.ctyun.cdn.CdnProduct
import aoba.isp.ctyun.cdn.CtCDN
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class Domain(
    private val ct: CtCDN
) {

    @Serializable
    data class VerifyDomainOwnershipResp(
        val code: Int,
        val message: String,
        @SerialName("verify_result")
        val verifyResult: Boolean
    )

    /*** 域名归属权校验
     * @param domain 域名
     * @param verifyType 解析方法 1: DNS解析验证，2: 文件验证。默认1
     *
     * @see <a href="https://vip.ctcdn.cn/help/10005260/10014785/common/10014787">域名归属权校验</a>
     */
    suspend fun verifyDomainOwnership(domain: String, verifyType: Int? = null) =
        ct.get<VerifyDomainOwnershipResp>("/v1/verify_domain_ownership") {
            "domain" to domain
            "verifyType" to verifyType
        }

    @Serializable
    data class DomainOwnershipVerifyContentResp(
        @SerialName("code")
        val code: Int,
        @SerialName("content")
        val content: String,
        @SerialName("domain_zone")
        val domainZone: String,
        @SerialName("message")
        val message: String,
        @SerialName("verify_desc")
        val verifyDesc: String,
        @SerialName("verify_result")
        val verifyResult: Boolean
    )

    /*** 获取域名归属校验内容
     * @param domain 域名 仅支持单个，示例: aa.bb.ctyun.cn
     *
     * @see <a href="https://vip.ctcdn.cn/help/10005260/10014785/common/10016169">获取域名归属校验内容</a>
     */
    suspend fun domainOwnershipVerifyContent(domain: String) =
        ct.get<DomainOwnershipVerifyContentResp>("/v1/verify_domain_ownership/verify_content") {
            "domain" to domain
        }



    enum class CdnArea(val value: String) {
        // 中国大陆
        ChineseMainland("1"),
        // 海外
        Overseas("2"),
        // 全球
        Global("3"),
    }

    @Serializable
    data class CdnOrigin(
        // 源站ip或域名
        val origin: String,
        // 源站端口
        val port: Int,
        // 权重
        val weight: Int,
        // 源站角色
        val role: String
    )

    /*** 新增域名
     * @param domain 域名
     * @param productCode 产品类型
     * @param origin 源站信息
     * @param wafEnable 是否开启WAF
     * @param ddosEnable 是否开启 DDoS 防护
     * @param areaScope 加速区域
     * @param ipv6 是否开启 IPv6
     *
     * @see <a href="https://vip.ctcdn.cn/help/10005260/10014785/common/10014776">新增域名</a>
     */
    suspend fun manage(
        domain: String,
        productCode: CdnProduct,
        origin: Collection<CdnOrigin>,
        wafEnable: Boolean? = null,
        ddosEnable: Boolean? = null,
        areaScope: CdnArea? = null,
        ipv6: Boolean? = null,
    ) = ct.post<CtResp>("/v1/domain/manage") {
        "action" to 1
        "domain" to domain
        "product_code" to productCode.value
        "waf_enable" to wafEnable?.let { if (it) 1 else 2 }
        "ddos_enable" to ddosEnable?.let { if (it) 1 else 2 }
        "area_scope" to areaScope?.value
        "ipv6_enable" to ipv6?.let { if (it) 1 else 2 }
        "origin" to origin
    }
}