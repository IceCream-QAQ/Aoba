package aoba.isp.tencent.dnspod

import aoba.`fun`.buildJson
import aoba.isp.tencent.TencentCloud
import aoba.isp.tencent.dnspod.model.DomainCountInfo
import aoba.isp.tencent.dnspod.model.DomainInfo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class Domain(private val qCloud: TencentCloud) {
    enum class ListType(val value: String) {
        All("ALL"),
        Mine("MINE"),
        Share("SHARE"),
        IsMark("ISMARK"),
        Pause("PAUSE"),
        Vip("VIP"),
        Recent("RECENT"),
        ShareOut("SHARE_OUT"),
        Free("free")
    }

    data class TagItemFilter(
        val key: String,
        val values: Collection<String>
    )

    @Serializable
    data class ListResponse(
        @SerialName("RequestId")
        val requestId: String,
        @SerialName("DomainCountInfo")
        val count: DomainCountInfo,
        @SerialName("DomainList")
        val list: List<DomainInfo>
    )

    suspend fun list(
        type: ListType? = null,
        offset: Int? = null,
        limit: Int? = null,
        groupId: Int? = null,
        keyword: String? = null,
        filter: TagItemFilter? = null
    ) = qCloud.post<ListResponse>(
        "dnspod",
        "DescribeDomainList",
        "ap-guangzhou",
        "2021-03-23",
        buildJson {
            "Type" to type?.value
            "Offset" to offset
            "Limit" to limit
            "GroupId" to groupId
            "Keyword" to keyword
            "Filter" to filter
        }
    )
}