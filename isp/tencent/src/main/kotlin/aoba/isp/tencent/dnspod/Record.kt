package aoba.isp.tencent.dnspod

import aoba.dns.RecordType
import aoba.`fun`.buildJson
import aoba.isp.tencent.TencentCloud
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class Record(private val qCloud: TencentCloud) {

    @Serializable
    data class AddRecordResp(
        @SerialName("RecordId")
        val recordId: String,
        @SerialName("RequestId")
        val requestId: String
    )

    suspend fun addRecord(
        domain: String,
        subDomain: String,
        recordType: RecordType,
        value: String,
        recordLine: String = "默认",
        ttl: Int? = null,
        weight: Int? = null,
        mx: Int? = null,
        remark: String? = null,
    ) = qCloud.post<AddRecordResp>(
        "dnspod",
        "CreateRecord",
        "",
        "2021-03-23",
        buildJson {
            "Domain" to domain
            "SubDomain" to subDomain
            "RecordType" to recordType.value
            "Value" to value
            "TTL" to ttl
            "RecordLine" to recordLine
            "Weight" to weight
            "MX" to mx
            "Remark" to remark
        }
    )

    @Serializable
    data class DeleteRecordResp(
        @SerialName("RequestId")
        val requestId: String
    )

    suspend fun deleteRecord(
        domain: String,
        recordId: String
    ) = qCloud.post<DeleteRecordResp>(
        "dnspod",
        "DeleteRecord",
        "",
        "2021-03-23",
        buildJson {
            "Domain" to domain
            "RecordId" to recordId.toLong()
        }
    )

    @Serializable
    data class ListResp(
        @SerialName("RecordCountInfo")
        val page: Page,
        @SerialName("RecordList")
        val list: List<RecordItem>,
        @SerialName("RequestId")
        val requestId: String
    ) {
        @Serializable
        data class Page(
            @SerialName("SubDomainCount")
            val subDomainCount: Int? = null,
            @SerialName("ListCount")
            val listCount: Int,
            @SerialName("TotalCount")
            val totalCount: Int,
        )

        @Serializable
        data class RecordItem(
            @SerialName("RecordId")
            val recordId: String,
            @SerialName("Value")
            val value: String,
            @SerialName("Status")
            val status: String,
            @SerialName("UpdatedOn")
            val updateOn: String? = null,
            @SerialName("Name")
            val name: String,
            @SerialName("Line")
            val line: String,
            @SerialName("LineId")
            val lineId: String? = null,
            @SerialName("Type")
            val type: String,
            @SerialName("Weight")
            val weight: Int? = null,
            @SerialName("MonitorStatus")
            val monitorStatus: String? = null,
            @SerialName("Remark")
            val remark: String? = null,
            @SerialName("TTL")
            val ttl: Int,
            @SerialName("MX")
            val mx: Int? = null
        )
    }

    suspend fun list(
        domain: String,
        sub: String? = null,
        type: RecordType? = null,
        line: String? = null,
        groupId: Int? = null,
        keyword: String? = null,
        sortField: String? = null,
        sortType: String? = null,
        offset: Int? = null,
        limit: Int? = null
    ) = qCloud.post<ListResp>(
        "dnspod",
        "DescribeRecordList",
        "",
        "2021-03-23",
        buildJson {
            "Domain" to domain
            "Subdomain" to sub
            "RecordType" to type?.value
            "RecordLine" to line
            "GroupId" to groupId
            "Keyword" to keyword
            "SortField" to sortField
            "SortType" to sortType
            "Offset" to offset
            "Limit" to limit
        }
    )

}