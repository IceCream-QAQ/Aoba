package aoba.isp.aliyun.dns

import aoba.`fun`.buildJson
import aoba.`fun`.stringTreeMap
import aoba.isp.aliyun.Aliyun
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class Domain(
    private val aliyun: Aliyun
) {

    @Serializable
    data class ListResp(
        @SerialName("TotalCount")
        val total: Long,
        @SerialName("PageNumber")
        val page: Long,
        @SerialName("PageSize")
        val size: Long,
        @SerialName("Domains")
        val domains: DomainsBody
    ){
        @Serializable
        data class DomainsBody(
            @SerialName("Domain")
            val domain: List<DomainEntry>
        )
        @Serializable
        data class DomainEntry(
            @SerialName("DomainId")
            val domainId: String,
            @SerialName("DomainName")
            val domainName: String,
            @SerialName("PunyCode")
            val punycode: String,
        )

    }

    suspend fun list(
        lang: String? = null,
        keyword: String? = null,
        groupId: String? = null,
        pageNumber: Long? = null,
        pageSize: Long? = null,
        accurate: Boolean? = null,
        resourceGroup: String? = null,
        starMark: Boolean? = null,
    ) = aliyun.post<ListResp>(
        "alidns.cn-hangzhou.aliyuncs.com",
        "DescribeDomains",
        "2015-01-09",
        stringTreeMap {
            "Lang" to lang
            "KeyWord" to keyword
            "GroupId" to groupId
            "PageNumber" to pageNumber
            "PageSize" to pageSize
            "SearchMode" to accurate?.let { if (it) "EXACT" else "LIKE" }
            "ResourceGroupId" to resourceGroup
            "Starmark" to starMark?.toString()
        },
        buildJson {
            "Lang" to lang
            "KeyWord" to keyword
            "GroupId" to groupId
            "PageNumber" to pageNumber
            "PageSize" to pageSize
            "SearchMode" to accurate?.let { if (it) "EXACT" else "LIKE" }
            "ResourceGroupId" to resourceGroup
            "Starmark" to starMark
        }
    )

}