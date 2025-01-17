package aoba.isp.ct.cdn

import aoba.isp.ctyun.CtResp
import aoba.isp.ctyun.cdn.*
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
        val content: String? = null,
        @SerialName("domain_zone")
        val domainZone: String? = null,
        @SerialName("message")
        val message: String? = null,
        @SerialName("verify_desc")
        val verifyDesc: String? = null,
        @SerialName("verify_result")
        val verifyResult: Boolean? = null
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


    @Serializable
    data class ManageResp(
        val code: Int,
        val message: String,
        val domainZone: String? = null,
        val zoneType: String? = null,
        val zoneHost: String? = null,
        val zoneRecord: String? = null,
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
    ) = ct.post<ManageResp>("/v1/domain/manage") {
        "action" to 1
        "domain" to domain
        "product_code" to productCode.value
        "waf_enable" to wafEnable?.let { if (it) 1 else 2 }
        "ddos_enable" to ddosEnable?.let { if (it) 1 else 2 }
        "area_scope" to areaScope?.value
        "ipv6_enable" to ipv6?.let { if (it) 1 else 2 }
        "origin" to origin
    }

    @Serializable
    data class QueryResp(
        val code: Int,
        val message: String,
        val total: Int? = null,
        @SerialName("total_count")
        val totalCount: Int? = null,
        val page: Int? = null,
        @SerialName("page_count")
        val pageCount: Int? = null,
        @SerialName("page_size")
        val pageSize: Int? = null,
        val result: List<DomainInfo>? = null
    )

    @Serializable
    data class DomainInfo(
        val domain: String,
        val cname: String,
        @SerialName("product_code")
        @Serializable(CdnProduct.Serializer::class)
        val productCode: CdnProduct,
        @Serializable(CdnStatus.Serializer::class)
        val status: CdnStatus,
        @SerialName("insert_date")
        val insertData: Long,
        @SerialName("area_scope")
        @Serializable(CdnArea.Serializer::class)
        val areaScope: CdnArea,
        @SerialName("record_num")
        val recordNum: String
    )

    /** 查询域名列表
     *
     * @see <a href="https://vip.ctcdn.cn/help/10005260/10014785/common/10014801">查询域名列表</a>
     */
    suspend fun query(
        domain: String? = null,
        productCode: CdnProduct? = null,
        status: CdnStatus? = null,
        areaScope: CdnArea? = null,
        page: Int? = null,
        pageSize: Int? = null
    ) = ct.get<QueryResp>("/v2/domain/query") {
        "domain" to domain
        "product_code" to productCode?.value
        "status" to status?.value
        "area_scope" to areaScope?.value
        "page" to page
        "page_size" to pageSize
    }

    @Serializable
    data class RefererConfig(
        // referer是否允许为空 取值"on", “off”，默认“off”
        @SerialName("allow_empty")
        val allowEmpty: String? = null,
        @SerialName("allow_list")
        // referer黑名单列表 默认[]
        val allowList: Collection<String>? = null,
        @SerialName("referer_empty_protocol")
        // referer允许空协议 取值"on", “off”，默认off
        val refererEmptyProtocol: String? = null,
        @SerialName("match_all_ports")
        // referer是否匹配所有端口 枚举值：on、off；默认off
        val matchAllPorts: String? = null,
        @SerialName("ignore_case")
        // referer是否忽略大小写 枚举值：on、off；默认off
        val ignoreCase: String? = null,
        @SerialName("is_append")
        /** 是否追加referer黑名单列表
         * 是否在原来的黑名单列表基础上追加黑名单，取值 1:追加, 0:覆盖，不传默认0覆盖。
         * 当传入的值为1的时候，将当前传入的allow_list追加到已有配置的黑名单列表。
         * 当传入值0或者不传值的时候，当前传入的allow_list覆盖已有配置的黑名单列表
         */
        val isAppend: Int? = null,
        @SerialName("except_list")
        // referer例外名单
        val exceptList: Collection<String>? = null,
    )

    @Serializable
    data class UserAgentConfig(
        // 类型 枚举值：0（黑名单），1（白名单）
        val type: Int,
        /** user_agent列表，多个用英文,隔开
         * 他文档上类型是 list，但说明用 , 分隔，我不理解。
         */
        val ua: List<String>
    )

    @Serializable
    data class FiletypeTtlSingle(
        // 缓存类型 1（不缓存）；2（遵循源站）； 3（强制缓存）。
        @SerialName("cache_type")
        val cacheType: Int,
        // 是否带参数缓存 0（不带参数缓存）；1（带参数缓存）；
        @SerialName("cache_with_args")
        val cacheWithArgs: Int,
        /** 缓存文件类型，多个以逗号隔开
         * 当模式mode为文件后缀时,缓存文件类型例如："jpg,png,css"（以 "," 分割）
         * 当模式mode为目录时，缓存文件类型例如 "/test", "/a/b/c"（不能以 "/" 结尾）
         * 当模式mode为首页时，缓存文件类型固定为 "/"
         * 当模式mode为全部文件时，缓存文件类型固定为 "/"
         * 当模式mode为全路径时，缓存文件类型例如 "/index.html", "/test/\*.jpg"
         */
        @SerialName("file_type")
        val fileType: String,
        // 缓存时间，单位秒 取值范围：0-94608000。
        val ttl: Int,
        // 模式 0（文件后缀）；1（目录）； 2（首页）；3（全部文件）；4:（全路径），默认0
        val mode: Int? = null,
        // 	优先级 范围：1-100，默认10。
        val priority: Int? = null,
    )

    @Serializable
    data class ReqHeadersSingle(
        val key: String,
        val value: String? = null
    )

    @Serializable
    data class RespHeadersSingle(
        val key: String,
        val value: String? = null,
        @SerialName("cors_check")
        val cors: String? = null
    )

    @Serializable
    data class ErrorCodeSingle(
        // 错误状态码
        val code: Collection<Int>,
        // 缓存时间 单位秒,取值范围：0-94608000。
        val ttl: Int
    )

    @Serializable
    data class HttpsConfig(
        @SerialName("force_status")
        val forceStatus: String? = null,
        @SerialName("http_force")
        val httpForce: String? = null,
        @SerialName("https_force")
        val httpsForce: String? = null,
        @SerialName("origin_protocol")
        val originProtocol: String
    )

    @Serializable
    data class BasicConf(
        // 是否拉取跳转后文件 0（否）；1（是）
        @SerialName("follow_302")
        val follow302: Int? = null,
        @SerialName("use_http2")
        // 是否开启http2 取值：0（不开启）；1（开启）；默认0，该字段只有在证书开启状态下才会有效
        val http2: Int? = null,
        @SerialName("http_origin_port")
        // http请求回源端口 未传不修改；不支持443端口
        val httpOriginPort: Int? = null,
        @SerialName("https_origin_port")
        // https请求回源端口 未传不修改；取值范围：1-65535
        val httpsOriginPort: Int? = null,
    )

    /** 增量修改域名配置
     * 修改域名之前，您需要先开通对应产品类型的服务，且保证资源包/按需服务有效；
     * 该域名没有在途工单；
     * 单个用户一分钟限制调用10次。
     *
     * @param domain 域名
     * @param productCode 产品类型
     * @param ipv6 是否开启 IPv6
     * @param origin 源站信息
     * @param reqHost 回源 Host 设置
     * @param originHostType 回源 Host 类型
     * @param originHostHttp 回源 Host HTTP头设置
     * @param backupOriginTimeout 备份源站超时时间
     * @param backupOriginRespTimeout 备份源站响应超时时间
     * @param blackReferer 黑名单 referer 配置
     * @param blackRefererCondition 黑名单 referer 条件配置
     * @param whiteReferer 白名单 referer 配置
     * @param whiteRefererCondition 白名单 referer 条件配置
     * @param userAgent User-Agent 配置
     * @param filetypeTtl 文件类型缓存配置
     * @param blackIps IP 黑名单
     * @param whiteIps IP 白名单
     * @param reqHeaders 请求头配置
     * @param respHeaders 响应头配置
     * @param errorCode 错误码配置
     * @param https 是否开启 HTTPS
     * @param certName 证书名称
     * @param httpsConfig HTTPS 配置
     */
    suspend fun increUpdate(
        domain: String,
        productCode: CdnProduct? = null,
        ipv6: Boolean? = null,
        origin: Collection<CdnOrigin>? = null,
        reqHost: String? = null,
        originHostType: Boolean? = null,
        originHostHttp: Map<String, String>? = null,
        backupOriginTimeout: String? = null,
        backupOriginRespTimeout: String? = null,
        deleteReferer: Boolean? = null,
        blackReferer: RefererConfig? = null,
        blackRefererCondition: RefererConfig? = null,
        whiteReferer: RefererConfig? = null,
        whiteRefererCondition: RefererConfig? = null,
        userAgent: UserAgentConfig? = null,
        filetypeTtl: Collection<FiletypeTtlSingle>? = null,
        blackIps: String? = null,
        whiteIps: String? = null,
        reqHeaders: Collection<ReqHeadersSingle>? = null,
        respHeaders: Collection<RespHeadersSingle>? = null,
        errorCode: Collection<ErrorCodeSingle>? = null,
        https: Boolean? = null,
        certName: String? = null,
        httpsConfig: HttpsConfig? = null,
        tlsVersions: String? = null,
        basicConf: BasicConf? = null,
    ) = ct.post<CtResp>("/v1/domain/incre_update") {
        "domain" to domain
        "product_code" to productCode?.value
        "ipv6_enable" to ipv6?.let { if (it) 1 else 2 }
        "origin" to origin
        "req_host" to reqHost
        "origin_host_type" to originHostType?.let { if (it) "on" else "off" }
        "origin_host_http" to originHostHttp
        "backup_origin_timeout" to backupOriginTimeout
        "backup_origin_resp_timeout" to backupOriginRespTimeout
        if (deleteReferer == true) "white_referer" to emptyMap<String,String>()
        "black_referer" to blackReferer
        "black_referer_condition" to blackRefererCondition
        "white_referer" to whiteReferer
        "white_referer_condition" to whiteRefererCondition
        "user_agent" to userAgent
        "filetype_ttl" to filetypeTtl
        "ip_black_list" to blackIps
        "ip_white_list" to whiteIps
        "req_headers" to reqHeaders
        "resp_headers" to respHeaders
        "error_code" to errorCode
        "https_status" to https?.let { if (it) "on" else "off" }
        "cert_name" to certName
        "https_basic" to httpsConfig
        "ssl" to tlsVersions
        "basic_conf" to basicConf
    }
}