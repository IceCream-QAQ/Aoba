package aoba.isp.volcengine.cdn

import aoba.`fun`.buildJson
import aoba.isp.volcengine.VolcEngine
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName


class Domain(
    private val ve: VolcEngine
) {

    @Serializable
    data class ListResp(
        @SerialName("Data")
        val `data`: List<Data>,
        @SerialName("PageNum")
        val pageNum: Int,
        @SerialName("PageSize")
        val pageSize: Int,
        @SerialName("Total")
        val total: Int
    ) {
        @Serializable
        data class Data(
            @SerialName("Domain")
            val domain: String,
            @SerialName("ServiceType")
            val serviceType: String,
            @SerialName("Status")
            val status: String,
            @SerialName("Cname")
            val cname: String,
            @SerialName("ServiceRegion")
            val serviceRegion: String,
            @SerialName("CreateTime")
            val createTime: Long,
            @SerialName("UpdateTime")
            val updateTime: Long,
            @SerialName("Project")
            val project: String,
            @SerialName("OriginProtocol")
            val originProtocol: String,
            @SerialName("IPv6")
            val ipv6: Boolean,
            @SerialName("HTTPS")
            val https: Boolean,
            @SerialName("PrimaryOrigin")
            val primaryOrigin: List<String>,
            @SerialName("BackupOrigin")
            val backupOrigin: List<String>? = null,
            @SerialName("ResourceTags")
            val resourceTags: List<ResourceTag>,
            @SerialName("CacheShared")
            val cacheShared: String,
            @SerialName("CacheSharedTargetHost")
            val cacheSharedTargetHost: String,
            @SerialName("IsConflictDomain")
            val isConflictDomain: Boolean,
            @SerialName("BackupCname")
            val backupCname: String,
            @SerialName("ConfigStatus")
            val configStatus: String,
            @SerialName("DomainLock")
            val domainLock: DomainLock,
        ) {


            @Serializable
            data class DomainLock(
                @SerialName("Remark")
                val remark: String,
                @SerialName("Status")
                val status: String
            )

            @Serializable
            data class ResourceTag(
                val key: String,
                val value: String
            )
        }
    }

    /** 获取域名列表
     *
     * @see https://www.volcengine.com/docs/6454/75269
     */
    suspend fun list(
        domain: String? = null,
        serviceType: Cdn.ServiceType? = null,
        resourceTags: List<String>? = null,
        status: Cdn.Status? = null,
        project: String? = null,
        originProtocol: String? = null,
        ipv6: Boolean? = null,
        https: Boolean? = null,
        primaryOrigin: String? = null,
        pageNum: Long? = null,
        pageSize: Long? = null
    ) = ve.post<ListResp>(
        "CDN",
        "cn-north-1",
        "ListCdnDomains",
        "2021-03-01",
        buildJson {
            "Domain" to domain
            "ServiceType" to serviceType?.type
            "ResourceTags" to resourceTags
            "Status" to status?.status
            "Project" to project
            "OriginProtocol" to originProtocol
            "IPv6" to ipv6
            "Https" to https
            "PrimaryOrigin" to primaryOrigin
            "PageNum" to pageNum
            "PageSize" to pageSize
        }
    )

}